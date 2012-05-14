package com.ppclink.iqarena.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.database.DatabaseHelper;
import com.ppclink.iqarena.object.QuestionLite;

public class LocalMode extends Activity implements OnClickListener {

	RadioButton mRbA, mRbB, mRbC, mRbD; // answer item
	RadioGroup mRgAnswer; // answer group
	TextView mTvQuestion, mTvQuestionTimer, mTvQuestionTitle;

	Button mBtnHelpX2, mBtnHelpRelease, mBtnHelp5050;
	AlertDialog mDialog;

	private final static int TIME_PER_QUESTION = 30000;

	private CountDownTimer mTimer;

	private int mCurrentQues = 0;

	private ArrayList<QuestionLite> mQuestions;

	private String tag = "LocalMode";

	DatabaseHelper mDataHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_mode);

		// question & timer
		mTvQuestionTitle = (TextView) findViewById(R.id.local_mode_tv_question_title);
		mTvQuestion = (TextView) findViewById(R.id.local_mode_tv_question_name);
		mTvQuestionTimer = (TextView) findViewById(R.id.local_mode_tv_timer);

		// button help
		mBtnHelpX2 = (Button) findViewById(R.id.local_mode_btn_help_x2);
		mBtnHelpRelease = (Button) findViewById(R.id.local_mode_btn_help_release);
		mBtnHelp5050 = (Button) findViewById(R.id.local_mode_btn_help_50_50);

		// answer
		mRgAnswer = (RadioGroup) findViewById(R.id.local_mode_rg_answer);
		mRbA = (RadioButton) findViewById(R.id.local_mode_rb_answer_a);
		mRbB = (RadioButton) findViewById(R.id.local_mode_rb_answer_b);
		mRbC = (RadioButton) findViewById(R.id.local_mode_rb_answer_c);
		mRbD = (RadioButton) findViewById(R.id.local_mode_rb_answer_d);

		// event listener
		mBtnHelpX2.setOnClickListener(this);
		mBtnHelpRelease.setOnClickListener(this);
		mBtnHelp5050.setOnClickListener(this);
		mRbA.setOnClickListener(this);
		mRbB.setOnClickListener(this);
		mRbC.setOnClickListener(this);
		mRbD.setOnClickListener(this);

		// connect database and get data
		if (mDataHelper == null) {
			mDataHelper = new DatabaseHelper(this);
		}
		try {
			mDataHelper.createDataBase();
		} catch (IOException e) {
			Log.e(tag, e.getMessage());
		}

		try {
			mDataHelper.openDataBase();
		} catch (SQLException e) {
			Log.e(tag, e.getMessage());
		}
		mQuestions = mDataHelper.getData();
		int size = mQuestions.size();
		Log.i(tag, "size: " + size);
		for (int i = 0; i < size; i++) {
			Log.i(tag, mQuestions.get(i).getQuesName());
		}
		mDataHelper.close();
		// --> ket thuc lay du lieu

		// hien thong bao san sang
		if (mDialog == null) {
			Builder builder = new Builder(this);
			builder.setTitle("Info");
			builder.setMessage("Are you ready!!!");
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mTimer = new CountDownTimer(TIME_PER_QUESTION, 1000) {

								@Override
								public void onTick(long millisUntilFinished) {
									// hien thi cau hoi
									if (mQuestions == null) {
										return;
									}
									QuestionLite question = mQuestions
											.get(mCurrentQues);
									mTvQuestionTitle.setText("Question "
											+ String.valueOf(mCurrentQues + 1));
									mTvQuestion.setText(question.getQuesName());
									mRbA.setText(question.getAnswerA());
									mRbB.setText(question.getAnswerB());
									mRbC.setText(question.getAnswerC());
									mRbD.setText(question.getAnswerD());

									// dem thoi gian
									int time = (int) millisUntilFinished / 1000;
									if (time > 20) {
										mTvQuestionTimer
												.setTextColor(Color.GREEN);
									} else if (time > 10) {
										mTvQuestionTimer
												.setTextColor(Color.YELLOW);
									} else {
										mTvQuestionTimer
												.setTextColor(Color.RED);
									}
									mTvQuestionTimer.setText(String
											.valueOf(millisUntilFinished / 1000));
								}

								@Override
								public void onFinish() {

								}
							};
						}
					});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.local_mode_btn_help_50_50:

			break;
		case R.id.local_mode_btn_help_release:

			break;
		case R.id.local_mode_btn_help_x2:

			break;
		case R.id.local_mode_rb_answer_a:
			showDialogConfirmAnswer();
			
			break;
		case R.id.local_mode_rb_answer_b:
			showDialogConfirmAnswer();

			break;
		case R.id.local_mode_rb_answer_c:
			showDialogConfirmAnswer();

			break;
		case R.id.local_mode_rb_answer_d:
			showDialogConfirmAnswer();

			break;

		default:
			break;
		}
	}
	
	private void showDialogConfirmAnswer(){
		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Confirm");
		builder.setMessage("Are you sure?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto- method stub
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}
	
	private void showDialogConfirmNextQuestion(){
		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Confirm");
		builder.setMessage("Are you ready for question "+ (mCurrentQues+1) +"?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto- method stub
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}

}
