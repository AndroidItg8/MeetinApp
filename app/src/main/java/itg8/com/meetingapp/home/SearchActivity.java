package itg8.com.meetingapp.home;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.widget.search.SearchBox;
import itg8.com.meetingapp.widget.search.SearchResult;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    @BindView(R.id.searchbox)
    SearchBox searchbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchbox = (SearchBox) findViewById(R.id.searchbox);
//        searchbox.enableVoiceRecognition(this);
        for(int x = 0; x < 10; x++){
            SearchResult option = new SearchResult("Result " + Integer.toString(x), getResources().getDrawable(R.drawable.ic_history));
            searchbox.addSearchable(option);
        }

        searchbox.setSearchListener(new SearchBox.SearchListener(){

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
            public void onSearchTermChanged(String term) {
                //React to the search term changing
                //Called after it has updated results
                Log.d(TAG, "onSearchTermChanged: "+term);

            }

            @Override
            public void onSearch(String searchTerm) {
                Log.d(TAG, "onSearch: "+searchTerm);


            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
                Log.d(TAG, "onResultClick: "+result);

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
