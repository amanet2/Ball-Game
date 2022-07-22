public class uiMenusNewGame extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                "-Start-",
                String.format("Map [%s]", eManager.mapSelectionIndex < 0 ? "<random map>"
                        : eManager.mapsFileSelection[eManager.mapSelectionIndex]),
        });
    }
    public uiMenusNewGame() {
        super("Host Game",
            new uiMenuItem[]{
                new uiMenuItem("-Start-"){
                    public void doItem() {
                        xCon.ex("newgame;joingame localhost " + cServerLogic.listenPort + ";pause");
                        uiMenus.selectedMenu = uiMenus.MENU_MAIN;
                    }
                },
                new uiMenuItem("MAP [<random map>]"){
                    public void doItem() {
                        uiMenus.selectedMenu = uiMenus.MENU_MAP;
                    }
                }
            },
            uiMenus.MENU_MAIN);
    }
}
