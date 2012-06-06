package com.ppclink.iqarena.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.ultil.AnalysisData;

public class UploadQuestion extends Activity implements OnClickListener, IRequestServer{
	
	ConnectionManager mConnection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_question);
		
		Button btnUpload = (Button) findViewById(R.id.uq_btn_upload);
		Button btnReset = (Button) findViewById(R.id.uq_btn_reset);
		Spinner spLevel = (Spinner) findViewById(R.id.up_sp_level);
		Spinner spAnswer = (Spinner) findViewById(R.id.up_sp_answer);
		
		btnUpload.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		
		ArrayList<Integer> alLevel = new ArrayList<Integer>();
		for (int i=1; i<16; i++){
			alLevel.add(i);
		}
		ArrayList<String> alAnswer = new ArrayList<String>();
		alAnswer.add("A");
		alAnswer.add("B");
		alAnswer.add("C");
		alAnswer.add("D");				
		ArrayAdapter<Integer> aaLevel = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, alLevel);
		ArrayAdapter<String> aaAnswer = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, alAnswer);
		aaLevel.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		aaAnswer.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		spAnswer.setAdapter(aaAnswer);
		spLevel.setAdapter(aaLevel);
		spAnswer.setSelection(0);
		spLevel.setSelection(0);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.uq_btn_upload:
			String strQuestion = ((EditText)findViewById(R.id.uq_et_question)).
									getText().toString();
			String strA = ((EditText)findViewById(R.id.up_et_a)).
					getText().toString();
			String strB = ((EditText)findViewById(R.id.up_et_b)).
					getText().toString();
			String strC = ((EditText)findViewById(R.id.up_et_c)).
					getText().toString();
			String strD = ((EditText)findViewById(R.id.up_et_d)).
					getText().toString();
			String strAnswer = String.valueOf(((Spinner)findViewById(
					R.id.up_sp_answer)).
					getSelectedItemPosition()+1);
			String strDescrible = ((EditText)findViewById(R.id.up_et_des)).
					getText().toString();
			String strLevel = String.valueOf(((Spinner)findViewById(
					R.id.up_sp_level)).
					getSelectedItemPosition()+1);
			if (strQuestion.length()==0 || strA.length()==0 || 
					strB.length()==0 || strC.length()==0 || strD.length()==0){
				((TextView)findViewById(R.id.tv_result)).setText("You must " +
						"enter full infomation");
			}
			else{
				((Button) findViewById(R.id.uq_btn_upload)).setEnabled(false);
				if (mConnection != null){
					if (!mConnection.isCancelled()){
						mConnection.cancel(true);
					}
				}
				mConnection = new ConnectionManager(this);
				mConnection.uploadQuestion(strQuestion, strLevel, 
						strA, strB, strC, strD, strAnswer, strDescrible);
			}
			break;
			
		case R.id.uq_btn_reset:
			((Button) findViewById(R.id.uq_btn_upload)).setEnabled(true);
			((EditText)findViewById(R.id.uq_et_question)).setText("");
			((EditText)findViewById(R.id.up_et_a)).setText("");
			((EditText)findViewById(R.id.up_et_b)).setText("");
			((EditText)findViewById(R.id.up_et_c)).setText("");
			((EditText)findViewById(R.id.up_et_d)).setText("");
			((EditText)findViewById(R.id.up_et_des)).setText("");
			((TextView)findViewById(R.id.tv_result)).setText("");
			break;

		default:
			break;
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		if (sResult != null){
			if (sResult.contains("{")){
				AnalysisData.analyze(sResult);
				((TextView) findViewById(R.id.tv_result)).
					setText(AnalysisData.message);
			}
			else{
				((TextView) findViewById(R.id.tv_result)).
				setText(sResult);
			}
		}
	}	
}
