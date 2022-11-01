package com.example.a1;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    TextView username;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

    }

    public void ReturnToLogin(View view) {
        finish();
    }

    public void LoginActivated(View view) {
        String userLogStr = username.getText().toString();
        Log.e("READ USERNAME", userLogStr);
        String passLogStr = password.getText().toString();
        Log.d("READ PASSWORD", passLogStr);

        if (userLogStr.equals("Rohan") && passLogStr.equals("Rohan")){
            Intent intent = new Intent(this, NewRunActivity.class);
            startActivity(intent);
            finish();
        }
    }
}