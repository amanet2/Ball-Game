import java.util.ArrayList;
import java.util.HashMap;

public class eConsoleLogicShell extends eConsoleLogicAdapter {
//    ArrayList<String> previousCommands;
//    ArrayList<String> stringLines;
//    int prevCommandIndex;
//    String commandString;
//    int linesToShowStart;
//    int linesToShow;
//    int cursorIndex;

    public void doCommand(String fullCommand) {
        System.out.println(fullCommand);
        if(!fullCommand.isEmpty()) {
            String[] args = fullCommand.trim().split(" ");
            for(int i = 0; i < args.length; i++) {
                if(args[i].startsWith("$") && xMain.shellLogic.serverVars.contains(args[i].substring(1)))
                    args[i] = xMain.shellLogic.serverVars.get(args[i].substring(1));
                else if(args[i].startsWith("$") && xMain.shellLogic.clientVars.contains(args[i].substring(1)))
                    args[i] = xMain.shellLogic.clientVars.get(args[i].substring(1));
            }
            String command = args[0];
            if(command.startsWith("-"))
                command = command.substring(1);
            eConsoleCommand cd = commands.get(command);
            if (cd != null) {
                StringBuilder realcom = new StringBuilder();
                for(String arg : args) {
                    realcom.append(" ").append(arg);
                }
                String comstring = realcom.substring(1);
                if(comstring.charAt(0) == '-')
                    cd.undoCommand(comstring);
                else
                    cd.doCommand(comstring);
//                while (stringLines.size() > 1024) {
//                    stringLines.remove(0);
//                }
//                while (previousCommands.size() > 32) {
//                    previousCommands.remove(0);
//                }
            }
        }
    }

    public eConsoleLogicShell() {
        // SHELL commands should be able to handle playing as host and as a client
        // the SERVER and CLIENT console commands should be called from here
        // they will interact with network, scenes, etc.
        commands = new HashMap<>();
        commands.put("activatemenu", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                activateMenu();
            }
        });
        commands.put("addbot", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                addbot();
            }
        });
        commands.put("addcom", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                addcom();
            }
        });
        commands.put("addcomi", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                addcomi();
            }
        });
        commands.put("addcomx", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                addcomx();
            }
        });
        commands.put("bind", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                bind();
            }
            @Override
            public void undoCommand(String command) {
                unbind();
            }
        });
        commands.put("changemap", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                changemap();
            }
        });
        commands.put("changemaprandom", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                changemaprandom();
            }
        });
        commands.put("chat", new eConsoleCommand(){
            @Override
            public void doCommand(String command) {
                chat();
            }
        });
    }

    @Override
    public void activateMenu() {
        System.out.println("ACTIVATE MENU");
//        if(!sSettings.inplay && !sSettings.show_mapmaker_ui)
//            uiMenus.menuSelection[uiMenus.selectedMenu].items[uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem].doItem();
    }
}
