package kyLab.droidcoupler;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundMessageRecorder {

	final static int SAMPLING_RATE = 44100;
	private AudioRecord audioRec = null;
	private int bufSize = 0;
	public boolean isRecordingSuccess = false;
	public boolean isRecordingStop = false;

	SoundMessageRecorder() {
		// calculate buffer size
		bufSize = AudioRecord.getMinBufferSize(SAMPLING_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
		Log.d("SoundMessageRecoder", "bufSize = " + bufSize);
		audioRec = new AudioRecord(MediaRecorder.AudioSource.MIC,
				SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufSize);
	}

	public void getSoundMessage() {
		// start recording
		Log.d("SoundMessageRecoder", "startRecording");
		audioRec.startRecording();
		isRecording = true;
		// recording thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte buf[] = new byte[bufSize];
				while (isRecording) {
					audioRec.read(buf, 0, buf.length);
					Log.d("SoundMessageRecoder", "read " + buf.length + " bytes");
					

				}
				// stop recording
				Log.d("SoundMessageRecoder", "stop");
				audioRec.stop();
			}
		}).start();
	}

	public void stopRecording() {
		if (isRecording) {
			isRecording = false;
		}
	}
}