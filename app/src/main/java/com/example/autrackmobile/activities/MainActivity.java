package com.example.autrackmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.widget.Toast;

import com.example.autrackmobile.R;
import com.example.autrackmobile.databinding.ActivityMainBinding;
import com.example.autrackmobile.models.ChatMessage;
import com.example.autrackmobile.utilities.Constants;
import com.example.autrackmobile.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        loadUserDetails();
        getToken();
        setListeners();

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView; //findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        //code deals with bottom navigation view movements
        //testing to press on messaging to make it go to each specific activities

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

        binding.floatingActionButton2.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), AddWorkout.class));
            overridePendingTransition(0,0);
            //Toast.makeText(this, "Btn Clicked", Toast.LENGTH_SHORT).show();
        });

    }


    private void setListeners() {
        binding.signOut.setOnClickListener(v -> signOut());
    }

    private void loadUserDetails() {
        binding.textFName.setText(preferenceManager.getString(Constants.KEY_FNAME));
        //binding.textLName.setText(preferenceManager.getString(Constants.KEY_LNAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);

    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

//    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
//        if (error != null) {
//            return;
//        }
//        if (value != null) {
//            for (DocumentChange documentChange : value.getDocumentChanges()) {
//                if (documentChange.getType() == DocumentChange.Type.ADDED) {
//                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
//                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
//                    ChatMessage chatMessage = new ChatMessage();
//                    chatMessage.senderId = senderId;
//                    chatMessage.receiverId = receiverId;
//                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
//                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
//                        chatMessage.conversationFName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
//                        chatMessage.ConversationId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
//                    } else {
//                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
//                        chatMessage.conversationFName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
//                        chatMessage.ConversationId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
//                    }
//
//                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
//                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
//
//                }
//            }
//        }
//    });

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> showToast("Token Successfully Updated!"))
                .addOnFailureListener(e -> showToast("Unable to update token."));
    }

    private void signOut() {
        showToast("Signing Out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to Sign Out."));
    }

}