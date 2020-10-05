public class uiMenusOptions extends uiMenu {
    public uiMenusOptions() {
        super("Options",
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
            uiMenus.MENU_MAIN);
    }
}
