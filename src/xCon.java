import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.ArrayList;

public class xCon {
    private static xCon instance = null;
    static int maxlinelength = 128;
    HashMap<String, xCom> commands;
    HashMap<Integer, String> releaseBinds;
    HashMap<Integer, String> pressBinds;
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
        return (int)((double)sSettings.width/new Font(dFonts.fontnameconsole, Font.PLAIN,
            dFonts.fontsize*sSettings.height/sSettings.gamescale/2).getStringBounds("_",
                dFonts.fontrendercontext).getWidth());
    }

    public void debug(String s) {
        if(cClientLogic.debug) {
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
        previousCommands = new ArrayList<>(); //TODO: turn into queue to avoid storing too many
        stringLines = new ArrayList<>();    //TODO: turn into queue to avoid storing too many
        commandString = "";
        prevCommandIndex = -1;

        commands = new HashMap<>();
        commands.put("activateui", new xComActivateUI());
        commands.put("addbot", new xComAddBot());
        commands.put("addcom", new xComAddCommand());
        commands.put("addcomi", new xComAddCommandIgnore());
        commands.put("addcomx", new xComAddCommandExclusive());
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
        commands.put("deleteitem", new xComDeleteItem());
        commands.put("deleteplayer", new xComDeletePlayer());
        commands.put("deleteprefab", new xComDeletePrefab());
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
        commands.put("getnargs", new xComGetServerArgs());
        commands.put("getres", new xComGetRes());
        commands.put("getrand", new xComGetRandom());
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
        commands.put("putitem", new xComPutItem());
        commands.put("quit", new xComQuit());
        commands.put("respawnnetplayer", new xComRespawnNetPlayer());
        commands.put("say", new xComSay());
        commands.put("selectdown", new xComSelectDown());
        commands.put("selectleft", new xComSelectLeft());
        commands.put("selectright", new xComSelectRight());
        commands.put("selectup", new xComSelectUp());
        commands.put("setcam", new xComSetCamera());
        commands.put("setthing", new xComSetThing());
        commands.put("setnargs", new xComSetServerArgs());
        commands.put("cl_setthing", new xComSetThingClient());
        commands.put("setvar", new xComSetVar());
        commands.put("showscore", new xComShowScore());
        commands.put("soundlist", new xComSoundlist());
        commands.put("spawnplayer", new xComSpawnPlayer());
        commands.put("spawnpointgiver", new xComSpawnPointgiver());
        commands.put("startserver", new xComStartServer());
        commands.put("svarlist", new xComSVarlist());
        commands.put("testres", new xComTestRes());
        commands.put("testresn", new xComTestResN());
        commands.put("thetime", new xComThetime());
        commands.put("unbind", new xComUnbind());
        commands.put("zoom", new xComZoom());
        commands.put("cl_addcom", new xComAddCommandClient());
        commands.put("cl_clearthingmap", new xComClearThingMapClient());
        commands.put("cl_clearthingmappreview", new xComClearThingMapPreview());
        commands.put("cl_deleteblock", new xComDeleteBlockClient());
        commands.put("cl_deleteitem", new xComDeleteItemClient());
        commands.put("cl_deleteplayer", new xComDeletePlayerClient());
        commands.put("cl_deleteprefab", new xComDeletePrefabClient());
        commands.put("cl_exec", new xComExecClient());
        commands.put("cl_execpreview", new xComExecClientPreview());
        commands.put("cl_fireweapon", new xComFireWeaponClient());
        commands.put("cl_load", new xComLoadClient());
        commands.put("cl_putblock", new xComPutBlockClient());
        commands.put("cl_putblockpreview", new xComPutBlockPreview());
        commands.put("cl_putitem", new xComPutItemClient());
        commands.put("cl_setplayercoords", new xComSetPlayerCoords());
        commands.put("cl_setvar", new xComSetVarClient());
        commands.put("cl_spawnanimation", new xComSpawnAnimationClient());
        commands.put("cl_spawnpopup", new xComSpawnPopupClient());
        commands.put("cl_spawnplayer", new xComSpawnPlayerClient());
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
            for(int i = 0; i < args.length; i++) {
                if(args[i].startsWith("$") && cServerVars.instance().contains(args[i].substring(1))) {
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                }
            }
            if(args.length > 0) {
                String configval = args[0];
                if(cServerVars.instance().contains(configval)) {
                    if(args.length > 1) {
                        cServerVars.instance().put(configval, args[1]);
                    }
                    return cServerVars.instance().get(configval);
                }
                else if(cClientVars.instance().contains(configval)) {
                    if(args.length > 1) {
                        cClientVars.instance().put(configval, args[1]);
                    }
                    return cClientVars.instance().get(configval);
                }
            }
            String command = args[0];
            if(command.startsWith("-"))
                command = command.substring(1);
            xCom cp = commands.get(command);
            if (cp != null) {
                StringBuilder realcom = new StringBuilder();
                for(int i = 0; i < args.length; i++) {
                    realcom.append(" ").append(args[i]);
                }
                String comstring = realcom.substring(1);
                stringLines.add(String.format("console:~$ %s", comstring));
                String result = comstring.charAt(0) == '-' ? cp.undoCommand(comstring) : cp.doCommand(comstring);
                if (result.length() > 0)
                    stringLines.add(result);
                linesToShowStart = Math.max(0, stringLines.size() - linesToShow);
                return result;
            }
            else {
                return String.format("No result: %s", command);
            }
        }
        return "";
    }
}
