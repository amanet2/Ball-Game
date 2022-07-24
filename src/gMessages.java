import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class gMessages {
    static ArrayList<String> screenMessages = new ArrayList<>();
    static Queue<Long> expirs = new LinkedList<>();
    static boolean messageSend = false;
    static boolean enteringMessage = false;
    static String msgInProgress = "";
    static int fadetime = 10000;

    public static void addScreenMessage(String s) {
        screenMessages.add(s);
        expirs.add(gTime.gameTime + fadetime);
    }

    public static void checkMessages() {
        if(messageSend) {
            if(msgInProgress.toLowerCase().equals("thetime")) {
                addScreenMessage(xCon.instance().commands.get("thetime").doCommand("thetime"));
            }
            msgInProgress = "";
            messageSend = false;
        }
        //expired msgs
        if(expirs.size() > 0) {
            if(expirs.peek() != null && expirs.peek() < gTime.gameTime) {
                screenMessages.remove(0);
                expirs.remove();
            }
        }
    }
}
