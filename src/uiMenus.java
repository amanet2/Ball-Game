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
    static final int MENU_REFRESH = 10;
    static final int MENU_MAP = 11;
    static final int MENU_VOLUME = 12;
    static final int MENU_COLOR = 13;
    static final int MENU_CREDITS = 14;

    static int selectedMenu = MENU_MAIN;

    static void init() {
        menuSelection[MENU_MAP].setupMenuItems();
//        menuSelection[MENU_CONTROLS].items = uiMenusControls.getControlsMenuItems();
    }

    static final uiMenu[] menuSelection = new uiMenu[]{
        new uiMenu(
                "Welcome",
                new uiMenuItem[]{
                        new uiMenuItem("Host Game") {
                            public void doItem(){
                                selectedMenu = (MENU_NEWGAME);
                            }
                        },
                        new uiMenuItem("Join Game") {
                            public void doItem(){
                                selectedMenu = (MENU_JOINGAME);
                            }
                        },
                        new uiMenuItem("Disconnect") {
                            public void doItem(){
                                if(xCon.ex("e_showlossalert").equals("0"))
                                    xCon.ex("disconnect");
                            }
                        },
                        new uiMenuItem("Options") {
                            public void doItem(){
                                selectedMenu = (MENU_OPTIONS);
                            }
                        },
                        new uiMenuItem("Credits") {
                            public void doItem(){
                                selectedMenu = (MENU_CREDITS);
                            }
                        },
                        new uiMenuItem("Quit") {
                            public void doItem(){
                                selectedMenu = (MENU_QUIT);
                            }
                        }
                },
                -1
        ),
        new uiMenu(
                "Quit Game?",
                new uiMenuItem[]{
                        new uiMenuItem("No") {
                            public void doItem(){
                                selectedMenu = MENU_MAIN;
                            }
                        },
                        new uiMenuItem("Yes") {
                            public void doItem(){
                                xCon.ex("quit");
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
                                uiMenus.selectedMenu = (uiMenus.MENU_CONTROLS);
                            }
                        },
                        new uiMenuItem("Audio") {
                            public void doItem(){
                                uiMenus.menuSelection[uiMenus.MENU_AUDIO].refresh();
                                uiMenus.selectedMenu = (uiMenus.MENU_AUDIO);
                            }
                        },
                        new uiMenuItem("Video") {
                            public void doItem(){
                                uiMenus.menuSelection[uiMenus.MENU_VIDEO].refresh();
                                uiMenus.selectedMenu = (uiMenus.MENU_VIDEO);
                            }
                        },
                        new uiMenuItem("Profile") {
                            public void doItem(){
                                uiMenus.menuSelection[uiMenus.MENU_PROFILE].refresh();
                                uiMenus.selectedMenu = (uiMenus.MENU_PROFILE);
                            }
                        }
                },
                uiMenus.MENU_MAIN
        ),
        new uiMenu(
                "Controls",
                new uiMenuItem[] {
                        new uiMenuItem("throw rock: MOUSE_LEFT"),
                        new uiMenuItem("move up: "+(char)(int)xCon.getKeyCodeForComm("playerup")),
                        new uiMenuItem("move down: "+(char)(int)xCon.getKeyCodeForComm("playerdown")),
                        new uiMenuItem("move left: "+(char)(int)xCon.getKeyCodeForComm("playerleft")),
                        new uiMenuItem("move right: "+(char)(int)xCon.getKeyCodeForComm("playerright")),
                        new uiMenuItem("show scoreboard: TAB"),
                        new uiMenuItem("chat: "+(char)(int)xCon.getKeyCodeForComm("chat"))
                },
                uiMenus.MENU_OPTIONS
        ),
        new uiMenu(
                "Video",
                new uiMenuItem[]{
                        new uiMenuItem(String.format("Resolution [%dx%d]",sSettings.width,sSettings.height)) {
                            public void doItem() {
                                uiMenus.selectedMenu = (uiMenus.MENU_RESOLUTION);
                            }

                            public void refreshText() {
                                text = String.format("Resolution: [%dx%d]", sSettings.width, sSettings.height);
                            }
                        },
                        new uiMenuItem(String.format("Framerate [%d]",sSettings.framerate)) {
                            public void doItem() {
                                uiMenus.selectedMenu = uiMenus.MENU_REFRESH;
                            }
                        },
                        new uiMenuItem(String.format("Borderless [%s]",
                                sSettings.displaymode == oDisplay.displaymode_borderless ? "X" : "  ")) {
                            public void doItem() {
                                cClientVars.instance().put("displaymode",
                                        sSettings.displaymode == oDisplay.displaymode_windowed ? "1" : "0");
                                oDisplay.instance().refreshDisplaymode();
                                refreshText();
                            }
                            public void refreshText() {
                                text = String.format("Borderless [%s]",
                                        sSettings.displaymode == oDisplay.displaymode_borderless ? "X" : "  ");
                            }
                        },
                        new uiMenuItem(String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  ")){
                            public void doItem() {
                                sSettings.vfxenableanimations = !sSettings.vfxenableanimations;
                                cClientVars.instance().put("vfxenableanimations", sSettings.vfxenableanimations ? "1" : "0");
                                text = String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  ");
                            }
                        },
                        new uiMenuItem(String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  ")){
                            public void doItem() {
                                sSettings.vfxenableflares = !sSettings.vfxenableflares;
                                cClientVars.instance().put("vfxenableflares", sSettings.vfxenableflares ? "1" : "0");
                                text = String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  ");
                            }
                        },
                        new uiMenuItem(String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  ")){
                            public void doItem() {
                                sSettings.vfxenableshading = !sSettings.vfxenableshading;
                                cClientVars.instance().put("vfxenableshading", sSettings.vfxenableshading ? "1" : "0");
                                text = String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  ");
                            }
                        },
                        new uiMenuItem(String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ")){
                            public void doItem() {
                                sSettings.vfxenableshadows = !sSettings.vfxenableshadows;
                                cClientVars.instance().put("vfxenableshadows", sSettings.vfxenableshadows ? "1" : "0");
                                text = String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ");
                            }
                        }
                },
                uiMenus.MENU_OPTIONS
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        String.format("Resolution [%dx%d]",sSettings.width,sSettings.height),
                        String.format("Framerate [%d]",sSettings.framerate),
                        String.format("Borderless [%s]", sSettings.displaymode == oDisplay.displaymode_borderless
                                ? "X" : "  "),
                        String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  "),
                        String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  "),
                        String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  "),
                        String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ")
                });
            }
        },
        new uiMenu(
                "Host Game",
                new uiMenuItem[]{
                        new uiMenuItem("-Start-"){
                            public void doItem() {
                                xCon.ex(String.format("exec scripts/hostgame %d", cServerLogic.listenPort));
                                uiMenus.selectedMenu = uiMenus.MENU_MAIN;
                            }
                        },
                        new uiMenuItem("MAP [<random map>]"){
                            public void doItem() {
                                uiMenus.selectedMenu = uiMenus.MENU_MAP;
                            }
                        }
                },
                uiMenus.MENU_MAIN
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        "-Start-",
                        String.format("Map [%s]", eManager.mapSelectionIndex < 0 ? "<random map>"
                                : eManager.mapsFileSelection[eManager.mapSelectionIndex]),
                });
            }
        },
        new uiMenu(
                "Join Game",
                new uiMenuItem[]{
                        new uiMenuItem("-Start-"){
                            public void doItem() {
                                xCon.ex("joingame;pause");
                                uiMenus.selectedMenu = uiMenus.MENU_MAIN;
                            }
                        },
                        new uiMenuItem("Join IP []") {
                            public void doItem() {
                                xCon.ex("e_changejoinip");
                            }
                        },
                        new uiMenuItem("Join Port []") {
                            public void doItem() {
                                xCon.ex("e_changejoinport");
                            }
                        }
                },
                uiMenus.MENU_MAIN
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        "-Start-",
                        String.format("Join IP [%s]", xCon.ex("cl_setvar joinip")),
                        String.format("Join Port [%s]", xCon.ex("cl_setvar joinport"))
                });
            }
        },
        new uiMenu(
                "Profile",
                new uiMenuItem[]{
                        new uiMenuItem(String.format("Name [%s]", cClientLogic.playerName)) {
                            public void doItem() {
                                xCon.ex("e_changeplayername");
                            }
                        },
                        new uiMenuItem(String.format("Color [%s]", cClientLogic.playerColor)) {
                            public void doItem() {
                                uiMenus.selectedMenu = uiMenus.MENU_COLOR;
                            }
                        },
                },
                uiMenus.MENU_OPTIONS
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        String.format("Name [%s]", cClientLogic.playerName),
                        String.format("Color [%s]", cClientLogic.playerColor),
                });
            }
        },
        new uiMenu(
                "Audio",
                new uiMenuItem[]{
                        new uiMenuItem(String.format("Mute Audio [%s]", sSettings.audioenabled ? "  " : "X")) {
                            public void doItem() {
                                cClientVars.instance().put("audioenabled", sSettings.audioenabled ? "0" : "1");
                                uiMenus.menuSelection[uiMenus.MENU_AUDIO].refresh();
                            }
                        },
                        new uiMenuItem(String.format("Volume [%f]", cClientLogic.volume)) {
                            public void doItem() {
                                uiMenus.selectedMenu = uiMenus.MENU_VOLUME;
                            }
                        }
                },
                uiMenus.MENU_OPTIONS
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        String.format("Mute Audio [%s]", sSettings.audioenabled ? "  " : "X"),
                        String.format("Volume [%f]", cClientLogic.volume)
                });
            }
        },
        new uiMenusResolution(),
        new uiMenusRefresh(),
        new uiMenusMap(),
        new uiMenusVolume(),
        new uiMenusColor(),
        new uiMenu(
                "Credits",
                new uiMenuItem[] {
                        new uiMenuItem("by Stallion 2021-2023"),
                        new uiMenuItem("venmo @StallionUSA")
                },
                uiMenus.MENU_MAIN
        )
    };

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
}
