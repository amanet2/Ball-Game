public class xMain {
	public static void main(String[] args) {
		sVars.loadFromFile(sSettings.CONFIG_FILE_LOCATION);
		sLaunchArgs.setLaunchArguments(args);
		eUtils.disableApplePressAndHold();
		uiInterface.init();
	}
}