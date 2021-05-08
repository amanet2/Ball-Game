import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class gMessages {
    static ArrayList<String> screenMessages = new ArrayList<>();
    static Queue<Long> expirs = new LinkedList<>();
    static boolean messageSend = false;
    static boolean enteringMessage = false;
    static String msgInProgress = "";
    static boolean optionSet = false;
    static String enteringOptionText = "";

    public static void addScreenMessage(String s) {
        screenMessages.add(s);
        expirs.add(System.currentTimeMillis() + sVars.getInt("msgfadetime"));
    }

    public static void checkMessages() {
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
