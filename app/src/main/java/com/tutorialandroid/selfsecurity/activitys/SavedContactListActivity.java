package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.adapter.ContactsAdapter;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;
import com.tutorialandroid.selfsecurity.decoration.DividerItemDecoration;
import com.tutorialandroid.selfsecurity.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SavedContactListActivity extends AppCompatActivity {
    private final String TAG = SavedContactListActivity.class.getSimpleName();
    private DataBaseHandler dataBaseHandler;
    private ArrayList<ContactDetails> arrayList;
    private ContactsAdapter adapter;

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_contact_list);
        ButterKnife.bind(this);
        dataBaseHandler=new DataBaseHandler(this);
        arrayList=dataBaseHandler.getContacts();
        adapter=new ContactsAdapter(SavedContactListActivity.this,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(SavedContactListActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
    @OnClick({R.id.btn_edit,R.id.btn_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_edit:
                startActivity(new Intent(SavedContactListActivity.this,EditContactsActivity.class));
                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }
}
