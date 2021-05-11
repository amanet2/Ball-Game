public class uiMenusProfile extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                String.format("Name [%s]", sVars.get("playername")),
                String.format("Color [%s]", sVars.get("playercolor")),
                String.format("Hat [%s]", sVars.get("playerhat"))
        });
    }
    public uiMenusProfile() {
        super("Profile",
            new uiMenuItem[]{
                new uiMenuItem(String.format("Name [%s]", sVars.get("playername"))) {
                    public void doItem() {
                        xCon.ex("e_changename");
                        uiMenus.menuSelection[uiMenus.MENU_PROFILE].refresh();
                    }
                },
                new uiMenuItem(String.format("Color [%s]", sVars.get("playercolor"))) {
                    public void doItem() {
                        uiMenus.selectedMenu = uiMenus.MENU_COLOR;
                    }
                },
                new uiMenuItem(String.format("Hat [%s]", sVars.get("playerhat"))) {
                    public void doItem() {
                        uiMenus.selectedMenu = uiMenus.MENU_HAT;
                    }
                },
            },
            uiMenus.MENU_OPTIONS);
    }
}
