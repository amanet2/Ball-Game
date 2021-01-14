public class xComSlot5 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.weapon_gloves);
        return fullCommand;
    }
}