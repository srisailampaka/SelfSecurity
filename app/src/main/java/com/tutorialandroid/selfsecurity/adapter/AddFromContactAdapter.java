package com.tutorialandroid.selfsecurity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.R;

import java.util.ArrayList;

/**
 * Created by vijayalaxmi on 05/27/2017.
 */

public class AddFromContactAdapter extends BaseAdapter {
    private ArrayList<ContactDetails> arrayList;
    private Context context;
    private LayoutInflater inflater;
    public AddFromContactAdapter(Context context,ArrayList<ContactDetails> arrayList){
        this.context=context;
        this.arrayList=arrayList;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.itemview,null);
            holder.txtName=(TextView)convertView.findViewById(R.id.txt_item_name);
            holder.txtNumber=(TextView)convertView.findViewById(R.id.txt_item_number);
            holder.txtNumber.setVisibility(View.GONE);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.txtName.setText(arrayList.get(position).getName() +"\n" +arrayList.get(position).getNumber());
       // holder.txtNumber.setText(arrayList.get(position).getNumber());
        return convertView;
    }
    public class ViewHolder{
        TextView txtName;
        TextView txtNumber;
    }
}
