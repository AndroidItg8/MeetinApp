package itg8.com.meetingapp.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.widget.search.SearchBox;
import itg8.com.meetingapp.widget.search.SearchResult;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    @BindView(R.id.searchbox)
    SearchBox searchbox;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_lbl)
    TextView txtLbl;
    @BindView(R.id.rl_no_tag)
    RelativeLayout rlNoTag;

    private DaoMeetingInteractor daoMeetingIntractor;
    private List<SearchResult> listSearchResult= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setRecyclerView();

        searchbox = (SearchBox) findViewById(R.id.searchbox);
        List<TblMeeting> listMeeting = getMeetingFromDatabase();
        if (listMeeting != null) {
            for (TblMeeting meeting : listMeeting
                    ) {
                Log.d(TAG, "onCreate: ListMeeting "+  meeting.toString());
                SearchResult option = new SearchResult(meeting, getResources().getDrawable(R.drawable.ic_history));
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
            }

            @Override
            public void onSearchTermChanged(SearchResult term) {
                Log.d(TAG, "onSearchTermChanged: " + term);

            }

            @Override
            public void onSearch(SearchResult result) {
                Log.d(TAG, "onSearch: " + result);

            }


            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked

                    Log.d(TAG, "onResultClick: " + result);
                    listSearchResult.add(result);
                    setRecyclerView();




            }

            @Override
            public void onBackPressClick() {
                onBackPressed();
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
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

    private void setRecyclerView() {
        if(listSearchResult.size()>0) {
            showHideView(recyclerView, rlNoTag);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new SearchResultAdapter(this, listSearchResult));
        }
        else{
            showHideView( rlNoTag,recyclerView);

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


}
