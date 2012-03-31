/**
 * 
 */
package at.test.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import at.test.R;
import at.test.connect.RequestServer;
import at.test.connect.RequestServer.REQUEST_TYPE;
import at.test.data.DataInfo;
import at.test.delegate.IRequestServer;
import at.test.object.MemberScore;
import at.test.object.Question;

/**
 * @author hoangnh
 * 
 */
public class GamePlayActivity extends Activity implements IRequestServer,
		View.OnClickListener, RadioGroup.OnCheckedChangeListener {

	String mStrRoomId; // room_id
	TextView mTvQuestion, mTvQuestionTimer, mTvAnswerTimer, mTvQuestionTitle,
			mTvAnswerResult, mTvAnswerInfo; // question and timer
	ListView mLvResult; // listview result answer of members in room

	RadioGroup mRgAnswer; // answer group
	RadioButton mRbA, mRbB, mRbC, mRbD; // answer item
	Button mBtnSummit; // summit
	RequestServer mRequestServer = null;
	String mStrQuestionId = null;
	String mAnswer = "0"; // luu cau tra loi cua user
	// layout cho question va answer
	LinearLayout mLlQuestion, mLlAnswer;
	private int mCurQuestion = 1;
	int mTimePerQuestion, mMemberId;

	// adapter cho listview hien thi tra loi cua cac nguoi choi
	AnswerAdapter mAdapterAnswer;
	ArrayList<MemberScore> mAlAnswer = new ArrayList<MemberScore>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_game);
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

		// answer
		mLlAnswer = (LinearLayout) findViewById(R.id.play_game_layout_answer);
		mTvAnswerResult = (TextView) findViewById(R.id.play_game_tv_result);
		mTvAnswerTimer = (TextView) findViewById(R.id.play_game_answer_tv_counter);
		mLvResult = (ListView) findViewById(R.id.play_game_lv_answer);
		mTvAnswerInfo = (TextView) findViewById(R.id.play_game_tv_answer_info);
		// event listener
		mBtnSummit.setOnClickListener(this);
		mRgAnswer.setOnCheckedChangeListener(this);

		// visibility
		mLlQuestion.setVisibility(View.VISIBLE);
		mLlAnswer.setVisibility(View.GONE);
		mTvQuestionTitle.setText("Question "+mCurQuestion);

		// request cau hoi
		mRequestServer = new RequestServer(this);
		mRequestServer.getQuestion(mStrRoomId);

		mAdapterAnswer = new AnswerAdapter(this, mAlAnswer);
		mLvResult.setAdapter(mAdapterAnswer);
	}

	void showLayout() {
		if (mLlQuestion.getVisibility() == View.VISIBLE) {
			// hien thi phan tra loi
			mLlAnswer.setVisibility(View.VISIBLE);
			mLlQuestion.setVisibility(View.GONE);

			// lay ve phan tra loi cua cac nguoi choi khac
			if (mRequestServer != null) {
				if (!mRequestServer.isCancelled()) {
					mRequestServer.cancel(true);
				}
			}
			mRequestServer = new RequestServer(this);
			mRequestServer.getMembersAnswer(mStrRoomId, String.valueOf(mMemberId), mStrQuestionId, mAnswer);
		} else {
			// hien thi phan hoi
			mLlAnswer.setVisibility(View.GONE);
			mLlQuestion.setVisibility(View.VISIBLE);
			// enable view
			mBtnSummit.setEnabled(true);
			int count = mRgAnswer.getChildCount();
			for (int i=0; i<count; i++){
				mRgAnswer.getChildAt(i).setEnabled(true);
			}
			
			new CountDownTimer(mTimePerQuestion*1000, 1000) {

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

				@Override
				public void onFinish() {
					showLayout();
				}
			}.start();
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		if (mRequestServer.getRequestType() == REQUEST_TYPE.REQUEST_GET_QUESTION) {
			if (sResult != null) {
				try {
					DataInfo.setData(sResult);
					if (DataInfo.value) {
						Question question = DataInfo.question;
						mStrQuestionId = question.getmStrId();
						mTvQuestion.setText(question.getmStrContent());
						mRbA.setText(question.getmStrAnswerA());
						mRbB.setText(question.getmStrAnswerB());
						mRbC.setText(question.getmStrAnswerC());
						mRbD.setText(question.getmStrAnswerD());
						new CountDownTimer(mTimePerQuestion*1000, 1000) {

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

							@Override
							public void onFinish() {
								showLayout();
							}
						}.start();

					}
					else{
						Toast.makeText(this, "sorry, do not create question, please try again", 500).show();
					}
				} catch (Exception e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Error");
					builder.setMessage(e.getMessage());
					builder.create().show();
				}
			}
		} else if (mRequestServer.getRequestType() == REQUEST_TYPE.REQUEST_GET_MEMBERS_ANSWER) {
			if (sResult != null) {
				try {
					DataInfo.setData(sResult);
					if (DataInfo.value) {
						// hien thi danh sach nguoi choi trong room va phan tra loi cua moi nguoi
						ArrayList<MemberScore> temp = DataInfo.mListMembersScore;
						if (temp != null){
							mAlAnswer.clear();
							int size = temp.size();
							for (int i=0; i<size; i++){
								mAlAnswer.add(temp.get(i));
							}
							mAdapterAnswer.notifyDataSetChanged();
						}
						
						// hien thi cau tra loi dung		
						// so sanh xem cau tra loi cua nguoi choi dung hay ko, neu dung thi hien thi
						// cau hoi tiep da dc lay ve
						if (mAnswer.equals(DataInfo.mTrueAnswer)){
							String strTrueAnswer = new String(DataInfo.mTrueAnswer);
							if (strTrueAnswer.equals("1")){
								strTrueAnswer = "A";
							}
							else if (strTrueAnswer.equals("2")){
								strTrueAnswer = "B";
							}if (strTrueAnswer.equals("3")){
								strTrueAnswer = "C";
							}if (strTrueAnswer.equals("4")){
								strTrueAnswer = "D";
							}
							mTvAnswerResult.setText("Answer: "+strTrueAnswer+". You are true!");
							// hien thi dong ho dem nguoc de cho cau tiep theo
							new CountDownTimer(10000, 1000) {
								
								@Override
								public void onTick(long millisUntilFinished) {
									mTvAnswerTimer.setText(String.valueOf((int)millisUntilFinished/1000));
								}
								
								@Override
								public void onFinish() {
									showLayout();
									mCurQuestion++;
									mTvQuestionTitle.setText("Question "+mCurQuestion);
									Question question = DataInfo.question;
									mStrQuestionId = question.getmStrId();
									mTvQuestion.setText(question.getmStrContent());
									mRbA.setText(question.getmStrAnswerA());
									mRbB.setText(question.getmStrAnswerB());
									mRbC.setText(question.getmStrAnswerC());
									mRbD.setText(question.getmStrAnswerD());
								}
							}.start();
						}
						// truong hop nguoi choi tra loi sai, quay tro lai roomlist
						else{
							mTvAnswerInfo.setText("Game exit in:");
							String strTrueAnswer = DataInfo.mTrueAnswer;
							if (strTrueAnswer.equals("1")){
								strTrueAnswer = "A";
							}
							else if (strTrueAnswer.equals("2")){
								strTrueAnswer = "B";
							}if (strTrueAnswer.equals("3")){
								strTrueAnswer = "C";
							}if (strTrueAnswer.equals("4")){
								strTrueAnswer = "D";
							}
							mTvAnswerResult.setText("Answer: "+strTrueAnswer+". You are false!");
							// hien thi dong ho dem nguoc roi quay ve list room khi finish
							new CountDownTimer(10000, 1000) {
								
								@Override
								public void onTick(long millisUntilFinished) {
									mTvAnswerTimer.setText(String.valueOf((int)millisUntilFinished/1000));
								}
								
								@Override
								public void onFinish() {
									onBackPressed();
								}
							}.start();
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
			mRequestServer.answerQuestion(mStrRoomId, String.valueOf(mMemberId),
					String.valueOf(DataInfo.userInfo.getUserId()),
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

	class AnswerAdapter extends ArrayAdapter<MemberScore> {

		LayoutInflater inflater = null;
		ArrayList<MemberScore> alMembers;

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
						R.layout.play_game_listview_answer_row, null);

				holder = new MemberScoreHolder();
				holder.tvIndex = (TextView) convertView
						.findViewById(R.id.cl_tv_index);
				holder.tvUserName = (TextView) convertView
						.findViewById(R.id.cl_tv_username);
				holder.tvAnswer = (TextView) convertView
						.findViewById(R.id.cl_tv_answer);
				holder.tvScore = (TextView) convertView
						.findViewById(R.id.cl_tv_score);
				holder.tvAbility = (TextView) convertView
						.findViewById(R.id.cl_tv_ability);
				holder.tvCombo = (TextView) convertView
						.findViewById(R.id.cl_tv_combo);

				convertView.setTag(holder);
			} else {
				holder = (MemberScoreHolder) convertView.getTag();
			}
			MemberScore member = alMembers.get(position);
			holder.tvIndex.setText(String.valueOf(position));
			holder.tvUserName.setText(member.getStrUserName());
			holder.tvAnswer.setText(member.getStrQuestionAnswer());
			holder.tvScore.setText(member.getStrScore());
			holder.tvAbility.setText(member.getStrAbility());
			holder.tvCombo.setText(member.getStrCombo());

			return convertView;
		}
	}

	static class MemberScoreHolder {
		TextView tvIndex, tvUserName, tvAnswer, tvScore, tvAbility, tvCombo;
	}

}
