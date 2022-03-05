public class gCamera {
	// selection of modes
	static final int MODE_FREE = 0;
	static final int MODE_TRACKING = 1;
	static int mode = MODE_FREE;
	private static int[] move = {0, 0, 0, 0};
	static int[] coords = {0, 0};
	static int velocity = 8;

	public static boolean isTracking() {
		return mode == MODE_TRACKING;
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
		switch(mode) {
//			case MODE_TRACKING:
////				centerCamera();
//				break;
			case MODE_FREE:
				cVars.addIntVal("camx", velocity*move[3] - velocity*move[2]);
				cVars.addIntVal("camy", velocity*move[1] - velocity*move[0]);
				break;
			default:
				break;
		}
	}

	public static void centerCamera() {
		gThing p = cClientLogic.getUserPlayer();
		if(p == null)
			p = cClientLogic.getPlayerById(cVars.get("camplayertrackingid"));
		if(p != null) {
			mode = MODE_TRACKING;
			cVars.putInt("camx",
					((p.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2) + p.getInt("dimw")/2));
			cVars.putInt("camy",
					((p.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2) + p.getInt("dimh")/2));
		}
		else
			mode = MODE_FREE;
	}
}
