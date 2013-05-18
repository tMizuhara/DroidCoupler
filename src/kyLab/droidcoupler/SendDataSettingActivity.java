package kyLab.droidcoupler;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.widget.EditText;

public class SendDataSettingActivity extends Activity implements Runnable{
	
	private static ProgressDialog sendProgressDialog;
	private Thread thread;
	private SoundMessageSender soundMessageSender;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_data_setting);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_data_setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendText(View view) {
		sendProgressDialog = new ProgressDialog(this);
		sendProgressDialog.setMessage("データ送信中...");
		sendProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		sendProgressDialog.show();
		
		thread = new Thread(this);
		thread.start();
	}
	
	 public void run() {
	        // 時間のかかる処理をここに記述。
		 	EditText editText = (EditText)findViewById(R.id.editSendText);
			byte[] sendingData = (editText.getText().toString()).getBytes();
		 	soundMessageSender = new SoundMessageSender();
			soundMessageSender.sendSoundMessage(sendingData);
			
			sendProgressDialog.dismiss();
    		sendProgressDialog = null;
	 }
}
