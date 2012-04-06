/**
 * 
 */
package com.ppclink.iqarena.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.communication.RequestServer;
import com.ppclink.iqarena.communication.RequestServer.REQUEST_TYPE;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.MemberScore;
import com.ppclink.iqarena.object.Question;
import com.ppclink.iqarena.ultil.FilterResponse;

/**
 * @author hoangnh
 * 
 */
public class RoomPlay extends Activity implements IRequestServer,
		View.OnClickListener, RadioGroup.OnCheckedChangeListener {

	// adapter cho listview hien thi tra loi cua cac nguoi choi
	AnswerAdapter mAdapterAnswer;

	ArrayList<MemberScore> mAlAnswer = new ArrayList<MemberScore>();
	String mAnswer = "0"; // luu cau tra loi cua user
	Button mBtnSummit; // summit
	// button help 
	Button mBtnHelpX2, mBtnHelpRelease;
	private int mCurQuestion = 1;
	// layout cho question va answer
	LinearLayout mLlQuestion;
	RelativeLayout mRlAnswer;
	ListView mLvResult; // listview result answer of members in room
	RadioButton mRbA, mRbB, mRbC, mRbD; // answer item
	RequestServer mRequestServer = null;
	RadioGroup mRgAnswer; // answer group

	String mStrQuestionId = null;
	String mStrRoomId; // room_id

	int mTimePerQuestion, mMemberId;

	TextView mTvQuestion, mTvQuestionTimer, mTvAnswerTimer, mTvQuestionTitle,
			mTvAnswerResult, mTvAnswerInfo; // question and timer

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
		mLvResult = (ListView) findViewById(R.id.play_game_lv_answer);
		mTvAnswerInfo = (TextView) findViewById(R.id.play_game_tv_answer_info);
		// event listener
		mBtnSummit.setOnClickListener(this);
		mRgAnswer.setOnCheckedChangeListener(this);
	
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

	void showLayout() {
		if (mLlQuestion.getVisibility() == View.VISIBLE) {
			// hien thi phan tra loi
			mRlAnswer.setVisibility(View.VISIBLE);
			mLlQuestion.setVisibility(View.GONE);
			mLvResult.setVisibility(View.INVISIBLE);
			setProgressBarIndeterminateVisibility(true);
			
			mTvAnswerInfo.setText("");
			mTvAnswerResult.setText("");
			mTvAnswerTimer.setText("");
	
			// lay ve phan tra loi cua cac nguoi choi khac
			if (mRequestServer != null) {
				if (!mRequestServer.isCancelled()) {
					mRequestServer.cancel(true);
				}
			}
			// doi 2s de dong bo cau tra loi
			new CountDownTimer(2000, 1000) {
				
				@Override
				public void onTick(long arg0) {
					
				}
				
				@Override
				public void onFinish() {
					mRequestServer = new RequestServer(RoomPlay.this);
					mRequestServer.getMembersAnswer(mStrRoomId,
							String.valueOf(mMemberId), mStrQuestionId, mAnswer);
				}
			}.start();
		} else {
			// hien thi phan hoi
			mRlAnswer.setVisibility(View.GONE);
			mLlQuestion.setVisibility(View.VISIBLE);
			
			mTvQuestion.setText("");
			mTvQuestionTimer.setText("");
			mRbA.setText("");
			mRbB.setText("");
			mRbC.setText("");
			mRbD.setText("");
			// enable view
			mBtnSummit.setEnabled(true);
			int count = mRgAnswer.getChildCount();
			for (int i = 0; i < count; i++) {
				mRgAnswer.getChildAt(i).setEnabled(true);
			}
	
			new CountDownTimer(mTimePerQuestion * 1000, 1000) {
	
				@Override
				public void onFinish() {
					showLayout();
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
			}.start();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_game_summit:
			// disable selected
			mBtnSummit.setEnabled(false);
			int count = mRgAnswer.getChildCount();
			for (int i = 0; i < count; i++) {
				mRgAnswer.getChildAt(i).setEnabled(false);
			}

			// lay ve index cua user da chon
			mAnswer = String.valueOf(mRgAnswer
					.indexOfChild(findViewById(mRgAnswer
							.getCheckedRadioButtonId())) + 1);
			if (mRequestServer != null) {
				if (!mRequestServer.isCancelled()) {
					mRequestServer.cancel(true);
				}
			}
			mRequestServer = new RequestServer(this);
			mRequestServer.answerQuestion(
					String.valueOf(mMemberId), mStrRoomId,
					mStrQuestionId, mAnswer);
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
						new CountDownTimer(mTimePerQuestion * 1000, 1000) {

							@Override
							public void onFinish() {
								showLayout();
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
						}.start();

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
			setProgressBarIndeterminateVisibility(false);
			if (sResult != null) {
				try {
					FilterResponse.filter(sResult);
					if (FilterResponse.value) {
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
						if (mAnswer.equals(FilterResponse.mTrueAnswer)) {
							// reset lai cau tra loi
							mAnswer = "0";
							String strTrueAnswer = new String(
									FilterResponse.mTrueAnswer);
							if (strTrueAnswer.equals("1")) {
								strTrueAnswer = "A";
							} else if (strTrueAnswer.equals("2")) {
								strTrueAnswer = "B";
							}
							if (strTrueAnswer.equals("3")) {
								strTrueAnswer = "C";
							}
							if (strTrueAnswer.equals("4")) {
								strTrueAnswer = "D";
							}
							mTvAnswerResult.setText("True Answer: " + strTrueAnswer
									+ "\nYou are true!");
							mTvAnswerInfo.setText("Next question in:");
							// hien thi dong ho dem nguoc de cho cau tiep theo
							new CountDownTimer(10000, 1000) {

								@Override
								public void onFinish() {
									showLayout();
									mCurQuestion++;
									mTvQuestionTitle.setText("Question "
											+ mCurQuestion);
									Question question = FilterResponse.question;
									mStrQuestionId = question.getmStrId();
									mTvQuestion.setText(question
											.getmStrContent());
									mRbA.setText(question.getmStrAnswerA());
									mRbB.setText(question.getmStrAnswerB());
									mRbC.setText(question.getmStrAnswerC());
									mRbD.setText(question.getmStrAnswerD());
								}

								@Override
								public void onTick(long millisUntilFinished) {
									mTvAnswerTimer
											.setText(String
													.valueOf((int) millisUntilFinished / 1000));
								}
							}.start();
						}
						// truong hop nguoi choi tra loi sai, quay tro lai
						// roomlist
						else {
							mTvAnswerInfo.setText("Press back button to exit!");
							mTvAnswerTimer.setVisibility(View.GONE);
							String strTrueAnswer = FilterResponse.mTrueAnswer;
							if (strTrueAnswer.equals("1")) {
								strTrueAnswer = "A";
							} else if (strTrueAnswer.equals("2")) {
								strTrueAnswer = "B";
							}
							if (strTrueAnswer.equals("3")) {
								strTrueAnswer = "C";
							}
							if (strTrueAnswer.equals("4")) {
								strTrueAnswer = "D";
							}
							mTvAnswerResult.setText("True Answer: " + strTrueAnswer
									+ ".\nYou are false!");
						}
					}
				} catch (Exception e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Error");
					builder.setMessage(e.getMessage());
					builder.create().show();
				}
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
	
			return convertView;
		}
	}

	static class MemberScoreHolder {
		TextView tvIndex, tvUserName, tvAnswer, tvScore, tvInfo;
	}

}
