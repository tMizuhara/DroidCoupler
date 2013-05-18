package kyLab.droidcoupler;

import android.util.*;

public class BinarySoundEncoder {

	//
	// Byte to Sound data (byte[SAMPLING_RATE])
	//
	public void encByteSound (byte data, byte[] sound) {
		double fs = SoundParam.freq_s;
		double fe = SoundParam.freq_e;
		double f0 = SoundParam.freq_0;
		double f1 = SoundParam.freq_1;
		int L = SoundParam.SAMPLING_RATE;
		int	dL = SoundParam.NUMBER_OF_SAMPLES;
		int t = 0;
		
		Log.d("encode", "data = " + data);

		// start bit
		for (int i = 0; i < dL; ++i, ++t) {
			sound[t] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI * t * fs / L));
		}
		for (int bit = 0; bit < 8; ++bit) {
			Log.d("encode", "[" + bit + "]" + (data >> bit));
			
			if (((data >> bit) & 0x01) == 0x01) {
				for (int i = 0; i < dL; ++i, ++t) {
					sound[t] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI * t * f1 / L));
				}
			} else {
				for (int i = 0; i < dL; ++i, ++t) {
					sound[t] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI * t * f0 / L));
				}
			}
		}
		// stop bit
		for (int i = 0; i < dL; ++i, ++t) {
			sound[t] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI * t * fe / L));
		}
	}
	
}
