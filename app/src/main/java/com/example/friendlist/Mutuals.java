package com.example.friendlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Mutuals extends AppCompatActivity {
    ImageButton HomeButton;
    ListView listView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutuals);

        //GETS DATA
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        HomeButton = findViewById(R.id.goToHome);
        HomeButton.setOnClickListener(view -> GoBacktoMainActivity());

        // Initialize the listView here
        listView = findViewById(R.id.listview);
        listView.setEmptyView(findViewById(R.id.emptyElement));

        runOnUiThread(() -> getMutuals());
    }


    public void GoBacktoMainActivity(){
        Intent switchActivityIntent  = new Intent(Mutuals.this, MainActivity.class);
        switchActivityIntent.putExtra("username", username);
        startActivity(switchActivityIntent);
    }
    public void getMutuals(){


        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2554397/getmutuals.php?username=" + username;

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
                    String mutuals = response.body().string();
                    try {
                        JSONObject JSONmutuals = new JSONObject(mutuals);
                        runOnUiThread(() -> {
                            try {
                                setMutuals(JSONmutuals);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void setMutuals(JSONObject mutuals) throws JSONException {

        Iterator<String> keys = mutuals.keys(); //HELPS ITERATE OVER USERS FRIENDS
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();

        while (keys.hasNext()){

            String key = keys.next(); //USERS FRIEND
            JSONArray mutualsArray = (JSONArray) mutuals.get(key);

            for (int i = 0; i < mutualsArray.length(); i++){

                String mutual = (String) mutualsArray.get(i);
                ArrayList<String> pair = new ArrayList<>();
                pair.add(mutual);
                pair.add("Friends with " + key);
                arrayList.add(pair);
            }
        }
        try {
            MutualAdapter mutualAdapter = new MutualAdapter(this, arrayList);
            listView.setAdapter(mutualAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}