package com.example.quizzer.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.example.quizzer.Fragments.SubmitChallengeFragment;
import com.example.quizzer.Model.Challenge;
import com.example.quizzer.Firestore;
import com.example.quizzer.Model.Question;
import com.example.quizzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

/*
This activity will be called when user start to play the quiz/ challenge.
It will help to display a list of questions, calculate the marks and update the result
 */

public class PlayChallengeActivity extends AppCompatActivity implements View.OnClickListener,Serializable {

    private TextView textViewQuestionId;
    private TextView textViewQuestion;

    private Button buttonOption1;
    private Button buttonOption2;
    private Button buttonOption3;
    private Button buttonOption4;
    private Button buttonStart;
    String correctAnswer;
    Integer score;
    private String userId, challengeId;
    private static final String TAG = "PlayActivity";
    private ArrayList<Question> questionList = new ArrayList<>();
    Challenge c;

    private Integer questionId = 1;
    private Integer maxQuestionId = 5;

    private FirebaseFirestore db;
    Integer[] qNo;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_challenge);

        userId= getIntent().getStringExtra("USER_ID"); //pass from loginActivity
        challengeId= getIntent().getStringExtra("CHALLENGE_ID"); //pass from loginActivity
        c = (Challenge)getIntent().getSerializableExtra("CHALLENGE_OBJECT");

        fragmentManager = getSupportFragmentManager();

        findViewById(R.id.FragmentContainer).setVisibility(View.INVISIBLE);

        //questionList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        getAllQuestions();

        score = 0;

        textViewQuestionId = findViewById(R.id.tv_questionId);
        textViewQuestion = findViewById(R.id.tv_questionDesc);


        buttonOption1 = findViewById(R.id.bt_option1);
        buttonOption2 = findViewById(R.id.bt_option2);
        buttonOption3 = findViewById(R.id.bt_option3);
        buttonOption4 = findViewById(R.id.bt_option4);
        buttonStart = findViewById(R.id.bt_START);

        textViewQuestionId.setVisibility(View.INVISIBLE);
        textViewQuestion.setVisibility(View.INVISIBLE);
        buttonOption1.setVisibility(View.INVISIBLE);
        buttonOption2.setVisibility(View.INVISIBLE);
        buttonOption3.setVisibility(View.INVISIBLE);
        buttonOption4.setVisibility(View.INVISIBLE);

        buttonOption1.setOnClickListener(this);
        buttonOption2.setOnClickListener(this);
        buttonOption3.setOnClickListener(this);
        buttonOption4.setOnClickListener(this);
        buttonStart.setOnClickListener(this);

        //findViewById(R.id.textview_view_challenges).setOnClickListener(this);
    }

   /*private void playChallenge(Integer pos){

        Question question = questionList.get(pos);
        correctAnswer = question.getCorrectAnswer();
        //difficultyLevel = document.getString("difficulty_level");
        buttonOption1.setText(question.getOption1());
        buttonOption2.setText(question.getOption2());
        buttonOption3.setText(question.getOption3());
        buttonOption4.setText(question.getOption4());

        textViewQuestion.setText(question.getQuestion());
        textViewQuestionId.setText("Question : " + questionId + "  , Score : " + score);

    }*/

   /* private void getQuestion(){
        //db = FirebaseFirestore.getInstance();


        //Log.d("test", questionList.get(0).getQuestion());
        Toast.makeText(this, String.valueOf(questionList.size()),Toast.LENGTH_LONG).show();

        String module = "Requirement Engineering";
        String topic = "Requirement Elicitation";
        db.collection("questions").document("1")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //Question q = document.toObject(Question.class);
                    correctAnswer = document.getString("correct_answer");
                    //difficultyLevel = document.getString("difficulty_level");
                    buttonOption1.setText(document.getString("option1"));
                    buttonOption2.setText(document.getString("option2"));
                    buttonOption3.setText(document.getString("option3"));
                    buttonOption4.setText(document.getString("option4"));

                    textViewQuestion.setText(document.getString("question"));
                    textViewQuestionId.setText("Question : " + questionId + "  , Score : " + score);
                }

            }
        });*/

        /*.whereEqualTo("difficulty_level" , "Easy").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d : list){
                                Question q = d.toObject(Question.class);
                                q.setQuestionId(d.getId());
                                questionList.add(q);
                                Toast.makeText(getApplicationContext(),"Test", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }*/

    private void getAllQuestions(){
        //ArrayList<Question> questionList = new ArrayList<>();
        String module = c.getModule();
        String topic = c.getTopic();
        String difficulty = c.getDifficulty();
        /***
         * if difficulty is empty, get student's current stats and determine the difficulty level
         */
        double studentCurrentScore = 30.00;
        if (studentCurrentScore >= 0 && studentCurrentScore < 33) {
            difficulty = "Easy";
        }
        else if (studentCurrentScore >= 33 && studentCurrentScore < 66) {
            difficulty = "Medium";
        }
        else {
            difficulty = "Hard";
        }


       /* db.collection("questions")
                //.whereEqualTo("module", module)//.whereEqualTo("topic", topic)
                //.whereEqualTo("difficulty_level",difficulty)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d : list){
                                Question q = d.toObject(Question.class);
                                q.setQuestionId(d.getId());
                                questionList.add(q);
                                Log.d("test", q.getQuestion());
                            }
                        }
                    }
                });*/

        Firestore.db.collection("questions")
                //.whereEqualTo("module", module)
                //.whereEqualTo("topic", topic)
                //.whereEqualTo("difficulty", difficulty)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG, document.getId());
                        questionList.add(document.toObject(Question.class));
                    }
                } else {
                    Log.d(TAG, "Failed to retrieve");
                }
            }
        });

    }

    private void getRandom(){
        Integer total = 0;


        if(questionList.isEmpty()){
            Toast.makeText(this, "No Questions for this topic",Toast.LENGTH_LONG).show();
            //finish();

        }
        else {
            total = questionList.size();

            if (total >=  5) {
                maxQuestionId = 5;
            }
            else {
                maxQuestionId = total;
            }
            //Toast.makeText(this, String.valueOf(total),Toast.LENGTH_LONG).show();
            Set<Integer> randomSet = makeRandomSet(maxQuestionId, 0, total - 1);

            qNo = randomSet.toArray(new Integer[randomSet.size()]);
        }
        //Toast.makeText(this, String.valueOf(total),Toast.LENGTH_LONG).show();

    }
    private void getQuestion2() {
        //Log.d(TAG, String.valueOf(total));
        //Log.d(TAG, String.valueOf(qNo[0]));
        //Log.d(TAG, String.valueOf(qNo[1]));
        //Log.d(TAG, String.valueOf(qNo[2]));
        //Log.d(TAG, String.valueOf(qNo[3]));
        //Log.d(TAG, String.valueOf(qNo[4]));

        //Toast.makeText(this, String.valueOf(qNo[3]),Toast.LENGTH_LONG).show();

        correctAnswer = questionList.get(qNo[questionId-1]).getCorrect_answer();
        //difficultyLevel = document.getString("difficulty_level");
        buttonOption1.setText(questionList.get(qNo[questionId-1]).getOption1());
        buttonOption2.setText(questionList.get(qNo[questionId-1]).getOption2());
        buttonOption3.setText(questionList.get(qNo[questionId-1]).getOption3()); //qNo[questionId-1]
        buttonOption4.setText(questionList.get(qNo[questionId-1]).getOption4());

        textViewQuestion.setText(questionList.get(qNo[questionId-1]).getQuestion());
        textViewQuestionId.setText("Question : " + questionId + "  , Score : " + score);

    }

    public static Set<Integer> makeRandomSet(int howManyNumber, int startNumber, int endNumber){
        Set<Integer> integerSet = new HashSet<>();

        boolean couldBeAdded = false;
        for(int i=0; i< howManyNumber; i++) {
            while (!couldBeAdded) {
                Integer randomInt = randomInteger(startNumber, endNumber);
                couldBeAdded = integerSet.add(randomInt);
            }

            couldBeAdded = false;
        }

        return integerSet;
    }

    public static int randomInteger(int min, int max) {
        Random rd = new Random();
        return rd.nextInt((max - min) + 1) + min;
    }

    @Override
    public void onClick(View v) {

        calculateMarks(v);
    }

 /*   private void saveScore(){

            CollectionReference dbChallenges = db.collection("users/"+userId+"/challenges");
            Score s = new Score
                    (challengeId,userId , score , 5);

            dbChallenges.add(s)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(PlayChallengeActivity.this, "score Added", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PlayChallengeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
    }

    private void updateMark() {
        c.setMark(score);
        c.setIsComplete(true);
        db.collection("challenges").document(c.getId())
                .set(c)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PlayChallengeActivity.this, "Score Updated", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PlayChallengeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }
*/

    private void calculateMarks(View v) {
        switch (v.getId()) {

            case R.id.bt_START:
                textViewQuestionId.setVisibility(View.VISIBLE);
                textViewQuestion.setVisibility(View.VISIBLE);
                buttonOption1.setVisibility(View.VISIBLE);
                buttonOption2.setVisibility(View.VISIBLE);
                buttonOption3.setVisibility(View.VISIBLE);
                buttonOption4.setVisibility(View.VISIBLE);
                buttonStart.setVisibility(View.INVISIBLE);
                getRandom();
                getQuestion2();
                break;

            case R.id.bt_option1:
                if(correctAnswer.trim().equals(buttonOption1.getText())){
                    score += 1;
                }
                if (questionId < maxQuestionId){
                    questionId++;
                    getQuestion2();
                }
                else
                {
                    update();
                }
                break;
            case R.id.bt_option2:
                if(correctAnswer.trim().equals(buttonOption2.getText())){
                    score += 1;
                }
                if (questionId < maxQuestionId) {
                    questionId++;
                    getQuestion2();
                }
                else
                {
                    update();
                }
                break;
            case R.id.bt_option3:
                if(correctAnswer.trim().equals(buttonOption3.getText())){
                    score += 1;
                }
                if (questionId < maxQuestionId){
                    questionId++;
                    getQuestion2();
                }
                else
                {
                    update();
                }
                break;
            case R.id.bt_option4:
                if(correctAnswer.trim().equals(buttonOption4.getText())){
                    score += 1;
                }
                if (questionId < maxQuestionId){
                    questionId++;
                    getQuestion2();
                }
                else
                {
                    update();
                }
                break;
        }
    }


    private void update(){

        textViewQuestionId.setVisibility(View.INVISIBLE);
        textViewQuestion.setVisibility(View.INVISIBLE);
        buttonOption1.setVisibility(View.INVISIBLE);
        buttonOption2.setVisibility(View.INVISIBLE);
        buttonOption3.setVisibility(View.INVISIBLE);
        buttonOption4.setVisibility(View.INVISIBLE);
        buttonStart.setVisibility(View.INVISIBLE);
        findViewById(R.id.FragmentContainer).setVisibility(View.VISIBLE);


        Bundle bundle = new Bundle();
        bundle.putSerializable("CHALLENGE_OBJECT", c); //test
        bundle.putInt("SCORE",score);
        SubmitChallengeFragment submitChallengeFragment = new SubmitChallengeFragment();
        submitChallengeFragment.setArguments(bundle);
        PlayChallengeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, submitChallengeFragment).commit();


    }

}