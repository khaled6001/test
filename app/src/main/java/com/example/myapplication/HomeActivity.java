package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.Map;

public class HomeActivity extends HttpActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeTextView = findViewById(R.id.welcome_textview);
    }

    @Override
    protected void responseReceived(String response, Map<String, String> params) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoodMenu();
        //   Toast.makeText(this, "onResume(), this method can be called several times", Toast.LENGTH_SHORT).show();
    }

    private void loadUserData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstName = preferences.getString("first_name", "");
        String familyName = preferences.getString("family_name", "");
        welcomeTextView.setText("Hello " + firstName + " " + familyName);
    }

    private void loadFoodMenu() {
        // TODO: Load the food menu list from the server and display it on the screen
    }


}