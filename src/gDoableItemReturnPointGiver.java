public class gDoableItemReturnPointGiver extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        return new gItemPointGiver(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
    }
}
