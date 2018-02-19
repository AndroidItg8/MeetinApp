package itg8.com.meetingapp.import_meeting;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblMeeting;

public class ImportMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ImportMeetingActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_messages)
    TextView txtMessages;
//    @BindView(R.id.toolbar_layout)
//    CollapsingToolbarLayout toolbarLayout;
//    @BindView(R.id.app_bar)
//    AppBarLayout appBar;
    @BindView(R.id.img_sad)
    ImageView imgSad;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.txt_message_import)
    TextView txtMessageImport;
    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.fab_show)
    FloatingActionButton fabShow;
    @BindView(R.id.frame_progress)
    FrameLayout frameProgress;
    @BindView(R.id.rl_data)
    RelativeLayout rlData;
    @BindView(R.id.txt_message_finished)
    TextView txtMessageFinished;
    @BindView(R.id.btn_detail)
    Button btnDetail;
    @BindView(R.id.rl_finished)
    RelativeLayout rlFinished;
    @BindView(R.id.btn_paste)
    Button btnPaste;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_import)
    Button btnImport;
    private TblMeeting meeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_meeting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        init();


    }

    private void init() {
        showData();
//        txtMessages.setMovementMethod(ScrollingMovementMethod.getInstance());
        btnPaste.setOnClickListener(this);
        btnImport.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnDetail.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_paste:
                ClipboardPaste();
                showClearButton();
                break;

            case R.id.btn_import:
                importMessage();
                break;

            case R.id.btn_clear:
                clearPasteData();
                break;
                case R.id.btn_detail:
                    Intent intent=new Intent(this,MeetingDetailActivity.class);
                    intent.putExtra(CommonMethod.EXTRA_MEETING,meeting);
                    startActivity(intent);
//                startActivity(new Intent(getApplicationContext(),MeetingDetailActivity.class));
                break;

        }


    }

    private void importMessage() {
        showProgress();
        Observable.just(txtMessages.getText().toString())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, TblMeeting>() {
                    @Override
                    public TblMeeting apply(String content) throws Exception {
                        String[] lines = content.split(System.getProperty("line.separator"));
                        TblMeeting meeting=new TblMeeting();
                        boolean hasError=false;
                        for (String s :
                                lines) {
                            if(s.contains("Agenda")){
                                String[] splited=s.split(":");
                                if(splited.length>1){
                                    meeting.setTitle(splited[1]);
                                }
                            }else if(s.contains("Date")){
                                String[] splited=s.split(":");
                                if(splited.length>1){
                                    meeting.setDateOnly(Helper.parseDateFromString(splited[1]));
                                }
                            }else if(s.contains("Start Time")){
                                String[] splited=s.split(" : ");
                                if(splited.length>1){
                                    try {
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.setTime(meeting.getDateOnly());
                                        Date startTime=Helper.parseTimeFromString(splited[1]);
                                        Calendar calendar1=Calendar.getInstance();
                                        calendar1.setTime(startTime);
                                        calendar.set(Calendar.HOUR_OF_DAY,calendar1.get(Calendar.HOUR_OF_DAY));
                                        calendar.set(Calendar.MINUTE,calendar1.get(Calendar.MINUTE));
                                        meeting.setStartTime(calendar.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        hasError=true;
                                    }
                                }
                            }else if(s.contains("End Time")){
                                String[] splited=s.split(" : ");
                                if(splited.length>1){
                                    Date endTime= null;
                                    try {
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.setTime(meeting.getDateOnly());
                                        endTime = Helper.parseTimeFromString(splited[1]);
                                        Calendar calendar1=Calendar.getInstance();
                                        calendar1.setTime(endTime);
                                        calendar.set(Calendar.HOUR_OF_DAY,calendar1.get(Calendar.HOUR_OF_DAY));
                                        calendar.set(Calendar.MINUTE,calendar1.get(Calendar.MINUTE));
                                        meeting.setEndTime(calendar.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        hasError=true;

                                    }
                                }
                            }else if(s.contains("Location")){
                                String[] splited=s.split(":");
                                if(splited.length>1){
                                    meeting.setAddress((splited[1]));
                                }
                            }
                        }
                        meeting.setCreated(Calendar.getInstance().getTime());
                        meeting.setPriority(CommonMethod.PRIORITY_INT_MEDIUM);

                        if(!hasError)
                            return meeting;
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TblMeeting>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TblMeeting tblMeeting) {
                        if(tblMeeting==null) {
                            showError();
                            return;
                        }
                        Log.d(TAG, "onNext: ");
                        DaoMeetingInteractor interactor = new DaoMeetingInteractor(ImportMeetingActivity.this);
                        try {
                            interactor.insert(tblMeeting);
                            setMeeting(tblMeeting);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        showFinished();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        showFinished();
                    }
                });
//        showFinished();
    }

    private void showError() {
        progress.setVisibility(View.GONE);
        Toast.makeText(this, "Fail to import Meeting", Toast.LENGTH_SHORT).show();
    }

    private void showFinished() {
        rlFinished.setVisibility(View.VISIBLE);
        rlData.setVisibility(View.GONE);

    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        btnImport.setVisibility(View.GONE);
    }

    private void showData() {
        rlData.setVisibility(View.GONE);
        rlEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmptyPage() {
        rlEmpty.setVisibility(View.GONE);

    }


    private void clearPasteData() {
        txtMessages.setText(getString(R.string.show_message));
        showPasteButton();
        hideFinishedLayout();
        hideImportLayout();
        showEmptyPage();
    }

    private void hideImportLayout() {
        rlData.setVisibility(View.GONE);
    }

    private void hideFinishedLayout() {
        rlFinished.setVisibility(View.GONE);
    }

    private void showEmptyPage() {
        rlEmpty.setVisibility(View.VISIBLE);

    }

    private void showClearButton() {
        btnClear.setVisibility(View.VISIBLE);
        btnPaste.setVisibility(View.GONE);
        hideEmptyPage();
        ShowImportLayout();

    }

    private void ShowImportLayout() {
        rlData.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        btnImport.setVisibility(View.VISIBLE);

    }

    private void showPasteButton() {
        btnPaste.setVisibility(View.VISIBLE);
        btnClear.setVisibility(View.GONE);
    }

    private void ClipboardPaste() {

        String pasteText;

        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            pasteText = clipboard.getText().toString();
            txtMessages.setText(pasteText);

        } else {

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.hasPrimaryClip() == true) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                pasteText = item.getText().toString();
                txtMessages.setText(pasteText);

            } else {

                Toast.makeText(getApplicationContext(), "Nothing to Paste", Toast.LENGTH_SHORT).show();

            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMeeting(TblMeeting meeting) {
        this.meeting = meeting;
    }
}
