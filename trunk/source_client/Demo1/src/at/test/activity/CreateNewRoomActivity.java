/**
 * 
 */
package at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.test.R;

/**
 * @author hoangnh
 *
 */
public class CreateNewRoomActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_new_room);
		Button btnCreate = (Button) findViewById(R.id.btnCreate);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		
		btnCancel.setOnClickListener(this);
		btnCreate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCreate:
			
			break;

		case R.id.btnCancel:
			onBackPressed();
			break;
		default:
			break;
		}
	}
	
}
