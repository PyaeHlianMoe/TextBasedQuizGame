package com.example.quizzer.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.Activities.PlayChallengeActivity;
import com.example.quizzer.Fragments.ModifyChallengeFragment;
import com.example.quizzer.Model.Challenge;
import com.example.quizzer.CommonActions;
import com.example.quizzer.R;

import java.io.Serializable;
import java.util.List;

/*
This is the adapter that will be used for all challenge related activities/ fragments
 */

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> implements Serializable {

    private Context mCtx;
    private List<Challenge> challengeList;
    private String challengeId;
    private int selectedChallengeIndex;
    private String userId;
    Challenge c;

    public ChallengeAdapter(Context mCtx, List<Challenge> challengeList, String userId) {
        this.mCtx = mCtx;
        this.challengeList = challengeList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChallengeViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.layout_challenge, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);

        holder.textViewChallengeId.setText("Challenge Id : " + challenge.getId());
        holder.textViewCreator.setText("Challenger : " + challenge.getCreatorId());
        holder.textViewReceiver.setText("Receiver : " + challenge.getReceiverId());
        holder.textViewModule.setText("Module : " + challenge.getModule());
        holder.textViewTopic.setText("Topic : " + challenge.getTopic());
        holder.textViewDifficulty.setText("Difficulty : " + challenge.getDifficulty());

        c = new Challenge(challenge.getCreatorId(), challenge.getReceiverId(), challenge.getModule(),
                challenge.getTopic(),challenge.getDifficulty(),challenge.getType(), 0, false);

        c.setId(challenge.getId());
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    class ChallengeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewChallengeId,textViewCreator, textViewReceiver, textViewModule, textViewTopic, textViewDifficulty,textViewType;

        public ChallengeViewHolder(View itemView) {
            super(itemView);

            textViewChallengeId = itemView.findViewById(R.id.textview_challengeId);
            textViewCreator = itemView.findViewById(R.id.textview_creator);
            textViewReceiver = itemView.findViewById(R.id.textview_receiver);
            textViewModule = itemView.findViewById(R.id.textview_module);
            textViewTopic = itemView.findViewById(R.id.textview_topic);
            textViewDifficulty = itemView.findViewById(R.id.textview_difficulty);

            itemView.setOnClickListener(this);
              //c.setId(challengeId);
            }

        @Override
        public void onClick(View v) {
            // Go to the challenge to play a game
            if (CommonActions.userType.equals("Student")) {
                Intent intent = new Intent(mCtx, PlayChallengeActivity.class);
                intent.putExtra("CHALLENGE_ID", challengeId);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("CHALLENGE_OBJECT" , c);
                mCtx.startActivity(intent);
            } else {
                // Go to modify the challenge
                Challenge challenge = challengeList.get(getAdapterPosition());

                Bundle bundle = new Bundle();
                bundle.putString("ID", challenge.getId());
                bundle.putString("MODULE", challenge.getModule());
                bundle.putString("TOPIC", challenge.getTopic());
                bundle.putString("RECEIVER", challenge.getReceiverId());
                bundle.putString("DIFFICULTY", challenge.getDifficulty());
                ModifyChallengeFragment modifyChallengeFragment = new ModifyChallengeFragment();
                modifyChallengeFragment.setArguments(bundle);
                HomeActivity.fragmentManager.beginTransaction().replace(R.id.FragmentContainer, modifyChallengeFragment).commit();
            }

        }


    }

}