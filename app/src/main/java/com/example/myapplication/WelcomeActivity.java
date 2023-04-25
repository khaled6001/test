package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogin();
        setContentView(R.layout.activity_welcome);
        Button getStartedButton = findViewById(R.id.get_started_button);
        getStartedButton.setOnClickListener(this);
        Button alreadyHaveAccountButton = findViewById(R.id.already_have_account_button);
        alreadyHaveAccountButton.setOnClickListener(this);
    }

    private void checkLogin() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
        String email = preferences.getString("email", "");
        if (!"".equals(email)) {
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.already_have_account_button:
                intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.get_started_button:
                intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            default: break;
        }
    }
}