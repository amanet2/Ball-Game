import java.util.Arrays;

public class uiMenusHat extends uiMenu {
    public uiMenusHat() {
        super("Select Hat", new uiMenuItem[]{}, uiMenus.MENU_PROFILE);
        setupMenuItems();
    }

    public void setupMenuItems() {
        String[] selection = sVars.get("hatselection").split(",");
        for(int i = 0; i < selection.length;i++){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(selection[i]){
                public void doItem() {
                    sVars.put("playerhat",text);
                    uiMenus.menuSelection[uiMenus.MENU_PROFILE].items[2].text =
                        String.format("Hat: [%s]", sVars.get("playerhat"));
                    uiMenus.selectedMenu = parentMenu;
                }
            };
            if(items[items.length-1].text.equals(sVars.get("playerhat"))) {
                selectedItem = i;
            }
        }
    }
}
