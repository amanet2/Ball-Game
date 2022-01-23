import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class uiEditorMenus {
    static Map<String,JMenu> menus = new HashMap<>();
    static Stack<gScene> undoStateStack = new Stack<>(); //move top from here to tmp for undo
    static Stack<gScene> redoStateStack = new Stack<>(); //move top from here to main for redo
    static int snapToX = 50;
    static int snapToY = 50;
    private static ArrayList<JCheckBoxMenuItem> prefabCheckboxMenuItems = new ArrayList<>();
    private static ArrayList<JCheckBoxMenuItem> itemCheckBoxMenuItems = new ArrayList<>();
    private static ArrayList<JCheckBoxMenuItem> gametypeCheckBoxMenuItems = new ArrayList<>();
    private static ArrayList<JCheckBoxMenuItem> colorCheckBoxMenuItems = new ArrayList<>();
    private static ArrayList<JCheckBoxMenuItem> overlayCheckboxMenuItems = new ArrayList<>();

    public static void refreshCheckBoxItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : prefabCheckboxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(uiEditorMenus.getRotateName(cVars.get("newprefabname")))) {
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

    public static void refreshColorCheckBoxItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : colorCheckBoxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(sVars.get("playercolor"))) {
                checkBoxMenuItem.setSelected(true);
            }
        }
    }

    public static void refreshGametypeCheckBoxMenuItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : gametypeCheckBoxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals("Killmaster") && cVars.isInt("gamemode", cGameLogic.DEATHMATCH))
                checkBoxMenuItem.setSelected(true);
            else if(checkBoxMenuItem.getText().equals("Flagmaster") && cVars.isInt("gamemode", cGameLogic.FLAG_MASTER))
                checkBoxMenuItem.setSelected(true);
            else if(checkBoxMenuItem.getText().equals("Virusmaster") && cVars.isInt("gamemode", cGameLogic.VIRUS))
                checkBoxMenuItem.setSelected(true);
        }
    }

    public static void setupMapMakerWindow() {
        JMenuBar menubar = new JMenuBar();
        oDisplay.instance().frame.setJMenuBar(menubar);
        createNewMenu("File");
        createNewMenu("Multiplayer");
        createNewMenu("Prefabs");
        createNewMenu("Items");
        createNewMenu("Gametype");
        createNewMenu("Settings");

        JMenuItem newtopmap = new JMenuItem("New");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem saveas = new JMenuItem("Save As...");
        saveas.setEnabled(false);
        JMenuItem exportasprefab = new JMenuItem("Export as Prefab");
        JMenuItem playerName = new JMenuItem("Name: " + sVars.get("playername"));
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem join = new JMenuItem("Join Game");
        JMenuItem joinip = new JMenuItem("Address: " + sVars.get("joinip"));
        JMenuItem joinport = new JMenuItem("Port: " + sVars.get("joinport"));
//        JMenuItem prefabs = new JMenuItem("Select Prefab");

        menus.get("File").add(newtopmap);
        menus.get("File").add(open);
        menus.get("File").add(saveas);
//        menus.get("File").add(exportasprefab);
        menus.get("File").add(exit);
        menus.get("Multiplayer").add(join);
        menus.get("Multiplayer").add(joinip);
        menus.get("Multiplayer").add(joinport);
//        menus.get("Prefabs").add(prefabs);
        menus.get("Settings").add(playerName);
        createNewSubmenu("Settings", "Color");
        createNewSubmenu("Settings", "Controls");
        createNewSubmenu("Settings", "Overlays");
        menus.get("Controls").add(new JLabel(" MOUSE_LEFT : throw rock "));
        menus.get("Controls").add(new JLabel(" W : move up "));
        menus.get("Controls").add(new JLabel(" S : move down "));
        menus.get("Controls").add(new JLabel(" A : move left "));
        menus.get("Controls").add(new JLabel(" D : move right "));
        menus.get("Controls").add(new JLabel(" TAB : show scoreboard "));
        menus.get("Controls").add(new JLabel(" Y : chat "));
        menus.get("Controls").add(new JLabel(" = : zoom in "));
        menus.get("Controls").add(new JLabel(" - : zoom out "));
        menus.get("Controls").add(new JLabel(" ~ : console "));

        addConsoleActionToJMenuItem(exit,"quit");

        newtopmap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(cVars.isZero("maploaded"))
                    delegate();
                else if(xCon.instance().getInt("e_showlossalert") <= 0)
                    delegate();
                saveas.setEnabled(true);
            }

            private void delegate() {
                if(!nServer.instance().isAlive()) {
                    xCon.ex("startserver");
                    xCon.ex("load");
                    xCon.ex("joingame localhost 5555");
                }
                else {
                    xCon.ex("e_newmap");
                }
            }
        });

//        prefabs.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                xCon.ex("e_openprefab");
//            }
//        });

        join.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("joingame");
                newtopmap.setEnabled(false);
                open.setEnabled(false);
                saveas.setEnabled(true);
            }
        });

        joinip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("e_changejoinip");
            }
        });

        joinport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("e_changejoinport");
            }
        });

        playerName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("e_changeplayername");
            }
        });

        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("e_openfile");
                saveas.setEnabled(true);
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
        String[] prefabs = {"corner", "cube", "cube_a", "floor", "hallway", "hallway_a", "hallway_b", "room",
                            "room_large", "room_large_a", "room_large_b", "room_large_c"};
        String[] prefabsRotate = {"corner", "hallway", "hallway_a", "hallway_b", "room_large_a", "room_large_b",
                                    "room_large_c"};
        ArrayList<String> prefabList = new ArrayList<>(Arrays.asList(prefabs));
        ArrayList<String> prefabRotateList = new ArrayList<>(Arrays.asList(prefabsRotate));
        for(String s : prefabs) {
            JCheckBoxMenuItem prefabmenuitem = new JCheckBoxMenuItem(s);
            if(uiEditorMenus.getRotateName(cVars.get("newprefabname")).contains(prefabmenuitem.getText()))
                prefabmenuitem.setSelected(true);
            prefabmenuitem.addActionListener(e -> {
                String name = prefabmenuitem.getText();
                if(prefabRotateList.contains(name))
                    cVars.put("newprefabname", name+"_000");
                else
                    cVars.put("newprefabname", name);
                cVars.put("newitemname", "");
                refreshCheckBoxItems();
            });
            prefabCheckboxMenuItems.add(prefabmenuitem);
            menus.get("Prefabs").add(prefabmenuitem);

        }
//        for(String prefabname : eManager.prefabSelection) {
//            JCheckBoxMenuItem prefabmenuitem = new JCheckBoxMenuItem(prefabname);
//            if(prefabmenuitem.getText().equals(uiEditorMenus.getRotateName(cVars.get("newprefabname")))) {
//                prefabmenuitem.setSelected(true);
//            }
//            prefabmenuitem.addActionListener(e -> {
//                cVars.put("newprefabname", prefabname);
//                cVars.put("newitemname", "");
//                refreshCheckBoxItems();
//            });
//            prefabCheckboxMenuItems.add(prefabmenuitem);
//            menus.get("Prefabs").add(prefabmenuitem);
//        }
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
            if(gametypeMenuItem.getText().equals("Killmaster") && cVars.isInt("gamemode", cGameLogic.DEATHMATCH))
                gametypeMenuItem.setSelected(true);
            else if(gametypeMenuItem.getText().equals("Flagmaster") && cVars.isInt("gamemode", cGameLogic.FLAG_MASTER))
                gametypeMenuItem.setSelected(true);
            else if(gametypeMenuItem.getText().equals("Virusmaster") && cVars.isInt("gamemode", cGameLogic.VIRUS))
                gametypeMenuItem.setSelected(true);
            gametypeMenuItem.addActionListener(e -> {
                if(gametypeMenuItem.getText().equals("Killmaster"))
                    cVars.putInt("gamemode", cGameLogic.DEATHMATCH);
                else if(gametypeMenuItem.getText().equals("Flagmaster"))
                    cVars.putInt("gamemode", cGameLogic.FLAG_MASTER);
                else if(gametypeMenuItem.getText().equals("Virusmaster"))
                    cVars.putInt("gamemode", cGameLogic.VIRUS);
                refreshGametypeCheckBoxMenuItems();
            });
            gametypeCheckBoxMenuItems.add(gametypeMenuItem);
            menus.get("Gametype").add(gametypeMenuItem);
        }
        //fill colors menu
        for(String color : sVars.getArray("colorselection")) {
            JCheckBoxMenuItem colorMenuItem = new JCheckBoxMenuItem(color);
            if(colorMenuItem.getText().equals(sVars.get("playercolor")))
                colorMenuItem.setSelected(true);
            colorMenuItem.addActionListener(e -> {
                sVars.put("playercolor", colorMenuItem.getText());
                refreshColorCheckBoxItems();
            });
            colorCheckBoxMenuItems.add(colorMenuItem);
            menus.get("Color").add(colorMenuItem);
        }
        //fill overlays menu
        for(String option : new String[]{"drawhitboxes","drawmapmakergrid","vfxenableshading","vfxenableshadows",
        "vfxenableflares", "vfxenableanimations"}) {
            JCheckBoxMenuItem ovmenuitem = new JCheckBoxMenuItem(option);
            if(sVars.getInt(option) == 1)
                ovmenuitem.setSelected(true);
            ovmenuitem.addActionListener(e -> {
                sVars.put(option, sVars.isIntVal(option, 1) ? "0" : "1");
                ovmenuitem.setSelected(sVars.getInt(option) == 1);
            });
            menus.get("Overlays").add(ovmenuitem);
        }
    }

    private static void createNewMenu(String title) {
        JMenu newmenu = new JMenu(title);
//        newmenu.setFont(
//                new Font(sVars.get("fontnameui"), sVars.getInt("fontmode"),
//                        sVars.getInt("fontsize") * sSettings.height / cVars.getInt("gamescale")
//                )
//        );
        menus.put(title,newmenu);
        oDisplay.instance().frame.getJMenuBar().add(newmenu);
    }

    private static void createNewSubmenu(String title, String subtitle) {
        JMenu newmenu = new JMenu(subtitle);
        menus.put(subtitle,newmenu);
        menus.get(title).add(newmenu);
    }

    private static void addConsoleActionToJMenuItem(JMenuItem item, String fullCommand) {
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex(fullCommand);
            }
        });
    }

    public static String getRotateName(String s) {
        return s.replace("_000", "").replace("_090",""
        ).replace("_180", "").replace("_270", "");
    }
}
