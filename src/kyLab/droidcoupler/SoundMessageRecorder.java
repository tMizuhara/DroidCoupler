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
	private int startSoundCount = 0;
	private boolean isRecording = false;
	private static int FFT_SIZE = 256;
	private SoundBinaryDecoder decoder;
	public byte[] out;

	SoundMessageRecorder() {
		// calculate buffer size
		bufSize = AudioRecord.getMinBufferSize(SAMPLING_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
		Log.d("SoundMessageRecoder", "bufSize = " + bufSize);
		audioRec = new AudioRecord(MediaRecorder.AudioSource.MIC,
				SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufSize);
		decoder = new SoundBinaryDecoder();
		out = new byte[100000];
		for (int i = 0; i < 100000; i++) {
			out[i] = -1;
		}
	}
	
	public void recordSoundMessage() {
		// start recording
		Log.d("SoundMessageRecoder", "startRecording");
		if (audioRec.getState()==android.media.AudioRecord.STATE_INITIALIZED) // check to see if the recorder has initialized yet.
		if (audioRec.getRecordingState()==android.media.AudioRecord.RECORDSTATE_STOPPED)
		audioRec.startRecording();
		// recording thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte buf[] = new byte[bufSize];
				while (!(isRecordingSuccess || isRecordingStop)) {
					audioRec.read(buf, 0, buf.length);
					for (int index = 0; index < bufSize; index++) {
						//Log.d("SoundMessageRecoder", "buf:" + index + ":" + buf[index]);
					}
					//Log.d("SoundMessageRecoder", "read " + buf.length + " bytes");
					
					double[] micBufferData = new double[bufSize];//size may need to change
					
					//int length = convertSoundFromShortToDouble(buf, micBufferData);;
					//Log.d("SoundMessageRecoder", "length:" + length);
					for (int index = 0; index < bufSize; index++) {
						micBufferData[index] = (double)buf[index];
					}
					Log.d("SoundMessageRecoder", "micBuffer:" + micBufferData.length);
				    
					int outIndex = 0;
					
					for (int micBufferDataIndex = 0; micBufferDataIndex < micBufferData.length; ) {
						if (isRecording) {
							byte[] results = new byte[4];
							for (int resultNum = 0; resultNum < 4; resultNum++) {
								double[] decodeSoundData = new double[FFT_SIZE];
								for (int i = 0; i < FFT_SIZE; i++) {
									decodeSoundData[i] = micBufferData[micBufferDataIndex++];
								}
								results[resultNum] = decoder.decSoundByte(decodeSoundData);
							}
						    out[outIndex] = decoder.vote(results);
						    Log.d("SoundMessageRecoder", "out:" + out[outIndex]);
						    if (out[outIndex] == 2) {
						    	isRecordingSuccess = true;
						    }
						    outIndex++;
						} else {
							double[] decodeSoundData = new double[FFT_SIZE];
							for (int j = 0; j < FFT_SIZE; j++) {
								decodeSoundData[j] = micBufferData[micBufferDataIndex++];
								//Log.d("SoundMessageRecoder", "data:" + decodeSoundData[j]);
							}
							isRecording = decoder.checkStart(decodeSoundData);
							if (isRecording) {
								byte[] results = new byte[4];
								for (int resultNum = 0; resultNum < 4; resultNum++) {
									results[resultNum] = decoder.decSoundByte(decodeSoundData);
								}
							    out[outIndex++] = decoder.vote(results);
							}
						}
					}
				}
				// stop recording
				Log.d("SoundMessageRecoder", "stop");
				audioRec.stop();
			}
		}).start();
	}

	public void stopRecording() {
		isRecordingStop = true;
		for (int i = 0; i < 100000; i++) {
			out[i] = -1;
		}
	}
	
	private int convertSoundFromShortToDouble(short[] soundData, double[] micBufferData) {
		//Conversion from short to double
	    final int bytesPerSample = 2; // As it is 16bit PCM
	    final double amplification = 100.0; // choose a number as you like
	    
	    int length = 0;
	    
	    for (int soundIndex = 0, micBufferDataIndex = 0; soundIndex < bufSize - bytesPerSample + 1; soundIndex += bytesPerSample, micBufferDataIndex++) {
	        double sample = 0;
	        for (int b = 0; b < bytesPerSample; b++) {
	            int v = soundData[soundIndex + b];
	            if (b < bytesPerSample - 1 || bytesPerSample == 1) {
	                v &= 0xFF;
	            }
	            sample += v << (b * 8);
	        }
	        double sample32 = amplification * (sample / 32768.0);
	        micBufferData[micBufferDataIndex] = sample32;
	        length++;
	    }
	    
	    return length;
	}
}