/**
 * 
 */
package com.ppclink.iqarena.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.connection.CheckServer;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.connection.ConnectionManager.REQUEST_TYPE;
import com.ppclink.iqarena.delegate.ICheckServer;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.MemberScore;
import com.ppclink.iqarena.ultil.AnalysisData;

/**
 * @author Administrator
 * 
 */
public class RoomWaiting extends Activity implements OnClickListener,
		IRequestServer, ICheckServer {

	private MembersAdapter adapter;
	// bien kiem tra loai user khi vao room
	// hoac la owner hoac la member
	private boolean isOwner;

	// arraylist member
	private ArrayList<MemberScore> mAlMembers = null;
	private Button mBtnPlay, mBtnExit;
	private CheckServer mCheck;
	// list members in room
	private ListView mLvMembers;
	private ConnectionManager mRequest;

	// room id
	String mRoomID;

	// // bien kiem tra xem nguoi dung an back hay an nut exit
	// // co bien nay la do khi nguoi dung an nut 'exit' ta goi onBackPress()
	// // gio neu nguoi dung ko an 'exit' ma an nut back
	// // can goi lai cac request cho server
	// private boolean isBackPress = true;

	String mStrOwnerName;

	String mStrRoomName;

	int mTimePerQuestion, mMemberId;

	private TextView mTvCounter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide statusbar
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().setTitle("Room Waiting");
		
		// Nhan ve du lieu la owner hay member
		Bundle extras = getIntent().getExtras();
		isOwner = extras.getBoolean("owner");
	
		mRoomID = extras.getString("room_id");
		mStrRoomName = extras.getString("room_name");
		mStrOwnerName = extras.getString("owner_name");
		mTimePerQuestion = extras.getInt("time_per_question");
		mMemberId = extras.getInt("member_id");
		
		setContentView(R.layout.room_waiting);
		
		// check server
		mCheck = new CheckServer(this);
		mCheck.checkMembersInRoom(mRoomID);
	
		// init ui
		initUI(isOwner);
		mRequest = new ConnectionManager(this);
		mRequest.getMembersInRoom(mRoomID);
	
		mAlMembers = new ArrayList<MemberScore>();
		adapter = new MembersAdapter(getApplicationContext(), mAlMembers);
		mLvMembers.setAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.test.delegate.ICheckServer#onCheckServerComplete()
	 */
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see at.test.delegate.ICheckServer#onCheckServerComplete()
	 */
	
	@Override
	protected void onPause() {
		super.onPause();
		// if (mCheck != null) {
		// if (!mCheck.isCancelled()) {
		// mCheck.cancel(true);
		// }
		// }
		// if (isOwner) {
		// if (mRequest != null) {
		// if (!mRequest.isCancelled()) {
		// mRequest.cancel(true);
		// }
		// }
		// mRequest = new RequestServer(this);
		// mRequest.removeRoom(mRoomID);
		// } else {
		// if (mRequest != null) {
		// if (!mRequest.isCancelled()) {
		// mRequest.cancel(true);
		// }
		// }
		// mRequest = new RequestServer(this);
		// mRequest.exitRoom(mRoomID,
		// String.valueOf(DataInfo.userInfo.getUserId()));
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isOwner) {
				mRequest = new ConnectionManager(this);
				mRequest.removeRoom(mRoomID);
			} else {
				mRequest = new ConnectionManager(this);
				mRequest.exitRoom(mRoomID,
						String.valueOf(AnalysisData.userInfo.getUserId()));
			}
		}
		return true;
	}

	/*
	 * khi click vao button Play hay Exit
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.room_waiting_btn_play:
	
			mBtnPlay.setEnabled(false);
			mBtnExit.setEnabled(false);
			
			mRequest = new ConnectionManager(this);
			mRequest.playGame(mRoomID);
			break;
	
		// exit
		case R.id.room_waiting_btn_exit:
	
			// // xac lap bien isBackPress = false => chung to ko phai an Back
			// de thoat
			// isBackPress = false;
	
			if (isOwner) {
				mRequest = new ConnectionManager(this);
				mRequest.removeRoom(mRoomID);
			} else {
				mRequest = new ConnectionManager(this);
				mRequest.exitRoom(String.valueOf(mMemberId), mRoomID);
			}
			if (mCheck != null) {
				if (!mCheck.isCancelled()) {
					mCheck.cancel(true);
				}
			}
			break;
	
		default:
			break;
		}
	}

	/*
	 * init ui Nhan vao la bien xac dinh xem la owner hay member
	 */
	void initUI(boolean isOwner) {
		mBtnPlay = (Button) findViewById(R.id.room_waiting_btn_play);
		mBtnExit = (Button) findViewById(R.id.room_waiting_btn_exit);
		mLvMembers = (ListView) findViewById(R.id.room_waiting_lv_member);
		mTvCounter = (TextView) findViewById(R.id.room_waiting_tv_time);
		
		TextView tvHeaderListLogout = (TextView) findViewById(R.id.room_waiting_tv_header_logout);
		TextView tvHeader = (TextView) findViewById(R.id.room_waiting_tv_header);

		String strHeader = "<font color=#ff158a><b><big>" + mStrRoomName
				+ "</big></b></font>";
		tvHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
		tvHeader.setText(Html.fromHtml(strHeader));

		if (!isOwner) {
			mBtnPlay.setVisibility(View.GONE);
			tvHeaderListLogout.setVisibility(View.INVISIBLE);
		}
		mTvCounter.setVisibility(View.INVISIBLE);
		mBtnPlay.setOnClickListener(this);
		mBtnExit.setOnClickListener(this);

	}

	@Override
	public void onCheckServerComplete(String result) {
		
		if (result.contains("time")){
			Toast.makeText(this, result, 500).show();
			return;
		}

		// tin hieu thoat khoi phong - chu phong da xoa phong
		if (result.contains("exit")) {
			if (mCheck != null) {
				if (!mCheck.isCancelled()) {
					mCheck.cancel(true);
				}
			}
			Toast.makeText(this, "Owner destroy room", 500).show();
			onBackPressed();
		}

		// tin hieu bat dau choi tu chu phong
		else if (result.contains("play")) {
			if (mCheck != null) {
				if (!mCheck.isCancelled()) {
					mCheck.cancel(true);
				}
			}

			mBtnExit.setEnabled(false);
			mTvCounter.setVisibility(View.VISIBLE);
			new CountDownTimer(5000, 1000) {

				@Override
				public void onFinish() {
					mTvCounter.setText("Game start in 0...");
					Intent intent = new Intent(getApplicationContext(),
							RoomPlay.class);
					intent.putExtra("room_id", mRoomID);
					intent.putExtra("time_per_question", mTimePerQuestion);
					intent.putExtra("member_id", mMemberId);
					startActivity(intent);
				}

				@Override
				public void onTick(long millisUntilFinished) {
					mTvCounter.setText("Game start in "
							+ (int) (millisUntilFinished / 1000) + "...");
				}
			}.start();
		}

		// tin hieu co su thay doi nguoi choi trong phong
		else {
			mRequest = new ConnectionManager(this);
			mRequest.getMembersInRoom(mRoomID);
		}
	}

	

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.test.delegate.ICheckServer#onCheckServerComplete()
	 */

	

	/*
	 * holder adapter
	 */

	/*
	 * interface request server Tra ve du lieu nhan duoc tu server
	 */
	@Override
	public void onRequestComplete(String sResult) {
		// neu thuoc request get members
		if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_GET_MEMBERS_IN_ROOM) {
			setProgressBarIndeterminateVisibility(true);
			int len = sResult.length();
			if (sResult.contains("{") && len > 0) {
				int start = sResult.indexOf("{");
				sResult = sResult.substring(start, len);
				AnalysisData.analyze(sResult);
				if (AnalysisData.value) {
					ArrayList<MemberScore> alMembers = AnalysisData.mListMemberInRoom;
					if (alMembers != null) {
						int size = alMembers.size();
						// co room
						if (size > 0) {
							// truong hop lay ve list member
							// neu trong danh sach member lay ve co user thi thoa man
							// ko tuc member da bi chu phong duoi khoi phong
							
							// kiem tra xem trong danh sach member co member hien tai ko
							
							boolean isLogouted = false;
							for (int i=0; i<size; i++){
								if (alMembers.get(i).getStrMemberId().
										equals(String.valueOf(mMemberId))){
									isLogouted = true;
									break;
								}
							}
							if (!isLogouted){
								onBackPressed();
							}
							
							// truong hop lay ve danh sach binh thuong
							mAlMembers.clear();
							for (int i = 0; i < size; i++) {
								mAlMembers.add(alMembers.get(i));
							}
							adapter.notifyDataSetChanged();
						}
					}					
				} else {
					mAlMembers.clear();
					adapter.notifyDataSetChanged();
				}
			}
			else{
				Toast.makeText(this, sResult, 500).show();
			}
		}
		else if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_EXIT_ROOM ||
				mRequest.getRequestType() == REQUEST_TYPE.REQUEST_REMOVE_ROOM)
			onBackPressed();
	}

	/*
	 * Adapter ListView members
	 */
	class MembersAdapter extends ArrayAdapter<MemberScore> {
	
		private ArrayList<MemberScore> alListMembers;
		private LayoutInflater mInflater;
	
		public MembersAdapter(Context context, ArrayList<MemberScore> objects) {
			super(context, 1, objects);
			mInflater = LayoutInflater.from(context);
			alListMembers = objects;
		}
	
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			MemberHolder holder;			
	
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.room_waiting_row, null);
	
				holder = new MemberHolder();
				holder.tvIndex = (TextView) convertView
						.findViewById(R.id.room_waiting_row_tv_index);
				holder.tvUsername = (TextView) convertView
						.findViewById(R.id.room_waiting_row_username);
				holder.ibLogout = (ImageButton) convertView.findViewById(R.id.room_waiting_ib_logout);
	
				convertView.setTag(holder);
			} else {
				holder = (MemberHolder) convertView.getTag();
			}
	
			final MemberScore member = alListMembers.get(position);
			holder.tvUsername.setText(member.getStrUserName());
			holder.tvIndex.setText((position+1) + "");
			// highlight owner
			if (member.getStrMemberType().equals("1")){
				holder.tvUsername.setTextColor(Color.GREEN);
				holder.ibLogout.setVisibility(View.INVISIBLE);
			}
			else{
				holder.tvUsername.setTextColor(Color.WHITE);
				holder.ibLogout.setVisibility(View.VISIBLE);
			}
			if (!isOwner){
				holder.ibLogout.setVisibility(View.INVISIBLE);
			}
			
			// highlight user
			if (member.getStrMemberId().equals(String.valueOf(mMemberId))){
				convertView.setBackgroundDrawable(getResources().
						getDrawable(R.drawable.focused_application_background));
			}
			else{
				convertView.setBackgroundColor(android.R.color.transparent);
			}
			
			// logout member
			holder.ibLogout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					setProgressBarIndeterminateVisibility(true);
					mRequest = new ConnectionManager(RoomWaiting.this);
					mRequest.removeMemberInRoom(member.getStrMemberId(), mRoomID);
				}
			});
			
			return convertView;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	static class MemberHolder {
		TextView tvIndex;
		TextView tvUsername;
		ImageButton ibLogout;
	}

}
