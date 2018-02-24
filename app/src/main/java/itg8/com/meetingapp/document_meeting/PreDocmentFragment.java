package itg8.com.meetingapp.document_meeting;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.support.v7.widget.ShareActionProvider;
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
import itg8.com.meetingapp.meeting.MeetingDocumentAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreDocmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreDocmentFragment extends Fragment implements PreDocAdpater.ItemClickListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = PreDocmentFragment.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
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
    private MeetingDocumentAdapter adapter;
    private List<TblDocument> documents = new ArrayList<>();
    private ArrayList<TblDocument> mPreDocuments;


    public PreDocmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreDocmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreDocmentFragment newInstance(ArrayList<TblDocument> preDocument) {
        PreDocmentFragment fragment = new PreDocmentFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, preDocument);
        fragment.setArguments(args);
        return fragment;
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence title, String sub_title,
                                              CharSequence doc_name, String remaing) {
        Log.e(TAG, res.getString(R.string.no_document_details, title, sub_title, doc_name, remaing));
        return Html.fromHtml(res.getString(R.string.no_document_details, title, sub_title, doc_name, remaing));

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPreDocuments = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pre_docment, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (mPreDocuments.size() > 0) {
            showRecyclerView();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setAdapter(new PreDocAdpater(getActivity(), mPreDocuments, this));
        } else {
            hideRecyclerView();
            txtTitle.setText(formatPlaceDetails(getResources(), "Till Now Not Add Document To meeting", "Quickly add document to", "DocWallet", "to get access fast!!!!"));

        }

    }

    private void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
        rlNoDocItem.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        rlNoDocItem.setVisibility(View.GONE);

    }

    @NonNull
    private List<TblDocument> getTblDocuments() {


        TblDocument document = new TblDocument();
        List<TblDocument> list = new ArrayList<>();

        document.setFileName("DOC FILE");
        document.setFileExt(CommonMethod.EXT_DOC);
        list.add(document);

        document.setFileName("EXCEL FILE");
        document.setFileExt(CommonMethod.EXT_EXL);
        list.add(document);
        document.setFileName("PDF FILE");
        document.setFileExt(CommonMethod.EXT_PDF);
        list.add(document);
        document.setFileName("JPG FILE");
        document.setFileExt(CommonMethod.EXT_JPG);
        list.add(document);
        document.setFileName("TXT FILE");
        document.setFileExt(CommonMethod.EXT_TXT);
        list.add(document);
        document.setFileName("ZIP FILE");
        document.setFileExt(CommonMethod.EXT_ZIP);
        list.add(document);
        document.setFileName("PPT FILE");
        document.setFileExt(CommonMethod.EXT_PPT);
        list.add(document);
        try {
            return new DaoDocumentInteractor(getActivity()).getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
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
//        ((DocumentMeetingActivity) getActivity()).onPredocumentClick();

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
    public void onItemClickedShowListener(int position, TblDocument item) {
//        MimeTypeMap myMime = MimeTypeMap.getSingleton();
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

    @Override
    public void onItemClcikedListener(int position, final TblDocument items, ImageView img) {
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
//                 return true;
//            }
//        });

        popup.show(); //showing popup menu
    }

    private Uri generateUriFromFile(String fileActPath) {
        Log.d(TAG, "generateUriFromFile: FilePath:"+fileActPath);
        return Uri.fromFile(new File(fileActPath));
    }
}
