package kyLab.droidcoupler;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class SoundMessageSender {

	private AudioTrack track = null;
	private byte[] sendingData = { 1, 2, 3, 4, 5 };
	private BinarySoundEncoder encoder;
	
	//private static int SAMPLING_RATE = 441000;
	//private static int BPS = 10;
	//private static int NUMBER_OF_SAMPLES = (int) (SAMPLING_RATE * (1.0 / BPS));

	SoundMessageSender() {
		Log.d("SoundMessageSender", "sampling_rate:" + SoundParam.SAMPLING_RATE);
		Log.d("SoundMessageSender", "bps:" + SoundParam.BPS);
		Log.d("SoundMessageSender", "number_of_samples:" + SoundParam.NUMBER_OF_SAMPLES);
		
		if (track == null) {
			// track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
			// AudioFormat.CHANNEL_CONFIGURATION_DEFAULT,
			// AudioFormat.ENCODING_DEFAULT, 44100, AudioTrack.MODE_STATIC);
			track = new AudioTrack(AudioManager.STREAM_MUSIC, SoundParam.NUMBER_OF_SAMPLES,
					AudioFormat.CHANNEL_OUT_DEFAULT,
					AudioFormat.ENCODING_DEFAULT, SoundParam.NUMBER_OF_SAMPLES, AudioTrack.MODE_STATIC);
		}
		
		encoder = new BinarySoundEncoder();
	}

	public void sendSoundMessage(byte[] inputMessage) {
		byte[] soundData = new byte[SoundParam.NUMBER_OF_SAMPLES];

		for (int i = 0; i < sendingData.length; i++) {
			encoder.encByteSound(sendingData[i], soundData);
			track.write(soundData, 0, soundData.length);

			if (track.getPlayState() == android.media.AudioTrack.PLAYSTATE_PLAYING) {
				track.stop();
				track.reloadStaticData();
			}
			track.play();
		}
	}
}
