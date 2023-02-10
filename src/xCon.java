import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

    private xCon() {
        linesToShowStart = 0;
        linesToShow = 24;
        cursorIndex = 0;
        commands = new HashMap<>();
        pressBinds = new HashMap<>();
        releaseBinds = new HashMap<>();
        previousCommands = new ArrayList<>();
        stringLines = new ArrayList<>();
        commandString = "";
        prevCommandIndex = -1;

        commands.put("activatemenu", new xCom() {
            public String doCommand(String fullCommand) {
                if(!uiInterface.inplay && !sSettings.show_mapmaker_ui) {
                    uiMenus.menuSelection[uiMenus.selectedMenu].items[
                            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem].doItem();
                }
                return "1";
            }
        });
        commands.put("addbot", new xCom() {
            public String doCommand(String fullCommand) {
                String[] botnameselection = sSettings.botnameSelection;
                String[] colorselection = sSettings.colorSelection;
                String botname = botnameselection[(int)(Math.random()*(botnameselection.length))];
                String botcolor = colorselection[(int)(Math.random()*(colorselection.length))];

                gPlayer p = new gPlayer("bot"+eManager.createBotId(), -6000,-6000,
                        Integer.parseInt(xCon.ex("cv_maxhp")),
                        eUtils.getPath(String.format("animations/player_%s/a03.png", botcolor)));
                cServerLogic.scene.getThingMap("THING_PLAYER").put(p.get("id"), p);
                cServerLogic.scene.getThingMap("THING_BOTPLAYER").put(p.get("id"), p);
                nVarsBot.update(p, gTime.gameTime);
                nServer.instance().masterStateMap.put(p.get("id"), new nStateBallGame());
                nServer.instance().masterStateMap.get(p.get("id")).put("color", botcolor);
                nServer.instance().masterStateMap.get(p.get("id")).put("name", botname);
                gScoreboard.addId(p.get("id"));
                nServer.instance().addExcludingNetCmd("server", "echo " + botname + " joined the game");
                xCon.ex("exec scripts/respawnnetplayer " + p.get("id"));
                return "spawned bot";
            }
        });
        commands.put("addcom", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcom can only be used by active server";
                if(eUtils.argsLength(fullCommand) < 2)
                    return "usage: addcom <command to execute>";
                String[] args = eUtils.parseScriptArgsServer(fullCommand);
                StringBuilder act = new StringBuilder("");
                for(int i = 1; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                nServer.instance().addNetCmd(actStr);
                return "server net com: " + actStr;
            }
        });
        commands.put("cl_addcom", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_CLIENT)
                    return "cl_addcom can only be used by active clients";
                if(eUtils.argsLength(fullCommand) < 2)
                    return "usage: cl_addcom <command to execute>";
                String[] args = eUtils.parseScriptArgsClient(fullCommand);
                StringBuilder act = new StringBuilder("");
                for(int i = 1; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                nClient.instance().addNetCmd(actStr);
                return "client net com: " + actStr;
            }
        });
        commands.put("addcomi", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcomi can only be used by the host";
                String[] args = fullCommand.split(" ");
                if(args.length < 3)
                    return "usage: addcomi <ignore id> <string>";
                for(int i = 1; i < args.length; i++) {
                    if(args[i].contains("#")) {
                        String[] toks = args[i].split("#");
                        for(int j = 0; j < toks.length; j++) {
                            if(!toks[j].startsWith("$"))
                                continue;
                            if(cServerVars.instance().contains(toks[j].substring(1)))
                                toks[j] = cServerVars.instance().get(toks[j].substring(1));
                            else if(sVars.get(toks[j]) != null)
                                toks[j] = sVars.get(toks[0]);
                        }
                        args[i] = toks[0] + "#" + toks[1];
                    }
                    else if(args[i].startsWith("$") && cServerVars.instance().contains(args[i].substring(1)))
                        args[i] = cServerVars.instance().get(args[i].substring(1));
                    else if(args[i].startsWith("$") && sVars.get(args[i]) != null)
                        args[i] = sVars.get(args[i]);
                }
                String ignoreId = args[1];
                StringBuilder act = new StringBuilder("");
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                nServer.instance().addExcludingNetCmd(ignoreId, actStr);
                return "server net com ignoring: " + actStr;
            }
        });
        commands.put("addcomx", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcomx can only be used by active server";
                String[] args = fullCommand.split(" ");
                if(args.length < 3)
                    return "usage: addcomx <exclusive id> <string>";
                for(int i = 1; i < args.length; i++) {
                    //parse the $ vars for placing prefabs
                    if(args[i].startsWith("$")) {
                        if(args[i].contains("#")) {
                            String[] toks = args[i].split("#");
                            if(cServerVars.instance().contains(toks[0].substring(1)))
                                toks[0] = cServerVars.instance().get(toks[0].substring(1));
                            if(cServerVars.instance().contains(toks[1].substring(1)))
                                toks[1] = cServerVars.instance().get(toks[1].substring(1));
                            args[i] = toks[0] + "#" + toks[1];
                        }
                        else if(cServerVars.instance().contains(args[i].substring(1)))
                            args[i] = cServerVars.instance().get(args[i].substring(1));
                        else if(sVars.get(args[i]) != null)
                            args[i] = sVars.get(args[i]);
                    }
                }
                String exlusiveId = args[1];
                StringBuilder act = new StringBuilder("");
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                nServer.instance().addNetCmd(exlusiveId, actStr);
                return "server net com exclusive: " + actStr;
            }
        });
        commands.put("addevent", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addevent can only be used by active server";
                if(eUtils.argsLength(fullCommand) < 3)
                    return "usage: addevent <time> <string to execute>";
                String[] args = eUtils.parseScriptArgsServer(fullCommand);
                StringBuilder act = new StringBuilder("");
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String timeToExec = args[1];
                String actStr = act.substring(1);
                cServerLogic.timedEvents.put(timeToExec,
                        new gTimeEvent() {
                            public void doCommand() {
                                xCon.ex(actStr);
                            }
                        }
                );
                return "added time event @" + timeToExec + ": " + actStr;
            }
        });
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
        commands.put("constr", new xComConStr());
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
        commands.put("foreach", new xComForEach());
        commands.put("foreachlong", new xComForEachLong());
        commands.put("foreachclient", new xComForEachClient());
        commands.put("foreachthing", new xComForEachThing());
        commands.put("getres", new xComGetRes());
        commands.put("cl_getres", new xComGetResClient());
        commands.put("gametimemillis", new xComGameTimeMillis());
        commands.put("getadd", new xComGetAdd());
        commands.put("getaddint", new xComGetAddInt());
        commands.put("getaddlong", new xComGetAddLong());
        commands.put("cl_getadd", new xComGetAddClient());
        commands.put("cl_getaddlong", new xComGetAddLongClient());
        commands.put("getsnap", new xComGetSnapshot());
        commands.put("getsub", new xComGetSub());
        commands.put("cl_getsub", new xComGetSubClient());
        commands.put("getnewitemid", new xComGetNewItemId());
        commands.put("getrand", new xComGetRandom());
        commands.put("getrandclid", new xComGetRandomClientId());
        commands.put("getrandthing", new xComGetRandomThing());
        commands.put("getwinnerid", new xComGetWinnerId());
        commands.put("givewin", new xComGiveWin());
        commands.put("givepoint", new xComGivePoint());
        commands.put("gobackui", new xComGoBackUI());
        commands.put("joingame", new xComJoingame());
        commands.put("load", new xComLoad());
        commands.put("mouseleft", new xComMouseLeft());
        commands.put("newgame", new xComNewgame());
        commands.put("newgamerandom", new xComNewgameRandom());
        commands.put("pause", new xComPause());
        commands.put("playsound", new xComPlaySound());
        commands.put("putblock", new xComPutBlock());
        commands.put("putitem", new xComPutItem());
        commands.put("quit", new xComQuit());
        commands.put("say", new xComSay());
        commands.put("selectdown", new xComSelectDown());
        commands.put("selectleft", new xComSelectLeft());
        commands.put("selectright", new xComSelectRight());
        commands.put("selectup", new xComSelectUp());
        commands.put("setcam", new xComSetCamera());
        commands.put("setcamcoords", new xComSetCamCoords());
        commands.put("setcammovs", new xComSetCamMovs());
        commands.put("setplayercoords", new xComSetPlayerCoords());
        commands.put("setthing", new xComSetThing());
        commands.put("setnstate", new xComSetState());
        commands.put("cl_setthing", new xComSetThingClient());
        commands.put("setvar", new xComSetVar());
        commands.put("spawnplayer", new xComSpawnPlayer());
        commands.put("startserver", new xComStartServer());
        commands.put("svarlist", new xComSVarlist());
        commands.put("testres", new xComTestRes());
        commands.put("testreslte", new xComTestResLte());
        commands.put("cl_testreslte", new xComTestResLteClient());
        commands.put("testreslteint", new xComTestResLteInt());
        commands.put("cl_testres", new xComTestResClient());
        commands.put("testresn", new xComTestResN());
        commands.put("cl_testresn", new xComTestResNClient());
        commands.put("unbind", new xComUnbind());
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
        commands.put("cl_setplayercoords", new xComSetPlayerCoordsClient());
        commands.put("cl_setvar", new xComSetVarClient());
        commands.put("cl_spawnanimation", new xComSpawnAnimationClient());
        commands.put("cl_spawnpopup", new xComSpawnPopupClient());
        commands.put("cl_spawnplayer", new xComSpawnPlayerClient());
    }

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

    public void saveLog(String s) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(s), StandardCharsets.UTF_8))) {
            for(String line : stringLines) {
                writer.write(line+"\n");
            }
        } catch (IOException e) {
            eLogging.logException(e);
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
                while (stringLines.size() > 1024) {
                    stringLines.remove(0);
                }
                while (previousCommands.size() > 32) {
                    previousCommands.remove(0);
                }
                return result;
            }
            else {
                return String.format("No result: %s", command);
            }
        }
        return "";
    }
}
