public class gCamera {
	// selection of modes
	static final int MODE_FREE = 0;
	static final int MODE_TRACKING = 1;
	static int mode = MODE_FREE;
	static int[] move = {0, 0, 0, 0};
	static int[] coords = {0, 0};
	static int velocity = 8;

	public static boolean isTracking() {
		return mode == MODE_TRACKING;
	}
	//enable camera to move in one direction
	public static void move(int p) {
		if(isTracking())
			mode = MODE_FREE;
		cVars.put("cammov"+p, "1");
	}
	//disable camera moving direction
	public static void stopMove(int p) {
		cVars.put("cammov"+p,"0");
	}

	public static void updatePosition() {
		switch(mode) {
			case MODE_TRACKING:
				centerCamera();
				break;
			case MODE_FREE:
				cVars.addIntVal("camx", cVars.getInt("velocitycam")*cVars.getInt("cammov3")
						- cVars.getInt("velocitycam")*cVars.getInt("cammov2"));
				cVars.addIntVal("camy", cVars.getInt("velocitycam")*cVars.getInt("cammov1")
						- cVars.getInt("velocitycam")*cVars.getInt("cammov0"));
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
