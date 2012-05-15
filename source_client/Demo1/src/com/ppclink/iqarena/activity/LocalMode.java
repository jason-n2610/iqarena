package com.ppclink.iqarena.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.database.DatabaseHelper;
import com.ppclink.iqarena.object.QuestionLite;

public class LocalMode extends Activity implements OnClickListener {

	RadioButton mRbA, mRbB, mRbC, mRbD; // answer item
	RadioGroup mRgAnswer; // answer group
	TextView mTvQuestion, mTvQuestionTimer, mTvQuestionTitle, mTvScore;

	Button mBtnHelpX2, mBtnHelpRelease, mBtnHelp5050, mBtnSummit;
	AlertDialog mDialog;

	private final static int TIME_PER_QUESTION = 30000;

	private static int mScore = 0;

	private CountDownTimer mTimer;

	private int mCurrentQues = 0;

	private ArrayList<QuestionLite> mQuestions;

	private String tag = "LocalMode";
	// mang 15 phan tu tuong ung so diem cho moi cau
	private int[] mScoreLevels = { 10, 10, 10, 10, 10, 50, 100, 200, 500, 1000,
			2000, 3000, 5000, 8000, 2000 };

	DatabaseHelper mDataHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_mode);

		// question & timer
		mTvQuestionTitle = (TextView) findViewById(R.id.local_mode_tv_question_title);
		mTvQuestion = (TextView) findViewById(R.id.local_mode_tv_question_name);
		mTvQuestionTimer = (TextView) findViewById(R.id.local_mode_tv_timer);
		mTvScore = (TextView) findViewById(R.id.local_mode_tv_score);

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

		mBtnSummit = (Button) findViewById(R.id.local_mode_btn_summit);

		// event listener
		mBtnHelpX2.setOnClickListener(this);
		mBtnHelpRelease.setOnClickListener(this);
		mBtnHelp5050.setOnClickListener(this);
		mRbA.setOnClickListener(this);
		mRbB.setOnClickListener(this);
		mRbC.setOnClickListener(this);
		mRbD.setOnClickListener(this);
		mBtnSummit.setOnClickListener(this);

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
			Log.i(tag, mQuestions.get(i).getQuesName() + " \n"
					+ mQuestions.get(i).getQuesType());
		}
		mDataHelper.close();
		// --> ket thuc lay du lieu

		// hien thong bao san sang
		if (mDialog == null) {
			Builder builder = new Builder(this);
			builder.setTitle("Confirm");
			builder.setMessage("Are you ready!!!");
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

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

							// bat du tinh thoi gian
							mTimer = new CountDownTimer(TIME_PER_QUESTION, 1000) {

								@Override
								public void onTick(long millisUntilFinished) {

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
									showDialogTimeLimit();
								}
							};
							mTimer.start();
						}
					});
			builder.create().show();
		}

		// init UI
		initUISuportMultiScreen();
	}

	private void initUISuportMultiScreen() {
		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		if (screenWidth == 1280 || screenWidth == 800) {
			mTvQuestion.setTextSize(24);
			mTvQuestionTimer.setTextSize(48);

			mRbA.setHeight(56);
			mRbA.setTextSize(20);
			mRbB.setHeight(56);
			mRbB.setTextSize(20);
			mRbC.setHeight(56);
			mRbC.setTextSize(20);
			mRbD.setHeight(56);
			mRbD.setTextSize(20);

			mBtnHelp5050.setTextSize(20);
			mBtnHelpRelease.setTextSize(20);
			mBtnHelpX2.setTextSize(20);
			mBtnSummit.setTextSize(20);

			mBtnHelp5050.setWidth(160);
			mBtnHelpRelease.setWidth(160);
			mBtnHelpX2.setWidth(160);
			mBtnSummit.setWidth(160);

			mBtnHelp5050.setHeight(60);
			mBtnHelpRelease.setHeight(60);
			mBtnHelpX2.setHeight(60);
			mBtnSummit.setHeight(60);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.local_mode_btn_help_50_50:
			mBtnHelp5050.setEnabled(false);
			int trueAnswer = mQuestions.get(mCurrentQues).getAnswer() - 1;
			ArrayList<Integer> sample = new ArrayList<Integer>(4);
			sample.add(1);
			sample.add(2);
			sample.add(3);
			sample.add(0);
			sample.remove(new Integer(trueAnswer));
			Collections.shuffle(sample);
			mRgAnswer.getChildAt(sample.get(0)).setEnabled(false);
			mRgAnswer.getChildAt(sample.get(1)).setEnabled(false);
			break;
		case R.id.local_mode_btn_help_release:
			// cau binh thuong
			if (mCurrentQues != 14){
				mBtnHelpRelease.setEnabled(false);
				showDialogTrueAnswer(1);
			}
			else{
				// cau thu 15
				Toast.makeText(this, "Do not use for question 15", 800).show();
			}
			break;
		case R.id.local_mode_btn_help_x2:
			mBtnHelpX2.setEnabled(false);
			mScoreLevels[mCurrentQues] = mScoreLevels[mCurrentQues] * 2;

			break;
		case R.id.local_mode_rb_answer_a:

			break;
		case R.id.local_mode_rb_answer_b:

			break;
		case R.id.local_mode_rb_answer_c:

			break;
		case R.id.local_mode_rb_answer_d:

			break;

		case R.id.local_mode_btn_summit:
			if (mTimer != null) {
				// disable answer
				int count = mRgAnswer.getChildCount();
				for (int i = 0; i < count; i++) {
					mRgAnswer.getChildAt(i).setEnabled(false);
				}
				mTimer.cancel();

				// check answer
				int answer = mRgAnswer.indexOfChild(findViewById(mRgAnswer
						.getCheckedRadioButtonId())) + 1;
				if (answer == mQuestions.get(mCurrentQues).getAnswer()) {
					if (mCurrentQues != 14){
						// hien thi dialog tra loi dung
						showDialogTrueAnswer(0);
					}
					else{
						// victory
						showDialogVictory();
					}
				} else {
					// hien thi dialog tra loi sai
					showDialogFalseAnswer();
				}
			}
			break;

		default:
			break;
		}
	}

	private void showDialogTrueAnswer(int type) {
		// type 0 la cho truong hop tra loi dung
		// type 1 cho truong hop help release
		// lay ve ket qua dung
		String result = getAnswer();
		if (result == null){
			return;
		}

		// score
		if (type != 1){
			mScore = mScore + mScoreLevels[mCurrentQues];
		}
		// hien thi score
		mTvScore.setText(String.valueOf(mScore));

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Result");
		if (type == 0){
			builder.setMessage(result + " is TRUE answer. Your score is: " + mScore
					+ "\nReady for next question?");
		}
		else if (type == 1){
			builder.setMessage("You have choose help release. "+ result + " is TRUE answer. Your score is: " + mScore
					+ "\nReady for next question?");
		}
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				int count = mRgAnswer.getChildCount();
				for (int i = 0; i < count; i++) {
					mRgAnswer.getChildAt(i).setEnabled(true);
				}
				mRgAnswer.clearCheck();

				int size = mQuestions.size();
				if ((mCurrentQues + 1) > size) {
					return;
				}

				mCurrentQues++;

				// hien thi cau hoi
				QuestionLite question = mQuestions.get(mCurrentQues);
				mTvQuestionTitle.setText("Question "
						+ String.valueOf(mCurrentQues + 1));
				mTvQuestion.setText(question.getQuesName());
				mRbA.setText(question.getAnswerA());
				mRbB.setText(question.getAnswerB());
				mRbC.setText(question.getAnswerC());
				mRbD.setText(question.getAnswerD());

				// bat du tinh thoi gian
				mTimer = new CountDownTimer(TIME_PER_QUESTION, 1000) {

					@Override
					public void onTick(long millisUntilFinished) {

						// dem thoi gian
						int time = (int) millisUntilFinished / 1000;
						if (time > 20) {
							mTvQuestionTimer.setTextColor(Color.GREEN);
						} else if (time > 10) {
							mTvQuestionTimer.setTextColor(Color.YELLOW);
						} else {
							mTvQuestionTimer.setTextColor(Color.RED);
						}
						mTvQuestionTimer.setText(String
								.valueOf(millisUntilFinished / 1000));
					}

					@Override
					public void onFinish() {
						showDialogTimeLimit();
					}
				};
				mTimer.start();

			}
		});
		dialog = builder.create();
		dialog.show();
	}

	private void showDialogFalseAnswer() {
		mBtnHelp5050.setEnabled(false);
		mBtnHelpRelease.setEnabled(false);
		mBtnHelpX2.setEnabled(false);
		mBtnSummit.setEnabled(false);
		
		String result = getAnswer();
		if (result == null){
			return;
		}

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Result");
		builder.setMessage("You are FALSE. True answer is: " + result);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// hien thi ket qua
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	private void showDialogVictory(){
		
		// lay ve ket qua dung
		String result = getAnswer();
		if (result == null){
			return;
		}

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Victory");
		builder.setMessage(result + " is TRUE answer. You are victory!");
		builder.setPositiveButton("Show award", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// hien thi ket qua
				
			}
		});
		dialog = builder.create();
		dialog.show();
		
	}
	
	private void showDialogTimeLimit() {
		int count = mRgAnswer.getChildCount();
		for (int i = 0; i < count; i++) {
			mRgAnswer.getChildAt(i).setEnabled(false);
		}

		mBtnHelp5050.setEnabled(false);
		mBtnHelpRelease.setEnabled(false);
		mBtnHelpX2.setEnabled(false);
		mBtnSummit.setEnabled(false);

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Result");
		builder.setMessage("Time limited. You are lost");
		builder.setPositiveButton("Show result",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// hien thi ket qua
					}
				});
		dialog = builder.create();
		dialog.show();
	}
	
	private String getAnswer(){
		String result = null;
		int answer = mQuestions.get(mCurrentQues).getAnswer();
		switch (answer) {
		case 1:
			result = "A";
			break;
		case 2:
			result = "B";
			break;
		case 3:
			result = "C";
			break;
		case 4:
			result = "D";
			break;

		default:
			result = null;
			break;
		}
		return result;
	}

	public ArrayList<QuestionLite> insertDataToDB() {
		ArrayList<QuestionLite> questions = new ArrayList<QuestionLite>();
		try {
			for (int i = 1; i < 57; i++) {

				InputStream is = this.getAssets().open(i + ".txt");
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is), 8192);
				String line;
				QuestionLite question;
				ArrayList<String> temp = new ArrayList<String>();
				while ((line = br.readLine()) != null) {
					temp.add(line);
				}
				int size = temp.size();
				if (size == 6) {
					String quesName = temp.get(0);
					if (quesName.contains(".")) {
						quesName = quesName
								.substring(quesName.indexOf(".") + 1);
					}
					if (quesName.contains("'")) {
						quesName = quesName.replace("'", " ");
					}
					String a = temp.get(1);
					if (a.contains("'")) {
						a = a.replace("'", "-");
					}
					String b = temp.get(2);
					if (b.contains("'")) {
						b = b.replace("'", "-");
					}
					String c = temp.get(3);
					if (c.contains("'")) {
						c = c.replace("'", "-");
					}
					String d = temp.get(4);
					if (d.contains("'")) {
						d = d.replace("'", "-");
					}
					int answer;
					if (temp.get(5).trim().equals("a")) {
						answer = 1;
					} else if (temp.get(5).trim().equals("b")) {
						answer = 2;
					} else if (temp.get(5).trim().equals("c")) {
						answer = 3;
					} else if (temp.get(5).trim().equals("d")) {
						answer = 4;
					} else {
						answer = 0;
					}
					question = new QuestionLite(0, quesName, 15, a, b, c, d,
							answer, "");
					questions.add(question);
				}
			}
			// int size = questions.size();
			// Log.i(tag, "size insert: "+size);
			// for (int i=0; i<size; i++){
			// Log.i(tag, "question: "+questions.get(i).getQuesName());
			// }

		} catch (IOException e) {
			Log.e(tag, e.getMessage());
		}
		return questions;
	}

}
