package kyLab.droidcoupler;

public class SoundParam {

	public static int SAMPLING_RATE = 44100;
	public static int BITRATE = 10;
	// Sound samples per bit data
	public static int SAMPLES_PER_BIT = SAMPLING_RATE / BITRATE;
	// Bits per packet
	public static int PACKET_BITS = 11;
	// Number of samples per packet data
	public static int PACKET_SIZE = PACKET_BITS * SAMPLES_PER_BIT;

	// Half of period (2/f) should be integral divisions of sound window
	// (s/bit).
	public static double freq_0 = 1000;		// binary [Hz]
	public static double freq_1 = 2000; 	// binary [Hz]
	public static double freq_s = 1500; 	// start bit[Hz]
	public static double freq_e = freq_s; 	// end bit[Hz]

}
