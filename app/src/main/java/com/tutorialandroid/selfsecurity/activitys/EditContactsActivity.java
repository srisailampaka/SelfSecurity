package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;
import com.tutorialandroid.selfsecurity.decoration.DividerItemDecoration;
import com.tutorialandroid.selfsecurity.adapter.EditContactAdapter;
import com.tutorialandroid.selfsecurity.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditContactsActivity extends AppCompatActivity {
    private final String TAG = EditContactsActivity.class.getSimpleName();
    private DataBaseHandler dataBaseHandler;
    private ArrayList<ContactDetails> arrayList;
    private EditContactAdapter adapter;
    public static ArrayList<String> idlist;

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_contacts)
    Button btnContact;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);
        ButterKnife.bind(this);
        dataBaseHandler=new DataBaseHandler(this);
        arrayList=dataBaseHandler.getContacts();
        adapter=new EditContactAdapter(EditContactsActivity.this,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(EditContactsActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
    @OnClick({R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                for(CheckBox c:EditContactAdapter.checkArray){
                    if(c.isChecked()){
                        dataBaseHandler.deleteRec(String.valueOf(c.getId()));
                        startActivity(new Intent(EditContactsActivity.this,EditContactsActivity.class));
                    }

                }

                break;
            default:
                break;
        }
    }
}
