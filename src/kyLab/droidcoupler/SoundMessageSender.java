package kyLab.droidcoupler;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class SoundMessageSender {

	private AudioTrack track = null;
	private BinarySoundEncoder encoder;
	private enum HeaderSound {
		START,
		END,
	}

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
		byte[] soundData = new byte[SoundParam.PACKET_SIZE];
		
		// send start sound data
		sendHeaderSound(HeaderSound.START);

		for (int i = 0; i < inputMessage.length; i++) {
			encoder.encByteSoundSimple(inputMessage[i], soundData);
			track.write(soundData, 0, soundData.length);

			if (track.getPlayState() == android.media.AudioTrack.PLAYSTATE_PLAYING) {
				track.stop();
				track.reloadStaticData();
			}
			track.play();
		}
		
		// send end sound data
		sendHeaderSound(HeaderSound.END);
	}
	
	private void sendHeaderSound(HeaderSound headerType) {
		switch (headerType) {
		case START:
			track.write(encoder.genStartSound(), 0, encoder.genStartSound().length);
			break;
		case END:
			track.write(encoder.genEndSound(), 0, encoder.genEndSound().length);
			break;
		default:
			break;
		}
		
		if (track.getPlayState() == android.media.AudioTrack.PLAYSTATE_PLAYING) {
			track.stop();
			track.reloadStaticData();
		}
		
		track.play();
	}
}
