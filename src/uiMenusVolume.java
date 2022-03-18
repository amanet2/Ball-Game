import java.util.Arrays;

public class uiMenusVolume extends uiMenu {
    public uiMenusVolume() {
        super("Volume Level", new uiMenuItem[]{}, uiMenus.MENU_AUDIO);
        setupMenuItems();
    }

    public void setupMenuItems() {
        for(int i = 0; i <= 100; i+= 10){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(Integer.toString(i)){
                public void doItem() {
                    xCon.ex("volume " + text);
                    uiMenus.menuSelection[uiMenus.MENU_AUDIO].items[1].text =
                        String.format("Volume [%f]", cClientLogic.volume);
                    uiMenus.selectedMenu = uiMenus.MENU_AUDIO;
                }
            };
//            if(items[items.length-1].text.equals(cClientLogic.volume)) {
//                selectedItem = i;
//            }
        }
    }
}
