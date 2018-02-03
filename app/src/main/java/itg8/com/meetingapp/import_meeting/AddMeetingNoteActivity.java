package itg8.com.meetingapp.import_meeting;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;

public class AddMeetingNoteActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lbl_title)
    TextView lblTitle;
    @BindView(R.id.txt_note_value)
    TextView txtNoteValue;
    @BindView(R.id.txt_note_frame)
    TextView txtNoteFrame;
    @BindView(R.id.img_doc)
    ImageView imgDoc;
    @BindView(R.id.lbl_tag_name)
    TextView lblTagName;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.lbl_time)
    TextView lblTime;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting_note);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();


    }

    private void init() {
        imgAdd.setOnClickListener(this);
        imgShare.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_save) {
            return true;
        }
        if (id == R.id.action_reminder) {
           openDialogueTimePlace();
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialogueTimePlace() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                openAddDocumentBottomSheet();
                break;
            case R.id.img_share:
                openShareDocumentBottomSheet();

                break;
        }
    }

    private void openAddDocumentBottomSheet() {

        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_add_document_menu, null);

        final Dialog mBottomSheetDialog = new Dialog(this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);



//        mBottomSheetDialog.dismiss();

        mBottomSheetDialog.show();

    }

    private void openShareDocumentBottomSheet() {

        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_document_share_menu, null);

        final Dialog mBottomSheetDialog = new Dialog(this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

//        mBottomSheetDialog.dismiss();

        mBottomSheetDialog.show();

    }
}
