package kyLab.droidcoupler;

import android.util.Log;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class SoundBinaryDecoder {

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
	public byte decSoundByte(double[] sound) {
		int FFT_SIZE = sound.length;
		DoubleFFT_1D fft = new DoubleFFT_1D(FFT_SIZE);
		Log.d("FFT", "test");
		fft.realForward(sound);
		
		// data[n]:real, data[n+1}:complex, n=0~N-1
		double[] power = new double[FFT_SIZE / 2];
		double maxPower = 0;
		int maxFFTIndex = 0;
		for (int i = 0; i < FFT_SIZE / 2; i++) {
			power[i] = Math.pow(sound[i * 2], 2) + Math.pow(sound[i * 2 + 1], 2);
			//Log.d("FFT", "i=" + i + " val=" + power[i]);
			if(power[i] > maxPower){
				maxPower = power[i];
				maxFFTIndex = i;
			}
		}
		if(maxFFTIndex > 13)
		{
			return -1;
		}
		else if(maxFFTIndex > 10){
			return 1;
		}
		else if(maxFFTIndex > 7)
		{
			return 2;
		}
		else if(maxFFTIndex > 4)
		{
			return 0;
		}
		
		return -1;
	}
	
	public byte vote(byte[] results, int threshold){
		int[] counter = {0, 0, 0};
		for(int i = 0; i < results.length; i++)
		{
			if(results[i] >= 0){
				counter[results[i]]++;
			}
		}
		byte maxType = -1;
		int maxCount = 0;
		for(byte i = 0; i < 3; i++)
		{
			if(counter[i] > maxCount)
			{
				maxType = i;
				maxCount = counter[i];
			}
		}
		if(maxCount > threshold)
		{
			return maxType;
		}
		return -1;
	}
}
