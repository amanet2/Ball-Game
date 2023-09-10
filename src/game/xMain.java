package game;

public class xMain {
	public static eGameLogicShell shellLogic;
	public static String[] launchArgs;
	public static void main(String[] args) {
		try {
			launchArgs = args;
			shellLogic = new eGameLogicShell();
			new eGameSession(shellLogic, sSettings.rateShell);
		}
		catch (Exception err) {
			err.printStackTrace();
			System.exit(-1);
		}
	}
}
