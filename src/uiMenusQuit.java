public class uiMenusQuit extends uiMenu {
    public uiMenusQuit() {
        super("Quit Game?",
            new uiMenuItem[]{
                new uiMenuItem("No") {
                    public void doItem(){
                        uiMenus.selectedMenu = (uiMenus.MENU_MAIN);
                    }
                },
                new uiMenuItem("Yes") {
                    public void doItem(){
                        xCon.ex("quit");
                    }
                }
            },
            uiMenus.MENU_MAIN);
    }
}
