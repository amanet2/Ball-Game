public class gCamera {
	static int[] move = {0, 0, 0, 0};
	static int[] coords = {0, 0};
	static int[] trackMove = {0,0,0,0};
	static int[] vels = {0,0,0,0};
	private static final int maxVelocity = 32;

	public static void updatePositionMapmaker() {
		double mod = (double)sSettings.ratesimulation/(double)sSettings.rateShell;

		coords[0] += (maxVelocity * mod * move[3] - maxVelocity * mod * move[2]);
		coords[1] += (maxVelocity * mod * move[1] - maxVelocity * mod * move[0]);
	}

	public static void snapToCoords(int x, int y) {
		gCamera.coords = new int[]{x, y};
	}
}
