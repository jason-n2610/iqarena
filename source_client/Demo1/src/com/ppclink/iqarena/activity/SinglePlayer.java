package com.ppclink.iqarena.activity;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.communication.CheckServer;
import com.ppclink.iqarena.communication.RequestServer;
import com.ppclink.iqarena.communication.RequestServer.REQUEST_TYPE;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.Question;
import com.ppclink.iqarena.object.QuestionLite;
import com.ppclink.iqarena.ultil.FilterResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SinglePlayer extends Activity implements OnClickListener, IRequestServer {

	RadioButton mRbA, mRbB, mRbC, mRbD; // answer item
	RadioGroup mRgAnswer; // answer group
	TextView mTvQuestion, mTvQuestionTimer, mTvQuestionTitle, mTvScore;
	private ViewFlipper mVfMain;
	Button mBtnHelpX2, mBtnHelpRelease, mBtnHelp5050, mBtnSummit,
			mBtnHelpChangeQuestion;
	AlertDialog mDialog;

	ListView mLvRanks;

	private final static int TIME_PER_QUESTION = 30000;

	private int mScore = 0;

	private CountDownTimer mTimer;

	private int mCurrentQues = 0;
	
	String mQuestionId = null;
	
	private String tag = "SinglePlayer";
	
	RequestServer mRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_mode);

		mVfMain = (ViewFlipper) findViewById(R.id.local_mode_vf_main);

		// question & timer
		mTvQuestionTitle = (TextView) findViewById(R.id.local_mode_tv_question_title);
		mTvQuestion = (TextView) findViewById(R.id.local_mode_tv_question_name);
		mTvQuestionTimer = (TextView) findViewById(R.id.local_mode_tv_timer);
		mTvScore = (TextView) findViewById(R.id.local_mode_tv_score);

		// button help
		mBtnHelpX2 = (Button) findViewById(R.id.local_mode_btn_help_x2);
		mBtnHelpRelease = (Button) findViewById(R.id.local_mode_btn_help_release);
		mBtnHelp5050 = (Button) findViewById(R.id.local_mode_btn_help_50_50);
		mBtnHelpChangeQuestion = (Button) findViewById(R.id.local_mode_btn_help_change_question);

		// answer
		mRgAnswer = (RadioGroup) findViewById(R.id.local_mode_rg_answer);
		mRbA = (RadioButton) findViewById(R.id.local_mode_rb_answer_a);
		mRbB = (RadioButton) findViewById(R.id.local_mode_rb_answer_b);
		mRbC = (RadioButton) findViewById(R.id.local_mode_rb_answer_c);
		mRbD = (RadioButton) findViewById(R.id.local_mode_rb_answer_d);

		mBtnSummit = (Button) findViewById(R.id.local_mode_btn_summit);

		mLvRanks = (ListView) findViewById(R.id.local_mode_lv_rank);

		// event listener
		mBtnHelpX2.setOnClickListener(this);
		mBtnHelpRelease.setOnClickListener(this);
		mBtnHelp5050.setOnClickListener(this);
		mRbA.setOnClickListener(this);
		mRbB.setOnClickListener(this);
		mRbC.setOnClickListener(this);
		mRbD.setOnClickListener(this);
		mBtnSummit.setOnClickListener(this);
		mBtnHelpChangeQuestion.setOnClickListener(this);

		mBtnHelpChangeQuestion.setText("? ? ?");

		initUISuportMultiScreen();
		
		if (mRequest == null){
			mRequest = new RequestServer(this);
			mRequest.getQuestionByType(mCurrentQues+1);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mTimer != null){
			mTimer.cancel();
		}
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

			mBtnHelp5050.getLayoutParams().width = 160;
			mBtnHelpChangeQuestion.getLayoutParams().width = 160;
			mBtnHelpRelease.getLayoutParams().width = 160;
			mBtnHelpX2.getLayoutParams().width = 160;

			mBtnSummit.setWidth(160);

			mBtnHelp5050.setHeight(60);
			mBtnHelpRelease.setHeight(60);
			mBtnHelpX2.setHeight(60);
			mBtnSummit.setHeight(60);
			mBtnHelpChangeQuestion.setHeight(60);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.local_mode_btn_summit:
			if (mRequest != null){
				if (!mRequest.isCancelled()){
					mRequest.cancel(true);	
				}
			}
			mRequest = new RequestServer(this);
			break;

		default:
			break;
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		// request lay ve cau hoi hoan tat
		if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_GET_QUESTION_BY_TYPE) {
			if (sResult != null) {
				try {
					FilterResponse.filter(sResult);
					// co cau hoi tra ve
					if (FilterResponse.value) {
						Question question = FilterResponse.question;
						mQuestionId = question.getmStrId();
						mTvQuestion.setText(question.getmStrContent());
						mRbA.setText(question.getmStrAnswerA());
						mRbB.setText(question.getmStrAnswerB());
						mRbC.setText(question.getmStrAnswerC());
						mRbD.setText(question.getmStrAnswerD());
						mTimer = new CountDownTimer(TIME_PER_QUESTION, 1000) {

							@Override
							public void onFinish() {
								showDialogTimeLimit();
							}

							@Override
							public void onTick(long millisUntilFinished) {
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
						};
						mTimer.start();

					}
					// truong hop ko lay dc cau hoi, dua ra thong bao
					else {
						Toast.makeText(this,
								"sorry, do not create question, please try again",
								500).show();
					}
				} catch (Exception e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Error");
					builder.setMessage(sResult);
					builder.create().show();
				}
			}
		}
	}
	


	private void showDialogTrueAnswer(int type) {
		// type 0 la cho truong hop tra loi dung
		// type 1 cho truong hop help release
		// lay ve ket qua dung
		if (type == 0){
//			mScore = mScore + mScoreLevels[mCurrentQues];
		}
		else{
			mScore = mScore * 3 / 4;
		}
		// hien thi score
		mTvScore.setText("Score: " + String.valueOf(mScore));

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Result");
		if (type == 0){
			builder.setMessage( " is TRUE answer. \nYour score is: " + mScore
					+ "\nReady for next question?");
		}
		else if (type == 1){
			builder.setMessage("You have choose help release. \n" + " is TRUE answer. \nYour score is: " + mScore
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

				mCurrentQues++;				
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
		mBtnHelpChangeQuestion.setEnabled(false);
		

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Result");
		builder.setMessage("You are FALSE. True answer is: ");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// hien thi ket qua
				mVfMain.showNext();
				
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	private void showDialogVictory(){
		
		// lay ve ket qua dung

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Victory");
		builder.setMessage(" is TRUE answer. You are victory!");
		builder.setPositiveButton("Show award", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// hien thi ket qua
				mVfMain.showNext();
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
		mBtnHelpChangeQuestion.setEnabled(false);

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
						mVfMain.showNext();		
					}
				});
		dialog = builder.create();
		dialog.show();
	}
	

}
