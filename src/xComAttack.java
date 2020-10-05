public class xComAttack extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("cv_firing 1");
        gPlayer br = eManager.currentMap.scene.players().get(0);
        if(br.getLong("cooldown") < System.currentTimeMillis()) {
            br.fireWeapon();
            br.putLong("cooldown",
                    (System.currentTimeMillis()
                            + (long)(gWeapons.weapons_selection[br.getInt("weapon")].firerate*(
                                    cVars.isOne("sicknessfast") ? 0.5 : cVars.isOne("sicknessslow") ? 2 : 1))));
        }
        return "attack";
    }

    public String undoCommand(String fullCommand) {
        xCon.ex("cv_firing 0");
        return "-attack";
    }
}
