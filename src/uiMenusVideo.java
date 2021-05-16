public class uiMenusVideo extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                String.format("Resolution [%dx%d]",sSettings.width,sSettings.height),
                String.format("Framerate [%d]",sSettings.framerate),
                String.format("Borderless [%s]", sVars.isIntVal("displaymode", oDisplay.displaymode_borderless)
                        ? "X" : "  "),
                String.format("Animations [%s]", sVars.isOne("vfxenableanimations") ? "X" : "  "),
                String.format("Flares [%s]", sVars.isOne("vfxenableflares") ? "X" : "  "),
                String.format("Shading [%s]", sVars.isOne("vfxenableshading") ? "X" : "  "),
                String.format("Shadows [%s]", sVars.isOne("vfxenableshadows") ? "X" : "  ")
        });
    }

    public uiMenusVideo() {
        super("Video",
            new uiMenuItem[]{
                new uiMenuItem(String.format("Resolution [%dx%d]",sSettings.width,sSettings.height)) {
                    public void doItem() {
                        uiMenus.selectedMenu = (uiMenus.MENU_RESOLUTION);
                    }
                },
                new uiMenuItem(String.format("Framerate [%d]",sSettings.framerate)) {
                    public void doItem() {
                        uiMenus.selectedMenu = uiMenus.MENU_REFRESH;
                    }
                },
                new uiMenuItem(String.format("Borderless [%s]",
                        sVars.isIntVal("displaymode", oDisplay.displaymode_borderless) ? "X" : "  ")) {
                    public void doItem() {
                        sVars.putInt("displaymode", sVars.isIntVal("displaymode", oDisplay.displaymode_windowed)
                                ? oDisplay.displaymode_borderless : oDisplay.displaymode_windowed);
                        text = String.format("Borderless [%s]",
                                sVars.isIntVal("displaymode", oDisplay.displaymode_borderless) ? "X" : "  ");
                        oDisplay.instance().createPanels();
                        oDisplay.instance().showFrame();
                    }
                },
                new uiMenuItem(String.format("Animations [%s]", sVars.isOne("vfxenableanimations") ? "X" : "  ")){
                    public void doItem() {
                        sVars.put("vfxenableanimations", sVars.isOne("vfxenableanimations") ? "0" : "1");
                        text = String.format("Animations [%s]", sVars.isOne("vfxenableanimations") ? "X" : "  ");
                    }
                },
                new uiMenuItem(String.format("Flares [%s]", sVars.isOne("vfxenableflares") ? "X" : "  ")){
                    public void doItem() {
                        sVars.put("vfxenableflares", sVars.isOne("vfxenableflares") ? "0" : "1");
                        text = String.format("Flares [%s]", sVars.isOne("vfxenableflares") ? "X" : "  ");
                    }
                },
                new uiMenuItem(String.format("Shading [%s]", sVars.isOne("vfxenableshading") ? "X" : "  ")){
                    public void doItem() {
                        sVars.put("vfxenableshading", sVars.isOne("vfxenableshading") ? "0" : "1");
                        text = String.format("Shading [%s]", sVars.isOne("vfxenableshading") ? "X" : "  ");
                    }
                },
                new uiMenuItem(String.format("Shadows [%s]", sVars.isOne("vfxenableshadows") ? "X" : "  ")){
                    public void doItem() {
                        sVars.put("vfxenableshadows", sVars.isOne("vfxenableshadows") ? "0" : "1");
                        text = String.format("Shadows [%s]", sVars.isOne("vfxenableshadows") ? "X" : "  ");
                    }
                }
            },
            uiMenus.MENU_OPTIONS);
    }
}
