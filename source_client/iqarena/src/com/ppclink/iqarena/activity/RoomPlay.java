/**
 * 
 */
package com.ppclink.iqarena.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
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
import com.ppclink.iqarena.connection.CheckServer;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.connection.CheckServer.REQUEST_CHECK_TYPE;
import com.ppclink.iqarena.connection.ConnectionManager.REQUEST_TYPE;
import com.ppclink.iqarena.delegate.ICheckServer;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.MemberScore;
import com.ppclink.iqarena.object.Question;
import com.ppclink.iqarena.object.QuestionLite;
import com.ppclink.iqarena.ultil.AnalysisData;
import com.ppclink.iqarena.ultil.Rotate3dAnimation;

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

	// bien kiem soat viec trong mot luot tra loi cau hoi,
	// member chi co quyen su dung duy nhat 1 quyen giai thoat
	boolean isHelp = false;

	private CountDownTimer mTimer;

	Button mBtnSummit, mBtnHelpX2, mBtnHelpRelease, mBtnHelp5050, mBtnHelpChangeQuestion;
	// layout cho question va answer
	LinearLayout mLlQuestion;
	RelativeLayout mRlAnswer;
	ListView mLvResult; // listview result answer of members in room
	RadioButton mRbA, mRbB, mRbC, mRbD; // answer item
	RadioGroup mRgAnswer; // answer group
	TextView mTvQuestion, mTvQuestionTimer, mTvAnswerTimer, mTvQuestionTitle,
			mTvAnswerResult, mTvAnswerInfo, mTvAnswerTitle; // question and
															// timer
	CheckBox mCkReady;
	private ViewGroup mContainer;

	ConnectionManager mRequestServer = null;
	CheckServer mCheckServer = null;
	boolean isChangeQuestion = false, isHelpX2 = false, isHelpRelease = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.room_play);
		Bundle extra = getIntent().getExtras();
		mStrRoomId = extra.getString("room_id");
		mTimePerQuestion = extra.getInt("time_per_question");
		mMemberId = extra.getInt("member_id");

		mContainer = (ViewGroup) findViewById(R.id.container);
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
		mBtnHelp5050 = (Button) findViewById(R.id.room_play_btn_help_50_50);
		mBtnHelpChangeQuestion = (Button) findViewById(R.id.room_play_btn_help_change_question);
		mBtnHelpChangeQuestion.setText("???");

		// answer
		mRlAnswer = (RelativeLayout) findViewById(R.id.play_game_layout_answer);
		mTvAnswerResult = (TextView) findViewById(R.id.play_game_tv_result);
		mTvAnswerTimer = (TextView) findViewById(R.id.play_game_answer_tv_counter);
		mTvAnswerInfo = (TextView) findViewById(R.id.play_game_tv_answer_info);
		mLvResult = (ListView) findViewById(R.id.play_game_lv_answer);
		mCkReady = (CheckBox) findViewById(R.id.play_game_ck_ready);
		mTvAnswerTitle = (TextView) findViewById(R.id.play_game_answer_title);

		mContainer
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

		// event listener
		mBtnSummit.setOnClickListener(this);
		mRgAnswer.setOnCheckedChangeListener(this);
		mCkReady.setOnClickListener(this);
		mBtnHelpX2.setOnClickListener(this);
		mBtnHelpRelease.setOnClickListener(this);
		mBtnHelp5050.setOnClickListener(this);
		mBtnHelpChangeQuestion.setOnClickListener(this);

		// visibility
		mLlQuestion.setVisibility(View.VISIBLE);
		mRlAnswer.setVisibility(View.GONE);
		mTvQuestionTitle.setText("Question " + mCurQuestion);

		setProgressBarIndeterminateVisibility(true);

		// request cau hoi
		mRequestServer = new ConnectionManager(this);
		mRequestServer.getQuestion(mStrRoomId);

		mAdapterAnswer = new AnswerAdapter(this, mAlAnswer);
		mLvResult.setAdapter(mAdapterAnswer);

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
	public void onBackPressed() {
		super.onBackPressed();
		if (mRequestServer != null) {
			if (!mRequestServer.isCancelled()) {
				mRequestServer.cancel(true);
			}
		}
		if (mCheckServer != null) {
			if (!mCheckServer.isCancelled()) {
				mCheckServer.cancel(true);
			}
		}
		Intent t = new Intent(this, TabHostMain.class);
		startActivity(t);
	}

	void showLayout() {
		if (mLlQuestion.getVisibility() == View.VISIBLE) {
			// hien thi phan tra loi
			mCkReady.setChecked(false);
			// hieu ung hien thi phan tra loi
			applyRotation(1, 0, 90);

			mLvResult.setVisibility(View.INVISIBLE);
			setProgressBarIndeterminateVisibility(true);

			mTvAnswerInfo.setText("");
			mTvAnswerResult.setText("");
			mTvAnswerTimer.setText("");

			mTvAnswerTitle.setText("Waiting for others answer...");

		} else {
			// hien thi phan hoi
			// tra lai quyen tro giup cho member
			isHelp = false;
			mTimer.cancel();
			// hieu ung hien thi phan cau hoi
			applyRotation(0, 180, 90);

			mCurQuestion++;
			mTvQuestionTitle.setText("Question " + mCurQuestion);
			Question question = AnalysisData.question;
			if (question == null) {
				return;
			}
			mStrQuestionId = question.getmStrId();
			mTvQuestion.setText(question.getmStrContent());
			mRbA.setText(question.getmStrAnswerA());
			mRbB.setText(question.getmStrAnswerB());
			mRbC.setText(question.getmStrAnswerC());
			mRbD.setText(question.getmStrAnswerD());
			// enable view
			mBtnSummit.setEnabled(true);
			int count = mRgAnswer.getChildCount();
			for (int i = 0; i < count; i++) {
				mRgAnswer.getChildAt(i).setEnabled(true);
				mRgAnswer.getChildAt(i).setSelected(false);
			}
			mRgAnswer.clearCheck();

			// gan answer is false
			mIsAnswer = false;

			// dem nguoc thoi gian
			mTimer = new CountDownTimer(mTimePerQuestion * 1000, 1000) {

				@Override
				public void onFinish() {
					if (!mIsAnswer) {
						// nguoi choi chua tra loi
						// gui request cho server
						if (mRequestServer != null) {
							if (!mRequestServer.isCancelled()) {
								mRequestServer.cancel(true);
							}
						}
						if (mCheckServer != null) {
							if (!mCheckServer.isCancelled()) {
								mCheckServer.cancel(true);
							}
						}

						// check others answer
						mCheckServer = new CheckServer(RoomPlay.this);
						mCheckServer.checkOthersAnswer(mStrRoomId);

						mRequestServer = new ConnectionManager(RoomPlay.this);
						mRequestServer.answerQuestion(
								String.valueOf(mMemberId), mStrRoomId,
								mStrQuestionId, "0");
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
			if (mCheckServer != null) {
				if (!mCheckServer.isCancelled()) {
					mCheckServer.cancel(true);
				}
			}
			mCheckServer = new CheckServer(this);
			mCheckServer.checkOthersAnswer(mStrRoomId);

			if (isHelpX2){	// su dung quyen tro giup x2 so diem
				mRequestServer = new ConnectionManager(this);
				mRequestServer.answerQuestion(String.valueOf(mMemberId),
						mStrRoomId, mStrQuestionId, mStrAnswer, "helpx2");
				isHelpX2 = !isHelpX2;
			}
			else{
				mRequestServer = new ConnectionManager(this);
				mRequestServer.answerQuestion(String.valueOf(mMemberId),
						mStrRoomId, mStrQuestionId, mStrAnswer);
			}

			mTimer.cancel();
			showLayout();
			break;

		case R.id.play_game_ck_ready:
			if (mCkReady.isChecked()) {
				mCkReady.setEnabled(false);
				// request server member is ready
				if (!mRequestServer.isCancelled()) {
					mRequestServer.cancel(true);
				}
				mRequestServer = new ConnectionManager(this);
				mRequestServer.readyForGame(String.valueOf(mMemberId),
						mStrRoomId);
				// check others is ready
				if (!mCheckServer.isCancelled()) {
					mCheckServer.cancel(true);
				}
				mCheckServer = new CheckServer(this);
				mCheckServer.checkRoomReady(mStrRoomId);
			}
			break;

		// help x2 score
		case R.id.room_play_btn_help_x2:
			isHelpX2 = true;
			mBtnHelpX2.setEnabled(false);
			if (!isHelp) {
				mBtnHelpX2.setEnabled(false);
				isHelp = true;
			} else {
				Toast.makeText(
						this,
						"Báº¡n khÃ´ng Ä‘Æ°á»£c sá»­ dÃ¹ng 2 quyá»�n trá»£ giÃºp cho 1 cÃ¢u há»�i",
						500).show();
			}
			break;

		// help answer question subtract %score
		case R.id.room_play_btn_help_release:
			mBtnHelpRelease.setEnabled(false);
			
			// set answer is true
			mIsAnswer = true;
			// disable selected
			mBtnSummit.setEnabled(false);

			// lay ve index cua user da chon
			if (mRequestServer != null) {
				if (!mRequestServer.isCancelled()) {
					mRequestServer.cancel(true);
				}
			}
			// check others answer
			if (mCheckServer != null) {
				if (!mCheckServer.isCancelled()) {
					mCheckServer.cancel(true);
				}
			}
			mCheckServer = new CheckServer(this);
			mCheckServer.checkOthersAnswer(mStrRoomId);

			mRequestServer = new ConnectionManager(this);
			mRequestServer.answerQuestion(String.valueOf(mMemberId),
					mStrRoomId, mStrQuestionId, mStrAnswer, "release");

			mTimer.cancel();
			showLayout();
			if (!isHelp) {
				mBtnHelpRelease.setEnabled(false);
				isHelp = true;
			} else {
				Toast.makeText(
						this,
						"Báº¡n khÃ´ng Ä‘Æ°á»£c sá»­ dÃ¹ng 2 quyá»�n trá»£ giÃºp cho 1 cÃ¢u há»�i",
						500).show();
			}
			break;
			
		case R.id.room_play_btn_help_change_question:
			mBtnHelpChangeQuestion.setEnabled(false);
			isChangeQuestion = true;
			if (mRequestServer != null){
				if (!mRequestServer.isCancelled()){
					mRequestServer.cancel(true);
				}
			}
			mRequestServer = new ConnectionManager(this);
			mRequestServer.getQuestionByType(mCurQuestion+1);
			break;

		// help 50:50
		case R.id.room_play_btn_help_50_50:
			if (!isHelp) {			
				setProgressBarIndeterminateVisibility(false);

				mBtnHelp5050.setEnabled(false);
				if (!mRequestServer.isCancelled()) {
					mRequestServer.cancel(true);
				}
				mRequestServer = new ConnectionManager(this);
				mRequestServer.help5050(mStrQuestionId);
				isHelp = true;
			} else {
				Toast.makeText(
						this,
						"Báº¡n khÃ´ng Ä‘Æ°á»£c sá»­ dÃ¹ng 2 quyá»�n trá»£ giÃºp cho 1 cÃ¢u há»�i",
						500).show();
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
		setProgressBarIndeterminateVisibility(false);
		
		// request help change question
		if (mRequestServer.getRequestType() == 
				REQUEST_TYPE.REQUEST_GET_QUESTION_BY_TYPE){
			if (sResult != null) {
				try {
					AnalysisData.analyze(sResult);
					// co cau hoi tra ve
					if (AnalysisData.value) {
						QuestionLite question = AnalysisData.questionLite;
						mTvQuestion.setText(question.getQuesName());
						mRbA.setText(question.getAnswerA());
						mRbB.setText(question.getAnswerB());
						mRbC.setText(question.getAnswerC());
						mRbD.setText(question.getAnswerD());
					}
				} catch (Exception e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Error");
					builder.setMessage(sResult);
					builder.create().show();
				}
			}
		}

		// request lay ve cau hoi hoan tat
		else if (mRequestServer.getRequestType() == REQUEST_TYPE.REQUEST_GET_QUESTION) {
			if (sResult != null) {
				try {
					AnalysisData.analyze(sResult);
					// co cau hoi tra ve
					if (AnalysisData.value) {
						Question question = AnalysisData.question;
						mStrQuestionId = question.getmStrId();
						mTvQuestion.setText(question.getmStrContent());
						mRbA.setText(question.getmStrAnswerA());
						mRbB.setText(question.getmStrAnswerB());
						mRbC.setText(question.getmStrAnswerC());
						mRbD.setText(question.getmStrAnswerD());
						mTimer = new CountDownTimer(mTimePerQuestion * 1000,
								1000) {

							@Override
							public void onFinish() {
								if (!mIsAnswer) {
									// nguoi choi chua tra loi
									// gui request cho server
									if (mRequestServer != null) {
										if (!mRequestServer.isCancelled()) {
											mRequestServer.cancel(true);
										}
									}
									if (mCheckServer != null) {
										if (!mCheckServer.isCancelled()) {
											mCheckServer.cancel(true);
										}
									}

									// check others answer
									mCheckServer = new CheckServer(
											RoomPlay.this);
									mCheckServer.checkOthersAnswer(mStrRoomId);

									mRequestServer = new ConnectionManager(
											RoomPlay.this);
									mRequestServer.answerQuestion(
											String.valueOf(mMemberId),
											mStrRoomId, mStrQuestionId, "0");

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
					builder.setMessage(sResult);
					builder.create().show();
				}
			}
		}

		// neu la request lay ve list members trong room tra loi ntn
		else if (mRequestServer.getRequestType() == 
				REQUEST_TYPE.REQUEST_GET_MEMBERS_ANSWER) {
			if (!mCheckServer.isCancelled()) {
				mCheckServer.cancel(true);
			}
			mTvAnswerTitle.setText("Members answer");
			if (sResult != null || sResult != "null") {
				try {
					AnalysisData.analyze(sResult);
					if (AnalysisData.value) {
						mCkReady.setEnabled(false);
						// lay ve cau tra loi dung
						if (isChangeQuestion){
							mStrTrueAnswer = String.valueOf(
									AnalysisData.questionLite.getAnswer());
							isChangeQuestion = !isChangeQuestion;
							AnalysisData.mTrueAnswer = new String(mStrTrueAnswer);
						}
						else{
							mStrTrueAnswer = new String(AnalysisData.mTrueAnswer);
						}
						if (mStrTrueAnswer.equals("1")) {
							mStrTrueAnswer = "A";
						} else if (mStrTrueAnswer.equals("2")) {
							mStrTrueAnswer = "B";
						} else if (mStrTrueAnswer.equals("3")) {
							mStrTrueAnswer = "C";
						} else if (mStrTrueAnswer.equals("4")) {
							mStrTrueAnswer = "D";
						}

						// hien thi danh sach nguoi choi trong room va phan tra
						// loi cua moi nguoi
						ArrayList<MemberScore> temp = AnalysisData.
								mListMembersScore;
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
						if (mStrAnswer.equals(AnalysisData.mTrueAnswer)) {
							// reset lai cau tra loi
							mStrAnswer = "0";
							mTvAnswerResult.setText("True Answer: "
									+ mStrTrueAnswer + "\nYou are true!");
							mCkReady.setEnabled(true);
							// truong hop cuoc choi con tiep tuc
							if (AnalysisData.question != null) {
								mTvAnswerInfo.setText("Next question in:");
								// hien thi dong ho dem nguoc de cho cau tiep
								// theo
								mTimer = new CountDownTimer(20000, 1000) {

									@Override
									public void onFinish() {
										if (!mCkReady.isChecked()) {
											if (!mCheckServer.isCancelled()) {
												mCheckServer.cancel(true);
											}

											if (!mRequestServer.isCancelled()) {
												mRequestServer.cancel(true);
											}
											mRequestServer = new ConnectionManager(
													RoomPlay.this);
											mRequestServer.readyForGame(
													String.valueOf(mMemberId),
													mStrRoomId);
											mCheckServer = new CheckServer(
													RoomPlay.this);
											mCheckServer
													.checkRoomReady(mStrRoomId);
										}
									}

									@Override
									public void onTick(long millisUntilFinished) {
										mTvAnswerTimer
											.setText(String.valueOf((int) 
													millisUntilFinished / 1000));
									}
								};
								mTimer.start();
							} else {
								mTvAnswerInfo
										.setText("You are winner!!! " +
												"\nPress back button to exit!");
								mTvAnswerInfo.setTextColor(Color.YELLOW);
								mTvAnswerTimer.setVisibility(View.GONE);
								mCkReady.setEnabled(false);
								// thong bao diem moi cua user
								Toast.makeText(
										this,
										"New score: "
												+ AnalysisData.updateScore,
										400).show();

							}
						}
						// truong hop nguoi choi tra loi sai, kiem tra xem
						// tro choi co tiep tuc ko
						// neu tro choi tiep tuc dua ra thong bao hoi nguoi choi
						// co muon xem cac nguoi choi khac thi dau ko
						// neu ko thi ket thuc tro choi
						else {
							// kiem tra xem tro choi con tiep tuc ko
							int count = 0; // dem so nguoi choi tra loi dung
							int size = mAlAnswer.size();
							for (int i=0; i<size; i++){
								MemberScore member = mAlAnswer.get(i);
								if (member.getStrQuestionAnswer().equals(AnalysisData.mTrueAnswer)){
									count++;
								}
							}
							if (count > 1){
								// hien thi dialog hoi nguoi choi
								AlertDialog.Builder builder = new Builder(this);
								builder.setTitle("Info");
								builder.setMessage("Bạn có muốn xem tiếp trận đấu không?");
								builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
									
								});
								builder.setNegativeButton("No", new OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
							}
							else{
								mTvAnswerInfo.setText("Press back button to exit!");
								mTvAnswerTimer.setVisibility(View.GONE);
								mTvAnswerResult.setText("True Answer: "
										+ mStrTrueAnswer + ".\nYou are false!");
								// thong bao diem moi cua user
								Toast.makeText(
										this,
										"New score: "
												+ AnalysisData.userInfo
														.getScoreLevel(), 400)
										.show();
							}
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
		else if (mRequestServer.getRequestType() == 
				REQUEST_TYPE.REQUEST_HELP_5050) {
			// truong hop request help (support only 5050)
			if (sResult != null || sResult != "null") {
				try {
					AnalysisData.analyze(sResult);
					if (AnalysisData.value) {
						int itemDisable1, itemDisable2;
						itemDisable1 = AnalysisData.help_5050_remove1;
						itemDisable2 = AnalysisData.help_5050_remove2;
						if (itemDisable1 != 0 && itemDisable2 != 0){
							Log.i("2", "item: "+itemDisable1+ " "+itemDisable2);
							mRgAnswer.getChildAt(itemDisable1-1).setEnabled(false);
							mRgAnswer.getChildAt(itemDisable2-1).setEnabled(false);
						}
					}
				}
				catch (Exception e){
					
				}
			}
			
		}
	}

	@Override
	public void onCheckServerComplete(String result) {
		if (result.contains("time")) {
			Toast.makeText(this, result, 500).show();
			return;
		}
		// check lay ve members trong room
		if (result.contains("get")) {
			if (mCheckServer != null) {
				if (!mCheckServer.isCancelled()) {
					mCheckServer.cancel(true);
				}
			}
			if (!mRequestServer.isCancelled()) {
				mRequestServer.cancel(true);
			}
			mRequestServer = new ConnectionManager(this);
			mRequestServer.getMembersAnswer(mStrRoomId,
					String.valueOf(mMemberId), mStrQuestionId, mStrAnswer);
		} else if (mCheckServer.getRequestType() == 
				REQUEST_CHECK_TYPE.REQUEST_CHECK_ROOM_READY) {
			if (result.contains("ready")) {
				if (!mCheckServer.isCancelled()) {
					mCheckServer.cancel(true);
				}
				showLayout();
			}
		}
	}

	/**
	 * Setup a new 3D rotation on the container view.
	 * 
	 * @param position
	 *            the item that was clicked to show a picture, or -1 to show the
	 *            list
	 * @param start
	 *            the start angle at which the rotation must begin
	 * @param end
	 *            the end angle of the rotation
	 */
	private void applyRotation(int type, float start, float end) {
		// Find the center of the container
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(type));

		mContainer.startAnimation(rotation);
	}

	/**
	 * This class listens for the end of the first half of the animation. It
	 * then posts a new action that effectively swaps the views when the
	 * container is rotated 90 degrees and thus invisible.
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final int mType;

		public DisplayNextView(int type) {
			this.mType = type;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			mContainer.post(new SwapViews(mType));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
	private final class SwapViews implements Runnable {
		private final int mType;

		public SwapViews(int type) {
			mType = type;
		}

		public void run() {
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			if (mType == 0) {
				// hien thi cau hoi
				mLlQuestion.setVisibility(View.VISIBLE);
				mRlAnswer.setVisibility(View.GONE);

				rotation = new Rotate3dAnimation(90, 360, centerX, centerY,
						310.0f, false);
			} else {
				// hien thi phan tra loi
				mLlQuestion.setVisibility(View.GONE);
				mRlAnswer.setVisibility(View.VISIBLE);

				rotation = new Rotate3dAnimation(90, 0, centerX, centerY,
						310.0f, false);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
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
				convertView = inflater.inflate(R.layout.room_play_answer_row,
						null);

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
			holder.tvIndex.setText((position + 1) + "");
			holder.tvUserName.setText(member.getStrUserName());
			holder.tvAnswer.setText(member.getStrQuestionAnswer());
			holder.tvScore.setText(member.getStrScore());
			holder.tvInfo.setText(member.getStrAbility() + ", "
					+ member.getStrCombo());
			holder.tvIndex.setTextColor(Color.BLACK);
			if (member.getStrQuestionAnswer().equals(mStrTrueAnswer)) {
				holder.tvIndex.setBackgroundColor(Color.YELLOW);
			} else {
				holder.tvIndex.setBackgroundColor(Color.RED);
			}

			if (member.getStrMemberId().equals(String.valueOf(mMemberId))) {
				convertView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.focused_application_background));
			} else {
				convertView.setBackgroundColor(android.R.color.transparent);
			}

			return convertView;
		}
	}

	static class MemberScoreHolder {
		TextView tvIndex, tvUserName, tvAnswer, tvScore, tvInfo;
	}

}
