package itg8.com.meetingapp.import_meeting;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.custom_tag.MaxHeightScrollView;
import itg8.com.meetingapp.custom_tag.TagContainerLayout;
import itg8.com.meetingapp.db.DaoContactInteractor;
import itg8.com.meetingapp.db.DaoDocumentInteractor;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.DaoTagInteractor;
import itg8.com.meetingapp.db.TblContact;
import itg8.com.meetingapp.db.TblDocument;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.db.TblMeetingTag;
import itg8.com.meetingapp.db.TblTAG;
import itg8.com.meetingapp.document_meeting.DocumentMeetingActivity;
import itg8.com.meetingapp.meeting.MeetingActivity;
import itg8.com.meetingapp.service.NotificationService;

public class MeetingDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MeetingDetailActivity";
    private static final int RC_REQUEST_CODE = 987;
    private static final int RC_MEETING_EDIT = 986;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lbl_date)
    TextView lblDate;
    @BindView(R.id.lbl_date_value)
    TextView lblDateValue;
    @BindView(R.id.lbl_time)
    TextView lblTime;
    @BindView(R.id.lbl_time_value)
    TextView lblTimeValue;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.lbl_title)
    TextView lblTitle;
    @BindView(R.id.lbl_priority)
    TextView lblPriority;
    @BindView(R.id.lbl_priority_value)
    TextView lblPriorityValue;
    @BindView(R.id.lbl_place)
    TextView lblPlace;
    @BindView(R.id.lbl_place_value)
    TextView lblPlaceValue;
    @BindView(R.id.lbl_participeint)
    TextView lblParticipeint;
    @BindView(R.id.recyclerView_participant)
    RecyclerView recyclerViewParticipant;
    @BindView(R.id.lbl_Document)
    TextView lblDocument;
    @BindView(R.id.lbl_more_document)
    TextView lblMoreDocument;
    @BindView(R.id.ll_button)
    LinearLayout llButton;
    @BindView(R.id.recyclerView)
    RelativeLayout recyclerView;
    @BindView(R.id.lbl_no_participant)
    TextView lblNoParticipant;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.view_button)
    View viewButton;
    @BindView(R.id.txtMeetingStatus)
    TextView txtMeetingStatus;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.img_navigate)
    ImageView imgNavigate;
    @BindView(R.id.tag_container_layout)
    TagContainerLayout tagContainerLayout;
    @BindView(R.id.scrollView)
    MaxHeightScrollView scrollView;
    TblMeeting meeting = null;
    private DaoContactInteractor daoContact;
    private DaoDocumentInteractor daoDocument;
    private DaoMeetingInteractor daoMeeting;
    private DaoTagInteractor daoTag;
    private boolean isInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_message_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        daoContact = new DaoContactInteractor(this);
        daoDocument = new DaoDocumentInteractor(this);
        daoMeeting = new DaoMeetingInteractor(this);
        daoTag = new DaoTagInteractor(this);
        init();
    }

    private void init() {
        lblMoreDocument.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        imgNavigate.setOnClickListener(this);
        if (getIntent().hasExtra(CommonMethod.EXTRA_MEETING)) {
            changeActionbarName();

            TblMeeting tempMeeting = getIntent().getParcelableExtra(CommonMethod.EXTRA_MEETING);
            try {
                meeting = daoMeeting.getMeetingById(tempMeeting.getPkid());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (meeting == null)
                return;

            setMeetingRelatedDetail();
        }

    }

    private void setMeetingRelatedDetail() {
        lblDateValue.setText(Helper.getDateFromDate(meeting.getStartTime()));
        lblTitle.setText(meeting.getTitle());
        lblPlaceValue.setText(checkNull(meeting.getAddress()));
        if (meeting.getLongitude() == 0 || meeting.getLatitude() == 0)
            imgNavigate.setVisibility(View.GONE);
        lblPriorityValue.setText(Helper.getPriorityFromType(meeting.getPriority()));
        lblTimeValue.setText(new StringBuilder().append(Helper.getStringTimeFromDate(meeting.getStartTime())).append(" - ").append(Helper.getStringTimeFromDate(meeting.getEndTime())).toString());
        if (Calendar.getInstance().getTimeInMillis() < meeting.getEndTime().getTime() && Calendar.getInstance().getTimeInMillis() > meeting.getStartTime().getTime()) {
            txtMeetingStatus.setText(R.string.meeting_in_progress);
            isInProgress = true;
            txtMeetingStatus.setTextColor(Color.parseColor("#5BCDCD"));
            btnComplete.setOnClickListener(this);
        } else if (Calendar.getInstance().getTimeInMillis() < meeting.getStartTime().getTime()) {
            txtMeetingStatus.setText(R.string.meeting_pending);
            txtMeetingStatus.setTextColor(Color.parseColor("#FFBE41"));
            btnComplete.setEnabled(false);
            btnComplete.setText("Pending");
            btnComplete.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
        } else if (Calendar.getInstance().getTimeInMillis() > meeting.getEndTime().getTime()) {
            txtMeetingStatus.setText(R.string.meeting_over);
            btnComplete.setEnabled(false);
//                txtMeetingStatus.setTextColor(Color.parseColor("#96CC9A"));
            btnComplete.setText("Completed");
            btnComplete.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
        }
        try {
            List<TblContact> participents = daoContact.getContactsByMeetingId(meeting.getPkid());
            showParticipant(participents);
            final List<TblDocument> documents = daoDocument.getDocumentsByMeetingId(meeting.getPkid());
            final List<TblDocument> preDocuments = daoDocument.getPreDocumentsByMeetingId(meeting.getPkid());
            final List<TblDocument> postDocuments = daoDocument.getPostDocumentsByMeetingId(meeting.getPkid());
            if (documents.size() > 0)
                lblMoreDocument.setText(documents.size() + " No of Documents");
            else
                lblMoreDocument.setText("No Documents");

            lblMoreDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MeetingDetailActivity.this, DocumentMeetingActivity.class);
                    intent.putExtra(CommonMethod.EXTRA_MEETING, meeting.getPkid());
                    intent.putExtra(CommonMethod.EXTRA_PROGRESS, isInProgress);
                    intent.putParcelableArrayListExtra(CommonMethod.EXTRA_PRE_DOCUMENTS, (ArrayList<? extends Parcelable>) preDocuments);
                    intent.putParcelableArrayListExtra(CommonMethod.EXTRA_POST_DOCUMENTS, (ArrayList<? extends Parcelable>) postDocuments);
                    startActivity(intent);
                }
            });




            ArrayList<TblTAG> tempTagList = new ArrayList<>();
            for (TblMeetingTag tagMeeting :
                    meeting.getTags()) {
                tempTagList.add(new TblTAG(tagMeeting.getTag(),false));
//                tagList.add(tagMeeting.getTag());
            }

            if (tempTagList.size() > 0) {
                List<int[]> colors = new ArrayList<int[]>();
                for (int i = 0; i < tempTagList.size(); i++) {


                    int[] col1 = {Color.parseColor("#C5E1A5"), Color.parseColor("#C5E1A5"), Color.parseColor("#000000")};
                    colors.add(col1);
                }
                tagContainerLayout.setEnableCross(false);
                tagContainerLayout.setTags(tempTagList, colors, false);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeActionbarName() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.title_activity_meeting_detail);
    }

    private String checkNull(String s) {
        if (TextUtils.isEmpty(s))
            return "Not Available";
        return s;
    }

    private void showNoParticipant() {
        lblNoParticipant.setVisibility(View.VISIBLE);
        recyclerViewParticipant.setVisibility(View.INVISIBLE);

    }

    private void showParticipant(List<TblContact> participents) {
        lblNoParticipant.setVisibility(View.INVISIBLE);
        recyclerViewParticipant.setVisibility(View.VISIBLE);
        setRecyclerview(recyclerViewParticipant, participents);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_share) {
            shareMeeting();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareMeeting() {
        String content = createMeetingContentForSharing();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Meeting App:");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lbl_more_document:
                // openBottomSheetAddDocument();
                Intent intent = new Intent(getApplicationContext(), DocumentMeetingActivity.class);
//                intent.putExtra(CommonMethod.EXTRA_MEETING,meeting.getPkid());
                startActivity(intent);
                break;
            case R.id.img_edit:
                callMeetingActivityForEdit();
                break;
            case R.id.btn_complete:
                completeMeeting();
                break;
            case R.id.btn_delete:
                deleteMeeting();
                break;
            case R.id.img_navigate:
                navigateToMap();
                break;
        }
    }

    private void navigateToMap() {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + meeting.getLatitude() + "," + meeting.getLongitude() + "&daddr=" + meeting.getLatitude() + "," + meeting.getLongitude()));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    private void deleteMeeting() {
        try {
            TblMeeting tempMeeting = (TblMeeting) meeting.clone();
            Log.d(TAG, "deleteMeeting: " + tempMeeting.toString());
            passToService();
            daoMeeting.delete(meeting);
            finish();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void passToService() {
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING, meeting);
        intent.putExtra(CommonMethod.EXTRA_MEETING_CANCELED, meeting.getPkid());
        startService(intent);

    }

    private void completeMeeting() {
        meeting.setEndTime(Calendar.getInstance().getTime());
        try {
            daoMeeting.update(meeting);
            setMeetingRelatedDetail();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Fail to update meeting", Toast.LENGTH_SHORT).show();
        }
    }

    private void callMeetingActivityForEdit() {
        Intent intent = new Intent(MeetingDetailActivity.this, MeetingActivity.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING, meeting.getPkid());
        startActivityForResult(intent, RC_MEETING_EDIT);

    }

    private void setRecyclerview(RecyclerView recyclerView, List<TblContact> participents) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        int[] listOfColor = getResources().getIntArray(R.array.androidcolors);

        recyclerView.setAdapter(new ParticipantTagAdapter(participents));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_MEETING_EDIT) {
            setAllTextView();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAllTextView() {

    }


    /**
     * create meeting details content to share
     */
    private String createMeetingContentForSharing() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello,").append("\n\n")
                .append("I would like to invite you to a meeting. Please refer following details").append("\n\n")
                .append("Agenda : ").append(meeting.getTitle()).append("\n")
                .append("Date : ").append(Helper.getDateFromDate(meeting.getDateOnly())).append("\n")
                .append("Start Time : ").append(Helper.getStringTimeFromDate(meeting.getStartTime())).append("\n")
                .append("End Time : ").append(Helper.getStringTimeFromDate(meeting.getEndTime())).append("\n")
                .append("Location : ").append((meeting.getAddress())).append("\n\n")
                .append("This message is created from ").append(getString(R.string.app_name)).append(" app. You can import meeting directly to the app. Just copy provided message content and click on import from app.")
                .append("\n\n").append("download link : -").append("---");
        return sb.toString();
    }


}
