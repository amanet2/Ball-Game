import java.util.Arrays;

public class uiMenusResolution extends uiMenu {
    public uiMenusResolution() {
        super("Resolution", new uiMenuItem[]{}, uiMenus.MENU_VIDEO);
        setupMenuItems();
    }

    public void setupMenuItems() {
        super.setupMenuItems();
        for(int i = 0; i < sVars.getArray("resolutions").length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(sVars.getArray("resolutions")[i]){
                public void doItem() {
                    String[] toks = text.split("x");
                    cClientVars.instance().put("vidmode",
                            String.format("%d,%d,%d", Integer.parseInt(toks[0]), Integer.parseInt(toks[1]),
                            sSettings.framerate) );
                    uiMenus.selectedMenu = parentMenu;
                }
            };
            if(Integer.parseInt(items[items.length-1].text.split("x")[0]) == sSettings.width &&
            Integer.parseInt(items[items.length-1].text.split("x")[1]) == sSettings.height) {
                selectedItem = i;
            }
        }
    }
}
