import java.awt.event.KeyEvent;
import java.util.Arrays;

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

    static int selectedMenu = MENU_MAIN;

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
                                selectedMenu = MENU_DISCONNECT;
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
                                selectedMenu = MENU_QUIT;
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
                                selectedMenu = (MENU_PROFILE);
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

                            public void refreshText() {
                                text = String.format("Resolution: [%dx%d]", sSettings.width, sSettings.height);
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
                                refreshText();
                            }
                            public void refreshText() {
                                text = String.format("Borderless [%s]", sSettings.borderless ? "X" : "  ");
                            }
                        },
                        new uiMenuItem(String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  ")){
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar vfxenableanimations "
                                        + (sSettings.vfxenableanimations ? "0" : "1"));
                                text = String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  ");
                            }
                        },
                        new uiMenuItem(String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  ")){
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar vfxenableflares " + (sSettings.vfxenableflares ? "0" : "1"));
                                text = String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  ");
                            }
                        },
                        new uiMenuItem(String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  ")){
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar vfxenableshading " + (sSettings.vfxenableshading ? "0" : "1"));
                                text = String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  ");
                            }
                        },
                        new uiMenuItem(String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ")){
                            public void doItem() {
                                xMain.shellLogic.console.ex("cl_setvar vfxenableshadows " + (sSettings.vfxenableshadows ? "0" : "1"));
                                text = String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ");
                            }
                        }
                },
                MENU_OPTIONS
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        String.format("Resolution [%dx%d]", sSettings.width,sSettings.height),
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
                "Host Game",
                new uiMenuItem[]{
                        new uiMenuItem("-Start-"){
                            public void doItem() {
                                xMain.shellLogic.console.ex("hostgame");
                                selectedMenu = MENU_MAIN;
                            }
                        },
                        new uiMenuItem("MAP [<random map>]"){
                            public void doItem() {
                                selectedMenu = MENU_MAP;
                            }
                        },
                        new uiMenuItem(String.format("Number of Bots [%d]", sSettings.botCount)){
                            public void doItem() {
                                sSettings.botCount++;
                                if(sSettings.botCount > 3)
                                    sSettings.botCount = 0;
                                this.text = String.format("Number of Bots [%d]", sSettings.botCount);
                            }
                        }
                },
                MENU_MAIN
        ) {
            public void refresh() {
                setMenuItemTexts(new String[]{
                        "-Start-",
                        String.format("Map [%s]", eManager.mapSelectionIndex < 0 ? "<random map>"
                                : eManager.mapsFileSelection[eManager.mapSelectionIndex]),
                        String.format("Number of Bots [%d]", sSettings.botCount),
                });
            }
        },
        new uiMenu(
                "Join Game",
                new uiMenuItem[]{
                        new uiMenuItem("-Start-"){
                            public void doItem() {
                                xMain.shellLogic.console.ex("joingame;pause");
                                selectedMenu = MENU_MAIN;
                            }
                        },
                        new uiMenuItem("Join IP []") {
                            public void doItem() {
                                xMain.shellLogic.console.ex("e_changejoinip");
                            }
                        },
                        new uiMenuItem("Join Port []") {
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
                        String.format("Join IP [%s]", xMain.shellLogic.console.ex("cl_setvar joinip")),
                        String.format("Join Port [%s]", xMain.shellLogic.console.ex("cl_setvar joinport"))
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
        new uiMenu("Select Resolution", getResolutionMenuItems(), MENU_VIDEO),
        new uiMenu("Select Framerate", getFramerateMenuItems(), MENU_VIDEO),
        new uiMenu("Select Map", getMapMenuItems(), MENU_NEWGAME),
        new uiMenu(
                "Volume Level",
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
        new uiMenu("Select Color", getColorMenuItems(), MENU_PROFILE),
        new uiMenu(
                "Credits",
                new uiMenuItem[] {
                        new uiMenuItem("Ballmaster 2021-2024"),
                        new uiMenuItem("Programming and Design by Anthony Manetti"),
                        new uiMenuItem("SFX by Kevin Fowler (hitrison.itch.io)"),
                        new uiMenuItem("SFX from mixkit.io"),
                        new uiMenuItem("VFX by Anthony Manetti"),
                        new uiMenuItem("VFX by drummyfish (opengameart.org)"),
                        new uiMenuItem("VFX by Master484 (opengameart.org)")
                },
                MENU_MAIN
        ),
        new uiMenu(
                "Disconnect?",
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

    private static uiMenuItem[] getMapMenuItems() {
        uiMenuItem[] items = new uiMenuItem[]{
            new uiMenuItem("<random map>") {
                public void doItem() {
                    eManager.mapSelectionIndex = -1;
                    menuSelection[MENU_NEWGAME].items[1].text = "MAP [<random map>]";
                    selectedMenu = MENU_NEWGAME;
                }
            }
        };
        for(int i = 0; i < eManager.mapsFileSelection.length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(eManager.mapsFileSelection[i]){
                public void doItem() {
                    for(int i = 0; i < eManager.mapsFileSelection.length; i++) {
                        if(eManager.mapsFileSelection[i].equals(text))
                            eManager.mapSelectionIndex = i;
                    }
                    if(eManager.mapSelectionIndex > -1) {
                        menuSelection[MENU_NEWGAME].items[1].text =
                                String.format("Map [%s]", eManager.mapsFileSelection[eManager.mapSelectionIndex]);
                    }
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
                    menuSelection[MENU_VIDEO].items[0].refreshText();
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
                    selectFramerateAfterSubmit();
                }
            };
        }
        return items;
    }
    
    private static void selectFramerateAfterSubmit() {
        menuSelection[MENU_VIDEO].refresh();
        selectedMenu = MENU_VIDEO;
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
                new uiMenuItem("throw rock: MOUSE_LEFT"),
                new uiMenuItem("move up: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("playerup"))),
                new uiMenuItem("move down: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("playerdown"))),
                new uiMenuItem("move left: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("playerleft"))),
                new uiMenuItem("move right: "+KeyEvent.getKeyText(xMain.shellLogic.console.getKeyCodeForComm("playerright"))),
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
