package itg8.com.meetingapp.document_meeting;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.db.DaoDocumentInteractor;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblDocument;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.meeting.MeetingDocumentAdapter;
import itg8.com.meetingapp.wallet_document.WalletActivity;

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MeetingDocumentAdapter adapter;
    private List<TblDocument> documents = new ArrayList<>();


    public PreDocmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreDocmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreDocmentFragment newInstance(String param1, String param2) {
        PreDocmentFragment fragment = new PreDocmentFragment();
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
        View view = inflater.inflate(R.layout.fragment_pre_docment, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new PreDocAdpater(getActivity(), getTblDocuments(), this));

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
        return new DaoDocumentInteractor(getActivity()).getAll();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:

                shareItem(getActivity(),"TITLE","BODY", null);

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
                shareItem(getActivity(),"TITLE","BODY", null);
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
}
