public class xComGetNewItemId extends xCom {
    public String doCommand(String fullCommand) {
        return Integer.toString(cServerLogic.getNewItemId());
    }
}
