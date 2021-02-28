public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
        sSettings.net_server = true;
        sSettings.NET_MODE = sSettings.NET_SERVER;
        xCon.ex(String.format("load wp_steam.map"));
//        uiMenus.selectedMenu = uiMenus.MENU_MAIN;
//        //server
//        sSettings.net_client = false;
//        sSettings.net_server = true;
//        sSettings.NET_MODE = sSettings.NET_SERVER;
//        uiInterface.uuid = "server";
//        int toplay = eManager.mapSelectionIndex > -1 ? eManager.mapSelectionIndex
//                : (int)(Math.random()*eManager.mapsSelection.length);
//        eManager.mapSelectionIndex = toplay;
//        xCon.ex(String.format("load %s", eManager.mapsSelection[toplay]));
        return "new game started";
    }
}
