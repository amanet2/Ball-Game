public class xComDisconnect extends xCom {
    public String doCommand(String fullCommand) {
        nClient.instance().disconnect();
        xCon.ex("cl_load");
        if (uiInterface.inplay)
            xCon.ex("pause");
        return fullCommand;
    }
}
