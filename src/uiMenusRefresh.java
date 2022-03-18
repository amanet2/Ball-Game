import java.util.Arrays;

public class uiMenusRefresh extends uiMenu {
    public uiMenusRefresh() {
        super("Refresh Rate (hz)", new uiMenuItem[]{}, uiMenus.MENU_VIDEO);
        setupMenuItems();
    }

    public void setupMenuItems() {
        super.setupMenuItems();
        for(int i = 0; i < sVars.getArray("framerates").length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(sVars.getArray("framerates")[i]){
                public void doItem() {
                    cClientVars.instance().put("vidmode",
                            String.format("%d,%d,%d",sSettings.width,sSettings.height,
                                    Integer.parseInt(text)));
                    uiMenus.selectedMenu = parentMenu;
                }
            };
            if(Integer.parseInt(items[items.length-1].text) == sSettings.framerate) {
                selectedItem = i;
            }
        }
    }
}
