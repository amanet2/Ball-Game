import java.util.Arrays;

public class uiMenusRefresh extends uiMenu {
    public uiMenusRefresh() {
        super("Framerate Limit", new uiMenuItem[]{}, uiMenus.MENU_VIDEO);
        setupMenuItems();
    }

    public void setupMenuItems() {
        super.setupMenuItems();
        items = new uiMenuItem[]{
                new uiMenuItem("<None>") {
                    public void doItem() {
                        sSettings.framerate = -1;
                        afterSubmit();
                    }
                }
        };
        for(int i = 0; i < sSettings.framerates.length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(Integer.toString(sSettings.framerates[i])){
                public void doItem() {
                    sSettings.framerate = Integer.parseInt(text);
                    afterSubmit();
                }
            };
        }
    }

    private void afterSubmit() {
        cClientVars.instance().put("vidmode",
                String.format("%d,%d,%d", sSettings.width, sSettings.height,
                        sSettings.framerate));
        uiMenus.menuSelection[uiMenus.MENU_VIDEO].refresh();
        uiMenus.selectedMenu = parentMenu;
    }
}
