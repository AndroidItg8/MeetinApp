package itg8.com.meetingapp.import_meeting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblContact;
import itg8.com.meetingapp.document_meeting.DocumentMeetingActivity;
import itg8.com.meetingapp.meeting.MeetingActivity;

public class ImportMessageDetailActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_message_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

    }

    private void init() {
        lblMoreDocument.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        showParticipant();

    }

    private void showNoParticipant() {
        lblNoParticipant.setVisibility(View.VISIBLE);
        recyclerViewParticipant.setVisibility(View.INVISIBLE);

    }

    private void showParticipant() {
        lblNoParticipant.setVisibility(View.INVISIBLE);
        recyclerViewParticipant.setVisibility(View.VISIBLE);
        setRecyclerview(recyclerViewParticipant);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lbl_more_document:
                // openBottomSheetAddDocument();
                startActivity(new Intent(getApplicationContext(), DocumentMeetingActivity.class));
                break;
            case R.id.img_edit:
                callMeetingActivityForEdith();
                break;
        }
    }

    private void callMeetingActivityForEdith() {
        Intent intent = new Intent(ImportMessageDetailActivity.this, MeetingActivity.class);
        intent.putParcelableArrayListExtra(CommonMethod.MEETING, (ArrayList<? extends Parcelable>) new DaoMeetingInteractor(ImportMessageDetailActivity.this).getMeetings());
        startActivityForResult(intent, RC_MEETING_EDIT);

    }

    private void openBottomSheetAddDocument() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_document, null);

        final Dialog mBottomSheetDialog = new Dialog(this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);


        Button btnDismiss = mBottomSheetDialog.findViewById(R.id.btn_dismiss);
        final RecyclerView recyclerView = mBottomSheetDialog.findViewById(R.id.recyclerView);
        setRecyclerview(recyclerView);
        Button btnAddDocument = mBottomSheetDialog.findViewById(R.id.btn_add_comment);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        btnAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddMeetingNoteActivity.class);
                startActivityForResult(intent, RC_REQUEST_CODE);

            }
        });
        mBottomSheetDialog.show();
    }

    private void setRecyclerview(RecyclerView recyclerView) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecoration);
        int[] listOfColor = getResources().getIntArray(R.array.androidcolors);

        List<TblContact> contactList = new ArrayList<>();
        recyclerView.setAdapter(new ParticipantTagAdapter(contactList));
    }

    private void openBottomSheetAddComment() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_add_document_menu, null);

        final Dialog mBottomSheetDialog = new Dialog(this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        Button btnDismiss = mBottomSheetDialog.findViewById(R.id.btn_dismiss);
        final RecyclerView recyclerView = mBottomSheetDialog.findViewById(R.id.recyclerView);
        setRecyclerview(recyclerView);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==RC_MEETING_EDIT)
       {
           setAllTextView();
       }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAllTextView() {

    }

}
