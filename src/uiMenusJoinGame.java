public class uiMenusJoinGame extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                "-Start-",
                String.format("Join IP [%s]", xCon.ex("cl_setvar joinip")),
                String.format("Join Port [%s]", xCon.ex("cl_setvar joinport"))
        });
    }
    public uiMenusJoinGame() {
        super("Join Game",
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
            uiMenus.MENU_MAIN);
    }
}
