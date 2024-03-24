public class gCamera {
	static int[] move = {0, 0, 0, 0};
	static int[] coords = {0, 0};
	static int[] trackMove = {0,0,0,0};
	static int[] vels = {0,0,0,0};
	static int accelrate = 2;
	static int decelrate = 1;
	static long acceltick = 0;
	static int acceldelay = 100;
	private static final int maxVelocity = 32;

	public static void updatePositionMapmaker() {
		double mod = (double) sSettings.ratesimulation / (double) sSettings.rateShell;
		if (acceltick < sSettings.gameTime) {
			acceltick = sSettings.gameTime + acceldelay;
			if (move[0] > 0)
				vels[0] = Math.min(sSettings.clientVelocityPlayerBase, vels[0] + accelrate);
			else
				vels[0] = Math.max(0, vels[0] - decelrate);
			if (move[1] > 0)
				vels[1] = Math.min(sSettings.clientVelocityPlayerBase, vels[1] + accelrate);
			else
				vels[1] = Math.max(0, vels[1] - decelrate);
			if (move[2] > 0)
				vels[2] = Math.min(sSettings.clientVelocityPlayerBase, vels[2] + accelrate);
			else
				vels[2] = Math.max(0, vels[2] - decelrate);
			if (move[3] > 0)
				vels[3] = Math.min(sSettings.clientVelocityPlayerBase, vels[3] + accelrate);
			else
				vels[3] = Math.max(0, vels[3] - decelrate);
		}
		int mx = vels[3] - vels[2];
		int my = vels[1] - vels[0];
		int cdx = (int)(coords[0] + ((double)mx * mod));
		int cdy = (int)(coords[1] + ((double)my * mod));
		coords = new int[]{cdx, cdy};
	}

	public static void snapToCoords(int x, int y) {
		coords = new int[]{x, y};
	}
}
