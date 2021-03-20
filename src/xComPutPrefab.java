public class xComPutPrefab extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            gPrefabFactory factory = gPrefabFactory.instance();
            String prefabString = toks[1];
            if(factory.prefabLoadMap.containsKey(prefabString)) {
                gDoablePrefabReturn prefabReturn = factory.prefabLoadMap.get(prefabString);
                String argString = fullCommand.replace(toks[0] + " ",
                        "").replace(toks[1] + " ", "");
                String[] args = argString.split(" ");
                prefabReturn.storePrefab(prefabReturn.getPrefab(args), eManager.currentMap.scene);
            }
        }
        return "usage: putblock <BLOCK_TITLE> <args>";
    }
}
