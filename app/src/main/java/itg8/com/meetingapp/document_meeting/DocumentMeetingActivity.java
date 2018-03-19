package itg8.com.meetingapp.document_meeting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.db.DaoDocumentInteractor;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblDocument;
import itg8.com.meetingapp.db.TblMeeting;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static itg8.com.meetingapp.meeting.MeetingActivity.MIME_TYPE_IMAGE;
import static itg8.com.meetingapp.meeting.MeetingActivity.MIME_TYPE_PDF;
import static itg8.com.meetingapp.meeting.MeetingActivity.MIME_TYPE_TEXT_PLAIN;
import static itg8.com.meetingapp.meeting.MeetingActivity.RC_CAMERAWITHSTORAGE;
import static itg8.com.meetingapp.meeting.MeetingActivity.RC_STORAGE;
import static itg8.com.meetingapp.meeting.MeetingActivity.READ_REQUEST_CODE;
import static itg8.com.meetingapp.meeting.MeetingActivity.REQUEST_TAKE_PHOTO;

public class DocumentMeetingActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "DocumentMeetingActivity";
    public String[] value = {
            "Camera",
            "File"
    };
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
    NoteItemListener listener;
    private ArrayList<TblDocument> documentPreList;
    private ArrayList<TblDocument> documentPostList;
    private boolean canAccessCamera;
    private boolean canStorage;
    private String[] permissions;
    private String mCurrentPhotoPath;
    private DaoMeetingInteractor daoMeeting;
    private TblMeeting meeting;
    private DaoDocumentInteractor daoDocument;
    private boolean isInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_meeting);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Meeting Documents");
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(this);
        daoMeeting = new DaoMeetingInteractor(this);
        daoDocument = new DaoDocumentInteractor(this);
        init();
        setupViewPager(viewPager);
        checkPermissions();
        tabLayout.setupWithViewPager(viewPager);
        onPredocumentClick();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: position:" + position + " positionOffset:" + positionOffset);
//                if(position==0){
//                    onPredocumentClick();
//                }else {
//                    onPostdocumentClick();
//                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: ");
                if (position == 0) {
                    onPredocumentClick();
                } else {
                    onPostdocumentClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void checkPermissions() {
        canAccessCamera = hasCameraPermission();
        canStorage = hasStoragePermission();
    }

    private boolean hasCameraPermission() {
        if (EasyPermissions.hasPermissions(this, getCameraPermission()))
            canAccessCamera = true;
        return canAccessCamera;
    }

    @AfterPermissionGranted(RC_CAMERAWITHSTORAGE)
    private void checkCameraPerm() {
        permissions = getCameraPermission();
        if (!hasCameraPermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_no_permission), RC_CAMERAWITHSTORAGE, permissions);
        }
    }

    private boolean hasStoragePermission() {
        if (EasyPermissions.hasPermissions(this, getStoragePermission())) {
            canStorage = true;
        }
        return canStorage;
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

    @NonNull
    private String[] getCameraPermission() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void init() {
        if (getIntent().hasExtra(CommonMethod.EXTRA_PRE_DOCUMENTS)) {
            long meetingId = getIntent().getLongExtra(CommonMethod.EXTRA_MEETING, 0);
            isInProgress = getIntent().getBooleanExtra(CommonMethod.EXTRA_PROGRESS, false);
            if (meetingId > 0) {
                try {
                    meeting = daoMeeting.getMeetingById(meetingId);
                    documentPreList = (ArrayList<TblDocument>) daoDocument.getPreDocumentsByMeetingId(meetingId);
                    documentPostList = (ArrayList<TblDocument>) daoDocument.getPostDocumentsByMeetingId(meetingId);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
//            documentPreList=getIntent().getParcelableArrayListExtra(CommonMethod.EXTRA_PRE_DOCUMENTS);
//            documentPostList=getIntent().getParcelableArrayListExtra(CommonMethod.EXTRA_POST_DOCUMENTS);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PreDocmentFragment.newInstance(documentPreList), "Pre Meeting");
        adapter.addFragment(PostDocumnetFragment.newInstance(documentPostList, isInProgress), "Post Meeting");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
//                showDialogBox();
                showDialogToAddDocument();
        }
    }

    public void showDialogBox(final NoteItemListener listener) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(DocumentMeetingActivity.this);
        builderSingle.setIcon(R.drawable.ic_mode_edit);

        builderSingle.setTitle("Add Note:-");
//
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(DocumentMeetingActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.add_note_meeting, null);
        builderSingle.setView(mView);
        builderSingle.setCancelable(false);
        final EditText edtDocumentNote = (EditText) mView.findViewById(R.id.edt_document_title);
        final TextInputLayout inputLayout = (TextInputLayout) mView.findViewById(R.id.input_layout_name);

        if (!TextUtils.isEmpty(meeting.getNote())) {
            edtDocumentNote.setText(meeting.getNote());
        } else {
            edtDocumentNote.setText("");
        }
//


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {

            }
        });
        builderSingle.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {



            }
        });
        final AlertDialog dialog = builderSingle.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(edtDocumentNote.getText())) {
            String lblNote = edtDocumentNote.getText().toString().trim();
            meeting.setNote(lblNote);
            updateMeetingTable();
            Log.d("TAG", "lblNote:" + lblNote);
            if (listener != null)
                listener.sendItemToFragment(lblNote, meeting.getPkid());
            dialog.dismiss();
        } else {

            inputLayout.setError("Enter note regarding meeting");


        }
    }
});

    }

    private void updateMeetingTable() {
        try {
            daoMeeting.update(meeting);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    public void onPredocumentClick() {
        fab.setVisibility(View.GONE);
    }

    public void onPostdocumentClick() {
        if (isInProgress)
            fab.setVisibility(View.GONE);
        else
            fab.setVisibility(View.VISIBLE);
    }

    private void showDialogToAddDocument() {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(DocumentMeetingActivity.this);
        alertdialogbuilder.setTitle("Select Document Type ");
        alertdialogbuilder.setItems(value, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (canAccessCamera)
                        showCamera();
                    else
                        checkCameraPerm();
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

    private void checkPrems(List<String> perms, boolean isGranted) {
        if (perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)
                || perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            canStorage = isGranted;
        }

        if (perms.contains(Manifest.permission.CAMERA)) {
            canAccessCamera = isGranted;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                createDocumentFile(mCurrentPhotoPath);
            }
        } else if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // The document selected by the user won't be returned in the intent.
                // Instead, a URI to that document will be contained in the return intent
                // provided to this method as a parameter.
                // Pull that URI using resultData.getData().

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
        }
    }


    private File createDocumentTempFile(String fileName) throws IOException {
        // Create an image file name

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


    public void writeFile(byte[] data, String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
    }

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

    public void setListener(NoteItemListener listener) {
        this.listener = listener;
    }

    private void createDocumentFile(String mCurrentPhotoPath) {
        if (meeting == null) {
            throw new NullPointerException("Meeting is null. cannot create document");
        }
        TblDocument document = new TblDocument();
        document.setFileActPath(mCurrentPhotoPath);
        document.setFileName(new File(mCurrentPhotoPath).getName());
        document.setFileExt(Helper.getFileExtFromName(document.getFileName()));
        document.setType(CommonMethod.TYPE_POST_MEETING);
        document.setMeeting(meeting);
        try {
            daoDocument.insert(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (listener != null)
            listener.addNewPostDocument(document);
//        documents.add(document);
//        adapter.notifyDataSetChanged();

    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        checkPrems(perms, true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        checkPrems(perms, false);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public interface NoteItemListener {
        void sendItemToFragment(String note, long pkid);

        void addNewPostDocument(TblDocument document);
    }
}






