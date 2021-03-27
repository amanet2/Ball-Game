import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class cEditorLogic {
    static Map<String,JMenu> menus = new HashMap<>();
    static Stack<cEditorLogicState> undoStateStack = new Stack<>(); //move top from here to tmp for undo
    static Stack<cEditorLogicState> redoStateStack = new Stack<>(); //move top from here to main for redo
    static cEditorLogicState state = new cEditorLogicState(30,30, "",
            new JMenuItem(""),0,0,0, gScene.THING_TILE,
            new gTile(0, 0, 1200, 1200,  100, 150, 1200, 100, 150, 100,
                    100, "none", "none", "none", 255),
            new gProp(gProps.TELEPORTER, 0, 0, 0, 0, 300, 300),
            new gFlare(0, 0, 300, 300, 255, 255, 255, 255, 0, 0, 0, 0),
            new gScene());

    public static void setupMapMakerWindow() {
        JMenuBar menubar = new JMenuBar();
        oDisplay.instance().frame.setJMenuBar(menubar);
        createNewMenu("File");
        createNewMenu("Edit");
        createNewMenu("Parameters");
        createNewMenu("Blocks");
        createNewMenu("Collisions");
        createNewMenu("Prefabs");
        createNewMenu("Tiles"); //getting rid of you... eventually
        createNewMenu("Props");
        createNewMenu("Flares");
        createNewMenu("Scene");
        createNewMenu("Settings");
        createNewSubmenu("File", "New");
        createNewSubmenu("Tiles", "Current Tile");
        createNewSubmenu("Tiles","Tile Selection");
        createNewSubmenu("Tiles", "Textures");
        createNewSubmenu("Props","Create: " + gProps.titles[state.newProp.getInt("code")]);
        createNewSubmenu("Textures", "Current Textures");
        createNewSubmenu("Textures", "top_textures");
        createNewSubmenu("Textures", "wall_textures");
        createNewSubmenu("Textures", "floor_textures");
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
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem newtopmap = new JMenuItem("Map/Prefab");
        JMenuItem showControls = new JMenuItem("Show Controls");
        JMenuItem sceneObjs = new JMenuItem("Scene Tiles");
        JMenuItem sceneProps = new JMenuItem("Scene Props");
        JMenuItem sceneFlares = new JMenuItem("Scene Flares");
        JMenuItem setNewTileDimensions = new JMenuItem("Set New Tile Dimensions");
        JMenuItem setSelectedTileDimensions = new JMenuItem("Set Selected Tile Dimensions");
        JMenuItem transferThingDimensions = new JMenuItem("Inject Copied Dimensions Into Selected Tile");
        JMenuItem moveTileDown = new JMenuItem("Move Selected Tile Down 1 Layer");
        JMenuItem moveTileUp = new JMenuItem("Move Selected Tile Up 1 Layer");
        JMenuItem copySelectedThingDimensions = new JMenuItem("Copy Selected Tile Dimensions");
        JMenuItem setCreatePropInts = new JMenuItem("Set New Prop Settings");
        JMenuItem setSelectedPropInts = new JMenuItem("Edit Selected Prop Settings");
        JMenuItem setCreateFlareDims = new JMenuItem("Set New Flare Dimensions");
        JMenuItem setSelectedFlareDims = new JMenuItem("Edit Selected Flare Dimensions");
        JMenuItem editorUndo = new JMenuItem("Undo (ctrl+z)");
        JMenuItem editorRedo = new JMenuItem("Redo (ctrl+shift+z)");
        JMenuItem setSVarsEditor = new JMenuItem("Set SVars");

        menus.get("File").add(open);
        menus.get("File").add(save);
        menus.get("File").add(saveas);
        menus.get("File").add(exportasprefab);
        menus.get("File").add(exit);
        menus.get("New").add(newtopmap);
        menus.get("Edit").add(editorUndo);
        menus.get("Edit").add(editorRedo);
        menus.get("Scene").add(sceneObjs);
        menus.get("Scene").add(sceneProps);
        menus.get("Scene").add(sceneFlares);
        menus.get("Current Tile").add(state.selectedTileMenuItem);
        menus.get("Current Textures").add(state.selectedTextureMenuItems[0]);
        menus.get("Current Textures").add(state.selectedTextureMenuItems[1]);
        menus.get("Current Textures").add(state.selectedTextureMenuItems[2]);
        menus.get("Tiles").add(setNewTileDimensions);
        menus.get("Tiles").add(setSelectedTileDimensions);
        menus.get("Tiles").add(copySelectedThingDimensions);
        menus.get("Tiles").add(transferThingDimensions);
        menus.get("Tiles").add(moveTileDown);
        menus.get("Tiles").add(moveTileUp);
        menus.get("Props").add(setCreatePropInts);
        menus.get("Props").add(setSelectedPropInts);
        menus.get("Flares").add(setCreateFlareDims);
        menus.get("Flares").add(setSelectedFlareDims);
        menus.get("Settings").add(showControls);
        menus.get("Settings").add(setSVarsEditor);

        newtopmap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("load");
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

        addConsoleActionToJMenuItem(exit,"quit");
        addConsoleActionToJMenuItem(setNewTileDimensions,"e_newtile");
        addConsoleActionToJMenuItem(setSelectedTileDimensions,"e_setselectedtile");
        addConsoleActionToJMenuItem(copySelectedThingDimensions,"e_copytile");
        addConsoleActionToJMenuItem(transferThingDimensions,"e_pastetile");
        addConsoleActionToJMenuItem(moveTileDown,"e_tiledown");
        addConsoleActionToJMenuItem(moveTileUp,"e_tileup");
        addConsoleActionToJMenuItem(setCreatePropInts,"e_newprop");
        addConsoleActionToJMenuItem(setSelectedPropInts,"e_setselectedprop");
        addConsoleActionToJMenuItem(setCreateFlareDims,"e_newflare");
        addConsoleActionToJMenuItem(setSelectedFlareDims,"e_setselectedflare");
        addConsoleActionToJMenuItem(showControls,"e_showcontrols");
        addConsoleActionToJMenuItem(sceneObjs,"e_showtiles");
        addConsoleActionToJMenuItem(sceneProps,"e_showprops");
        addConsoleActionToJMenuItem(sceneFlares,"e_showflares");
        addConsoleActionToJMenuItem(editorUndo,"e_undo");
        addConsoleActionToJMenuItem(editorRedo,"-e_undo");
        addConsoleActionToJMenuItem(setSVarsEditor,"e_setsvars");

        addPrefMenuItem("Nearest X Coord","1");
        addPrefMenuItem("Nearest X Coord","5");
        addPrefMenuItem("Nearest X Coord","10");
        addPrefMenuItem("Nearest X Coord","15");
        addPrefMenuItem("Nearest X Coord","30");
        addPrefMenuItem("Nearest Y Coord","1");
        addPrefMenuItem("Nearest Y Coord","5");
        addPrefMenuItem("Nearest Y Coord","10");
        addPrefMenuItem("Nearest Y Coord","15");
        addPrefMenuItem("Nearest Y Coord","30");

        for(String s : gTiles.tile_selection) {
            JMenuItem newmenuitem = new JMenuItem(s);
            newmenuitem.addActionListener(e -> {
                state.selectedTitle = s;
                state.selectedTileMenuItem.setText(state.selectedTitle);
                gTiles.setCreateDimsForTile(s);
            });
            menus.get("Tile Selection").add(newmenuitem);
        }
        for(String s : gTextures.selection_top) {
            addTextureMenuItem("top_textures",s);
        }
        for(String s : gTextures.selection_wall) {
            addTextureMenuItem("wall_textures",s);
        }
        for(String s : gTextures.selection_floor) {
            addTextureMenuItem("floor_textures",s);
        }
        for(String s : gProps.titles) {
            JMenuItem newmenuitem = new JMenuItem(s);
            newmenuitem.addActionListener(e -> {
                state.newProp.putInt("code", gProps.getCodeForTitle(s));
                menus.get("Props").getItem(0).setText("Create: " + s);
            });
            menus.get("Props").getItem(0).add(newmenuitem);
        }
        for(String s : new String[]{"THING_TILE", "THING_PROP", "THING_FLARE"}){
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
                menus.get("Scene").getItem(1).setText("Game Mode: "
                        + cGameMode.net_gamemode_texts[cVars.getInt("gamemode")]);
            });
            menus.get("Scene").getItem(1).add(newmenuitem);
        }
        JMenuItem addexecmenuitem = new JMenuItem("Add New Exec");
        JMenuItem showexecsmenuitem = new JMenuItem("View All");
        addConsoleActionToJMenuItem(showexecsmenuitem, "e_showexecs");
        addConsoleActionToJMenuItem(addexecmenuitem, "-e_showexecs");
        menus.get("Scene").getItem(3).add(addexecmenuitem);
        menus.get("Scene").getItem(3).add(showexecsmenuitem);
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
                menus.get("Scene").getItem(2).setText("Bot Behavior: " + b);
            });
            menus.get("Scene").getItem(2).add(newmenuitem);
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

    private static void addTextureMenuItem(String menutitle, String title) {
        JMenuItem newmenuitem = new JMenuItem(title);
        newmenuitem.addActionListener(e -> {
            if(menutitle.contains("top")) {
                state.newTile.put("sprite0", title);
                state.selectedTextureMenuItems[0].setText(title);
            }
            else if(menutitle.contains("wall")) {
                state.newTile.put("sprite1", title);
                state.selectedTextureMenuItems[1].setText(title);
            }
            else if(menutitle.contains("floor")) {
                state.newTile.put("sprite2", title);
                state.selectedTextureMenuItems[2].setText(title);
            }
        });
        menus.get(menutitle).add(newmenuitem);
    }

    private static void addPrefMenuItem(String menutitle, String title) {
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
        state.selectedTitle = newstate.selectedTitle;
        state.selectedTileMenuItem = newstate.selectedTileMenuItem;
        state.selectedTextureMenuItems = newstate.selectedTextureMenuItems;
        state.selectedTileId = newstate.selectedTileId;
        state.selectedPropId = newstate.selectedPropId;
        state.selectedFlareTag = newstate.selectedFlareTag;
        state.createObjCode = newstate.createObjCode;
        state.newTile = newstate.newTile;
        state.newProp = newstate.newProp;
        state.newFlare = newstate.newFlare;
        eManager.currentMap.scene = newstate.mapScene;
    }

    public static cEditorLogicState getEditorState() {
        return new cEditorLogicState(state.snapToX, state.snapToY, state.selectedTitle, state.selectedTileMenuItem,
                state.selectedTileId, state.selectedPropId, state.selectedFlareTag, state.createObjCode, state.newTile,
                state.newProp, state.newFlare, eManager.currentMap.scene.copy());
    }
}
