package com.tutorialandroid.selfsecurity.activitys;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tutorialandroid.selfsecurity.SecurityApplication;
import com.tutorialandroid.selfsecurity.adapter.ContactsCursorAdapter;
import com.tutorialandroid.selfsecurity.database.ContactsProvider;
import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;
import com.tutorialandroid.selfsecurity.adapter.EditContactAdapter;
import com.tutorialandroid.selfsecurity.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditContactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String TAG = EditContactsActivity.class.getSimpleName();
    private DataBaseHandler dataBaseHandler;

    private CursorAdapter cursorAdapter;

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_contacts)
    Button btnContact;
    @BindView(R.id.listView_layout)
    LinearLayout listViewLayout;
    @BindView(R.id.delete_layout)
    LinearLayout deleteLayout;
    @BindView(R.id.empty_message)
    TextView emptyMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);
        dataBaseHandler = new DataBaseHandler(getApplicationContext());
        ButterKnife.bind(this);
        updateContent();

    }

    private void updateContent() {
        restartLoader();
        cursorAdapter = new ContactsCursorAdapter(this, null, 0);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        if (((SecurityApplication) getApplication()).getContacts(getApplicationContext()).size() > 0) {
            listViewLayout.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);
            deleteLayout.setVisibility(View.VISIBLE);
        } else {
            listViewLayout.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
            deleteLayout.setVisibility(View.GONE);
        }
    }



    @OnClick({R.id.btn_delete, R.id.btn_cancel, R.id.btn_back,R.id.btn_contacts})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                try {
                    for (CheckBox c : ContactsCursorAdapter.checkArray) {
                        if (c.isChecked()) {
                            dataBaseHandler.deleteRec(String.valueOf(c.getTag()));
                            updateContent();
                        }

                    }
                } catch (Exception e) {

                }


                break;
            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_contacts:
               Intent intent=new Intent(EditContactsActivity.this,EmergencyContactsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btn_cancel:
                for (CheckBox c : ContactsCursorAdapter.checkArray) {
                    {
                        c.setChecked(false);
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, ContactsProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

}
