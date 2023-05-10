public class xMain {
	public static eGameLogicShell shellLogic;
	public static String[] launchArgs;
	public static void main(String[] args) {
		try {
			launchArgs = args;
			shellLogic = new eGameLogicShell(args);
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
