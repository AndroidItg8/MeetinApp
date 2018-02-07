package itg8.com.meetingapp.meeting;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.meetingapp.R;
import itg8.com.meetingapp.db.TblContact;
import itg8.com.meetingapp.db.TblTAG;
import itg8.com.meetingapp.meeting.model.Contact;

public class TAGActivity extends AppCompatActivity implements View.OnClickListener {

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
    private TblTAG tag;
    private TAGAddAdapter tagAddAdapter;

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
        btnAdd.setOnClickListener(this);
        createRecyclerView();


    }

    private void createRecyclerView() {
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // specify an adapter (see also next example)
        tagAddAdapter = new TAGAddAdapter(this, tagList);
        recyclerView.setAdapter(tagAddAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_add:
                updateTAGItem();
                break;
        }
    }

    private void updateTAGItem() {
        tag = new TblTAG();
        tag.setName(edtDocumentTitle.getText().toString().trim());
        tagList.add(tag);
        tagAddAdapter.notifyDataSetChanged();
        edtDocumentTitle.setText("");


    }
}
