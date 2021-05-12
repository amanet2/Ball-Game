import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class gMap {
    String mapName;
    gScene scene;

	public gMap() {
        scene = new gScene();
        mapName = "new";
		cVars.putInt("gamemode", cGameLogic.DEATHMATCH);
	}
}
