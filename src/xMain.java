import java.util.ArrayList;

public class xMain {
	private static void initGameObjectsAndScenes() {
		xCon.ex("exec " + sSettings.CONFIG_FILE_LOCATION_GAME);
		int ctr = 0;
		ArrayList<String> thingTypes = new ArrayList<>();
		while(!xCon.ex("setvar THING_"+ctr).equals("null")) {
			thingTypes.add(xCon.ex("setvar THING_"+ctr));
			ctr++;
		}
		sSettings.object_titles = thingTypes.toArray(String[]::new);
		cClientLogic.scene = new gScene();
		cServerLogic.scene = new gScene();
		uiEditorMenus.previewScene = new gScene();
	}

	private static void init(String[] args) {
		eManager.init();
		gExecDoableFactory.instance().init();
		gScriptFactory.instance().init();
		cServerLogic.init(args);
		cClientLogic.init(args);
		initGameObjectsAndScenes();
		uiMenus.init();
	}

	public static eGameLogicShell shellLogic;

	public static void main(String[] args) {
		try {
			init(args);
			shellLogic = new eGameLogicShell();
			eGameSession shellSession = new eGameSession(shellLogic, sSettings.rateShell);
			shellLogic.setParentSession(shellSession);
			shellSession.start();
		}
		catch (Exception err) {
			err.printStackTrace();
			System.exit(-1);
		}
	}
}
