public class gCamera {
	// selection of modes
	static final int MODE_LOCKED = -1;
	static final int MODE_FREE = 0;
	static final int MODE_TRACKING = 1;

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
				xCon.ex("centercamera");
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
}
