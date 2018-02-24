package itg8.com.meetingapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;

import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.import_meeting.ImportMeetingActivity;
import itg8.com.meetingapp.meeting.MeetingActivity;
import itg8.com.meetingapp.meeting.TAGActivity;
import itg8.com.meetingapp.service.NotificationService;
import itg8.com.meetingapp.setting.SettingActivity;
import itg8.com.meetingapp.wallet_document.WalletActivity;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName() ;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static final String TAG = "HomeActivity";
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private HashMap<Integer,Boolean> prioritiesByFilter=new HashMap<>();
    private Menu mPopupMenu;
    private PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        callFragment();
        setDefaultPriorityFilters();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                testMeetingNotification();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:MM:ss.SSS");
        Log.d(TAG, "onCreate: df:"+df.format(new Date()));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//      this.deleteDatabase("meetingDb");
//        Log.d("TAG","DeletedDatabase()"+this.deleteDatabase("meetingDb"));


    }

    private void setDefaultPriorityFilters() {
        prioritiesByFilter.put(CommonMethod.PRIORITY_INT_HIGH,true);
        prioritiesByFilter.put(CommonMethod.PRIORITY_INT_MEDIUM,true);
        prioritiesByFilter.put(CommonMethod.PRIORITY_INT_LOW,true);
    }

    private void callFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame_container,  HomeFragment.newInstance(prioritiesByFilter),HomeFragment.class.getSimpleName()).commit();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
//            Intent intent = new Intent(this, SearchActivity.class);
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            return true;
        }   if (id == R.id.menu_filter) {
            showDropdownMenu(findViewById(R.id.menu_filter));
            return true;
        }
         if(id== android.R.id.home)
         {
             Log.d(TAG, "onOptionsItemSelected: HomeActivity");
             onBackPressed();
         }

        return super.onOptionsItemSelected(item);
    }

        private void showDropdownMenu(View view) {
            popupMenu = new PopupMenu(this,view);
            popupMenu.getMenuInflater().inflate(R.menu.filter_pop_menu,popupMenu.getMenu());
            mPopupMenu=popupMenu.getMenu();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    item.setChecked(!item.isChecked());
                    switch (item.getItemId()){
                        case R.id.menu_high:
                            prioritiesByFilter.put(CommonMethod.PRIORITY_INT_HIGH,item.isChecked());
                            break;
                        case R.id.menu_medium:
                            prioritiesByFilter.put(CommonMethod.PRIORITY_INT_MEDIUM,item.isChecked());
                            break;
                        case R.id.menu_low:
                            prioritiesByFilter.put(CommonMethod.PRIORITY_INT_LOW,item.isChecked());
                            break;
                    }
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                    item.setActionView(new View(HomeActivity.this));
                    callFragment();
                    return false;
                }
            });
            prepareMenuAndShow();
        }

    private void prepareMenuAndShow() {
        prepareMenuItems();
        popupMenu.show();
    }

    private void prepareMenuItems() {
        MenuItem highItem=mPopupMenu.findItem(R.id.menu_high);
        highItem.setChecked(prioritiesByFilter.get(CommonMethod.PRIORITY_INT_HIGH));
        MenuItem mediumItem=mPopupMenu.findItem(R.id.menu_medium);
        mediumItem.setChecked(prioritiesByFilter.get(CommonMethod.PRIORITY_INT_MEDIUM));
        MenuItem lowItem=mPopupMenu.findItem(R.id.menu_low);
        lowItem.setChecked(prioritiesByFilter.get(CommonMethod.PRIORITY_INT_LOW));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.nav_meeting:
                startActivity(new Intent(this, MeetingActivity.class));
                break;

            case R.id.nav_camera:
                startActivity(new Intent(this, ImportMeetingActivity.class));
                break;
            case R.id.nav_wallet:
                startActivity(new Intent(this, WalletActivity.class));
                break;
            case R.id.nav_tag:
               Intent intent = new Intent(this, TAGActivity.class);
               intent.putExtra(CommonMethod.FROM_HOME,true);
                startActivity(intent);
                break;

        }
//        if (fragment != null) {
//            callFragment(fragment);
//            toolbar.setTitle(from);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
