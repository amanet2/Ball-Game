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
    static cEditorLogicState state = new cEditorLogicState(50,50, new gScene());
    private static ArrayList<JCheckBoxMenuItem> prefabCheckboxMenuItems = new ArrayList<>();
    private static ArrayList<JCheckBoxMenuItem> itemCheckBoxMenuItems = new ArrayList<>();
    private static ArrayList<JCheckBoxMenuItem> gametypeCheckBoxMenuItems = new ArrayList<>();

    public static void refreshCheckBoxItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : prefabCheckboxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(cVars.get("newprefabname"))) {
                checkBoxMenuItem.setSelected(true);
            }
        }
        for(JCheckBoxMenuItem checkBoxMenuItem : itemCheckBoxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(cVars.get("newitemname"))) {
                checkBoxMenuItem.setSelected(true);
            }
        }
    }

    public static void refreshGametypeCheckBoxMenuItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : gametypeCheckBoxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals("Killmaster") && cVars.isInt("gamemode", cGameMode.DEATHMATCH))
                checkBoxMenuItem.setSelected(true);
            else if(checkBoxMenuItem.getText().equals("Flagmaster") && cVars.isInt("gamemode", cGameMode.FLAG_MASTER))
                checkBoxMenuItem.setSelected(true);
            else if(checkBoxMenuItem.getText().equals("Virusmaster") && cVars.isInt("gamemode", cGameMode.VIRUS))
                checkBoxMenuItem.setSelected(true);
        }
    }

    public static void setupMapMakerWindow() {
        JMenuBar menubar = new JMenuBar();
        oDisplay.instance().frame.setJMenuBar(menubar);
        createNewMenu("File");
//        createNewMenu("Parameters");
        createNewMenu("Prefabs");
        createNewMenu("Items");
//        createNewMenu("Scene");
        createNewMenu("Gametype");
//        createNewMenu("Settings");
//        createNewSubmenu("Settings", "Game Mode: " +
//                cGameMode.net_gamemode_texts[cVars.getInt("gamemode")]);
//        createNewSubmenu("Parameters", "Snap-To: " + state.snapToX + "," + state.snapToY);
//        createNewSubmenu(menus.get("Parameters").getItem(0).getText(),"Nearest X Coord");
//        createNewSubmenu(menus.get("Parameters").getItem(0).getText(),"Nearest Y Coord");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveas = new JMenuItem("Save As...");
        JMenuItem exportasprefab = new JMenuItem("Export as Prefab");
        JMenuItem exit = new JMenuItem("Exit (ctrl+q)");
        JMenuItem newtopmap = new JMenuItem("New");
        JMenuItem showControls = new JMenuItem("Show Controls");

        menus.get("File").add(newtopmap);
        menus.get("File").add(open);
        menus.get("File").add(saveas);
        menus.get("File").add(exportasprefab);
        menus.get("File").add(showControls);
        menus.get("File").add(exit);

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
                cVars.put("newitemname", "");
                refreshCheckBoxItems();
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
                cVars.put("newprefabname", "");
                cVars.put("newitemname", itemname);
                refreshCheckBoxItems();
            });
            itemCheckBoxMenuItems.add(itemMenuItem);
            menus.get("Items").add(itemMenuItem);
        }
        //fill gametypes menu
        for(String gametype : new String[]{"Killmaster", "Flagmaster", "Virusmaster"}) {
            JCheckBoxMenuItem gametypeMenuItem = new JCheckBoxMenuItem(gametype);
            if(gametypeMenuItem.getText().equals("Killmaster") && cVars.isInt("gamemode", cGameMode.DEATHMATCH))
                gametypeMenuItem.setSelected(true);
            else if(gametypeMenuItem.getText().equals("Flagmaster") && cVars.isInt("gamemode", cGameMode.FLAG_MASTER))
                gametypeMenuItem.setSelected(true);
            else if(gametypeMenuItem.getText().equals("Virusmaster") && cVars.isInt("gamemode", cGameMode.VIRUS))
                gametypeMenuItem.setSelected(true);
            gametypeMenuItem.addActionListener(e -> {
                if(gametypeMenuItem.getText().equals("Killmaster"))
                    cVars.putInt("gamemode", cGameMode.DEATHMATCH);
                else if(gametypeMenuItem.getText().equals("Flagmaster"))
                    cVars.putInt("gamemode", cGameMode.FLAG_MASTER);
                else if(gametypeMenuItem.getText().equals("Virusmaster"))
                    cVars.putInt("gamemode", cGameMode.VIRUS);
                refreshGametypeCheckBoxMenuItems();
            });
            gametypeCheckBoxMenuItems.add(gametypeMenuItem);
            menus.get("Gametype").add(gametypeMenuItem);
        }

        addConsoleActionToJMenuItem(exit,"quit");
        addConsoleActionToJMenuItem(showControls,"e_showcontrols");

//        addSnapToMenuItem("Nearest X Coord","1");
//        addSnapToMenuItem("Nearest X Coord","5");
//        addSnapToMenuItem("Nearest X Coord","10");
//        addSnapToMenuItem("Nearest X Coord","25");
//        addSnapToMenuItem("Nearest X Coord","50");
//        addSnapToMenuItem("Nearest X Coord","100");
//        addSnapToMenuItem("Nearest Y Coord","1");
//        addSnapToMenuItem("Nearest Y Coord","5");
//        addSnapToMenuItem("Nearest Y Coord","10");
//        addSnapToMenuItem("Nearest Y Coord","25");
//        addSnapToMenuItem("Nearest Y Coord","50");
//        addSnapToMenuItem("Nearest Y Coord","100");

//        for(int i = 0; i < cGameMode.net_gamemode_texts.length; i++) {
//            JMenuItem newmenuitem = new JMenuItem(cGameMode.net_gamemode_texts[i]);
//            newmenuitem.addActionListener(e -> {
//                for(int j = 0; j < cGameMode.net_gamemode_texts.length; j++) {
//                    if(cGameMode.net_gamemode_texts[j].equals(newmenuitem.getText()))
//                        cVars.putInt("gamemode", j);
//                }
//                menus.get("Settings").getItem(0).setText("Game Mode: "
//                        + cGameMode.net_gamemode_texts[cVars.getInt("gamemode")]);
//            });
//            menus.get("Settings").getItem(0).add(newmenuitem);
//        }
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

//    private static void addSnapToMenuItem(String menutitle, String title) {
//        JMenuItem newmenuitem = new JMenuItem(title);
//        newmenuitem.addActionListener(e -> {
//            if(menutitle.contains("Nearest X Coord"))
//                state.snapToX = Integer.parseInt(title);
//            else if(menutitle.contains("Nearest Y Coord"))
//                state.snapToY = Integer.parseInt(title);
//            menus.get("Parameters").getItem(0).setText(String.format("Snap-To: %d,%d",
//                    state.snapToX, state.snapToY));
//        });
//        menus.get(menutitle).add(newmenuitem);
//    }

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
        eManager.currentMap.scene = newstate.mapScene;
    }

    public static cEditorLogicState getEditorState() {
        return new cEditorLogicState(state.snapToX, state.snapToY, eManager.currentMap.scene.copy());
    }
}
