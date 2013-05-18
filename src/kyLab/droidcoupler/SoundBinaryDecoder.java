package kyLab.droidcoupler;

//import android.util.Log;
//import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class SoundBinaryDecoder {

	/*
	byte[] sinWave_s;	// start bit array
	byte[] sinWave_e;	// end bit array
	byte[] sinWave_0;	// 0 bit array
	byte[] sinWave_1;	// 1 bit array

	SoundBinaryDecoder() {
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
	// Decode Sound data to Byte data
	//
	//public void decSoundByte(double[] data, double[] sound)
	public void decSoundByte(double[] sound) {
		Log.d("FFT", "test");
		int FFT_SIZE = sound.length;		
		DoubleFFT_1D fft = new DoubleFFT_1D(FFT_SIZE);
		fft.realForward(sound);
		
		// data[n]:real, data[n+1}:complex, n=0~N-1
		for (int i = 0; i < FFT_SIZE; ++i) {
			Log.d("FFT", "i=" + i + " val=" + sound[i]);
		}
	}
	*/

}
