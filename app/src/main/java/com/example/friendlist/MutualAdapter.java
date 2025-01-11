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



class MutualAdapter implements ListAdapter {


    ArrayList< ArrayList<String> > arrayList;
    Context context;


    public MutualAdapter(Context context, ArrayList< ArrayList<String> > arrayList) {
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


        ArrayList<String> mutualData = arrayList.get(position);

        if(convertView==null){

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.mutuals, null);
            convertView.setOnClickListener(v -> {

            });

            TextView usernameMutual = convertView.findViewById(R.id.usernameOfMutual);
            TextView usernameFriend = convertView.findViewById(R.id.usernameOfFriend);

            usernameMutual.setText(mutualData.get(0));
            usernameFriend.setText(mutualData.get(1));
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
