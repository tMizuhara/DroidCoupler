package kyLab.droidcoupler;

import android.util.*;

public class BinarySoundEncoder {

	//
	// Byte to Sound data (byte[length = 44100])
	//
	public void encByteSound (byte data, byte[] sound, int length) {
		double freq_s = 200; //[Hz];
		double freq_0 = 100; //[Hz];
		double freq_1 = 300; //[Hz];
		double freq_e = freq_1;
		
		int	dL = length / 10;
		int t = 0;
		
		Log.d("encode", "data = " + data);

		// start bit
		for (int i = 0; i < dL; ++i, ++t) {
			sound[t] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI * t * freq_s / length));
		}
		for (int bit = 0; bit < 8; ++bit) {
			Log.d("encode", "[" + bit + "]" + (data >> bit));
			
			if (((data >> bit) & 0x01) == 0x01) {
				for (int i = 0; i < dL; ++i, ++t) {
					sound[t] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI * t * freq_1 / length));
				}
			} else {
				for (int i = 0; i < dL; ++i, ++t) {
					sound[t] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI * t * freq_0 / length));
				}
			}
		}
		// stop bit
		for (int i = 0; i < dL; ++i, ++t) {
			sound[t] = (byte) (Byte.MAX_VALUE * Math.sin(2.0 * Math.PI * t * freq_e));
		}
	}
	
}
