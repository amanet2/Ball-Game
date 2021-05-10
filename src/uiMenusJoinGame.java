public class uiMenusJoinGame extends uiMenu {
    public uiMenusJoinGame() {
        super("Join Game",
            new uiMenuItem[]{
                new uiMenuItem("-Start-"){
                    public void doItem() {
                        xCon.ex("joingame;pause");
                        uiMenus.selectedMenu = uiMenus.MENU_MAIN;
                    }
                },
                new uiMenuItem(String.format("Server IP [%s]", sVars.get("joinip"))) {
                    public void doItem() {
                        gMessages.enteringMessage = true;
                        gMessages.enteringOptionText = "New IP";
                    }
                },
                new uiMenuItem(String.format("Server Port [%s]",sVars.get("joinport"))) {
                    public void doItem() {
                        gMessages.enteringMessage = true;
                        gMessages.enteringOptionText = "New Port";
                    }
                }
            },
            uiMenus.MENU_MAIN);
    }
}
