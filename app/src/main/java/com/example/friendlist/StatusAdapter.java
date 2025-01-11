package com.example.friendlist;


import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;


class StatusAdapter implements ListAdapter {


    ArrayList<Statuses> arrayList;
    Context context;


    public StatusAdapter(Context context, ArrayList<Statuses> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }


    @Override
    public boolean areAllItemsEnabled() {return false;}

    @Override
    public boolean isEnabled(int position) {return true;}

    @Override
    public void registerDataSetObserver(DataSetObserver observer) { }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) { }

    @Override
    public int getCount() {return arrayList.size();}

    @Override
    public Object getItem(int position) {return position;}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public boolean hasStableIds() {return false;}


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Statuses statusData = arrayList.get(position);

        if(convertView==null){

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.statuses, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            TextView username=convertView.findViewById(R.id.username);
            TextView status = convertView.findViewById(R.id.content);
            TextView datetime = convertView.findViewById(R.id.datetime);

            username.setText(statusData.Username);
            status.setText(statusData.StatusContent);
            datetime.setText(statusData.Datetime);
        }
        return convertView;
    }


    @Override
    public int getItemViewType(int position) {return position;}

    @Override
    public int getViewTypeCount() {return 1;}

    @Override
    public boolean isEmpty() {return false;}
}
