package itg8.com.meetingapp.meeting;

import android.Manifest;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.meeting.fragment.CustomPrioritesFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MeetingActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {


    private static final boolean SHOW_DUE = true;
    private static final boolean HIDE_DUE = false;
    private static final boolean HIDE_REMINDER = false;
    private static final boolean SHOW_REMINDER = true;
    private static final boolean HIDE_PLACE = false;
    private static final boolean SHOW_PLACE = true;
    private static final boolean SHOW_DOCUMENT = true;
    private static final boolean HIDE_DOCUMENT = false;
    private static final int RC_LOCATION = 234;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 9001;
    private static final String TAG = MeetingActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.lbl_due_date)
    TextView lblDueDate;
    @BindView(R.id.lbl_lab)
    TextView lblLab;
    @BindView(R.id.img_due_cross)
    ImageView imgDueCross;
    @BindView(R.id.img_time)
    ImageView imgTime;
    @BindView(R.id.edt_date)
    EditText edtDate;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.img_end_time)
    ImageView imgEndTime;
    @BindView(R.id.edt_end_date)
    EditText edtEndDate;
    @BindView(R.id.end_time)
    EditText endTime;
    @BindView(R.id.rl_due)
    RelativeLayout rlDue;
    @BindView(R.id.ll_due)
    LinearLayout llDue;
    @BindView(R.id.lbl_reminder)
    TextView lblReminder;
    @BindView(R.id.lbl_reminder_lbl)
    TextView lblReminderLbl;
    @BindView(R.id.img_reminder_cross)
    ImageView imgReminderCross;
    @BindView(R.id.img_times)
    ImageView imgTimes;
    @BindView(R.id.txt_pri)
    TextView txtPri;
    @BindView(R.id.rl_reminder)
    RelativeLayout rlReminder;
    @BindView(R.id.ll_reminder)
    LinearLayout llReminder;
    @BindView(R.id.lbl_place)
    TextView lblPlace;
    @BindView(R.id.lbl_add_place)
    TextView lblAddPlace;
    @BindView(R.id.img_place_cross)
    ImageView imgPlaceCross;
    @BindView(R.id.lbl_add_name)
    TextView lblAddName;
    @BindView(R.id.ll_place)
    LinearLayout llPlace;
    @BindView(R.id.lbl_image)
    TextView lblImage;
    @BindView(R.id.lbl_add_image)
    TextView lblAddImage;
    @BindView(R.id.img_doc)
    ImageView imgDoc;
    @BindView(R.id.txt_docName)
    TextView txtDocName;
    @BindView(R.id.ll_doc)
    LinearLayout llDoc;
    @BindView(R.id.lbl_participeint)
    TextView lblParticipeint;
    @BindView(R.id.img_phone_book)
    ImageView imgPhoneBook;
    @BindView(R.id.lbl_add_person)
    TextView lblAddPerson;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_add_person)
    LinearLayout llAddPerson;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    private String[] permissions;
    private boolean canAccessCamera;
    private boolean canAccessLocation;
    private boolean canStorage;
    private boolean canPhoneState;
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         mClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        init();


    }

    @Override
    protected void onStart() {
        mClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }


    private void init() {
        setOnClickedListener();

    }

    private void setOnClickedListener() {
        llDue.setOnClickListener(this);
        llReminder.setOnClickListener(this);
        llPlace.setOnClickListener(this);
        llDoc.setOnClickListener(this);
        imgDueCross.setOnClickListener(this);
        imgReminderCross.setOnClickListener(this);
        llAddPerson.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_reminder:
                toggleReminderView(SHOW_REMINDER);
                break;
            case R.id.ll_due:
                toggleDueView(SHOW_DUE);
                break;
            case R.id.ll_place:
                togglePlaceView(SHOW_PLACE);
                break;
            case R.id.ll_doc:
                toggleDocumentView(SHOW_DOCUMENT);
                break;
            case R.id.img_due_cross:
                toggleDueView(HIDE_DUE);
                break;
            case R.id.img_reminder_cross:
                toggleReminderView(HIDE_REMINDER);
                break;
            case R.id.edt_date:
                openDateCalender();
                break;
            case R.id.edt_end_date:
                OpenTimeCalender();
                break;
            case R.id.end_time:
                OpenTimeCalender();
                break;


        }
    }

    private void OpenTimeCalender() {


    }

    private void openDateCalender() {

    }

    private void toggleDocumentView(boolean showDocument) {
        if (showDocument) {
            hideShowView(lblAddPlace, rlAddress);


        } else {
            hideShowView(lblAddPlace, rlAddress);

        }
    }

    private void togglePlaceView(boolean showPlace) {
//        startActivity(new Intent(getApplicationContext(), NearByLocationActivity.class));
        OpenGooglePlaces();
        if (showPlace) {
            hideShowView(lblAddPlace, rlAddress);



        } else {
            hideShowView(rlAddress, lblAddPlace);

        }
    }

    private void OpenGooglePlaces() {

        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
//            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                    .build(this);
            startActivityForResult(builder.build(this), REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

//            Log.e(TAG, message);
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                lblAddPlace.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    lblAddPlace.setText(Html.fromHtml(attributions.toString()));
                } else {
                    lblAddPlace.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }
    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }


    private void toggleReminderView(boolean showReminder) {
        if (showReminder) {
            OpenCustomDialogueFragment();
            hideShowView(lblReminderLbl, rlReminder);

        } else {
            hideShowView(rlReminder, lblReminderLbl);

        }
    }

    private void OpenCustomDialogueFragment() {

        showDialogBox();
//
//
//
//
//        CustomPrioritesFragment dialog = new CustomPrioritesFragment();
//        dialog.show(getSupportFragmentManager(), "YourDialog");

    }

    private void toggleDueView(boolean showDue) {
        if (showDue) {
            hideShowView(lblLab, rlDue);

        } else {
            hideShowView(rlDue, lblLab);

        }

    }
    private void showDialogBox() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MeetingActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select Priority:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MeetingActivity.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("High");
        arrayAdapter.add("Medium");
        arrayAdapter.add("Low");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                txtPri.setText(strName);


            }
        });
        builderSingle.show();
    }


    private void hideShowView(View hide, View show) {
        hide.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void checkStoragePerm() {
        permissions =new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, permissions[0])
                && (EasyPermissions.hasPermissions(getApplicationContext(), permissions[1]))) {
            canAccessCamera = true;

        }
        if((EasyPermissions.hasPermissions(getApplicationContext(), permissions[2]))){

            canAccessLocation = true;
        }
        if((EasyPermissions.hasPermissions(getApplicationContext(), permissions[3])))
        {canStorage = true;

        }
        if((EasyPermissions.hasPermissions(getApplicationContext(), permissions[4])))
        {
            canPhoneState = true;
        }

        if(!canPhoneState || !canStorage || !canAccessCamera || !canAccessLocation)
        {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_no_permission), RC_LOCATION, permissions);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);



    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        checkPrems(perms, true);
    }

    private void checkPrems(List<String> perms, boolean isGranted) {


        if (perms.contains(permissions[0])
                && perms.contains(permissions[1])) {
            canAccessCamera = isGranted;

        }
        if (perms.contains(permissions[2])) {

            canAccessLocation = isGranted;
        }
        if (perms.contains(permissions[3])) {
            canStorage = isGranted;

        }
        if (perms.contains(permissions[4])) {
            canPhoneState = isGranted;
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        checkPrems(perms,false);
    }

}
