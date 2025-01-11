package com.example.friendlist;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUp extends AppCompatActivity {


    Button cancel;
    Button signUp;
    EditText username_sign;
    EditText email_sign;
    EditText password_sign;
    String username;
    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //ALL VIEWS INITIALISED
        cancel = findViewById(R.id.cancelButton);
        signUp = findViewById(R.id.signUpButton);
        username_sign = findViewById(R.id.usernameSignUp);
        email_sign = findViewById(R.id.Email_SignUp);
        password_sign = findViewById(R.id.passwordSignUp);

        cancel.setOnClickListener(view -> cancelSignUp());
        signUp.setOnClickListener(view -> checkInputs());
    }


    private void checkInputs() {

        email = email_sign.getText().toString();
        username = username_sign.getText().toString();
        password = password_sign.getText().toString();

        //CHECKS IF ANY INPUT IS PURELY WHITESPACE
        if (TextUtils.isEmpty( email.trim() ) || TextUtils.isEmpty( username.trim() ) || TextUtils.isEmpty(password.trim() )) {
            Toast.makeText(SignUp.this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
        }

        //CHECKS IF USERNAME OR EMAIL HAS WHITESPACE
        else if (email.startsWith(" ") || email.endsWith(" ") || email.contains(" ")){
            Toast.makeText(SignUp.this, "Please remove whitespace from email", Toast.LENGTH_SHORT).show();
        }

        else if (username.startsWith(" ") || username.endsWith(" ")){
            Toast.makeText(SignUp.this, "Please remove whitespace from username", Toast.LENGTH_SHORT).show();
        }

        //CHECKS IF EMAIL HAS VALID CHARACTERS. DO NOT REMOVE [].
        else if (email.matches("(.*)[\\p{Punct}&&[^.@_-]](.*)")){
            Toast.makeText(SignUp.this, "Please only use valid characters in email (., @, _, -)", Toast.LENGTH_SHORT).show();
        }

        else if (username.matches("(.*)[\\p{Punct}&&[^._-]](.*)")){
            Toast.makeText(SignUp.this, "Please only use valid characters in username (., _, -)", Toast.LENGTH_SHORT).show();
        }

        else {
            signUp();
        }
    }


    private void signUp() {

        username = username_sign.getText().toString();
        String password = password_sign.getText().toString();
        String email = email_sign.getText().toString();

        Thread thread = new Thread(() -> {

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("email", email)
                    .build();

            Request request = new Request.Builder()
                    .url("https://lamp.ms.wits.ac.za/home/s2554397/register.php") // chloe: we need to create a php file in nashita's database that inserts this data into the table i just used the welcome as a placeholder
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    // Process the successful response
                    final String serverResponse = response.body().string();

                    // Use Handler or runOnUiThread to update UI elements
                    runOnUiThread(() -> {

                        if (serverResponse.trim().equals("Registration successful")) {

                            username_sign.getText().clear();
                            password_sign.getText().clear();
                            email_sign.getText().clear();

                            // Registration was successful, switch to the MainActivity
                             switchToMainActivity();
                        }

                        else {
                            // There was an issue with the registration, display the server response
                            Toast.makeText(SignUp.this, serverResponse.replace("\n", ""), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else {
                    // Handle the unsuccessful response
                    runOnUiThread(() -> Toast.makeText(SignUp.this, "Signup failed", Toast.LENGTH_SHORT).show());
                }
                // Close the response
                response.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Start the thread
        thread.start();
    }


    private void switchToMainActivity(){

        username_sign.getText().clear();
        password_sign.getText().clear();
        email_sign.getText().clear();

        Intent switchActivityIntent  = new Intent(this, MainActivity.class);
        switchActivityIntent.putExtra("username", username );

        startActivity(switchActivityIntent);
    }


    private void cancelSignUp(){

        username_sign.getText().clear();
        password_sign.getText().clear();
        email_sign.getText().clear();

        Intent switchActivityIntent  = new Intent(this, Login.class);
        startActivity(switchActivityIntent);
    }
}




