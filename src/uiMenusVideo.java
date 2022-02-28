public class uiMenusVideo extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                String.format("Resolution [%dx%d]",sSettings.width,sSettings.height),
                String.format("Framerate [%d]",sSettings.framerate),
                String.format("Borderless [%s]", sVars.isIntVal("displaymode", oDisplay.displaymode_borderless)
                        ? "X" : "  "),
                String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  "),
                String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  "),
                String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  "),
                String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ")
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
                new uiMenuItem(String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  ")){
                    public void doItem() {
                        sSettings.vfxenableanimations = !sSettings.vfxenableanimations;
                        text = String.format("Animations [%s]", sSettings.vfxenableanimations ? "X" : "  ");
                    }
                },
                new uiMenuItem(String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  ")){
                    public void doItem() {
                        sSettings.vfxenableflares = !sSettings.vfxenableflares;
                        text = String.format("Flares [%s]", sSettings.vfxenableflares ? "X" : "  ");
                    }
                },
                new uiMenuItem(String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  ")){
                    public void doItem() {
                        sSettings.vfxenableshading = !sSettings.vfxenableshading;
                        text = String.format("Shading [%s]", sSettings.vfxenableshading ? "X" : "  ");
                    }
                },
                new uiMenuItem(String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ")){
                    public void doItem() {
                        sSettings.vfxenableshadows = !sSettings.vfxenableshadows;
                        text = String.format("Shadows [%s]", sSettings.vfxenableshadows ? "X" : "  ");
                    }
                }
            },
            uiMenus.MENU_OPTIONS);
    }
}
