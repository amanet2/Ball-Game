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
    static gScene previewScene;
    static int snapToX = 300;
    static int snapToY = 300;
    static String newitemname = "";

    private static final ArrayList<JCheckBoxMenuItem> prefabCheckboxMenuItems = new ArrayList<>();
    private static final ArrayList<JCheckBoxMenuItem> itemCheckBoxMenuItems = new ArrayList<>();
    private static final ArrayList<JCheckBoxMenuItem> gametypeCheckBoxMenuItems = new ArrayList<>();
    private static final ArrayList<JCheckBoxMenuItem> colorCheckBoxMenuItems = new ArrayList<>();

    public static void refreshCheckBoxItems() {
        if(sSettings.clientNewPrefabName.contains("cube") || newitemname.length() > 0) {
            snapToX = 50;
            snapToY = 50;
        }
        else {
            snapToX = 300;
            snapToY = 300;
        }
        for(JCheckBoxMenuItem checkBoxMenuItem : prefabCheckboxMenuItems) {
            checkBoxMenuItem.setSelected(false);
            if(checkBoxMenuItem.getText().equals(uiEditorMenus.getRotateName(sSettings.clientNewPrefabName)))
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
            if(checkBoxMenuItem.getText().equals(sSettings.clientPlayerColor))
                checkBoxMenuItem.setSelected(true);
        }
    }

    public static void resetCheckBoxMenuItem(JCheckBoxMenuItem checkBoxMenuItem) {
        checkBoxMenuItem.setSelected(xMain.shellLogic.console.ex(String.format(
                "cl_setvar GAMETYPE_%d_title", sSettings.clientGameMode)).equalsIgnoreCase(checkBoxMenuItem.getText()));
    }

    public static void refreshGametypeCheckBoxMenuItems() {
        for(JCheckBoxMenuItem checkBoxMenuItem : gametypeCheckBoxMenuItems) {
            resetCheckBoxMenuItem(checkBoxMenuItem);
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
        xMain.shellLogic.displayPane.frame.setJMenuBar(menubar);
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
        JMenuItem joinip = addMenuItem("Multiplayer", "Address: " + xMain.shellLogic.console.ex("cl_setvar joinip"));
        JMenuItem joinport = addMenuItem("Multiplayer", "Port: " + xMain.shellLogic.console.ex("cl_setvar joinport"));
        JMenuItem playerName = addMenuItem("Settings", "Name: " + sSettings.clientPlayerName);
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

        exit.addActionListener(e -> xMain.shellLogic.console.ex("quit"));

        newtopmap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!sSettings.clientMapLoaded || xMain.shellLogic.console.ex("e_showlossalert").equals("0"))
                    delegate();
                saveas.setEnabled(true);
            }

            private void delegate() {
                if(!sSettings.IS_SERVER) {
                    xMain.shellLogic.console.ex("startserver");
                    xMain.shellLogic.console.ex("load");
                    xMain.shellLogic.console.ex("joingame localhost " + sSettings.serverListenPort);
                }
                else
                    xMain.shellLogic.console.ex("e_newmap");
            }
        });

        join.addActionListener(e -> {
            xMain.shellLogic.console.ex("joingame");
            newtopmap.setEnabled(false);
            open.setEnabled(false);
            saveas.setEnabled(true);
        });

        joinip.addActionListener(e -> xMain.shellLogic.console.ex("e_changejoinip"));

        joinport.addActionListener(e -> xMain.shellLogic.console.ex("e_changejoinport"));

        playerName.addActionListener(e -> xMain.shellLogic.console.ex("e_changeplayername"));

        open.addActionListener(e -> {
            xMain.shellLogic.console.ex("e_openfile");
            saveas.setEnabled(true);
        });

        saveas.addActionListener(e -> xMain.shellLogic.console.ex("e_saveas"));

//        exportasprefab.addActionListener(e -> xMain.shellLogic.console.ex("exportasprefab"));

        //fill prefabs menu
        ArrayList<String> allPrefabFiles = new ArrayList<>(Arrays.asList(sSettings.prefab_titles));
        ArrayList<String> allPrefabs = new ArrayList<>();
        ArrayList<String> allPrefabsRotate = new ArrayList<>();
        for(String s : allPrefabFiles) {
            String rs = s;
            for(String rt : new String[]{"_000", "_090", "_180", "_270"}) {
                if(s.contains(rt)) {
                    rs = s.split(rt)[0];
                    if(!allPrefabsRotate.contains(rs))
                        allPrefabsRotate.add(rs);
                    break;
                }
            }
            if(!allPrefabs.contains(rs))
                allPrefabs.add(rs);
        }
        for(String s : allPrefabs) {
            JCheckBoxMenuItem prefabmenuitem = new JCheckBoxMenuItem(s);
            prefabmenuitem.setFont(dFonts.getFontNormal());
            if(uiEditorMenus.getRotateName(sSettings.clientNewPrefabName).contains(prefabmenuitem.getText()))
                prefabmenuitem.setSelected(true);
            prefabmenuitem.addActionListener(e -> {
                String name = prefabmenuitem.getText();
                if(allPrefabsRotate.contains(name))
                    sSettings.clientNewPrefabName = name+"_000";
                else
                    sSettings.clientNewPrefabName = name;
                xMain.shellLogic.console.ex("cl_clearthingmappreview");
                xMain.shellLogic.console.ex(String.format("cl_execpreview prefabs/%s 0 0 12500 5600", sSettings.clientNewPrefabName));
                newitemname = "";
                refreshCheckBoxItems();
            });
            prefabCheckboxMenuItems.add(prefabmenuitem);
            menus.get("Prefabs").add(prefabmenuitem);
        }
        //fill items menu
        StringBuilder sb = new StringBuilder();
        for(String tt : sSettings.object_titles) {
            if(tt.startsWith("ITEM_"))
                sb.append(";").append(tt);
        }
        String[] itemTitles = sb.substring(1).split(";");
        for(String itemname : itemTitles) {
            JCheckBoxMenuItem itemMenuItem = new JCheckBoxMenuItem(itemname);
            itemMenuItem.setFont(dFonts.getFontNormal());
            if(itemMenuItem.getText().equals(newitemname))
                itemMenuItem.setSelected(true);
            itemMenuItem.addActionListener(e -> {
                sSettings.clientNewPrefabName = "";
                newitemname = itemname;
                refreshCheckBoxItems();
            });
            itemCheckBoxMenuItems.add(itemMenuItem);
            menus.get("Items").add(itemMenuItem);
        }
        //fill gametypes menu
        int ctr = 0;
        ArrayList<String> gameTypeTitles = new ArrayList<>();
        while(!xMain.shellLogic.console.ex("cl_setvar GAMETYPE_"+ctr+"_title").equals("null")) {
            gameTypeTitles.add(xMain.shellLogic.console.ex("cl_setvar GAMETYPE_"+ctr+"_title"));
            ctr++;
        }
        for(int gtr = 0; gtr < gameTypeTitles.size(); gtr++) {
            String gameTypeTitle = gameTypeTitles.get(gtr);
            JCheckBoxMenuItem gametypeMenuItem = new JCheckBoxMenuItem(gameTypeTitle);
            gametypeMenuItem.setFont(dFonts.getFontNormal());
            resetCheckBoxMenuItem(gametypeMenuItem);
            int mygameType = gtr;
            gametypeMenuItem.addActionListener(e -> {
                if(sSettings.IS_SERVER)
                    xMain.shellLogic.console.ex("gamemode " + mygameType);
                else
                    xMain.shellLogic.clientNetThread.addNetCmd("gamemode " + mygameType);
                refreshGametypeCheckBoxMenuItems();
            });
            gametypeCheckBoxMenuItems.add(gametypeMenuItem);
            menus.get("Gametype").add(gametypeMenuItem);
        }
        //fill colors menu
        for(String color : sSettings.colorSelection) {
            JCheckBoxMenuItem colorMenuItem = new JCheckBoxMenuItem(color);
            colorMenuItem.setFont(dFonts.getFontNormal());
            if(colorMenuItem.getText().equals(sSettings.clientPlayerColor))
                colorMenuItem.setSelected(true);
            colorMenuItem.addActionListener(e -> {
                sSettings.clientPlayerColor = colorMenuItem.getText();
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
        xMain.shellLogic.displayPane.frame.getJMenuBar().add(newmenu);
    }

    private static void createNewSubmenu(String title, String subtitle) {
        JMenu newmenu = new JMenu(subtitle);
              newmenu.setFont(dFonts.getFontNormal());
        menus.put(subtitle,newmenu);
        menus.get(title).add(newmenu);
    }

    public static String getRotateName(String s) {
        return s.replace("_000", "").replace("_090",""
        ).replace("_180", "").replace("_270", "");
    }

    public static void setFileChooserFont(Component[] comp) {
        for (Component component : comp) {
            if (component instanceof Container)
                setFileChooserFont(((Container) component).getComponents());

            try {
                component.setFont(component.getFont().deriveFont(component.getFont().getSize() * 2f));
            } catch (Exception ignored) {
            } // do nothing
        }
    }
}
