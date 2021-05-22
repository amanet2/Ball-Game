public class uiMenusControls extends uiMenu {
    public static uiMenuItem[] getControlsMenuItems() {
        uiMenuItem[] gameControls = new uiMenuItem[] {
                new uiMenuItem("throw rock: MOUSE_LEFT"),
                new uiMenuItem("move up: "+(char)(int)xCon.getKeyCodeForComm("playerup")),
                new uiMenuItem("move down: "+(char)(int)xCon.getKeyCodeForComm("playerdown")),
                new uiMenuItem("move left: "+(char)(int)xCon.getKeyCodeForComm("playerleft")),
                new uiMenuItem("move right: "+(char)(int)xCon.getKeyCodeForComm("playerright")),
                new uiMenuItem("show scoreboard: TAB"),
                new uiMenuItem("chat: "+(char)(int)xCon.getKeyCodeForComm("chat"))
        };
        return gameControls;
    }

    public uiMenusControls() {
        super("Controls", getControlsMenuItems(), uiMenus.MENU_OPTIONS);
    }
}
