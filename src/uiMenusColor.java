import java.util.Arrays;

public class uiMenusColor extends uiMenu {
    public uiMenusColor() {
        super("Select Color", new uiMenuItem[]{}, uiMenus.MENU_PROFILE);
        setupMenuItems();
    }

    public void setupMenuItems() {
        String[] selection = sSettings.colorSelection;
        for(int i = 0; i < selection.length;i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(selection[i]){
                public void doItem() {
                    String color = text;
                    gPlayer user = cClientLogic.getUserPlayer();
                    cClientLogic.playerColor = color;
                    uiMenus.menuSelection[uiMenus.MENU_PROFILE].refresh();
                    if(user != null) {
                        user.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a00.png", color)));
                    }
                    uiMenus.selectedMenu = parentMenu;
                }
            };
            if(items[items.length-1].text.equals(cClientLogic.playerColor)) {
                selectedItem = i;
            }
        }
    }
}
