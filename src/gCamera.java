public class gCamera {
	// selection of modes
	private static final int MODE_FREE = 0;
	private static final int MODE_TRACKING = 1;
	private static int mode = MODE_FREE;
	private static final int[] move = {0, 0, 0, 0};
	private static final int[] coords = {0, 0};
	private static final int velocity = 8;

	public static int getX() {
		return coords[0];
	}

	public static int getY() {
		return coords[1];
	}

	public static void setX(int x) {
		coords[0] = x;
	}

	public static void setY(int y) {
		coords[1] = y;
	}

	public static boolean isTracking() {
		return mode == MODE_TRACKING;
	}

	public static void free() {
		mode = MODE_FREE;
	}
	//enable camera to move in one direction
	public static void move(int p) {
		if(isTracking())
			mode = MODE_FREE;
		move[p] = 1;
	}
	//disable camera moving direction
	public static void stopMove(int p) {
		move[p] = 0;
	}

	public static void updatePosition() {
		if (mode == MODE_FREE) {
			coords[0] += (velocity * move[3] - velocity * move[2]);
			coords[1] += (velocity * move[1] - velocity * move[0]);
		}
	}

	public static void centerCamera() {
		gThing p = cClientLogic.getUserPlayer();
		if(p == null)
			p = cClientLogic.getPlayerById(cVars.get("camplayertrackingid"));
		if(p != null) {
			mode = MODE_TRACKING;
			coords[0] = (((p.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2) + p.getInt("dimw")/2));
			coords[1] = (((p.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2) + p.getInt("dimh")/2));
		}
		else
			mode = MODE_FREE;
	}
}
