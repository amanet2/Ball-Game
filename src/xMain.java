public class xMain {
	private static void init(String[] args) {
		eManager.init();
		gExecDoableFactory.instance().init();
		cServerVars.instance().init(args);
		cClientVars.instance().init(args);
		xCon.ex("exec config/autoexec.cfg");
		uiMenus.init();
		if(sSettings.show_mapmaker_ui) {
			sSettings.drawhitboxes = true;
			sSettings.drawmapmakergrid = true;
			eUtils.zoomLevel = 0.5;
		}
	}

	public static void main(String[] args) {
		try {
			init(args);
			eGameEngine eng = new eGameEngine(new eGameLogicBallGame());
			eng.run();
		}
		catch (Exception err) {
			err.printStackTrace();
			System.exit(-1);
		}
	}
}
