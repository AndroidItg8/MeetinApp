package itg8.com.meetingapp.document_meeting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;

public class DocumentMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private String lblNote;
    NoteItemListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_meeting);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab.setOnClickListener(this);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PreDocmentFragment(), "PreDoc");
        adapter.addFragment(new PostDocumnetFragment(), "PostDoc");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.fab:
                showDialogBox();
        }
    }

    private void showDialogBox() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(DocumentMeetingActivity.this);
        builderSingle.setIcon(R.drawable.ic_mode_edit);
      //  AlertDialog dialog = new AlertDialog(DocumentMeetingActivity.this);
        builderSingle.setTitle("Add Note:-");
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
//        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_mode_edit_black_24dp);

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(DocumentMeetingActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.add_note_meeting, null);
        builderSingle.setView(mView);
        final EditText lblDocumentNote = (EditText) mView.findViewById(R.id.edt_document_title);
//
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        builderSingle.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!TextUtils.isEmpty(lblDocumentNote.getText()))
                {
                    lblNote=lblDocumentNote.getText().toString().trim();
                    Log.d("TAG","lblNote:"+lblNote);
                    if(listener != null)
                        listener.sendItemToFragment(lblNote);


                }
            }
        });
//        dialog = builderSingle.create();

        builderSingle.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
    }
        return super.onOptionsItemSelected(item);

    }


  public   interface NoteItemListener{
        void sendItemToFragment(String note);
    }
}






