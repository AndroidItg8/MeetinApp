package itg8.com.meetingapp.meeting;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.DocType;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.custom_tag.MaxHeightScrollView;
import itg8.com.meetingapp.custom_tag.TagContainerLayout;
import itg8.com.meetingapp.custom_tag.TagView;
import itg8.com.meetingapp.db.DaoContactInteractor;
import itg8.com.meetingapp.db.DaoDocumentInteractor;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.DaoMeetingTagInteractor;
import itg8.com.meetingapp.db.DaoTagInteractor;
import itg8.com.meetingapp.db.TblContact;
import itg8.com.meetingapp.db.TblDocument;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.db.TblMeetingTag;
import itg8.com.meetingapp.db.TblTAG;
import itg8.com.meetingapp.import_meeting.ParticipantTagAdapter;
import itg8.com.meetingapp.meeting.model.Contact;
import itg8.com.meetingapp.meeting.mvp.MeetingMVP;
import itg8.com.meetingapp.meeting.mvp.MeetingPresenterImp;
import itg8.com.meetingapp.service.NotificationService;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MeetingActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, MeetingDocumentAdapter.OnRecyclerviewItemClickedListener, MeetingMVP.MeetingView, TAGAdapter.TAGItemClickedListner, View.OnTouchListener {


    public static final int RC_STORAGE = 11;
    public static final int RC_CAMERAWITHSTORAGE = 12;
    public static final int REQUEST_TAKE_PHOTO = 30;
    public static final int READ_REQUEST_CODE = 34;
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_IMAGE_JPG = "image/jpeg";
    public static final String MIME_TYPE_IMAGE_PNG = "image/png";
    public static final String MIME_TYPE_PDF = "application/pdf";
    public static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    private static final boolean SHOW_DUE = true;
    private static final boolean HIDE_DUE = false;
    private static final boolean HIDE_REMINDER = false;
    private static final boolean SHOW_REMINDER = true;
    private static final boolean HIDE_PLACE = false;
    private static final boolean SHOW_PLACE = true;
    private static final boolean SHOW_DOCUMENT = true;
    private static final boolean HIDE_DOCUMENT = false;
    private static final int RC_LOCATION = 234;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 9001;
    private static final String TAG = MeetingActivity.class.getSimpleName();
    private static final int RC_CONCTACT = 13;
    private static final int RC_PHONE_BOOK = 900;
    private static final int RC_TAG = 800;
    private static final String DOCUMENT_LIST = "DOCUMENT_LIST";
    private static final String PARTICIPANT_LIST = "PARTICIPANT_LIST";
    private static final String AGENDA = "AGENDA";
    private static final String PLACE = "PLACE";
    private static final String ENDED_TIME = "ENDED_TIME";
    private static final String STARTED_TIME = "STARTED_TIME";
    private static final String DATE_VALUE = "DATE_VALUE";
    private static final String PRIPROTY = "PRIPROTY";
    private static final String TAG_LIST = "TAG_LIST";
    private static final int FROM_SAVE_INSTANCE = 2;
    private static final int FROM_ON_CREATE = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_agenda)
    EditText edtAgenda;
    @BindView(R.id.tag_container_layout)
    TagContainerLayout tagContainerLayout;


    @BindView(R.id.rl_tag_container)
    RelativeLayout rlTagContainer;
    @BindView(R.id.lbl_due_date)
    TextView lblDueDate;
    @BindView(R.id.lbl_lab)
    TextView lblLab;
    @BindView(R.id.img_time)
    ImageView imgTime;
    @BindView(R.id.edt_date)
    EditText edtDate;
    @BindView(R.id.img_end_time)
    ImageView imgEndTime;
    @BindView(R.id.edt_start_time)
    EditText edtStartTime;
    @BindView(R.id.edt_end_time)
    EditText edtEndTime;
    @BindView(R.id.rl_due)
    RelativeLayout rlDue;
    @BindView(R.id.img_due_cross)
    ImageView imgDueCross;
    @BindView(R.id.ll_due)
    LinearLayout llDue;
    @BindView(R.id.lbl_reminder)
    TextView lblReminder;
    @BindView(R.id.lbl_reminder_lbl)
    TextView lblReminderLbl;
    @BindView(R.id.txt_pri)
    TextView txtPri;
    @BindView(R.id.img_reminder_cross)
    ImageView imgReminderCross;
    @BindView(R.id.ll_reminder)
    LinearLayout llReminder;
    @BindView(R.id.lbl_place)
    TextView lblPlace;
    @BindView(R.id.img_place_cross)
    ImageView imgPlaceCross;
    @BindView(R.id.lbl_add_name)
    TextView lblAddName;
    @BindView(R.id.ll_place)
    LinearLayout llPlace;
    @BindView(R.id.lbl_image)
    TextView lblImage;
    @BindView(R.id.lbl_add_image)
    TextView lblAddImage;
    @BindView(R.id.img_doc_close)
    ImageView imgDocClose;
    @BindView(R.id.rvDocuments)
    RecyclerView rvDocuments;
    @BindView(R.id.ll_doc)
    LinearLayout llDoc;
    @BindView(R.id.lbl_participeint)
    TextView lblParticipeint;
    @BindView(R.id.lbl_add_person)
    TextView lblAddPerson;
    @BindView(R.id.img_phone_book)
    ImageView imgPhoneBook;
    @BindView(R.id.rl_participant)
    RelativeLayout rlParticipant;
    @BindView(R.id.tag_container_layout_participant)
    TagContainerLayout tagContainerLayoutParticipant;
    @BindView(R.id.scrollView)
    MaxHeightScrollView scrollView;
    @BindView(R.id.ll_add_person)
    LinearLayout llAddPerson;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    List<TblTAG> contacts = new ArrayList<TblTAG>();
    private String[] permissions;
    private boolean canAccessCamera;
    private boolean canAccessLocation;
    private boolean canStorage;
    private boolean canPhoneState;
    private GoogleApiClient mClient;
    private String[] value = {
            "Camera",
            "File"
    };
    private List<TblDocument> documents = new ArrayList<>();
    private MeetingDocumentAdapter adapter;
    private TblMeeting meeting = new TblMeeting();
    private MeetingMVP.MeetingPresenter presenter;
    private Calendar selectedDate = Calendar.getInstance();
    private Calendar selectedStartTime = Calendar.getInstance();
    private Calendar selectedEndTime = Calendar.getInstance();
    private String mCurrentPhotoPath;
    private List<TblContact> contactList = new ArrayList<>();
    private List<String> tagList = new ArrayList<>();
    private ParticipantTagAdapter adapterContact;
    private TAGAdapter adapterTAG;
    private TblTAG tagModel;
    private TAGAddAdapter tagAddAdapter;
    private DaoTagInteractor tagInteractor;
    private TblTAG tag;
    private DaoContactInteractor daoContact;
    private DaoDocumentInteractor daoDocument;
    private DaoMeetingInteractor daoMeeting;
    private DaoTagInteractor daoTag;
    private DaoMeetingTagInteractor daoMeetingTag;
    private boolean isFromEdit = false;
    //    private HashMap<String, TblTAG> tagHashMap = new HashMap<>();
    private TblMeetingTag meetingTag;
    private HashMap<String, TblContact> hashMap = new HashMap<>();
    private AlertDialog dialog;
    private String strName;
    private ArrayList<TblTAG> temTagList;
    private DaoContactInteractor contactInteractor;
    private EditText edtNumber;
    private EditText lblDocumentNote;
    private TblContact contact;
    private TextInputLayout inputLayout;
    private TextInputLayout inputLayoutNumber;


    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (documents != null) {
            outState.putParcelableArrayList(DOCUMENT_LIST, (ArrayList<? extends Parcelable>) documents);
        }
        if (tagContainerLayoutParticipant != null) {
            outState.putParcelableArrayList(PARTICIPANT_LIST, (ArrayList<? extends Parcelable>) contactList);
        }

        outState.putParcelableArrayList(TAG_LIST, (ArrayList<? extends Parcelable>) contacts);
        outState.putString(AGENDA, edtAgenda.getText().toString());
        outState.putString(DATE_VALUE, Helper.getStringDateFromCalander(selectedDate));
        outState.putString(STARTED_TIME, Helper.getStringDateFromCalander(selectedStartTime));
        outState.putString(ENDED_TIME, Helper.getStringDateFromCalander(selectedEndTime));
        outState.putString(PLACE, lblAddName.getText().toString());
        outState.putString(PRIPROTY, txtPri.getText().toString());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(null);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        collapsed();
//        adapterTAG = new TAGAdapter(tagList, this);

        presenter = new MeetingPresenterImp(this);
        mClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).build();
        contactInteractor = new DaoContactInteractor(MeetingActivity.this);
        createRecyclerviewForDocuments();
        initDaoInteractors();
        init();
        checkEveryPermissions();
        checkIfFromEdit();
        checkSaveInstanceState(savedInstanceState);

    }

    private void checkSaveInstanceState(Bundle outState) {
        if (outState != null) {
            Log.d(TAG, "onCreate: OnSAVE INSTANCE STATE");

            if (outState.getString(AGENDA) != null)
                edtAgenda.setText(outState.getString(AGENDA));
            if (outState.getString(PLACE) != null)
                lblAddName.setText(outState.getString(PLACE));
            if (outState.getString(PRIPROTY) != null) {
                txtPri.setText(outState.getString(PRIPROTY));
                hideShowView(lblReminderLbl, txtPri);
                imgPlaceCross.setVisibility(View.VISIBLE);
            }
            if (outState.getString(ENDED_TIME) != null && outState.getString(STARTED_TIME) != null) {
                if (!TextUtils.isEmpty(edtEndTime.getText().toString()) && !TextUtils.isEmpty(edtStartTime.getText().toString())) {
                    edtEndTime.setText((outState.getString(ENDED_TIME)));
                    edtStartTime.setText(outState.getString(STARTED_TIME));
                    hideShowView(lblLab, rlDue);
                }


            }
//            edtDate.setText(Helper.getDateFromDate(String.valueOf(outState.getString(DATE_VALUE))));
            if (rvDocuments != null) {
                documents.clear();
                if (outState.<TblDocument>getParcelableArrayList(DOCUMENT_LIST) != null) {
                    documents.addAll(outState.<TblDocument>getParcelableArrayList(DOCUMENT_LIST));
                    adapter.notifyDataSetChanged();
                }

            }
            if (tagContainerLayoutParticipant != null) {
                contactList.clear();
                contactList.addAll(outState.<TblContact>getParcelableArrayList(PARTICIPANT_LIST));
                Log.d(TAG, "checkSaveInstanceState: contactTemp:" + contactList.size());
                setParticipantTAG();
            }
            if (tagContainerLayout != null) {
                contacts = outState.getParcelableArrayList(TAG_LIST);
                createRecyclerViewForTAG(contacts);
            }


            //            list = savedInstanceState.getParcelableArrayList(Search_LIST);
//            try {
//                setTagViewsAndMeeting(tagList);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }


        }
    }

    private void initTagHashmap() {
//        try {
////            List<TblTAG> tagList=daoTag.getTags();
//            tagHashMap.clear();
//            for (String tag :
//                    this.tagList) {
//                tag.setSelected(true);
//                tagHashMap.put(tag.getPkid(), tag);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void checkIfFromEdit() {
        if (getIntent().hasExtra(CommonMethod.EXTRA_MEETING)) {
            long pkid = getIntent().getLongExtra(CommonMethod.EXTRA_MEETING, 0);
            if (pkid > 0) {
                try {
                    isFromEdit = true;
                    meeting = daoMeeting.getMeetingById(pkid);
                    setMeetingContent();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setMeetingContent() {
        edtAgenda.setText(meeting.getTitle());
        selectedEndTime.setTime(meeting.getEndTime());
        edtEndTime.setText(Helper.getStringTimeFromDate(selectedEndTime.getTime()));
//        selectedDate.setTime(Helper.getDateFromString(meeting.getDateOnly()));
        selectedDate.setTime((meeting.getDateOnly()));
        edtDate.setText(Helper.getDateFromDate(selectedDate.getTime()));

        selectedStartTime.setTime(meeting.getStartTime());
        edtStartTime.setText(Helper.getStringTimeFromDate(selectedStartTime.getTime()));
        expandDue();

        hideShowView(lblReminderLbl, txtPri);
        imgReminderCross.setVisibility(View.VISIBLE);
        txtPri.setText(Helper.getPriorityFromType(meeting.getPriority()));


        if (!TextUtils.isEmpty(meeting.getAddress())) {
            lblAddName.setText(meeting.getAddress());
//            hideShowView(lblAddPlace, rlAddress);
            imgPlaceCross.setVisibility(View.VISIBLE);
        }
        try {
            documents.addAll(daoDocument.getDocumentsByMeetingId(meeting.getPkid()));
            adapter.notifyDataSetChanged();
            if (documents.size() > 0) {
                toggleDocumentView(SHOW_DOCUMENT);
            }
            contactList.clear();
            setSelectedContactTag();
            contactList.addAll(daoContact.getContactsByMeetingId(meeting.getPkid()));
            Log.d(TAG, "setMeetingContent: contactTemp:" + contactList.size());

//            adapterContact.notifyDataSetChanged();
            setParticipantTAG();
            List<TblMeetingTag> tags = meeting.getTags();
            ArrayList<TblTAG> tempTagList = new ArrayList<>();
            for (TblMeetingTag tagMeeting :
                    tags) {
                tempTagList.add(new TblTAG(tagMeeting.getTag()));
                tagList.add(tagMeeting.getTag());
            }
//            tagList.addAll(daoMeetingTag.getTagsMyMeetingId(meeting));
//            changeHashmapValueFromUpdatedTagList();
            initTagHashmap();
            createRecyclerViewForTAG(tempTagList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        for (TblTAG tag :
//                tag) {
//
//        }

    }

    private void setSelectedContactTag() {
        try {
            for (TblContact contact : daoContact.getContactsByMeetingId(meeting.getPkid())) {
                contact.setSelected(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        {

        }

    }

    private void changeHashmapValueFromUpdatedTagList() {
//        for (TblTAG tag :
//                tagList) {
//            TblTAG tempTag = tagHashMap.get(tag.getPkid());
//            tempTag.setSelected(true);
//            tagHashMap.put(tag.getPkid(), tag);
//        }
    }

    private void expandDue() {
        toggleDueView(SHOW_DUE);
    }


    private void createRecyclerViewForTAG(List<TblTAG> contacts) {
        if (contacts.size() > 0) {
            int size = contacts.size();

            List<int[]> colors = new ArrayList<int[]>();
            for (int i = 0; i < size; i++) {
                int[] col1 = {Color.parseColor("#00000000"), Color.parseColor("#ffffff"), Color.parseColor("#ffffff")};
                colors.add(col1);
            }

            tagContainerLayout.setTags(contacts, colors, false);
        }


    }

    private void collapsed() {
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
//        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
//        toolbarLayout.setLayoutParams(params);
    }

    private void checkEveryPermissions() {
        canAccessLocation = hasLocationPermission();
        canPhoneState = hasContactPermission();
        canAccessCamera = hasCameraPermission();
        canStorage = hasStoragePermission();
    }

    private void createRecyclerviewForDocuments() {
        documents.add(null);
        adapter = new MeetingDocumentAdapter(documents, this);
        rvDocuments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        rvDocuments.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        mClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }


    private void init() {
        setDefaultInfo();
        setOnClickedListener();
        setParticipantTAG();

    }

    private void initDaoInteractors() {
        daoContact = new DaoContactInteractor(this);
        daoDocument = new DaoDocumentInteractor(this);
        daoMeeting = new DaoMeetingInteractor(this);
        daoTag = new DaoTagInteractor(this);
        daoMeetingTag = new DaoMeetingTagInteractor(this);
    }

    private void setDefaultInfo() {
        meeting.setPriority(2);
        txtPri.setText("Medium");
        edtDate.setText(Helper.getStringDateFromCalander(selectedDate));
        edtStartTime.setText(Helper.getStringTimeFromDate(selectedStartTime.getTime()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedStartTime.getTime());
        calendar.add(Calendar.HOUR, 1);
        selectedEndTime.setTime(calendar.getTime());
        edtEndTime.setText(Helper.getStringTimeFromDate(selectedEndTime.getTime()));
    }

    private void setOnClickedListener() {
        llDue.setOnClickListener(this);
        llReminder.setOnClickListener(this);
        llPlace.setOnClickListener(this);
        llDoc.setOnClickListener(this);
        imgDueCross.setOnClickListener(this);
        imgPlaceCross.setOnClickListener(this);
        imgReminderCross.setOnClickListener(this);
        llAddPerson.setOnClickListener(this);
        edtStartTime.setOnClickListener(this);
        edtEndTime.setOnClickListener(this);
        edtDate.setOnClickListener(this);
        rlParticipant.setOnClickListener(this);
        imgPhoneBook.setOnClickListener(this);
        fab.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem item = menu.findItem(R.id.action_save);
        SpannableString s = new SpannableString("SAVE");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                storeToDb();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void storeToDb() {
        if (!validateMeetingDetails()) {
            return;
        }
        boolean isInserted =
                getCompleteDetailForMeeting();
        if (isInserted) {
            presenter.saveMeeting(meeting);
            Toast.makeText(this, "Meeting stored successfully", Toast.LENGTH_SHORT).show();
            if (isFromEdit) {
                setResult(RESULT_OK);
                onBackPressed();
            } else
                onBackPressed();

        } else {
            Toast.makeText(this, "Fail to store meeting.", Toast.LENGTH_SHORT).show();
        }
//        DaoMeetingInteractor interactor = new DaoMeetingInteractor(MeetingActivity.this);
//        interactor.insert(meeting);

    }

    private boolean validateMeetingDetails() {
        if (TextUtils.isEmpty(edtAgenda.getText().toString())) {
            String error = "Please enter meeting agenda";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorWhite));
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(error);
            ssbuilder.setSpan(fgcspan, 0, error.length(), 0);
            edtAgenda.setError(ssbuilder);
            return false;
        }
        if (!isReminderDateTimeSet()) {
            toggleDueView(SHOW_DUE);
            showSnackbar("Set meeting date and time first");
            return false;
        } else if (!isPrioritySet()) {
            toggleReminderView(SHOW_REMINDER);
            showSnackbar("Set meeting priority");
            return false;
        }
        return true;
    }

    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(rlParticipant, text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private boolean getCompleteDetailForMeeting() {
        meeting.setTitle(edtAgenda.getText().toString());
        meeting.setDateOnly(selectedDate.getTime());
        meeting.setCreated(Calendar.getInstance().getTime());
        meeting.setStartTime(selectedStartTime.getTime());
        meeting.setEndTime(selectedEndTime.getTime());
        meeting.setNotified(CommonMethod.NOT_NOTIFIED);
        if (lblAddName.getText().toString().equalsIgnoreCase("Select place of meeting"))
            meeting.setAddress(null);
        else
            meeting.setAddress(lblAddName.getText().toString());

        try {
            if (isFromEdit) {
                daoMeeting.update(meeting);
            } else
                daoMeeting.insert(meeting);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (isFromEdit) {
            daoContact.deleteAll(meeting.getContacts());
        }
        for (TblContact contact : contactList) {
            contact.setSelected(true);
            contact.setMeeting(meeting);
            try {
                daoContact.insert(contact);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (isFromEdit)
            for (TblDocument document :
                    meeting.getDocuments()) {
                try {
                    daoDocument.delete(document);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        for (TblDocument document :
                documents) {
            if (document == null)
                continue;
            document.setMeeting(meeting);
            try {
                daoDocument.insert(document);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (isFromEdit) {
            try {
                daoMeetingTag.deleteAll(meeting.getTags());
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        for (String tag :
                tagList) {
            meetingTag = new TblMeetingTag();
            meetingTag.setMeeting(meeting);
            meetingTag.setTag(tag);
//            tag.setMeeting(meeting);
            try {
                daoMeetingTag.insert(meetingTag);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_reminder:
                toggleReminderView(SHOW_REMINDER);
                break;
            case R.id.ll_due:
                toggleDueView(SHOW_DUE);
                break;
            case R.id.ll_place:
                if (canAccessLocation)
                    togglePlaceView(SHOW_PLACE);
                else {
                    checkLocationPerm();

                }
                break;
            case R.id.ll_doc:
                toggleDocumentView(SHOW_DOCUMENT);
                break;
            case R.id.img_due_cross:
                toggleDueView(HIDE_DUE);
                break;
            case R.id.img_reminder_cross:
                toggleReminderView(HIDE_REMINDER);
                break;
            case R.id.edt_date:
                openDateCalender();
                break;
//            case R.id.edt_start_time:
//                break;
            case R.id.edt_end_time:
                OpenEndTimePicker();
                break;
            case R.id.edt_start_time:
                openStartTimePicker();
                break;
            case R.id.img_place_cross:
                lblAddName.setText("Select place of meeting");
                imgPlaceCross.setVisibility(View.GONE);
                break;
            case R.id.rl_participant:
                if (canPhoneState)
                    showDialogConatctBox();
                else {
                    checkContactPerm();
                }
                break;
            case R.id.img_phone_book:
                openPhoneBook2();
                break;
            case R.id.fab:
                initTagHashmap();
//                changeHashmapValueFromUpdatedTagList();
                Intent intent = new Intent(MeetingActivity.this, TAGActivity.class);
                intent.putStringArrayListExtra(CommonMethod.EXTRA_TAGS, (ArrayList<String>) tagList);
                startActivityForResult(intent, RC_TAG);
                break;

        }
    }

    private void openPhoneBook2() {
        if (canPhoneState) {
            startActivityForResult(new Intent(this, MultipleContactPickerActivity.class), RC_PHONE_BOOK);
        } else {
            checkContactPerm();
        }
    }

    private void openPhoneBook() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//        intent.setType();
        startActivityForResult(intent, RC_PHONE_BOOK);

    }

    private void showDialogConatctBox() {

        View mView = null;


        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MeetingActivity.this);
//        builderSingle.setIcon(R.drawable.ic_mode_edit);
        //  AlertDialog dialog = new AlertDialog(DocumentMeetingActivity.this);

        Button btnAdd = null;


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MeetingActivity.this);


        builderSingle.setTitle("Add Person:-");
        mView = layoutInflaterAndroid.inflate(R.layout.add_participant, null);
        edtNumber = (EditText) mView.findViewById(R.id.edt_person_number);
        lblDocumentNote = (EditText) mView.findViewById(R.id.edt_document_title);
        inputLayout = (TextInputLayout) mView.findViewById(R.id.input_layout_name);
        inputLayoutNumber = (TextInputLayout) mView.findViewById(R.id.input_layout_number);
        builderSingle.setCancelable(false);


//        else {
//            builderSingle.setTitle("Add TAG:-");
//            mView = layoutInflaterAndroid.inflate(R.layout.add_tag, null);
//            btnAdd = (Button) mView.findViewById(R.id.btn_add);
//            final RecyclerView recyclerViewTag = (RecyclerView) mView.findViewById(R.id.recyclerView);
//            createRecyclerView(recyclerViewTag);
//
//        }

//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
//        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_mode_edit_black_24dp);


        builderSingle.setView(mView);


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


//            btnAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (!TextUtils.isEmpty(finalLblDocumentNote1.getText())) {
//                        updateTAGItem(lblDocumentNote1.getText().toString().trim(), lblDocumentNote);
//                    }
//
//                }
//            });


        builderSingle.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        dialog = builderSingle.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPersonFromDialog();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void addPersonFromDialog() {
        contact = new TblContact();
        if (!TextUtils.isEmpty(lblDocumentNote.getText())) {
            contact.setName(lblDocumentNote.getText().toString().trim());
            if (TextUtils.isEmpty(edtNumber.getText())) {
                contact.setNumber("NOT AVAILABLE");
                insertContactNotExits(contact);
//                adapterContact.notifyDataSetChanged();
                AddContactToPhoneContactList(lblDocumentNote.getText().toString().trim(), edtNumber.getText().toString().trim());
                dialog.dismiss();
            } else {
                if (edtNumber.getText().toString().trim().length() == 10) {

                    contact.setName(lblDocumentNote.getText().toString().trim());
                    contact.setNumber(edtNumber.getText().toString().trim());
                    contact.setSelected(true);
                    insertContactNotExits(contact);

                    setParticipantTAG();

//                    adapterContact.notifyDataSetChanged();
                    AddContactToPhoneContactList(lblDocumentNote.getText().toString().trim(), edtNumber.getText().toString().trim());
                    dialog.dismiss();
                } else {

                    inputLayoutNumber.setError("Add 10 digit Number");
                }
            }


        } else {
            inputLayout.setEnabled(true);
            inputLayout.setError("Add Person Name");


        }
    }

    private void AddContactToPhoneContactList(String name, String number) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (name != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            name).build());
        }

        //------------------------------------------------------ Mobile Number
        if (number != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }


        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(MeetingActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


//
//    private void createRecyclerView(RecyclerView recyclerViewTag) {
//
//        recyclerViewTag.setHasFixedSize(true);
//
//        // use a linear layout manager
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerViewTag.setLayoutManager(linearLayoutManager);
//
//        // specify an adapter (see also next example)
//        tagAddAdapter = new TAGAddAdapter(this, tagList, this);
//        recyclerViewTag.setAdapter(tagAddAdapter);
//    }
//
//    private void updateTAGItem(String value, EditText lblDocumentNote) {
//        tag = new TblTAG();
//        tag.setName(value);
//        //     tagInteractor.insert(tag);
//
//        tagList.add(tag);
//        tagAddAdapter.notifyDataSetChanged();
//        lblDocumentNote.setText("");
//
//
//    }


    private void openStartTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                selectedStartTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                selectedStartTime.set(Calendar.MINUTE, selectedMinute);
                if (selectedStartTime.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                    showSnackbar("Past time cannot allowed!!!");
                    return;
                }
                edtStartTime.setText(Helper.getStringTimeFromDate(selectedStartTime.getTime()));
                meeting.setStartTime(selectedStartTime.getTime());
                if ((selectedEndTime.getTimeInMillis() - selectedStartTime.getTimeInMillis()) < 60000) {
                    selectedEndTime.set(Calendar.HOUR_OF_DAY, selectedHour + 1);
                    selectedEndTime.set(Calendar.MINUTE, selectedMinute);
                    edtEndTime.setText(Helper.getStringTimeFromDate(selectedEndTime.getTime()));
                }
                meeting.setEndTime(selectedEndTime.getTime());
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void OpenEndTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        mcurrentTime.setTime(selectedEndTime.getTime());
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (selectedStartTime.getTimeInMillis() > selectedEndTime.getTimeInMillis()) {
                    showSnackbar("End time should be greater than Start time");
                    return;
                }
                selectedEndTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                selectedEndTime.set(Calendar.MINUTE, selectedMinute);
                edtEndTime.setText(Helper.getStringTimeFromDate(selectedEndTime.getTime()));

            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    private void openDateCalender() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MeetingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.d(TAG, "onDateSet: " + Calendar.getInstance().getTimeInMillis() + " - " + calendar.getTimeInMillis());
                if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                    showSnackbar("Date cannot be less than today");
                    return;
                }
                setYearInAllCalander(year);
                setMonthInAllCalander(month);
                setDateInAllCalander(dayOfMonth);
                String getSelectedDateFromCalander = Helper.getStringDateFromCalander(selectedDate);
                edtDate.setText(getSelectedDateFromCalander);
            }
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("SELECT DATE");
        datePickerDialog.show();
    }

    private void setDateInAllCalander(int dayOfMonth) {
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedStartTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedEndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private void setMonthInAllCalander(int month) {
        selectedDate.set(Calendar.MONTH, month);
        selectedStartTime.set(Calendar.MONTH, month);
        selectedEndTime.set(Calendar.MONTH, month);
    }

    private void setYearInAllCalander(int year) {
        selectedDate.set(Calendar.YEAR, year);
        selectedStartTime.set(Calendar.YEAR, year);
        selectedEndTime.set(Calendar.YEAR, year);
    }

    private void toggleDocumentView(boolean showDocument) {
//        if (showDocument) {
//            hideShowView(lblAddPlace, rlAddress);
//
//
//        } else {
//            hideShowView(lblAddPlace, rlAddress);
//
//        }
    }

    private void togglePlaceView(boolean showPlace) {
//        startActivity(new Intent(getApplicationContext(), NearByLocationActivity.class));
        OpenGooglePlaces();
        if (showPlace) {
//            hideShowView(lblAddPlace, rlAddress);
//            imgPlaceCross.setVisibility(View.VISIBLE);
            lblAddName.setVisibility(View.VISIBLE);


        } else {
//            hideShowView(rlAddress, lblAddPlace);
            imgPlaceCross.setVisibility(View.GONE);
            lblAddName.setVisibility(View.VISIBLE);
            lblAddName.setText("Select place of meeting");

        }
    }

    private void OpenGooglePlaces() {

        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

//            Log.e(TAG, message);
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());
                showPlaceCross();
                // Format the place's details and display them in the TextView.
//                lblAddName.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
                meeting.setLatitude(place.getLatLng().latitude);
                meeting.setLongitude(place.getLatLng().longitude);
                meeting.setAddress(String.valueOf(place.getAddress()));
                lblAddName.setText(place.getAddress());
                imgPlaceCross.setVisibility(View.VISIBLE);


                // Display attributions if required.
//                CharSequence attributions = place.getAttributions();
//                if (!TextUtils.isEmpty(attributions)) {
//                    lblAddName.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    lblAddName.setText("No place selected");
//                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                createDocumentFile(mCurrentPhotoPath);
            }
        } else if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // The document selected by the user won't be returned in the intent.
                // Instead, a URI to that document will be contained in the return intent
                // provided to this method as a parameter.
                // Pull that URI using resultData.getData().
                Uri uri = null;

                if (data != null) {
                    uri = data.getData();
                    assert uri != null;
                    Log.i(TAG, "Uri: " + uri.toString());
                    String selectedMimeType = CommonMethod.getMimetypeFromUri(uri, getContentResolver());
                    Log.d(TAG, "SelectedMimeType:" + selectedMimeType);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String newFileName = timeStamp + CommonMethod.getFilenameFromMimetype(selectedMimeType);
//                        showImage(uri);
//                        DocumentFile file;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            if (DocumentsContract.isDocumentUri(this, uri)) {
//                                file = DocumentFile.fromSingleUri(this, uri);
//                            } else {
//                                file = DocumentFile.fromTreeUri(this, uri);
//                            }
//                        }
//                        file.get
                    InputStream in = null;
                    try {
                        /** Here we keep it all in a try-catch in case, so we don't
                         * force-close if something doesn't go to plan.
                         *
                         * This finds the location of the device's local storage (don't
                         * assume that this will be /sdcard/!), and appends a hard-
                         *  coded string with a new subfolder, and gives the file that
                         *  we are going to create a name.
                         *
                         *  Note: You'll want to replace 'gdrive_image.jpg' with the
                         *  filename that you fetch from Drive if you want to preserve
                         *  the filename. That's out of the scope of this post. */


                        Log.d(TAG, "onActivityResult: FileName:" + newFileName);
                        String output_path = createDocumentTempFile(newFileName).getAbsolutePath();

                        // Create the file in the location that we just defined.
                        File oFile = new File(output_path);

                        /**   Create the file if it doesn't exist; be aware that if it
                         * does, we'll be overwriting it further down. */
                        if (!oFile.exists()) {
                            /**   Note that this isn't just mkdirs; that would make our
                             * file into a directory! The 'getParentFile()' bit ensures
                             * that the tail end remains a File. */
                            oFile.getParentFile().mkdirs();
                            oFile.createNewFile();
                        }

                        /**   The 'getActivity()' bit assumes that this is being run from
                         * within a Fragment, which it is of course. You wouldn't be
                         * working outside of current Android good practice would
                         * you?... */
                        InputStream iStream = getContentResolver()
                                .openInputStream(uri);

                        /**   Create a byte array to hold the content that exists at the
                         * Uri we're interested in; this preserves all of the data that
                         * exists within the file, including any JPEG meta data. If
                         * you punt this straight to a Bitmap object, you'll lose all
                         * of that.
                         *
                         * Note: This is reallt the main point of this entire post, as
                         * you're getting ALL OF THE DATA from the source file, as
                         * is. */
                        byte[] inputData = getBytes(iStream);

                        writeFile(inputData, output_path);
                        createDocumentFile(oFile.getAbsolutePath());


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else if (requestCode == RC_PHONE_BOOK) {
//            getContactDetail(resultCode, data);
            if (resultCode == RESULT_OK) {

                ArrayList<Contact> contacts = new ArrayList<Contact>();
                contacts = data.getParcelableArrayListExtra("contacts");


                for (Contact c : contacts) {
                    Log.d("Selected contact = ", c.getEmail());
                    contact = new TblContact();
                    contact.setName(c.getName());
                    contact.setNumber(c.getNumber());
                    contact.setSelected(true);
                    insertContactNotExits(contact);


//                    adapterContact.notifyItemInserted(contactList.size()-1);
                }

                setParticipantTAG();

            }
        } else if (requestCode == RC_TAG) {
//            getContactDetail(resultCode, data);
            if (resultCode == RESULT_OK) {


                tagList.clear();

                contacts = data.getParcelableArrayListExtra("tag");
                Log.d(TAG, "onActivityResult: TAGSLIST SIZE" + contacts.size());

                int itemCountOld = tagList.size();

                for (TblTAG c : contacts) {
                    tagList.add(c.getName());
                }
                //initTagHashmap();
//                changeHashmapValueFromUpdatedTagList();
//                contacts.clear();
                Log.d(TAG, "onActivityResult: TAGSLIST SIZE" + tagList.size());
                createRecyclerViewForTAG(contacts);

            }
        }
    }

    private void getContactDetail(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Cursor cursor = null;
            try {
                String phoneNo = null;
                String name = null;
                // getData() method will have the Content Uri of the selected contact
                Uri uri = data.getData();
                //Query the content uri
                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                // column index of the phone number
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                // column index of the contact name
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                phoneNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);
                // Set the value to the textviews

            } catch (Exception e) {
                e.printStackTrace();
            }
            // column index of the phone number


        }

    }

    public void insertContactNotExits(TblContact tbl) {
        if (!hashMap.containsKey(tbl.getName())) {
            contactList.add(tbl);
            hashMap.put(tbl.getName(), tbl);
        }


    }

    public void setParticipantTAG() {
        if (contactList.size() > 0) {
            int size = contactList.size();
            List<int[]> colors = new ArrayList<int[]>();
            for (int i = 0; i < size; i++) {
                int randomColor = new Random().nextInt(R.array.androidcolors);

                int[] col1 = {Color.parseColor("#ffffff"), Color.parseColor("#C1562E"), Color.parseColor("#C1562E")};
                colors.add(col1);
            }
//            tagContainerLayoutParticipant.setEnableCross(true);
            tagContainerLayoutParticipant.setCrossColor(Color.parseColor("#C1562E"));
            tagContainerLayoutParticipant.setParticipantTags(contactList, colors);
            tagContainerLayoutParticipant.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, Object object) {
//                try {
////                    TblContact text= (TblContact) object;
////                    text.setSelected(!text.isSelected());
////                    contactInteractor.update(text);
////                    tagContainerLayoutParticipant.changeSelectColor(position, text);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }

                }

                @Override
                public void onTagLongClick(int position, Object text) {

                }

                @Override
                public void onTagCrossClick(int position, Object object) {
                    TblContact test = (TblContact) object;
                    if (test.isSelected()) {
                        tagContainerLayoutParticipant.removeTag(position);
                        contactList.remove(position);
                        test.setSelected(false);
//                    tagContainerLayoutParticipant.changeSelectColor(position, test);

                    }

                }
            });
        }

    }

    private void createDocumentFile(String mCurrentPhotoPath) {
        TblDocument document = new TblDocument();
        document.setFileActPath(mCurrentPhotoPath);
        document.setFileName(new File(mCurrentPhotoPath).getName());
        document.setFileExt(Helper.getFileExtFromName(document.getFileName()));
        document.setMeeting(meeting);
        documents.add(document);
        adapter.notifyDataSetChanged();

    }

    /**
     * This function rewrites the byte array to the provided filename.
     * <p>
     * Note: A String, NOT a file object, though you could easily tweak it to do
     * that.
     */
    public void writeFile(byte[] data, String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
    }

    /**
     * This function puts everything in the provided InputStream into a byte array
     * and returns it to the calling function.
     */
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

//


    private void showPlaceCross() {
        imgPlaceCross.setVisibility(View.VISIBLE);
    }

    private void showFailToGetAddress() {
        Toast.makeText(this, "Fail to get Map Result", Toast.LENGTH_SHORT).show();
    }

    private void toggleReminderView(boolean showReminder) {
        if (showReminder) {
            OpenCustomDialogueFragment();
            hideShowView(lblReminderLbl, txtPri);
            imgReminderCross.setVisibility(View.VISIBLE);

        } else {
            hideShowView(txtPri, lblReminderLbl);
            imgReminderCross.setVisibility(View.GONE);


        }
    }

    private void OpenCustomDialogueFragment() {

        showDialogBox();
//
//
//
//
//        CustomPrioritesFragment dialog = new CustomPrioritesFragment();
//        dialog.show(getSupportFragmentManager(), "YourDialog");

    }

    private void toggleDueView(boolean showDue) {
        if (showDue) {
            hideShowView(lblLab, rlDue);
            imgDueCross.setVisibility(View.VISIBLE);


        } else {
            hideShowView(rlDue, lblLab);
            imgDueCross.setVisibility(View.GONE);

        }

    }

    /**
     * check if reminder date set or not
     */
    private boolean isReminderDateTimeSet() {
        return rlDue.getVisibility() == View.VISIBLE;
    }

    /**
     * check if priority set or not
     */
    private boolean isPrioritySet() {
        return txtPri.getVisibility() == View.VISIBLE;
    }


    private void showDialogBox() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MeetingActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select Priority:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MeetingActivity.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("High");
        arrayAdapter.add("Medium");
        arrayAdapter.add("Low");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strName = arrayAdapter.getItem(which);
                txtPri.setText(strName);
                meeting.setPriority(Helper.getTypeFromPriorityText(strName));

            }
        });
        builderSingle.show();
    }


    private void hideShowView(View hide, View show) {
        hide.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
    }

    @AfterPermissionGranted(RC_STORAGE)
    private void checkStoragePerm() {
        permissions = getStoragePermission();
        if (!hasStoragePermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_no_permission), RC_STORAGE, permissions);
        }
    }

    private String[] getStoragePermission() {
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    }

    private boolean hasStoragePermission() {
        if (EasyPermissions.hasPermissions(this, getStoragePermission())) {
            canStorage = true;
        }
        return canStorage;
    }

    @AfterPermissionGranted(RC_CAMERAWITHSTORAGE)
    private void checkCameraPerm() {
        permissions = getCameraPermission();
        if (!hasCameraPermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_no_permission), RC_CAMERAWITHSTORAGE, permissions);
        }
    }

    private boolean hasCameraPermission() {
        if (EasyPermissions.hasPermissions(this, getCameraPermission()))
            canAccessCamera = true;
        return canAccessCamera;
    }

    @NonNull
    private String[] getCameraPermission() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    }


    @AfterPermissionGranted(RC_CONCTACT)
    private void checkContactPerm() {
        permissions = getContactPermission();
        if (!hasContactPermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_no_permission), RC_CONCTACT, permissions);
        }
    }

    private boolean hasContactPermission() {
        if (EasyPermissions.hasPermissions(this, getContactPermission()))
            canPhoneState = true;
        return canPhoneState;
    }

    @NonNull
    private String[] getContactPermission() {
        return new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void checkLocationPerm() {
        permissions = getLocationPermission();
        if (!hasLocationPermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_no_permission), RC_LOCATION, permissions);
        }
    }

    private boolean hasLocationPermission() {
        if (EasyPermissions.hasPermissions(this, getLocationPermission()))
            canAccessLocation = true;
        return canAccessLocation;
    }

    @NonNull
    private String[] getLocationPermission() {
        return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        checkPrems(perms, true);
    }

    private void checkPrems(List<String> perms, boolean isGranted) {
        if (perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)
                || perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            canStorage = isGranted;
            if (isGranted && Arrays.equals(permissions, getStoragePermission())) showFileManager();
        }
        if (perms.contains(Manifest.permission.ACCESS_COARSE_LOCATION) || perms.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
            canAccessLocation = isGranted;
            if (canAccessLocation) togglePlaceView(SHOW_PLACE);


        }
        if (perms.contains(Manifest.permission.CAMERA)) {
            canAccessCamera = isGranted;

            if (isGranted && Arrays.equals(permissions, getCameraPermission())) {
                Log.d(TAG, "checkPrems: ShowCamera");
                showCamera();
            }


        }
        if (perms.contains(Manifest.permission.READ_CONTACTS)) {
            canPhoneState = isGranted;
            if (canPhoneState) openPhoneBook2();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        checkPrems(perms, false);
    }

    @Override
    public void onNewEntryClicked() {
        showDialogToAddDocument();
    }

    private void showDialogToAddDocument() {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(MeetingActivity.this);
        alertdialogbuilder.setTitle("Select Document Type ");
        alertdialogbuilder.setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (canAccessCamera)
                        showCamera();
                    else {
                        checkCameraPerm();

                    }
                } else {
                    if (canStorage)
                        showFileManager();
                    else
                        checkStoragePerm();
                }
            }
        });

        AlertDialog dialog = alertdialogbuilder.create();

        dialog.show();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "createImageFile: ImageFIe:" + mCurrentPhotoPath);

        return image;
    }

    private File createDocumentTempFile(String fileName) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = fileName.split("\\.(?=[^\\.]+$)")[0];
        String ext = "." + fileName.split("\\.(?=[^\\.]+$)")[1];
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ext,         /* suffix */
                storageDir      /* directory */
        );
        Log.d(TAG, "createDocumentTempFile: DocumentFile:" + image.getAbsolutePath());
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }


    private void showCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "itg8.com.meetingapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void showFileManager() {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{MIME_TYPE_IMAGE, MIME_TYPE_PDF, MIME_TYPE_TEXT_PLAIN});
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onDeleteDocumentClicked(int position, TblDocument document) {
        documents.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onPreviewClicked(int position, TblDocument document) {
        showPreviewOfDocument(document);
    }

    private void showPreviewOfDocument(TblDocument document) {
//        DocType type = Helper.getDocumentExtention(document.getFileName());
        openDocumentWithType(document );
    }

    private void openDocumentWithType(TblDocument type) {

        viewDocumentFromEdit( type);

    }

    private void viewDocumentFromEdit(TblDocument document) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setDataAndType(FileProvider.getUriForFile(MeetingActivity.this, "itg8.com.meetingapp.fileprovider", new File(document.getFileActPath())), CommonMethod.getMimetypeFromFilename("." + document.getFileExt()));
        newIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            MeetingActivity.this.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MeetingActivity.this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void startServiceToSetAlarm(long diffByPriority, TblMeeting meeting) {
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING, meeting);
        long timeDifference = Helper.getTimeDifferenceFromCurrent(meeting.getStartTime());
        if (timeDifference > 1) {
            if (diffByPriority > 0) {
                intent.putExtra(CommonMethod.EXTRA_MEETING_TIME_DIFF, diffByPriority);
            }
        }
        startService(intent);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemAddTagClicked(int position, TblTAG item) {
//        adapterTAG.notifyItemRemoved(position);
//        adapterTAG.notifyDataSetChanged();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Disallow ScrollView to intercept touch events.
                v.getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_UP:
                // Allow ScrollView to intercept touch events.
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        // Handle ScrollView touch events.
        v.onTouchEvent(event);
        return false;
    }


}
