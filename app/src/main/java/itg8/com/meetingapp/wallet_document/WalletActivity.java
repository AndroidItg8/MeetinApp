package itg8.com.meetingapp.wallet_document;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Prefs;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblDocument;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.showcase.MaterialIntroView;
import itg8.com.meetingapp.showcase.animation.MaterialIntroListener;
import itg8.com.meetingapp.showcase.shape.Focus;
import itg8.com.meetingapp.showcase.shape.FocusGravity;
import itg8.com.meetingapp.showcase.shape.ShapeType;

public class WalletActivity extends AppCompatActivity implements WalletAdapter.cardOnLongPressListerner, MaterialIntroListener {

    private static final String INTRO_CARD = "INTRO_CARD";
    private static final String DOCUMENT_LIST = "DOCUMENT_LIST";
    private static final String TAG = "WalletActivity";
    private static final String CHECK_INSTANCE = "CHECK_INSTANCE";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_no_meeting)
    ImageView imgNoMeeting;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_sub_title)
    TextView txtSubTitle;
    @BindView(R.id.rl_no_meeting_item)
    RelativeLayout rlNoMeetingItem;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private WalletAdapter adapter;
    private DaoMeetingInteractor daoDocument;
    private List<TblMeeting> listDoc;
    private boolean isWallet=false;

//    public void shareItem(Context context, File fileWithinMyDir) {
//        Intent intentShareFile =new Intent(Intent.ACTION_SEND);
//        if(fileWithinMyDir.exists()) {
//            intentShareFile.setType(CommonMethod.getMimetypeFromUri(Uri.fromFile(fileWithinMyDir),getContentResolver()));
//            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileWithinMyDir.getAbsolutePath()));
//
//            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
//                    "Sharing File...");
//            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//
//            startActivity(Intent.createChooser(intentShareFile, "Share File"));
//        }
//
//    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence title, String sub_title,
                                              CharSequence doc_name) {
        return Html.fromHtml(res.getString(R.string.no_meeting, title, sub_title, doc_name));

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(DOCUMENT_LIST, (ArrayList<? extends Parcelable>) listDoc);

    }

    public void shareItem(Context context, String title, String ext, File file, ShareActionProvider provider) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

        sharingIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, "itg8.com.meetingapp.fileprovider", file));
        Log.d(TAG, "shareItem: File Extension:" + ext);
        Log.d(TAG, "shareItem: File Name:" + title);
        sharingIntent.setType(CommonMethod.getMimetypeFromFilename("." + ext));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, title);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        provider.setShareIntent(sharingIntent);

        //context.startActivity(Intent.createChooser(sharingIntent, "Share"));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Meeting Wallet");
        daoDocument = new DaoMeetingInteractor(WalletActivity.this);
        init();
        if (savedInstanceState != null) {
            listDoc = savedInstanceState.getParcelableArrayList(DOCUMENT_LIST);
            adapter.notifyDataSetChanged();

        }

    }


    private void init() {
        listDoc = getTblDocuments();
        if (listDoc.size() > 0) {
            showRecyclerView();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new WalletAdapter(getApplicationContext(), listDoc, this);
            recyclerView.setAdapter(adapter);
            if(!Prefs.getBoolean(CommonMethod.FIRST_TIME_WALLET,false))
                showMaterialInfo();


        } else {

            hideRecyclerView();
            txtTitle.setText(formatPlaceDetails(getResources(), "Today you do not have", "Meetings related documents", "Here you can quickly get document of all today's meeting."));

        }

    }

    private void showMaterialInfo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMaterialIntro();
            }
        }, 2000);
    }

    private void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
        rlNoMeetingItem.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        rlNoMeetingItem.setVisibility(View.GONE);

    }

    private void showMaterialIntro() {
        new MaterialIntroView.Builder(WalletActivity.this)
                .enableDotAnimation(true)
                .setShape(ShapeType.CIRCLE)
                .setInfoTextSize(24)
                .setTextColor(Color.WHITE)
                .setMaskColor(R.color.transparentBlack)
                .setTarget((recyclerView.getChildAt(0).findViewById(R.id.cardView)))
                .setTargetPadding(0)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.ALL)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .setListener(this)
                .performClick(true)
                .setIdempotent(true)
                .setInfoText("Click this to see full agenda detail.")
                .setUsageId(INTRO_CARD) //THIS SHOULD BE UNIQUE ID
                .show();
    }

    @Override
    public boolean onLongPressClickListner(int position, TblMeeting list, RelativeLayout layout, CardView view, TextView textView, TextView lblTitleFull, MotionEvent motionEvent) {
        RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int width = view.getWidth();
        int height = view.getHeight();
        int fullWidth = layout.getWidth();
        int fullHeight = layout.getHeight();
        boolean b = longPressEvent(view, textView, lblTitleFull, motionEvent, height, fullHeight);
        return b;

    }

    @Override
    public void onItemImgMoreClickListner(int position, final TblDocument document, ImageView img) {
        PopupMenu popup = new PopupMenu(this, img);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());
        MenuItem item = popup.getMenu().findItem(R.id.action_share);
        ShareActionProvider provider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareItem(WalletActivity.this, " File Share of " + document.getMeeting().getTitle() + " Meeting...", document.getFileExt(), new File(document.getFileActPath()), provider);


        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(
//                        WalletActivity.this,
//                        "You Clicked : " + item.getTitle(),
//                        Toast.LENGTH_SHORT
//                ).show();

//                shareItem(WalletActivity.this,new File(document.getFileActPath()));

                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    private void propertyAnimation(CardView view, int width) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", width);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 100);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY).start();
    }

    @NonNull
    private List<TblMeeting> getTblDocuments() {


        List<TblMeeting> listMeeting = null;
        List<TblMeeting> listMeetingTemp = null;
        try {

            listMeetingTemp = daoDocument.getMeetingsByDate(Calendar.getInstance().getTime());
            listMeeting = new ArrayList<>();
            for (TblMeeting meet :
                    listMeetingTemp) {
                List<TblDocument> documents = meet.getDocuments();
                if (documents.size() > 0) {
                    listMeeting.add(meet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMeeting;
    }


    private boolean longPressEvent(CardView view, TextView textView, TextView lblTitleFull, MotionEvent motionEvent, int height, int fullHeight) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("TouchTest", "Touch down");
            lblTitleFull.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            setZoomInAnimation(view);
//            view.animate().scaleY(height/2).start();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d("TouchTest", "Touch up");
            lblTitleFull.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            setZoomOutAnimation(view);
        }

        return true;


    }

    private void setZoomOutAnimation(CardView view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        view.startAnimation(animation);
    }

    private void setZoomInAnimation(CardView view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        view.startAnimation(animation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUserClicked(String materialIntroViewId) {
        if (materialIntroViewId.equals(INTRO_CARD)) {
            Prefs.putBoolean(CommonMethod.FIRST_TIME_WALLET, true);
            new MaterialIntroView.Builder(WalletActivity.this).dismissOnTouch(true);
        }
    }
}
