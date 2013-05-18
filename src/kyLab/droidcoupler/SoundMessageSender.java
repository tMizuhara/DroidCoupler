package kyLab.droidcoupler;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class SoundMessageSender {

	private AudioTrack track = null;
	private BinarySoundEncoder encoder;

	SoundMessageSender() {
		Log.d("SoundMessageSender", "sampling_rate:" + SoundParam.SAMPLING_RATE);
		
		if (track == null) {
			track = new AudioTrack(AudioManager.STREAM_MUSIC, SoundParam.SAMPLING_RATE,
					AudioFormat.CHANNEL_OUT_DEFAULT,
					AudioFormat.ENCODING_DEFAULT, SoundParam.SAMPLING_RATE, AudioTrack.MODE_STREAM);
		}
		encoder = new BinarySoundEncoder();
	}

	public void sendSoundMessage(byte[] inputMessage) {
		byte[] soundData = new byte[SoundParam.SAMPLING_RATE];

		for (int i = 0; i < inputMessage.length; i++) {
			encoder.encByteSound(inputMessage[i], soundData);
			track.write(soundData, 0, soundData.length);

			if (track.getPlayState() == android.media.AudioTrack.PLAYSTATE_PLAYING) {
				track.stop();
				track.reloadStaticData();
			}
			track.play();
		}
	}
}
