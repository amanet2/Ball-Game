public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
        uiMenus.selectedMenu = uiMenus.MENU_MAIN;
        //server
        sSettings.net_client = false;
        sSettings.net_server = true;
        uiInterface.uuid = "server";
        int toplay = eManager.mapSelectionIndex > -1 ? eManager.mapSelectionIndex
                : (int)(Math.random()*eManager.mapsSelection.length);
        eManager.mapSelectionIndex = toplay;
        xCon.ex(String.format("load %s", eManager.mapsSelection[toplay]));
        return "new game started";
    }
}
