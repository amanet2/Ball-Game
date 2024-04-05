public class gCamera {
	static int[] move = {0, 0, 0, 0};
	static double[] coords = {0.0, 0.0};
	static double[] vels = {0.0, 0.0, 0.0, 0.0};
	static double accelrate = 0.5;
	static double decelrate = 0.7;
	static long acceltick = 0;
	static int acceldelay = 0;
	static long startTrackingTick = 0;
	static int startTrackingDelay = 200;
	static double trackingMaxDistance = 50;
	static boolean isTracking = false;
	static boolean impulse = false;
	static final double maxVelocity = 32.0;
	static final double maxVelocityTracking = sSettings.clientVelocityPlayerBase;
	static double linearVelocity = 0.0;
	static double fv = 0.0;
	private static boolean isShaking = false;
	private static long shakeDuration = 200;
	private static long shakeTick = 0;

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
		double mx = vels[3] - vels[2];
		double my = vels[1] - vels[0];
		double cdx = coords[0] + mx * mod;
		double cdy = coords[1] + my * mod;
		coords = new double[]{cdx, cdy};
	}

	public static void updatePositionTrackThing(gThing obj) {
		double mod = (double) sSettings.ratesimulation / (double) sSettings.rateShell;
		int[] snapCoords = new int[]{obj.coords[0] + obj.dims[0] / 2, obj.coords[1] + obj.dims[1] / 2};
		double snapDistanceX = Math.abs(coords[0] + eUtils.unscaleInt(sSettings.width / 2) - snapCoords[0]);
		double snapDistanceY = Math.abs(coords[1] + eUtils.unscaleInt(sSettings.height / 2) - snapCoords[1]);
		double camToSnapVectorLength = Math.sqrt(Math.pow(snapDistanceX, 2) + Math.pow(snapDistanceY, 2));

		if(isShaking) {
			shakeAtWorldCoords(snapCoords[0], snapCoords[1]);
			if(shakeTick < sSettings.gameTime)
				isShaking = false;
			return;
		}

		if(!isTracking && camToSnapVectorLength > trackingMaxDistance && startTrackingTick < sSettings.gameTime) {
			startTrackingTick = sSettings.gameTime + startTrackingDelay;
			isTracking = true;
			impulse = true;
		}

		if (acceltick < sSettings.gameTime) {
			acceltick = sSettings.gameTime + acceldelay;
			if (impulse)
				linearVelocity = Math.min(maxVelocityTracking, linearVelocity + accelrate);
			else
				linearVelocity = Math.max(0.0, linearVelocity - decelrate);
		}

		if (camToSnapVectorLength < trackingMaxDistance) {
			impulse = false;
			startTrackingTick = sSettings.gameTime + startTrackingDelay;
			isTracking = false;
		}

		pointAtWorldCoords(snapCoords[0], snapCoords[1]);

		coords = new double[]{
				coords[0] + linearVelocity * mod * Math.cos(fv),
				coords[1] + linearVelocity * mod * Math.sin(fv)
		};

		if(camToSnapVectorLength > 1200)
			snapToWorldCoords(snapCoords[0], snapCoords[1]);
	}

	public static void snapToWorldCoords(int worldX, int worldY) {
		coords = new double[]{
				(double)(worldX - eUtils.unscaleInt(sSettings.width / 2)),
				(double)(worldY - eUtils.unscaleInt(sSettings.height / 2))
		};
	}

	public static void pointAtWorldCoords(int worldX, int worldY) {
		double dx = worldX - eUtils.unscaleInt(sSettings.width / 2) - gCamera.coords[0];
		double dy = worldY - eUtils.unscaleInt(sSettings.height / 2) - gCamera.coords[1];
		double newFV = Math.atan2(dy, dx);
		if(newFV < 0.0)
			newFV += (Math.PI*2);
		fv = newFV;
	}

	// Call this function every tick for a shake effect
	private static void shakeAtWorldCoords(int x, int y) {
		double mod = (double) sSettings.ratesimulation / (double) sSettings.rateShell;
		pointAtWorldCoords(x, y);
		coords = new double[]{
				coords[0] - (16.0)*mod*Math.cos(fv+Math.PI/2),
				coords[1] - (16.0)*mod*Math.sin(fv+Math.PI/2)
		};
	}

	public static void shake() {
		isShaking = true;
		shakeTick = sSettings.gameTime + shakeDuration;
	}
}
