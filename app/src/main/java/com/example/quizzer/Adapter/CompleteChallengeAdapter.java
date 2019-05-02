package com.example.quizzer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quizzer.Model.Challenge;
import com.example.quizzer.R;

import java.io.Serializable;
import java.util.List;

/*
This activity will be called after user finished a game/ quiz
 */

public class CompleteChallengeAdapter extends RecyclerView.Adapter<CompleteChallengeAdapter.ChallengeViewHolder> implements Serializable {

    private Context mCtx;
    private List<Challenge> challengeList;
    private String challengeId;
    private String userId;
    Challenge c;

    public CompleteChallengeAdapter(Context mCtx, List<Challenge> challengeList, String userId) {
        this.mCtx = mCtx;
        this.challengeList = challengeList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChallengeViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.layout_complete_challenge, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);

        //holder.textViewChallengeId.setText("Challenge Id : " + challenge.getId());
        //holder.textViewCreator.setText("Challenger : " + challenge.getCreatorId());
        //holder.textViewReceiver.setText("Receiver : " + challenge.getReceiverId());
        holder.textViewModule.setText("Module : " + challenge.getModule());
        holder.textViewTopic.setText("Topic : " + challenge.getTopic());
        //holder.textViewDifficulty.setText("Difficulty : " + challenge.getDifficulty());
        //holder.textViewType.setText("Type : " + challenge.getType());
        holder.textViewScore.setText("Score : " + challenge.getMark());


        c = new Challenge(challenge.getCreatorId(), challenge.getReceiverId(), challenge.getModule(),
                challenge.getTopic(),challenge.getDifficulty(),challenge.getType(), challenge.getMark(), challenge.getIsComplete());

        c.setId(challenge.getId());


    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    class ChallengeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewChallengeId,textViewCreator, textViewReceiver, textViewModule, textViewTopic, textViewDifficulty,textViewType,textViewScore;

        public ChallengeViewHolder(View itemView) {
            super(itemView);

            //textViewChallengeId = itemView.findViewById(R.id.textview_challengeId);
            //textViewCreator = itemView.findViewById(R.id.textview_creator);
            //textViewReceiver = itemView.findViewById(R.id.textview_receiver);
            textViewModule = itemView.findViewById(R.id.textview_module);
            textViewTopic = itemView.findViewById(R.id.textview_topic);
            //textViewDifficulty = itemView.findViewById(R.id.textview_difficulty);
            //textViewType = itemView.findViewById(R.id.textview_type);
            textViewScore = itemView.findViewById(R.id.textview_score);


            itemView.setOnClickListener(this);
              //c.setId(challengeId);
            }

            @Override
            public void onClick(View v) {

            }
    }

}