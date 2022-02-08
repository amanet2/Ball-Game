public class xComEditorRotNewThing extends xCom {
    public String doCommand(String fullCommand) {
        String newprefabname = cVars.get("newprefabname");
        if (newprefabname.contains("_000"))
            cVars.put("newprefabname", newprefabname.replace("_000", "_090"));
        else if (newprefabname.contains("_090"))
            cVars.put("newprefabname", newprefabname.replace("_090", "_180"));
        else if (newprefabname.contains("_180"))
            cVars.put("newprefabname", newprefabname.replace("_180", "_270"));
        else if (newprefabname.contains("_270"))
            cVars.put("newprefabname", newprefabname.replace("_270", "_000"));
        if(newprefabname.contains("_000")
        || newprefabname.contains("_090")
        || newprefabname.contains("_180")
        || newprefabname.contains("_270")) {
            uiEditorMenus.previewScene = new gScene();
//            xCon.ex("cl_clearthingmappreview");
            xCon.ex(String.format("cl_execpreview prefabs/%s 12500 6000", cVars.get("newprefabname")));
        }
        return "";
    }
}
