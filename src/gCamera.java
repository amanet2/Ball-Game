public class gCamera {
	// selection of modes
	static final int MODE_FREE = 0;
	static final int MODE_TRACKING = 1;
	static final int MODE_LOCKED = 2;
	static final int MODE_PROCEEDING = 3;
	static final int MODE_SHAKYPROCEEDING = 4;

	//enable camera to move in one direction
	public static void move(int p) {
		if(!cVars.isInt("cammode", MODE_LOCKED)) {
			if(!cVars.isInt("cammode", MODE_PROCEEDING))
				cVars.putInt("cammode", MODE_FREE);
			cVars.put("cammov"+p, "1");
		}
	}

	public static void shakyproceedto(int x, int y) {
		int cx = cVars.getInt("camx");
		int cy = cVars.getInt("camy");
		if(cx != x) {
			if(cx > x + cVars.getInt("velocitycam")) {
				cVars.put("cammov2", "1");
				cVars.put("cammov3", "0");
			}
			if(cx < x - cVars.getInt("velocitycam")) {
				cVars.put("cammov2", "0");
				cVars.put("cammov3", "1");
			}
		}
		if(cy != y) {
			if(cy > y + cVars.getInt("velocitycam")) {
				cVars.put("cammov0", "1");
				cVars.put("cammov1", "0");
			}
			if(cy < y - cVars.getInt("velocitycam")) {
				cVars.put("cammov0", "0");
				cVars.put("cammov1", "1");
			}
		}
	}

	public static void proceedTo(int x, int y) {
		for(int i = 0; i < 4; i++) {
			cVars.put("cammov"+i, "0");
		}
		shakyproceedto(x, y);
	}
	//disable camera moving direction
	public static void stopMove(int p) {
		cVars.put("cammov"+p,"0");
	}

	public static void updatePosition() {
		gPlayer userPlayer = cGameLogic.userPlayer();
		switch(cVars.getInt("cammode")) {
			case MODE_TRACKING:
				xCon.ex("centercamera");
				break;
			case MODE_PROCEEDING:
				proceedTo((userPlayer.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2)
								+ userPlayer.getInt("dimw")/2,
						(userPlayer.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2)
								+ userPlayer.getInt("dimh")/2);
				break;
			case MODE_SHAKYPROCEEDING:
				shakyproceedto(
						(userPlayer.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2)
								+ userPlayer.getInt("dimw")/2,
						(userPlayer.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2)
								+ userPlayer.getInt("dimh")/2);
				break;
			case MODE_LOCKED:
				cVars.addIntVal("camx", cVars.getInt("velocitycam")*cVars.getInt("cammov3")
						- cVars.getInt("velocitycam")*cVars.getInt("cammov2"));
				cVars.addIntVal("camy", cVars.getInt("velocitycam")*cVars.getInt("cammov1")
						- cVars.getInt("velocitycam")*cVars.getInt("cammov0"));
				break;
			default:
				break;
		}
	}
}
