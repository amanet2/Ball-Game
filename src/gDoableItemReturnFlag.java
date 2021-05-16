public class gDoableItemReturnFlag extends gDoableItemReturn {
    public gItem getItem(String[] args) {
        gItemFlag flag = new gItemFlag(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        return flag;
    }
}
