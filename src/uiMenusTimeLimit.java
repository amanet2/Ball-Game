import java.util.Arrays;

public class uiMenusTimeLimit extends uiMenu {
    public uiMenusTimeLimit() {
        super("Time Limit", new uiMenuItem[]{}, uiMenus.MENU_NEWGAME);
        setupMenuItems();
    }

    public void setupMenuItems() {
        for(int i = 0; i <= 100; i+= 10){
            items = Arrays.copyOf(items,items.length+1);
            items[items.length-1] = new uiMenuItem(Integer.toString(i)){
                public void doItem() {
                    if(text.equals("1:00"))
                        sVars.putInt("timelimit", 60000);
                    if(text.equals("2:00"))
                        sVars.putInt("timelimit", 60000);
                    if(text.equals("3:00"))
                        sVars.putInt("timelimit", 60000);
                    if(text.equals("4:00"))
                        sVars.putInt("timelimit", 60000);
                    xCon.ex("timelimit " + text);
                    uiMenus.menuSelection[uiMenus.MENU_AUDIO].items[1].text =
                        String.format("Volume [%s]", sVars.get("volume"));
                    uiMenus.selectedMenu = uiMenus.MENU_AUDIO;
                }
            };
            if(items[items.length-1].text.equals(sVars.get("volume"))) {
                selectedItem = i;
            }
        }
    }
}
