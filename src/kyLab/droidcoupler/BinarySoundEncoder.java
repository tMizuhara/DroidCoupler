package kyLab.droidcoupler;

import android.util.*;

public class BinarySoundEncoder {
	byte[] sinWave_s;		// start bit array
	byte[] sinWave_e;		// end bit array
	byte[] sinWave_0;		// 0 bit array
	byte[] sinWave_1;		// 1 bit array

	BinarySoundEncoder() {
		sinWave_s = new byte[SoundParam.SAMPLES_PER_BIT];
		sinWave_e = new byte[SoundParam.SAMPLES_PER_BIT];
		sinWave_0 = new byte[SoundParam.SAMPLES_PER_BIT];
		sinWave_1 = new byte[SoundParam.SAMPLES_PER_BIT];
		
		for (int i = 0; i < SoundParam.SAMPLES_PER_BIT; ++i) {
			sinWave_s[i] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI
					* SoundParam.freq_s * i / SoundParam.SAMPLING_RATE));
			sinWave_e[i] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI
					* SoundParam.freq_e * i / SoundParam.SAMPLING_RATE));
			sinWave_0[i] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI
					* SoundParam.freq_0 * i / SoundParam.SAMPLING_RATE));
			sinWave_1[i] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI
					* SoundParam.freq_1 * i / SoundParam.SAMPLING_RATE));
		}
	}

	//
	// Encode Byte data to Sound data (byte[SAMPLING_RATE])
	//
	public void encByteSound(byte data, byte[] sound) {
		int t = 0;
		Log.d("encode", "data = " + data);

		// start bit
		System.arraycopy(sinWave_s, 0, sound, t, SoundParam.SAMPLES_PER_BIT);
		t += SoundParam.SAMPLES_PER_BIT;
		Log.d("encode", "[" + t + "]");
		for (int bit = 0; bit < 8; ++bit) {
			Log.d("encode", "[" + bit + "]" + (data >> bit));
			if (((data >> bit) & 0x01) == 0x01) {
				System.arraycopy(sinWave_1, 0, sound, t, SoundParam.SAMPLES_PER_BIT);
			} else {
				System.arraycopy(sinWave_0, 0, sound, t, SoundParam.SAMPLES_PER_BIT);
			}
			t += SoundParam.SAMPLES_PER_BIT;
			Log.d("encode", "[" + t + "]");
		}
		// stop bit
		System.arraycopy(sinWave_e, 0, sound, t, SoundParam.SAMPLES_PER_BIT);
		t += SoundParam.SAMPLES_PER_BIT;
		System.arraycopy(sinWave_e, 0, sound, t, SoundParam.SAMPLES_PER_BIT);
		Log.d("encode", "[" + t + "]");
	}

}
