package itg8.com.meetingapp.meeting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.custom_tag.TagContainerLayout;
import itg8.com.meetingapp.custom_tag.TagView;
import itg8.com.meetingapp.db.DaoTagInteractor;
import itg8.com.meetingapp.db.TblTAG;

public class TAGActivity extends AppCompatActivity implements View.OnClickListener, TAGAddAdapter.onItemClickedListener, SearchView.OnQueryTextListener {


    private static final String TAG = TAGActivity.class.getSimpleName();
    private static final int FROM_SEARCH = 2;
    private static final int FROM_TAG = 1;
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
    private int checkBoxCount;
    private Menu menuItem;
    private ActionMode mActionMode;
    private List<TblTAG> tempFilterList;
    private SearchView searchView;
    private MenuItem myActionMenuItem;

    private ActionMode.Callback mActionModeCallback;
    private boolean searchViewClosed = true;
    private HashMap<Long, TblTAG> tagHashMap= new HashMap<>();
    private boolean isFromHome;
    private boolean hasTagClear=false;

    private static Spanned formatPlaceDetails(Resources res, CharSequence title, String sub_title,
                                              CharSequence doc_name) {
        return Html.fromHtml(res.getString(R.string.no_tag, title, sub_title, doc_name));

    }

    private static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
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
                    finishWithResult();
                    finish();
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
                if(!hasTagClear) {
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
                createActionMenuCallback();
//                startSupportActionMode(mActionModeCallback);
//                setTagCount();
                mActionMode = null;
                updateActionBar(FROM_TAG, isFromCancel);
                createRecyclerViewForTAG();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        ButterKnife.bind(this);
        createActionMenuCallback();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        tagAddAdapter = new TAGAddAdapter(this, tagList, this);
        tagInteractor = new DaoTagInteractor(this);
        initTagList();
        getDataFromIntent();
        init();


    }

    private void initTagList() {
        try {
            tagList = tagInteractor.getTags();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSelectedTAG() {
        for (TblTAG tbl :
                tagList) {
            tbl.setSelected(true);

        }
    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra(CommonMethod.EXTRA_TAGS)) {
            tagHashMap= (HashMap<Long, TblTAG>) getIntent().getSerializableExtra(CommonMethod.EXTRA_TAGS);
            setHashmapToList();
//            for(Map.Entry<Long, TblTAG> entry : tagHashMap.entrySet()) {
//                tagList.add(entry.getValue());
//            }
        } else if (getIntent().hasExtra(CommonMethod.FROM_HOME)) {
            isFromHome = getIntent().getBooleanExtra(CommonMethod.FROM_HOME, false);
            setSelectedTAG();

//            setHashmapToList();

        }
    }

    private void setHashmapToList() {
        for (TblTAG tag :
                tagList) {
            if (tagHashMap.containsKey(tag.getPkid())) {
                tag.setSelected(true);
            }
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
            for (TblTAG tag :
                    tagList) {
                if (tag.isSelected())
                    tagHashMap.put(tag.getPkid(), tag);
            }

            tagList.clear();
            tagList.addAll(tagInteractor.getTags());
            if(isFromHome)
                setSelectedTAG();
//            setHashmapToList();
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

                updateActionBar(FROM_TAG, false);
            }

            if (isFromHome) {
                tagContainerLayout.setEnableCross(true);
            } else {
                tagContainerLayout.setEnableCross(false);
            }
            tagContainerLayout.setTags(tagList, colors);
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
//                    tagInteractor.update(text);
                    tagContainerLayout.changeSelectColor(position, text);



                } catch (Exception e) {
                    e.printStackTrace();
                }

                updateActionBar(FROM_TAG, false);
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
                    if(!isFromHome) {
                        test.setSelected(false);
                        tagContainerLayout.changeSelectColor(position, test);
                    }else
                    {
                        try {
                            removeTagFromHome(position, test);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
                updateActionBar(from, false);

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
        menu.findItem(R.id.menu_ok).setVisible(false);
        myActionMenuItem = menu.findItem(R.id.m_search).setVisible(true);
        searchQueryChange(myActionMenuItem, FROM_TAG);

        return true;
    }

    private void searchQueryChange(final MenuItem myActionMenuItem, int from) {
        searchView = (SearchView) myActionMenuItem.getActionView();
        Log.d(TAG, "searchQueryChange: SearchView=" + searchView);
        if (from == FROM_TAG)
            searchView.setOnQueryTextListener(this);

    }
//

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

    private void setUpFilterTag(List<TblTAG> tempFilterList) {
        int size = tempFilterList.size();
        List<int[]> colors = new ArrayList<int[]>();
        for (int i = 0; i < size; i++) {
            int[] col1 = {Color.parseColor("#631109"), Color.parseColor("#ffffff"), Color.parseColor("#ffffff")};
            colors.add(col1);
        }

        tagContainerLayout.setTags(tempFilterList, colors);

        tagOnClickListener(FROM_SEARCH);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected:" + item.getItemId());

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }


        return false;
    }

    private void updateActionBar(int from, boolean isFromCancel) {

        if (from == FROM_TAG) {
            checkBoxCount = 0;
            boolean hasValaues = false;
            for (int i = 0; i < tagList.size(); i++) {
                hasValaues=true;
                if (tagList.get(i).isSelected())
                    checkBoxCount++;

            }
            if(!hasValaues)
            {

            }



        } else {
            checkBoxCount = 0;
            for (int i = 0; i < tempFilterList.size(); i++) {
                if (tempFilterList.get(i).isSelected())
                    checkBoxCount++;

            }

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
            setTagCount();
        }

    }

    private void setTagCount() {
        if (checkBoxCount == 1) {
            mActionMode.setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.tag));
        } else {
            mActionMode.setTitle(getString(R.string.add) + " " + String.valueOf(checkBoxCount) + " " + getString(R.string.tags));
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
                 if(TextUtils.isEmpty(edtDocumentTitle.getText().toString()))
                     return;
                updateTAGItem();
                break;
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
                tagHashMap.put(tag.getPkid(), tag);
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
            Toast.makeText(TAGActivity.this, "SearchOnQueryTextSubmit: " + query, Toast.LENGTH_SHORT).show();

        }
        myActionMenuItem.collapseActionView();
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        Log.d(TAG, "onQueryTextChange: newText=" + newText);

        Toast.makeText(TAGActivity.this, "onQueryTextChange: " + newText, Toast.LENGTH_SHORT).show();
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
        updateActionBar(FROM_TAG, false);

    }


}
