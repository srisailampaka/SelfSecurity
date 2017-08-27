package com.tutorialandroid.selfsecurity.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tutorialandroid.selfsecurity.R;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;
import com.tutorialandroid.selfsecurity.model.ContactDetails;

import java.util.ArrayList;

public class ContactsCursorAdapter extends CursorAdapter {
    private int flags;
    public static ArrayList<CheckBox> checkArray = new ArrayList<CheckBox>();


    public ContactsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.flags = flags;
        checkArray.clear();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(
                R.layout.contact_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String contactName = cursor.getString(
                cursor.getColumnIndex(DataBaseHandler.KEY_NAME));
        String contactPhone = cursor.getString(
                cursor.getColumnIndex(DataBaseHandler.KEY_NUMBER));

        final String id = cursor.getString(
                cursor.getColumnIndex(DataBaseHandler.KEY_ID));

        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_checkbox);
        checkBox.setTag(id);
        if (flags == 0) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }
        nameTextView.setText(contactName);
        phoneTextView.setText(contactPhone);
        checkArray.add(checkBox);

    }
    public ArrayList<CheckBox> getCheckedList()
    {
        return checkArray;
    }
}
