package com.example.friendlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import org.json.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.*;


public class ViewFriendStatus extends AppCompatActivity {


    ListView listView;
    ImageButton popUpStuff;
    ImageButton scrollUp;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popUpStuff = findViewById(R.id.addStuff);
        scrollUp = findViewById(R.id.scrollUp);
        listView = findViewById(R.id.listview);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");//gets username of current user

        popUpStuff.setOnClickListener(view -> doPopUpStuff(view));
        scrollUp.setOnClickListener(view -> listView.smoothScrollToPosition(0));
        listView.setEmptyView(findViewById(R.id.emptyElement)); //IF YOU HAVE NO FRIENDS


        /*ONLY RUN ONE AT A TIME. I WAS TOO LAZY TO MAKE MULTIPLE ACTIVITIES
        PLEASE PUT THE FUNCTIONS IN THE RUNNABLE OTHERWISE YOU'LL CRY
            1) setFriendStatuses()
                USES THE STATUES CLASS AND THE ADAPTER TO SHOW WHAT SHIT YOUR FRIENDS SAY
            2) getFriendList()
                USES STRINGS AND FRIEND ADAPTER TO SHOW A LIST OF YOUR FRIENDS
            3) setStatus()
                USES STATUS CLASS TOO BECAUSE I'M LAZY*/

        runOnUiThread(() -> getFriendStatus());
    }

    public void doPopUpStuff(View view) {

        PopupMenu popUp = new PopupMenu(ViewFriendStatus.this, view);
        popUp.getMenuInflater().inflate(R.menu.popup_menu, popUp.getMenu());

        popUp.setOnMenuItemClickListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.addFriendsPopup:
            }
            return true;
        });
        popUp.show();
    }

    public void setFriendStatus(JSONArray statuses) throws JSONException {

        ArrayList<Statuses> arrayList = new ArrayList<>();

        if (statuses.length() == 0) {
            return;
        }

        for (int i = statuses.length() - 1; i >= 0; i--) {

            JSONObject status = (JSONObject) statuses.get(i);
            String creation = (String) status.get("creationTime");
            String content = (String) status.get("content");
            String friendUsername = (String) status.get("USERNAME");
            arrayList.add(new Statuses(friendUsername, content, creation));
        }

        StatusAdapter statusAdapter = new StatusAdapter(this, arrayList);
        listView.setAdapter(statusAdapter);
    }


    public void getFriendStatus() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2554397/friendstatus.php?username=" + username;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException exception) {
                exception.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    String statuses = response.body().string();

                    if (statuses.equals("")) {
                        return;
                    }
                    ;

                    try {
                        JSONArray JSONstatuses = new JSONArray(statuses);
                        runOnUiThread(() -> {
                            try {
                                setFriendStatus(JSONstatuses);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }
}