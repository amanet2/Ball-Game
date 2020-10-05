public class uiMenusAudio extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                String.format("Mute Audio [%s]", sVars.isOne("audioenabled") ? "  " : "X"),
                String.format("Volume [%s]", sVars.get("volume"))
        });
    }

    public uiMenusAudio() {
        super("Audio",
            new uiMenuItem[]{
                new uiMenuItem(String.format("Mute Audio [%s]", sVars.isOne("audioenabled") ? "  " : "X")) {
                    public void doItem() {
                        sVars.put("audioenabled", sVars.isOne("audioenabled") ? "0" : "1");
                        uiMenus.menuSelection[uiMenus.MENU_AUDIO].refresh();
                    }
                },
                new uiMenuItem(String.format("Volume [%s]", sVars.get("volume"))) {
                    public void doItem() {
                        uiMenus.selectedMenu = uiMenus.MENU_VOLUME;
                    }
                }
            },
            uiMenus.MENU_OPTIONS);
    }
}
