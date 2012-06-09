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
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.database.DatabaseHelper;
import com.ppclink.iqarena.object.QuestionLite;
import com.ppclink.iqarena.object.Rank;

public class LocalMode extends Activity implements OnClickListener {

	RadioButton mRbA, mRbB, mRbC, mRbD; // answer item
	RadioGroup mRgAnswer; // answer group
	TextView mTvQuestion, mTvQuestionTimer, mTvQuestionTitle, mTvScore;
	private ViewFlipper mVfMain;
	Button mBtnHelpX2, mBtnHelpRelease, mBtnHelp5050, mBtnSummit, mBtnHelpChangeQuestion;
	AlertDialog mDialog;
	
	ListView mLvRanks;
	
	private int mLastRank;

	private final static int TIME_PER_QUESTION = 30000;

	private int mScore = 0;

	private CountDownTimer mTimer;

	private int mCurrentQues = 0;

	private ArrayList<QuestionLite> mQuestions;

	private String tag = "LocalMode";
	// mang 15 phan tu tuong ung so diem cho moi cau
	private int[] mScoreLevels = { 10, 10, 10, 10, 20, 50, 100, 200, 500, 1000,
			2000, 3000, 5000, 8000, 10000 };

	DatabaseHelper mDataHelper;
	
	MediaPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_mode);
		Bundle extra = getIntent().getExtras();
		boolean isSoundOn = extra.getBoolean("sound");
		

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
		mVfMain.setAnimation(AnimationUtils.loadAnimation(this, R.anim.toast_enter));

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
		mQuestions = mDataHelper.getQuestions();
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
									mTimer.cancel();
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
		
		if (isSoundOn){
			if (player == null){
				player = MediaPlayer.create(this, R.raw.play_theme1);
				player.setLooping(true);
				try {
					player.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.start();
			}
			else{
				player.start();
			}
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mTimer != null){
			mTimer.cancel();
		}
		if (player != null){
			if (player.isPlaying()){
				player.pause();
				player.stop();
			}
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
			mBtnHelpChangeQuestion.setTextSize(20);
			
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
			// lay ve cau hoi
			QuestionLite question = null;
			try {
				mDataHelper.openDataBase();
				question = mDataHelper.getQuestion(mCurrentQues+1);
				mDataHelper.close();
			} catch (SQLException e) {
				Log.e(tag, e.getMessage());
			}
			if (question == null){
				return;
			}
			mTvQuestionTitle.setText("Question "
					+ String.valueOf(mCurrentQues + 1));
			mTvQuestion.setText(question.getQuesName());
			mRbA.setText(question.getAnswerA());
			mRbB.setText(question.getAnswerB());
			mRbC.setText(question.getAnswerC());
			mRbD.setText(question.getAnswerD());
			
			mQuestions.get(mCurrentQues).setAnswer(question.getAnswer());
			
			break;

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
			builder.setMessage(result + " is TRUE answer. \nYour score is: " + mScore
					+ "\nReady for next question?");
		}
		else if (type == 1){
			builder.setMessage("You have choose help release. \n"+ result + " is TRUE answer. \nYour score is: " + mScore
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
						mTimer.cancel();
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
		mBtnHelpChangeQuestion.setEnabled(false);
		
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
				mVfMain.showNext();
				showDialogInsertRank();
				
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
				mVfMain.showNext();
				showDialogInsertRank();
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
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// them rank
				String name = etUsername.getText().toString();
				if (name != null){
					mDataHelper.insertAward(name, mScore);
				}
				
				// do du lieu vao listview
				ArrayList<Rank> ranks = mDataHelper.getAwards();
				if (ranks != null){
					int size = ranks.size();
					if (size != 0){
						mLastRank = ranks.get(0).getId();
						for (int i=0; i<size; i++){
							int id = ranks.get(i).getId();
							if (mLastRank < id){
								mLastRank = id;
							}
						}
						RankAdapter adapter = new RankAdapter(
								LocalMode.this, ranks);
						mLvRanks.setAdapter(adapter);
					}
				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// khong them rank
				// do du lieu vao listview
				ArrayList<Rank> ranks = mDataHelper.getAwards();
				if (ranks != null){
					if (ranks.size() != 0){
						RankAdapter adapter = new RankAdapter(
								LocalMode.this, ranks);
						mLvRanks.setAdapter(adapter);
					}
				}
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
			if (rank.getId() == mLastRank){
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
