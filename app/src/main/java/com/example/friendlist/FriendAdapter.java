package com.example.friendlist;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class FriendAdapter implements ListAdapter {


    ArrayList<String> arrayList;
    Context context;


    public FriendAdapter(Context context, ArrayList<String> arrayList) {
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


        String friend = arrayList.get(position);

        if(convertView==null){

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.friendlist, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            TextView username=convertView.findViewById(R.id.friend_username);
            username.setText(friend);
        }
        return convertView;
    }


    @Override
    public int getItemViewType(int position) {return position;}

    @Override
    public int getViewTypeCount() {return arrayList.size();}

    @Override
    public boolean isEmpty() {return false;}
}

