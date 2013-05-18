package kyLab.droidcoupler;

public class SoundParam {

	public static int SAMPLING_RATE = 44100;
	public static int BPS = 10;

	// Half of period (2/f) should be integral divisions of sound window (s/bit).
	public static double freq_s = 200;		// start bit[Hz]
	public static double freq_0 = 100;		// binary [Hz]
	public static double freq_1 = 300;		// binary [Hz]
	public static double freq_e = freq_1;	// end bit[Hz]

}