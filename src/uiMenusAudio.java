public class uiMenusAudio extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                String.format("Mute Audio [%s]", sSettings.audioenabled ? "  " : "X"),
                String.format("Volume [%s]", sVars.get("volume"))
        });
    }

    public uiMenusAudio() {
        super("Audio",
            new uiMenuItem[]{
                new uiMenuItem(String.format("Mute Audio [%s]", sSettings.audioenabled ? "  " : "X")) {
                    public void doItem() {
                        cClientVars.instance().put("audioenabled", sSettings.audioenabled ? "0" : "1");
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
