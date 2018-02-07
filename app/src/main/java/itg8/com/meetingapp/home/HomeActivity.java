package itg8.com.meetingapp.home;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;

import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.import_meeting.ImportMeetingActivity;
import itg8.com.meetingapp.meeting.MeetingActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        callFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testMeetingNotification();
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

    }

    private void callFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frame_container,  HomeFragment.newInstance("",""),HomeFragment.class.getSimpleName()).commit();
    }

    //TODO DELETE:
    private void testMeetingNotification() {
        TblMeeting meeting = new TblMeeting();
        meeting.setDate(Calendar.getInstance().getTime());
        meeting.setTitle("MEETING TEST1");
        meeting.setPriority(CommonMethod.PRIORITY_INT_HIGH);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 44);
        meeting.setStartTime(calendar.getTime());
        calendar.add(Calendar.MINUTE, 1);
        meeting.setEndTime(calendar.getTime());
        meeting.setCreated(Calendar.getInstance().getTime());
        meeting.setPkid(1);
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING, meeting);
        long timeDifference = Helper.getTimeDifferenceFromCurrent(meeting.getStartTime());
        if (timeDifference > 1) {
            long diffByPriority = CommonMethod.getDifferenceFromPriority(meeting.getPriority(), timeDifference);
            if (diffByPriority > 0) {
                intent.putExtra(CommonMethod.EXTRA_MEETING_TIME_DIFF, diffByPriority);
            }
        }
        startService(intent);
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
