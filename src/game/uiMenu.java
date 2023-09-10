package game;

public class uiMenu {
    String title;
    uiMenuItem[] items;
    int parentMenu;
    int selectedItem;

    public void refresh() {

    }

    public void setMenuItemTexts(String[] newTitles) {
        for(int i = 0; i < items.length; i++) {
            items[i].text = newTitles[i];
        }
    }

    public uiMenu(String t, uiMenuItem[] i, int parent) {
        title = t;
        items = i;
        parentMenu = parent;
        selectedItem = -1;
    }
}