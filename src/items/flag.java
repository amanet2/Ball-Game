package items;
import game.gThing;
import game.nState;
import game.nStateMap;
import game.xMain;

public class flag extends itemTemplate {
    public flag() {
        index = 1;
        title = "ITEM_FLAG";
    }

    public void activateItem() {
        super.activateItem();
    }

    public void activateItem(gThing p) {
        super.activateItem();
        /*
        *   getres hf setnstate $2 flag
            setnstate $2 flag 1
            testresn $hf 1 getres pname setnstate $2 name
            testresn $hf 1 getres pcol setnstate $2 color
            testresn $hf 1 getres pfs constr $pname # $pcol
            testresn $hf 1 testresn $pname null testresn $gamemode $mode_bringmaster echo $pfs has the flag
            testresn $hf 1 testresn $pname null deleteitem $1
            testresn $hf 1 testresn $pname null setnplayer $2 decorationsprite misc/flag.png
            testresn $hf 1 testresn $pname null testresn $gamemode $mode_bringmaster setnplayer $2 waypoint FLAG
        * */
        nStateMap serverStateSnapshot = new nStateMap(xMain.shellLogic.serverNetThread.masterStateSnapshot);
        String stateId = p.id;
        if(serverStateSnapshot.contains(stateId)) {
            nState serverState = serverStateSnapshot.get(stateId);
            if(!serverState.contains("effects"))
                serverState.put("effects", "");
            String effects = serverState.get("effects");
            serverState.put("effects", effects.replaceAll(":flag", "") + ":flag");
            String pc = serverState.get("color");
            String pn = serverState.get("name");
            String colorString = pc+"#"+pn;

        }
    }
}
