package itg8.com.meetingapp.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.db.TblTAG;
import itg8.com.meetingapp.import_meeting.MeetingDetailActivity;
import itg8.com.meetingapp.meeting.TAGActivity;
import itg8.com.meetingapp.widget.animation.ResizeAnimation;
import itg8.com.meetingapp.widget.search.SearchBox;
import itg8.com.meetingapp.widget.search.SearchResult;

public class SearchActivity extends AppCompatActivity implements SearchResultAdapter.MeetingItemClicked, View.OnClickListener, TagContainerLayout.OnHeightAvailbleListner {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final int RC_FILTER_TAG = 987;
    private static final String TAG_LIST = "TAG_LIST";
    @BindView(R.id.searchbox)
    SearchBox searchbox;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.txt_selected_tag)
    TextView txtSelectedTag;
    @BindView(R.id.txt_clear_tag)
    TextView txtClearTag;
    @BindView(R.id.tag_container_layout)
    TagContainerLayout tagContainerLayout;
    @BindView(R.id.img_up)
    ImageView imgUp;
    @BindView(R.id.rl_collapsing)
    RelativeLayout rlCollapsing;
    @BindView(R.id.rl_tag)
    RelativeLayout rlTag;
    HashMap<Long, TblMeeting> hashMap = new HashMap<>();
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    private DaoMeetingInteractor daoMeetingIntractor;
    private List<TblMeeting> listSearchResult = new ArrayList<>();
    private SearchResultAdapter adapter;
    private boolean isCollapsed = false;
    private boolean hasFromActivityResult = false;
    private float singLineTagContainerHeight;
    private float oldHeight;
    private ArrayList<TblTAG> tagList;
    private List<TblMeeting> list;
    //    private List<String> list;
    private List<TblMeeting> searchList;
    private int fullHeight = 0;
    private boolean notFirstTime = false;
    private String Search_LIST = "Search_LIST";
    private int heightScreen = 0;
    private int keyBoardHeight = 0;
    private boolean isFirstTime = false;
    private boolean isSearchFirst = false;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recyclerView != null) {
            Log.d(TAG, "onSaveInstanceState: ");
            outState.putParcelableArrayList(Search_LIST, (ArrayList<? extends Parcelable>) list);
            //    outState.putParcelableArrayList(FILTER_LIST, (ArrayList<? extends Parcelable>) tourismList);
        }
        if (tagContainerLayout != null) {
            Log.d(TAG, "onSaveInstanceState: ");
            outState.putParcelableArrayList(TAG_LIST, tagList);

        }
        rootLayoutObserver();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        tagContainerLayout.setOnHeightAvailableListner(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
        heightScreen = CommonMethod.getScreenResolution(SearchActivity.this);
        Log.d(TAG, "onCreate: height:" + heightScreen);
        if (savedInstanceState != null) {
            tagList = savedInstanceState.getParcelableArrayList(TAG_LIST);
            Log.d(TAG, "onCreate: OnSAVE INSTANCE STATE");
            list = savedInstanceState.getParcelableArrayList(Search_LIST);
            try {
                setTagViewsAndMeeting(tagList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    private void init() {
        setRecyclerView();
        rootLayoutObserver();

        setTagTextWithImage();
        recyclerView.setVisibility(View.VISIBLE);
        hideItems(rlCollapsing, tagContainerLayout, txtClearTag);
        getWindowHeightWidth();
        setOnClickListener();

        searchbox = (SearchBox) findViewById(R.id.searchbox);
        List<TblMeeting> listMeeting = getMeetingFromDatabase();
        if (listMeeting != null) {
            for (TblMeeting meeting : listMeeting) {
                Log.d(TAG, "onCreate: ListMeeting " + meeting.toString());
                Log.d(TAG, "onCreate: ListMeeting " + meeting.toString());

                SearchResult option = new SearchResult(meeting.getTitle(), getResources().getDrawable(R.drawable.ic_history));
                searchbox.addSearchable(option);
            }
        }


        searchbox.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
                Log.d(TAG, "onSearchOpened: ");
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
                Log.d(TAG, "onSearchClosed: ");
                removeSearchItemFromAdapter();
            }

            @Override
            public void onSearchTermChanged(SearchResult term) {
//                fetchMeetingFromText(term.getTitle());


            }

            @Override
            public void onSearch(SearchResult result) {
                Log.d(TAG, "onSearch: " + result.getTitle());
                fetchMeetingFromText(result.getTitle());
            }


            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
                fetchMeetingFromText(result.getTitle());
            }

            @Override
            public void onBackPressClick() {
                onBackPressed();
            }

            @Override
            public void onFilterTagClicked() {
                Intent intent = new Intent(SearchActivity.this, TAGActivity.class);
                intent.putExtra(CommonMethod.FROM_SEARCH, true);
                startActivityForResult(intent, RC_FILTER_TAG);
            }

            @Override
            public void onKeyboardShowHide(boolean isVisible) {
                if (!isVisible) {
                    onBackPressed();
                    Log.d(TAG, "onKeyboardShowHide: " + isVisible);
                }
            }

            @Override
            public void onSearchCleared() {
                Log.d(TAG, "onSearchCleared: ");
            }

        });

//        searchbox.setOverflowMenu(R.menu.overflow_menu);
//        search.setOverflowMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.test_menu_item:
//                        Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
//                        return true;
//                }
//                return false;
//            }
//        });
    }

    private void hideItems(View... view) {
        for (View vi : view
                ) {
            vi.setVisibility(View.GONE);
        }
    }

    private void setOnClickListener() {

        rlCollapsing.setOnClickListener(this);
        txtClearTag.setOnClickListener(this);
    }

    private void getWindowHeightWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.d(TAG, "getWindowHeightWidth: hei" + height);
        Log.d(TAG, "getWindowHeightWidth: wi" + width);

    }

    private void fetchMeetingFromText(String title) {
        try {
            searchList = daoMeetingIntractor.getMeetingByTitleLike(title);
            removeDuplicateSearchItem();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeDuplicateSearchItem() {
        if (searchList.size() > 0) {
//                listSearchResult.clear();
            checkItemIsExist(searchList);
        }
    }

    private void removeSearchItemFromAdapter() {
        if (searchList == null)
            return;
        searchList.clear();
        initSearchAndTagList();
    }

    private void removeTagItemFromAdapter() {
        list.clear();
        tagList.clear();
        initSearchAndTagList();
    }


    private void initSearchAndTagList() {
        hashMap.clear();
        listSearchResult.clear();

        hideTagItem();
        if (searchList != null)
            checkItemIsExist(searchList);
        if (list != null)
            checkItemIsExist(list);


    }


    private void setRecyclerView() {
        adapter = new SearchResultAdapter(this, listSearchResult, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void rootLayoutObserver() {

        rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.

                rlRoot.getWindowVisibleDisplayFrame(r);
                int screenHeight = rlRoot.getRootView().getHeight();
                Log.d(TAG, "onGlobalLayout screenHeight: " + screenHeight);

                if (fullHeight == 0) {
                    fullHeight = r.bottom;
                    Log.d(TAG, "onGlobalLayout: fullHeight First" + fullHeight);
                    return;
                }

                    if (r.bottom< fullHeight && keyBoardHeight == 0) {

                        Log.d(TAG, "onGlobalLayout: r.bottom" + r.bottom);
                        Log.d(TAG, "onGlobalLayout: fullHeight" + fullHeight);
                        keyBoardHeight = r.bottom;

                        return;
                    }

                    if(r.bottom== fullHeight)
                    {
                        Log.d(TAG, "onGlobalLayout: Keyboard Open fullHeight " + fullHeight +"rBottom"+r.bottom);
                        if (searchbox != null)
                            searchbox.removeSearchFocus();

                    }

                }
    });
    }


    private void hideTagItem() {
        if (tagList == null || tagList.size() == 0) {
            txtSelectedTag.setVisibility(View.VISIBLE);

            setTagTextWithImage();
            txtClearTag.setVisibility(View.GONE);
            rlCollapsing.setVisibility(View.GONE);
        }
    }


    private void showHideView(View show, View hide) {
        show.setVisibility(View.VISIBLE);
        hide.setVisibility(View.GONE);

    }

    private List<TblMeeting> getMeetingFromDatabase() {
        daoMeetingIntractor = new DaoMeetingInteractor(SearchActivity.this);
        try {
            return daoMeetingIntractor.getMeetings();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

//        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.m_search_meeting));
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(false);

        return true;
    }


    @Override
    public void onItemClicked(int position, TblMeeting meeting) {
        Intent intent = new Intent(SearchActivity.this, MeetingDetailActivity.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING, meeting);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_FILTER_TAG && resultCode == RESULT_OK) {
            hasFromActivityResult = true;
            Log.d(TAG, "onActivityResult: getMaxLines" + tagContainerLayout.getMaxLines());
            Log.d(TAG, "setContainerLayoutHeight: ContainerHeight:" + tagContainerLayout.getHeight());
            Log.d(TAG, "setContainerLayoutHeight: ContainerHeight:" + tagContainerLayout.getHeight());

            List<TblTAG> tag = data.getParcelableArrayListExtra("tag");
            try {
                setTagViewsAndMeeting(tag);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setTagViewsAndMeeting(List<TblTAG> tag) throws SQLException {
        txtSelectedTag.setText("Selected TAG");
        showItems(rlCollapsing, tagContainerLayout, txtClearTag, txtSelectedTag);
        showView(rlTag, recyclerView);
        fetchMeetingFromTAGS(tag);

    }

    private void showItems(View... view) {
        for (View vi : view) {
            vi.setVisibility(View.VISIBLE);

        }
    }

    private void showView(View show, View shows) {
        show.setVisibility(View.VISIBLE);
        shows.setVisibility(View.VISIBLE);
    }

    private void fetchMeetingFromTAGS(List<TblTAG> tag) throws SQLException {
        tagList = new ArrayList<>();
        tagList.addAll(tag);
        getMeetingFromTag();
        createTags();

    }

    private void getMeetingFromTag() throws SQLException {
//        list = daoMeetingIntractor.getMeetingByTagsLike(tagList);
        list = daoMeetingIntractor.getMeetingByTagsLike(tagList);
    }

    private void createTags() {
        if (list.size() > 0) {
//            listSearchResult.clear();
//            listSearchResult.addAll(list);
//            adapter.notifyDataSetChanged();
            checkItemIsExist(list);

        }
        addTagToContainerLayout(tagList);

    }


    private void addTagToContainerLayout(List<TblTAG> list) {

        if (list.size() > 0) {
            List<int[]> colors = new ArrayList<int[]>();

            for (int i = 0; i < list.size(); i++) {
                int[] col1 = {Color.parseColor("#076810"), Color.parseColor("#ffffff"), Color.parseColor("#ffffff")};
                colors.add(col1);
            }
            tagContainerLayout.setTags(list, colors, false);
        }
        tagOnClickListener();

    }

    private void tagOnClickListener() {
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, Object object) {
//                try {
//                    TblTAG text= (TblTAG) object;
//                    text.setSelected(!text.isSelected());
////                    tagInteractor.update(text);
//                    tagContainerLayout.changeSelectColor(position, text);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


            }

            @Override
            public void onTagLongClick(int position, Object text) {

            }

            @Override
            public void onTagCrossClick(int position, Object object) {
                TblTAG test = (TblTAG) object;
                if (test.isSelected()) {
                    tagContainerLayout.removeTag(position);
                    removeTagFromTagList(position);

//                    listSearchResult.remove(position);

//                    test.setSelected(false);
//                    tagContainerLayout.changeSelectColor(position, test);

                }

            }
        });
    }

    private void removeTagFromTagList(int test) {
        tagList.remove(test);
        try {
            getMeetingFromTag();
            initSearchAndTagList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void checkItemIsExist(List<TblMeeting> list) {
        for (TblMeeting tblMeeting : list) {
            if (tblMeeting == null)
                continue;

            if (!hashMap.containsKey(tblMeeting.getPkid())) {
                listSearchResult.add(tblMeeting);
                hashMap.put(tblMeeting.getPkid(), tblMeeting);
            }
        }
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_collapsing:
                setContainerLayoutHeight();
                break;
            case R.id.txt_clear_tag:
                clearTag();
                break;

        }
    }


    //
    @Override
    public void onBackPressed() {
//        SensorManager.M`
        Log.d(TAG, "onBackPressed: ");
        Log.i(TAG, "onBackPressed: ");
        super.onBackPressed();
    }

    private void clearTag() {
        tagContainerLayout.removeAllTags();
        hideItems(rlCollapsing, tagContainerLayout, txtClearTag);
        txtSelectedTag.setVisibility(View.VISIBLE);
        setTagTextWithImage();
        removeTagItemFromAdapter();

    }


    private void setTagTextWithImage() {
        String tagWithImageTxt = "<img src='ic_tags'><b>  Click this icon to filter result by Tags  </b>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtSelectedTag.setText(Html.fromHtml(tagWithImageTxt, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    Drawable drawable;
                    int dourceId =
                            getApplicationContext()
                                    .getResources()
                                    .getIdentifier(source, "drawable", getPackageName());
                    drawable =
                            getApplicationContext()
                                    .getResources()
                                    .getDrawable(dourceId);

                    drawable.setBounds(
                            0,
                            0,
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight());

                    return drawable;
                }
            }, null));

        } else {
            txtSelectedTag.setText(" Click this icon to filter result by Tags");
            txtSelectedTag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tags, 0, 0, 0);
        }
    }


    private void setContainerLayoutHeight() {
        if (isCollapsed) {
            imgUp.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            Log.d(TAG, "setContainerLayoutHeight: ContainerHeight isCollapsed:" + tagContainerLayout.getHeight());
            Log.d(TAG, "setContainerLayoutHeight: singLineTagContainerHeight UpArrow:" + singLineTagContainerHeight);

            resizedLayoutWithAnimation(tagContainerLayout,
                    (int) tagContainerLayout.getHeight(),
                    ((int) (singLineTagContainerHeight + CommonMethod.dp2px(getApplicationContext(), 10))),
                    CommonMethod.FROM_ARROW_UP);
        } else {
            imgUp.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            Log.d(TAG, "setContainerLayoutHeight: singLineTagContainerHeight DownArrow:" + singLineTagContainerHeight);

            resizedLayoutWithAnimation(tagContainerLayout,
                    (int) (singLineTagContainerHeight + CommonMethod.dp2px(getApplicationContext(), 10)),
                    (int) (oldHeight - (int) (singLineTagContainerHeight + CommonMethod.dp2px(getApplicationContext(), 10))),
                    CommonMethod.FROM_ARROW_DOWN);
        }
        isCollapsed = !isCollapsed;
    }

    private void resizedLayoutWithAnimation(View rlTag, int startHeight, int targetHeight, int from) {

        ResizeAnimation resizeAnimation = new ResizeAnimation(
                rlTag,
                targetHeight,
                startHeight,
                from
        );
        resizeAnimation.setDuration(500);
        this.rlTag.startAnimation(resizeAnimation);
    }


    @Override
    public void onHeightAvailble(final float height) {
        final ViewTreeObserver observer = tagContainerLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!hasFromActivityResult)
                    return;
                Log.d(TAG, "tagContainerView: " + tagContainerLayout.getHeight() + " heightView " + height);
                oldHeight = tagContainerLayout.getHeight();
                singLineTagContainerHeight = (height + CommonMethod.dp2px(getApplicationContext(), 10));
                float numberOfLines = (float) (tagContainerLayout.getHeight() / singLineTagContainerHeight);
                Log.d(TAG, "onGlobalLayout: NumberOfLines:" + numberOfLines);
                if ((int) numberOfLines > 1) {
                    rlCollapsing.setVisibility(View.VISIBLE);
                    resizedLayoutWithAnimation(tagContainerLayout,
                            (int) tagContainerLayout.getHeight(),
                            ((int) (singLineTagContainerHeight + CommonMethod.dp2px(getApplicationContext(), 10))),
                            CommonMethod.FROM_ARROW_UP);
                } else {
                    rlCollapsing.setVisibility(View.GONE);

                }
                observer.removeGlobalOnLayoutListener(this);
                hasFromActivityResult = false;
            }
        });


//        Log.d(TAG, "showDropArrow: " + tagContainerLayout.getNumberOfTagLines(tagContainerLayout));
    }


}
