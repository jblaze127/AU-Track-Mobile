package com.example.autrackmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.autrackmobile.R;
import com.example.autrackmobile.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.signIn.setOnClickListener(v -> onBackPressed());
    }
}