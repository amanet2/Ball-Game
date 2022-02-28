public class uiMenusProfile extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                String.format("Name [%s]", sVars.get("playername")),
                String.format("Color [%s]", sVars.get("playercolor")),
        });
    }
    public uiMenusProfile() {
        super("Profile",
            new uiMenuItem[]{
                new uiMenuItem(String.format("Name [%s]", sVars.get("playername"))) {
                    public void doItem() {
                        xCon.ex("e_changeplayername");
                        text = String.format("Name [%s]", sVars.get("playername"));
                    }
                },
                new uiMenuItem(String.format("Color [%s]", sVars.get("playercolor"))) {
                    public void doItem() {
                        uiMenus.selectedMenu = uiMenus.MENU_COLOR;
                    }
                },
            },
            uiMenus.MENU_OPTIONS);
    }
}
