import java.util.Arrays;

public class uiMenusColor extends uiMenu {
    public uiMenusColor() {
        super("Select Color", new uiMenuItem[]{}, uiMenus.MENU_PROFILE);
        setupMenuItems();
    }

    public void setupMenuItems() {
        String[] selection = sVars.get("colorselection").split(",");
        for(int i = 0; i < selection.length;i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(selection[i]){
                public void doItem() {
                    sVars.put("playercolor", text);
                    uiMenus.menuSelection[uiMenus.MENU_PROFILE].items[1].text =
                        String.format("Color: [%s]", sVars.get("playercolor"));
                    if(eManager.currentMap.scene.players().size() > 0) {
                        cGameLogic.userPlayer().setSpriteFromPath(
                                eUtils.getPath(String.format("animations/player_%s/a00.png", sVars.get("playercolor"))));
                        xCon.ex("THING_PLAYER.0.color playercolor");
                    }
                    uiMenus.selectedMenu = parentMenu;
                }
            };
            if(items[items.length-1].text.equals(sVars.get("playercolor"))) {
                selectedItem = i;
            }
        }
    }
}
