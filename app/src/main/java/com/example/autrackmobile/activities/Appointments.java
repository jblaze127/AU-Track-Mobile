package com.example.autrackmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.autrackmobile.R;
import com.example.autrackmobile.databinding.ActivityAppointmentsBinding;
import com.example.autrackmobile.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Appointments extends AppCompatActivity {

    private ActivityAppointmentsBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WebView webView = binding.appointmentsView;
        webView.loadUrl("https://square.site/book/LY5Y8ZY8624ZG/adelphi-athletic-training-garden-city-ny");

        webView.getSettings().setJavaScriptEnabled(true);


        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.setSelectedItemId(R.id.appointments);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.appointments) {
                return true;
            } else if (item.getItemId() == R.id.messaging) {
                startActivity(new Intent(getApplicationContext(), UsersActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

//        webView.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
    }


}