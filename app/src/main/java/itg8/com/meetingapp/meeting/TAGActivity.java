package itg8.com.meetingapp.meeting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.custom_tag.TagContainerLayout;
import itg8.com.meetingapp.custom_tag.TagView;
import itg8.com.meetingapp.db.DaoMeetingTagInteractor;
import itg8.com.meetingapp.db.DaoTagInteractor;
import itg8.com.meetingapp.db.TblMeetingTag;
import itg8.com.meetingapp.db.TblTAG;

public class TAGActivity extends AppCompatActivity implements View.OnClickListener, TAGAddAdapter.onItemClickedListener, SearchView.OnQueryTextListener {


    private static final String TAG = TAGActivity.class.getSimpleName();
    private static final int FROM_SEARCH = 2;
    private static final int FROM_TAG = 1;
    private static final String TAG_LIST = "TAG_LIST";
    @BindView(R.id.input_layout_name)
    TextInputLayout inputLayoutName;

    //    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    List<TblTAG> tagList = new ArrayList<>();
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_lbl)
    TextView txtLbl;
    @BindView(R.id.rl_no_tag)
    RelativeLayout rlNoTag;
    @BindView(R.id.anchor_dropdown)
    View anchorDropdown;
    @BindView(R.id.tag_container_layout)
    TagContainerLayout tagContainerLayout;
    @BindView(R.id.edt_document_title)
    EditText edtDocumentTitle;

    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    private boolean isFromCancel = false;
    private TblTAG tag;
    //    private TAGAddAdapter tagAddAdapter;
    private DaoTagInteractor tagInteractor;
    private MenuItem mSerachItem;
    private Menu menuItem;
    private ActionMode mActionMode;
    private List<TblTAG> tempFilterList;
    private SearchView searchView;
    private MenuItem myActionMenuItem;

    private ActionMode.Callback mActionModeCallback;
    private boolean searchViewClosed = true;
    private ArrayList<String> tagHashMap = new ArrayList<>();
    /**
     * isFromHome is For Home Enable
     */
    private boolean isFromHome = false;
    private boolean hasTagClear = false;
    private boolean fromSearch;
    private DaoMeetingTagInteractor tblTagMeetingInteractor;
    private WindowCallbackDelegate mWindowCallbackDelegate;

    private static Spanned formatPlaceDetails(Resources res, CharSequence title, String sub_title,
                                              CharSequence doc_name) {
        return Html.fromHtml(res.getString(R.string.no_tag, title, sub_title, doc_name));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG_LIST, (ArrayList<? extends Parcelable>) tagList);

    }

    private static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
    }

    private boolean dispachKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
            Log.d(TAG, "dispachKeyEvent: ");

        }
        return false;
    }

    void createActionMenuCallback() {

        mActionModeCallback = new ActionMode.Callback() {

            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = mode.getMenuInflater();
                Log.d(TAG, "onCreateActionMode:" + mode);
                inflater.inflate(R.menu.menu_tag, menu);
                menu.findItem(R.id.m_search).setVisible(true);
                Window window = TAGActivity.this.getWindow();
                mWindowCallbackDelegate = new WindowCallbackDelegate(window.getCallback());
                window.setCallback(TAGActivity.this);

                if (isFromHome) {
                    menu.findItem(R.id.menu_delete).setVisible(true).getActionView();
                    menu.findItem(R.id.menu_ok).setVisible(false).getActionView();


                } else {
                    menu.findItem(R.id.menu_ok).setVisible(true).getActionView();
                    menu.findItem(R.id.menu_delete).setVisible(false).getActionView();
                }

                MenuItem menuDelete = menu.findItem(R.id.menu_delete);

                searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.m_search));
                searchView.setIconifiedByDefault(true);
                searchView.setIconified(true);


                menuDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        showDialoge();
                        return true;
                    }
                });

                searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        Log.d(TAG, "onFocusChange: " + hasFocus);
                        searchViewClosed = !hasFocus;

                    }
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "onQueryTextSubmit: onCreateActionMode" + s);
                        searchViewClosed = false;
                        if (!searchView.isIconified()) {
                            searchView.setIconified(true);
                            Toast.makeText(TAGActivity.this, "SearchOnQueryTextSubmit: " + s, Toast.LENGTH_SHORT).show();

                        }
                        myActionMenuItem.collapseActionView();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(final String s) {
                        Log.d(TAG, "onQueryTextChange: onCreateActionMode" + s);
                        searchViewClosed = false;
                        Toast.makeText(TAGActivity.this, "onQueryTextChange: " + s, Toast.LENGTH_SHORT).show();
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(s))
                                    searchFilterTag(s);
                                else
                                    createRecyclerViewForTAG();
                            }
                        });

                        return false;
                    }
                });

                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        Log.d(TAG, "SearchView onClose: ");
                        searchViewClosed = true;
                        return false;
                    }
                });


                return true;

            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int id = item.getItemId();
                Log.d(TAG, "onActionItemClicked:" + item.getItemId());

                if (id == R.id.menu_ok) {

                    return true;
                }
                if (id == R.id.m_search) {
                    Log.d(TAG, "onActionItemClicked m_search :" + item);
                    //searchQueryChange(item,FROM_SEARCH);

                } else {
                    return false;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Log.d(TAG, "onDestroyActionMode:mode " + mode.getTitle());

//                Window.Callback originalWindowCallback = mWindowCallbackDelegate.mOriginalWindowCallback;
//                if(originalWindowCallback != null)
//                {
//                    TAGActivity.this.getWindow().setCallback(originalWindowCallback);
//                }
                if (!hasTagClear) {
                    if (!searchViewClosed)
                        onCancelButtonClicked();
                    else
                        simonGoBack();
                }
            }
        };

    }

    private void showDialoge() {
        final AlertDialog alertDialog = new AlertDialog.Builder(TAGActivity.this)
                .setTitle("Delete Tags")
                .setMessage("Would you like to delete all tags?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearAllTag();

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        alertDialog.show();
    }

    private void simonGoBack() {
        onBackPressed();
    }

    private void onCancelButtonClicked() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
//                createActionMenuCallback();/**/
//                startSupportActionMode(mActionModeCallback);
//                setTagCount();
//                mActionMode = null;
                /**
                 * Change Now For Toolbar Action Mode.
                 */
//                updateActionBar(FROM_TAG, isFromCancel);
                invalidateOptionsMenu();

//                setTagCount();

                createRecyclerViewForTAG();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        ButterKnife.bind(this);
//        createActionMenuCallback();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        tagAddAdapter = new TAGAddAdapter(this, tagList, this);
        tagInteractor = new DaoTagInteractor(this);
        initTagList();
        getDataFromIntent();
        init();
        checkSaveInstanceSate(savedInstanceState);


    }

    private void checkSaveInstanceSate(Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {
            tagList = savedInstanceState.getParcelableArrayList(TAG_LIST);
//            setSelectedTAG();
            createRecyclerViewForTAG();
            invalidateOptionsMenu();

        }
    }

    private void initTagList() {
        try {
            tagHashMap.clear();
            for (TblTAG tag :
                    tagList) {
                if (tag.isSelected())
                    tagHashMap.add(tag.getName());
            }

            tagList.clear();
            tagList.addAll(tagInteractor.getTags());
            if (isFromHome)
                setSelectedTAG();
            setHashmapToList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This Method is for set All TAG Management .
     */
    private void setSelectedTAG() {
        for (TblTAG tbl :
                tagList) {
            tbl.setSelected(true);

        }
    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra(CommonMethod.EXTRA_TAGS)) {
            tagHashMap = getIntent().getStringArrayListExtra(CommonMethod.EXTRA_TAGS);
            setHashmapToList();
//            for(Map.Entry<Long, TblTAG> entry : tagHashMap.entrySet()) {
//                tagList.add(entry.getValue());
//            }
        } else if (getIntent().hasExtra(CommonMethod.FROM_HOME)) {
            isFromHome = getIntent().getBooleanExtra(CommonMethod.FROM_HOME, false);
            setSelectedTAG();

//            setHashmapToList();

        } else if (getIntent().hasExtra(CommonMethod.FROM_SEARCH)) {
            fromSearch = true;
            tblTagMeetingInteractor = new DaoMeetingTagInteractor(this);
            rlBottom.setVisibility(View.GONE);
            clearListAndAddNamesOnly();
        }
    }

    private void clearListAndAddNamesOnly() {
        tagList.clear();
        try {
            List<TblMeetingTag> tempList = tblTagMeetingInteractor.getDistrictTags();
            for (TblMeetingTag tags :
                    tempList) {
                tagList.add(new TblTAG(tags.getTag(), false));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setHashmapToList() {
        for (String tagString :
                tagHashMap) {
            boolean passtest = false;
            for (TblTAG tag :
                    tagList) {

                if (tagString.equalsIgnoreCase(tag.getName())) {
                    passtest = true;
                    tag.setSelected(true);
                }
            }
            if (!passtest)
                tagList.add(new TblTAG(tagString, true));
        }
    }

    private void init() {
        btnAdd.setOnClickListener(this);
        if (tagList != null && tagList.size() > 0) {
            showHideView(rlTop, rlNoTag);
            createRecyclerViewForTAG();
        } else {
            showHideView(rlNoTag, rlTop);
            txtLbl.setText(formatPlaceDetails(getResources(), "Yet No", "TAG", "Available"));

        }


    }

    private void clearTagList() {
        tagList.clear();
    }

    private void showHideView(View show, View hide) {
        show.setVisibility(View.VISIBLE);
        hide.setVisibility(View.GONE);
    }

    private void updateTAGItem() {
        tag = new TblTAG();
        tag.setName(edtDocumentTitle.getText().toString().trim());
        try {

            tagInteractor.insert(tag);
//            tagAddAdapter.notifyDataSetChanged();
            edtDocumentTitle.setText("");
            showHideView(rlTop, rlNoTag);
            setHashmapToList();
            updateTagList();
        } catch (SQLException e) {
            e.printStackTrace();
        }


//        tagList.add(tag);
//        tagAddAdapter.notifyDataSetChanged();
//        edtDocumentTitle.setText("");


    }

    private void updateTagList() {
        try {
//            tagHashMap.clear();
            for (TblTAG tag : tagList) {
                if (tag.isSelected())
                    tagHashMap.add(tag.getName());
            }


            tagList.clear();
            tagList.addAll(tagInteractor.getTags());
            if (isFromHome)
                setSelectedTAG();
            setHashmapToList();
            if (tagList != null && tagList.size() > 0) {
                showHideView(tagContainerLayout, rlNoTag);
                createRecyclerViewForTAG();
            }
//            tagAddAdapter.notifyDataSetChanged();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createRecyclerViewForTAG() {
        if (tagList.size() > 0) {
            int size = tagList.size();

            List<int[]> colors = new ArrayList<int[]>();
            for (int i = 0; i < size; i++) {


                int[] col1 = {Color.parseColor("#631109"), Color.parseColor("#ffffff"), Color.parseColor("#ffffff")};
                colors.add(col1);
            }

            if (tagContainerLayout.isEnableCross()) {

                /**
                 * Change It NOW
                 */
//                updateActionBar(FROM_TAG, false);

//                setTagCount();
            }

            if (isFromHome) {
                tagContainerLayout.setEnableCross(true);
            } else {
                tagContainerLayout.setEnableCross(false);
            }
            tagContainerLayout.setTags(tagList, colors, isFromHome);
            tagOnClickListener(FROM_TAG);
        }


    }

    private void tagOnClickListener(final int from) {
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, Object object) {
                try {
                    if (isFromHome)
                        return;

                    TblTAG text = (TblTAG) object;
                    text.setSelected(!text.isSelected());


                    /**
                     * Change It Nowe
                     */
//                    tagInteractor.update(text);
                    tagContainerLayout.changeSelectColor(position, text);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                /**
                 * Change It Now
                 */
//                updateActionBar(FROM_TAG, false);

                invalidateOptionsMenu();



//                setTagCount();
            }

            @Override
            public void onTagLongClick(int position, Object text) {
                if (isFromHome) {
                    tagContainerLayout.isInEditMode();
                }

            }

            @Override
            public void onTagCrossClick(int position, Object object) {
                TblTAG test = (TblTAG) object;
                if (test.isSelected()) {
//                    tagContainerLayout.removeTag(position);
//                    tagList.remove(position);
                    if (!isFromHome) {
                        test.setSelected(false);

                        tagContainerLayout.changeSelectColor(position, test);



                    } else {
                        try {
                            removeTagFromHome(position, test);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
//
                invalidateOptionsMenu();

            }
        });
    }

    private void removeTagFromHome(int position, TblTAG test) throws SQLException {
        tagContainerLayout.removeTag(position);
        tagList.remove(position);
        test.setSelected(false);
        tagInteractor.delete(test);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.m_search));


        searchView.setOnQueryTextListener(this);


        int checkBoxCount = 0;

        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).isSelected()) checkBoxCount++;
            Log.d(TAG, "updateActionBars: checkBoxCount" + checkBoxCount);
        }
        if (checkBoxCount == 0) {
            menu.findItem(R.id.menu_ok).setVisible(false);
            getSupportActionBar().setTitle(getString(R.string.tags));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));

        } else {
            menu.findItem(R.id.menu_ok).setVisible(true);
            if (checkBoxCount == 1) {
                getSupportActionBar().setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.tag));
            } else {
                getSupportActionBar().setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.tags));
            }
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorBlack)));


        }

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchViewClosed = true;
                initTagList();
              init();


                return false;
            }
        });


        return true;
    }


    private void searchFilterTag(String query) {
        tempFilterList = new ArrayList<>();
        if (!TextUtils.isEmpty(query)) {
            if (tagList != null && tagList.size() > 0) {
                for (TblTAG tagFilter : tagList) {
                    if (tagFilter.getName().toLowerCase().contains(query.toLowerCase())) {
                        tempFilterList.add(tagFilter);
                    }
                }
                setUpFilterTag(tempFilterList);

            }
        } else {
            createRecyclerViewForTAG();
        }
    }
//

    private void setUpFilterTag(List<TblTAG> tempFilterList) {
        int size = tempFilterList.size();
        List<int[]> colors = new ArrayList<int[]>();
        for (int i = 0; i < size; i++) {
            int[] col1 = {Color.parseColor("#631109"), Color.parseColor("#ffffff"), Color.parseColor("#ffffff")};
            colors.add(col1);
        }

        tagContainerLayout.setTags(tempFilterList, colors, false);

        tagOnClickListener(FROM_SEARCH);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected:" + item.getItemId());

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.menu_ok) {
            finishWithResult();
            finish();
            return true;
        }


        return false;
    }

    private void updateActionBar(int from, boolean isFromCancel) {
        int checkBoxCount = 0;
        if (from == FROM_TAG) {
            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i).isSelected())
                    checkBoxCount++;

            }
        }
        setTagCount(checkBoxCount);



//        } else {
//            checkBoxCount = 0;
//            for (int i = 0; i < tempFilterList.size(); i++) {
//                if (tempFilterList.get(i).isSelected())
//                    checkBoxCount++;
//
//            }

        }


//        if (checkBoxCount == 0) {
//            if (mActionMode != null) {
//                mActionMode.finish();
//                mActionMode = null;
//            }
//
//        } else {
//            if (mActionMode == null)
//                mActionMode = startSupportActionMode(mActionModeCallback);
//        }
//        if (mActionMode != null) {
//            setTagCount();
//        }



    private void setTagCount(int checkBoxCount) {
        if (checkBoxCount == 1) {
            getSupportActionBar().setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.tag));
        } else {
            getSupportActionBar().setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.tags));
        }
    }

    public void finishWithResult() {
//            DaoContactInteractor contactInteractor = new DaoContactInteractor(activity) ;
        ArrayList<TblTAG> selectedContacts = new ArrayList<TblTAG>();
        for (TblTAG c : tagList) {
            if (c.isSelected()) {
                selectedContacts.add(c);
////                    tagTemp.add(contacts.get(position));
//                    contactInteractor.insert(c);

            }
        }

        Bundle resultData = new Bundle();

        resultData.putParcelableArrayList("tag", selectedContacts);
        Intent intent = new Intent();
        intent.putExtras(resultData);
        setResult(RESULT_OK, intent);
        this.finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                if (TextUtils.isEmpty(edtDocumentTitle.getText().toString()))
                    return;
                closeSearchViewAddBtn();
                invalidateOptionsMenu();
                updateTAGItem();
                break;
        }
    }

    private void closeSearchViewAddBtn() {
        if(searchView!= null)
        {
            if(!searchView.isIconified())
            {
                searchView.setIconified(true);
            }
        }
    }

    @Override
    public void onItemDelete(int position, TblTAG tag) {
        if (!tag.isSelected()) {
            try {
                tagInteractor.delete(tag);
                tagList.remove(tag);
//
//                tagAddAdapter.notifyItemRemoved(position);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onItemClicked(int position, TblTAG tag) {

        tag.setSelected(!tag.isSelected());
        if (tag.isSelected()) {
            try {
                tagHashMap.add(tag.getName());
//                tagInteractor.update(tag);
//                tagAddAdapter.notifyDataSetChanged();
//                Log.d(TAG, new Gson().toJson(tag));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit: query=" + query);
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
//            Toast.makeText(TAGActivity.this, "SearchOnQueryTextSubmit: " + query, Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        Log.d(TAG, "onQueryTextChange: newText=" + newText);

//        Toast.makeText(TAGActivity.this, "onQueryTextChange: " + newText, Toast.LENGTH_SHORT).show();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                searchFilterTag(newText);
            }
        });

        return false;
    }

    private void openDialogueBox() {

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(TAGActivity.this);
//

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(TAGActivity.this);


        builderSingle.setTitle("Do You want exit ");
        View mView = layoutInflaterAndroid.inflate(android.R.layout.select_dialog_item, null);

        builderSingle.setCancelable(true);

        builderSingle.setNegativeButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        builderSingle.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builderSingle.setView(mView);


        final AlertDialog dialog = builderSingle.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult();
                onBackPressed();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelButtonClicked();
                dialog.dismiss();

            }
        });


        builderSingle.setNeutralButton("Clear",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        clearAllTag();
                        //dialog.cancel();
                    }
                });


    }

    private void clearAllTag() {
        try {
            tagInteractor.deleteAll(tagList);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        tagList.clear();
        init();


        createRecyclerViewForTAG();

        hasTagClear = true;
//        updateActionBar(FROM_TAG, false);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFromHome = false;
    }

    public class WindowCallbackDelegate implements Window.Callback {
        private Window.Callback mOriginalWindowCallback;

        public WindowCallbackDelegate(Window.Callback mOriginalWindowCallback) {
            this.mOriginalWindowCallback = mOriginalWindowCallback;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return TAGActivity.this.dispachKeyEvent(keyEvent) || mOriginalWindowCallback.dispatchKeyEvent(keyEvent);
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return false;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            return false;
        }

        @Nullable
        @Override
        public View onCreatePanelView(int i) {
            return null;
        }

        @Override
        public boolean onCreatePanelMenu(int i, Menu menu) {
            return false;
        }

        @Override
        public boolean onPreparePanel(int i, View view, Menu menu) {
            return false;
        }

        @Override
        public boolean onMenuOpened(int i, Menu menu) {
            return false;
        }

        @Override
        public boolean onMenuItemSelected(int i, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {

        }

        @Override
        public void onContentChanged() {

        }

        @Override
        public void onWindowFocusChanged(boolean b) {

        }

        @Override
        public void onAttachedToWindow() {

        }

        @Override
        public void onDetachedFromWindow() {

        }

        @Override
        public void onPanelClosed(int i, Menu menu) {

        }

        @Override
        public boolean onSearchRequested() {
            return false;
        }

        @Override
        public boolean onSearchRequested(SearchEvent searchEvent) {
            return false;
        }

        @Nullable
        @Override
        public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
            return null;
        }

        @Nullable
        @Override
        public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback, int i) {
            return null;
        }

        @Override
        public void onActionModeStarted(android.view.ActionMode actionMode) {

        }

        @Override
        public void onActionModeFinished(android.view.ActionMode actionMode) {

        }
    }


}
