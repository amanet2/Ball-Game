import javafx.scene.media.AudioClip;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.Cursor;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
                    ex("playsound sounds/tap.wav");
                    uiMenus.menuSelection[uiMenus.selectedMenu].items[
                            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem].doItem();
                }
                return "1";
            }
        });
        commands.put("addcom", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcom can only be used by active server";
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "usage: addcom <command to execute>";
                StringBuilder act = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                cServerLogic.netServerThread.addNetCmd(actStr);
                return "server net com: " + actStr;
            }
        });
        commands.put("addcomi", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcomi can only be used by the host";
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: addcomi <ids to ignore separated by any token except ',' > <string>";
                String ignoreId = args[1];
                StringBuilder act = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                cServerLogic.netServerThread.addIgnoringNetCmd(ignoreId, actStr);
                return "server net com ignoring: " + actStr;
            }
        });
        commands.put("addcomx", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "addcomx can only be used by active server";
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: addcomx <exclusive id> <string>";
                String exlusiveId = args[1];
                StringBuilder act = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String actStr = act.substring(1);
                cServerLogic.netServerThread.addNetCmd(exlusiveId, actStr);
                return "server net com exclusive: " + actStr;
            }
        });
        commands.put("bind", new xCom() {
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
                        return "";
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
                        return "";
                    }
                }
                return "cannot bindrelease ";
            }
        });
        commands.put("changemap", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 2)
                    return "usage: changemap <path_to_mapfile>";
                String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
                cServerLogic.changeMap(mapPath);
                return "";
            }
        });
        commands.put("changemaprandom", new xCom() {
            public String doCommand(String fullCommand) {
                if(eManager.mapsFileSelection.length < 1)
                    return "no maps found for changemap (random)";
                int rand = (int)(Math.random()*eManager.mapsFileSelection.length);
                while(rand == eManager.mapSelectionIndex) {
                    rand = (int)(Math.random()*eManager.mapsFileSelection.length);
                }
                cServerLogic.changeMap("maps/" + eManager.mapsFileSelection[rand]);
                eManager.mapSelectionIndex = rand;
                return "changed map (random)";
            }
        });
        commands.put("chat", new xCom() {
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
        commands.put("cl_clearthingmappreview", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 1) {
                    String thing_title = toks[1];
                    if(uiEditorMenus.previewScene.objectMaps.containsKey(thing_title))
                        uiEditorMenus.previewScene.objectMaps.put(thing_title, new LinkedHashMap<>());
                }
                for(String thing_title : uiEditorMenus.previewScene.objectMaps.keySet()) {
                    uiEditorMenus.previewScene.objectMaps.get(thing_title).clear();
                }
                return "usage: cl_clearthingmappreview";
            }
        });
        commands.put("clientlist", new xCom() {
            public String doCommand(String fullCommand) {
                StringBuilder s = new StringBuilder();
                nStateMap svMap = new nStateMap(cServerLogic.netServerThread.masterStateSnapshot);
                for(String k : svMap.keys()) {
                    s.append(String.format("%s%s/%s,", k.equals(uiInterface.uuid) ? "*": "",
                            svMap.get(k).get("name"), k));
                }
                return s.substring(0, s.length()-1);
            }
        });
        commands.put("commandlist", new xCom() {
            public String doCommand(String fullCommand) {
                TreeSet<String> sorted = new TreeSet<>(commands.keySet());
                return sorted.toString();
            }
        });
        commands.put("console", new xCom() {
            public String doCommand(String fullCommand) {
                uiInterface.inconsole = !uiInterface.inconsole;
                return "console";
            }
        });
        commands.put("constr", new xCom() {
            //concatenate two or more strings
            //usage: constr <disparate elements to combine and return>
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                StringBuilder esb = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    esb.append(args[i]);
                }
                return esb.toString();
            }
        });
        commands.put("cvarlist", new xCom() {
            public String doCommand(String fullCommand) {
                TreeMap<String, gArg> sorted = new TreeMap<>(cClientLogic.vars.args);
                return sorted.toString();
            }
        });
        commands.put("damageplayer", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 2) {
                    String id = toks[1];
                    int dmg = Integer.parseInt(toks[2]);
                    String shooterid = "";
                    if(toks.length > 3)
                        shooterid = toks[3];
                    gPlayer player = cServerLogic.getPlayerById(id);
                    if(player != null) {
                        player.putInt("stockhp", player.getInt("stockhp") - dmg);
                        cServerLogic.netServerThread.masterStateMap.get(id).put("hp", player.get("stockhp"));
                        ex(String.format("exec scripts/sv_handledamageplayer %s %d %d", id, dmg, gTime.gameTime));
                        //handle death
                        if(player.getDouble("stockhp") < 1) {
                            //more server-side stuff
                            int dcx = player.getInt("coordx");
                            int dcy = player.getInt("coordy");
                            ex("deleteplayer " + id);
                            if(shooterid.length() < 1)
                                shooterid = "null";
                            ex("exec scripts/sv_handledestroyplayer " + id + " " + shooterid);
                            int animInd = gAnimations.ANIM_EXPLOSION_REG;
                            String colorName = cServerLogic.netServerThread.masterStateMap.get(id).get("color");
                            if(gAnimations.colorNameToExplosionAnimMap.containsKey(colorName))
                                animInd = gAnimations.colorNameToExplosionAnimMap.get(colorName);
                            ex(String.format("addcomi server cl_spawnanimation %d %d %d", animInd, dcx, dcy));
                            ex(String.format("scheduleevent %d respawnnetplayer %s",
                                    gTime.gameTime + cServerLogic.respawnwaittime, id));
                        }
                        return id + " took " + dmg + " dmg from " + shooterid;
                    }
                }
                return "usage: damageplayer <player_id> <dmg_amount> <optional-shooter_id>";
            }
        });
        commands.put("deleteblock", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 2)
                    return "usage: deleteblock <id>";
                deleteBlockDelegate(toks, cServerLogic.scene);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "deleted block";
            }
        });
        commands.put("cl_deleteblock", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1)
                    deleteBlockDelegate(toks, cClientLogic.scene);
                return "usage: cl_deleteblock <id>";
            }
        });
        commands.put("deleteitem", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1) {
                    String id = toks[1];
                    if(cServerLogic.scene.getThingMap("THING_ITEM").containsKey(id)) {
                        gItem itemToDelete = (gItem) cServerLogic.scene.getThingMap("THING_ITEM").get(id);
                        String type = itemToDelete.get("type");
                        cServerLogic.scene.getThingMap("THING_ITEM").remove(id);
                        cServerLogic.scene.getThingMap(type).remove(id);
                        cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_"+fullCommand);
                    }
                }
                return "usage: deleteitem <id>";
            }
        });
        commands.put("cl_deleteitem", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1) {
                    String id = toks[1];
                    if(cClientLogic.scene.getThingMap("THING_ITEM").containsKey(id)) {
                        gItem itemToDelete = (gItem) cClientLogic.scene.getThingMap("THING_ITEM").get(id);
                        String type = itemToDelete.get("type");
                        cClientLogic.scene.getThingMap("THING_ITEM").remove(id);
                        cClientLogic.scene.getThingMap(type).remove(id);
                    }
                }
                return "usage: deleteitem <id>";
            }
        });
        commands.put("deleteplayer", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1) {
                    String id = toks[1];
                    ex("exec scripts/sv_handledeleteplayer " + id);
                    cServerLogic.scene.getThingMap("THING_PLAYER").remove(id);
                    cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_"+fullCommand);
                }
                return "usage: deleteplayer <id>";
            }
        });
        commands.put("cl_deleteplayer", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1)
                    cClientLogic.scene.getThingMap("THING_PLAYER").remove(toks[1]);
                return "usage: deleteplayer <id>";
            }
        });
        commands.put("deleteprefab", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1) {
                    deletePrefabDelegate(cServerLogic.scene, toks[1]);
                    cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                }
                return "usage: deleteprefab <id>";
            }
        });
        commands.put("cl_deleteprefab", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1) {
                    deletePrefabDelegate(cClientLogic.scene, toks[1]);
                }
                return "usage: cl_deleteprefab <id>";
            }
        });
        commands.put("disconnect", new xCom() {
            public String doCommand(String fullCommand) {
                if(sSettings.IS_SERVER && sSettings.IS_CLIENT) {
                    cClientLogic.vars.put("maploaded", "0");
                    cClientLogic.netClientThread.disconnect();
                    ex("cl_load");
                    cServerLogic.netServerThread.disconnect();
                    cServerLogic.localGameThread.disconnect();
                }
                else if(sSettings.IS_CLIENT) {
                    cClientLogic.vars.put("maploaded", "0");
                    cClientLogic.netClientThread.disconnect();
                    ex("cl_load");
                }
                if (uiInterface.inplay)
                    ex("pause");
                return fullCommand;
            }
        });
        commands.put("echo", new xCom() {
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
                            if(cServerLogic.vars.contains(toks[j].substring(1)))
                                toks[j] = cServerLogic.vars.get(toks[j].substring(1));
                        }
                        lineArgCallTokens[i] = toks[0] + "#" + toks[1];
                    }
                    else if(lineArgCallTokens[i].startsWith("$")) {
                        String tokenKey = lineArgCallTokens[i];
                        if (cServerLogic.vars.contains(tokenKey.substring(1)))
                            lineArgCallTokens[i] = cServerLogic.vars.get(tokenKey.substring(1));
                    }
                }
                StringBuilder parsedStringBuilder = new StringBuilder();
                for(String lineArgtoken : lineArgCallTokens) {
                    parsedStringBuilder.append(" ").append(lineArgtoken);
                }
                String parsedCommand = parsedStringBuilder.substring(1);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + parsedCommand);
                return "FANOUT to clients: cl_" + parsedCommand;
            }
        });
        commands.put("cl_echo", new xCom() {
            public String doCommand(String fullCommand) {
                String rs = fullCommand.substring(fullCommand.indexOf(" ")+1);
                gMessages.addScreenMessage(rs);
                return rs;
            }
        });
        commands.put("e_changejoinip", new xCom() {
            public String doCommand(String fullCommand) {
                ex("chat Enter New IP Address");
                return "";
            }
        });
        commands.put("e_changejoinport", new xCom() {
            public String doCommand(String fullCommand) {
                ex("chat Enter New Port");
                return "";
            }
        });
        commands.put("e_changeplayername", new xCom() {
            public String doCommand(String fullCommand) {
                ex("chat Enter New Name");
                return "";
            }
        });
        commands.put("e_delthing", new xCom() {
            public String doCommand(String fullCommand) {
                if(cClientLogic.selectedPrefabId.length() > 0) {
                    cClientLogic.netClientThread.addNetCmd("deleteprefab " + cClientLogic.selectedPrefabId);
                    return "deleted prefab " + cClientLogic.selectedPrefabId;
                }
                if(cClientLogic.selecteditemid.length() > 0) {
                    cClientLogic.netClientThread.addNetCmd("deleteitem " + cClientLogic.selecteditemid);
                    return "deleted item " + cClientLogic.selecteditemid;
                }
                return "nothing to delete";
            }
        });
        commands.put("e_newmap", new xCom() {
            public String doCommand(String fullCommand) {
                ex("load;addcomi server cl_setvar maploaded 1");
                //reset game state
                gScoreboard.resetScoresMap();
                cServerLogic.voteSkipList = new ArrayList<>();
                return "";
            }
        });
        commands.put("e_openfile", new xCom() {
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
                            ex("joingame localhost " + cServerLogic.listenPort);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        cServerLogic.isLoadingFromHDD = true;
                        ex("changemap " + file.getPath());
                        uiEditorMenus.refreshGametypeCheckBoxMenuItems();
                        return "opening " + file.getPath();
                    }
                }
                return "";
            }
        });
        commands.put("e_openprefab", new xCom() {
            public String doCommand(String fullCommand) {
                if(sSettings.show_mapmaker_ui) {
                    JFileChooser fileChooser = new JFileChooser();
                    File workingDirectory = new File("prefabs");
                    fileChooser.setCurrentDirectory(workingDirectory);
                    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        cClientLogic.newprefabname = file.getName();
                        uiEditorMenus.newitemname = "";
                        return "set prefab from file " + file.getPath();
                    }
                }
                return "";
            }
        });
        commands.put("e_rotthing", new xCom() {
            public String doCommand(String fullCommand) {
                String newprefabname = cClientLogic.newprefabname;
                if (newprefabname.contains("_000"))
                    cClientLogic.newprefabname = newprefabname.replace("_000", "_090");
                else if (newprefabname.contains("_090"))
                    cClientLogic.newprefabname = newprefabname.replace("_090", "_180");
                else if (newprefabname.contains("_180"))
                    cClientLogic.newprefabname = newprefabname.replace("_180", "_270");
                else if (newprefabname.contains("_270"))
                    cClientLogic.newprefabname = newprefabname.replace("_270", "_000");
                if(newprefabname.contains("_000") || newprefabname.contains("_090") || newprefabname.contains("_180")
                        || newprefabname.contains("_270")) {
                    ex("cl_clearthingmappreview");
                    ex(String.format("cl_execpreview prefabs/%s 0 0 12500 5600", cClientLogic.newprefabname));
                }
                return "";
            }
        });
        commands.put("e_saveas", new xCom() {
            public String doCommand(String fullcommand) {
                JFileChooser fileChooser = new JFileChooser();
                uiEditorMenus.setFileChooserFont(fileChooser.getComponents());
                File workingDirectory = new File("maps");
                fileChooser.setCurrentDirectory(workingDirectory);
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filename = file.getName();
                    String foldername = file.getParent();
                    cClientLogic.scene.saveAs(filename, foldername);
                    return "saved " + file.getPath();
                }
                return "";
            }
        });
        commands.put("e_showlossalert", new xCom() {
            public  String doCommand(String fullcomm) {
                return Integer.toString(JOptionPane.showConfirmDialog(oDisplay.instance(),
                        "Any unsaved changes will be lost...", "Are You Sure?", JOptionPane.YES_NO_OPTION));
            }
        });
        commands.put("exec", new xCom() {
            public  String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 2)
                    return "usage: exec <script_id> <optional: args>";
                String scriptId = args[1];
                if(cServerLogic.isLoadingFromHDD) { //detect loading from openFile
                    System.out.println("LOADING MAP FROM HDD");
                    cServerLogic.isLoadingFromHDD = false;
                    ex("loadingscreen");
                    try (BufferedReader br = new BufferedReader(new FileReader(scriptId))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if(line.trim().length() > 0 && line.trim().charAt(0) != '#')
                                ex(line);
                        }
                    }
                    catch (Exception e) {
                        eLogging.logException(e);
                        e.printStackTrace();
                    }
                    ex("-loadingscreen");
                    return "loaded map " + scriptId;
                }
                gScript theScript = gScriptFactory.instance().getScript(scriptId);
                if(theScript == null)
                    return "no script found for: " + scriptId;
                if(args.length > 2) {
                    String[] callArgs = new String[args.length - 2];
                    for(int i = 0; i < callArgs.length; i++) {
                        callArgs[i] = args[i+2];
                        if(callArgs[i].startsWith("$")) {
                            String tokenKey = callArgs[i];
                            if(cServerLogic.vars.contains(tokenKey))
                                callArgs[i] = cServerLogic.vars.get(tokenKey);
                        }
                    }
                    theScript.callScript(callArgs);
                }
                else
                    theScript.callScript(new String[]{});
                return "script completed successfully";
            }
        });
        commands.put("cl_execpreview", new xCom() {
            public  String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 2)
                    return "usage: cl_execpreview <script_id> <optional: args>";
                String scriptId = args[1];
                gScript theScript = gScriptFactory.instance().getScript(scriptId);
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
        commands.put("exportasprefab", new xCom() {
            public String doCommand(String fullcommand) {
                JFileChooser fileChooser = new JFileChooser();
                File workingDirectory = new File("prefabs");
                fileChooser.setCurrentDirectory(workingDirectory);
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filename = file.getName();
                    cClientLogic.scene.exportasprefab(filename);
                    return "exported " + file.getPath();
                }
                return "";
            }
        });
        commands.put("fireweapon", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 2) {
                    String id = toks[1];
                    int weapon = Integer.parseInt(toks[2]);
                    gWeapons.fromCode(weapon).fireWeapon(cServerLogic.getPlayerById(id), cServerLogic.scene);
                    return id + " fired weapon " + weapon;
                }
                return "usage: fireweapon <player_id> <weapon_code>";
            }
        });
        commands.put("cl_fireweapon", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 2) {
                    String id = toks[1];
                    int weapon = Integer.parseInt(toks[2]);
                    gWeapons.fromCode(weapon).fireWeapon(cClientLogic.getPlayerById(id), cClientLogic.scene);
                    return id + " fired weapon " + weapon;
                }
                return "usage: cl_fireweapon <player_id> <weapon_code>";
            }
        });
        commands.put("foreach", new xCom() {
            //usage: foreach $var $start $end $incr <script to execute where $var is preloaded>
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 6)
                    return "usage: foreach $var $start $end $incr <script where $var is num>";
                String varname = args[1];
                int start = Integer.parseInt(args[2]);
                int end = Integer.parseInt(args[3]);
                int incr = Integer.parseInt(args[4]);
                for(int i = start; i <= end; i+=incr) {
                    ex(String.format("setvar %s %s", varname, i));
                    String[] cargs = cServerLogic.vars.parseScriptArgs(fullCommand);
                    String[] subarray = Arrays.stream(cargs, 5, cargs.length).toArray(String[]::new);
                    String es = String.join(" ", subarray);
                    ex(es);
                    cServerLogic.vars.args.remove(varname); //why is this needed here and not in foreachthing???
                }
                return "usage: foreach $var $start $end $incr <script where $var is num>";
            }
        });
        commands.put("foreachclient", new xCom() {
            //usage: foreachclient $id <script to execute where $id is preloaded>
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: foreachclient $id <script where $id is preloaded>";
                String varname = args[1];
                for(String id : cServerLogic.netServerThread.masterStateMap.keys()) {
                    ex(String.format("setvar %s %s", varname, id));
                    String[] cargs = cServerLogic.vars.parseScriptArgs(fullCommand);
                    StringBuilder esb = new StringBuilder();
                    for(int i = 2; i < cargs.length; i++) {
                        esb.append(" ").append(cargs[i]);
                    }
                    String es = esb.substring(1);
                    ex(es);
                    cServerLogic.vars.args.remove(varname); //why is this needed here and not in foreachthing???
                }
                return "usage: foreachclient $id <script to execute where $id is preloaded>";
            }
        });
        commands.put("foreachlong", new xCom() {
            //usage: foreachlong $var $start $end $incr <script to execute where $var is preloaded>
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 6)
                    return "usage: foreachlong $var $start $end $incr <script where $var is num>";
                String varname = args[1];
                long start = Long.parseLong(args[2]);
                long end = Long.parseLong(args[3]);
                int incr = Integer.parseInt(args[4]);
                for(long i = start; i <= end; i+=incr) {
                    ex(String.format("setvar %s %s", varname, i));
                    String[] cargs = cServerLogic.vars.parseScriptArgs(fullCommand);
                    String[] subarray = Arrays.stream(cargs, 5, cargs.length).toArray(String[]::new);
                    String es = String.join(" ", subarray);
                    ex(es);
                    cServerLogic.vars.args.remove(varname); //why is this needed here and not in foreachthing???
                }
                return "usage: foreachlong $var $start $end $incr <script where $var is num>";
            }
        });
        commands.put("foreachthing", new xCom() {
            //usage: foreachthing $var $THING_TYPE <script to execute where $var is preloaded>
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 4)
                    return "usage: foreach $var $THING_TYPE <script where $var is preloaded>";
                gScene scene = cServerLogic.scene;
                String varname = args[1];
                String thingtype = args[2];
                if(!scene.objectMaps.containsKey(thingtype))
                    return "no thing type in scene: " + thingtype;
                for(String id : scene.getThingMapIds(thingtype)) {
                    ex(String.format("setvar %s %s", varname, id));
                    String[] cargs = cServerLogic.vars.parseScriptArgs(fullCommand);
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
        commands.put("gamemode", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "only server can do 'gamemode'";
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return cServerLogic.vars.get("gamemode");
                String setmode = args[1];
                cServerLogic.vars.put("gamemode", setmode);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_setvar gamemode " + setmode);
                return "changed game mode to " + cServerLogic.vars.get("gamemode");
            }
        });
        commands.put("gametimemillis", new xCom() {
            public String doCommand(String fullCommand) {
                return Long.toString(gTime.gameTime);
            }
        });
        commands.put("getnewitemid", new xCom() {
            public String doCommand(String fullCommand) {
                int itemId = 0;
                for(String id : cServerLogic.scene.getThingMap("THING_ITEM").keySet()) {
                    if(itemId < Integer.parseInt(id))
                        itemId = Integer.parseInt(id);
                }
                return Integer.toString(itemId+1); //want to be the NEXT id
            }
        });
        commands.put("getrand", new xCom() {
            // usage: getrand $min $max
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "0";
                int start = Integer.parseInt(args[1]);
                int end = Integer.parseInt(args[2]);
                return Integer.toString(ThreadLocalRandom.current().nextInt(start, end + 1));
            }
        });
        commands.put("getrandclid", new xCom() {
            // usage: getrandclid
            public String doCommand(String fullCommand) {
                if(cServerLogic.netServerThread.masterStateMap.keys().size() < 1)
                    return "null";
                int randomClientIndex = (int) (Math.random() * cServerLogic.netServerThread.masterStateMap.keys().size());
                ArrayList<String> clientIds = new ArrayList<>(cServerLogic.netServerThread.masterStateMap.keys());
                return clientIds.get(randomClientIndex);
            }
        });
        commands.put("getrandthing", new xCom() {
            // usage: getrandthing $type
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                String type = args[1];
                if(!cServerLogic.scene.objectMaps.containsKey(type) || cServerLogic.scene.objectMaps.get(type).size() < 1)
                    return "null";
                List<String> keysAsArray = new ArrayList<>(cServerLogic.scene.objectMaps.get(type).keySet());
                return keysAsArray.get(ThreadLocalRandom.current().nextInt(0, keysAsArray.size()));
            }
        });
        commands.put("getres", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                String tk = args[1];
                if(args.length < 3) {
                    if (!cServerLogic.vars.contains(tk))
                        return "null";
                    return cServerLogic.vars.get(tk);
                }
                StringBuilder tvb = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    tvb.append(" ").append(args[i]);
                }
                String tv = tvb.substring(1);
                String res = ex(tv);
                cServerLogic.vars.put(tk, res);
                return cServerLogic.vars.get(tk);
            }
        });
        commands.put("cl_getres", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = cClientLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                String tk = args[1];
                if(args.length < 3) {
                    if (!cClientLogic.vars.contains(tk))
                        return "null";
                    return cClientLogic.vars.get(tk);
                }
                StringBuilder tvb = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    tvb.append(" ").append(args[i]);
                }
                String tv = tvb.substring(1);
                String res = ex(tv);
                cClientLogic.vars.put(tk, res);
                return cClientLogic.vars.get(tk);
            }
        });
        commands.put("getsnap", new xCom() {
            //usage: getsnap $id $key
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return cServerLogic.netServerThread.clientStateSnapshots.toString();
                String cid = args[1];
                if(!cServerLogic.netServerThread.clientStateSnapshots.containsKey(cid))
                    return "null";
                nStateMap clientSnapshot = new nStateMap(cServerLogic.netServerThread.clientStateSnapshots.get(cid));
                if(args.length < 3)
                    return clientSnapshot.toString();
                String tk = args[2];
                nState clientState = clientSnapshot.get(cid);
                if(!clientState.contains(tk))
                    return "null";
                return clientState.get(tk);
            }
        });
        commands.put("giveweapon", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: giveweapon <player_id> <weap_code>";
                String pid = args[1];
                String weap = args[2];
                String giveString = String.format("setthing THING_PLAYER %s weapon %s", pid, weap);
                cServerLogic.netServerThread.addNetCmd("server", giveString);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + giveString);
                ex(String.format("exec scripts/sv_handlegiveweapon %s %s", pid, weap));
                return "gave weapon " + weap + " to player " + pid;
            }
        });
        commands.put("givedecoration", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: givedecoration <player_id> <sprite_path>";
                String pid = args[1];
                String path = args[2];
                String giveString = String.format("setthing THING_PLAYER %s decorationsprite %s", pid, path);
                xCon.ex(giveString);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + giveString);
                return "applied decoration " + path + " to player " + pid;
            }
        });
        commands.put("givewaypoint", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: givewaypoint <player_id> <waypoint_string>";
                String pid = args[1];
                String val = args[2];
                String giveString = String.format("setthing THING_PLAYER %s waypoint %s", pid, val);
                xCon.ex(giveString);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + giveString);
                return String.format("Set waypoint '%s' for player %s", val, pid);
            }
        });
        commands.put("givepoint", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 2)
                    return "usage: givepoint <playerid> <points#optional>";
                String id = args[1];
                int score = 100;
                if(args.length > 2)
                    score = Integer.parseInt(args[2]);
                gScoreboard.addToScoreField(id, "score", score);
                String color = cServerLogic.netServerThread.masterStateMap.get(id).get("color");
                xCon.ex(String.format("spawnpopup %s +%d#%s", id, score, color));
                return "gave point to " + id;
            }
        });
        commands.put("givewin", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                String id = args[1];
                gScoreboard.incrementScoreFieldById(id, "wins");
                return id;
            }
        });
        commands.put("gobackui", new xCom() {
            public String doCommand(String fullCommand) {
                if(uiInterface.inplay)
                    ex("pause");
                else {
                    if(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0) {
                        if(!sSettings.IS_CLIENT) {
                            //offline mode do this
                            uiMenus.selectedMenu = uiMenus.MENU_QUIT;
                            ex("playsound sounds/splash.wav");
                        }
                        else
                            ex("pause");
                    }
                    else {
                        uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
                        ex("playsound sounds/splash.wav");
                    }
                }
                return fullCommand;
            }
        });
        commands.put("hostgame", new xCom() {
            public String doCommand(String fullCommand) {
                ex(new String[] {"newgame", "joingame localhost " + cServerLogic.listenPort, "pause"});
                return "hosting game on port " + cServerLogic.listenPort;
            }
        });
        commands.put("joingame", new xCom() {
            public String doCommand(String fullCommand) {
                cClientLogic.netClientThread = new eGameLogicClient();
                eGameSession clientSession = new eGameSession(cClientLogic.netClientThread, sSettings.rateclient);
                cClientLogic.netClientThread.setParentSession(clientSession);
                clientSession.start();
                sSettings.IS_CLIENT = true;
                return "joined game";
            }
        });
        commands.put("load", new xCom() {
            public String doCommand(String fullCommand) {
                //load the most basic blank map
                gTextures.clear();
                ex("setvar gamemode 0");
                cServerLogic.scene = new gScene();
                if(sSettings.IS_SERVER)
                    cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_load");
                return "";
            }
        });
        commands.put("loadingscreen", new xCom() {
            public String doCommand(String fullCommand) {
                if(sSettings.IS_SERVER)
                    cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_setvar maploaded 0");
                return "loading screen ON";
            }
            public String undoCommand(String fullCommand) {
                if(sSettings.IS_SERVER)
                    cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_setvar maploaded 1");
                return "loading screen OFF";
            }
        });
        commands.put("cl_load", new xCom() {
            public String doCommand(String fullCommand) {
                //load the most basic blank map
                gTextures.clear();
                ex("cl_setvar gamemode 0");
                cClientLogic.scene = new gScene();
                return "";
            }
        });
        commands.put("mouseleft", new xCom() {
            public String doCommand(String fullCommand) {
                if(oDisplay.instance().frame.hasFocus()) {
                    if (uiInterface.inplay) {
                        iMouse.holdingMouseLeft = true;
                    }
                    else {
                        if(sSettings.show_mapmaker_ui && cClientLogic.maploaded) {
                            int[] mc = uiInterface.getMouseCoordinates();
                            if(cClientLogic.newprefabname.length() > 0) {
                                int[] pfd = dMapmakerOverlay.getNewPrefabDims();
                                int w = pfd[0];
                                int h = pfd[1];
                                int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + gCamera.getX() - w / 2,
                                        uiEditorMenus.snapToX);
                                int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + gCamera.getY() - h / 2,
                                        uiEditorMenus.snapToY);
                                int bid = 0;
                                int pid = 0;
                                for(String id : cClientLogic.scene.getThingMap("THING_BLOCK").keySet()) {
                                    if(bid < Integer.parseInt(id))
                                        bid = Integer.parseInt(id);
                                    int tpid = cClientLogic.scene.getThingMap("THING_BLOCK").get(id).getInt("prefabid");
                                    if(pid < tpid)
                                        pid = tpid;
                                }
                                bid++; //want to be the _next_ id
                                pid++; //want to be the _next_ id
                                String cmd = String.format("exec prefabs/%s %d %d %d %d", cClientLogic.newprefabname, bid, pid, pfx, pfy);
                                cClientLogic.netClientThread.addNetCmd(cmd);
                                return "put prefab " + cClientLogic.newprefabname;
                            }
                            if(uiEditorMenus.newitemname.length() > 0) {
                                int iw = 300;
                                int ih = 300;
                                int ix = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + gCamera.getX() - iw/2,
                                        uiEditorMenus.snapToX);
                                int iy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + gCamera.getY() - ih/2,
                                        uiEditorMenus.snapToY);
                                String cmd = String.format("putitem %s %d %d %d",
                                        uiEditorMenus.newitemname, cClientLogic.getNewItemId(), ix, iy);
                                cClientLogic.netClientThread.addNetCmd(cmd);
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
        commands.put("newgame", new xCom() {
            public String doCommand(String fullCommand) {
                ex("startserver");
                int toplay = eManager.mapSelectionIndex;
                if(toplay < 0)
                    ex("newgamerandom");
                else
                    ex("changemap maps/" + eManager.mapsFileSelection[toplay]);
                return "new game started";
            }
        });
        commands.put("newgamerandom", new xCom() {
            public String doCommand(String fullCommand) {
                if(eManager.mapsFileSelection.length < 1)
                    return "no maps found for new game (random)";
                else if(eManager.mapsFileSelection.length > 1) {
                    int rand = (int)(Math.random()*eManager.mapsFileSelection.length);
                    while(rand == eManager.mapSelectionIndex) {
                        rand = (int)(Math.random()*eManager.mapsFileSelection.length);
                    }
                    eManager.mapSelectionIndex = rand;
                    ex("changemap maps/" + eManager.mapsFileSelection[rand]);
                }
                else {
                    eManager.mapSelectionIndex = 0;
                    ex("changemap maps/" + eManager.mapsFileSelection[0]);
                }
                return "new game (random) started";
            }
        });
        commands.put("pause", new xCom() {
            public String doCommand(String fullCommand) {
                uiInterface.inplay = !uiInterface.inplay;
                oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if(uiInterface.inplay) {
                    oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
                    if(sSettings.show_mapmaker_ui)
                        cClientLogic.netClientThread.addNetCmd("respawnnetplayer " + uiInterface.uuid);
                }
                else if(sSettings.show_mapmaker_ui)
                    cClientLogic.netClientThread.addNetCmd("deleteplayer " + uiInterface.uuid);
                return fullCommand;
            }
        });
        commands.put("playerdown", new xCom() {
            public String doCommand(String fullCommand) {
                playerMoveDelegate(1);
                return "player down";
            }
            public String undoCommand(String fullCommand) {
                playerStopMoveDelegate(1);
                return "stop player down";
            }
        });
        commands.put("playerleft", new xCom() {
            public String doCommand(String fullCommand) {
                playerMoveDelegate(2);
                return "player left";
            }
            public String undoCommand(String fullCommand) {
                playerStopMoveDelegate(2);
                return "stop player left";
            }
        });
        commands.put("playerright", new xCom() {
            public String doCommand(String fullCommand) {
                playerMoveDelegate(3);
                return "player right";
            }
            public String undoCommand(String fullCommand) {
                playerStopMoveDelegate(3);
                return "stop player right";
            }
        });
        commands.put("playerup", new xCom() {
            public String doCommand(String fullCommand) {
                playerMoveDelegate(0);
                return "player up";
            }
            public String undoCommand(String fullCommand) {
                playerStopMoveDelegate(0);
                return "stop player up";
            }
        });
        commands.put("playsound", new xCom() {
            final double sfxrange = 1800.0;
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length > 1 && sSettings.audioenabled) {
                    AudioClip soundClip = new AudioClip(getClass().getResource(eManager.getPath(toks[1])).toString());
                    if(toks.length > 2) {
                        int cycs = Integer.parseInt(toks[2]);
                        soundClip.setCycleCount(cycs);
                        if(cycs < 1)
                            soundClip.setCycleCount(AudioClip.INDEFINITE);
                    }
                    if(toks.length > 4) {
                        int diffx = gCamera.getX() + eUtils.unscaleInt(sSettings.width)/2-Integer.parseInt(toks[3]);
                        int diffy = gCamera.getY() + eUtils.unscaleInt(sSettings.height)/2-Integer.parseInt(toks[4]);
                        double balance = 0.0;
                        double ratio = Math.abs(diffx/(sfxrange-300));
                        if(diffx < 0)
                            balance = ratio;
                        else if(diffx > 0)
                            balance = -ratio;
                        soundClip.setBalance(balance);
                        soundClip.play((sfxrange/Math.sqrt(Math.pow((diffx),2)+Math.pow((diffy),2)))
                                *(cClientLogic.volume/100.0));
                        oAudio.instance().clips.add(soundClip);
                    }
                    else {
                        soundClip.play(cClientLogic.volume / 100.0);
                        oAudio.instance().clips.add(soundClip);
                    }
                }
                return fullCommand;
            }
        });
        commands.put("putblock", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length < 8)
                    return "usage: putblock <BLOCK_TITLE> <id> <pid> <x> <y> <w> <h>. opt: <t> <m> ";
                putBlockDelegate(toks, cServerLogic.scene, toks[1], toks[2], toks[3]);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "1";
            }
        });
        commands.put("cl_putblock", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length < 8)
                    return "usage: cl_putblock <BLOCK_TITLE> <id> <pid> <x> <y> <w> <h>. opt: <t> <m> ";
                putBlockDelegate(toks, cClientLogic.scene, toks[1], toks[2], toks[3]);
                return "1";
            }
        });
        commands.put("cl_putblockpreview", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length < 8)
                    return "usage:cl_putblockpreview <BLOCK_TITLE> <id> <pid> <x> <y> <w> <h>. opt: <t> <m> ";
                putBlockDelegate(toks, uiEditorMenus.previewScene, toks[1], toks[2], toks[3]);
                return "1";
            }
        });
        commands.put("putitem", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 5)
                    return "usage: putitem <ITEM_TITLE> <id> <x> <y>";
                putItemDelegate(toks, cServerLogic.scene);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "put item";
            }
        });
        commands.put("cl_putitem", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 5)
                    return "usage: cl_putitem <ITEM_TITLE> <id> <x> <y>";
                putItemDelegate(toks, cClientLogic.scene);
                return "cl_put item";
            }
        });
        commands.put("quit", new xCom() {
            public String doCommand(String fullCommand) {
                if(sSettings.show_mapmaker_ui && cClientLogic.maploaded) {
                    if(!ex("e_showlossalert").equals("0"))
                        return "";
                }
                xMain.shellLogic.disconnect();
                return "";
            }
        });
        commands.put("respawnnetplayer", new xCom() {
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
                    gThing randomSpawn = cServerLogic.scene.getThingMap("ITEM_SPAWNPOINT").get(randomSpawnId);
                    if(randomSpawn.get("occupied").equals("1"))
                        ex("respawnnetplayer " + toks[1]);
                    else {
                        tries = 0;
                        xCon.ex(String.format("spawnplayer %s %s %s", toks[1], randomSpawn.get("coordx"), randomSpawn.get("coordy")));
                    }
                }
                return fullCommand;
            }
        });
        commands.put("say", new xCom() {
            public String doCommand(String fullCommand) {
                if(fullCommand.length() > 0) {
                    String msg = fullCommand.substring(fullCommand.indexOf(" ")+1);
                    msg = cClientLogic.playerName + "#"+cClientLogic.playerColor+": " + msg;
                    cClientLogic.netClientThread.addNetCmd("echo " + msg);
                    gMessages.msgInProgress = "";
                }
                return fullCommand;
            }
        });
        commands.put("scheduleevent", new xCom() {
            public String doCommand(String fullCommand) {
                if(!sSettings.IS_SERVER)
                    return "scheduleevent can only be used by active server";
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "usage: scheduleevent <time> <string to execute>";
                StringBuilder act = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    act.append(" ").append(args[i]);
                }
                String timeToExec = args[1];
                String actStr = act.substring(1);
                synchronized (cServerLogic.timedEvents.events) {
                    cServerLogic.timedEvents.put(timeToExec,
                            new gTimeEvent() {
                                public void doCommand() {
                                    ex(actStr);
                                }
                            }
                    );
                }
                return "added time event @" + timeToExec + ": " + actStr;
            }
        });
        commands.put("selectdown", new xCom() {
            public String doCommand(String fullCommand) {
                ex("playerdown");
                if(!sSettings.show_mapmaker_ui && !uiInterface.inplay) {
                    uiInterface.hideMouseUI = true;
                    uiMenus.nextItem();
                }
                return fullCommand;
            }
        });
        commands.put("selectleft", new xCom() {
            public String doCommand(String fullCommand) {
                ex("playerleft");
                if((!sSettings.show_mapmaker_ui && !uiInterface.inplay) &&
                        !(uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu < 0))
                    uiMenus.selectedMenu = uiMenus.menuSelection[uiMenus.selectedMenu].parentMenu;
                return fullCommand;
            }
        });
        commands.put("selectright", new xCom() {
            public String doCommand(String fullCommand) {
                ex("playerright");
                if(!sSettings.show_mapmaker_ui && !uiInterface.inplay) {
                    uiMenus.menuSelection[uiMenus.selectedMenu].items[uiMenus.menuSelection[
                            uiMenus.selectedMenu].selectedItem].doItem();
                    ex("playsound sounds/splash.wav");
                }
                return fullCommand;
            }
        });
        commands.put("selectup", new xCom() {
            public String doCommand(String fullCommand) {
                ex("playerup");
                if(!sSettings.show_mapmaker_ui && !uiInterface.inplay) {
                    uiInterface.hideMouseUI = true;
                    uiMenus.prevItem();
                }
                return fullCommand;
            }
        });
        commands.put("setnstate", new xCom() {
            //usage: setnstate $id $key $value
            public String doCommand(String fullCommand) {
                nStateMap serverState = cServerLogic.netServerThread.masterStateMap;
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return serverState.toString();
                String pid = args[1];
                nState clientState = serverState.get(pid);
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
                clientState.put(tk, tv);
                return clientState.get(tk);
            }
        });
        commands.put("setplayercoords", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 4)
                    return "null";
                gPlayer p = cServerLogic.getPlayerById(args[1]);
                if(p == null)
                    return "null";
                p.put("coordx", args[2]);
                p.put("coordy", args[3]);
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return fullCommand;
            }
        });
        commands.put("cl_setplayercoords", new xCom() {
            public String doCommand(String fullCommand) {
                String[] args = fullCommand.split(" ");
                if(args.length < 4)
                    return "null";
                gPlayer p = cClientLogic.getPlayerById(args[1]);
                if(p == null)
                    return "null";
                p.put("coordx", args[2]);
                p.put("coordy", args[3]);
                return fullCommand;
            }
        });
        commands.put("setthing", new xCom() {
            //usage: setthing $THING_TYPE $id $key $val
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                return setThingDelegate(args, cServerLogic.scene);
            }
        });
        commands.put("cl_setthing", new xCom() {
            //usage cl_setthing $type $id $key $var
            public String doCommand(String fullCommand) {
                String[] args = cClientLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                return setThingDelegate(args, cClientLogic.scene);
            }
        });
        commands.put("setvar", new xCom() {
            public String doCommand(String fullCommand) {
                //usage setvar $key $val
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 2)
                    return "null";
                String tk = args[1];
                if(args.length < 3) {
                    if (!cServerLogic.vars.contains(tk))
                        return "null";
                    return cServerLogic.vars.get(tk);
                }
                StringBuilder tvb = new StringBuilder();
                for(int i = 2; i < args.length; i++) {
                    tvb.append(" ").append(args[i]);
                }
                String tv = tvb.substring(1);
                cServerLogic.vars.put(tk, tv);
                return cServerLogic.vars.get(tk);
            }
        });
        commands.put("cl_setvar", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if(toks.length < 2)
                    return "null";
                String tk = toks[1];
                if(toks.length < 3) {
                    if (!cClientLogic.vars.contains(tk))
                        return "null";
                    return cClientLogic.vars.get(tk);
                }
                StringBuilder tvb = new StringBuilder();
                for(int i = 2; i < toks.length; i++) {
                    tvb.append(" ").append(toks[i]);
                }
                String tv = tvb.substring(1);
                if(tv.charAt(0) == '$' && cClientLogic.vars.contains(tv.substring(1)))
                    cClientLogic.vars.put(tk, cClientLogic.vars.get(tv.substring(1)));
                else
                    cClientLogic.vars.put(tk, tv);
                return cClientLogic.vars.get(tk);
            }
        });
        commands.put("cl_spawnanimation", new xCom() {
            public String doCommand(String fullCommand) {
                if(sSettings.vfxenableanimations) {
                    String[] toks = fullCommand.split(" ");
                    if (toks.length > 3) {
                        int animcode = Integer.parseInt(toks[1]);
                        int x = Integer.parseInt(toks[2]);
                        int y = Integer.parseInt(toks[3]);
                        String aid = eUtils.createId();
                        cClientLogic.scene.getThingMap("THING_ANIMATION").put(aid,
                                new gAnimationEmitter(animcode, x, y));
                        gAnimation anim = gAnimations.animation_selection[animcode];
                        cClientLogic.timedEvents.put(
                                Long.toString(gTime.gameTime + anim.frames.length*anim.framerate), new gTimeEvent() {
                                    public void doCommand() {
                                        cClientLogic.scene.getThingMap("THING_ANIMATION").remove(aid);
                                    }
                                });
                        return "spawned animation " + animcode + " at " + x + " " + y;
                    }
                }
                return "usage: cl_spawnanimation <animation_code> <x> <y>";
            }
        });
        commands.put("spawnplayer", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 3) {
                    String playerId = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    spawnPlayerDelegate(playerId, x, y, cServerLogic.scene);
                    ex("exec scripts/sv_handlespawnplayer " + playerId);
                    cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                    return "spawned player " + playerId + " at " + x + " " + y;
                }
                return "usage: spawnplayer <player_id> <x> <y>";
            }

            private void spawnPlayerDelegate(String playerId, int x, int y, gScene sceneToStore) {
                sceneToStore.getThingMap("THING_PLAYER").remove(playerId);
                gPlayer newPlayer = new gPlayer(playerId, x, y);
                sceneToStore.getThingMap("THING_PLAYER").put(playerId, newPlayer);
            }
        });
        commands.put("cl_spawnplayer", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 3) {
                    String playerId = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    spawnPlayerDelegate(playerId, x, y, cClientLogic.scene);
                    return "spawned player " + playerId + " at " + x + " " + y;
                }
                return "usage: spawnplayer <player_id> <x> <y>";
            }

            private void spawnPlayerDelegate(String playerId, int x, int y, gScene sceneToStore) {
                sceneToStore.getThingMap("THING_PLAYER").remove(playerId);
                gPlayer newPlayer = new gPlayer(playerId, x, y);
                nStateMap clStateMap = new nStateMap(cClientLogic.netClientThread.clientStateSnapshot);
                if(clStateMap.contains(playerId)) {
                    newPlayer.put("color", clStateMap.get(playerId).get("color"));
                    newPlayer.setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a03.png",
                            clStateMap.get(playerId).get("color"))));
                }
                sceneToStore.getThingMap("THING_PLAYER").put(playerId, newPlayer);
            }
        });
        commands.put("spawnpopup", new xCom() {
            public String doCommand(String fullCommand) {
                cServerLogic.netServerThread.addIgnoringNetCmd("server", "cl_" + fullCommand);
                return "spawned popup";
            }
        });
        commands.put("cl_spawnpopup", new xCom() {
            public String doCommand(String fullCommand) {
                String[] toks = fullCommand.split(" ");
                if (toks.length > 2) {
                    gPlayer p = cClientLogic.getPlayerById(toks[1]);
                    if(p == null)
                        return "no player for id: " + toks[1];
                    String msg = toks[2];
                    String id = eUtils.createId();
                    cClientLogic.scene.getThingMap("THING_POPUP").put(id,
                            new gPopup(p.getInt("coordx") + (int)(Math.random()*(p.getInt("dimw")+1)),
                                    p.getInt("coordy") + (int)(Math.random()*(p.getInt("dimh")+1)),
                                    msg, 0.0));
                    cClientLogic.timedEvents.put(Long.toString(gTime.gameTime + sSettings.popuplivetime),
                            new gTimeEvent() {
                                public void doCommand() {
                                    cClientLogic.scene.getThingMap("THING_POPUP").remove(id);
                                }
                            });
                    return "spawned popup " + msg + " for player_id " + toks[1];
                }
                return "usage: cl_spawnpopup <player_id> <points>";
            }
        });
        commands.put("startserver", new xCom() {
            public String doCommand(String fullCommand) {
                cServerLogic.localGameThread = new eGameLogicSimulation();
                eGameSession localGameSession = new eGameSession(cServerLogic.localGameThread, sSettings.ratesimulation);
                cServerLogic.localGameThread.setParentSession(localGameSession);
                localGameSession.start();
                cServerLogic.netServerThread = new eGameLogicServer();
                eGameSession serverNetSession = new eGameSession(cServerLogic.netServerThread, sSettings.rateserver);
                cServerLogic.netServerThread.setParentSession(serverNetSession);
                sSettings.IS_SERVER = true;
                serverNetSession.start();
                return "new game started";
            }
        });
        commands.put("sumint", new xCom() {
            public String doCommand(String fullCommand) {
                //usage: sumint $num1 $num2 //return result (use getres)
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
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
        commands.put("sumlong", new xCom() {
            public String doCommand(String fullCommand) {
                //usage: sumlong $num1 $num2
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "null";
                return Long.toString(Long.parseLong(args[1]) + Long.parseLong(args[2]));
            }
        });
        commands.put("svarlist", new xCom() {
            public String doCommand(String fullCommand) {
                TreeMap<String, gArg> sorted = new TreeMap<>(cServerLogic.vars.args);
                return sorted.toString();
            }
        });
        commands.put("gte", new xCom() {
            //usage: gte $res $val // return 1 if res >= val
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
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
        commands.put("showscore", new xCom() {
            public String doCommand(String fullCommand) {
                dScreenMessages.showscore = true;
                return "show score";
            }
            public String undoCommand(String fullCommand) {
                dScreenMessages.showscore = false;
                return "hide score";
            }
        });
        commands.put("testres", new xCom() {
            //usage: testres $res $val <string that will exec if res == val>
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "0";
                return testResDelegate(args);
            }
        });
        commands.put("testresn", new xCom() {
            //usage: testres $res $val <string that will exec if res == val>
            public String doCommand(String fullCommand) {
                String[] args = cServerLogic.vars.parseScriptArgs(fullCommand);
                if(args.length < 3)
                    return "0";
                return testResNDelegate(args);
            }
        });
        commands.put("unbind", new xCom() {
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
        commands.put("zoom", new xCom() {
            public String doCommand(String fullCommand) {
                sSettings.zoomLevel = Math.min(1.5, sSettings.zoomLevel + 0.5);
                return "zoom in";
            }
            public String undoCommand(String fullCommand) {
                sSettings.zoomLevel = Math.max(0.5, sSettings.zoomLevel - 0.5);
                return "zoom out";
            }
        });
    }

    public static xCon instance() {
        if(instance == null)
            instance = new xCon();
        return instance;
    }

    private void playerMoveDelegate(int dir) {
        gPlayer p = cClientLogic.getUserPlayer();
        if(p != null)
            p.put("mov" + dir, "1");
        else if(sSettings.show_mapmaker_ui) {
            gCamera.put("mov" + dir, "1");
        }
    }

    private void playerStopMoveDelegate(int dir) {
        gPlayer p = cClientLogic.getUserPlayer();
        if(p != null)
            p.put("mov" + dir, "0");
        gCamera.put("mov" + dir, "0");
    }

    private void putItemDelegate(String[] toks, gScene scene) {
        String itemTitle = toks[1];
        String itemId = toks[2];
        int iw = Integer.parseInt(ex("setvar " + itemTitle+"_dimw"));
        int ih = Integer.parseInt(ex("setvar " + itemTitle+"_dimh"));
        String isp = ex("setvar " + itemTitle + "_sprite");
        String isc = ex("setvar " + itemTitle + "_script");
        String newItemFlare = ex("setvar " + itemTitle + "_flare");
        gItem item = new gItem(itemTitle, Integer.parseInt(toks[3]), Integer.parseInt(toks[4]), iw, ih,
                isp.equalsIgnoreCase("null") ? null : gTextures.getGScaledImage(eManager.getPath(isp),
                        iw, ih));
        item.put("script", isc);
        item.put("flare", newItemFlare);
        item.put("id", itemId);
        item.put("occupied", "0");
        scene.getThingMap("THING_ITEM").put(itemId, item);
        scene.getThingMap(item.get("type")).put(itemId, item);
    }

    private void putBlockDelegate(String[] toks, gScene scene, String blockString, String blockid, String prefabid) {
        gDoableThingReturn blockReturn = gBlockFactory.instance().blockLoadMap.get(blockString);
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
        gThing newBlock = blockReturn.getThing(args);
        newBlock.put("id", blockid);
        newBlock.put("prefabid", prefabid);
        scene.getThingMap("THING_BLOCK").put(blockid, newBlock);
        scene.getThingMap(newBlock.get("type")).put(blockid, newBlock);
    }

    private String setThingDelegate(String[] args, gScene scene) {
        String ttype = args[1];
        if(scene.getThingMap(ttype) == null)
            return "null";
        HashMap<String, gThing> thingMap = scene.getThingMap(ttype);
        if(args.length < 3)
            return thingMap.toString();
        String tid = args[2];
        if(!thingMap.containsKey(tid))
            return "null";
        gThing thing = thingMap.get(tid);
        if(args.length < 4)
            return thing.toString();
        String tk = args[3];
        if(args.length < 5) {
            if(thing.get(tk) == null)
                return "null";
            return thing.get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 4; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        thing.put(tk, tv);
        return thing.get(tk);
    }


    private void deleteBlockDelegate(String[] toks, gScene scene) {
        String id = toks[1];
        if(scene.getThingMap("THING_BLOCK").containsKey(id)) {
            gBlock blockToDelete = (gBlock) scene.getThingMap("THING_BLOCK").get(id);
            String type = blockToDelete.get("type");
            scene.getThingMap("THING_BLOCK").remove(id);
            scene.getThingMap(type).remove(id);
        }
    }

    private void deletePrefabDelegate(gScene scene, String prefabId) {
        for(String id : scene.getThingMapIds("THING_BLOCK")) {
            gBlock block = (gBlock) scene.getThingMap("THING_BLOCK").get(id);
            if(!block.isVal("prefabid", prefabId))
                continue;
            String type = block.get("type");
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

    public static String[] ex(String[] ss) {
        String[] rr = new String[ss.length];
        for(int i = 0; i < ss.length; i++) {
            rr[i] = ex(ss[i]);
        }
        return rr;
    }

    public static String ex(String s) {
        try {
            String[] commandTokens = s.split(";");
            StringBuilder result = new StringBuilder();
            for (String com : commandTokens) {
                result.append(instance().doCommand(com)).append(";");
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
                if(args[i].startsWith("$") && cServerLogic.vars.contains(args[i].substring(1)))
                    args[i] = cServerLogic.vars.get(args[i].substring(1));
                else if(args[i].startsWith("$") && cClientLogic.vars.contains(args[i].substring(1)))
                    args[i] = cClientLogic.vars.get(args[i].substring(1));
            }
            String command = args[0];
            if(command.startsWith("-"))
                command = command.substring(1);
            xCom cp = commands.get(command);
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
