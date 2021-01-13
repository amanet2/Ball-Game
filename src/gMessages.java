import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class gMessages {
    static ArrayList<String> screenMessages = new ArrayList<>();
    static Queue<Long> expirs = new LinkedList<>();
    static boolean messageSend = false;
    static boolean enteringMessage = false;
    static String msgInProgress = "";
    static String networkMessage = "";
    static boolean optionSet = false;
    static String enteringOptionText = "";
    static Queue<String> sayMessages = new LinkedList<>();

    public static void addScreenMessage(String s) {
        screenMessages.add(s);
        expirs.add(System.currentTimeMillis() + sVars.getInt("msgfadetime"));
    }

    public static void checkMessages() {
        //reset msg/sfx/cmd
        if(sSettings.net_server && cScripts.allClientsReceivedMessage("netmsgrcv")
                && gMessages.networkMessage.length() > 0) {
            gMessages.networkMessage = "";
            for(String id : nServer.clientArgsMap.keySet()) {
                nServer.clientArgsMap.get(id).remove("netmsgrcv");
            }
        }
        if(sSettings.net_server && cScripts.allClientsReceivedMessage("netsfxrcv")
                && cVars.get("sendsound").length() > 0) {
            cVars.put("sendsound", "");
            for(String id : nServer.clientArgsMap.keySet()) {
                nServer.clientArgsMap.get(id).remove("netsfxrcv");
            }
        }
        if(sSettings.net_server && cScripts.allClientsReceivedMessage("netcmdrcv")
                && cVars.get("sendcmd").length() > 0) {
            cVars.put("sendcmd", "");
            for(String id : nServer.clientArgsMap.keySet()) {
                nServer.clientArgsMap.get(id).remove("netcmdrcv");
            }
        }

        if(sayMessages.size() > 0 && networkMessage.length() < 1) {
            String plr = sVars.get("playername");
            String msg = String.format("%s: %s", plr, sayMessages.remove());
            if (sSettings.net_server)
                msg = msg.replaceFirst(plr + ": ", "");
            if (!optionSet && (!cScripts.isNetworkGame() || sSettings.net_server))
                addScreenMessage(msg);
            networkMessage = msg;
            if(sSettings.net_server) {
                //check for sound
                cScripts.checkMsgSpecialFunction(msg);
            }
        }
        if(messageSend) {
            if(optionSet) {
                cScripts.processOptionText(enteringOptionText, msgInProgress);
            }
            else {
                if(msgInProgress.toLowerCase().equals("thetime")) {
                    addScreenMessage(xCon.instance().commands.get("thetime").doCommand("thetime"));
                }
            }
            msgInProgress = "";
            messageSend = false;
            optionSet = false;
            enteringOptionText = "";
        }
        //expired msgs
        if(expirs.size() > 0) {
            if(expirs.peek() != null && expirs.peek() < System.currentTimeMillis()) {
                screenMessages.remove(0);
                expirs.remove();
            }
        }
    }
}
