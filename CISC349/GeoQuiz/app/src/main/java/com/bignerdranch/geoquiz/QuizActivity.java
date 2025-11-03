package com.bignerdranch.geoquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mScoreTextView;
    private TextView mUsesTextView;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private static final String KEY_SCORE = "score";

    private static final String KEY_USES = "uses";
    public static final String EXTRA_MESSAGE = "com.bignerdranch.geoquiz.MESSAGE";
    private static final int REQUEST_CODE_CHEAT = 0;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private int totalCount;
    private int score;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate(Bundle) called");

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                Intent i = CheatActivity.newIntent(QuizActivity.this,
                        mQuestionBank[mCurrentIndex].ismAnswerTrue() );
                String message = "Hello from MainActivity";
                i.putExtra(EXTRA_MESSAGE, message);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mScoreTextView = (TextView) findViewById(R.id.score_text_view);
        mUsesTextView = (TextView) findViewById(R.id.uses_text_view);

        prefs = getPreferences(Context.MODE_PRIVATE);
        editor = prefs.edit();


        if (savedInstanceState == null) {
            totalCount = prefs.getInt(KEY_USES, 0);
            totalCount++;
            editor.putInt(KEY_USES, totalCount);
            editor.commit();
        }
        else
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            score = savedInstanceState.getInt(KEY_SCORE, 0);
            totalCount = savedInstanceState.getInt(KEY_USES, 0);
        }

        mUsesTextView.setText(Integer.toString(totalCount));
        mScoreTextView.setText(Integer.toString(score));
        Log.d(TAG, "Total count: " + totalCount);
        Log.d(TAG, "Score: " + score);
        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_SCORE, score);
        savedInstanceState.putInt(KEY_USES, totalCount);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getmTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            score++;
            mScoreTextView.setText(Integer.toString(score));
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHEAT){
            Log.d(TAG, Integer.toString(requestCode));
            if (null != data && data.getStringExtra("result") != null) {
                int messageResId = R.string.judgment_toast;
                Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }
}