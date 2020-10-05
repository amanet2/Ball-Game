import java.util.Arrays;

public class uiMenusResolution extends uiMenu {
    public uiMenusResolution() {
        super("Resolution", new uiMenuItem[]{}, uiMenus.MENU_VIDEO);
        setupMenuItems();
    }

    public void setupMenuItems() {
        super.setupMenuItems();
        for(int i = 0; i < xCon.ex("resolutions").split(",").length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(xCon.ex("resolutions").split(",")[i]){
                public void doItem() {
                    String[] toks = text.split("x");
                    sVars.put("vidmode",
                            String.format("%d,%d,%d", Integer.valueOf(toks[0]), Integer.valueOf(toks[1]),
                                    sSettings.framerate));
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
