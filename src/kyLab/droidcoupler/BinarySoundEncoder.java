package kyLab.droidcoupler;

import android.util.*;

public class BinarySoundEncoder {
	int SAMPLES_PER_BIT;
	byte[] sinWave_s;		// start bit array
	byte[] sinWave_e;		// end bit array
	byte[] sinWave_0;		// 0 bit array
	byte[] sinWave_1;		// 1 bit array

	BinarySoundEncoder() {
		SAMPLES_PER_BIT = SoundParam.SAMPLING_RATE / SoundParam.BPS;
		sinWave_s = new byte[SAMPLES_PER_BIT];
		sinWave_e = new byte[SAMPLES_PER_BIT];
		sinWave_0 = new byte[SAMPLES_PER_BIT];
		sinWave_1 = new byte[SAMPLES_PER_BIT];
		
		for (int i = 0; i < SAMPLES_PER_BIT; ++i) {
			sinWave_s[i] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI
					* i * SoundParam.freq_s / SoundParam.SAMPLING_RATE));
			sinWave_e[i] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI
					* i * SoundParam.freq_e / SoundParam.SAMPLING_RATE));
			sinWave_0[i] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI
					* i * SoundParam.freq_0 / SoundParam.SAMPLING_RATE));
			sinWave_1[i] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI
					* i * SoundParam.freq_1 / SoundParam.SAMPLING_RATE));
		}
	}

	//
	// Encode Byte data to Sound data (byte[SAMPLING_RATE])
	//
	public void encByteSound(byte data, byte[] sound) {
		int t = 0;
		Log.d("encode", "data = " + data);

		// start bit
		System.arraycopy(sinWave_s, 0, sound, t, SAMPLES_PER_BIT);
		t += SAMPLES_PER_BIT;
		Log.d("encode", "[" + t + "]");
		for (int bit = 0; bit < 8; ++bit) {
			Log.d("encode", "[" + bit + "]" + (data >> bit));
			if (((data >> bit) & 0x01) == 0x01) {
				System.arraycopy(sinWave_1, 0, sound, t, SAMPLES_PER_BIT);
			} else {
				System.arraycopy(sinWave_0, 0, sound, t, SAMPLES_PER_BIT);
			}
			t += SAMPLES_PER_BIT;
			Log.d("encode", "[" + t + "]");
		}
		// stop bit
		System.arraycopy(sinWave_e, 0, sound, t, SAMPLES_PER_BIT);
		Log.d("encode", "[" + t + "]");
	}

}
