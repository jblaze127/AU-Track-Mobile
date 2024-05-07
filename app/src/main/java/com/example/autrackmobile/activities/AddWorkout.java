package com.example.autrackmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.autrackmobile.R;
import com.example.autrackmobile.databinding.ActivityAddWorkoutBinding;
import com.example.autrackmobile.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddWorkout extends AppCompatActivity {

    private ActivityAddWorkoutBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.dashboard) {
                return true;
            } else if (item.getItemId() == R.id.messaging) {
                //finish after creating activity for messaging
                startActivity(new Intent(getApplicationContext(), UsersActivity.class));
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.appointments) {
                startActivity(new Intent(getApplicationContext(), Appointments.class));
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.profile) {
                //finish

            } else if (item.getItemId() == R.id.schedule) {
                //finish

            }
            return false;
        });


    }
}