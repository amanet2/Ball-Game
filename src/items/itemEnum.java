package items;

public enum itemEnum {
    flag(0, "ITEM_FLAG");

    int index;
    String title;

    itemEnum(int index, String title) {
        this.index = index;
        this.title = title;
    }
}
