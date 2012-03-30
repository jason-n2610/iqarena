/**
 * 
 */
package at.test.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import at.test.R;
import at.test.connect.RequestServer;
import at.test.data.DataInfo;
import at.test.delegate.IRequestServer;

/**
 * @author hoangnh
 * 
 */
public class CreateNewRoomActivity extends Activity implements OnClickListener,
			IRequestServer{

	Spinner spMaxNumber, spBetScore, spTimePerQuestion;
	EditText etRoomName;
	TextView tvResult;
	Button btnCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);	
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 	
		setContentView(R.layout.create_new_room);
		setProgressBarIndeterminateVisibility(false);

		Button btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCreate = (Button) findViewById(R.id.btn_create);
		etRoomName = (EditText) findViewById(R.id.et_room_name);
		spMaxNumber = (Spinner) findViewById(R.id.sp_max_member);
		spBetScore = (Spinner) findViewById(R.id.sp_bet_score);
		spTimePerQuestion = (Spinner) findViewById(R.id.sp_time_per_question);
		tvResult = (TextView) findViewById(R.id.tv_result);

		btnCancel.setOnClickListener(this);
		btnCreate.setOnClickListener(this);
		tvResult.setText("");
		
		ArrayList<Integer> alMaxNumber = new ArrayList<Integer>();
		for (int i = 0; i < 19; i++) {
			alMaxNumber.add(i + 2);
		}
		ArrayList<Integer> alBetScore = new ArrayList<Integer>();
		for (int i = 5; i < 16; i++) {
			alBetScore.add(i * 100);
		}
		ArrayList<Integer> alTimePerQuestion = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++) {
			alTimePerQuestion.add(i * 5);
		}

		ArrayAdapter<Integer> aaMaxNumber = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, alMaxNumber);
		ArrayAdapter<Integer> aaBetScore = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, alBetScore);
		ArrayAdapter<Integer> aaTimePerQuestion = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, alTimePerQuestion);

		aaMaxNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aaBetScore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aaTimePerQuestion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spMaxNumber.setAdapter(aaMaxNumber);
		spBetScore.setAdapter(aaBetScore);
		spTimePerQuestion.setAdapter(aaTimePerQuestion);
		
		spTimePerQuestion.setSelection(2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_create:
			String strRoomName = etRoomName.getText().toString().trim();
			String strMaxMember = spMaxNumber.getSelectedItem().toString();
			String strBetScore = spBetScore.getSelectedItem().toString();
			String strTimePerQuestion = spTimePerQuestion.getSelectedItem().toString();
			if (strRoomName.length() > 0){
				RequestServer requestServer = new RequestServer(this);
				requestServer.createNewRoom(strRoomName, strMaxMember, strBetScore, strTimePerQuestion);
				btnCreate.setEnabled(false);
				setProgressBarIndeterminateVisibility(true);
			}
			else{
				tvResult.setText("Tên room không hợp lệ!");
			}
			break;

		case R.id.btn_cancel:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	// interface IRequestServer
	@Override
	public void onRequestComplete(String sResult) {
		String strMessage = sResult;
		if (strMessage != null){
			sResult = sResult.trim();
			int length = sResult.length();
			// if co thong bao
			if (length > 0) {
				// kiem tra xem co thong tin tu server tra ve ko?
				if (sResult.contains("{")) {
					int start = sResult.indexOf("{");
					sResult = sResult.substring(start, length);
					boolean isSuccess = DataInfo.setData(sResult);
					strMessage = DataInfo.message;
					String roomId = String.valueOf(DataInfo.roomId);
					if (isSuccess) {
						// neu create thanh cong
						if (DataInfo.value) {
							Intent intent = new Intent(getApplicationContext(), RoomWaitingActivity.class);
							intent.putExtra("owner", true);
							intent.putExtra("room_id", roomId);
							intent.putExtra("room_name", etRoomName.getText().toString().trim());
							intent.putExtra("owner_name", DataInfo.userInfo.getUsername());
							intent.putExtra("time_per_question", value);
							startActivity(intent);
							strMessage = "";
						} 
					}
				}
			}
		}
		else{
			strMessage = "Tạo room thất bại";
		}
		btnCreate.setEnabled(true);
		tvResult.setText(strMessage);
		setProgressBarIndeterminateVisibility(false);
	}
}
