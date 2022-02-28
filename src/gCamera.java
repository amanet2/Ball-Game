public class gCamera {
	// selection of modes
	static final int MODE_FREE = 0;
	static final int MODE_TRACKING = 1;
	static int mode;
	static int[] move = {0, 0, 0, 0};
	static int[] coords = {0, 0};
	static int velocity = 9;
	//enable camera to move in one direction
	public static void move(int p) {
		if(cVars.isInt("cammode", MODE_TRACKING))
			cVars.putInt("cammode", MODE_FREE);
		cVars.put("cammov"+p, "1");
	}
	//disable camera moving direction
	public static void stopMove(int p) {
		cVars.put("cammov"+p,"0");
	}

	public static void updatePosition() {
		switch(cVars.getInt("cammode")) {
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
			cVars.putInt("cammode", gCamera.MODE_TRACKING);
			cVars.putInt("camx",
					((p.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2) + p.getInt("dimw")/2));
			cVars.putInt("camy",
					((p.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2) + p.getInt("dimh")/2));
		}
		else
			cVars.putInt("cammode", gCamera.MODE_FREE);
	}
}
