import java.util.TreeMap;

public class gWeapons {
	static final int none = 0;
	static final int pistol = 1;
	static final int shotgun = 2;
	static final int autorifle = 3;
	static final int launcher = 4;
	static final int gloves = 5;

	private static TreeMap<Integer, gWeapon> selection;

	private static void init() {
		selection = new TreeMap<>();
		selection.put(none, new gWeaponsNone());
		selection.put(pistol, new gWeaponsPistol());
		selection.put(shotgun, new gWeaponsShotgun());
		selection.put(autorifle, new gWeaponsAutorifle());
		selection.put(launcher, new gWeaponsLauncher());
		selection.put(gloves, new gWeaponsGloves());
	}

	public static gWeapon fromCode(int code) {
		if(selection == null)
			init();
		return selection.get(code);
	}
}
