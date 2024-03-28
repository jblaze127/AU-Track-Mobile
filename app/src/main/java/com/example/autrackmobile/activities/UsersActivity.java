package com.example.autrackmobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.MenuItem;
import android.view.View;

import com.example.autrackmobile.R;
import com.example.autrackmobile.adapters.UserAdapter;
import com.example.autrackmobile.databinding.ActivityMainBinding;
import com.example.autrackmobile.databinding.ActivityUsersBinding;
import com.example.autrackmobile.models.User;
import com.example.autrackmobile.utilities.Constants;
import com.example.autrackmobile.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class UsersActivity extends AppCompatActivity {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        //getUsers();
        setListeners();


        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.messaging) {
                return true;
            } else if (item.getItemId() == R.id.dashboard) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

    }

    private void setListeners() {
        binding.backImage.setOnClickListener(v -> onBackPressed());
        binding.newChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SelectUsers.class)));
    }

//    private void getUsers() {
//        loading(true);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .get()
//                .addOnCompleteListener(task -> {
//                   loading(false);
//                   String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
//                   if (task.isSuccessful() && task.getResult() != null) {
//                       List<User> users = new ArrayList<>();
//                       for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                           if (currentUserId.equals(queryDocumentSnapshot.getId())) {
//                               continue;
//                           }
//                           User user = new User();
//                           user.fname = queryDocumentSnapshot.getString(Constants.KEY_FNAME);
//                           user.lname = queryDocumentSnapshot.getString(Constants.KEY_LNAME);
//                           user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
//                           user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
//                           user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
//                           users.add(user);
//                       }
//
//                       if (users.size() > 0) {
//                           UserAdapter userAdapter = new UserAdapter(users);
//                           binding.usersRecyclerView.setAdapter(userAdapter);
//                           binding.usersRecyclerView.setVisibility(View.VISIBLE);
//                       } else {
//                           showErrorMessage();
//                       }
//                   } else {
//                       showErrorMessage();
//                   }
//                });
//    }



//    private void loading(Boolean isLoading) {
//        if(isLoading) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//        } else {
//            binding.progressBar.setVisibility(View.INVISIBLE);
//        }
//    }

//    private void showErrorMessage() {
//        binding.errorMessage.setText(String.format("%s", "No user available."));
//        binding.errorMessage.setVisibility(View.VISIBLE);
//    }
}