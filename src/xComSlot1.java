public class xComSlot1 extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.changeWeapon(gWeapons.weapon_pistol);
        return fullCommand;
    }
}