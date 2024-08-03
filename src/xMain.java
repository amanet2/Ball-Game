public class xMain {
	public static eGameLogicShell shellLogic;
	public static String[] launchArgs;

	public static void main(String[] args) {
		launchArgs = args;
		shellLogic = new eGameLogicShell();
		new eGameSession(shellLogic, sSettings.rateShell);
	}
}
