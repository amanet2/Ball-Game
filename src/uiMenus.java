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

    static final uiMenu[] menuSelection = new uiMenu[]{
        new uiMenusHome(),
        new uiMenusQuit(),
        new uiMenusOptions(),
        new uiMenusControls(),
        new uiMenusVideo(),
        new uiMenusNewGame(),
        new uiMenusJoinGame(),
        new uiMenusProfile(),
        new uiMenusAudio(),
        new uiMenusResolution(),
        new uiMenusRefresh(),
        new uiMenusMap(),
        new uiMenusVolume(),
        new uiMenusColor(),
        new uiMenusCredits()
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
