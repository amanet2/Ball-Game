import java.awt.event.KeyEvent;

public class uiMenusControls extends uiMenu {
    public static uiMenuItem[] getControlsMenuItems() {
        uiMenuItem[] gameControls = new uiMenuItem[] {
                new uiMenuItem("player up: W"),
                new uiMenuItem("player down: S"),
                new uiMenuItem("player left: A"),
                new uiMenuItem("player right: D"),
                new uiMenuItem("attack: Mouse Left"),
                new uiMenuItem("speed boost: Mouse Right"),
                new uiMenuItem("jump: SPACE"),
                new uiMenuItem("crouch: L.Ctrl"),
                new uiMenuItem("reload weapon: R"),
                new uiMenuItem("flashlight: F"),
                new uiMenuItem("center camera: E"),
                new uiMenuItem("show scoreboard: Tab"),
                new uiMenuItem("chat message: Y"),
                new uiMenuItem("zoom in: ="),
                new uiMenuItem("zoom out: -"),
                new uiMenuItem("console: ~")
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
