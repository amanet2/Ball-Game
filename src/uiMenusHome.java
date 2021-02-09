public class uiMenusHome extends uiMenu {
    public uiMenusHome() {
        super("Welcome",
            new uiMenuItem[]{
                new uiMenuItem("New Game") {
                    public void doItem(){
                        uiMenus.selectedMenu = (uiMenus.MENU_NEWGAME);
                    }
                },
                new uiMenuItem("Join Game") {
                    public void doItem(){
                        uiMenus.selectedMenu = (uiMenus.MENU_JOINGAME);
                    }
                },
                new uiMenuItem("Options") {
                    public void doItem(){
                        uiMenus.selectedMenu = (uiMenus.MENU_OPTIONS);
                    }
                },
                new uiMenuItem("Credits") {
                    public void doItem(){
                        uiMenus.selectedMenu = (uiMenus.MENU_CREDITS);
                    }
                },
                new uiMenuItem("Quit") {
                    public void doItem(){
                        uiMenus.selectedMenu = (uiMenus.MENU_QUIT);
                    }
                }
            },
            -1);
    }
}
