package kyLab.droidcoupler;

public class SoundParam {

	public static int SAMPLING_RATE = 441000;
	public static int BPS = 10;
	public static int NUMBER_OF_SAMPLES = (int) (SAMPLING_RATE * (1.0 / BPS));

	public static double freq_s = 200;		// start bit[Hz]
	public static double freq_0 = 100;		// binary [Hz]
	public static double freq_1 = 300;		// binary [Hz]
	public static double freq_e = freq_1;	// end bit[Hz]

}
