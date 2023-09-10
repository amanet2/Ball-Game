package items;

import game.gThing;

public class itemTemplate {
    public int index = 0;
    public String title = "DEFAULT_ITEM";

    public void activateItem() {
        System.out.println(index + "_" + title);
    }

    public void activateItem(gThing p) {
        activateItem();
    }
}
