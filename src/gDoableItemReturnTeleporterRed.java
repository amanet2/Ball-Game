public class gDoableItemReturnTeleporterRed extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        gItemTeleporterRed teleporterRed = new gItemTeleporterRed(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        return teleporterRed;
    }
}
