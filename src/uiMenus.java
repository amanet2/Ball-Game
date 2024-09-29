import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

public class uiMenus {
    static boolean gobackSelected = false;
    static final int MENU_MAIN = 0;
    static final int MENU_QUIT = 1;
    static final int MENU_OPTIONS = 2;
    static final int MENU_CONTROLS = 3;
    static final int MENU_VIDEO = 4;
    static final int MENU_NEWGAME = 5;
    static final int MENU_JOINGAME = 6;
    static final int MENU_PROFILE = 7;
    static final int MENU_AUDIO = 8;
    static final int MENU_RESOLUTION = 9;
    static final int MENU_FRAMERATE = 10;
    static final int MENU_MAP = 11;
    static final int MENU_VOLUME = 12;
    static final int MENU_COLOR = 13;
    static final int MENU_CREDITS = 14;
    static final int MENU_DISCONNECT = 15;
    static final int MENU_BOTS = 16;
    static final int MENU_POWER = 17;
    static final int MENU_TIME = 18;
    static final int MENU_SERVER_BROWSER = 19;

    static int selectedMenu = MENU_MAIN;

    private static nState serverBrowserState;
    private static HashMap<String, String> serverBrowserNamesToAddressTable;

    static final uiMenu[] menuSelection = new uiMenu[]{
        new uiMenu(
                "Home",
                new uiMenuItem[]{
                        new uiMenuItem("Host Game") {
                            public void doItem(){
                                selectedMenu = MENU_NEWGAME;
                            }
                        },
                        new uiMenuItem("Join Game") {
                            public void doItem(){
                                selectedMenu = MENU_JOINGAME;
                            }
                        },
                        new uiMenuItem("Server Browser") {
                            public void doItem(){
                                selectedMenu = MENU_SERVER_BROWSER;
                            }
                        },
                        new uiMenuItem("Disconnect") {
                            public void doItem(){
                                selectedMenu = MENU_DISCONNECT;
                            }
                        },
                        new uiMenuItem("Options") {
                            public void doItem(){
                                selectedMenu = MENU_OPTIONS;
                            }
                        },
                        new uiMenuItem("Credits") {
                            public void doItem(){
                                selectedMenu = MENU_CREDITS;
                            }
                        },
                        new uiMenuItem("Quit") {
                            public void doItem(){
                                selectedMenu = MENU_QUIT;
                            }
                        }
                },
                -1
        ),
        new uiMenu(
                "Quit",
                new uiMenuItem[]{
                        new uiMenuItem("No") {
                            public void doItem(){
                                selectedMenu = MENU_MAIN;
                            }
                        },
                        new uiMenuItem("Yes") {
                            public void doItem(){
                                xMain.shellLogic.console.ex("quit");
                            }
                        }
                },
                MENU_MAIN
        ),
        new uiMenu(
                "Options",
                new uiMenuItem[]{
                        new uiMenuItem("Controls") {
                            public void doItem(){
                                menuSelection[MENU_CONTROLS].refresh();
                                selectedMenu = MENU_CONTROLS;
                            }
                        },
                        new uiMenuItem("Audio") {
                            public void doItem(){
                                menuSelection[MENU_AUDIO].refresh();
                                selectedMenu = MENU_AUDIO;
                            }
                        },
                        new uiMenuItem("Video") {
                            public void doItem(){
                                menuSelection[MENU_VIDEO].refresh();
                                selectedMenu = MENU_VIDEO;
                            }
                        },
                        new uiMenuItem("Profile") {
                            public void doItem(){
                                menuSelection[MENU_PROFILE].refresh();
                                selectedMenu = MENU_PROFILE;
                            }
                        },
                        new uiMenuItem("Power") {
                            public void doItem(){
                                menuSelection[MENU_POWER].refresh();
                                selectedMenu = MENU_POWER;
                            }
                        }
                },
                MENU_MAIN
        ),
        new uiMenu("Controls", getControlMenuItems(), MENU_OPTIONS) {
            public void refresh() {
                this.items = getControlMenuItems();
            }
        },
        new uiMenu(
                "Video",
                new uiMenuItem[]{
                        new uiMenuItem(String.format("Resolution [%dx%d]",sSettings.width,sSettings.height)) {
                            public void doItem() {
                                selectedMenu = MENU_RESOLUTION;
                            }
                        },
                        new uiMenuItem(String.format("Framerate [%d]",sSettings.rateShell)) {
                            public void doItem() {
                                selectedMenu = MENU_FRAMERATE;
                            }
                        },
                        new uiMenuItem(String.format("Borderless [%s]", sSettings.borderless ? "X" : "  ")) {
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar borderless " + (sSettings.borderless ? "0" : "1"));
                                menuSelection[MENU_VIDEO].refresh();
                            }
                        },
                        new uiMenuItem(String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  ")){
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar vfxenableanimations " + (sSettings.vfxenableanimations ? "0" : "1"));
                                menuSelection[MENU_VIDEO].refresh();
                            }
                        },
                        new uiMenuItem(String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  ")){
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar vfxenableflares " + (sSettings.vfxenableflares ? "0" : "1"));
                                menuSelection[MENU_VIDEO].refresh();
                            }
                        },
                        new uiMenuItem(String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  ")){
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar vfxenableshading " + (sSettings.vfxenableshading ? "0" : "1"));
                                menuSelection[MENU_VIDEO].refresh();
                            }
                        },
                        new uiMenuItem(String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ")){
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar vfxenableshadows " + (sSettings.vfxenableshadows ? "0" : "1"));
                                menuSelection[MENU_VIDEO].refresh();
                            }
                        }
                },
                MENU_OPTIONS
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        String.format("Resolution [%dx%d]", sSettings.width, sSettings.height),
                        String.format("Framerate [%d]", sSettings.rateShell),
                        String.format("Borderless [%s]", sSettings.borderless ? "X" : "  "),
                        String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  "),
                        String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  "),
                        String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  "),
                        String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ")
                });
            }
        },
        new uiMenu(
                "Host",
                new uiMenuItem[]{
                        new uiMenuItem("-Start-"){
                            public void doItem() {
                                xMain.shellLogic.console.ex("hostgame");
                                selectedMenu = MENU_MAIN;
                            }
                        },
                        new uiMenuItem(String.format("Bots [%d]", sSettings.botCount)){
                            public void doItem() {
                                selectedMenu = MENU_BOTS;
                            }
                        },
                        new uiMenuItem("Map [random]"){
                            public void doItem() {
                                selectedMenu = MENU_MAP;
                            }
                        },
                        new uiMenuItem(String.format("Time [%s]", eUtils.getTimeString((long)sSettings.serverTimeLimit))){
                            public void doItem() {
                                selectedMenu = MENU_TIME;
                            }
                        }
                },
                MENU_MAIN
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        "-Start-",
                        String.format("Bots [%d]", sSettings.botCount),
                        String.format("Map [%s]", eManager.mapSelectionIndex < 0 ? "random"
                                : eManager.mapsFileSelection[eManager.mapSelectionIndex]),
                        String.format("Time [%s]", eUtils.getTimeString((long)sSettings.serverTimeLimit))
                });
            }
        },
        new uiMenu(
                "Join",
                new uiMenuItem[]{
                        new uiMenuItem("-Start-"){
                            public void doItem() {
                                xMain.shellLogic.console.ex("joingame;pause");
                                selectedMenu = MENU_MAIN;
                            }
                        },
                        new uiMenuItem("IP []") {
                            public void doItem() {
                                xMain.shellLogic.console.ex("e_changejoinip");
                            }
                        },
                        new uiMenuItem("Port []") {
                            public void doItem() {
                                xMain.shellLogic.console.ex("e_changejoinport");
                            }
                        }
                },
                MENU_MAIN
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        "-Start-",
                        String.format("IP [%s]", xMain.shellLogic.console.ex("cl_setvar joinip")),
                        String.format("Port [%s]", xMain.shellLogic.console.ex("cl_setvar joinport"))
                });
            }
        },
        new uiMenu(
                "Profile",
                new uiMenuItem[]{
                        new uiMenuItem(String.format("Name [%s]", sSettings.clientPlayerName)) {
                            public void doItem() {
                                xMain.shellLogic.console.ex("e_changeplayername");
                            }
                        },
                        new uiMenuItem(String.format("Color [%s]", sSettings.clientPlayerColor)) {
                            public void doItem() {
                                selectedMenu = MENU_COLOR;
                            }
                        },
                },
                MENU_OPTIONS
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        String.format("Name [%s]", sSettings.clientPlayerName),
                        String.format("Color [%s]", sSettings.clientPlayerColor),
                });
            }
        },
        new uiMenu(
                "Audio",
                new uiMenuItem[]{
                        new uiMenuItem("mute") {
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar audioenabled " + (sSettings.audioenabled ? "0" : "1"));
                                menuSelection[MENU_AUDIO].refresh();
                            }
                        },
                        new uiMenuItem("volume") {
                            public void doItem() {
                                selectedMenu = MENU_VOLUME;
                            }
                        }
                },
                MENU_OPTIONS
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        String.format("Mute Audio [%s]", sSettings.audioenabled ? "  " : "X"),
                        String.format("Volume [%d]", (int) sSettings.clientVolume)
                });
            }
        },
        new uiMenu("Resolution", getResolutionMenuItems(), MENU_VIDEO),
        new uiMenu("Framerate", getFramerateMenuItems(), MENU_VIDEO),
        new uiMenu("Map", getMapMenuItems(), MENU_NEWGAME),
        new uiMenu(
                "Volume",
                new uiMenuItem[]{
                        new uiMenuItem("0"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("1"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("2"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("3"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("4"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("5"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("6"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("7"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("8"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        },
                        new uiMenuItem("9"){
                            public void doItem() {
                                setVolume(this.text);

                            }
                        },
                        new uiMenuItem("10"){
                            public void doItem() {
                                setVolume(this.text);
                            }
                        }
                },
                MENU_AUDIO
        ),
        new uiMenu("Color", getColorMenuItems(), MENU_PROFILE),
        new uiMenu(
                "Credits",
                new uiMenuItem[] {
                        new uiMenuItem("Ballmaster est. 2021"),
                        new uiMenuItem("Created by stallionusa (stallionusa.itch.io)"),
                        new uiMenuItem("Programming by stallionusa (stallionusa.itch.io)"),
                        new uiMenuItem("VFX by drummyfish (opengameart.org)"),
                        new uiMenuItem("VFX by Master484 (opengameart.org)"),
                        new uiMenuItem("VFX by stallionusa (stallionusa.itch.io)"),
                        new uiMenuItem("SFX by Kevin Fowler (hitrison.itch.io)"),
                        new uiMenuItem("SFX by mixkit.io")
                },
                MENU_MAIN
        ),
        new uiMenu(
                "Disconnect",
                new uiMenuItem[] {
                        new uiMenuItem("No") {
                            public void doItem(){
                                selectedMenu = MENU_MAIN;
                            }
                        },
                        new uiMenuItem("Yes") {
                            public void doItem(){
                                selectedMenu = MENU_MAIN;
                                xMain.shellLogic.console.ex("disconnect");
                            }
                        }
                },
                MENU_MAIN
        ),
        new uiMenu("Bots", getBotMenuItems(), MENU_NEWGAME),
        new uiMenu(
                "Power",
                new uiMenuItem[] {
                        new uiMenuItem(String.format("Enable Low Power Mode [%s]", sSettings.powerSave ? "X" : "   ")) {
                            public void doItem() {
                                xMain.shellLogic.clientVars.put("powersave", sSettings.powerSave ? "0" : "1");
                                menuSelection[MENU_POWER].refresh();
                            }
                        }
                },
                MENU_OPTIONS
        ) {
            public void refresh() {
                setMenuItemTexts(new String[] {
                        String.format("Enable Low Power Mode [%s]", sSettings.powerSave ? "X" : "   ")
                });
            }
        },
        new uiMenu(
                "Time", new uiMenuItem[]{
                    new uiMenuItem(eUtils.getTimeString((long) 60000)){
                        public void doItem() {
                            sSettings.serverTimeLimit = 60000;
                            menuSelection[MENU_NEWGAME].refresh();
                            selectedMenu = MENU_NEWGAME;
                        }
                    },
                    new uiMenuItem(eUtils.getTimeString((long) 120000)){
                        public void doItem() {
                            sSettings.serverTimeLimit = 120000;
                            menuSelection[MENU_NEWGAME].refresh();
                            selectedMenu = MENU_NEWGAME;
                        }
                    },
                    new uiMenuItem(eUtils.getTimeString((long) 180000)){
                        public void doItem() {
                            sSettings.serverTimeLimit = 180000;
                            menuSelection[MENU_NEWGAME].refresh();
                            selectedMenu = MENU_NEWGAME;
                        }
                    },
                    new uiMenuItem(eUtils.getTimeString((long) 240000)){
                        public void doItem() {
                            sSettings.serverTimeLimit = 240000;
                            menuSelection[MENU_NEWGAME].refresh();
                            selectedMenu = MENU_NEWGAME;
                        }
                    },
                    new uiMenuItem(eUtils.getTimeString((long) 300000)){
                        public void doItem() {
                            sSettings.serverTimeLimit = 300000;
                            menuSelection[MENU_NEWGAME].refresh();
                            selectedMenu = MENU_NEWGAME;
                        }
                    }
            },
            MENU_NEWGAME
        ),
        new uiMenu(
                "Server Browser",
                new uiMenuItem[]{
                        new uiMenuItem("Get List") {
                            public void doItem() {
                                try {
                                    URL availableIps = new URL("https://ballbrowser-0-842704188107.us-central1.run.app/list");
                                    BufferedReader res = new BufferedReader(new InputStreamReader(availableIps.openStream()));
                                    String resp = res.readLine(); //you get the IP as a String
                                    System.out.println("RESPONSE FROM FASTAPI SERVER: " + resp);
                                    serverBrowserState = new nState(resp.replace(":","="));
                                    serverBrowserNamesToAddressTable = new HashMap<>();
                                    String[] resp_servers = new String[serverBrowserState.keys().size()];
                                    int ctr = 0;
                                    for(String k : serverBrowserState.keys()) {
                                        resp_servers[ctr] = serverBrowserState.get(k).replace("\"", "");
                                        serverBrowserNamesToAddressTable.put(resp_servers[ctr], k.replace("\"", ""));
                                        ctr++;
                                    }
                                    String[] disp_servers = new String[]{
                                            "null",
                                            "null",
                                            "null",
                                            "null",
                                            "null",
                                            "null",
                                            "null",
                                            "null",
                                    };
                                    for(int i = 0; i < resp_servers.length; i++) {
                                        if(!resp_servers[i].isEmpty())
                                            disp_servers[i] = resp_servers[i];
                                    }
                                    uiMenus.menuSelection[uiMenus.MENU_SERVER_BROWSER].setMenuItemTexts(new String[]{
                                            "Get List",
                                            disp_servers[0],
                                            disp_servers[1],
                                            disp_servers[2],
                                            disp_servers[3],
                                            disp_servers[4],
                                            disp_servers[5],
                                            disp_servers[6],
                                            disp_servers[7]
                                    });
                                }
                                catch(Exception err) {
                                    err.printStackTrace();
                                    System.out.println("COULD NOT GET SERVER LIST");
                                }
                            }
                        },
                        new uiMenuItem("null"){
                            public void doItem() {
                                if(!this.text.equalsIgnoreCase("null")) {
                                    xMain.shellLogic.clientVars.put("joinip", serverBrowserNamesToAddressTable.get(this.text));
                                    xMain.shellLogic.console.ex("joingame;pause");
                                    selectedMenu = MENU_MAIN;
                                }
                            }
                        },
                        new uiMenuItem("null"){
                            public void doItem() {
                                if(!this.text.equalsIgnoreCase("null")) {
                                    xMain.shellLogic.clientVars.put("joinip", serverBrowserNamesToAddressTable.get(this.text));
                                    xMain.shellLogic.console.ex("joingame;pause");
                                    selectedMenu = MENU_MAIN;
                                }
                            }
                        },
                        new uiMenuItem("null"){
                            public void doItem() {
                                if(!this.text.equalsIgnoreCase("null")) {
                                    xMain.shellLogic.clientVars.put("joinip", serverBrowserNamesToAddressTable.get(this.text));
                                    xMain.shellLogic.console.ex("joingame;pause");
                                    selectedMenu = MENU_MAIN;
                                }
                            }
                        },
                        new uiMenuItem("null"){
                            public void doItem() {
                                if(!this.text.equalsIgnoreCase("null")) {
                                    xMain.shellLogic.clientVars.put("joinip", serverBrowserNamesToAddressTable.get(this.text));
                                    xMain.shellLogic.console.ex("joingame;pause");
                                    selectedMenu = MENU_MAIN;
                                }
                            }
                        },
                        new uiMenuItem("null"){
                            public void doItem() {
                                if(!this.text.equalsIgnoreCase("null")) {
                                    xMain.shellLogic.clientVars.put("joinip", serverBrowserNamesToAddressTable.get(this.text));
                                    xMain.shellLogic.console.ex("joingame;pause");
                                    selectedMenu = MENU_MAIN;
                                }
                            }
                        },
                        new uiMenuItem("null"){
                            public void doItem() {
                                if(!this.text.equalsIgnoreCase("null")) {
                                    xMain.shellLogic.clientVars.put("joinip", serverBrowserNamesToAddressTable.get(this.text));
                                    xMain.shellLogic.console.ex("joingame;pause");
                                    selectedMenu = MENU_MAIN;
                                }
                            }
                        },
                        new uiMenuItem("null"){
                            public void doItem() {
                                if(!this.text.equalsIgnoreCase("null")) {
                                    xMain.shellLogic.clientVars.put("joinip", serverBrowserNamesToAddressTable.get(this.text));
                                    xMain.shellLogic.console.ex("joingame;pause");
                                    selectedMenu = MENU_MAIN;
                                }
                            }
                        },
                        new uiMenuItem("null"){
                            public void doItem() {
                                if(!this.text.equalsIgnoreCase("null")) {
                                    xMain.shellLogic.clientVars.put("joinip", serverBrowserNamesToAddressTable.get(this.text));
                                    xMain.shellLogic.console.ex("joingame;pause");
                                    selectedMenu = MENU_MAIN;
                                }
                            }
                        },
                },
                MENU_MAIN
        ),
    };

    private static uiMenuItem[] getBotMenuItems() {
        uiMenuItem[] menuItems = new uiMenuItem[sSettings.botCountMax + 1];
        for(int i = 0; i < menuItems.length; i++) {
            menuItems[i] = new uiMenuItem(Integer.toString(i)){
                public void doItem() {
                    sSettings.botCount = Integer.parseInt(this.text);
                    menuSelection[MENU_NEWGAME].refresh();
                    selectedMenu = MENU_NEWGAME;
                }
            };
        }
        return menuItems;
    }

    public static void nextItem() {
        if(menuSelection[selectedMenu].selectedItem < menuSelection[selectedMenu].items.length-1)
            menuSelection[selectedMenu].selectedItem++;
        else
            menuSelection[selectedMenu].selectedItem = 0;
    }

    public static void prevItem() {
        if(menuSelection[selectedMenu].selectedItem > 0)
            menuSelection[selectedMenu].selectedItem--;
        else
            menuSelection[selectedMenu].selectedItem = menuSelection[selectedMenu].items.length-1;
    }

    private static uiMenuItem[] getMapMenuItems() {
        uiMenuItem[] items = new uiMenuItem[]{
            new uiMenuItem("random") {
                public void doItem() {
                    eManager.mapSelectionIndex = -1;
                    menuSelection[MENU_NEWGAME].refresh();
                    selectedMenu = MENU_NEWGAME;
                }
            }
        };
        for(int i = 0; i < eManager.mapsFileSelection.length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length - 1] = new uiMenuItem(eManager.mapsFileSelection[i]){
                public void doItem() {
                    for(int i = 0; i < eManager.mapsFileSelection.length; i++) {
                        if(eManager.mapsFileSelection[i].equals(text))
                            eManager.mapSelectionIndex = i;
                    }
                    menuSelection[MENU_NEWGAME].refresh();
                    selectedMenu = MENU_NEWGAME;
                }
            };
        }
        return items;
    }

    private static uiMenuItem[] getResolutionMenuItems() {
        uiMenuItem[] items = new uiMenuItem[] {};
        for(int i = 0; i < sSettings.resolutions.length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(sSettings.resolutions[i]){
                public void doItem() {
                    String[] toks = text.split("x");
                    xMain.shellLogic.console.ex("cl_setvar width " + toks[0]);
                    xMain.shellLogic.console.ex("cl_setvar height " + toks[1]);
                    uiMenus.menuSelection[uiMenus.MENU_VIDEO].refresh();
                    selectedMenu = MENU_VIDEO;
                }
            };
        }
        return items;
    }
    
    private static uiMenuItem[] getFramerateMenuItems() {
        uiMenuItem[] items = new uiMenuItem[sSettings.framerates.length];
        for(int i = 0; i < sSettings.framerates.length; i++){
            items[i] = new uiMenuItem(Integer.toString(sSettings.framerates[i])){
                public void doItem() {
                    xMain.shellLogic.console.ex("cl_setvar refresh " + text);
                    menuSelection[MENU_VIDEO].refresh();
                    selectedMenu = MENU_VIDEO;
                }
            };
        }
        return items;
    }

    private static uiMenuItem[] getColorMenuItems() {
        String[] selection = sSettings.colorSelection;
        uiMenuItem[] items = new uiMenuItem[] {};
        for (String s : selection) {
            items = Arrays.copyOf(items, items.length + 1);
            items[items.length - 1] = new uiMenuItem(s) {
                public void doItem() {
                    xMain.shellLogic.console.ex("cl_setvar playercolor " + text);
                    menuSelection[MENU_PROFILE].refresh();
                    selectedMenu = MENU_PROFILE;
                }
            };
        }
        return items;
    }

    private static uiMenuItem[] getControlMenuItems() {
        return new uiMenuItem[] {
                new uiMenuItem("action: Left Click"),
                new uiMenuItem("move up: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("playerup"))+" or ↑"),
                new uiMenuItem("move down: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("playerdown"))+" or ↓"),
                new uiMenuItem("move left: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("playerleft"))+" or ←"),
                new uiMenuItem("move right: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("playerright"))+" or →"),
                new uiMenuItem("show scoreboard: "+ KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("showscore"))),
                new uiMenuItem("chat: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("chat")))
        };
    }

    private static void setVolume(String val) {
        xMain.shellLogic.console.ex("cl_setvar volume " + val);
        menuSelection[MENU_AUDIO].refresh();
        selectedMenu = MENU_AUDIO;
    }
}
