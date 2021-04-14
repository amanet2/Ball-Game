public class gDoableItemReturnFlagBlue extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        gItemFlagBlue flagblue = new gItemFlagBlue(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        return flagblue;
    }
    public void storeItem(gItem itemToLoad, gScene sceneToStore) {
        super.storeItem(itemToLoad, sceneToStore);
    }
}
