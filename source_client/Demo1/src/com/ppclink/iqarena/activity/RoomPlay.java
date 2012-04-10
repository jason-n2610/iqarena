/**
 * 
 */
package com.ppclink.iqarena.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.communication.CheckServer;
import com.ppclink.iqarena.communication.CheckServer.REQUEST_CHECK_TYPE;
import com.ppclink.iqarena.communication.RequestServer;
import com.ppclink.iqarena.communication.RequestServer.REQUEST_TYPE;
import com.ppclink.iqarena.delegate.ICheckServer;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.MemberScore;
import com.ppclink.iqarena.object.Question;
import com.ppclink.iqarena.ultil.FilterResponse;

/**
 * @author hoangnh
 * 
 */
public class RoomPlay extends Activity implements IRequestServer, ICheckServer,
		View.OnClickListener, RadioGroup.OnCheckedChangeListener {

	// adapter cho listview hien thi tra loi cua cac nguoi choi
	AnswerAdapter mAdapterAnswer;
	
	ArrayList<MemberScore> mAlAnswer = new ArrayList<MemberScore>();	
	String mStrAnswer = "0"; // luu cau tra loi cua user
	private int mCurQuestion = 1, mTimePerQuestion, mMemberId;
	String mStrQuestionId = null, mStrTrueAnswer = null;
	String mStrRoomId; // room_id
	// bien kiem tra xem nguoi choi co tra loi cau hoi ko
	boolean mIsAnswer = false;
	private CountDownTimer mTimer;
	
	Button mBtnSummit, mBtnHelpX2, mBtnHelpRelease;
	// layout cho question va answer
	LinearLayout mLlQuestion;
	RelativeLayout mRlAnswer;
	ListView mLvResult; // listview result answer of members in room
	RadioButton mRbA, mRbB, mRbC, mRbD; // answer item
	RadioGroup mRgAnswer; // answer group
	TextView mTvQuestion, mTvQuestionTimer, mTvAnswerTimer, mTvQuestionTitle,
			mTvAnswerResult, mTvAnswerInfo; // question and timer
	CheckBox mCkReady;

	RequestServer mRequestServer = null;
	CheckServer mCheckServer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		setContentView(R.layout.room_play);
		Bundle extra = getIntent().getExtras();
		mStrRoomId = extra.getString("room_id");
		mTimePerQuestion = extra.getInt("time_per_question");
		mMemberId = extra.getInt("member_id");
	
		// question
		mLlQuestion = (LinearLayout) findViewById(R.id.play_game_layout_question);
		mTvQuestion = (TextView) findViewById(R.id.play_game_question);
		mTvQuestionTimer = (TextView) findViewById(R.id.play_game_time_counter);
		mTvQuestionTitle = (TextView) findViewById(R.id.play_game_tv_question_title);
		mBtnSummit = (Button) findViewById(R.id.play_game_summit);
		mRgAnswer = (RadioGroup) findViewById(R.id.play_game_rg_answer);
		mRbA = (RadioButton) findViewById(R.id.play_game_answer_a);
		mRbB = (RadioButton) findViewById(R.id.play_game_answer_b);
		mRbC = (RadioButton) findViewById(R.id.play_game_answer_c);
		mRbD = (RadioButton) findViewById(R.id.play_game_answer_d);
		
		// button help
		mBtnHelpX2 = (Button) findViewById(R.id.room_play_btn_help_x2);
		mBtnHelpRelease = (Button) findViewById(R.id.room_play_btn_help_release);
		
		// answer
		mRlAnswer = (RelativeLayout) findViewById(R.id.play_game_layout_answer);
		mTvAnswerResult = (TextView) findViewById(R.id.play_game_tv_result);
		mTvAnswerTimer = (TextView) findViewById(R.id.play_game_answer_tv_counter);
		mTvAnswerInfo = (TextView) findViewById(R.id.play_game_tv_answer_info);
		mLvResult = (ListView) findViewById(R.id.play_game_lv_answer);
		mCkReady = (CheckBox) findViewById(R.id.play_game_ck_ready);
		// event listener
		mBtnSummit.setOnClickListener(this);
		mRgAnswer.setOnCheckedChangeListener(this);
		mCkReady.setOnClickListener(this);
	
		// visibility
		mLlQuestion.setVisibility(View.VISIBLE);
		mRlAnswer.setVisibility(View.GONE);
		mTvQuestionTitle.setText("Question " + mCurQuestion);
		
	
		setProgressBarIndeterminateVisibility(true);
		
		// request cau hoi
		mRequestServer = new RequestServer(this);
		mRequestServer.getQuestion(mStrRoomId);
	
		mAdapterAnswer = new AnswerAdapter(this, mAlAnswer);
		mLvResult.setAdapter(mAdapterAnswer);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mRequestServer != null){
			if (!mRequestServer.isCancelled()){
				mRequestServer.cancel(true);
			}
		}
		if (mCheckServer != null){
			if (!mCheckServer.isCancelled()){
				mCheckServer.cancel(true);
			}
		}
	}

	void showLayout() {
		if (mLlQuestion.getVisibility() == View.VISIBLE) {
			// hien thi phan tra loi
			mCkReady.setChecked(false);
			mRlAnswer.setVisibility(View.VISIBLE);
			mLlQuestion.setVisibility(View.GONE);
			mLvResult.setVisibility(View.INVISIBLE);
			setProgressBarIndeterminateVisibility(true);
			
			mTvAnswerInfo.setText("");
			mTvAnswerResult.setText("");
			mTvAnswerTimer.setText("");
	
		} else {
			// hien thi phan hoi
			mTimer.cancel();
			mRlAnswer.setVisibility(View.GONE);
			mLlQuestion.setVisibility(View.VISIBLE);
			
			mCurQuestion++;
			mTvQuestionTitle.setText("Question "
					+ mCurQuestion);
			Question question = FilterResponse.question;
			if (question == null){
				return;
			}
			mStrQuestionId = question.getmStrId();
			mTvQuestion.setText(question
					.getmStrContent());
			mRbA.setText(question.getmStrAnswerA());
			mRbB.setText(question.getmStrAnswerB());
			mRbC.setText(question.getmStrAnswerC());
			mRbD.setText(question.getmStrAnswerD());
			// enable view
			mBtnSummit.setEnabled(true);
			int count = mRgAnswer.getChildCount();
			for (int i = 0; i < count; i++) {
				mRgAnswer.getChildAt(i).setEnabled(true);
			}
			
			// gan answer is false
			mIsAnswer = false;
	
			// dem nguoc thoi gian
			mTimer = new CountDownTimer(mTimePerQuestion * 1000, 1000) {
	
				@Override
				public void onFinish() {
					if (!mIsAnswer){
						// nguoi choi chua tra loi
						// gui request cho server
						if (mRequestServer != null) {
							if (!mRequestServer.isCancelled()) {
								mRequestServer.cancel(true);
							}
						}
						if (mCheckServer != null){
							if (!mCheckServer.isCancelled()){
								mCheckServer.cancel(true);
							}
						}
						mRequestServer = new RequestServer(RoomPlay.this);
						mRequestServer.answerQuestion(
								String.valueOf(mMemberId), mStrRoomId,
								mStrQuestionId, "0");
						
						// check others answer
						mCheckServer = new CheckServer(RoomPlay.this);
						mCheckServer.checkOthersAnswer(mStrRoomId);
						showLayout();
					}
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_game_summit:			
			// set answer is true
			mIsAnswer = true;
			// disable selected
			mBtnSummit.setEnabled(false);
			int count = mRgAnswer.getChildCount();
			for (int i = 0; i < count; i++) {
				mRgAnswer.getChildAt(i).setEnabled(false);
			}

			// lay ve index cua user da chon
			mStrAnswer = String.valueOf(mRgAnswer
					.indexOfChild(findViewById(mRgAnswer
							.getCheckedRadioButtonId())) + 1);
			if (mRequestServer != null) {
				if (!mRequestServer.isCancelled()) {
					mRequestServer.cancel(true);
				}
			}
			// check others answer
			if (mCheckServer != null){
				if(!mCheckServer.isCancelled()){
					mCheckServer.cancel(true);
				}
			}
			mCheckServer = new CheckServer(this);
			mCheckServer.checkOthersAnswer(mStrRoomId);
			
			mRequestServer = new RequestServer(this);
			mRequestServer.answerQuestion(
					String.valueOf(mMemberId), mStrRoomId,
					mStrQuestionId, mStrAnswer);			
			
			mTimer.cancel();
			showLayout();
			break;
			
		case R.id.play_game_ck_ready:
			if (mCkReady.isChecked()){
				mCkReady.setEnabled(false);
				// request server member is ready
				if (!mRequestServer.isCancelled()){
					mRequestServer.cancel(true);
				}
				mRequestServer = new RequestServer(this);
				mRequestServer.readyForGame(String.valueOf(mMemberId), mStrRoomId);
				// check others is ready
				if (!mCheckServer.isCancelled()){
					mCheckServer.cancel(true);
				}
				mCheckServer = new CheckServer(this);
				mCheckServer.checkRoomReady(mStrRoomId);
			}
			else{
				
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.play_game_answer_a:
	
			break;
		case R.id.play_game_answer_b:
	
			break;
		case R.id.play_game_answer_c:
	
			break;
		case R.id.play_game_answer_d:
	
			break;
	
		default:
			break;
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		// request lay ve cau hoi hoan tat
		if (mRequestServer.getRequestType() == REQUEST_TYPE.REQUEST_GET_QUESTION) {
			setProgressBarIndeterminateVisibility(false);
			if (sResult != null) {
				try {
					FilterResponse.filter(sResult);
					// co cau hoi tra ve
					if (FilterResponse.value) {
						Question question = FilterResponse.question;
						mStrQuestionId = question.getmStrId();
						mTvQuestion.setText(question.getmStrContent());
						mRbA.setText(question.getmStrAnswerA());
						mRbB.setText(question.getmStrAnswerB());
						mRbC.setText(question.getmStrAnswerC());
						mRbD.setText(question.getmStrAnswerD());
						mTimer = new CountDownTimer(mTimePerQuestion * 1000, 1000) {

							@Override
							public void onFinish() {
								if (!mIsAnswer){
									// nguoi choi chua tra loi
									// gui request cho server
									if (mRequestServer != null) {
										if (!mRequestServer.isCancelled()) {
											mRequestServer.cancel(true);
										}
									}
									mRequestServer = new RequestServer(RoomPlay.this);
									mRequestServer.answerQuestion(
											String.valueOf(mMemberId), mStrRoomId,
											mStrQuestionId, "0");
									
									// check others answer
									mCheckServer = new CheckServer(RoomPlay.this);
									mCheckServer.checkOthersAnswer(mStrRoomId);
								}
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
						Toast.makeText(
								this,
								"sorry, do not create question, please try again",
								500).show();
					}
				} catch (Exception e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Error");
					builder.setMessage(e.getMessage());
					builder.create().show();
				}
			}
		} 
		
		// neu la request lay ve list members trong room tra loi ntn
		else if (mRequestServer.getRequestType() == REQUEST_TYPE.REQUEST_GET_MEMBERS_ANSWER) {
			if (!mCheckServer.isCancelled()){
				mCheckServer.cancel(true);
			}
			setProgressBarIndeterminateVisibility(false);
			if (sResult != null || sResult != "null") {
				try {
					FilterResponse.filter(sResult);
					if (FilterResponse.value) {
						mCkReady.setEnabled(false);
						// lay ve cau tra loi dung
						mStrTrueAnswer = new String(
								FilterResponse.mTrueAnswer);
						if (mStrTrueAnswer.equals("1")) {
							mStrTrueAnswer = "A";
						} else if (mStrTrueAnswer.equals("2")) {
							mStrTrueAnswer = "B";
						}
						else if (mStrTrueAnswer.equals("3")) {
							mStrTrueAnswer = "C";
						}
						else if (mStrTrueAnswer.equals("4")) {
							mStrTrueAnswer = "D";
						}
						
						// hien thi danh sach nguoi choi trong room va phan tra
						// loi cua moi nguoi
						ArrayList<MemberScore> temp = FilterResponse.mListMembersScore;
						if (temp != null) {
							mAlAnswer.clear();
							int size = temp.size();
							for (int i = 0; i < size; i++) {
								mAlAnswer.add(temp.get(i));
							}
							mAdapterAnswer.notifyDataSetChanged();
							mLvResult.setVisibility(View.VISIBLE);
						}

						// hien thi cau tra loi dung
						// so sanh xem cau tra loi cua nguoi choi dung hay ko,
						// neu dung thi hien thi
						// cau hoi tiep da dc lay ve
						if (mStrAnswer.equals(FilterResponse.mTrueAnswer)) {
							// reset lai cau tra loi
							mStrAnswer = "0";
							mTvAnswerResult.setText("True Answer: " + mStrTrueAnswer
									+ "\nYou are true!");
							mCkReady.setEnabled(true);							
							// truong hop cuoc choi con tiep tuc
							if (FilterResponse.question != null){
								mTvAnswerInfo.setText("Next question in:");
								// hien thi dong ho dem nguoc de cho cau tiep theo
								mTimer = new CountDownTimer(20000, 1000) {

									@Override
									public void onFinish() {
										if (!mCkReady.isChecked()){
											if (!mCheckServer.isCancelled()){
												mCheckServer.cancel(true);
											}
											mCheckServer = new CheckServer(RoomPlay.this);
											mCheckServer.checkRoomReady(mStrRoomId);
											
											if (!mRequestServer.isCancelled()){
												mRequestServer.cancel(true);
											}						
											mRequestServer = new RequestServer(RoomPlay.this);
											mRequestServer.readyForGame(String.valueOf(mMemberId), mStrRoomId);
										}
									}

									@Override
									public void onTick(long millisUntilFinished) {
										mTvAnswerTimer
												.setText(String
														.valueOf((int) millisUntilFinished / 1000));
									}
								};
								mTimer.start();
							}
							else{
								mTvAnswerInfo.setText("You are winner!!! \nPress back button to exit!");
								mTvAnswerInfo.setTextColor(Color.YELLOW);
								mTvAnswerTimer.setVisibility(View.GONE);
								mCkReady.setEnabled(false);
								// thong bao diem moi cua user
								Toast.makeText(this, "New score: "+FilterResponse.updateScore, 400).show();
						
							}
						}
						// truong hop nguoi choi tra loi sai, quay tro lai
						// roomlist
						else {
							mTvAnswerInfo.setText("Press back button to exit!");
							mTvAnswerTimer.setVisibility(View.GONE);
							mTvAnswerResult.setText("True Answer: " + mStrTrueAnswer
									+ ".\nYou are false!");
							// thong bao diem moi cua user
							Toast.makeText(this, "New score: "+FilterResponse.userInfo.getScoreLevel(), 400).show();
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void onCheckServerComplete(String result) {
		if (result.contains("get")){
			if (mCheckServer != null) {
				if (!mCheckServer.isCancelled()) {
					mCheckServer.cancel(true);
				}
			}
			if (!mRequestServer.isCancelled()){
				mRequestServer.cancel(true);
			}			
			mRequestServer = new RequestServer(this);
			mRequestServer.getMembersAnswer(mStrRoomId, 
					String.valueOf(mMemberId), 
					mStrQuestionId, mStrAnswer);
		}
		else if (mCheckServer.getRequestType() == REQUEST_CHECK_TYPE.REQUEST_CHECK_ROOM_READY){
			if (result.contains("ready")){
				if (!mCheckServer.isCancelled()){
					mCheckServer.cancel(true);
				}
				showLayout();
			}
		}
	}

	class AnswerAdapter extends ArrayAdapter<MemberScore> {
	
		ArrayList<MemberScore> alMembers;
		LayoutInflater inflater = null;
	
		public AnswerAdapter(Context context, ArrayList<MemberScore> objects) {
			super(context, 1, objects);
			this.inflater = LayoutInflater.from(context);
			this.alMembers = objects;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MemberScoreHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.room_play_answer_row, null);
	
				holder = new MemberScoreHolder();
				holder.tvIndex = (TextView) convertView
						.findViewById(R.id.cl_tv_index);
				holder.tvUserName = (TextView) convertView
						.findViewById(R.id.cl_tv_username);
				holder.tvAnswer = (TextView) convertView
						.findViewById(R.id.cl_tv_answer);
				holder.tvScore = (TextView) convertView
						.findViewById(R.id.cl_tv_score);
				holder.tvInfo = (TextView) convertView
						.findViewById(R.id.cl_tv_info);
	
				convertView.setTag(holder);
			} else {
				holder = (MemberScoreHolder) convertView.getTag();
			}
			
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					AbsListView.LayoutParams.FILL_PARENT, 40);
			convertView.setLayoutParams(lp);
			
			MemberScore member = alMembers.get(position);
			holder.tvIndex.setText((position+1)+"");
			holder.tvUserName.setText(member.getStrUserName());
			holder.tvAnswer.setText(member.getStrQuestionAnswer());
			holder.tvScore.setText(member.getStrScore());
			holder.tvInfo.setText(member.getStrAbility() + ", " + 
								member.getStrCombo());
			holder.tvIndex.setTextColor(Color.BLACK);
			if (member.getStrQuestionAnswer().equals(mStrTrueAnswer)){
				holder.tvIndex.setBackgroundColor(Color.YELLOW);
			}
			else{
				holder.tvIndex.setBackgroundColor(Color.RED);
			}
			
			if (member.getStrMemberId().equals(String.valueOf(mMemberId))){
				convertView.setBackgroundDrawable(getResources().
						getDrawable(R.drawable.focused_application_background));
			}
			else{
				convertView.setBackgroundColor(android.R.color.transparent);
			}
			
			return convertView;
		}
	}

	static class MemberScoreHolder {
		TextView tvIndex, tvUserName, tvAnswer, tvScore, tvInfo;
	}

}
