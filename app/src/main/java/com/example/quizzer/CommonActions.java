package com.example.quizzer;

import android.support.annotation.NonNull;

import com.example.quizzer.Interfaces.FirestoreInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
This class is the collection of common actions that will be used in the application
 */

public class CommonActions {
    public static String userType;
    public static String userEmail;
    public static ArrayList<String> moduleList = new ArrayList<>();
    public static ArrayList<String> topicList = new ArrayList<>();
    public static ArrayList<String> userList = new ArrayList<>();

    public static Firestore firestore = new Firestore();

    public static void getUserRole(String username) {
        firestore.queryUserRole(username, new FirestoreInterface() {
            @Override
            public void onCallback(ArrayList<String> value) {
                userType = value.get(0);
            }
        });
    }

    public static void getUserEmail(String username) {
        firestore.queryUserEmail(username, new FirestoreInterface() {
            @Override
            public void onCallback(ArrayList<String> value) {
                userEmail = value.get(0);
            }
        });
    }

    public static void getModuleList() {
        // Always clear the ArrayList if it is not empty
        if (!moduleList.isEmpty()) {
            moduleList.clear();
        }

        Firestore.db.collection("modules")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        moduleList.add(document.getId());
                    }
                }
            }
        });
    }

    public static void getTopicList() {
        // Always clear the ArrayList if it is not empty
        if (!topicList.isEmpty()) {
            topicList.clear();
        }

        Firestore.db.collection("topics")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        topicList.add(document.getId());
                    }
                }
            }
        });
    }

    public static void getUserList() {
        // Always clear the ArrayList if it is not empty
        if (!userList.isEmpty()) {
            userList.clear();
        }

        Firestore.db.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        userList.add(document.getId());
                    }
                }
            }
        });
    }
}
