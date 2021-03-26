import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class xCon {
    private static xCon instance = null;
    static HashMap<String, xCom> commands;
    HashMap<Integer, String> releaseBinds;
    HashMap<Integer, String> pressBinds;
    static ArrayList<String> visibleCommands;
    ArrayList<String> previousCommands;
    static ArrayList<String> undoableCommands;
    static ArrayList<String> stringLines;
    int prevCommandIndex;
    String commandString;
    static int linesToShowStart;
    static int linesToShow;
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
            result.append(doCommand(com)).append(";");
        }
        String resultString = result.toString();
        return resultString.substring(0,resultString.length()-1);
    }

    public static String[] ex(String[] s) {
        String[] r = new String[s.length];
        r[0] = "No Result: "+ Arrays.toString(s);
        for(int i = 0; i < s.length; i++) {
            r[i] = ex(s[i]);
        }
        return r;
    }

    public static int getInt(String s) {
        System.out.println("CONSOLE RETURNING INT: " + s);
        return Integer.parseInt(doCommand(s));
    }

    public static boolean isOne(String s) {
        return getInt(s) == 1;
    }

    public static long getLong(String s) {
        System.out.println("CONSOLE RETURNING LONG: " + s);
        return Long.parseLong(doCommand(s));
    }

    public static int charlimit() {
        return (int)((double)sSettings.width/new Font(sVars.get("fontnameconsole"), sVars.getInt("fontmode"),
            sVars.getInt("fontsize")*sSettings.height/cVars.getInt("gamescale")/2).getStringBounds("_",
                dFonts.fontrendercontext).getWidth());
    }

    public void debug(String s) {
        if(sVars.isOne("debug"))
            log(s);
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
        visibleCommands.add("bind");
        visibleCommands.add("exec");
        visibleCommands.add("joingame");
        visibleCommands.add("load");
        visibleCommands.add("newgame");
        visibleCommands.add("newgamerandom");

        undoableCommands = new ArrayList<>();
        undoableCommands.add("e_copytile");
        undoableCommands.add("e_newflare");
        undoableCommands.add("e_newprop");
        undoableCommands.add("e_newtile");
        undoableCommands.add("e_newtilequick");
        undoableCommands.add("e_nextthing");
        undoableCommands.add("e_pastetile");
        undoableCommands.add("putflare");
        undoableCommands.add("putprop");
        undoableCommands.add("puttile");
        undoableCommands.add("putblock");
        undoableCommands.add("e_selectflare");
        undoableCommands.add("e_selectprop");
        undoableCommands.add("e_selecttile");
        undoableCommands.add("e_setselectedflare");
        undoableCommands.add("e_setselectedprop");
        undoableCommands.add("e_setselectedtile");
        undoableCommands.add("e_tiledown");
        undoableCommands.add("e_tileup");

        commands = new HashMap<>();
        commands.put("activateui", new xComActivateUI());
        commands.put("addbot", new xComAddBot());
        commands.put("attack", new xComAttack());
        commands.put("banid", new xComBanId());
        commands.put("bind", new xComBind());
        commands.put("bindlist", new xComBindList());
        commands.put("centercamera", new xComCentercamera());
        commands.put("changemap", new xComChangeMap());
        commands.put("changemaprandom", new xComChangeMapRandom());
        commands.put("chat", new xComChat());
        commands.put("clearbots", new xComClearBots());
        commands.put("clearthingmap", new xComClearThingMap());
        commands.put("clientlist", new xComClientlist());
        commands.put("clientplayer", new xComClientPlayer());
        commands.put("clientnetargs", new xComClientNetArgs());
        commands.put("commandlist", new xComCommandlist());
        commands.put("console", new xComConsole());
        commands.put("createserverplayer", new xComCreateServerPlayer());
        commands.put("createuserplayer", new xComCreateUserPlayer());
        commands.put("crouch", new xComCrouch());
        commands.put("cvarlist", new xComCVarList());
        commands.put("damageplayer", new xComDamagePlayer());
        commands.put("deleteblock", new xComDeleteBlock());
        commands.put("disconnect", new xComDisconnect());
        commands.put("dobotbehavior", new xComDoBotBehavior());
        commands.put("dropflagred", new xComDropFlagRed());
        commands.put("dropweapon", new xComDropWeapon());
        commands.put("dumpthingmap", new xComDumpThingMap());
        commands.put("exec", new xComExec());
        commands.put("exportasprefab", new xComExportAsPrefab());
        commands.put("e_copytile", new xComEditorCopyTile());
        commands.put("e_delthing", new xComEditorDelThing());
        commands.put("e_newflare", new xComEditorSetNewFlareDims());
        commands.put("e_newprop", new xComEditorSetNewPropDims());
        commands.put("e_newtile", new xComEditorSetNewTileDims());
        commands.put("e_newtilequick", new xComEditorSetNewTileQuickDims());
        commands.put("e_nextthing", new xComEditorNextThing());
        commands.put("e_openfile", new xComEditorOpenFile());
        commands.put("e_pastetile", new xComEditorPasteTile());
        commands.put("e_save", new xComEditorSave());
        commands.put("e_saveas", new xComEditorSaveAs());
        commands.put("e_selectprop", new xComEditorSelectProp());
        commands.put("e_selecttile", new xComEditorSelectTile());
        commands.put("e_selectflare", new xComEditorSelectFlare());
        commands.put("e_setselectedflare", new xComEditorSetSelectedFlareDims());
        commands.put("e_setselectedprop", new xComEditorSetSelectedPropDims());
        commands.put("e_setselectedtile", new xComEditorSetSelectedTileDims());
        commands.put("e_setsvars", new xComEditorSetSVars());
        commands.put("e_showcontrols", new xComEditorShowControls());
        commands.put("e_showflares", new xComEditorShowFlares());
        commands.put("e_showlossalert", new xComEditorShowLossAlert());
        commands.put("e_showprops", new xComEditorShowProps());
        commands.put("e_showexecs", new xComEditorShowExecs());
        commands.put("e_showtiles", new xComEditorShowTiles());
        commands.put("e_tiledown", new xComEditorTileDown());
        commands.put("e_tileup", new xComEditorTileUp());
        commands.put("e_undo", new xComEditorUndo());
        commands.put("echo", new xComEcho());
        commands.put("fireweapon", new xComFireWeapon());
        commands.put("flashlight", new xComFlashlight());
        commands.put("givepoint", new xComGivePoint());
        commands.put("giveweapon", new xComGiveWeapon());
        commands.put("gobackui", new xComGoBackUI());
        commands.put("joingame", new xComJoingame());
        commands.put("joingamespec", new xComJoingameSpec());
        commands.put("jump", new xComJump());
        commands.put("load", new xComLoad());
        commands.put("maplist", new xComMaplist());
        commands.put("mouseleft", new xComMouseLeft());
        commands.put("mouseright", new xComMouseRight());
        commands.put("newgame", new xComNewgame());
        commands.put("newgamerandom", new xComNewgameRandom());
        commands.put("pause", new xComPause());
        commands.put("playerdown", new xComPlayerDown());
        commands.put("playerleft", new xComPlayerLeft());
        commands.put("playerright", new xComPlayerRight());
        commands.put("playerup", new xComPlayerUp());
        commands.put("playsound", new xComPlaySound());
        commands.put("putblock", new xComPutBlock());
        commands.put("putflare", new xComPutFlare());
        commands.put("putprop", new xComPutProp());
        commands.put("puttile", new xComPutTile());
        commands.put("quit", new xComQuit());
        commands.put("kickbot", new xComKickBot());
        commands.put("removeplayer", new xComRemovePlayer());
        commands.put("respawn", new xComRespawn());
        commands.put("respawnclientbotplayer", new xComRespawnClientBotPlayer());
        commands.put("respawnplayer", new xComRespawnPlayer());
        commands.put("say", new xComSay());
        commands.put("selectdown", new xComSelectDown());
        commands.put("selectleft", new xComSelectLeft());
        commands.put("selectright", new xComSelectRight());
        commands.put("selectup", new xComSelectUp());
        commands.put("sendcmd", new xComSendCmd());
        commands.put("set", new xComSet());
        commands.put("showscore", new xComShowScore());
        commands.put("soundlist", new xComSoundlist());
        commands.put("spawnanimation", new xComSpawnAnimation());
        commands.put("spawnpopup", new xComSpawnPopup());
        commands.put("sspeed", new xComSuperSpeed());
        commands.put("status", new xComStatus());
        commands.put("svarlist", new xComSVarlist());
        commands.put("thetime", new xComThetime());
        commands.put("unbind", new xComUnbind());
        commands.put("userplayer", new xComUserPlayer());
        commands.put("zoom", new xComZoom());
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
                    if(xCon.instance().releaseBinds.get(j).equals(comm)) {
                        return j;
                    }
                }
            }
            for(Integer j : xCon.instance().pressBinds.keySet()) {
                if(xCon.instance().pressBinds.get(j).equals(comm)) {
                    return j;
                }
            }
        }
        return -1;
    }

    public static String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
            String[] args = fullCommand.trim().split(" ");
            if(args.length > 1) {

            }
            //
            // --- NEW ABOVE, OLD BELOW
            //
            if(args.length > 0) {
                String configval = args[0];
                if(sVars.contains(configval)) {
                    System.out.println("CONSOLE PARSING SVAR: " + configval);
                    //if we're setting instead of getting
                    if(args.length > 1) {
                        //check for valid input here
                        if(sVars.checkVal(configval, args[1]))
                            sVars.put(configval, args[1]);
                    }
                    return sVars.get(configval);
                }
                else if(configval.substring(0,3).equals("cv_") && cVars.contains(configval.substring(3))) {
                    System.out.println("CONSOLE PARSING CVAR: " + configval);
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
                String[] otoks = configval.split("\\.");
                if(otoks.length > 2) {
                    System.out.println("CONSOLE PARSING <THING>.<ID>.<VAR>: " + otoks[0] + "." + otoks[1] + "." + otoks[2]);
                    String type = otoks[0];
                    int tag = Integer.parseInt(otoks[1]);
                    String var = otoks[2];
                    if(eManager.currentMap.scene.objectLists.get(type).size() > tag) {
                        gThing g = (gThing) eManager.currentMap.scene.objectLists.get(type).get(tag);
                        if(args.length > 1) {
                            //process the arg by checking if svar or cvar can be subbed in
                            String val = args[1];
                            if(sVars.contains(val))
                                val = sVars.get(val);
                            if(val.length() > 3 && val.substring(0,3).equals("cv_")
                                    && cVars.contains(val.substring(0,3)))
                                val = cVars.get(val.substring(3));
                            g.put(var, val);
                        }
                        else if(g.canDo(var)) {
                            g.doDoable(var);
                        }
                        return g.get(var);
                    }
                }
                else if(otoks.length > 1) {
                    System.out.println("CONSOLE PARSING <THING>.<ID>: " + otoks[0] + "." + otoks[1]);
                    String type = otoks[0];
                    int tag = Integer.parseInt(otoks[1]);
                    if(eManager.currentMap.scene.objectLists.get(type).size() > tag) {
                        gThing g = (gThing) eManager.currentMap.scene.objectLists.get(type).get(tag);
                        return g.vars().toString();
                    }
                }
            }
            boolean isHidden = fullCommand.split(" ")[0].toLowerCase().equals("hidden");
            fullCommand = isHidden ? fullCommand.substring(fullCommand.indexOf(" ") + 1) : fullCommand;
            String command = fullCommand.split(" ")[0];
            command = fullCommand.charAt(0) == '-' || fullCommand.charAt(0) == '+'
                ? command.substring(1) : command;
            xCom cp = commands.get(command);
            if (cp != null) {
                if (undoableCommands.contains(fullCommand.split(" ")[0]) && !isHidden) {
                    cEditorLogic.undoStateStack.push(cEditorLogic.getEditorState());
                    eManager.currentMap.wasLoaded = 1;
                    cEditorLogic.redoStateStack.clear();
                }
                if (!visibleCommands.contains(command)) {
                    if (fullCommand.charAt(0) == '-')
                        return cp.undoCommand(fullCommand);
                    else
                        return cp.doCommand(fullCommand);
                }
                else {
                    stringLines.add(String.format("console:~$ %s", fullCommand));
                    String result = fullCommand.charAt(0) == '-' ? cp.undoCommand(fullCommand)
                        : cp.doCommand(fullCommand);
                    if (result.length() > 0) {
                        stringLines.add(result);
                    }
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
