import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.Cursor;
import java.awt.Font;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class xCon {
    static int maxlinelength = 128;
    HashMap<String, gDoable> commands;
    HashMap<Integer, String> releaseBinds;
    HashMap<Integer, String> pressBinds;
    ArrayList<String> previousCommands;
    ArrayList<String> stringLines;
    int prevCommandIndex;
    String commandString;
    int linesToShowStart;
    int linesToShow;
    int cursorIndex;

    public xCon() {
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

        commands.put("activatemenu", new gDoable() {
            public String doCommand(String fullCommand) {
                if(!sSettings.inplay && !sSettings.show_mapmaker_ui) {
                    ex("playsound sounds/tap.wav");
                    uiMenus.menuSelection[uiMenus.selectedMenu].items[
                            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem].doItem();
                }
                return "1";
            }
        });
        commands.put("addcom", new gDoable() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcom can only be used by active server";
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "usage: addcom <command to execute>";
                StringBuilder act = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                xMain.shellLogic.serverNetThread.addNetCmd(actStr);
                return "server fanout comm: " + actStr;
            }
        });
        commands.put("addcomi", new gDoable() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcomi can only be used by the host";
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: addcomi <ids to ignore separated by any token except ',' > <string>";
                String ignoreId = args[1];
                StringBuilder act = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd(ignoreId, actStr);
                return "server fanout comm ignoring " + ignoreId + ": " + actStr;
            }
        });
        commands.put("addcomx", new gDoable() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcomx can only be used by active server";
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: addcomx <exclusive id> <string>";
                String exlusiveId = args[1];
                StringBuilder act = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                xMain.shellLogic.serverNetThread.addNetCmd(exlusiveId, actStr);
                return "server comm exclusive: " + actStr;
            }
        });
        commands.put("bind", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 2) {
                    String key = toks[1];
                    StringBuilder comm = new StringBuilder();
                    for(int i = 2; i < toks.length; i++) {
                        comm.append(toks[i]).append(" ");
                    }
                    Integer keycode = iKeyboard.getCodeForKey(key);
                    if(keycode != null) {
                        pressBinds.put(keycode, comm.substring(0,comm.length()-1));
                        return "bound press to keycode " + keycode + " -> " + comm;
                    }
                }
                return "cannot bindpress ";
            }

            public String undoCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 2) {
                    String key = toks[1];
                    StringBuilder comm = new StringBuilder();
                    for(int i = 2; i < toks.length; i++) {
                        comm.append(toks[i]).append(" ");
                    }
                    Integer keycode = iKeyboard.getCodeForKey(key);
                    if(keycode != null) {
                        releaseBinds.put(keycode, comm.substring(0,comm.length()-1));
                        return "bound release to keycode " + keycode + " -> ";
                    }
                }
                return "cannot bindrelease ";
            }
        });
        commands.put("changemap", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 2)
                    return "usage: changemap <path_to_mapfile>";
                String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
                gScoreboard.resetScoresMap();
                xMain.shellLogic.serverSimulationThread.scheduledEvents.clear();
                ex("loadingscreen");
                ex("exec " + mapPath); //by exec'ing the map, server is actively streaming blocks
                ex("-loadingscreen");
                if(!sSettings.show_mapmaker_ui) {
                    //spawn in after finished loading
                    nStateMap svMap = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
                    for (String id : svMap.keys()) {
                        xMain.shellLogic.serverNetThread.addNetCmd("server", "respawnnetplayer " + id);
                    }
                    long starttime = sSettings.gameTime;
                    for (long t = starttime + 1000; t <= starttime + sSettings.serverTimeLimit; t += 1000) {
                        long lastT = t;
                        xMain.shellLogic.serverSimulationThread.scheduledEvents.put(Long.toString(t), new gDoable() {
                            public void doCommand() {
                                if (sSettings.serverTimeLimit > 0)
                                    sSettings.serverTimeLeft =  Math.max(0, (starttime + sSettings.serverTimeLimit) - lastT);
                            }
                        });
                    }
                    xMain.shellLogic.serverSimulationThread.scheduledEvents.put(Long.toString(starttime + sSettings.serverTimeLimit), new gDoable() {
                        public void doCommand() {
                            //select winner and run postgame script
                            String winid = gScoreboard.getWinnerId();
                            if (!winid.equalsIgnoreCase("null")) {
                                nState winState = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot).get(winid);
                                String wname = winState.get("name");
                                String wcolor = winState.get("color");
                                ex("givewin " + winid);
                                ex(String.format("echo %s#%s wins!#%s", wname, wcolor, wcolor));
                                ex(String.format("spawnpopup %s WINNER!#%s", winid, wcolor));
                            }
                            ex("exec scripts/sv_endgame");
                        }
                    });
                    //ensure this servervar is ready right when script execs, sometimes it isn't
                    xMain.shellLogic.serverVars.put("gametimemillis", Long.toString(System.currentTimeMillis()));
                    ex("exec scripts/sv_startgame");
                }
                return "changed map to " + mapPath;
            }
        });
        commands.put("changemaprandom", new gDoable() {
            public String doCommand(String fullCommand) {
                if(eManager.mapsFileSelection.length < 1)
                    return "no maps found for changemaprandom";
                return ex("changemap maps/" + eManager.mapsFileSelection[(int)(Math.random()*eManager.mapsFileSelection.length)]);
            }
        });
        commands.put("chat", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                gMessages.enteringMessage = true;
                if(args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for(int i = 1; i < args.length; i++) {
                        sb.append(" ").append(args[i]);
                    }
                    gMessages.prompt = sb.substring(1);
                }
                else if(!gMessages.prompt.equals("SAY"))
                    gMessages.prompt = "SAY";
                return fullCommand;
            }
        });
        commands.put("cl_clearthingmappreview", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 1) {
                    String thing_title = toks[1];
                    if(uiEditorMenus.previewScene.objectMaps.containsKey(thing_title))
                        uiEditorMenus.previewScene.objectMaps.put(thing_title, new ConcurrentHashMap<>());
                }
                for(String thing_title : uiEditorMenus.previewScene.objectMaps.keySet()) {
                    uiEditorMenus.previewScene.objectMaps.get(thing_title).clear();
                }
                return "usage: cl_clearthingmappreview";
            }
        });
        commands.put("clientlist", new gDoable() {
            public String doCommand(String fullCommand) {
                StringBuilder s = new StringBuilder();
                nStateMap svMap = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
                for(String k : svMap.keys()) {
                    s.append(String.format("%s%s/%s,", k.equals(sSettings.uuid) ? "*": "",
                            svMap.get(k).get("name"), k));
                }
                return s.substring(0, s.length()-1);
            }
        });
        commands.put("commandlist", new gDoable() {
            public String doCommand(String fullCommand) {
                TreeSet<String> sorted = new TreeSet<>(commands.keySet());
                return sorted.toString();
            }
        });
        commands.put("constr", new gDoable() {
            //concatenate two or more strings
            //usage: constr <disparate elements to combine and return>
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                StringBuilder esb = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    esb.append(args[i]);
                }
                return esb.toString();
            }
        });
        commands.put("cvarlist", new gDoable() {
            public String doCommand(String fullCommand) {
                TreeMap<String, gArg> sorted = new TreeMap<>(xMain.shellLogic.clientVars.args);
                return sorted.toString();
            }
        });
        commands.put("damageplayer", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 2) {
                    String id = toks[1];
                    int dmg = Integer.parseInt(toks[2]);
                    String shooterid = "";
                    if(toks.length > 3)
                        shooterid = toks[3];
                    gPlayer player = xMain.shellLogic.serverScene.getPlayerById(id);
                    nState playerState = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot).get(id);
                    if(player == null || playerState == null)
                        return "no player found: " ;
                    xMain.shellLogic.console.ex(String.format("spawnpopup %s %d", id, dmg));
                    int newhp = Integer.parseInt(playerState.get("hp")) - dmg;
                    xMain.shellLogic.serverNetThread.setClientState(id, "hp", Integer.toString(newhp));
                    double rand = Math.random()*3;
                    String sound = rand < 1 ? "shout.wav" : rand < 2 ? "death.wav" : "growl.wav";
                    xMain.shellLogic.serverNetThread.addNetCmd(id, String.format("playsound sounds/%s", sound));
                    ex(String.format("exec scripts/sv_handledamageplayer %s %d", id, dmg));
                    //handle death
                    if(newhp < 1) {
                        //more server-side stuff
                        int dcx = player.coords[0];
                        int dcy = player.coords[1];
                        ex("deleteplayer " + id);
                        if(shooterid.length() < 1)
                            shooterid = "null";
                        ex("exec scripts/sv_handledestroyplayer " + id + " " + shooterid);
                        int animInd = gAnimations.ANIM_EXPLOSION_REG;
                        String colorName = playerState.get("color");
                        if(gAnimations.colorNameToExplosionAnimMap.containsKey(colorName))
                            animInd = gAnimations.colorNameToExplosionAnimMap.get(colorName);
                        ex(String.format("addcomi server cl_spawnanimation %d %d %d", animInd, dcx, dcy));
                        if(sSettings.respawnEnabled)
                            ex(String.format("scheduleevent %d respawnnetplayer %s",
                                    sSettings.gameTime + sSettings.serverRespawnDelay, id));
                    }
                    return id + " took " + dmg + " dmg from " + shooterid;
                }
                return "usage: damageplayer <player_id> <dmg_amount> <optional-shooter_id>";
            }
        });
        commands.put("deleteblock", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 2)
                    return "usage: deleteblock <id>";
                deleteBlockDelegate(toks, xMain.shellLogic.serverScene);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "deleted block";
            }
        });
        commands.put("cl_deleteblock", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1)
                    deleteBlockDelegate(toks, xMain.shellLogic.clientScene);
                return "usage: cl_deleteblock <id>";
            }
        });
        commands.put("deleteitem", new gDoable() {
            public String doCommand(String fullCommand) {
                deleteItemDelegate(fullCommand, xMain.shellLogic.serverScene);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_"+fullCommand);
                return "usage: deleteitem <id>";
            }
        });
        commands.put("cl_deleteitem", new gDoable() {
            public String doCommand(String fullCommand) {
                deleteItemDelegate(fullCommand, xMain.shellLogic.clientScene);
                return "usage: deleteitem <id>";
            }
        });
        commands.put("deleteplayer", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1) {
                    String id = toks[1];
                    ex("exec scripts/sv_handledeleteplayer " + id);
                    xMain.shellLogic.serverScene.getThingMap("THING_PLAYER").remove(id);
                    xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_"+fullCommand);
                }
                return "usage: deleteplayer <id>";
            }
        });
        commands.put("cl_deleteplayer", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1)
                    xMain.shellLogic.clientScene.getThingMap("THING_PLAYER").remove(toks[1]);
                return "usage: deleteplayer <id>";
            }
        });
        commands.put("deleteprefab", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1) {
                    deletePrefabDelegate(xMain.shellLogic.serverScene, toks[1]);
                    xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                }
                return "usage: deleteprefab <id>";
            }
        });
        commands.put("cl_deleteprefab", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1)
                    deletePrefabDelegate(xMain.shellLogic.clientScene, toks[1]);
                return "usage: cl_deleteprefab <id>";
            }
        });
        commands.put("disconnect", new gDoable() {
            public String doCommand(String fullCommand) {
                if(sSettings.IS_SERVER && sSettings.IS_CLIENT) {
                    xMain.shellLogic.clientVars.put("maploaded", "0");
                    xMain.shellLogic.clientNetThread.disconnect();
                    ex("cl_load");
                    xMain.shellLogic.serverNetThread.disconnect();
                    xMain.shellLogic.serverSimulationThread.disconnect();
                }
                else if(sSettings.IS_CLIENT) {
                    xMain.shellLogic.clientVars.put("maploaded", "0");
                    xMain.shellLogic.clientNetThread.disconnect();
                    ex("cl_load");
                }
                if (sSettings.inplay)
                    ex("pause");
                return fullCommand;
            }
        });
        commands.put("echo", new gDoable() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "only server can use command: echo";
                String[] lineArgCallTokens = fullCommand.trim().split(" ");
                for(int i = 0; i < lineArgCallTokens.length; i++) {
                    if(lineArgCallTokens[i].contains("#")) {
                        String[] toks = lineArgCallTokens[i].split("#");
                        for(int j = 0; j < toks.length; j++) {
                            if(!toks[j].startsWith("$"))
                                continue;
                            if(xMain.shellLogic.serverVars.contains(toks[j].substring(1)))
                                toks[j] = xMain.shellLogic.serverVars.get(toks[j].substring(1));
                        }
                        lineArgCallTokens[i] = toks[0] + "#" + toks[1];
                    }
                    else if(lineArgCallTokens[i].startsWith("$")) {
                        String tokenKey = lineArgCallTokens[i];
                        if (xMain.shellLogic.serverVars.contains(tokenKey.substring(1)))
                            lineArgCallTokens[i] = xMain.shellLogic.serverVars.get(tokenKey.substring(1));
                    }
                }
                StringBuilder parsedStringBuilder = new StringBuilder();
                for(String lineArgtoken : lineArgCallTokens) {
                    parsedStringBuilder.append(" ").append(lineArgtoken);
                }
                String parsedCommand = parsedStringBuilder.substring(1);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + parsedCommand);
                return "FANOUT to clients: cl_" + parsedCommand;
            }
        });
        commands.put("cl_echo", new gDoable() {
            public String doCommand(String fullCommand) {
                String rs = fullCommand.substring(fullCommand.indexOf(" ")+1);
                gMessages.addScreenMessage(rs);
                return rs;
            }
        });
        commands.put("e_changejoinip", new gDoable() {
            public String doCommand(String fullCommand) {
                ex("chat Enter New IP Address");
                return "";
            }
        });
        commands.put("e_changejoinport", new gDoable() {
            public String doCommand(String fullCommand) {
                ex("chat Enter New Port");
                return "";
            }
        });
        commands.put("e_changeplayername", new gDoable() {
            public String doCommand(String fullCommand) {
                ex("chat Enter New Name");
                return "";
            }
        });
        commands.put("e_delthing", new gDoable() {
            public String doCommand(String fullCommand) {
                if(sSettings.clientSelectedPrefabId.length() > 0) {
                    xMain.shellLogic.clientNetThread.addNetCmd("deleteprefab " + sSettings.clientSelectedPrefabId);
                    return "deleted prefab " + sSettings.clientSelectedPrefabId;
                }
                if(sSettings.clientSelectedItemId.length() > 0) {
                    xMain.shellLogic.clientNetThread.addNetCmd("deleteitem " + sSettings.clientSelectedItemId);
                    return "deleted item " + sSettings.clientSelectedItemId;
                }
                return "nothing to delete";
            }
        });
        commands.put("e_newmap", new gDoable() {
            public String doCommand(String fullCommand) {
                ex("load;addcomi server cl_setvar maploaded 1");
                gScoreboard.resetScoresMap();
                return "";
            }
        });
        commands.put("e_openfile", new gDoable() {
            public String doCommand(String fullCommand) {
                if(sSettings.show_mapmaker_ui) {
                    JFileChooser fileChooser = new JFileChooser();
                    uiEditorMenus.setFileChooserFont(fileChooser.getComponents());
                    File workingDirectory = new File("maps");
                    fileChooser.setCurrentDirectory(workingDirectory);
                    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        if(!ex("e_showlossalert").equals("0"))
                            return "";
                        File file = fileChooser.getSelectedFile();
                        if(!sSettings.IS_SERVER) {
                            ex("startserver");
                            ex("load");
                            ex("joingame localhost " + sSettings.serverListenPort);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        sSettings.serverLoadingFromHDD = true;
                        ex("changemap " + file.getPath());
                        uiEditorMenus.refreshGametypeCheckBoxMenuItems();
                        return "opening " + file.getPath();
                    }
                }
                return "";
            }
        });
        commands.put("e_rotthing", new gDoable() {
            public String doCommand(String fullCommand) {
                String newprefabname = sSettings.clientNewPrefabName;
                if (newprefabname.contains("_000"))
                    sSettings.clientNewPrefabName = newprefabname.replace("_000", "_090");
                else if (newprefabname.contains("_090"))
                    sSettings.clientNewPrefabName = newprefabname.replace("_090", "_180");
                else if (newprefabname.contains("_180"))
                    sSettings.clientNewPrefabName = newprefabname.replace("_180", "_270");
                else if (newprefabname.contains("_270"))
                    sSettings.clientNewPrefabName = newprefabname.replace("_270", "_000");
                if(newprefabname.contains("_000") || newprefabname.contains("_090") || newprefabname.contains("_180")
                        || newprefabname.contains("_270")) {
                    ex("cl_clearthingmappreview");
                    ex(String.format("cl_execpreview prefabs/%s 0 0 12500 5600", sSettings.clientNewPrefabName));
                }
                return "";
            }
        });
        commands.put("e_saveas", new gDoable() {
            public String doCommand(String fullcommand) {
                JFileChooser fileChooser = new JFileChooser();
                uiEditorMenus.setFileChooserFont(fileChooser.getComponents());
                File workingDirectory = new File("maps");
                fileChooser.setCurrentDirectory(workingDirectory);
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filename = file.getName();
                    String foldername = file.getParent();
                    xMain.shellLogic.clientScene.saveAs(filename, foldername);
                    return "saved " + file.getPath();
                }
                return "";
            }
        });
        commands.put("e_showlossalert", new gDoable() {
            public  String doCommand(String fullcomm) {
                return Integer.toString(JOptionPane.showConfirmDialog(xMain.shellLogic.displayPane.contentPane,
                        "Any unsaved changes will be lost...", "Are You Sure?", JOptionPane.YES_NO_OPTION));
            }
        });
        commands.put("exec", new gDoable() {
            public  String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 2)
                    return "usage: exec <script_id> <optional: args>";
                String scriptId = args[1];
                if(sSettings.serverLoadingFromHDD) { //detect loading from openFile
                    System.out.println("LOADING MAP FROM HDD");
                    sSettings.serverLoadingFromHDD = false;
                    ex("loadingscreen");
                    try (BufferedReader br = new BufferedReader(new FileReader(scriptId))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if(line.trim().length() > 0 && line.trim().charAt(0) != '#')
                                ex(line);
                        }
                    }
                    catch (Exception e) {
                        logException(e);
                        e.printStackTrace();
                    }
                    ex("-loadingscreen");
                    return "loaded map " + scriptId;
                }
                gScript theScript = xMain.shellLogic.scriptFactory.getScript(scriptId);
                if(theScript == null)
                    return "no script found for: " + scriptId;
                if(args.length > 2) {
                    String[] callArgs = new String[args.length - 2];
                    for(int i = 0; i < callArgs.length; i++) {
                        callArgs[i] = args[i+2];
                        if(callArgs[i].startsWith("$")) {
                            String tokenKey = callArgs[i];
                            if(xMain.shellLogic.serverVars.contains(tokenKey))
                                callArgs[i] = xMain.shellLogic.serverVars.get(tokenKey);
                        }
                    }
                    theScript.callScript(callArgs);
                }
                else
                    theScript.callScript(new String[]{});
                return "script completed successfully";
            }
        });
        commands.put("cl_execpreview", new gDoable() {
            public  String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 2)
                    return "usage: cl_execpreview <script_id> <optional: args>";
                String scriptId = args[1];
                gScript theScript = xMain.shellLogic.scriptFactory.getScript(scriptId);
                StringBuilder moddedScriptContentBuilder = new StringBuilder();
                for(String rawLine : theScript.lines) {
                    if(rawLine.contains("putblock BLOCK_FLOOR")
                            || rawLine.contains("putblock BLOCK_CUBE")
                            || rawLine.contains("getres")) {
                        String clRawLine = rawLine.replace("getres",
                                "cl_getres").replace("putblock",
                                "cl_putblockpreview");
                        moddedScriptContentBuilder.append("\n").append(clRawLine);
                    }
                }
                gScript moddedScript = new gScript("tmp_script", moddedScriptContentBuilder.substring(1));
//                System.out.println("CL_EXECPREVIEW: " + moddedScript.lines);
                String[] callArgs = new String[args.length - 2];
                for(int i = 0; i < callArgs.length; i++) {
                    callArgs[i] = args[i+2];
                }
                moddedScript.callScriptClientPreview(callArgs);
                return "execpreview";
            }
        });
        commands.put("fireweapon", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 2) {
                    String id = toks[1];
                    int weapon = Integer.parseInt(toks[2]);
                    gWeapons.fromCode(weapon).fireWeapon(xMain.shellLogic.serverScene.getPlayerById(id), xMain.shellLogic.serverScene);
                    return id + " fired weapon " + weapon;
                }
                return "usage: fireweapon <player_id> <weapon_code>";
            }
        });
        commands.put("cl_fireweapon", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 2) {
                    String id = toks[1];
                    int weapon = Integer.parseInt(toks[2]);
                    gWeapons.fromCode(weapon).fireWeapon(xMain.shellLogic.getPlayerById(id), xMain.shellLogic.clientScene);
                    return id + " fired weapon " + weapon;
                }
                return "usage: cl_fireweapon <player_id> <weapon_code>";
            }
        });
        commands.put("foreach", new gDoable() {
            //usage: foreach $var $start $end $incr <script to execute where $var is preloaded>
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 6)
                    return "usage: foreach $var $start $end $incr <script where $var is num>";
                String varname = args[1];
                int start = Integer.parseInt(args[2]);
                int end = Integer.parseInt(args[3]);
                int incr = Integer.parseInt(args[4]);
                for(int i = start; i <= end; i+=incr) {
                    ex(String.format("setvar %s %s", varname, i));
                    String[] cargs = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                    String[] subarray = Arrays.stream(cargs, 5, cargs.length).toArray(String[]::new);
                    String es = String.join(" ", subarray);
                    ex(es);
                    xMain.shellLogic.serverVars.args.remove(varname); //why is this needed here and not in foreachthing???
                }
                return "usage: foreach $var $start $end $incr <script where $var is num>";
            }
        });
        commands.put("foreachclient", new gDoable() {
            //usage: foreachclient $id <script to execute where $id is preloaded>
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: foreachclient $id <script where $id is preloaded>";
                String varname = args[1];
                nStateMap svMap = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
                for(String id : svMap.keys()) {
                    ex(String.format("setvar %s %s", varname, id));
                    String[] cargs = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                    StringBuilder esb = new StringBuilder();
                    for(int i = 2; i < cargs.length; i++) {
                        esb.append(" ").append(cargs[i]);
                    }
                    String es = esb.substring(1);
                    ex(es);
                    xMain.shellLogic.serverVars.args.remove(varname); //why is this needed here and not in foreachthing???
                }
                return "usage: foreachclient $id <script to execute where $id is preloaded>";
            }
        });
        commands.put("foreachlong", new gDoable() {
            //usage: foreachlong $var $start $end $incr <script to execute where $var is preloaded>
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 6)
                    return "usage: foreachlong $var $start $end $incr <script where $var is num>";
                String varname = args[1];
                long start = Long.parseLong(args[2]);
                long end = Long.parseLong(args[3]);
                int incr = Integer.parseInt(args[4]);
                for(long i = start; i <= end; i+=incr) {
                    ex(String.format("setvar %s %s", varname, i));
                    String[] cargs = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                    String[] subarray = Arrays.stream(cargs, 5, cargs.length).toArray(String[]::new);
                    String es = String.join(" ", subarray);
                    ex(es);
                    xMain.shellLogic.serverVars.args.remove(varname); //why is this needed here and not in foreachthing???
                }
                return "usage: foreachlong $var $start $end $incr <script where $var is num>";
            }
        });
        commands.put("foreachthing", new gDoable() {
            //usage: foreachthing $var $THING_TYPE <script to execute where $var is preloaded>
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 4)
                    return "usage: foreach $var $THING_TYPE <script where $var is preloaded>";
                gScene scene = xMain.shellLogic.serverScene;
                String varname = args[1];
                String thingtype = args[2];
                if(!scene.objectMaps.containsKey(thingtype))
                    return "no thing type in scene: " + thingtype;
                for(String id : scene.getThingMapIds(thingtype)) {
                    ex(String.format("setvar %s %s", varname, id));
                    String[] cargs = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                    StringBuilder esb = new StringBuilder();
                    for(int i = 3; i < cargs.length; i++) {
                        esb.append(" ").append(cargs[i]);
                    }
                    String es = esb.substring(1);
                    ex(es);
                }
                return "usage: foreach $var $THING_TYPE <script to execute where $var is preloaded>";
            }
        });
        commands.put("gamemode", new gDoable() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "only server can do 'gamemode'";
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return xMain.shellLogic.serverVars.get("gamemode");
                String setmode = args[1];
                xMain.shellLogic.serverVars.put("gamemode", setmode);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_setvar gamemode " + setmode);
                return "changed game mode to " + xMain.shellLogic.serverVars.get("gamemode");
            }
        });
        commands.put("getnewitemid", new gDoable() {
            public String doCommand(String fullCommand) {
                int itemId = 0;
                for(String id : xMain.shellLogic.serverScene.getThingMap("THING_ITEM").keySet()) {
                    if(itemId < Integer.parseInt(id))
                        itemId = Integer.parseInt(id);
                }
                return Integer.toString(itemId+1); //want to be the NEXT id
            }
        });
        commands.put("getrand", new gDoable() {
            // usage: getrand $min $max
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "0";
                int start = Integer.parseInt(args[1]);
                int end = Integer.parseInt(args[2]);
                return Integer.toString(ThreadLocalRandom.current().nextInt(start, end + 1));
            }
        });
        commands.put("getrandclid", new gDoable() {
            // usage: getrandclid
            public String doCommand(String fullCommand) {
                nStateMap svMap = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
                if(svMap.keys().size() < 1)
                    return "null";
                int randomClientIndex = (int) (Math.random() * svMap.keys().size());
                ArrayList<String> clientIds = new ArrayList<>(svMap.keys());
                return clientIds.get(randomClientIndex);
            }
        });
        commands.put("getrandthing", new gDoable() {
            // usage: getrandthing $type
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                String type = args[1];
                if(!xMain.shellLogic.serverScene.objectMaps.containsKey(type) || xMain.shellLogic.serverScene.objectMaps.get(type).size() < 1)
                    return "null";
                List<String> keysAsArray = new ArrayList<>(xMain.shellLogic.serverScene.objectMaps.get(type).keySet());
                return keysAsArray.get(ThreadLocalRandom.current().nextInt(0, keysAsArray.size()));
            }
        });
        commands.put("getres", new gDoable() {
            public String doCommand(String fullCommand) {
                return getResDelegate(xMain.shellLogic.serverVars, fullCommand);
            }
        });
        commands.put("cl_getres", new gDoable() {
            public String doCommand(String fullCommand) {
                return getResDelegate(xMain.shellLogic.clientVars, fullCommand);
            }
        });
        commands.put("givepoint", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 2)
                    return "usage: givepoint <playerid> <points#optional>";
                String id = args[1];
                int score = 100;
                if(args.length > 2)
                    score = Integer.parseInt(args[2]);
                gScoreboard.addToScoreField(id, "score", score);
                nStateMap svMap = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
                String color = svMap.get(id).get("color");
                ex(String.format("spawnpopup %s +%d#%s", id, score, color));
                return "gave point to " + id;
            }
        });
        commands.put("givewin", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                String id = args[1];
                gScoreboard.addToScoreField(id, "wins", 1);
                return id;
            }
        });
        commands.put("gobackui", new gDoable() {
            public String doCommand(String fullCommand) {
                if(sSettings.inplay)
                    ex("pause");
                else {
                    if(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0) {
                        if(!sSettings.IS_CLIENT) {
                            //offline mode do this
                            uiMenus.selectedMenu = uiMenus.MENU_QUIT;
                            ex("playsound sounds/goodwork.wav");
                        }
                        else
                            ex("pause");
                    }
                    else {
                        uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
                        ex("playsound sounds/goodwork.wav");
                    }
                }
                return fullCommand;
            }
        });
        commands.put("hostgame", new gDoable() {
            public String doCommand(String fullCommand) {
                ex(new String[] {
                        "startserver",
                        String.format("changemap%s", eManager.mapSelectionIndex < 0 ? "random" : " maps/"+eManager.mapsFileSelection[eManager.mapSelectionIndex]),
                        "joingame localhost " + sSettings.serverListenPort,
                        "pause"
                });
                return "hosting game on port " + sSettings.serverListenPort;
            }
        });
        commands.put("joingame", new gDoable() {
            public String doCommand(String fullCommand) {
                xMain.shellLogic.clientNetThread = new eGameLogicClient();
                new eGameSession(xMain.shellLogic.clientNetThread, sSettings.rateclient);
                sSettings.IS_CLIENT = true;
                return "joined game";
            }
        });
        commands.put("load", new gDoable() {
            public String doCommand(String fullCommand) {
                //load the most basic blank map
                gTextures.clear();
                ex("setvar gamemode 0");
                xMain.shellLogic.serverScene = new gScene();
                if(sSettings.IS_SERVER)
                    xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_load");
                return "";
            }
        });
        commands.put("cl_load", new gDoable() {
            public String doCommand(String fullCommand) {
                //load the most basic blank map
                gTextures.clear();
                ex("cl_setvar gamemode 0");
                xMain.shellLogic.clientScene = new gScene();
                return "";
            }
        });
        commands.put("loadingscreen", new gDoable() {
            public String doCommand(String fullCommand) {
                if(sSettings.IS_SERVER)
                    xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_setvar maploaded 0");
                return "loading screen ON";
            }
            public String undoCommand(String fullCommand) {
                if(sSettings.IS_SERVER)
                    xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_setvar maploaded 1");
                return "loading screen OFF";
            }
        });
        commands.put("mouseleft", new gDoable() {
            public String doCommand(String fullCommand) {
                if(xMain.shellLogic.displayPane.frame.hasFocus()) {
                    if (sSettings.inplay)
                        iMouse.holdingMouseLeft = true;
                    else {
                        if(sSettings.show_mapmaker_ui && sSettings.clientMapLoaded) {
                            int[] mc = uiInterface.getMouseCoordinates();
                            if(sSettings.clientNewPrefabName.length() > 0) {
                                int[] pfd = dHUD.getNewPrefabDims();
                                int w = pfd[0];
                                int h = pfd[1];
                                int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + gCamera.coords[0] - w / 2,
                                        uiEditorMenus.snapToX);
                                int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + gCamera.coords[1] - h / 2,
                                        uiEditorMenus.snapToY);
                                int bid = 0;
                                int pid = 0;
                                for(String id : xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").keySet()) {
                                    if(bid < Integer.parseInt(id))
                                        bid = Integer.parseInt(id);
                                    int tpid = Integer.parseInt(xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").get(id).prefabId);
                                    if(pid < tpid)
                                        pid = tpid;
                                }
                                bid++; //want to be the _next_ id
                                pid++; //want to be the _next_ id
                                String cmd = String.format("exec prefabs/%s %d %d %d %d", sSettings.clientNewPrefabName, bid, pid, pfx, pfy);
                                xMain.shellLogic.clientNetThread.addNetCmd(cmd);
                                return "put prefab " + sSettings.clientNewPrefabName;
                            }
                            if(uiEditorMenus.newitemname.length() > 0) {
                                int iw = 300;
                                int ih = 300;
                                int ix = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + gCamera.coords[0] - iw/2,
                                        uiEditorMenus.snapToX);
                                int iy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + gCamera.coords[1] - ih/2,
                                        uiEditorMenus.snapToY);
                                String cmd = String.format("putitem %s %d %d %d",
                                        uiEditorMenus.newitemname, xMain.shellLogic.getNewItemIdClient(), ix, iy);
                                xMain.shellLogic.clientNetThread.addNetCmd(cmd);
                                return "put item " + uiEditorMenus.newitemname;
                            }
                        }
                        else if(uiMenus.gobackSelected) {
                            ex("gobackui");
                        }
                        else if(uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem > -1) {
                            ex("activatemenu");
                        }
                    }
                }
                return fullCommand;
            }

            public String undoCommand(String fullCommand) {
                iMouse.holdingMouseLeft = false;
                return fullCommand;
            }
        });
        commands.put("pause", new gDoable() {
            public String doCommand(String fullCommand) {
                sSettings.inplay = !sSettings.inplay;
                xMain.shellLogic.displayPane.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if(sSettings.inplay) {
                    xMain.shellLogic.displayPane.frame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    if(sSettings.show_mapmaker_ui)
                        xMain.shellLogic.clientNetThread.addNetCmd("respawnnetplayer " + sSettings.uuid);
                }
                else if(sSettings.show_mapmaker_ui)
                    xMain.shellLogic.clientNetThread.addNetCmd("deleteplayer " + sSettings.uuid);
                return fullCommand;
            }
        });
        commands.put("playerdown", new gDoable() {
            public String doCommand(String fullCommand) {
                playerMoveDelegate(1);
                return "player down";
            }
            public String undoCommand(String fullCommand) {
                playerStopMoveDelegate(1);
                return "stop player down";
            }
        });
        commands.put("playerleft", new gDoable() {
            public String doCommand(String fullCommand) {
                playerMoveDelegate(2);
                return "player left";
            }
            public String undoCommand(String fullCommand) {
                playerStopMoveDelegate(2);
                return "stop player left";
            }
        });
        commands.put("playerright", new gDoable() {
            public String doCommand(String fullCommand) {
                playerMoveDelegate(3);
                return "player right";
            }
            public String undoCommand(String fullCommand) {
                playerStopMoveDelegate(3);
                return "stop player right";
            }
        });
        commands.put("playerup", new gDoable() {
            public String doCommand(String fullCommand) {
                playerMoveDelegate(0);
                return "player up";
            }
            public String undoCommand(String fullCommand) {
                playerStopMoveDelegate(0);
                return "stop player up";
            }
        });
        commands.put("playsound", new gDoable() {
            final double sfxrange = 2400.0;
            public String doCommand(String fullCommand) {
                if(!sSettings.audioenabled)
                    return "audio muted";
                String[] toks = fullCommand.split(" ");
                try{
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(eManager.getAudioFile(eManager.getPath(toks[1]))));
                    if(toks.length > 2) {
                        if(toks.length > 4) {
                            int diffx = gCamera.coords[0] + eUtils.unscaleInt(sSettings.width)/2-Integer.parseInt(toks[3]);
                            int diffy = gCamera.coords[1] + eUtils.unscaleInt(sSettings.height)/2-Integer.parseInt(toks[4]);
                            double absdistance = Math.sqrt(Math.pow((diffx), 2) + Math.pow((diffy), 2));
                            double distanceAdj = 1.0 - (absdistance /sfxrange);
                            if(distanceAdj < 0 )
                                return "sound too far away to hear";
                            double panAdjust = absdistance/sfxrange;
                            if(diffx > 0)
                                panAdjust = -panAdjust;
                            if(clip.isControlSupported(FloatControl.Type.BALANCE)) {
                                FloatControl balControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
                                balControl.setValue((float) panAdjust);
                            }
                            else if(clip.isControlSupported(FloatControl.Type.PAN)) {
                                FloatControl balControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                                balControl.setValue((float) panAdjust);
                            }
                            if(clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                                float dB = (float) ((Math.log(distanceAdj * (sSettings.clientVolume*0.01)) / Math.log(10.0) * 20.0));
                                gainControl.setValue(dB);
                            }
                        }
                        clip.loop(Integer.parseInt(toks[2]));
                    }
                    else {
                        if(clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            float dB = (float) (Math.log((sSettings.clientVolume*0.01)) / Math.log(10.0) * 20.0);
                            gainControl.setValue(dB);
                        }
                        clip.start();
                    }
                    xMain.shellLogic.soundClips.add(clip);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return fullCommand;
            }
        });
        commands.put("putblock", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length < 8)
                    return "usage: putblock <BLOCK_TITLE> <id> <pid> <x> <y> <w> <h>. opt: <t> <m> ";
                putBlockDelegate(toks, xMain.shellLogic.serverScene, toks[1], toks[2], toks[3]);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "1";
            }
        });
        commands.put("cl_putblock", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length < 8)
                    return "usage: cl_putblock <BLOCK_TITLE> <id> <pid> <x> <y> <w> <h>. opt: <t> <m> ";
                putBlockDelegate(toks, xMain.shellLogic.clientScene, toks[1], toks[2], toks[3]);
                return "1";
            }
        });
        commands.put("cl_putblockpreview", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length < 8)
                    return "usage:cl_putblockpreview <BLOCK_TITLE> <id> <pid> <x> <y> <w> <h>. opt: <t> <m> ";
                putBlockDelegate(toks, uiEditorMenus.previewScene, toks[1], toks[2], toks[3]);
                return "1";
            }
        });
        commands.put("putfloor", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 5)
                    return "usage: putfloor <id> <prefabId> <x> <y>";
                new gBlockFloor(toks[1], toks[2], Integer.parseInt(toks[3]), Integer.parseInt(toks[4])).addToScene(xMain.shellLogic.serverScene);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "put floor server";
            }
        });
        commands.put("cl_putfloor", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 5)
                    return "usage: cl_putfloor <id> <prefabId> <x> <y>";
                new gBlockFloor(toks[1], toks[2], Integer.parseInt(toks[3]), Integer.parseInt(toks[4])).addToScene(xMain.shellLogic.clientScene);
                return "put floor client";
            }
        });
        commands.put("putcollision", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 7)
                    return "usage: putcollision <id> <prefabId> <x> <y> <w> <h>";
                new gBlockCollision(toks[1], toks[2], Integer.parseInt(toks[3]), Integer.parseInt(toks[4]),
                        Integer.parseInt(toks[5]), Integer.parseInt(toks[6])
                ).addToScene(xMain.shellLogic.serverScene);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "put collision server";
            }
        });
        commands.put("cl_putcollision", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 7)
                    return "usage: cl_putcollision <id> <prefabId> <x> <y> <w> <h>";
                new gBlockCollision(toks[1], toks[2], Integer.parseInt(toks[3]), Integer.parseInt(toks[4]),
                        Integer.parseInt(toks[5]), Integer.parseInt(toks[6])
                ).addToScene(xMain.shellLogic.clientScene);
                return "put collision client";
            }
        });
        commands.put("putcube", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 9)
                    return "usage: putcube <id> <prefabid> <x> <y> <w> <y> <top_height> <wall_height>";
                new gBlockCube(
                        toks[1], toks[2],
                        Integer.parseInt(toks[3]),
                        Integer.parseInt(toks[4]),
                        Integer.parseInt(toks[5]),
                        Integer.parseInt(toks[6]),
                        Integer.parseInt(toks[7]),
                        Integer.parseInt(toks[8])
                    ).addToScene(xMain.shellLogic.serverScene);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "put cube server";
            }
        });
        commands.put("cl_putcube", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 9)
                    return "usage: cl_putcube <id> <prefabid> <x> <y> <w> <y> <top_height> <wall_height>";
                new gBlockCube(
                        toks[1], toks[2],
                        Integer.parseInt(toks[3]),
                        Integer.parseInt(toks[4]),
                        Integer.parseInt(toks[5]),
                        Integer.parseInt(toks[6]),
                        Integer.parseInt(toks[7]),
                        Integer.parseInt(toks[8])
                ).addToScene(xMain.shellLogic.clientScene);
                return "put cube client";
            }
        });
        commands.put("putitem", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 5)
                    return "usage: putitem <ITEM_TITLE> <id> <x> <y>";
                putItemDelegate(toks, xMain.shellLogic.serverScene);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "put item";
            }
        });
        commands.put("cl_putitem", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 5)
                    return "usage: cl_putitem <ITEM_TITLE> <id> <x> <y>";
                putItemDelegate(toks, xMain.shellLogic.clientScene);
                return "cl_put item";
            }
        });
        commands.put("quit", new gDoable() {
            public String doCommand(String fullCommand) {
                if(sSettings.show_mapmaker_ui && sSettings.clientMapLoaded) {
                    if(!ex("e_showlossalert").equals("0"))
                        return "";
                }
                xMain.shellLogic.disconnect();
                return "";
            }
        });
        commands.put("REM", new gDoable() {
            public String doCommand(String fullCommand) {
                return "comment";
            }
        });
        commands.put("respawnnetplayer", new gDoable() {
            int tries = 0;
            final int trylimit = 5;
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length < 2)
                    return "usage: respawnnetplayer <id>";
                tries++;
                if(tries > trylimit) {
                    tries = 0;
                    return "couldn't find available ITEM_SPAWNPOINT";
                }
                String randomSpawnId = ex("getrandthing ITEM_SPAWNPOINT");
                if(!randomSpawnId.equalsIgnoreCase("null")) {
                    gThing randomSpawn = xMain.shellLogic.serverScene.getThingMap("ITEM_SPAWNPOINT").get(randomSpawnId);
                    if(((gItem) randomSpawn).occupied > 0)
                        ex("respawnnetplayer " + toks[1]);
                    else {
                        tries = 0;
                        ex(String.format("spawnplayer %s %s %s teal", toks[1], randomSpawn.coords[0], randomSpawn.coords[1]));
                    }
                }
                return fullCommand;
            }
        });
        commands.put("say", new gDoable() {
            public String doCommand(String fullCommand) {
                if(fullCommand.length() > 0) {
                    String msg = sSettings.clientPlayerName + "#"+ sSettings.clientPlayerColor +": "
                            + fullCommand.substring(fullCommand.indexOf(" ") + 1);
                    xMain.shellLogic.clientNetThread.addNetCmd("echo " + msg);
                    gMessages.msgInProgress = "";
                }
                return "said enough";
            }
        });
        commands.put("scheduleevent", new gDoable() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "scheduleevent can only be used by active server";
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: scheduleevent <time> <string to execute>";
                StringBuilder act = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String timeToExec = args[1];
                String actStr = act.substring(1);
                xMain.shellLogic.serverSimulationThread.scheduledEvents.put(timeToExec,
                        new gDoable() {
                            public void doCommand() {
                                ex(actStr);
                            }
                        }
                );
                return "added time event @" + timeToExec + ": " + actStr;
            }
        });
        commands.put("selectdown", new gDoable() {
            public String doCommand(String fullCommand) {
                ex("playerdown");
                if(!sSettings.show_mapmaker_ui && !sSettings.inplay) {
                    sSettings.hideMouseUI = true;
                    uiMenus.nextItem();
                }
                return fullCommand;
            }
        });
        commands.put("selectleft", new gDoable() {
            public String doCommand(String fullCommand) {
                ex("playerleft");
                if((!sSettings.show_mapmaker_ui && !sSettings.inplay) &&
                        !(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0))
                    uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
                return fullCommand;
            }
        });
        commands.put("selectright", new gDoable() {
            public String doCommand(String fullCommand) {
                ex("playerright");
                if(!sSettings.show_mapmaker_ui && !sSettings.inplay) {
                    uiMenus.menuSelection[uiMenus.selectedMenu].items[uiMenus.menuSelection[
                            uiMenus.selectedMenu].selectedItem].doItem();
                    ex("playsound sounds/goodwork.wav");
                }
                return fullCommand;
            }
        });
        commands.put("selectup", new gDoable() {
            public String doCommand(String fullCommand) {
                ex("playerup");
                if(!sSettings.show_mapmaker_ui && !sSettings.inplay) {
                    sSettings.hideMouseUI = true;
                    uiMenus.prevItem();
                }
                return fullCommand;
            }
        });
        commands.put("setnstate", new gDoable() {
            //usage: setnstate $id $key $value
            public String doCommand(String fullCommand) {
                nStateMap serverStateSnapshot = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return serverStateSnapshot.toString();
                String pid = args[1];
                nState clientState = serverStateSnapshot.get(pid);
                if(clientState == null)
                    return "null";
                if(args.length < 3)
                    return clientState.toString();
                String tk = args[2];
                if(args.length < 4) {
                    if(!clientState.contains(tk))
                        return "null";
                    return clientState.get(tk);
                }
                StringBuilder tvb = new StringBuilder();
                for(int i = 3; i < args.length; i++) {
                    tvb.append(" ").append(args[i]);
                }
                String tv = tvb.substring(1);
                return xMain.shellLogic.serverNetThread.setClientState(pid, tk, tv);
            }
        });
        commands.put("setplayer", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 4)
                    return "usage: setplayer <player_id> <var_name> <var_value>";
                String pid = args[1];
                String varname = args[2];
                String varval = args[3];
                String giveString = String.format("setthing THING_PLAYER %s %s %s", pid, varname, varval);
                ex(giveString);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + giveString);
                return "player " + pid + " given var '" + varname + "' with value of " + varval;
            }
        });
        commands.put("setplayercoords", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 4)
                    return "null";
                gPlayer p = xMain.shellLogic.serverScene.getPlayerById(args[1]);
                if(p == null)
                    return "null";
                p.coords[0] = Integer.parseInt(args[2]);
                p.coords[1] = Integer.parseInt(args[3]);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return fullCommand;
            }
        });
        commands.put("cl_setplayercoords", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 4)
                    return "null";
                gPlayer p = xMain.shellLogic.getPlayerById(args[1]);
                if(p == null)
                    return "null";
                p.coords[0] = Integer.parseInt(args[2]);
                p.coords[1] = Integer.parseInt(args[3]);
                return fullCommand;
            }
        });
        commands.put("setthing", new gDoable() {
            //usage: setthing $THING_TYPE $id $key $val
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                return setThingDelegate(args, xMain.shellLogic.serverScene);
            }
        });
        commands.put("cl_setthing", new gDoable() {
            //usage cl_setthing $type $id $key $var
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.clientVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                return setThingDelegate(args, xMain.shellLogic.clientScene);
            }
        });
        commands.put("setvar", new gDoable() {
            public String doCommand(String fullCommand) {
                //usage setvar $key $val
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                String tk = args[1];
                if(args.length < 3) {
                    if (!xMain.shellLogic.serverVars.contains(tk))
                        return "null";
                    return xMain.shellLogic.serverVars.get(tk);
                }
                StringBuilder tvb = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    tvb.append(" ").append(args[i]);
                }
                String tv = tvb.substring(1);
                xMain.shellLogic.serverVars.put(tk, tv);
                return xMain.shellLogic.serverVars.get(tk);
            }
        });
        commands.put("cl_setvar", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 2)
                    return "null";
                String tk = toks[1];
                if(toks.length < 3) {
                    if(!xMain.shellLogic.clientVars.contains(tk))
                        return "null";
                    return xMain.shellLogic.clientVars.get(tk);
                }
                StringBuilder tvb = new StringBuilder();
                for(int i = 2; i < toks.length; i++) {
                    tvb.append(" ").append(toks[i]);
                }
                String tv = tvb.substring(1);
                if(tv.charAt(0) == '$' && xMain.shellLogic.clientVars.contains(tv.substring(1)))
                    xMain.shellLogic.clientVars.put(tk, xMain.shellLogic.clientVars.get(tv.substring(1)));
                else
                    xMain.shellLogic.clientVars.put(tk, tv);
                return xMain.shellLogic.clientVars.get(tk);
            }
        });
        commands.put("cl_spawnanimation", new gDoable() {
            public String doCommand(String fullCommand) {
                if(sSettings.vfxenableanimations) {
                    String[] toks = fullCommand.split(" ");
                    if (toks.length > 3) {
                        int animcode = Integer.parseInt(toks[1]);
                        int x = Integer.parseInt(toks[2]);
                        int y = Integer.parseInt(toks[3]);
                        xMain.shellLogic.clientScene.getThingMap("THING_ANIMATION").put(eUtils.createId(),
                                new gAnimation(animcode, x, y));
                        return "spawned animation " + animcode + " at " + x + " " + y;
                    }
                }
                return "usage: cl_spawnanimation <animation_code> <x> <y>";
            }
        });
        commands.put("spawnplayer", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 3)
                    return spawnPlayerDelegate(toks[1], Integer.parseInt(toks[2]), Integer.parseInt(toks[3]));
                return "usage: spawnplayer <player_id> <x> <y>";
            }

            private String spawnPlayerDelegate(String playerId, int x, int y) {
                xMain.shellLogic.serverScene.getThingMap("THING_PLAYER").remove(playerId);
                gPlayer newPlayer = new gPlayer(playerId, x, y);
                xMain.shellLogic.serverScene.getThingMap("THING_PLAYER").put(playerId, newPlayer);
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server",
                        String.format("cl_spawnplayer %s %d %d", playerId, x, y));
                ex("exec scripts/sv_handlespawnplayer " + playerId);
                return "spawned player " + playerId + " at " + x + " " + y;
            }
        });
        commands.put("cl_spawnplayer", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 3)
                    return clSpawnPlayerDelegate(toks[1], Integer.parseInt(toks[2]), Integer.parseInt(toks[3]));
                return "usage: spawnplayer <player_id> <x> <y>";
            }

            private String clSpawnPlayerDelegate(String playerId, int x, int y) {
                xMain.shellLogic.clientScene.getThingMap("THING_PLAYER").remove(playerId);
                gPlayer newPlayer = new gPlayer(playerId, x, y);
                nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
                nState clState = clStateMap.get(playerId);
                if(clState != null)
                    newPlayer.color = clState.get("color");
                newPlayer.setSpriteFromPath(eManager.getPath("animations/player_" + newPlayer.color + "/a03.png"));
                xMain.shellLogic.clientScene.getThingMap("THING_PLAYER").put(playerId, newPlayer);
                return "spawned player " + playerId + " at " + x + " " + y;
            }
        });
        commands.put("spawnpopup", new gDoable() {
            public String doCommand(String fullCommand) {
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "spawned popup";
            }
        });
        commands.put("cl_spawnpopup", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 2) {
                    String playerId = toks[1];
                    gPlayer p = xMain.shellLogic.getPlayerById(playerId);
                    if(p == null)
                        return "no player for id: " + playerId;
                    String id = eUtils.createId();
                    gPopup popup = new gPopup(
                            toks[2],
                            p.coords[0] + (int)(Math.random()*(p.dims[0]+1)),
                            p.coords[1] + (int)(Math.random()*(p.dims[1]+1))
                    );
                    xMain.shellLogic.clientScene.getThingMap("THING_POPUP").put(id, popup);
                    xMain.shellLogic.scheduledEvents.put(Long.toString(sSettings.gameTime + sSettings.popuplivetime),
                            new gDoable() {
                                public void doCommand() {
                                    xMain.shellLogic.clientScene.getThingMap("THING_POPUP").remove(id);
                                }
                            });
                    return "spawned popup " + popup.text + " for player_id " + playerId;
                }
                return "usage: cl_spawnpopup <player_id> <points>";
            }
        });
        commands.put("startserver", new gDoable() {
            public String doCommand(String fullCommand) {
                xMain.shellLogic.serverSimulationThread = new eGameLogicSimulation();
                new eGameSession(xMain.shellLogic.serverSimulationThread, sSettings.ratesimulation);
                xMain.shellLogic.serverNetThread = new eGameLogicServer();
                new eGameSession(xMain.shellLogic.serverNetThread, sSettings.rateserver);
                sSettings.IS_SERVER = true;
                return "new game started";
            }
        });
        commands.put("sumint", new gDoable() {
            public String doCommand(String fullCommand) {
                //usage: sumint $num1 $num2 //return result (use getres)
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "null";
                Number n1;
                Number n2;
                try {
                    n1 = NumberFormat.getInstance().parse(args[1]);
                    n2 = NumberFormat.getInstance().parse(args[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "null";
                }
                boolean n1d = n1 instanceof Double;
                boolean n1l = n1 instanceof Long;
                boolean n2d = n2 instanceof Double;
                boolean n2l = n2 instanceof Long;
                if(n1d || n2d)
                    return Integer.toString((int) ((double) n1 + (double) n2));
                else if(n1l || n2l)
                    return Long.toString((long) n1 + (long) n2);
                else
                    return Integer.toString((int) n1 + (int) n2);
            }
        });
        commands.put("sumlong", new gDoable() {
            public String doCommand(String fullCommand) {
                //usage: sumlong $num1 $num2
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "null";
                return Long.toString(Long.parseLong(args[1]) + Long.parseLong(args[2]));
            }
        });
        commands.put("svarlist", new gDoable() {
            public String doCommand(String fullCommand) {
                TreeMap<String, gArg> sorted = new TreeMap<>(xMain.shellLogic.serverVars.args);
                return sorted.toString();
            }
        });
        commands.put("gte", new gDoable() {
            //usage: gte $res $val // return 1 if res >= val
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "0";
                String tk = args[1];
                String tv = args[2];
                Number n1 = null;
                Number n2 = null;
                try {
                    n1 = NumberFormat.getInstance().parse(tk);
                    n2 = NumberFormat.getInstance().parse(tv);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                boolean n1d = n1 instanceof Double;
                boolean n1l = n1 instanceof Long;
                boolean n2d = n2 instanceof Double;
                boolean n2l = n2 instanceof Long;
                if(n1d && n2d) {
                    if((double) n1 >= (double) n2)
                        return "1";
                }
                else if(n1l && n2d) {
                    if(Long.parseLong(tk) >= Double.parseDouble(tv))
                        return "1";
                }
                else if(n1d && n2l) {
                    if(Double.parseDouble(tk) >= Long.parseLong(tv))
                        return "1";
                }
                else if(n1l && n2l) {
                    if((long) n1 >= (long) n2)
                        return "1";
                }
                //default
                if(Double.parseDouble(tk) >= Double.parseDouble(tv))
                    return "1";
                return "0";
            }
        });
        commands.put("showscore", new gDoable() {
            public String doCommand(String fullCommand) {
                sSettings.showscore = true;
                return "show score";
            }
            public String undoCommand(String fullCommand) {
                sSettings.showscore = false;
                return "hide score";
            }
        });
        commands.put("testres", new gDoable() {
            //usage: testres $res $val <string that will exec if res == val>
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "0";
                return testResDelegate(args);
            }
        });
        commands.put("testresn", new gDoable() {
            //usage: testres $res $val <string that will exec if res == val>
            public String doCommand(String fullCommand) {
                String[] args = xMain.shellLogic.serverVars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "0";
                return testResNDelegate(args);
            }
        });
        commands.put("unbind", new gDoable() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 2)
                    return "cannot unbind";
                String k = toks[1];
                Integer kc = iKeyboard.getCodeForKey(k);
                if(kc == null)
                    return "cannot unbind";
                pressBinds.remove(kc);
                releaseBinds.remove(kc);
                return String.format("unbound %s", k);
            }
        });
        commands.put("zoom", new gDoable() {
            public String doCommand(String fullCommand) {
                if(sSettings.show_mapmaker_ui)
                    sSettings.zoomLevel = Math.min(1.5, sSettings.zoomLevel + 0.5);
                return "zoom in";
            }
            public String undoCommand(String fullCommand) {
                if(sSettings.show_mapmaker_ui)
                    sSettings.zoomLevel = Math.max(0.5, sSettings.zoomLevel - 0.5);
                return "zoom out";
            }
        });
    }

    public void logException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        ex("echo " + sStackTrace.split("\\n")[0]);
    }

    private void playerMoveDelegate(int dir) {
        gPlayer p = xMain.shellLogic.getUserPlayer();
        if(p != null) {
            if(dir == 0)
                p.mov0 = 1;
            else if(dir == 1)
                p.mov1 = 1;
            else if(dir == 2)
                p.mov2 = 1;
            else if(dir == 3)
                p.mov3 = 1;
        }
        else if(sSettings.show_mapmaker_ui)
            gCamera.move[dir] = 1;
    }

    private void playerStopMoveDelegate(int dir) {
        gPlayer p = xMain.shellLogic.getUserPlayer();
        if(p != null) {
            if(dir == 0)
                p.mov0 = 0;
            else if(dir == 1)
                p.mov1 = 0;
            else if(dir == 2)
                p.mov2 = 0;
            else if(dir == 3)
                p.mov3 = 0;
        }
        gCamera.move[dir] = 0;
    }

    private void putItemDelegate(String[] toks, gScene scene) {
        String itemTitle = toks[1];
        String itemId = toks[2];
        int iw = Integer.parseInt(ex("setvar " + itemTitle+"_dimw"));
        int ih = Integer.parseInt(ex("setvar " + itemTitle+"_dimh"));
        String isp = ex("setvar " + itemTitle + "_sprite");
        String isc = ex("setvar " + itemTitle + "_script");
        String newItemFlare = ex("setvar " + itemTitle + "_flare");
        gItem item = new gItem(itemId, itemTitle, Integer.parseInt(toks[3]), Integer.parseInt(toks[4]), iw, ih, isp);
        item.script = isc;
        item.flare = newItemFlare;
        scene.getThingMap("THING_ITEM").put(itemId, item);
        scene.getThingMap(item.type).put(itemId, item);
    }

    private void putBlockDelegate(String[] toks, gScene scene, String blockString, String blockid, String prefabid) {
        String rawX = toks[4];
        String rawY = toks[5];
        String width = toks[6];
        String height = toks[7];
        //args are x y w h (t m)
        String[] args = new String[toks.length - 4];
        args[0] = rawX;
        args[1] = rawY;
        args[2] = width;
        args[3] = height;
        if (args.length > 4) {
            args[4] = toks[8];
            args[5] = toks[9];
        }
        gThing newBlock = new gThing();
        newBlock.coords = new int[] {Integer.parseInt(args[0]), Integer.parseInt(args[1])};
        newBlock.dims = new int[] {Integer.parseInt(args[2]), Integer.parseInt(args[3])};
        newBlock.type = blockString;
        if(blockString.equals("BLOCK_CUBE")) {
            newBlock.toph = Integer.parseInt(args[4]);
            newBlock.wallh = Integer.parseInt(args[5]);
        }
        newBlock.id = blockid;
        newBlock.prefabId = prefabid;
        scene.getThingMap("THING_BLOCK").put(blockid, newBlock);
        scene.getThingMap(newBlock.type).put(blockid, newBlock);
    }

    private String setThingDelegate(String[] args, gScene scene) {
        String ttype = args[1];
        if(scene.getThingMap(ttype) == null)
            return "null";
        ConcurrentHashMap<String, gThing> thingMap = scene.getThingMap(ttype);
        if(args.length < 3)
            return thingMap.toString();
        String tid = args[2];
        if(!thingMap.containsKey(tid))
            return "null";
        gThing thing = thingMap.get(tid);
        if(args.length < 4)
            return thing.args.toString();
        String tk = args[3];
        if(args.length < 5) {
            if(thing.args.args.get(tk) == null)
                return "null";
            return thing.args.args.get(tk).getValue();
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 4; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        thing.args.put(tk, tv);
        return thing.args.args.get(tk).getValue();
    }


    private void deleteBlockDelegate(String[] toks, gScene scene) {
        String id = toks[1];
        if(scene.getThingMap("THING_BLOCK").containsKey(id)) {
            gThing blockToDelete = scene.getThingMap("THING_BLOCK").get(id);
            String type = blockToDelete.type;
            scene.getThingMap("THING_BLOCK").remove(id);
            scene.getThingMap(type).remove(id);
        }
    }

    private void deleteItemDelegate(String fullCommand, gScene scene) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(scene.getThingMap("THING_ITEM").containsKey(id)) {
                gItem itemToDelete = (gItem) scene.getThingMap("THING_ITEM").get(id);
                String type = itemToDelete.type;
                scene.getThingMap("THING_ITEM").remove(id);
                scene.getThingMap(type).remove(id);
            }
        }
    }

    private void deletePrefabDelegate(gScene scene, String prefabId) {
        for(String id : scene.getThingMapIds("THING_BLOCK")) {
            gThing block = scene.getThingMap("THING_BLOCK").get(id);
            if(!block.prefabId.equals(prefabId))
                continue;
            String type = block.type;
            scene.getThingMap("THING_BLOCK").remove(id);
            scene.getThingMap(type).remove(id);
        }
    }

    private String testResDelegate(String[] args) {
        StringBuilder esb = new StringBuilder();
        for(int i = 3; i < args.length; i++) {
            esb.append(" ").append(args[i]);
        }
        if(args[1].equalsIgnoreCase(args[2]))
            ex(esb.substring(1));
        return "1";
    }

    private String testResNDelegate(String[] args) {
        StringBuilder esb = new StringBuilder();
        for(int i = 3; i < args.length; i++) {
            esb.append(" ").append(args[i]);
        }
        if(!args[1].equalsIgnoreCase(args[2]))
            ex(esb.substring(1));
        return "1";
    }

    public String[] ex(String[] ss) {
        String[] rr = new String[ss.length];
        for(int i = 0; i < ss.length; i++) {
            rr[i] = ex(ss[i]);
        }
        return rr;
    }

    public String ex(String s) {
        try {
            String[] commandTokens = s.split(";");
            StringBuilder result = new StringBuilder();
            for (String com : commandTokens) {
                result.append(doCommand(com)).append(";");
            }
            String resultString = result.toString();
            return resultString.substring(0, resultString.length() - 1);
        }
        catch (Exception ee) {
            ee.printStackTrace();
            return "Exception caused by line: " + s;
        }
    }

    public static int charlimit() {
        return (int)((double)sSettings.width/new Font(dFonts.fontnameconsole, Font.PLAIN,
                dFonts.size *sSettings.height/sSettings.gamescale/2).getStringBounds("_",
                dFonts.fontrendercontext).getWidth());
    }

    public void debug(String s) {
        if(sSettings.clientDebug) {
            log(s);
            System.out.println(s);
        }
    }

    public void log(String s) {
        if(s.length() > charlimit()) {
            stringLines.add(s.substring(0, charlimit()));
            for(int i = charlimit(); i < s.length(); i+= charlimit()) {
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
            logException(e);
            e.printStackTrace();
        }
    }

    private String getResDelegate(gArgSet argSet, String fullCommand) {
        String[] args = argSet.parseScriptArgs(fullCommand);
        if(args.length < 2)
            return "null";
        String tk = args[1];
        if(args.length < 3) {
            if (!argSet.contains(tk))
                return "null";
            return argSet.get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 2; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        String res = ex(tv);
        argSet.put(tk, res);
        return argSet.get(tk);
    }

    public Integer getKeyCodeForComm(String comm) {
        if(comm.length() > 0) {
            if(comm.charAt(0) == '-') {
                for(Integer j : releaseBinds.keySet()) {
                    if(releaseBinds.get(j).equals(comm))
                        return j;
                }
            }
            for(Integer j : pressBinds.keySet()) {
                if(pressBinds.get(j).equals(comm))
                    return j;
            }
        }
        return -1;
    }

    public String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
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
            gDoable cp = commands.get(command);
            if (cp != null) {
                StringBuilder realcom = new StringBuilder();
                for(String arg : args) {
                    realcom.append(" ").append(arg);
                }
                String comstring = realcom.substring(1);
//                stringLines.add(String.format("console:~$ %s", comstring));
                String result = comstring.charAt(0) == '-' ? cp.undoCommand(comstring) : cp.doCommand(comstring);
//                if (result.length() > 0)
//                    stringLines.add(result);
//                linesToShowStart = Math.max(0, stringLines.size() - linesToShow);
                while (stringLines.size() > 1024) {
                    stringLines.remove(0);
                }
                while (previousCommands.size() > 32) {
                    previousCommands.remove(0);
                }
                return result;
            }
            else
                return "null";
        }
        return "";
    }
}
