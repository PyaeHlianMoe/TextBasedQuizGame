package com.example.quizzer.Interfaces;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public interface DBInterface {

    public void addNewUser(final String Name, String Email);

    public void addNewQuestion(String level, String answer, String option1, String option2, String option3, String option4, String title, String module, String topic);

    public void updateQuestion(final String id, final String level, final String answer, final String option1, final String option2, final String option3, final String option4, final String title);


    // This FirestoreInterface is just to fix the issue with async message reply from Firestore
    public void queryUserRole(String username,final FirestoreInterface myCallBack);

    public void queryUserEmail(String username, final FirestoreInterface myCallBack);
}
