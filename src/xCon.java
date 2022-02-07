import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.ArrayList;

public class xCon {
    private static xCon instance = null;
    HashMap<String, xCom> commands;
    HashMap<Integer, String> releaseBinds;
    HashMap<Integer, String> pressBinds;
    private ArrayList<String> visibleCommands;
    ArrayList<String> previousCommands;
    ArrayList<String> stringLines;
    int prevCommandIndex;
    String commandString;
    int linesToShowStart;
    int linesToShow;
    int cursorIndex;

    public static xCon instance() {
        if(instance == null)
            instance = new xCon();
        return instance;
    }

    public static String ex(String s) {
        String[] commandTokens = s.split(";");
        StringBuilder result = new StringBuilder();
        for(String com : commandTokens) {
            result.append(instance().doCommand(com)).append(";");
        }
        String resultString = result.toString();
        return resultString.substring(0,resultString.length()-1);
    }

    public int getInt(String s) {
        return Integer.parseInt(doCommand(s));
    }

    public static int charlimit() {
        return (int)((double)sSettings.width/new Font(sVars.get("fontnameconsole"), sVars.getInt("fontmode"),
            sVars.getInt("fontsize")*sSettings.height/cVars.getInt("gamescale")/2).getStringBounds("_",
                dFonts.fontrendercontext).getWidth());
    }

    public void debug(String s) {
        if(sVars.isOne("debug")) {
            log(s);
            System.out.println(s);
        }
    }

    public void log(String s) {
        if(s.length() > charlimit()) {
            stringLines.add(s.substring(0, charlimit()));
            for(int i = charlimit(); i < s.length();
                i+= charlimit()) {
                int lim = Math.min(s.length(), i+ charlimit());
                stringLines.add(s.substring(i,lim));
            }
        }
        else
            stringLines.add(s);
        linesToShowStart = Math.max(0, stringLines.size() - linesToShow);
    }

    private xCon() {
        linesToShowStart = 0;
        linesToShow = 24;
        cursorIndex = 0;
        pressBinds = new HashMap<>();
        releaseBinds = new HashMap<>();
        previousCommands = new ArrayList<>();
        stringLines = new ArrayList<>();
        commandString = "";
        prevCommandIndex = -1;

        visibleCommands = new ArrayList<>();
        visibleCommands.add("addbot");
        visibleCommands.add("banid");
        visibleCommands.add("bind");
        visibleCommands.add("changemap");
        visibleCommands.add("changemaprandom");
        visibleCommands.add("exec");
        visibleCommands.add("joingame");
        visibleCommands.add("load");
        visibleCommands.add("newgame");
        visibleCommands.add("newgamerandom");
        visibleCommands.add("disconnect");
        visibleCommands.add("cl_load");
        visibleCommands.add("cl_exec");
        visibleCommands.add("exportasprefab");
        visibleCommands.add("e_openfile");
        visibleCommands.add("e_saveas");

        commands = new HashMap<>();
        commands.put("activateui", new xComActivateUI());
        commands.put("addbot", new xComAddBot());
        commands.put("attack", new xComAttack());
        commands.put("banid", new xComBanId());
        commands.put("bind", new xComBind());
        commands.put("bindlist", new xComBindList());
        commands.put("changemap", new xComChangeMap());
        commands.put("changemaprandom", new xComChangeMapRandom());
        commands.put("chat", new xComChat());
        commands.put("clearthingmap", new xComClearThingMap());
        commands.put("clientlist", new xComClientlist());
        commands.put("commandlist", new xComCommandlist());
        commands.put("console", new xComConsole());
        commands.put("cvarlist", new xComCVarList());
        commands.put("damageplayer", new xComDamagePlayer());
        commands.put("deleteblock", new xComDeleteBlock());
        commands.put("deletecollision", new xComDeleteCollision());
        commands.put("deleteitem", new xComDeleteItem());
        commands.put("deleteplayer", new xComDeletePlayer());
        commands.put("disconnect", new xComDisconnect());
        commands.put("dobotbehavior", new xComDoBotBehavior());
        commands.put("exec", new xComExec());
        commands.put("exportasprefab", new xComExportAsPrefab());
        commands.put("e_changeplayername", new xComEditorChangePlayerName());
        commands.put("e_changejoinip", new xComEditorChangeJoinIP());
        commands.put("e_changejoinport", new xComEditorChangeJoinPort());
        commands.put("e_delthing", new xComEditorDelThing());
        commands.put("e_rotthing", new xComEditorRotNewThing());
        commands.put("e_newmap", new xComEditorNewMap());
        commands.put("e_openfile", new xComEditorOpenFile());
        commands.put("e_openprefab", new xComEditorOpenPrefab());
        commands.put("e_saveas", new xComEditorSaveAs());
        commands.put("e_showlossalert", new xComEditorShowLossAlert());
        commands.put("echo", new xComEcho());
        commands.put("fireweapon", new xComFireWeapon());
        commands.put("givepoint", new xComGivePoint());
        commands.put("giveweapon", new xComGiveWeapon());
        commands.put("gobackui", new xComGoBackUI());
        commands.put("joingame", new xComJoingame());
        commands.put("load", new xComLoad());
        commands.put("mouseleft", new xComMouseLeft());
        commands.put("newgame", new xComNewgame());
        commands.put("newgamerandom", new xComNewgameRandom());
        commands.put("pause", new xComPause());
        commands.put("playerdown", new xComPlayerDown());
        commands.put("playerleft", new xComPlayerLeft());
        commands.put("playerright", new xComPlayerRight());
        commands.put("playerup", new xComPlayerUp());
        commands.put("playsound", new xComPlaySound());
        commands.put("putblock", new xComPutBlock());
        commands.put("putcollision", new xComPutCollision());
        commands.put("putflare", new xComPutFlare());
        commands.put("putitem", new xComPutItem());
        commands.put("quit", new xComQuit());
        commands.put("respawnnetplayer", new xComRespawnNetPlayer());
        commands.put("say", new xComSay());
        commands.put("selectdown", new xComSelectDown());
        commands.put("selectleft", new xComSelectLeft());
        commands.put("selectright", new xComSelectRight());
        commands.put("selectup", new xComSelectUp());
        commands.put("showscore", new xComShowScore());
        commands.put("soundlist", new xComSoundlist());
        commands.put("spawnplayer", new xComSpawnPlayer());
        commands.put("startserver", new xComStartServer());
        commands.put("svarlist", new xComSVarlist());
        commands.put("thetime", new xComThetime());
        commands.put("unbind", new xComUnbind());
        commands.put("userplayer", new xComUserPlayer());
        commands.put("zoom", new xComZoom());
        commands.put("cl_clearthingmap", new xComClearThingMapClient());
        commands.put("cl_deleteblock", new xComDeleteBlockClient());
        commands.put("cl_deletecollision", new xComDeleteCollisionClient());
        commands.put("cl_deleteitem", new xComDeleteItemClient());
        commands.put("cl_deleteplayer", new xComDeletePlayerClient());
        commands.put("cl_exec", new xComExecClient());
        commands.put("cl_execpreview", new xComExecClientPreview());
        commands.put("cl_fireweapon", new xComFireWeaponClient());
        commands.put("cl_load", new xComLoadClient());
        commands.put("cl_putblock", new xComPutBlockClient());
        commands.put("cl_putblockpreview", new xComPutBlockPreview());
        commands.put("cl_putcollision", new xComPutCollisionClient());
        commands.put("cl_putflare", new xComPutFlareClient());
        commands.put("cl_putitem", new xComPutItemClient());
        commands.put("cl_sendcmd", new xComSendCmdClient());
        commands.put("cl_spawnanimation", new xComSpawnAnimationClient());
        commands.put("cl_spawnpopup", new xComSpawnPopupClient());
        commands.put("cl_spawnplayer", new xComSpawnPlayerClient());
        commands.put("cl_status", new xComStatusClient());
        commands.put("sv_sendcmd", new xComSendCmdServer());
        commands.put("sv_status", new xComStatusServer());
    }

    public void saveLog(String s) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(s), StandardCharsets.UTF_8))) {
            for(String line : stringLines) {
                writer.write(line+"\n");
            }
        } catch (IOException e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public static Integer getKeyCodeForComm(String comm) {
        if(comm.length() > 0) {
            if(comm.charAt(0) == '-') {
                for(Integer j : xCon.instance().releaseBinds.keySet()) {
                    if(xCon.instance().releaseBinds.get(j).equals(comm))
                        return j;
                }
            }
            for(Integer j : xCon.instance().pressBinds.keySet()) {
                if(xCon.instance().pressBinds.get(j).equals(comm))
                    return j;
            }
        }
        return -1;
    }

    public String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
            String[] args = fullCommand.trim().split(" ");
            if(args.length > 0) {
                String configval = args[0];
                if(sVars.contains(configval)) {
//                    System.out.println("CONSOLE PARSING SVAR: " + configval);
                    //if we're setting instead of getting
                    if(args.length > 1) {
                        //check for valid input here
                        if(sVars.checkVal(configval, args[1]))
                            sVars.put(configval, args[1]);
                    }
                    return sVars.get(configval);
                }
                else if(configval.substring(0,3).equals("cv_") && cVars.contains(configval.substring(3))) {
//                    System.out.println("CONSOLE PARSING CVAR: " + configval);
                    //if we're setting instead of getting
                    if(args.length > 1) {
                        String val = args[1];
                        //check if our "value" is a reference to svar or cvar
                        if(sVars.contains(val))
                            val = sVars.get(val);
                        if(val.length() > 3 && val.substring(0,3).equals("cv_") && cVars.contains(val.substring(3)))
                            val = cVars.get(val.substring(3));
                        //check for valid input here
                        if(cVars.checkVal(configval.substring(3), val))
                            cVars.put(configval.substring(3), val);
                    }
                    return cVars.get(configval.substring(3));
                }
            }
            String command = fullCommand.split(" ")[0];
            command = fullCommand.charAt(0) == '-' || fullCommand.charAt(0) == '+'
                ? command.substring(1) : command;
            xCom cp = commands.get(command);
            if (cp != null) {
//                if (undoableCommands.contains(fullCommand.split(" ")[0])) {
//                    uiEditorMenus.undoStateStack.push(uiEditorMenus.getEditorState());
//                    uiEditorMenus.redoStateStack.clear();
//                }
                if(!visibleCommands.contains(command)) {
                    if (fullCommand.charAt(0) == '-')
                        return cp.undoCommand(fullCommand);
                    else
                        return cp.doCommand(fullCommand);
                }
                else {
                    stringLines.add(String.format("console:~$ %s", fullCommand));
                    String result = fullCommand.charAt(0) == '-' ? cp.undoCommand(fullCommand)
                        : cp.doCommand(fullCommand);
                    if (result.length() > 0)
                        stringLines.add(result);
                    linesToShowStart = Math.max(0, stringLines.size() - linesToShow);
                    return result;
                }
            }
            else {
                return String.format("No result: %s", command);
            }
        }
        return "";
    }
}
