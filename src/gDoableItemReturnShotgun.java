public class gDoableItemReturnShotgun extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        gItemShotgun shotgun = new gItemShotgun(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        return shotgun;
    }
}
