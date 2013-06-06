package kyLab.droidcoupler;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundMessageRecorder {
	
	private AudioRecord audioRec = null;
	private SoundBinaryDecoder decoder;
	private int recordingBufSize = 0;
	private boolean canDecodeMessageSound = false;
	private static int BUF_SIZE_NECESSARY_FOR_VOTE = 4;
	
	public boolean isRecordingSuccess = false;
	public boolean isRecordingStop = false;
	public byte[] recordingBinaryData;

	SoundMessageRecorder() {
		// calculate buffer size
		recordingBufSize = AudioRecord.getMinBufferSize(SoundParam.SAMPLING_RATE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
		Log.d("SoundMessageRecoder", "bufSize = " + recordingBufSize);
		audioRec = new AudioRecord(MediaRecorder.AudioSource.MIC,
				SoundParam.SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, recordingBufSize);
		decoder = new SoundBinaryDecoder();
		recordingBinaryData = new byte[100000];
		for (int i = 0; i < 100000; i++) {
			recordingBinaryData[i] = -1;
		}
	}
	
	public void recordSoundMessage() {
		// start recording
		Log.d("SoundMessageRecoder", "startRecording");
		if (audioRec.getState()==android.media.AudioRecord.STATE_INITIALIZED) { // check to see if the recorder has initialized yet.
			if (audioRec.getRecordingState()==android.media.AudioRecord.RECORDSTATE_STOPPED) {
				audioRec.startRecording();
			}
		}
		
		// recording thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte recordingBuf[] = new byte[recordingBufSize];
				while (!(isRecordingSuccess || isRecordingStop)) {
					audioRec.read(recordingBuf, 0, recordingBuf.length);
					//for (int index = 0; index < recordingBufSize; index++) {
						//Log.d("SoundMessageRecoder", "buf:" + index + ":" + buf[index]);
					//}
					//Log.d("SoundMessageRecoder", "read " + buf.length + " bytes");
					double[] micBufferData = new double[recordingBufSize];
					
					//int length = convertSoundFromShortToDouble(buf, micBufferData);;
					//Log.d("SoundMessageRecoder", "length:" + length);
					for (int index = 0; index < recordingBufSize; index++) {
						micBufferData[index] = (double)recordingBuf[index];
					}
				    
					int recordingBinaryDataIndex = 0;
					for (int micBufferDataIndex = 0; micBufferDataIndex < micBufferData.length; ) {
						if (canDecodeMessageSound) {
							byte[] results = new byte[BUF_SIZE_NECESSARY_FOR_VOTE];
							for (int resultNum = 0; resultNum < BUF_SIZE_NECESSARY_FOR_VOTE; resultNum++) {
								double[] decodeSoundData = new double[SoundParam.FFT_SIZE];
								for (int i = 0; i < SoundParam.FFT_SIZE; i++) {
									decodeSoundData[i] = micBufferData[micBufferDataIndex++];
								}
								results[resultNum] = decoder.decSoundByte(decodeSoundData);
							}
						    recordingBinaryData[recordingBinaryDataIndex] = decoder.vote(results);
						    Log.d("SoundMessageRecoder", "out:" + recordingBinaryData[recordingBinaryDataIndex]);
						    if (recordingBinaryData[recordingBinaryDataIndex] == 2) {
						    	isRecordingSuccess = true;
						    }
						    recordingBinaryDataIndex++;
						} else {
							double[] decodeSoundData = new double[SoundParam.FFT_SIZE];
							for (int j = 0; j < SoundParam.FFT_SIZE; j++) {
								decodeSoundData[j] = micBufferData[micBufferDataIndex++];
								//Log.d("SoundMessageRecoder", "data:" + decodeSoundData[j]);
							}
							canDecodeMessageSound = decoder.checkStart(decodeSoundData);
							if (canDecodeMessageSound) {
								Log.d("SoundMessageRecoder", "message start");
								byte[] results = new byte[BUF_SIZE_NECESSARY_FOR_VOTE];
								for (int resultNum = 0; resultNum < BUF_SIZE_NECESSARY_FOR_VOTE; resultNum++) {
									results[resultNum] = decoder.decSoundByte(decodeSoundData);
								}
							    recordingBinaryData[recordingBinaryDataIndex++] = decoder.vote(results);
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
			recordingBinaryData[i] = -1;
		}
	}
}