package com.tutorialandroid.selfsecurity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.R;

import java.util.ArrayList;

/**
 * Created by vijayalaxmi on 05/26/2017.
 */

public class EditContactAdapter extends RecyclerView.Adapter<EditContactAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<ContactDetails> arrayList;
    private LayoutInflater inflater;
    public static ArrayList<CheckBox> checkArray = new ArrayList<CheckBox>();

    public EditContactAdapter(Context context,ArrayList<ContactDetails> arrayList){
        this.mContext=context;
        this.arrayList=arrayList;
        inflater=LayoutInflater.from(mContext);
    }
    @Override
    public EditContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.edit_contact_item,parent,false);
        EditContactAdapter.ViewHolder holder=new EditContactAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(EditContactAdapter.ViewHolder holder, int position) {
        ContactDetails details=arrayList.get(position);
        holder.txtId.setText(String.valueOf(position+1));
        holder.txtName.setText(details.getName());
        holder.txtNumber.setText(details.getNumber());
        holder.checkBox.setId(details.getId());
        checkArray.add(holder.checkBox);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId,txtName,txtNumber;
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            txtId=(TextView)itemView.findViewById(R.id.item_id);
            txtName=(TextView)itemView.findViewById(R.id.txt_item_name);
            txtNumber=(TextView)itemView.findViewById(R.id.txt_item_number);
            checkBox=(CheckBox)itemView.findViewById(R.id.item_checkbox);
        }
    }
}
