package com.example.friendlist;


import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import okhttp3.*;
import java.io.IOException;
import java.util.Objects;

import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {


    Button loginButton, signUp_login, exitButton;
    EditText email_login, password_login;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp_login = findViewById(R.id.SignUp_login);
        email_login = findViewById(R.id.Email_login);
        password_login = findViewById(R.id.Password_login);
        loginButton = findViewById(R.id.LoginButton);
        exitButton = findViewById(R.id.ExitButton);

        signUp_login.setOnClickListener(view -> switchToSignUp());
        loginButton.setOnClickListener(view -> runOnUiThread(this::checkInputs));
        exitButton.setOnClickListener(view -> exitApp());
    }

    private void checkInputs(){

        if (TextUtils.isEmpty( email_login.getText().toString().trim() )){
            Toast.makeText(Login.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
        }

        else if  (TextUtils.isEmpty( password_login.getText().toString().trim() )){
            Toast.makeText(Login.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        }

        else {
            runOnUiThread(this::authenticateLogin);
        }
    }


    private void authenticateLogin() {

        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2554397/test.php?email=" + email_login.getText().toString();

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

                String returned = response.body().string();
                JSONObject jsonObject;
                String password;

                    try {
                        jsonObject = new JSONObject(returned);
                        password = (String) jsonObject.get("PASSWORD");
                    }

                    catch (JSONException e) {
                        runOnUiThread(() -> Toast.makeText(Login.this, "Your login details are incorrect.", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    if (password.equals(password_login.getText().toString())) {
                        getUsername();
                    }
                    else runOnUiThread(() -> Toast.makeText(Login.this, "Your login details are incorrect.", Toast.LENGTH_SHORT).show());
                }
            }

        });
    }


    private void getUsername() {

        String welcomeUrl = "https://lamp.ms.wits.ac.za/home/s2554397/welcome.php?welcome=" + email_login.getText().toString();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(welcomeUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException exception) {
                exception.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    username = response.body().string();
                    switchToMainActivity();
                }
            }
        });
    }


    private void switchToMainActivity(){

        Intent switchActivityIntent  = new Intent(this, MainActivity.class);
        switchActivityIntent.putExtra("username", username);

        email_login.getText().clear();
        password_login.getText().clear();

        startActivity(switchActivityIntent);
    }


    private void switchToSignUp(){

        email_login.getText().clear();
        password_login.getText().clear();

        Intent switchActivityIntent  = new Intent(this, SignUp.class);
        startActivity(switchActivityIntent);
    }


    private void exitApp(){
        finish();
        System.exit(0);
    }
}
