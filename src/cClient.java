public class cClient {
    public static void processActionLoadClient(String actionload) {
        String[] actions = actionload.split("\\|");
        for(String action : actions) {

        }
    }

    public static void processCmd(String cmdload) {
        nSend.sendMap.put("netcmdrcv","");
        xCon.ex(cmdload);
    }
}
