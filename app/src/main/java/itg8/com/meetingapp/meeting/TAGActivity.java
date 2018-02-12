package itg8.com.meetingapp.meeting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.db.DaoTagInteractor;
import itg8.com.meetingapp.db.TblTAG;

public class TAGActivity extends AppCompatActivity implements View.OnClickListener, TAGAddAdapter.onItemClickedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.edt_document_title)
    EditText edtDocumentTitle;
    @BindView(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    List<TblTAG> tagList = new ArrayList<>();
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_lbl)
    TextView txtLbl;
    @BindView(R.id.rl_no_tag)
    RelativeLayout rlNoTag;
    private TblTAG tag;
    private TAGAddAdapter tagAddAdapter;
    private DaoTagInteractor tagInteractor;
    private MenuItem mSerachItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();


    }

    private void init() {
      //  tagInteractor = new DaoTagInteractor(TAGActivity.this);
        btnAdd.setOnClickListener(this);
        try {
            tagList = tagInteractor.getTags();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (tagList != null && tagList.size() > 0) {
            showHideView(recyclerView, rlNoTag);
            createRecyclerView();
        } else {
            showHideView(rlNoTag, recyclerView);
            txtLbl.setText(formatPlaceDetails(getResources(),"Yet No","TAG","Available"));

        }


    }




private static Spanned formatPlaceDetails(Resources res, CharSequence title, String sub_title,
                                          CharSequence doc_name) {
        return Html.fromHtml(res.getString(R.string.no_tag, title, sub_title, doc_name));

        }

    private void showHideView(View show, View hide) {
        show.setVisibility(View.VISIBLE);
        hide.setVisibility(View.GONE);
    }

    private void createRecyclerView() {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // specify an adapter (see also next example)
        tagAddAdapter = new TAGAddAdapter(this, tagList, this);
        recyclerView.setAdapter(tagAddAdapter);
    }
    private void updateTAGItem() {
        tag = new TblTAG();
        tag.setName(edtDocumentTitle.getText().toString().trim());
        try {
            tagInteractor.insert(tag);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        showHideView(recyclerView, rlNoTag);
//        tagList.add(tag);
//        tagAddAdapter.notifyDataSetChanged();
//        edtDocumentTitle.setText("");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
         if(item.getItemId()== R.id.m_search)
         {
             mSerachItem = item;

             return setUpSearchView();
         }
        return super.onOptionsItemSelected(item);
    }

    private boolean setUpSearchView() {
        MenuItemCompat.setOnActionExpandListener(mSerachItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Called when SearchView is collapsing
                if (mSerachItem.isActionViewExpanded()) {
                    animateSearchToolbar(1, false, false);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Called when SearchView is expanding
                animateSearchToolbar(1, true, true);
                return true;
            }
        });

        return true;
    }

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
//        mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.quantum_grey_600));

        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = toolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(toolbar,
                        isRtl(getResources()) ? toolbar.getWidth() - width : width, toolbar.getHeight() / 2, 0.0f, (float) width);
                createCircularReveal.setDuration(250);
                createCircularReveal.start();
            } else {
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-toolbar.getHeight()), 0.0f);
                translateAnimation.setDuration(220);
                toolbar.clearAnimation();
                toolbar.startAnimation(translateAnimation);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = toolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(toolbar,
                        isRtl(getResources()) ? toolbar.getWidth() - width : width, toolbar.getHeight() / 2, (float) width, 0.0f);
                createCircularReveal.setDuration(250);
                createCircularReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        toolbar.setBackgroundColor(getThemeColor(TAGActivity.this, R.attr.colorPrimary));
//                        mDrawerLayout.setStatusBarBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimaryDark));
                    }
                });
                createCircularReveal.start();
            } else {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-toolbar.getHeight()));
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(220);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toolbar.setBackgroundColor(getThemeColor(TAGActivity.this, R.attr.colorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                toolbar.startAnimation(animationSet);
            }
//            mDrawerLayout.setStatusBarBackgroundColor(ThemeUtils.getThemeColor(TAGActivity.this, R.attr.colorPrimaryDark));
        }
    }

    private boolean isRtl(Resources resources) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        }
        return false;
    }

    private static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                updateTAGItem();
                break;
        }
    }


    @Override
    public void onItemClicked(int position, TblTAG tag, boolean b) {
        tag.setSelected(b);
        tagAddAdapter.notifyDataSetChanged();

    }

    @Override
    public void onTagItemDelete(int position, TblTAG tag) {

    }
}
