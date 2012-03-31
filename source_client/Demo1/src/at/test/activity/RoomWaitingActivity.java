/**
 * 
 */
package at.test.activity;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import at.test.R;
import at.test.activity.RoomListActivity.ViewHolder;
import at.test.connect.CheckServer;
import at.test.connect.RequestServer;
import at.test.connect.RequestServer.REQUEST_TYPE;
import at.test.data.DataInfo;
import at.test.delegate.ICheckServer;
import at.test.delegate.IRequestServer;
import at.test.object.Room;
import at.test.object.User;

/**
 * @author Administrator
 * 
 */
public class RoomWaitingActivity extends Activity implements OnClickListener,
		IRequestServer, ICheckServer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	// room id
	String mRoomID;
	String mStrRoomName;
	String mStrOwnerName;
	int mTimePerQuestion, mMemberId;

	// list members in room
	private ListView mLvMembers;
	private RequestServer mRequest;
	private CheckServer mCheck;
	private TextView mTvCounter;
	// bien kiem tra loai user khi vao room
	// hoac la owner hoac la member
	private boolean isOwner;
	
	Button mBtnPlay;

	// // bien kiem tra xem nguoi dung an back hay an nut exit
	// // co bien nay la do khi nguoi dung an nut 'exit' ta goi onBackPress()
	// // gio neu nguoi dung ko an 'exit' ma an nut back
	// // can goi lai cac request cho server
	// private boolean isBackPress = true;

	// arraylist member
	private ArrayList<User> mAlMembers = null;

	private MembersAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Nhan ve du lieu la owner hay member
		Bundle extras = getIntent().getExtras();
		isOwner = extras.getBoolean("owner");

		mRoomID = extras.getString("room_id");
		mStrRoomName = extras.getString("room_name");
		mStrOwnerName = extras.getString("owner_name");
		mTimePerQuestion = extras.getInt("time_per_question");
		mMemberId = extras.getInt("member_id");
		
		// hide statusbar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setTitle("Room Waiting");

		setContentView(R.layout.room_waiting);

		// init ui
		initUI(isOwner);
		mRequest = new RequestServer(this);
		mRequest.getMembersInRoom(mRoomID);

		mAlMembers = new ArrayList<User>();
		adapter = new MembersAdapter(getApplicationContext(), mAlMembers);
		mLvMembers.setAdapter(adapter);
		
		// check server
		mCheck = new CheckServer(this);
		mCheck.checkMembersInRoom(mRoomID);
	}


	/*
	 * init ui Nhan vao la bien xac dinh xem la owner hay member
	 */
	void initUI(boolean isOwner) {
		mBtnPlay = (Button) findViewById(R.id.room_waiting_btn_play);
		Button btnExit = (Button) findViewById(R.id.room_waiting_btn_exit);
		mLvMembers = (ListView) findViewById(R.id.room_waiting_lv_member);
		mTvCounter = (TextView) findViewById(R.id.room_waiting_tv_time);
		TextView tvHeader = (TextView) findViewById(R.id.room_waiting_tv_header);

		String strHeader = "<font color=#ff158a><b><big>" + mStrRoomName
				+ "</big></b></font>" + "<br/>"
				+ "<font color=#00bf00><strong>" + mStrOwnerName
				+ "</strong></font>";
		tvHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
		tvHeader.setText(Html.fromHtml(strHeader));

		if (!isOwner) {
			mBtnPlay.setVisibility(View.GONE);
		}
		mTvCounter.setVisibility(View.INVISIBLE);
		mBtnPlay.setOnClickListener(this);
		btnExit.setOnClickListener(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
//		if (mCheck != null) {
//			if (!mCheck.isCancelled()) {
//				mCheck.cancel(true);
//			}
//		}
//		if (isOwner) {
//			if (mRequest != null) {
//				if (!mRequest.isCancelled()) {
//					mRequest.cancel(true);
//				}
//			}
//			mRequest = new RequestServer(this);
//			mRequest.removeRoom(mRoomID);
//		} else {
//			if (mRequest != null) {
//				if (!mRequest.isCancelled()) {
//					mRequest.cancel(true);
//				}
//			}
//			mRequest = new RequestServer(this);
//			mRequest.exitRoom(mRoomID,
//					String.valueOf(DataInfo.userInfo.getUserId()));
//		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isOwner) {
				if (mRequest != null) {
					if (!mRequest.isCancelled()) {
						mRequest.cancel(true);
					}
				}
				mRequest = new RequestServer(this);
				mRequest.removeRoom(mRoomID);
			} else {
				if (mRequest != null) {
					if (!mRequest.isCancelled()) {
						mRequest.cancel(true);
					}
				}
				mRequest = new RequestServer(this);
				mRequest.exitRoom(mRoomID,
						String.valueOf(DataInfo.userInfo.getUserId()));
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View) Event
	 * khi click vao button Play hay Exit
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.room_waiting_btn_play:
			
			mBtnPlay.setEnabled(false);
			
			if (mRequest != null){
				if (!mRequest.isCancelled()){
					mRequest.cancel(true);
				}
			}
			mRequest = new RequestServer(this);
			mRequest.playGame(mRoomID);
			break;

		// exit
		case R.id.room_waiting_btn_exit:

			// // xac lap bien isBackPress = false => chung to ko phai an Back
			// de thoat
			// isBackPress = false;

			if (isOwner) {
				if (mRequest != null) {
					if (!mRequest.isCancelled()) {
						mRequest.cancel(true);
					}
				}
				mRequest = new RequestServer(this);
				mRequest.removeRoom(mRoomID);
			} else {
				if (mRequest != null) {
					if (!mRequest.isCancelled()) {
						mRequest.cancel(true);
					}
				}
				mRequest = new RequestServer(this);
				mRequest.exitRoom(mRoomID,
						String.valueOf(DataInfo.userInfo.getUserId()));
			}
			if (mCheck != null){
				if (!mCheck.isCancelled()){
					mCheck.cancel(true);
				}
			}
			break;

		default:
			break;
		}
	}

	/*
	 * interface request server Tra ve du lieu nhan duoc tu server
	 */
	@Override
	public void onRequestComplete(String sResult) {
		// neu thuoc request get members
		if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_GET_MEMBERS_IN_ROOM) {
			int len = sResult.length();
			if (sResult.contains("{") && len > 0) {
				int start = sResult.indexOf("{");
				sResult = sResult.substring(start, len);
				DataInfo.setData(sResult);
				if (DataInfo.value) {
					ArrayList<User> alMembers = DataInfo.mListMemberInRoom;
					if (alMembers != null) {
						int size = alMembers.size();
						// co room
						if (size > 0) {
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
		}

		// neu la chu phong
		if (isOwner){
			// request thuoc remove room
			if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_REMOVE_ROOM){
				onBackPressed();
			}
			
			// request playgame
			else if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_PLAY_GAME){
				// tao dong ho dem nguoc 5s
//				int len = sResult.length();
//				if (sResult.contains("{") && len > 0) {
//					int start = sResult.indexOf("{");
//					int end = sResult.lastIndexOf("}");
//					sResult = sResult.substring(start, end);
//					DataInfo.setData(sResult);
//					if (DataInfo.value) {
//						new CountDownTimer(5000, 1000) {
//							
//							@Override
//							public void onTick(long millisUntilFinished) {
//								mTvCounter.setText((5000-millisUntilFinished) + "...");
//							}
//							
//							@Override
//							public void onFinish() {
//								Intent intent = new Intent(getApplicationContext(), GamePlayActivity.class);
//								startActivity(intent);
//							}
//						}.start();
//					} else {
//						
//					}
//				}
			}
		}
		// neu la member
		else{			
			// request exit room
			if (mRequest.getRequestType() == REQUEST_TYPE.REQUEST_EXIT_ROOM){
				onBackPressed();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.test.delegate.ICheckServer#onCheckServerComplete()
	 */

	@Override
	public void onCheckServerComplete(String result) {
		
		// tin hieu thoat khoi phong - chu phong da xoa phong
		if (result.contains("exit")){
			Log.i("onCheckServerComplete", "vao backpressed");
			if (mCheck!=null){
				if (!mCheck.isCancelled()){
					mCheck.cancel(true);
				}
			}
			onBackPressed();
		}
		
		// tin hieu bat dau choi tu chu phong
		else if (result.contains("play")){
			if (mCheck != null) {
				if (!mCheck.isCancelled()) {
					mCheck.cancel(true);
				}
			}

			mTvCounter.setVisibility(View.VISIBLE);
			new CountDownTimer(5000, 1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					mTvCounter.setText("Game playing in " + (int)(millisUntilFinished/1000) + "...");
				}
				
				@Override
				public void onFinish() {
					mTvCounter.setText("Game playing in 0...");
					Intent intent = new Intent(getApplicationContext(), GamePlayActivity.class);
					intent.putExtra("room_id", mRoomID);
					intent.putExtra("time_per_question", mTimePerQuestion);
					intent.putExtra("member_id", mMemberId);
					startActivity(intent);
				}
			}.start();
		}
		
		// tin hieu co nguoi choi moi vao phong
		else{
			if (!mRequest.isCancelled()) {
				mRequest.cancel(true);
			}
			mRequest = new RequestServer(this);
			mRequest.getMembersInRoom(mRoomID);			
		}
	}

	/*
	 * Adapter ListView members
	 */
	class MembersAdapter extends ArrayAdapter<User> {

		private LayoutInflater mInflater;
		private ArrayList<User> alListMembers;

		public MembersAdapter(Context context, ArrayList<User> objects) {
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
				holder.tvUsername = (TextView) convertView
						.findViewById(R.id.room_waiting_row_username);

				convertView.setTag(holder);
			} else {
				holder = (MemberHolder) convertView.getTag();
			}

			User user = alListMembers.get(position);
			Log.i("adapter", user.getUsername());
			holder.tvUsername.setText(user.getUsername());

			return convertView;
		}
	}

	/*
	 * holder adapter
	 */

	static class MemberHolder {
		TextView tvUsername;
	}

}
