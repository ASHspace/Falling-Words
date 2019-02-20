package com.ashr.fallingwords.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ashr.fallingwords.R;
import com.ashr.fallingwords.helper.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView mTextView, mOne, mTimer, mAnswer, tcorrect, twrong, tQuestion;
    Button mButton, mCorrect, mWrong, mReset, mResult;
    ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
    AnimationSet animationSet;
    int i = 0, status = 0, corr = 0, wro = 0;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.t_two);
        mOne = findViewById(R.id.t_one);
        mButton = findViewById(R.id.b_start);
        mTimer = findViewById(R.id.t_timer);
        mCorrect = findViewById(R.id.b_correct);
        mWrong = findViewById(R.id.b_wrong);
        mAnswer = findViewById(R.id.t_answer);
        tcorrect = findViewById(R.id.t_correct);
        twrong = findViewById(R.id.t_wrong);
        tQuestion = findViewById(R.id.t_answer);
        mReset = findViewById(R.id.b_reset);
        mResult = findViewById(R.id.b_result);

        mResult.setEnabled(false);

        JSONHelper jsonHelper = new JSONHelper(this);
        animationSet = (AnimationSet) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);


        // Counter for timer
        final CountDownTimer countDownTimer = new CountDownTimer(10 * 1000, 100) {
            public void onTick(long millisUntilFinished) {
                String padTime = String.format("%02d", millisUntilFinished / 100);
                mTimer.setText("Time: " + padTime);
            }

            public void onFinish() {
                mTimer.setText("Time: 00");
            }
        };

        //AnimationSet, used single instance for single onAnimationEnd
        animationSet.getAnimations().get(1).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCorrect.setEnabled(true);
                mWrong.setEnabled(true);
                mOne.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.VISIBLE);
                String padded = String.format("%02d", i);
                tQuestion.setText("Question No: " + padded);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (status == 1) {
                    countDownTimer.start();
                    setNewView();
                }

            }


            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //click event for multiple button

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowResult();
                i = 0;
                status = 0;
                corr = 0;
                wro = 0;
                countDownTimer.cancel();
                mTimer.setText("Time: 00");
                String padded = String.format("%02d", corr);
                tcorrect.setText("Correct: " + padded);
                padded = String.format("%02d", wro);
                twrong.setText("Wrong: " + padded);
                mButton.setText("START");
                mTextView.clearAnimation();
                mOne.setVisibility(View.INVISIBLE);
                mTextView.setVisibility(View.INVISIBLE);
                mCorrect.setEnabled(false);
                mWrong.setEnabled(false);
                mResult.setEnabled(false);
                tQuestion.setVisibility(View.INVISIBLE);
            }
        });

        mResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowResult();
            }
        });

        mCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formList.get(i - 1).get("text_spa") == mTextView.getText()) {
                    CustomToast("Correct Answer", 30, 1);
                    corr++;
                    String padded = String.format("%02d", corr);
                    tcorrect.setText("Correct: " + padded);
                    countDownTimer.start();
                    setNewView();

                } else {
                    CustomToast("Wrong Answer", 30, 2);
                    wro++;
                    status = 1;
                    String padded = String.format("%02d", wro);
                    twrong.setText("Wrong: " + padded);
                    countDownTimer.start();
                    setNewView();

                }

            }
        });

        mWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formList.get(i - 1).get("text_spa") != mTextView.getText()) {
                    CustomToast("Correct Answer", 30, 1);
                    corr++;
                    String padded = String.format("%02d", corr);
                    tcorrect.setText("Correct: " + padded);
                    countDownTimer.start();
                    setNewView();
                } else {
                    CustomToast("Wrong Answer", 30, 2);
                    wro++;
                    status = 1;
                    String padded = String.format("%02d", wro);
                    twrong.setText("Wrong: " + padded);
                    countDownTimer.start();
                    setNewView();
                }
            }
        });


        //JSON Array from JSONHelper
        try {
            String jsonString = jsonHelper.loadJSONFromAsset();
            JSONArray jsonArray = new JSONArray(jsonString);
            HashMap<String, String> mlist;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo_inside = jsonArray.getJSONObject(i);

                String formula_value = jo_inside.getString("text_eng");
                String url_value = jo_inside.getString("text_spa");

                mlist = new HashMap<String, String>();
                mlist.put("text_eng", formula_value);
                mlist.put("text_spa", url_value);

                formList.add(mlist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 0) {
                    countDownTimer.start();
                    setNewView();
                    status = 1;
                    tQuestion.setVisibility(View.VISIBLE);
                    mButton.setText("PAUSE");
                    mResult.setEnabled(true);

                } else {
                    status = 0;
                    countDownTimer.cancel();
                    mTextView.clearAnimation();
                    mCorrect.setEnabled(false);
                    mWrong.setEnabled(false);
                    mButton.setText("START");
                }


            }
        });

        mOne.setText(formList.get(0).get("text_eng"));
        mTextView.setText(formList.get(0).get("text_spa"));

        mOne.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
    }

    //Result dialog for user
    private void ShowResult() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("RESULT")
                .setMessage("Total Question:  " + i +
                        "\nCorrect Answer:  " + corr +
                        "\nWrong Answer:  " + wro +
                        "\nNo Answer:  " + (i - (corr + wro)))
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    private void setNewView() {
        int n = rand.nextInt(296);
        mCorrect.setEnabled(false);
        mWrong.setEnabled(false);
        mOne.setText(formList.get(i).get("text_eng"));
        mTextView.setText(formList.get(n).get("text_spa"));
        i++;
        mTextView.startAnimation(animationSet);

    }

    //Custom Toast message for user feedback
    private void CustomToast(String tText, int Size, int tColor) {
        Toast toast = Toast.makeText(this, tText, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (tColor == 1) {
            v.setTextColor(Color.rgb(102, 255, 51));
        } else {
            v.setTextColor(Color.rgb(255, 26, 26));
        }
        v.setTextSize(Size);
        v.setGravity(Gravity.CENTER);
        v.setShadowLayer(1.5f, -1, 1, Color.BLACK);

        toast.setGravity(Gravity.BOTTOM, 0, 420);
        toast.show();
    }

}

