package itg8.com.meetingapp.meeting;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.meeting.model.Contact;

import static android.database.DatabaseUtils.dumpCursorToString;

public class MultipleContactPickerActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener, ContactsAdapter.OnSearchInteractonListener {

    private static final String TAG = "MultipleContactPickerAc";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_ID,
                    ContactsContract.CommonDataKinds.Email.DATA,
                    ContactsContract.Contacts._ID
            };
    private static final String FILTER = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
    private static final String SORT_ORDER = "CASE WHEN "
            + ContactsContract.Contacts.DISPLAY_NAME
            + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
            + ContactsContract.Contacts.DISPLAY_NAME
            + ", "
            + ContactsContract.CommonDataKinds.Email.DATA
            + " COLLATE NOCASE";
    private static final String CONTACT_LIST = "CONTACT_LIST";
    private static final String RECYCLER_VIEW = "RECYCLER_VIEW";
    private static final String POSITION_RECYCLERVIEW = "";
    public
    ArrayList<Contact> contacts = new ArrayList<>();
    @BindView(R.id.contacts_recycler_view)
    RecyclerView contactsRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private RecyclerView contactsView;
    private ContactsAdapter contactsAdapter;
    private RecyclerView.LayoutManager contactsLayoutManager;
    private boolean isDestroyed = false;
    private MenuItem myActionMenuItem;
    private SearchView searchView;
    private ArrayList<Contact> tempFilterList;
    private ArrayList<Contact> originalContact = new ArrayList<>();
    private HashMap<Long, Contact> hashMap;
    private ActionMode mActionMode;
    private boolean isSearchClose = false;
    private ActionMode.Callback mActionModeCallback;
    private boolean needUpdate = false;
    private boolean hasInstanceState=false;
    private MenuItem menuOK;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: :"+contacts.size());
        outState.putParcelableArrayList(CONTACT_LIST, originalContact);
    }
//        outState.putInt(POSITION_RECYCLERVIEW);





    private boolean setOriginalContactList() {
        if (contacts != null && originalContact != null && contactsAdapter != null) {
            contacts.clear();
            contacts.addAll(originalContact);
            contactsAdapter.notifyDataSetChanged();

            return true;
        }
        return false;
    }

    private Bitmap getPhotoById(int id) {

        Bitmap bitmapPhoto = null;
        byte[] photo = null;

        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, id);
        Cursor cursorPhoto = getContentResolver().query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
        try {
            if (cursorPhoto.moveToFirst())
                photo = cursorPhoto.getBlob(0);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        } finally {

            cursorPhoto.close();
        }

        if (photo != null) {
            bitmapPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
//                    Log.d("!!!!!!!!!!!!!!!PICTURE!!!!!!!!!!", "!!!!!!!!!!!!!!!!");
 }
        return bitmapPhoto;
    }



    public void updateActionBars() {
        int checkBoxCount = 0;

        for (int i = 0; i < originalContact.size(); i++) {
            if (originalContact.get(i).isSelected()) checkBoxCount++;
            Log.d(TAG, "updateActionBars: checkBoxCount" + checkBoxCount);
        }

        if (checkBoxCount == 0) {
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }

        } else {
            if (mActionMode == null)
                mActionMode = startSupportActionMode(mActionModeCallback);
        }
        if (mActionMode != null) {
            if (checkBoxCount == 1) {
                getSupportActionBar().setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.friend));
            } else {
                getSupportActionBar().setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.friends));
            }
        }

    }

    private ArrayList<Contact> contactsFromCursor(Cursor cursor) {
        ArrayList<Contact> c = new ArrayList<Contact>();

        Log.d(TAG, "contactsFromCursor: " + dumpCursorToString(cursor));

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();


            do {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                long pkid = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));

                if (email != null) {
                    c.add(new Contact(name, email, getPhotoById(id), number, pkid));
//                    Log.d("Name: ", name);
//                    Log.d("number: ", number);

                }

            } while (cursor.moveToNext());

        }

        return c;
    }

    private void finishWithResult() {
//            DaoContactInteractor contactInteractor = new DaoContactInteractor(activity) ;
        ArrayList<Contact> selectedContacts = new ArrayList<Contact>();
        for (Contact c : contacts) {
            if (c.isSelected()) {
                selectedContacts.add(c);
//                    contactInteractor.insert(c);

            }
        }

        Bundle resultData = new Bundle();
        resultData.putParcelableArrayList("contacts", selectedContacts);
        Intent intent = new Intent();
        intent.putExtras(resultData);
        setResult(RESULT_OK, intent);
        finish();
    }
    private void checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            getLoaderManager().initLoader(0, null, this);

        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_multiple_contact_picker);
        ButterKnife.bind(this);
        checkPermissions();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_ab_close);
        getSupportActionBar().setTitle(getString(R.string.add_friends));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactsView = (RecyclerView) findViewById(R.id.contacts_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        createRecyclerView();
        checkSaveInstance(savedInstanceState);


    }

    private void checkSaveInstance(Bundle savedInstanceState) {
        Log.d(TAG, "checkSaveInstance: ");
        if(savedInstanceState!=null && savedInstanceState.getParcelableArrayList(CONTACT_LIST) != null ) {
            contacts= (savedInstanceState.getParcelableArrayList(CONTACT_LIST));
            if(contacts!=null && contacts.size()>0) {
                hasInstanceState = true;
                originalContact.addAll(contacts);
                createTempHashMap();
//                contactsAdapter.notifyDataSetChanged();
                createRecyclerView();
            }
        }



    }

    private void  createRecyclerView() {
        contactsView.setHasFixedSize(true);
        // use a linear layout manager
        contactsLayoutManager = new LinearLayoutManager(this);
        contactsView.setLayoutManager(contactsLayoutManager);
        // specify an adapter (see also next example)
        contactsAdapter = new ContactsAdapter(this, contacts, this);
        contactsView.setAdapter(contactsAdapter);
    }

    @Override
    public void updateActionBar() {
//        updateActionBars();
        setCountToTitle();
//        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: HOME");
                finish();
                return true;
            case  R.id.menu_ok:
                finishWithResult();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Initializes the loader

                    getLoaderManager().initLoader(0, null, this);

                } else {

                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
               /*
         * Makes search string into pattern and
         * stores it in the selection array
         */
        //  mSelectionArgs[0] = "%" + mSearchString + "%";
        // Starts the query
        Log.d(TAG, "onCreateLoader: ");
        return new CursorLoader(
                this,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                FILTER,
                null,
//                SORT_ORDER
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {

        if(hasInstanceState)
            return;
        Log.d(TAG, "onLoadFinished: ");


        showProgress();
        Observable.fromCallable(new Callable<ArrayList<Contact>>() {
            @Override
            public ArrayList<Contact> call() throws Exception {
                return contactsFromCursor(cursor);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Contact>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Contact> contactList) {
                        contacts.addAll(contactList);
                            originalContact.addAll(contactList);
                            createTempHashMap();
                            contactsAdapter.notifyDataSetChanged();
                   
                            
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        if (!isDestroyed)
                            hideProgress();
                    }
                });
    }

    private void createTempHashMap() {
        hashMap = new HashMap<>();
        for (Contact contact : originalContact
                ) {
            hashMap.put(contact.getContactId(), contact);

        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);

    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        isSearchClose = false;
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.context_menu, menu);
        myActionMenuItem = menu.findItem(R.id.m_search).setVisible(true);
        menuOK = menu.findItem(R.id.menu_ok);
        setCountToTitle();
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.m_search));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (setOriginalContactList()) return false;
                return false;
            }
        });


        searchQueryChange(myActionMenuItem);
        return super.onCreateOptionsMenu(menu);
    }

    private void setCountToTitle() {
        int checkBoxCount = 0;

        for (int i = 0; i < originalContact.size(); i++) {
            if (originalContact.get(i).isSelected()) checkBoxCount++;
        }
        Log.d(TAG, "updateActionBars: checkBoxCount" + checkBoxCount);
        if (checkBoxCount == 0) {

           menuOK.setVisible(false);
            getSupportActionBar().setTitle(getString(R.string.add_friends));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));

        } else {
           menuOK.setVisible(true);

            if (checkBoxCount == 1) {
                getSupportActionBar().setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.friend));
            } else {
                getSupportActionBar().setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.friends));
            }
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorBlack)));

        }
    }

    private void searchQueryChange(final MenuItem myActionMenuItem) {
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);

    }
//

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            Toast.makeText(MultipleContactPickerActivity.this, "SearchOnQueryTextSubmit: " + query, Toast.LENGTH_SHORT).show();

        }
        myActionMenuItem.collapseActionView();
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                searchFilterTag(newText);
            }
        });
        return false;
    }


    private void searchFilterTag(String query) {
        tempFilterList = new ArrayList<>();
        tempFilterList.clear();

        if (!TextUtils.isEmpty(query)) {
            if (originalContact != null && originalContact.size() > 0) {
                for (Contact tagFilter : originalContact) {
                    if (tagFilter.getName().toLowerCase().contains(query.toLowerCase())) {
                        tempFilterList.add(tagFilter);
                    }
                }
                contacts.clear();
                contacts.addAll(tempFilterList);
                contactsAdapter.notifyDataSetChanged();


            }
        } else {
            createRecyclerView();

        }
    }


    @Override
    public void
    onQueryTextListener(String query) {
        searchFilterTag(query);
    }



    @Override
    public void changeSelected(long id) {
//        Contact tempContact = hashMap.get(id);
//        tempContact = originalContact.get(originalContact.indexOf(tempContact));
//        if (tempContact != null)
//            tempContact.setSelected(!tempContact.isSelected());
//        else
//            throw new NullPointerException("OHHHHHHHHHHHHHHHHHHHHHHHHHH AISAAAAAAAAAAAA");
    }
}