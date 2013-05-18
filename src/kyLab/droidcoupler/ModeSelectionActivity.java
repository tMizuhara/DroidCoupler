package kyLab.droidcoupler;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Intent;

public class ModeSelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mode_selection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mode_selection, menu);
		return true;
	}

	public void selectSendMode(View view) {
		Intent intent = new Intent(this, SendDataSettingActivity.class);
		startActivity(intent);
	}

	public void selectRecvMode(View view) {
		Intent intent = new Intent(this, RecvDataSettingActivity.class);
		startActivity(intent);
	}

}
