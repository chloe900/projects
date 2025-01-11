package com.example.friendlist;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import okhttp3.*;


public class MainActivity extends AppCompatActivity {


    ListView listView;
    ImageButton popUpStuff;
    ImageButton scrollUp;
    Button HomeButton;
    String username;
    private OkHttpClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");//gets username of current user

        popUpStuff = findViewById(R.id.addStuff);
        scrollUp = findViewById(R.id.scrollUp);
        HomeButton = findViewById(R.id.goToHome);
        listView = findViewById(R.id.listview);

        HomeButton.setOnClickListener(view -> GoBacktoLogin());
        popUpStuff.setOnClickListener(view -> doPopUpStuff(view));
        scrollUp.setOnClickListener(view -> listView.smoothScrollToPosition(0));

        listView.setEmptyView(findViewById(R.id.emptyElement)); //IF YOU HAVE NO FRIENDS
        client = new OkHttpClient();

        runOnUiThread(() -> getFriendList());
    }

    public void doPopUpStuff(View view) {
        PopupMenu popUp = new PopupMenu(MainActivity.this, view);
        popUp.getMenuInflater().inflate(R.menu.popup_menu, popUp.getMenu());

        popUp.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.addFriendsPopup:
                    addFriendDialog();
                    return true;

                case R.id.listFriends:
                    getFriendList();
                    return true;
                case R.id.theMutualStuff:
                    GotoMutuals();
                    return true;
                case R.id.FriendStatus:
                    GotoFriendStatus();
                    return true;
        }

            return false;
        });

        popUp.show();
    }


    public void setFriendList(String friends) throws JSONException {

        ArrayList<String> arraylist = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(friends);

        for (int i = 0; i < jsonArray.length(); i++){
            arraylist.add(jsonArray.getString(i));
        }

        if (!arraylist.isEmpty()) {
            FriendAdapter friendAdapter = new FriendAdapter(this, arraylist);
            listView.setAdapter(friendAdapter);
        }

    }


    public void getFriendList(){

        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2554397/getfriends.php?username=" + username;

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

                    String friends = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            setFriendList(friends);
                        }
                        catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }


    public  void setStatus(JSONArray statuses) throws JSONException {

        ArrayList<Statuses> arrayList = new ArrayList<>();

        if (statuses.length() == 0){
            return;
        }

        for (int i = statuses.length() - 1; i >= 0; i--){

            JSONObject status = (JSONObject) statuses.get(i);
            String creation = (String)status.get("creationTime");
            String content = (String)status.get("content");
            arrayList.add(new Statuses("NCR", content, creation));
        }
        StatusAdapter statusAdapter = new StatusAdapter(this, arrayList);
        listView.setAdapter(statusAdapter);
    }



    public void getStatuses(){

        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2554397/displaystatus.php?username=" + username;

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
                    try {
                        JSONArray JSONstatuses= new JSONArray(statuses);
                        runOnUiThread(() -> {
                            try {
                                setStatus(JSONstatuses);
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





    //addFriends, main function, this is supposed to just GET request the php,
    //could add a toast that states whether adding a friend was sucessful or not.
    public void addFriend(String username, String friendUsername) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2554397/addfriends.php?username=" + username + "&f_username=" + friendUsername;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Display the response message
                            Toast.makeText(MainActivity.this, responseData, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //this big boy searches if the friend we want to add, is valid in the first place, i reckon
    //it would work if a user is logged in/signed up, will use this in the dialog.


    //the main star of the show, instead of creating a new screen to addFriends, this dialog is like a pop up box that
    //should allow for a user to dynamiclly search for a username, it should be checked if the username is valid
    //if it is then it should be the last username displayed{since they are unique}
    //then onClicking the username the user should be able to add it as a friend

    //version that should use the performSearch:

    public void addFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_addfriends, null);
        builder.setView(dialogView);
        builder.setTitle("Add Friend");

        EditText friendUser = dialogView.findViewById(R.id.friendname);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String friendUsername = friendUser.getText().toString();

                if (!friendUsername.isEmpty()) {
                    String loggedInUsername = username;
                    checkIfValidUser(friendUsername, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e("MainActivity", "User validation failed!", e);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "User validation failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseBody = response.body().string().trim();
                            Log.d("MainActivity", "Validation result: " + responseBody); // added log here

                            if (responseBody.equals("true")){
                                addFriend(loggedInUsername, friendUsername);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Friend added: " + friendUsername, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Invalid friend username", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Please enter the friend's username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void checkIfValidUser(String user, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2554397/searchfriends2.php?input=" + user;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }
    private void addStatus(String username,String formattedDate,String StatusText) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2554397/status.php?username="+username+"&creationDate="+formattedDate+"&content="+StatusText;
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Display the response message
                            Toast.makeText(MainActivity.this, responseData, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }






    //adds the dialog for the status -> similar to Chloe's code
    public void addStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.poststatus, null);
        builder.setView(dialogView);
        builder.setTitle("Post Status");

        EditText Status = dialogView.findViewById(R.id.StatusUpdate);




        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String StatusText = Status.getText().toString();

                if (!StatusText.isEmpty()) {
                    //add to database
                    //addFriend(loggedInUsername, friendUsername);

                    //gets the current date and time
                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = calendar.getTime();
                    //I belive the date format is correct
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String formattedDate = dateFormat.format(currentDate);

                    //refrences the method to add the status to the database.
                    addStatus(username,formattedDate,StatusText);
                    Toast.makeText(MainActivity.this, "Added your status update!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Please enter a status update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void GotoFriendStatus(){
        Intent switchActivityIntent  = new Intent(MainActivity.this, ViewFriendStatus.class);
        switchActivityIntent.putExtra("username", username);
        startActivity(switchActivityIntent);
    }

    public void GoBacktoLogin(){
        Intent switchActivityIntent  = new Intent(MainActivity.this, Login.class);
        startActivity(switchActivityIntent);
    }
    public void GotoMutuals(){
        Intent switchActivityIntent  = new Intent(MainActivity.this, Mutuals.class);
        switchActivityIntent.putExtra("username", username);
        startActivity(switchActivityIntent);
    }


}