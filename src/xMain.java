public class xMain {
	private static void init(String[] args) {
		eManager.init();
		gExecDoableFactory.instance().init();
		cServerVars.instance().init();
		cClientVars.instance().init();
		xCon.ex("exec "+sSettings.CONFIG_FILE_LOCATION_SERVER);
		xCon.ex("exec "+sSettings.CONFIG_FILE_LOCATION_CLIENT);
		xCon.ex("exec config/autoexec.cfg");
		cServerVars.instance().loadFromFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
		cClientVars.instance().loadFromFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
		cServerVars.instance().loadFromLaunchArgs(args);
		cClientVars.instance().loadFromLaunchArgs(args);
		uiMenus.menuSelection[uiMenus.MENU_MAP].setupMenuItems();
		uiMenus.menuSelection[uiMenus.MENU_CONTROLS].items = uiMenusControls.getControlsMenuItems();
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
