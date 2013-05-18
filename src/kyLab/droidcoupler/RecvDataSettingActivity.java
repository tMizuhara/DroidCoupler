package kyLab.droidcoupler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class RecvDataSettingActivity extends Activity implements Runnable{

	private static ProgressDialog recvProgressDialog;
	private Thread thread;
	private String recvText;
	private SoundMessageRecorder soundMessageRecorder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recv_data_setting);
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
		getMenuInflater().inflate(R.menu.recv_data_setting, menu);
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

	public void recvText(View view)
	{
		recvProgressDialog = new ProgressDialog(this);
		recvProgressDialog.setCancelable(false);
		recvProgressDialog.setMessage("データ受信中...");
		recvProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		recvProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "中断", 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				soundMessageRecorder.stopRecording();
                dialog.cancel();
			}
		});
		recvProgressDialog.show();
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		soundMessageRecorder = new SoundMessageRecorder();
		soundMessageRecorder.getSoundMessage();
		
		while(true){
			if(soundMessageRecorder.isRecordingSuccess){
				byte[] recvData = "test".getBytes();
				recvText = null;
				recvText = new String(recvData);
				
				handler.sendEmptyMessage(0);
				break;
			}
			else if(soundMessageRecorder.isRecordingStop){
				break;
			}
		}
		
		soundMessageRecorder = null;
	}
	
	private Handler handler = new Handler() {
	    public void handleMessage(Message msg){
	    	((TextView)findViewById(R.id.recvTextView)).setText(recvText);
			
	        // プログレスダイアログ終了
	        recvProgressDialog.dismiss();
			recvProgressDialog = null;
	    }
	};
}
