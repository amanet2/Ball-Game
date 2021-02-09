public class uiMenusNewGame extends uiMenu {
    public void refresh() {
        setMenuItemTexts(new String[]{
                "-Start-",
                String.format("Map [%s]", eManager.mapSelectionIndex < 0 ? "<random map>"
                        : eManager.mapsSelection[eManager.mapSelectionIndex]),
                String.format("Score Limit [%s]", sVars.get("scorelimit")),
                String.format("Time Limit [%s]", Double.toString((double)sVars.getInt("timelimit")/60000.0)),
                String.format("Number of Bots [%s]", sVars.get("botcount")),
                String.format("Powerups on Map [%s]", cVars.get("powerupson")),
                String.format("Team Game [%s]", cVars.isOne("gameteam") ? "X" : "  ")
//                String.format("Spawn Armed [%s]", cVars.isOne("gamespawnarmed") ? "X" : "  "),
//                String.format("Reloading [%s]", cVars.isOne("allowweaponreload") ? "X" : "  ")
        });
    }
    public uiMenusNewGame() {
        super("New Game",
            new uiMenuItem[]{
                new uiMenuItem("-Start-"){
                    public void doItem() {
                        xCon.ex("newgame");
                        xCon.ex("pause");
                    }
                },
                new uiMenuItem("MAP [<random map>]"){
                    public void doItem() {
                        uiMenus.selectedMenu = uiMenus.MENU_MAP;
                    }
                },
                new uiMenuItem(String.format("Score Limit [%s]", sVars.get("scorelimit"))) {
                    public void doItem() {
                        gMessages.enteringMessage = true;
                        gMessages.enteringOptionText = "New Score Limit";
                    }
                },
                new uiMenuItem(String.format("Time Limit [%s]",
                        Double.toString((double)sVars.getInt("timelimit")/60000.0))){
                    public void doItem() {
                        gMessages.enteringMessage = true;
                        gMessages.enteringOptionText = "New Time Limit";
                    }
                },
                new uiMenuItem(String.format("Number of Bots [%s]", sVars.get("botcount"))){
                    public void doItem() {
                        int cc = sVars.getInt("botcount");
                        sVars.putInt("botcount", cc + 1 > sVars.getInt("botcountmax") ? 0 : cc + 1);
                        text = String.format("Number of Bots [%s]", sVars.get("botcount"));
                    }
                },
                new uiMenuItem(String.format("Powerups on Map [%s]", cVars.get("powerupson"))){
                    public void doItem() {
                        int cc = cVars.getInt("powerupson");
                        cVars.putInt("powerupson", cc + 1 > sVars.getInt("powerupsmaxon") ? 0 : cc + 1);
                        text = String.format("Powerups on Map [%s]", cVars.get("powerupson"));
                    }
                },
                new uiMenuItem(String.format("Team Game [%s]", cVars.isOne("gameteam") ? "X" : "  ")){
                    public void doItem() {
                        cVars.put("gameteam", cVars.isOne("gameteam") ? "0" : "1");
                        text = String.format("Team Game [%s]", cVars.isOne("gameteam") ? "X" : "  ");
                    }
                }
//                new uiMenuItem(String.format("Spawn Armed [%s]", cVars.isOne("gamespawnarmed") ? "X" : "  ")){
//                    public void doItem() {
//                        cVars.put("gamespawnarmed", cVars.isOne("gamespawnarmed") ? "0" : "1");
//                        text = String.format("Spawn Armed [%s]", cVars.isOne("gamespawnarmed") ? "X" : "  ");
//                    }
//                },
//                new uiMenuItem(String.format("Reloading [%s]", cVars.isOne("allowweaponreload") ? "X" : "  ")){
//                    public void doItem() {
//                        cVars.put("allowweaponreload", cVars.isOne("allowweaponreload") ? "0" : "1");
//                        text = String.format("Reloading [%s]", cVars.isOne("allowweaponreload") ? "X" : "  ");
//                    }
//                }
            },
            uiMenus.MENU_MAIN);
    }
}
