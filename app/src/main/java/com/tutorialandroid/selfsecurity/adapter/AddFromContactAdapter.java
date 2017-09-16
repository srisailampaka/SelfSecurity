package com.tutorialandroid.selfsecurity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class AddFromContactAdapter extends RecyclerView.Adapter<AddFromContactAdapter.ViewHolder>{
private Context mContext;
private ArrayList<ContactDetails> arrayList;
private LayoutInflater inflater;

public AddFromContactAdapter(Context context,ArrayList<ContactDetails> arrayList){
        this.mContext=context;
        this.arrayList=arrayList;
        inflater=LayoutInflater.from(mContext);
        }
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.contact_itemview,parent,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {
        ContactDetails details=arrayList.get(position);
        holder.txtName.setText(details.getName());
        holder.txtNumber.setText(details.getNumber());
        }

@Override
public int getItemCount() {
        return arrayList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView txtName,txtNumber;
    public ViewHolder(View itemView) {
        super(itemView);
        txtName=(TextView)itemView.findViewById(R.id.txt_item_name);
        txtNumber=(TextView)itemView.findViewById(R.id.txt_item_number);
    }
}
}

