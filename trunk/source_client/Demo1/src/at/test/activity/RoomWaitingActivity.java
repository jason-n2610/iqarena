/**
 * 
 */
package at.test.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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
import at.test.data.DataInfo;
import at.test.delegate.ICheckServer;
import at.test.delegate.IRequestServer;
import at.test.object.Room;
import at.test.object.User;

/**
 * @author Administrator
 *
 */
public class RoomWaitingActivity extends Activity implements 
OnClickListener, IRequestServer, ICheckServer{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	// room id
	int mRoomID = 0;
	String mStrRoomName;
	String mStrOwnerName;
	
	// list members in room
	private ListView mLvMembers;
	private RequestServer mRequest;
	private CheckServer mCheck;
	
	// bien kiem tra loai request la getmembers hay exitroom
	private boolean mIsGetMembers = true;
	
	// bien kiem tra loai user khi vao room
	// hoac la owner hoac la member
	private boolean isOwner;

//	// bien kiem tra xem nguoi dung an back hay an nut exit
//	// co bien nay la do khi nguoi dung an nut 'exit' ta goi onBackPress()
//	// gio neu nguoi dung ko an 'exit' ma an nut back
//	// can goi lai cac request cho server
//	private boolean isBackPress = true;
	
	// arraylist member
	private ArrayList<User> mAlMembers = null;
	
	private MembersAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		// Nhan ve du lieu la owner hay member
		Bundle extras = getIntent().getExtras();
		isOwner = extras.getBoolean("owner");
		
		mRoomID = extras.getInt("room_id");
		mStrRoomName = extras.getString("room_name");
		mStrOwnerName = extras.getString("owner_name");
		// hide statusbar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 	
		
		setContentView(R.layout.room_waiting);
		
		// init ui
		initUI(isOwner);
		mRequest = new RequestServer(this);
		mRequest.getMembersInRoom(String.valueOf(mRoomID));
		
		mAlMembers = new ArrayList<User>();
		adapter = new MembersAdapter(getApplicationContext(), mAlMembers);
		mLvMembers.setAdapter(adapter);
	}
	
	/*
	 * init ui
	 * Nhan vao la bien xac dinh xem la owner hay member
	 */
	void initUI(boolean isOwner){
		Button btnPlay = (Button) findViewById(R.id.room_waiting_btn_play);
		Button btnExit = (Button) findViewById(R.id.room_waiting_btn_exit);
		mLvMembers = (ListView) findViewById(R.id.room_waiting_lv_member);
		TextView tvHeader = (TextView) findViewById(R.id.room_waiting_tv_header);
		
		String strHeader = "<font color=#ff158a><b><big>"+ 
				mStrRoomName+"</big></b></font>" + "<br/>" + 
				"<font color=#00bf00><strong>"+mStrOwnerName
				+"</strong></font>";
		tvHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
		tvHeader.setText(Html.fromHtml(strHeader));
		
		if (!isOwner){
			btnPlay.setVisibility(View.GONE);
		}
		btnPlay.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			if (isOwner){
				if (mRequest != null){
					if (!mRequest.isCancelled()){
						mRequest.cancel(true);
					}				
				}
				mRequest = new RequestServer(this);
				mRequest.removeRoom(String.valueOf(mRoomID));
			}
			else{
				if (mRequest != null){
					if (!mRequest.isCancelled()){
						mRequest.cancel(true);
					}
				}
				mRequest = new RequestServer(this);
				mRequest.exitRoom(String.valueOf(mRoomID), String.valueOf(DataInfo.userInfo.getUserId()));
			}
			mIsGetMembers = false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * Event khi click vao button Play hay Exit
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.room_waiting_btn_play:
			
			break;
			
		// exit
		case R.id.room_waiting_btn_exit:
			
//			// xac lap bien isBackPress = false => chung to ko phai an Back de thoat
//			isBackPress = false;
			
			if (isOwner){
				if (mRequest != null){
					if (!mRequest.isCancelled()){
						mRequest.cancel(true);
					}				
				}
				mRequest = new RequestServer(this);
				mRequest.removeRoom(String.valueOf(mRoomID));
			}
			else{
				if (mRequest != null){
					if (!mRequest.isCancelled()){
						mRequest.cancel(true);
					}
				}
				mRequest = new RequestServer(this);
				mRequest.exitRoom(String.valueOf(mRoomID), String.valueOf(DataInfo.userInfo.getUserId()));
			}
			mIsGetMembers = false;
			break;
			
		default:
			break;
		}
	}

	/*
	 * interface request server
	 * Tra ve du lieu nhan duoc tu server 
	 */
	@Override
	public void onRequestComplete(String sResult) {
		Log.i("onRequestComplete", "sResult: "+sResult);
		Log.i("onRequestComplete", "mIsgetMembers: "+mIsGetMembers);
		// neu thuoc request get members
		if (mIsGetMembers){
			int len = sResult.length();
			if (sResult.contains("{") && len > 0) {
				int start = sResult.indexOf("{");
				sResult = sResult.substring(start, len);
				DataInfo.setData(sResult);
				if (DataInfo.value){
					ArrayList<User> alMembers = DataInfo.mListMemberInRoom;
					if (alMembers != null){
						int size = alMembers.size();
						// co room
						if (size > 0) {
							mAlMembers.clear();
							for (int i=0; i<size; i++){
								mAlMembers.add(alMembers.get(i));
							}
							adapter.notifyDataSetChanged();
						}
					}
				}
				else{
					mAlMembers.clear();
					adapter.notifyDataSetChanged();
				}
				
				if (mCheck != null){
					if (!mCheck.isCancelled()){
						mCheck.cancel(true);
					}
				}
				mCheck = new CheckServer(this);
				mCheck.checkMembersInRoom(String.valueOf(mRoomID));
			}
		}
		
		// thuoc request remove room
		else{
			onBackPressed();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see at.test.delegate.ICheckServer#onCheckServerComplete()
	 */

	@Override
	public void onCheckServerComplete(String result) {
		if (!result.contains("false")){
			if (mRequest != null){
				if (!mRequest.isCancelled()){
					mRequest.cancel(true);
				}
				mRequest = new RequestServer(this);
				mRequest.getMembersInRoom(String.valueOf(mRoomID));
			}
		}
		else{
			Log.i("onCheckServerComplete", "vao backpressed");
			onBackPressed();
		}
	}
	
	/*
	 * Adapter ListView members
	 */
	class MembersAdapter extends ArrayAdapter<User>{

		private LayoutInflater mInflater;
		private ArrayList<User> alListMembers;

		public MembersAdapter(Context context, ArrayList<User> objects) {
			super(context, 1, objects);
			mInflater = LayoutInflater.from(context);
			alListMembers = objects;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			MemberHolder holder;
			
			if (convertView == null){
				convertView = mInflater.inflate(R.layout.room_waiting_row, null);
				
				holder = new MemberHolder();
				holder.tvUsername = (TextView) convertView.findViewById(R.id.room_waiting_row_username);
				
				convertView.setTag(holder);
			}
			else{
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
	
	static class MemberHolder{
		TextView tvUsername;
	}

}
