public class gDoableItemReturnTeleporterBlue extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        gItemTeleporterBlue teleporterBlue = new gItemTeleporterBlue(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        return teleporterBlue;
    }
}
