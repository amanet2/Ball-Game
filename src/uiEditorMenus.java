import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class uiEditorMenus {
    static Map<String,JMenu> menus = new HashMap<>();
    static gScene previewScene = new gScene();
    static int snapToX = 300;
    static int snapToY = 300;
    static String newitemname = "";

    private static final ArrayList<JCheckBoxMenuItem> prefabCheckboxMenuItems = new ArrayList<>();
    private static final ArrayList<JCheckBoxMenuItem> itemCheckBoxMenuItems = new ArrayList<>();
    private static final ArrayList<JCheckBoxMenuItem> gametypeCheckBoxMenuItems = new ArrayList<>();
    private static final ArrayList<JCheckBoxMenuItem> colorCheckBoxMenuItems = new ArrayList<>();

    public static void refreshCheckBoxItems() {
        if(cClientLogic.newprefabname.contains("cube") || newitemname.length() > 0) {
            snapToX = 50;
            snapToY = 50;
        }
        else {
            snapToX = 300;
            snapToY = 300;
        }
        for(JCheckBoxMenuItem checkBoxMenuItem : prefabCheckboxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(uiEditorMenus.getRotateName(cClientLogic.newprefabname)))
                checkBoxMenuItem.setSelected(true);
        }
        for(JCheckBoxMenuItem checkBoxMenuItem : itemCheckBoxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(newitemname))
                checkBoxMenuItem.setSelected(true);
        }
    }

    public static void refreshColorCheckBoxItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : colorCheckBoxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(cClientLogic.playerColor))
                checkBoxMenuItem.setSelected(true);
        }
    }

    public static void refreshGametypeCheckBoxMenuItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : gametypeCheckBoxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals("Rockmaster") && cGameLogic.isGame(cGameLogic.DEATHMATCH))
                checkBoxMenuItem.setSelected(true);
            else if(checkBoxMenuItem.getText().equals("Flagmaster") && cGameLogic.isGame(cGameLogic.FLAG_MASTER))
                checkBoxMenuItem.setSelected(true);
            else if(checkBoxMenuItem.getText().equals("Virusmaster") && cGameLogic.isGame(cGameLogic.VIRUS))
                checkBoxMenuItem.setSelected(true);
            else if(checkBoxMenuItem.getText().equals("Goldmaster") && cGameLogic.isGame(cGameLogic.GOLD_MASTER))
                checkBoxMenuItem.setSelected(true);
        }
    }

    public static JMenuItem addMenuItem(String parentMenu, String text) {
        JMenuItem newItem = new JMenuItem(text);
        newItem.setFont(dFonts.getFontNormal());
        menus.get(parentMenu).add(newItem);
        return newItem;
    }

    public static void addSubMenuLabel(String parentMenu, String text) {
        JLabel newLabel = new JLabel(text);
        newLabel.setFont(dFonts.getFontNormal());
        menus.get(parentMenu).add(newLabel);
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
        JMenuItem newtopmap = addMenuItem("File", "New");
        JMenuItem open = addMenuItem("File", "Open");
        JMenuItem saveas = addMenuItem("File", "Save As...");
        saveas.setEnabled(false);
//        JMenuItem exportasprefab = addMenuItem("File", "Export as Prefab");
        JMenuItem exit = addMenuItem("File", "Exit");
        JMenuItem join = addMenuItem("Multiplayer", "Join Game");
        JMenuItem joinip = addMenuItem("Multiplayer", "Address: " + xCon.ex("cl_setvar joinip"));
        JMenuItem joinport = addMenuItem("Multiplayer", "Port: " + xCon.ex("cl_setvar joinport"));
        JMenuItem playerName = addMenuItem("Settings", "Name: " + cClientLogic.playerName);
        createNewSubmenu("Settings", "Color");
        createNewSubmenu("Settings", "Controls");
        createNewSubmenu("Settings", "Overlays");
        addSubMenuLabel("Controls", " MOUSE_LEFT : throw rock");
        addSubMenuLabel("Controls", " W : move up ");
        addSubMenuLabel("Controls", " S : move down ");
        addSubMenuLabel("Controls", " A : move left ");
        addSubMenuLabel("Controls", " D : move right ");
        addSubMenuLabel("Controls", " TAB : show scoreboard ");
        addSubMenuLabel("Controls", " Y : chat ");
        addSubMenuLabel("Controls", " = : zoom in ");
        addSubMenuLabel("Controls", " - : zoom out ");
        addSubMenuLabel("Controls", " ~ : console ");

        addConsoleActionToJMenuItem(exit,"quit");

        newtopmap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!cClientLogic.maploaded)
                    delegate();
                else if(xCon.instance().getInt("e_showlossalert") <= 0)
                    delegate();
                saveas.setEnabled(true);
            }

            private void delegate() {
                if(!nServer.instance().isAlive()) {
                    xCon.ex("startserver");
                    xCon.ex("load");
                    xCon.ex("joingame localhost " + cServerLogic.listenPort);
                }
                else
                    xCon.ex("e_newmap");
            }
        });

        join.addActionListener(e -> {
            xCon.ex("joingame");
            newtopmap.setEnabled(false);
            open.setEnabled(false);
            saveas.setEnabled(true);
        });

        joinip.addActionListener(e -> xCon.ex("e_changejoinip"));

        joinport.addActionListener(e -> xCon.ex("e_changejoinport"));

        playerName.addActionListener(e -> xCon.ex("e_changeplayername"));

        open.addActionListener(e -> {
            xCon.ex("e_openfile");
            saveas.setEnabled(true);
        });

        saveas.addActionListener(e -> xCon.ex("e_saveas"));


//        exportasprefab.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                xCon.ex("exportasprefab");
//            }
//        });

        //fill prefabs menu
        String[] prefabs = {"corner", "cube", "hallway", "junction", "room",
                            "room_large"};
        String[] prefabsRotate = {"corner", "hallway", "junction"};
        ArrayList<String> prefabRotateList = new ArrayList<>(Arrays.asList(prefabsRotate));
        for(String s : prefabs) {
            JCheckBoxMenuItem prefabmenuitem = new JCheckBoxMenuItem(s);
            prefabmenuitem.setFont(dFonts.getFontNormal());
            if(uiEditorMenus.getRotateName(cClientLogic.newprefabname).contains(prefabmenuitem.getText()))
                prefabmenuitem.setSelected(true);
            prefabmenuitem.addActionListener(e -> {
                String name = prefabmenuitem.getText();
                if(prefabRotateList.contains(name))
                    cClientLogic.newprefabname = name+"_000";
                else
                    cClientLogic.newprefabname = name;
                xCon.ex("cl_clearthingmappreview");
                xCon.ex(String.format("cl_execpreview prefabs/%s 0 0 12500 5600", cClientLogic.newprefabname));
                newitemname = "";
                refreshCheckBoxItems();
            });
            prefabCheckboxMenuItems.add(prefabmenuitem);
            menus.get("Prefabs").add(prefabmenuitem);
        }
        //fill items menu
        for(String itemname: new String[]{"ITEM_SPAWNPOINT", "ITEM_FLAG", "ITEM_TELEPORTER_BLUE", "ITEM_TELEPORTER_RED",
                                          "ITEM_POINTGIVER", "ITEM_LANDMINE"}) {
            JCheckBoxMenuItem itemMenuItem = new JCheckBoxMenuItem(itemname);
            itemMenuItem.setFont(dFonts.getFontNormal());
            if(itemMenuItem.getText().equals(newitemname))
                itemMenuItem.setSelected(true);
            itemMenuItem.addActionListener(e -> {
                cClientLogic.newprefabname = "";
                newitemname = itemname;
                refreshCheckBoxItems();
            });
            itemCheckBoxMenuItems.add(itemMenuItem);
            menus.get("Items").add(itemMenuItem);
        }
        //fill gametypes menu
        for(String gametype : new String[]{"Rockmaster", "Flagmaster", "Virusmaster", "Goldmaster"}) {
            JCheckBoxMenuItem gametypeMenuItem = new JCheckBoxMenuItem(gametype);
            gametypeMenuItem.setFont(dFonts.getFontNormal());
            if(gametypeMenuItem.getText().equals("Rockmaster") && cGameLogic.isGame(cGameLogic.DEATHMATCH))
                gametypeMenuItem.setSelected(true);
            else if(gametypeMenuItem.getText().equals("Flagmaster") && cGameLogic.isGame(cGameLogic.FLAG_MASTER))
                gametypeMenuItem.setSelected(true);
            else if(gametypeMenuItem.getText().equals("Virusmaster") && cGameLogic.isGame(cGameLogic.VIRUS))
                gametypeMenuItem.setSelected(true);
            else if(gametypeMenuItem.getText().equals("Goldmaster") && cGameLogic.isGame(cGameLogic.GOLD_MASTER))
                gametypeMenuItem.setSelected(true);
            gametypeMenuItem.addActionListener(e -> {
                if(gametypeMenuItem.getText().equals("Rockmaster"))
                    xCon.ex("cv_gamemode " + cGameLogic.DEATHMATCH);
                else if(gametypeMenuItem.getText().equals("Flagmaster"))
                    xCon.ex("cv_gamemode " + cGameLogic.FLAG_MASTER);
                else if(gametypeMenuItem.getText().equals("Virusmaster"))
                    xCon.ex("cv_gamemode " + cGameLogic.VIRUS);
                else if(gametypeMenuItem.getText().equals("Goldmaster"))
                    xCon.ex("cv_gamemode " + cGameLogic.GOLD_MASTER);
                refreshGametypeCheckBoxMenuItems();
            });
            gametypeCheckBoxMenuItems.add(gametypeMenuItem);
            menus.get("Gametype").add(gametypeMenuItem);
        }
        //fill colors menu
        for(String color : sSettings.colorSelection) {
            JCheckBoxMenuItem colorMenuItem = new JCheckBoxMenuItem(color);
            colorMenuItem.setFont(dFonts.getFontNormal());
            if(colorMenuItem.getText().equals(cClientLogic.playerColor))
                colorMenuItem.setSelected(true);
            colorMenuItem.addActionListener(e -> {
                cClientLogic.playerColor = colorMenuItem.getText();
                refreshColorCheckBoxItems();
            });
            colorCheckBoxMenuItems.add(colorMenuItem);
            menus.get("Color").add(colorMenuItem);
        }
        //fill overlays menu
        HashMap<String, gDoable> overlaySelectionActionMap = new HashMap<>();
        overlaySelectionActionMap.put("drawhitboxes",
                new gDoable(){
                    public void exec() {
                        sSettings.drawhitboxes = !sSettings.drawhitboxes;
                    }

                    public boolean check() {
                        return sSettings.drawhitboxes;
                    }
                }
        );
        overlaySelectionActionMap.put("drawmapmakergrid",
                new gDoable(){
                    public void exec() {
                        sSettings.drawmapmakergrid = !sSettings.drawmapmakergrid;
                    }

                    public boolean check() {
                        return sSettings.drawmapmakergrid;
                    }
                }
        );
        overlaySelectionActionMap.put("vfxenableshading",
                new gDoable(){
                    public void exec() {
                        sSettings.vfxenableshading = !sSettings.vfxenableshading;
                    }

                    public boolean check() {
                        return sSettings.vfxenableshading;
                    }
                }
        );
        overlaySelectionActionMap.put("vfxenableshadows",
                new gDoable(){
                    public void exec() {
                        sSettings.vfxenableshadows = !sSettings.vfxenableshadows;
                    }

                    public boolean check() {
                        return sSettings.vfxenableshadows;
                    }
                }
        );
        overlaySelectionActionMap.put("vfxenableflares",
                new gDoable(){
                    public void exec() {
                        sSettings.vfxenableflares = !sSettings.vfxenableflares;
                    }

                    public boolean check() {
                        return sSettings.vfxenableflares;
                    }
                }
        );
        overlaySelectionActionMap.put("vfxenableanimations",
                new gDoable(){
                    public void exec() {
                        sSettings.vfxenableanimations = !sSettings.vfxenableanimations;
                    }

                    public boolean check() {
                        return sSettings.vfxenableanimations;
                    }
                }
        );
        for(String option : new String[]{"drawhitboxes","drawmapmakergrid","vfxenableshading","vfxenableshadows",
        "vfxenableflares", "vfxenableanimations"}) {
            JCheckBoxMenuItem ovmenuitem = new JCheckBoxMenuItem(option);
            ovmenuitem.setFont(dFonts.getFontNormal());
            ovmenuitem.setSelected(overlaySelectionActionMap.get(option).check());
            ovmenuitem.addActionListener(e -> {
                overlaySelectionActionMap.get(option).exec();
                ovmenuitem.setSelected(overlaySelectionActionMap.get(option).check());
            });
            menus.get("Overlays").add(ovmenuitem);
        }
    }

    private static void createNewMenu(String title) {
        JMenu newmenu = new JMenu(title);
        newmenu.setFont(dFonts.getFontNormal());
        menus.put(title, newmenu);
        oDisplay.instance().frame.getJMenuBar().add(newmenu);
    }

    private static void createNewSubmenu(String title, String subtitle) {
        JMenu newmenu = new JMenu(subtitle);
              newmenu.setFont(dFonts.getFontNormal());
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

    public static void setFileChooserFont(Component[] comp) {
        for (int x = 0; x < comp.length; x++) {
            if (comp[x] instanceof Container)
                setFileChooserFont(((Container) comp[x]).getComponents());

            try {
                    comp[x].setFont(comp[x].getFont().deriveFont(comp[x].getFont().getSize() * 2f));
            } catch (Exception e) {
            } // do nothing
        }
    }
}
