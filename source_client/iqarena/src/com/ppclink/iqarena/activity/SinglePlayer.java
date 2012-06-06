package com.ppclink.iqarena.activity;

import java.util.ArrayList;
import java.util.Collections;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.connection.CheckServer;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.connection.ConnectionManager.REQUEST_TYPE;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.Question;
import com.ppclink.iqarena.object.QuestionLite;
import com.ppclink.iqarena.object.Rank;
import com.ppclink.iqarena.ultil.AnalysisData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
	ProgressDialog mProgressDialog;

	ListView mLvRanks;

	private final static int TIME_PER_QUESTION = 30000;

	private int mScore = 0;
	
	// mang 15 phan tu tuong ung so diem cho moi cau
	private int[] mScoreLevels = { 10, 10, 10, 10, 20, 50, 100, 200, 500, 1000,
			2000, 3000, 5000, 8000, 10000 };

	private CountDownTimer mTimer;

	private int mCurrentQues = 0;
	
	private String tag = "SinglePlayer";
	
	ConnectionManager mRequest;
	
	QuestionLite mQuestion;
	
	boolean isGetOtherQuestion = false;

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
		mVfMain.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.dialog_enter));
		mVfMain.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.dialog_exit));
		
		initUISuportMultiScreen();
		
		if (mRequest == null){
			mRequest = new ConnectionManager(this);
			mRequest.getQuestionByType(mCurrentQues+1);
		}

		mProgressDialog = ProgressDialog.show(this, "Connection", "Waiting...");
		mTvQuestionTitle.setText("Question "
				+ String.valueOf(mCurrentQues + 1));
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
			if (mTimer != null) {
				mTimer.cancel();
				// disable answer
				int count = mRgAnswer.getChildCount();
				for (int i = 0; i < count; i++) {
					mRgAnswer.getChildAt(i).setEnabled(false);
				}

				// check answer
				int answer = mRgAnswer.indexOfChild(findViewById(mRgAnswer
						.getCheckedRadioButtonId())) + 1;
				// tra loi dung
				if (answer == mQuestion.getAnswer()) {
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
		case R.id.local_mode_btn_help_50_50:
			mBtnHelp5050.setEnabled(false);
			int trueAnswer = mQuestion.getAnswer() - 1;
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
			mBtnHelpRelease.setEnabled(false);
			if (mTimer != null){
				mTimer.cancel();
			}
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
		case R.id.local_mode_btn_help_change_question:
			mBtnHelpChangeQuestion.setEnabled(false);
			mProgressDialog = ProgressDialog.show(this, "Connection", "Waiting...");
			// lay ve cau hoi
			if (mRequest != null){
				if (!mRequest.isCancelled()){
					mRequest.cancel(true);
				}
			}			
			isGetOtherQuestion = true;
			mRequest = new ConnectionManager(this);
			mRequest.getQuestionByType(mCurrentQues+1);
			break;

		default:
			break;
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		// request lay ve cau hoi hoan tat
		if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_GET_QUESTION_BY_TYPE) {
			if (mProgressDialog != null){
				mProgressDialog.dismiss();
			}
			if (sResult != null) {
				try {
					if (sResult.contains("{")){		
						AnalysisData.analyze(sResult);				
						// co cau hoi tra ve
						if (AnalysisData.value) {
							mQuestion = AnalysisData.questionLite;
							mTvQuestion.setText(mQuestion.getQuesName());
							mRbA.setText(mQuestion.getAnswerA());
							mRbB.setText(mQuestion.getAnswerB());
							mRbC.setText(mQuestion.getAnswerC());
							mRbD.setText(mQuestion.getAnswerD());
							if (!isGetOtherQuestion){	// kiem tra xem co phai 
								// nguoi choi thay cau hoi khac khong
								// neu ko thi khong dem nguoc lai thoi gian
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
							else{
								isGetOtherQuestion = !isGetOtherQuestion;
							}
	
						}
						// truong hop ko lay dc cau hoi, dua ra thong bao
						else {
							Toast.makeText(this,
									"sorry, do not create question, please try again",
									500).show();
						}
					}
					else{
						Toast.makeText(this, sResult, 500).show();
					}
				} catch (Exception e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Error");
					builder.setMessage(sResult);
					builder.create().show();
				}
			}
		}
		else if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_GET_TOP_RECORD){
			if (mProgressDialog != null){
				mProgressDialog.dismiss();
			}
			if (sResult != null){
				if (sResult.contains("{")){
					AnalysisData.analyze(sResult);
					if (AnalysisData.value){
						if (AnalysisData.mRanks != null){
							RankAdapter adapter = new RankAdapter(this, AnalysisData.mRanks);
							mLvRanks.setAdapter(adapter);
						}
					}
				}
				else{
					Toast.makeText(this, sResult, 1000).show();
				}
			}
		}
		else if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_SUBMIT_RECORD){
			if (sResult != null){
				if (sResult.contains("{")){
					AnalysisData.analyze(sResult);
					if (!AnalysisData.value){
						Toast.makeText(this, AnalysisData.message, 1000).show();
					}
					else{
						int rankID = AnalysisData.mRankId;
						Log.e(tag, " "+rankID);
					}
				}
				else
					Toast.makeText(this, sResult, 1000).show();
			}
			if (mRequest != null){
				if (!mRequest.isCancelled()){
					mRequest.cancel(true);
				}
			}
			mRequest = new ConnectionManager(this);
			mRequest.getTopRecord();
		}
	}
	


	private void showDialogTrueAnswer(int type) {
		// type 0 la cho truong hop tra loi dung
		// type 1 cho truong hop help release
		// lay ve ket qua dung
		if (mTimer != null){
			mTimer.cancel();
		}
		
		if (type == 0){
			mScore = mScore + mScoreLevels[mCurrentQues];
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
			builder.setMessage(getAnswer() + " is TRUE answer. \nYour score is: " 
					+ mScore
					+ "\nReady for next question?");
		}
		else if (type == 1){
			builder.setMessage("You have choose help release. \n" + getAnswer() +
					" is TRUE answer. \nYour score is: " + mScore
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
				
				// request lay cau hoi tiep
				if (mRequest != null){
					if (!mRequest.isCancelled()){
						mRequest.cancel(true);
					}
					mRequest = new ConnectionManager(SinglePlayer.this);
					mRequest.getQuestionByType(mCurrentQues+1);
					mProgressDialog = ProgressDialog.show(
							SinglePlayer.this, "Connection", "Waiting...");
					mTvQuestionTitle.setText("Question "
									+ String.valueOf(mCurrentQues + 1));
				}
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	private void showDialogFalseAnswer() {
		if (mTimer != null){
			mTimer.cancel();
		}
		mBtnHelp5050.setEnabled(false);
		mBtnHelpRelease.setEnabled(false);
		mBtnHelpX2.setEnabled(false);
		mBtnSummit.setEnabled(false);
		mBtnHelpChangeQuestion.setEnabled(false);
		

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Result");
		builder.setMessage("You are FALSE. \nTrue answer is: " + getAnswer());
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// hien thi ket qua
				showDialogInsertRank();				
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	private void showDialogVictory(){
		if (mTimer != null){
			mTimer.cancel();
		}
		
		// lay ve ket qua dung

		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Victory");
		builder.setMessage(" is TRUE answer. You are victory!");
		builder.setPositiveButton("Show award", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDialogInsertRank();
			}
		});
		dialog = builder.create();
		dialog.show();
		
	}
	
	
	private void showDialogTimeLimit() {
		if (mTimer != null){
			mTimer.cancel();
		}
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
						showDialogInsertRank();		
					}
				});
		dialog = builder.create();
		dialog.show();
	}
	
	private void showDialogInsertRank(){

		final EditText etUsername = new EditText(this);
		etUsername.setText("user");
		AlertDialog dialog = null;
		Builder builder = new Builder(this);
		builder.setTitle("Confirm");
		builder.setView(etUsername);
		builder.setMessage("Username: ");
		builder.setPositiveButton("Summit", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// them rank
				String name = etUsername.getText().toString();
				if (name != null){
					if (mRequest != null){
						if (!mRequest.isCancelled()){
							mRequest.cancel(true);
						}
					}
					mRequest = new ConnectionManager(SinglePlayer.this);
					mRequest.submitRecord(name, mScore);
					mVfMain.showNext();
				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// khong them rank
				mVfMain.showNext();
				mRequest = new ConnectionManager(SinglePlayer.this);
				mRequest.getTopRecord();
				mProgressDialog = ProgressDialog.show(
						SinglePlayer.this, "Connection", "Waiting...");
			}
		});
		dialog = builder.create();
		dialog.show();
	}
	
	
	private String getAnswer(){
		String result = null;
		int answer = mQuestion.getAnswer();
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
	

	private class RankAdapter extends ArrayAdapter<Rank>{
		Context context;
		ArrayList<Rank> ranks;

		public RankAdapter(Context context, ArrayList<Rank> objects) {
			super(context, 1, objects);
			this.context = context;
			this.ranks = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RankHolder holder;
			if (convertView == null){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_rank_item, null);
				holder = new RankHolder();
				holder.tvRank = (TextView) convertView.findViewById(
						R.id.list_tv_rank);
				holder.tvUsername = (TextView) convertView.findViewById(
						R.id.list_tv_username);
				holder.tvScore = (TextView) convertView.findViewById(
						R.id.list_tv_score);
				convertView .setTag(holder);
			}
			else{
				holder = (RankHolder) convertView.getTag();
			}
			Rank rank = ranks.get(position);
			holder.tvRank.setText(String.valueOf(position+1));
			holder.tvUsername.setText(rank.getName());
			holder.tvScore.setText(String.valueOf(rank.getScore()));		
			
			// highlight current insert
			if (rank.getId() == AnalysisData.mRankId){
				convertView.setBackgroundDrawable(getResources().
						getDrawable(R.drawable.focused_application_background));
			}
			else{
				convertView.setBackgroundColor(android.R.color.transparent);
			}
			return convertView;
		}		
	}
	
	private static class RankHolder{
		TextView tvRank, tvUsername, tvScore;
	}

}
