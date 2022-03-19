public class uiMenusJoinGame extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                "-Start-",
                String.format("Join IP [%s]", cClientLogic.joinip),
                String.format("Join Port [%s]", cClientLogic.joinport)
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
                new uiMenuItem(String.format("Join IP [%s]", cClientLogic.joinip)) {
                    public void doItem() {
                        xCon.ex("e_changejoinip");
//                        text = String.format("Join IP [%s]", cClientLogic.joinip);
                    }
                },
                new uiMenuItem(String.format("Join Port [%s]",cClientLogic.joinport)) {
                    public void doItem() {
                        xCon.ex("e_changejoinport");
//                        text = String.format("Join Port [%s]", cClientLogic.joinport);
                    }
                }
            },
            uiMenus.MENU_MAIN);
    }
}
