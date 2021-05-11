import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class cEditorLogic {
    static Map<String,JMenu> menus = new HashMap<>();
    static Stack<gScene> undoStateStack = new Stack<>(); //move top from here to tmp for undo
    static Stack<gScene> redoStateStack = new Stack<>(); //move top from here to main for redo
    static int snapToX = 50;
    static int snapToY = 50;
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

    public static int[] getNewPrefabDims() {
        if(cVars.get("newprefabname").contains("room_large")) {
            return new int[]{2400, 2400};
        }
        if(cVars.isVal("newprefabname", "end_wall")) {
            return new int[]{300, 300};
        }
        if(cVars.isVal("newprefabname", "end_cap")) {
            return new int[]{300, 150};
        }
        if(cVars.isVal("newprefabname", "cube")) {
            return new int[]{300, 300};
        }
        if(cVars.isVal("newprefabname", "cube_large")) {
            return new int[]{600, 600};
        }
        return new int[]{1200, 1200};
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
        createNewMenu("Prefabs");
        createNewMenu("Items");
        createNewMenu("Gametype");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem saveas = new JMenuItem("Save As...");
        JMenuItem exportasprefab = new JMenuItem("Export as Prefab");
        JMenuItem exit = new JMenuItem("Exit");
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
                if(xCon.instance().getInt("e_showlossalert") <= 0) {
                    boolean join = false;
                    if(!sSettings.IS_SERVER) {
                        xCon.ex("startserver");
                        join = true;
                    }
                    xCon.ex("load");
                    if(join)
                        xCon.ex("joingame localhost 5555");
                }
            }
        });

        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex("e_openfile");
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

        addConsoleActionToJMenuItem(exit,"quit");
        addConsoleActionToJMenuItem(showControls,"e_showcontrols");
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

    private static void addConsoleActionToJMenuItem(JMenuItem item, String fullCommand) {
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xCon.ex(fullCommand);
            }
        });
    }
}
