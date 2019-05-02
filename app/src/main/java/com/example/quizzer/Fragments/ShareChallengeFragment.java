package com.example.quizzer.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizzer.Activities.HomeActivity;
import com.example.quizzer.CommonActions;
import com.example.quizzer.Model.Challenge;
import com.example.quizzer.R;
import com.example.quizzer.mail.SendMail;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.share.widget.ShareDialog.Mode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/*
This fragment will be called when a user want to share a challenge
 */

public class ShareChallengeFragment extends Fragment implements
        View.OnClickListener
{

    private static final String TAG = "ShareChallengeFragment";
    Challenge c;
    Integer score;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_challenge, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            c = (Challenge)bundle.getSerializable("CHALLENGE_OBJECT");
            score = bundle.getInt("SCORE");
        }

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        view.findViewById(R.id.btn_shareFacebook).setOnClickListener(this);
        view.findViewById(R.id.btn_shareTwitter).setOnClickListener(this);
        view.findViewById(R.id.btn_backToHome).setOnClickListener(this);
        view.findViewById(R.id.btn_notifyLecturer).setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shareFacebook:
                FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setQuote("I got " + score + " point(s) on " + c.getModule() + ", " + c.getTopic() + " challenged by " + c.getCreatorId() + " !!")
                            .build();
                    shareDialog.show(content, Mode.AUTOMATIC);
                }
                break;
            case R.id.btn_shareTwitter:
                shareTwitter("I got " + score + " point(s) on " + c.getModule() + ", " + c.getTopic() + " challenged by " + c.getCreatorId() + " !!");
                break;

            case R.id.btn_backToHome:
                this.getActivity().finish();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_notifyLecturer:
                CommonActions.getUserEmail(c.getCreatorId());

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendEmail();
                        Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_LONG).show();
                    }
                }, 2000);
                break;
        }
    }



    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.");
        tweetIntent.setType("text/plain");

        PackageManager packManager = getActivity().getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(getActivity(), "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "UTF-8 should always be supported", e);
            return "";
        }
    }

    private void sendEmail() {
        Log.d(TAG, "Receiver email address => " + CommonActions.userEmail);
        String email = CommonActions.userEmail;
        String subject = "Challenge completed";
        String message = "Someone has completed your challenge";

        //Creating SendMail object
        SendMail sm = new SendMail(getActivity(), email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

}
