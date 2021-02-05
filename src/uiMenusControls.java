import java.awt.event.KeyEvent;

public class uiMenusControls extends uiMenu {
    public static uiMenuItem[] getControlsMenuItems() {
        uiMenuItem[] gameControls = new uiMenuItem[] {
                new uiMenuItem("move up: "+(char)(int)xCon.getKeyCodeForComm("playerup")),
                new uiMenuItem("move down: "+(char)(int)xCon.getKeyCodeForComm("playerdown")),
                new uiMenuItem("move left: "+(char)(int)xCon.getKeyCodeForComm("playerleft")),
                new uiMenuItem("move right: "+(char)(int)xCon.getKeyCodeForComm("playerright")),
                new uiMenuItem("attack: L. Mouse"),
                new uiMenuItem("speed boost: Shift"),
                new uiMenuItem("drop weapon: Space"),
                new uiMenuItem("crouch: Ctrl"),
                new uiMenuItem("reload: "+(char)(int)xCon.getKeyCodeForComm("reload")),
                new uiMenuItem("flashlight: "+(char)(int)xCon.getKeyCodeForComm("flashlight")),
                new uiMenuItem("center camera: "+(char)(int)xCon.getKeyCodeForComm("centercamera")),
                new uiMenuItem("show scoreboard: Tab"),
                new uiMenuItem("chat: "+(char)(int)xCon.getKeyCodeForComm("chat")),
                new uiMenuItem("zoom in: "+(char)(int)xCon.getKeyCodeForComm("zoom")),
                new uiMenuItem("zoom out: "+(char)(int)xCon.getKeyCodeForComm("-zoom")),
        };
        return gameControls;
    }

    public static uiMenuItem[] getBindsAsMenuItems() {
        int size = xCon.instance().pressBinds.keySet().size() + xCon.instance().releaseBinds.keySet().size();
        for(Integer j : xCon.instance().releaseBinds.keySet()) {
            if(xCon.instance().pressBinds.containsKey(j)) {
                size--;
            }
        }
        uiMenuItem[] items = new uiMenuItem[size];
        int ctr = 0;
        for (Integer j : xCon.instance().pressBinds.keySet()) {
            items[ctr] = new uiMenuItem(KeyEvent.getKeyText(j)
                    +" : "+ xCon.instance().pressBinds.get(j)) {
                public void doItem(){

                }
            };
            ctr++;
        }
        for (Integer j : xCon.instance().releaseBinds.keySet()) {
            if(!xCon.instance().pressBinds.containsKey(j)) {
                items[ctr] = new uiMenuItem(KeyEvent.getKeyText(j)
                        + " : " + xCon.instance().releaseBinds.get(j)) {
                    public void doItem() {

                    }
                };
                ctr++;
            }
        }
        return items;
    }

    public uiMenusControls() {
        super("Controls", getControlsMenuItems(), uiMenus.MENU_OPTIONS);
    }
}
