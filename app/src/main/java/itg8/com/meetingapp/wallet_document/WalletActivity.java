package itg8.com.meetingapp.wallet_document;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.sql.SQLException;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private WalletAdapter adapter;

    public static void shareItem(Context context, String title, String body, Uri uri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        } else {
            sharingIntent.setType("text/plain");
        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(sharingIntent, "Share"));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();


    }

    private void init() {
        List<TblMeeting> listDoc = getTblDocuments();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WalletAdapter(getApplicationContext(), listDoc, this);
        recyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMaterialIntro();
            }
        }, 2000);

//   if(!Prefs.getBoolean(CommonMethod.FIRST_TIME_WALLET,false))
//       showMaterialIntro();
//
//
    }

    private void showMaterialIntro() {
        new MaterialIntroView.Builder(WalletActivity.this)
                .enableDotAnimation(true)
                .setShape(ShapeType.CIRCLE)
                .setInfoTextSize(14)
                .setMaskColor(R.color.colorRed)
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
    public void onItemImgMoreClickListner(int position, TblDocument document, ImageView img) {
        PopupMenu popup = new PopupMenu(this, img);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        WalletActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();

                shareItem(WalletActivity.this, "TITLE", "BODY", null);

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
        try {
            listMeeting = new DaoMeetingInteractor(WalletActivity.this).getMeetings();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//
//        TblDocument document = new TblDocument();
//        List<TblDocument> list = new ArrayList<>();
//
//        document.setFileName("DOC FILE");
//        document.setFileExt(CommonMethod.EXT_DOC);
//        list.add(document);
//        TblDocument document1 = new TblDocument();
//        document1.setFileName("EXCEL FILE");
//        document1.setFileExt(CommonMethod.EXT_EXL);
//        list.add(document1);
//        TblDocument document2 = new TblDocument();
//
//        document2.setFileName("PDF FILE");
//        document2.setFileExt(CommonMethod.EXT_PDF);
//        list.add(document2);
//        TblDocument document3 = new TblDocument();
//
//        document3.setFileName("JPG FILE");
//        document3.setFileExt(CommonMethod.EXT_JPG);
//        list.add(document3);
//        TblDocument document4 = new TblDocument();
//
//        document4.setFileName("TXT FILE");
//        document4.setFileExt(CommonMethod.EXT_TXT);
//        list.add(document4);
//        TblDocument document5 = new TblDocument();
//
//        document5.setFileName("ZIP FILE");
//        document5.setFileExt(CommonMethod.EXT_ZIP);
//        list.add(document5);
//        TblDocument document6 = new TblDocument();
//
//        document6.setFileName("PPT FILE");
//        document6.setFileExt(CommonMethod.EXT_PPT);
//        list.add(document6);

        return listMeeting;
    }


    private boolean longPressEvent(CardView view, TextView textView, TextView lblTitleFull, MotionEvent motionEvent, int height, int fullHeight) {

        if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
            Log.d("TouchTest", "Touch down");
            lblTitleFull.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            setZoomInAnimation(view);
//            view.animate().scaleY(height/2).start();
        } else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
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
            Toast.makeText(WalletActivity.this, "User Clicked", Toast.LENGTH_SHORT).show();
            Prefs.putBoolean(CommonMethod.FIRST_TIME_WALLET,true);
            new MaterialIntroView.Builder(WalletActivity.this).dismissOnTouch(true);
        }
    }
}
