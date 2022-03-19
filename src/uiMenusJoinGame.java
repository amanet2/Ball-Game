public class uiMenusJoinGame extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                "-Start-",
                String.format("Server IP [%s]", cClientLogic.joinip),
                String.format("Server Port [%s]", sVars.get("joinport"))
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
                new uiMenuItem(String.format("Server IP [%s]", cClientLogic.joinip)) {
                    public void doItem() {
                        xCon.ex("e_changejoinip");
                        text = String.format("Server IP [%s]", cClientLogic.joinip);
                    }
                },
                new uiMenuItem(String.format("Server Port [%s]",sVars.get("joinport"))) {
                    public void doItem() {
                        xCon.ex("e_changejoinport");
                        text = String.format("Server Port [%s]", sVars.get("joinport"));
                    }
                }
            },
            uiMenus.MENU_MAIN);
    }
}
