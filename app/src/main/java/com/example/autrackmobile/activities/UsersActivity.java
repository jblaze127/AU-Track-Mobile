package com.example.autrackmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.MenuItem;
import android.view.View;

import com.example.autrackmobile.R;
import com.example.autrackmobile.adapters.RecentConversationsAdapter;
import com.example.autrackmobile.adapters.UserAdapter;
import com.example.autrackmobile.databinding.ActivityMainBinding;
import com.example.autrackmobile.databinding.ActivityUsersBinding;
import com.example.autrackmobile.listeners.ConversationListener;
import com.example.autrackmobile.models.ChatMessage;
import com.example.autrackmobile.models.User;
import com.example.autrackmobile.utilities.Constants;
import com.example.autrackmobile.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UsersActivity extends BaseActivity implements ConversationListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        //getUsers();
        init();
        setListeners();
        listenConversations();



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

    private void init() {
        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations, this);
        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void setListeners() {
        binding.backImage.setOnClickListener(v -> onBackPressed());
        binding.newChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SelectUsers.class)));
    }

    private void listenConversations() {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);

        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.conversationFName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.ConversationId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    } else {
                        chatMessage.conversationImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.conversationFName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.ConversationId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }

                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }

            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationsAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    });

    @Override
    public void onConversationClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
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