import java.util.ArrayList;

public class xMain {
	private static void init(String[] args) {
		eManager.init();
		gExecDoableFactory.instance().init();
		// init thing types from def
		xCon.ex("exec config/thingsdef");
		int ctr = 0;
		ArrayList<String> thingTypes = new ArrayList<>();
		while(!xCon.ex("setvar THING_"+ctr).equals("null")) {
			thingTypes.add(xCon.ex("setvar THING_"+ctr));
			ctr++;
		}
		if(thingTypes.size() > 0)
			sSettings.object_titles = thingTypes.toArray(String[]::new);
		// end thing types
		cClientLogic.scene = new gScene();
		cServerLogic.scene = new gScene();
		uiEditorMenus.previewScene = new gScene();
		cServerVars.instance().init(args);
		cClientVars.instance().init(args);
		xCon.ex("exec config/autoexec");
		uiMenus.init();
		if(sSettings.show_mapmaker_ui) {
			sSettings.drawhitboxes = true;
			sSettings.drawmapmakergrid = true;
			cClientVars.instance().put("zoomlevel", "0.5");
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
