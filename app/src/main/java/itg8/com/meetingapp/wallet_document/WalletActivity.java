package itg8.com.meetingapp.wallet_document;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.db.TblDocument;

public class WalletActivity extends AppCompatActivity implements WalletAdapter.cardOnLongPressListerner {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private WalletAdapter adapter;

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

        List<TblDocument> listDoc = getTblDocuments();



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new WalletAdapter(getApplicationContext(), listDoc, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onLongPressClickListner(int position, TblDocument list, RelativeLayout layout, CardView view, TextView textView, TextView lblTitleFull, MotionEvent motionEvent) {
        RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int width = view.getWidth();
        int height = view.getHeight();
        int fullWidth = layout.getWidth();
        int fullHeight = layout.getHeight();
      boolean b=   longPressEvent(view, textView, lblTitleFull, motionEvent,height,fullHeight);
      return b;

    }

    @Override
    public void  onItemImgMoreClickListner(int position, TblDocument document, ImageView img) {
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

                shareItem(WalletActivity.this,"TITLE","BODY", null);

                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    public static void shareItem(Context context, String title, String body, Uri uri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        }else
        {
            sharingIntent.setType("text/plain");
        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(sharingIntent, "Share"));


    }

    private void propertyAnimation(CardView view, int width) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", width);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 100);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY).start();
    }

    @NonNull
    private List<TblDocument> getTblDocuments() {
        TblDocument document = new TblDocument();
        List<TblDocument> list = new ArrayList<>();

        document.setFileName("DOC FILE");
        document.setFileExt(CommonMethod.EXT_DOC);
        list.add(document);

//        document.setFileName("EXCEL FILE");
//        document.setFileExt(CommonMethod.EXT_EXL);
//        list.add(document);
//        document.setFileName("PDF FILE");
//        document.setFileExt(CommonMethod.EXT_PDF);
//        list.add(document);
//        document.setFileName("JPG FILE");
//        document.setFileExt(CommonMethod.EXT_JPG);
//        list.add(document);
//        document.setFileName("TXT FILE");
//        document.setFileExt(CommonMethod.EXT_TXT);
//        list.add(document);
//        document.setFileName("ZIP FILE");
//        document.setFileExt(CommonMethod.EXT_ZIP);
//        list.add(document);
//        document.setFileName("PPT FILE");
//        document.setFileExt(CommonMethod.EXT_PPT);
//        list.add(document);
        return list;
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


}
