public class gCamera {
	static int[] move = {0, 0, 0, 0};
	static int[] coords = {0, 0};
	static int[] vels = {0,0,0,0};
	static int accelrate = 2;
	static int decelrate = 3;
	static long acceltick = 0;
	static int acceldelay = 50;
	private static final int maxVelocity = 32;
	static double fv = 0.0;
	static double velx = 0.0;
	static double vely = 0.0;

	public static void updatePositionMapmaker() {
		double mod = (double) sSettings.ratesimulation / (double) sSettings.rateShell;
		if (acceltick < sSettings.gameTime) {
			acceltick = sSettings.gameTime + acceldelay;
			if (move[0] > 0)
				vels[0] = Math.min(maxVelocity, vels[0] + accelrate);
			else
				vels[0] = Math.max(0, vels[0] - decelrate);
			if (move[1] > 0)
				vels[1] = Math.min(maxVelocity, vels[1] + accelrate);
			else
				vels[1] = Math.max(0, vels[1] - decelrate);
			if (move[2] > 0)
				vels[2] = Math.min(maxVelocity, vels[2] + accelrate);
			else
				vels[2] = Math.max(0, vels[2] - decelrate);
			if (move[3] > 0)
				vels[3] = Math.min(maxVelocity, vels[3] + accelrate);
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

	public static void pointAtCoords(int x, int y) {
		double dx = Math.abs(x - gCamera.coords[0]);
		double dy = Math.abs(y - gCamera.coords[1]);
		//		if (angle < 0)
//			angle += 2*Math.PI;
//		angle += Math.PI/2;
		fv = Math.atan2(dy, dx);
	}

	// Call this function every tick for a shake effect
	public static void shakeAt(int x, int y) {
		double mod = (double) sSettings.ratesimulation / (double) sSettings.rateShell;
		int[] snapCoords = new int[]{
				x - eUtils.unscaleInt(sSettings.width / 2),
				y - eUtils.unscaleInt(sSettings.height / 2)
		};
		gCamera.pointAtCoords(snapCoords[0], snapCoords[1]);
		gCamera.coords = new int[]{
				gCamera.coords[0] - (int) (((double)16)*mod*Math.cos(gCamera.fv+Math.PI/2)),
				gCamera.coords[1] - (int) (((double)16)*mod*Math.sin(gCamera.fv+Math.PI/2))
		};
	}
}
