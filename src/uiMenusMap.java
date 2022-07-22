import java.util.Arrays;

public class uiMenusMap extends uiMenu {
    public uiMenusMap() {
        super("Select Map", new uiMenuItem[]{}, uiMenus.MENU_NEWGAME);
        setupMenuItems();
    }

    public void setupMenuItems() {
        super.setupMenuItems();
        items = new uiMenuItem[]{
                new uiMenuItem("<random map>") {
                    public void doItem() {
                        eManager.mapSelectionIndex = -1;
                        uiMenus.menuSelection[uiMenus.MENU_NEWGAME].items[1].text = "MAP [<random map>]";
                        uiMenus.selectedMenu = parentMenu;
                    }
                }
        };
        for(int i = 0; i < eManager.mapsFileSelection.length; i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(eManager.mapsFileSelection[i]){
                public void doItem() {
                    for(int i = 0; i < eManager.mapsFileSelection.length; i++) {
                        if(eManager.mapsFileSelection[i].equals(text))
                            eManager.mapSelectionIndex = i;
                    }
                    if(eManager.mapSelectionIndex > -1) {
                        uiMenus.menuSelection[uiMenus.MENU_NEWGAME].items[1].text =
                                String.format("Map [%s]", eManager.mapsFileSelection[eManager.mapSelectionIndex]);
                    }
                    uiMenus.selectedMenu = parentMenu;
                }
            };
        }
    }
}
