package itg8.com.meetingapp.import_meeting;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;

public class ImportMeetingActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_messages)
    TextView txtMessages;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_meeting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();


    }

    private void init() {
        showData();
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
                startActivity(new Intent(getApplicationContext(),ImportMessageDetailActivity.class));
                break;

        }


    }

    private void importMessage() {
        showProgress();
        showFinished();
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
}
