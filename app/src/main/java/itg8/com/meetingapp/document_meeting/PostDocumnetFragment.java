package itg8.com.meetingapp.document_meeting;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
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

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
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
    private ArrayList<TblDocument> postDocuments;
    private PreDocAdpater adapter;


    public PostDocumnetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PostDocumnetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostDocumnetFragment newInstance(ArrayList<TblDocument> postDocuments) {
        PostDocumnetFragment fragment = new PostDocumnetFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, postDocuments);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postDocuments = getArguments().getParcelableArrayList(ARG_PARAM1);
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
            adapter=new PreDocAdpater(getActivity(), postDocuments, this);
            recyclerView.setAdapter(adapter);
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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
//        ((DocumentMeetingActivity)getActivity()).onPostdocumentClick();
    }

    @Override
    public void sendItemToFragment(String note) {
        if (!TextUtils.isEmpty(note)) {
            lblNoteValue.setText(note);
        }

    }

    @Override
    public void addNewPostDocument(TblDocument document) {
        postDocuments.add(document);
        adapter.notifyDataSetChanged();
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



            default:
                break;
        }

        return false;
    }


    @Override
    public void onItemClcikedListener(int position, TblDocument items, ImageView img) {
        PopupMenu popup = new PopupMenu(getActivity(), img);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        MenuItem item = popup.getMenu().findItem(R.id.action_share);
        ShareActionProvider provider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareItem(getActivity(), " File Share of "+items.getMeeting().getTitle()+" Meeting...", items.getFileExt(), new File(items.getFileActPath()), provider);


        //registering popup with OnMenuItemClickListener
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(
//                        getActivity(),
//                        "You Clicked : " + item.getTitle(),
//                        Toast.LENGTH_SHORT
//                ).show();
//
//                shareItem(getActivity(), "TITLE", "BODY", null);
//
//                return true;
//            }
//        });

        popup.show(); //showing popup menu
    }

    public void shareItem(Context context, String title, String ext, File file, ShareActionProvider provider) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        ;
        sharingIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context,"itg8.com.meetingapp.fileprovider", file));
        Log.d(TAG, "shareItem: File Extension:"+ext);
        Log.d(TAG, "shareItem: File Name:"+title);
        sharingIntent.setType(CommonMethod.getMimetypeFromFilename("."+ext));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, title);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        provider.setShareIntent(sharingIntent);
        //context.startActivity(Intent.createChooser(sharingIntent, "Share"));


    }

    @Override
    public void onItemClickedShowListener(int position, TblDocument item) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
//        String mimeType = myMime.getMimeTypeFromExtension(item.getFileExt());


        newIntent.setDataAndType(FileProvider.getUriForFile(getActivity(),"itg8.com.meetingapp.fileprovider", new File(item.getFileActPath())),CommonMethod.getMimetypeFromFilename("."+item.getFileExt()));
        newIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            getActivity().startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }
}

