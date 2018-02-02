package itg8.com.meetingapp.import_meeting;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;

public class ImportMessageDetailActivity extends AppCompatActivity implements View.OnClickListener {

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
    @BindView(R.id.lbl_more_participeint)
    TextView lblMoreParticipeint;
    @BindView(R.id.lbl_tag_name)
    TextView lblTagName;
    @BindView(R.id.lbl_Document)
    TextView lblDocument;
    @BindView(R.id.lbl_more_document)
    TextView lblMoreDocument;
    @BindView(R.id.lbl_Comment)
    TextView lblComment;
    @BindView(R.id.lbl_add_comment)
    TextView lblAddComment;
    @BindView(R.id.ll_button)
    LinearLayout llButton;
    @BindView(R.id.recyclerView)
    ScrollView recyclerView;

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
        lblAddComment.setOnClickListener(this);
        lblMoreDocument.setOnClickListener(this);
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
        switch (view.getId())
        {
            case R.id.lbl_add_comment:
                openBottomSheetAddComment();
                break;
            case R.id.lbl_more_document:
                openBottomSheetAddDocument();
                break;
        }
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
        });  btnAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mBottomSheetDialog.show();
    }

    private void setRecyclerview(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new ImportMeetingAdapter(getApplicationContext()));
    }

    private void openBottomSheetAddComment() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_comment, null);

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
        });  btnAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mBottomSheetDialog.show();
    }
}
