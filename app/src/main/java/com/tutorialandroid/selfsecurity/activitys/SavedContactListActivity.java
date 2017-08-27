package com.tutorialandroid.selfsecurity.activitys;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tutorialandroid.selfsecurity.SecurityApplication;
import com.tutorialandroid.selfsecurity.adapter.ContactsCursorAdapter;
import com.tutorialandroid.selfsecurity.database.ContactsProvider;
import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.adapter.ContactsAdapter;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;
import com.tutorialandroid.selfsecurity.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SavedContactListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String TAG = SavedContactListActivity.class.getSimpleName();
    private DataBaseHandler dataBaseHandler;
    private ArrayList<ContactDetails> arrayList;
    private ContactsAdapter adapter;
    private CursorAdapter cursorAdapter;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_edit)
    Button btnEdit;

    @BindView(R.id.listView_layout)
    LinearLayout listViewLayout;

    @BindView(R.id.empty_message)
    TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_contact_list);
        ButterKnife.bind(this);
        upateTheList();
    }

    private void upateTheList() {

        restartLoader();
        cursorAdapter = new ContactsCursorAdapter(this, null, 1);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        if (((SecurityApplication) getApplication()).getContacts(getApplicationContext()).size() > 0) {
            listViewLayout.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        } else {
            listViewLayout.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btn_edit, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit:
                startActivity(new Intent(SavedContactListActivity.this, EditContactsActivity.class));
                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        upateTheList();
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
