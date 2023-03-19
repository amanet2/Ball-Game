import java.util.ArrayList;

public class xMain {
	private static void initGameObjects() {
		xCon.ex("exec config/gametypes");
		xCon.ex("exec config/thingsdef");
		int ctr = 0;
		ArrayList<String> thingTypes = new ArrayList<>();
		while(!xCon.ex("setvar THING_"+ctr).equals("null")) {
			thingTypes.add(xCon.ex("setvar THING_"+ctr));
			ctr++;
		}
		sSettings.object_titles = thingTypes.toArray(String[]::new);
	}

	private static void initGameScenes() {
		cClientLogic.scene = new gScene();
		cServerLogic.scene = new gScene();
		uiEditorMenus.previewScene = new gScene();
	}

	private static void initGameVars(String [] args) {
		cServerVars.instance().init(args);
		cClientVars.instance().init(args);
	}

	private static void init(String[] args) {
		eManager.init();
		gExecDoableFactory.instance().init();
		initGameObjects();
		initGameScenes();
		initGameVars(args);
		uiMenus.init();
	}

	public static void main(String[] args) {
		try {
			init(args);
			new eGameSessionClient().start();
		}
		catch (Exception err) {
			err.printStackTrace();
			System.exit(-1);
		}
	}
}
