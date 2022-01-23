public class xComEditorRotNewThing extends xCom {
    public String doCommand(String fullCommand) {
        String newprefabname = cVars.get("newprefabname");
        if(newprefabname.contains("_000"))
            cVars.put("newprefabname", newprefabname.replace("_000", "_090"));
        else if(newprefabname.contains("_090"))
            cVars.put("newprefabname", newprefabname.replace("_090", "_180"));
        else if(newprefabname.contains("_180"))
            cVars.put("newprefabname", newprefabname.replace("_180", "_270"));
        else if(newprefabname.contains("_270"))
            cVars.put("newprefabname", newprefabname.replace("_270", "_000"));
        return "";
    }
}
