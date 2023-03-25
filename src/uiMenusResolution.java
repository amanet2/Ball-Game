import java.util.Arrays;

public class uiMenusResolution extends uiMenu {
    public uiMenusResolution() {
        super("Resolution", new uiMenuItem[]{}, uiMenus.MENU_VIDEO);
        setupMenuItems();
    }

    public void setupMenuItems() {
        super.setupMenuItems();
        for(int i = 0; i < sSettings.resolutions.length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(sSettings.resolutions[i]){
                public void doItem() {
                    String[] toks = text.split("x");
                    xCon.ex(String.format("cl_setvar vidmode %s,%s,%d", toks[0], toks[1], sSettings.framerate));
                    uiMenus.menuSelection[uiMenus.MENU_VIDEO].items[0].refreshText();
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
