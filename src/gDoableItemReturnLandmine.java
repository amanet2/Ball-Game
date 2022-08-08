public class gDoableItemReturnLandmine extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        return new gItemLandmine(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
    }
}
