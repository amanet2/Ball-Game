public class gCamera {
	static int[] move = {0, 0, 0, 0};
	static int[] coords = {0, 0};
	private static final int velocity = 32;

	public static void updatePosition() {
		double mod = (double)sSettings.ratesimulation/(double)sSettings.rateShell;

		coords[0] += (velocity * mod * move[3] - velocity * mod * move[2]);
		coords[1] += (velocity * mod * move[1] - velocity * mod * move[0]);
	}
}
