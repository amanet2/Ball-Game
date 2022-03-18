public class uiMenusProfile extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                String.format("Name [%s]", cClientLogic.playerName),
                String.format("Color [%s]", cClientLogic.playerColor),
        });
    }
    public uiMenusProfile() {
        super("Profile",
            new uiMenuItem[]{
                new uiMenuItem(String.format("Name [%s]", cClientLogic.playerName)) {
                    public void doItem() {
                        xCon.ex("e_changeplayername");
                        text = String.format("Name [%s]", cClientLogic.playerName);
                    }
                },
                new uiMenuItem(String.format("Color [%s]", cClientLogic.playerColor)) {
                    public void doItem() {
                        uiMenus.selectedMenu = uiMenus.MENU_COLOR;
                    }
                },
            },
            uiMenus.MENU_OPTIONS);
    }
}
