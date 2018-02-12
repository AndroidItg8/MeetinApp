package itg8.com.meetingapp.document_meeting;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.db.DaoDocumentInteractor;
import itg8.com.meetingapp.db.TblDocument;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDocumnetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDocumnetFragment extends Fragment implements DocumentMeetingActivity.NoteItemListener, PreDocAdpater.ItemClickListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = PostDocumnetFragment.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.lbl_note)
    TextView lblNote;
    @BindView(R.id.lbl_note_value)
    TextView lblNoteValue;
    @BindView(R.id.cardView)
    CardView cardView;
    DocumentMeetingActivity.NoteItemListener listener;
    @BindView(R.id.rl_recyclerView)
    RelativeLayout rlRecyclerView;
    @BindView(R.id.img_no_meeting)
    ImageView imgNoMeeting;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_sub_title)
    TextView txtSubTitle;
    @BindView(R.id.rl_no_doc_item)
    RelativeLayout rlNoDocItem;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;


    public PostDocumnetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostDocumnetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostDocumnetFragment newInstance(String param1, String param2) {
        PostDocumnetFragment fragment = new PostDocumnetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_documnet, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        if (getTblDocuments().size() > 0) {
            showRecyclerView();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setAdapter(new PreDocAdpater(getActivity(), getTblDocuments(), this));
        } else {
            hideRecyclerView();
            txtTitle.setText(formatPlaceDetails(getResources(), "Till Now Not Add Document To meeting", "Quickly add document to", "DocWallet", "to get access fast!!!!"));

        }
    }

    private void hideRecyclerView() {
        rlRecyclerView.setVisibility(View.GONE);
        rlNoDocItem.setVisibility(View.VISIBLE);
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence title, String sub_title,
                                              CharSequence doc_name, String remaing) {
        Log.e(TAG, res.getString(R.string.no_document_details, title, sub_title, doc_name, remaing));
        return Html.fromHtml(res.getString(R.string.no_document_details, title, sub_title, doc_name, remaing));

    }

    private void showRecyclerView() {
        rlRecyclerView.setVisibility(View.VISIBLE);
        rlNoDocItem.setVisibility(View.GONE);
    }

    @NonNull
    private List<TblDocument> getTblDocuments() {
//        TblDocument document = new TblDocument();
//        List<TblDocument> list = new ArrayList<>();
//        document.setFileName("PPT FILE");
//        document.setFileExt(CommonMethod.EXT_PPT);
//        list.add(document);
        try {
            return  new DaoDocumentInteractor(getActivity()).getAll();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void sendItemToFragment(String note) {
        if (!TextUtils.isEmpty(note)) {
            lblNoteValue.setText(note);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        ((DocumentMeetingActivity) context).listener = (DocumentMeetingActivity.NoteItemListener) this;

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:

                shareItem(getActivity(), "TITLE", "BODY", null);

                return true;


            default:
                break;
        }

        return false;
    }


    @Override
    public void onItemClcikedListener(int position, TblDocument item, ImageView img) {
        PopupMenu popup = new PopupMenu(getActivity(), img);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        getActivity(),
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();

                shareItem(getActivity(), "TITLE", "BODY", null);

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

        } else {
            sharingIntent.setType("text/plain");
        }
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, body);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(sharingIntent, "Share"));


    }
}

