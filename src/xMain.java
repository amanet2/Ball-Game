public class xMain {
	public static void main(String[] args) {
		sLaunchArgs.setLaunchArguments(args);
//		eUtils.disableApplePressAndHold();
		oDisplay.instance().showFrame();
		uiInterface.addListeners();
		uiInterface.startNew();
	}
}