public class gDoableItemReturnFlagRed extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        gItemFlagRed flagred = new gItemFlagRed(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        return flagred;
    }
    public void storeItem(gItem itemToLoad, gScene sceneToStore) {
        super.storeItem(itemToLoad, sceneToStore);
    }
}
