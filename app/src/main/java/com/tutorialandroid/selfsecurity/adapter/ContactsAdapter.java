package com.tutorialandroid.selfsecurity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.R;

import java.util.ArrayList;

/**
 * Created by VenkatPc on 5/25/2017.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<ContactDetails> arrayList;
    private LayoutInflater inflater;

    public ContactsAdapter(Context context,ArrayList<ContactDetails> arrayList){
        this.mContext=context;
        this.arrayList=arrayList;
        inflater=LayoutInflater.from(mContext);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.itemview,parent,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactDetails details=arrayList.get(position);
        holder.txtId.setText(String.valueOf(position+1));
        holder.txtName.setText(details.getName());
        holder.txtNumber.setText(details.getNumber());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId,txtName,txtNumber;
        public ViewHolder(View itemView) {
            super(itemView);
            txtId=(TextView)itemView.findViewById(R.id.item_id);
            txtName=(TextView)itemView.findViewById(R.id.txt_item_name);
            txtNumber=(TextView)itemView.findViewById(R.id.txt_item_number);
        }
    }
}

/*BaseAdapter {
    private ArrayList<ContactDetails>arrayList;
    private Context context;
    private LayoutInflater inflater;
    public ContactsAdapter(Context context,ArrayList<ContactDetails> arrayList){
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
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.txtName.setText(arrayList.get(position).getName());
        holder.txtNumber.setText(arrayList.get(position).getNumber());
        return convertView;
    }
    public class ViewHolder{
        TextView txtName;
        TextView txtNumber;
    }
}
*/