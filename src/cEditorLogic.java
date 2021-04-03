import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class cEditorLogic {
    static Map<String,JMenu> menus = new HashMap<>();
    static Stack<cEditorLogicState> undoStateStack = new Stack<>(); //move top from here to tmp for undo
    static Stack<cEditorLogicState> redoStateStack = new Stack<>(); //move top from here to main for redo
    static cEditorLogicState state = new cEditorLogicState(50,50,0,0, gScene.THING_PREFAB,
            new gProp(gProps.TELEPORTER, 0, 0, 0, 0, 300, 300),
            new gFlare(0, 0, 300, 300, 255, 255, 255, 255, 0, 0, 0, 0),
            new gScene());
    static ArrayList<JCheckBoxMenuItem> prefabCheckboxMenuItems = new ArrayList<>();
    static ArrayList<JCheckBoxMenuItem> itemCheckBoxMenuItems = new ArrayList<>();

    public void refreshCheckBoxItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : prefabCheckboxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(cVars.get("newprefabname"))) {
                state.createObjCode = gScene.THING_PREFAB;
                checkBoxMenuItem.setSelected(true);
            }
        }
        for(JCheckBoxMenuItem checkBoxMenuItem : itemCheckBoxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(cVars.get("newitemname"))) {
                state.createObjCode = gScene.THING_ITEM;
                checkBoxMenuItem.setSelected(true);
            }
        }
    }

    public static void setupMapMakerWindow() {
        JMenuBar menubar = new JMenuBar();
        oDisplay.instance().frame.setJMenuBar(menubar);
        createNewMenu("File");
//        createNewMenu("Edit");
        createNewMenu("Parameters");
//        createNewMenu("Blocks");
//        createNewMenu("Collisions");
        createNewMenu("Prefabs");
        createNewMenu("Items");
//        createNewMenu("Props");
        createNewMenu("Flares");
        createNewMenu("Scene");
        createNewMenu("Settings");
//        createNewSubmenu("File", "New");
//        createNewSubmenu("Props","Create: " + gProps.titles[state.newProp.getInt("code")]);
        createNewSubmenu("Scene", "Game Mode: " +
                cGameMode.net_gamemode_texts[cVars.getInt("gamemode")]);
        createNewSubmenu("Scene", "Bot Behavior: " + cVars.get("botbehavior"));
        createNewSubmenu("Scene", "Map Execs: 0");
        createNewSubmenu("Parameters", "Snap-To: " + state.snapToX + "," + state.snapToY);
        createNewSubmenu("Parameters", "Create: " + gScene.getObjTitleForCode(state.createObjCode));
        createNewSubmenu(menus.get("Parameters").getItem(0).getText(),"Nearest X Coord");
        createNewSubmenu(menus.get("Parameters").getItem(0).getText(),"Nearest Y Coord");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveas = new JMenuItem("Save As...");
        JMenuItem exportasprefab = new JMenuItem("Export as Prefab");
        JMenuItem exit = new JMenuItem("Exit (ctrl+q)");
        JMenuItem newtopmap = new JMenuItem("New");
        JMenuItem showControls = new JMenuItem("Show Controls");
//        JMenuItem sceneProps = new JMenuItem("Scene Props");
        JMenuItem sceneFlares = new JMenuItem("Scene Flares");
        JMenuItem sceneBlocks = new JMenuItem("Scene Blocks");
        JMenuItem sceneCollisions = new JMenuItem("Scene Collisions");
        JMenuItem setCreatePropInts = new JMenuItem("Set New Prop Settings");
//        JMenuItem setSelectedPropInts = new JMenuItem("Edit Selected Prop Settings");
        JMenuItem setCreateFlareDims = new JMenuItem("Set New Flare Dimensions");
        JMenuItem setSelectedFlareDims = new JMenuItem("Edit Selected Flare Dimensions");
        JMenuItem editorUndo = new JMenuItem("Undo (ctrl+z)");
        JMenuItem editorRedo = new JMenuItem("Redo (ctrl+shift+z)");
        JMenuItem setSVarsEditor = new JMenuItem("Set SVars");

        menus.get("File").add(newtopmap);
        menus.get("File").add(open);
//        menus.get("File").add(save);
        menus.get("File").add(saveas);
        menus.get("File").add(exportasprefab);
        menus.get("File").add(exit);
//        menus.get("New").add(newtopmap);
//        menus.get("Edit").add(editorUndo);
//        menus.get("Edit").add(editorRedo);
        menus.get("Scene").add(sceneBlocks);
        menus.get("Scene").add(sceneCollisions);
//        menus.get("Scene").add(sceneProps);
        menus.get("Scene").add(sceneFlares);
//        menus.get("Props").add(setCreatePropInts);
//        menus.get("Props").add(setSelectedPropInts);
        menus.get("Flares").add(setCreateFlareDims);
        menus.get("Flares").add(setSelectedFlareDims);
        menus.get("Settings").add(showControls);
        menus.get("Settings").add(setSVarsEditor);

        newtopmap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(xCon.getInt("e_showlossalert") <= 0) {
                    xCon.ex("load");
                }
            }
        });

        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("e_openfile");
            }
        });

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(eManager.currentMap.wasLoaded < 1)
                    xCon.ex("e_saveas");
                else
                    xCon.ex("e_save");
            }
        });

        saveas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("e_saveas");
            }
        });

        exportasprefab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("exportasprefab");
            }
        });

        //fill prefabs menu
        for(String prefabname : eManager.prefabSelection) {
            JCheckBoxMenuItem prefabmenuitem = new JCheckBoxMenuItem(prefabname);
            if(prefabmenuitem.getText().equals(cVars.get("newprefabname"))) {
                prefabmenuitem.setSelected(true);
            }
            prefabmenuitem.addActionListener(e -> {
                cVars.put("newprefabname", prefabname);
                for(JCheckBoxMenuItem checkBoxMenuItem : prefabCheckboxMenuItems) {
                    checkBoxMenuItem.setSelected(false);
                    if(checkBoxMenuItem.getText().equals(cVars.get("newprefabname"))) {
                        state.createObjCode = gScene.THING_PREFAB;
                        checkBoxMenuItem.setSelected(true);
                    }
                }
            });
            prefabCheckboxMenuItems.add(prefabmenuitem);
            menus.get("Prefabs").add(prefabmenuitem);
        }
        //fill items menu
        for(String itemname: gItemFactory.instance().itemLoadMap.keySet()) {
            JCheckBoxMenuItem itemMenuItem = new JCheckBoxMenuItem(itemname);
            if(itemMenuItem.getText().equals(cVars.get("newitemname"))) {
                itemMenuItem.setSelected(true);
            }
            itemMenuItem.addActionListener(e -> {
                cVars.put("newitemname", itemname);
                for(JCheckBoxMenuItem checkBoxMenuItem : itemCheckBoxMenuItems) {
                    checkBoxMenuItem.setSelected(false);
                    if(checkBoxMenuItem.getText().equals(cVars.get("newitemname"))) {
                        state.createObjCode = gScene.THING_ITEM;
                        checkBoxMenuItem.setSelected(true);
                    }
                }
            });
            itemCheckBoxMenuItems.add(itemMenuItem);
            menus.get("Items").add(itemMenuItem);
        }

        addConsoleActionToJMenuItem(exit,"quit");
        addConsoleActionToJMenuItem(setCreatePropInts,"e_newprop");
//        addConsoleActionToJMenuItem(setSelectedPropInts,"e_setselectedprop");
        addConsoleActionToJMenuItem(setCreateFlareDims,"e_newflare");
        addConsoleActionToJMenuItem(setSelectedFlareDims,"e_setselectedflare");
        addConsoleActionToJMenuItem(showControls,"e_showcontrols");
//        addConsoleActionToJMenuItem(sceneProps,"e_showprops");
        addConsoleActionToJMenuItem(sceneFlares,"e_showflares");
        addConsoleActionToJMenuItem(sceneBlocks,"showblocks");
        addConsoleActionToJMenuItem(sceneCollisions,"showcollisions");
        addConsoleActionToJMenuItem(editorUndo,"e_undo");
        addConsoleActionToJMenuItem(editorRedo,"-e_undo");
        addConsoleActionToJMenuItem(setSVarsEditor,"e_setsvars");

        addSnapToMenuItem("Nearest X Coord","1");
        addSnapToMenuItem("Nearest X Coord","5");
        addSnapToMenuItem("Nearest X Coord","10");
        addSnapToMenuItem("Nearest X Coord","25");
        addSnapToMenuItem("Nearest X Coord","50");
        addSnapToMenuItem("Nearest X Coord","100");
        addSnapToMenuItem("Nearest Y Coord","1");
        addSnapToMenuItem("Nearest Y Coord","5");
        addSnapToMenuItem("Nearest Y Coord","10");
        addSnapToMenuItem("Nearest Y Coord","25");
        addSnapToMenuItem("Nearest Y Coord","50");
        addSnapToMenuItem("Nearest Y Coord","100");

        for(String s : new String[]{"THING_PREFAB", "THING_ITEM", "THING_FLARE"}){
            JMenuItem newmenuitem = new JMenuItem(s);
            newmenuitem.addActionListener(e -> {
                state.createObjCode = gScene.getObjCodeForTitle(s);
                menus.get("Parameters").getItem(1).setText("Create: "+gScene.getObjTitleForCode(state.createObjCode));
            });
            menus.get("Parameters").getItem(1).add(newmenuitem);
        }
        for(int i = 0; i < cGameMode.net_gamemode_texts.length; i++) {
            JMenuItem newmenuitem = new JMenuItem(cGameMode.net_gamemode_texts[i]);
            newmenuitem.addActionListener(e -> {
                for(int j = 0; j < cGameMode.net_gamemode_texts.length; j++) {
                    if(cGameMode.net_gamemode_texts[j].equals(newmenuitem.getText()))
                        cVars.putInt("gamemode", j);
                }
                menus.get("Scene").getItem(0).setText("Game Mode: "
                        + cGameMode.net_gamemode_texts[cVars.getInt("gamemode")]);
            });
            menus.get("Scene").getItem(0).add(newmenuitem);
        }
        JMenuItem addexecmenuitem = new JMenuItem("Add New Exec");
        JMenuItem showexecsmenuitem = new JMenuItem("View All");
        addConsoleActionToJMenuItem(showexecsmenuitem, "e_showexecs");
        addConsoleActionToJMenuItem(addexecmenuitem, "-e_showexecs");
        menus.get("Scene").getItem(2).add(addexecmenuitem);
        menus.get("Scene").getItem(2).add(showexecsmenuitem);
        for(String s : cBotsLogic.behaviors()) {
            JMenuItem newmenuitem = new JMenuItem(s);
            newmenuitem.addActionListener(e -> {
                String b = "";
                for(String ss : cBotsLogic.behaviors()) {
                    if(ss.equals(newmenuitem.getText())) {
                        b = ss;
                        cVars.put("botbehavior", b);
                    }
                }
                menus.get("Scene").getItem(1).setText("Bot Behavior: " + b);
            });
            menus.get("Scene").getItem(1).add(newmenuitem);
        }
    }

    private static void createNewMenu(String title) {
        JMenu newmenu = new JMenu(title);
        menus.put(title,newmenu);
        oDisplay.instance().frame.getJMenuBar().add(newmenu);
    }

    private static void createNewSubmenu(String title, String subtitle) {
        JMenu newmenu = new JMenu(subtitle);
        menus.put(subtitle,newmenu);
        menus.get(title).add(newmenu);
    }

    private static void addSnapToMenuItem(String menutitle, String title) {
        JMenuItem newmenuitem = new JMenuItem(title);
        newmenuitem.addActionListener(e -> {
            if(menutitle.contains("Nearest X Coord"))
                state.snapToX = Integer.parseInt(title);
            else if(menutitle.contains("Nearest Y Coord"))
                state.snapToY = Integer.parseInt(title);
            menus.get("Parameters").getItem(0).setText(String.format("Snap-To: %d,%d",
                    state.snapToX, state.snapToY));
        });
        menus.get(menutitle).add(newmenuitem);
    }

    private static void addConsoleActionToJMenuItem(JMenuItem item, String fullCommand) {
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex(fullCommand);
            }
        });
    }

    public static void setEditorState(cEditorLogicState newstate) {
        state.snapToX = newstate.snapToX;
        state.snapToY = newstate.snapToY;
        state.selectedPropId = newstate.selectedPropId;
        state.selectedFlareTag = newstate.selectedFlareTag;
        state.createObjCode = newstate.createObjCode;
        state.newProp = newstate.newProp;
        state.newFlare = newstate.newFlare;
        eManager.currentMap.scene = newstate.mapScene;
    }

    public static cEditorLogicState getEditorState() {
        return new cEditorLogicState(state.snapToX, state.snapToY, state.selectedPropId, state.selectedFlareTag,
                state.createObjCode, state.newProp, state.newFlare, eManager.currentMap.scene.copy());
    }
}
