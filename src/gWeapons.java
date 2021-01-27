import java.util.HashMap;

public class gWeapons {
	public enum Type {
		NONE (0),
		PISTOL (1),
		SHOTGUN (2),
		AUTORIFLE (3),
		LAUNCHER (4),
		GLOVES (5)
		;
		private final int code;
		private Type(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	private static HashMap<Type, gWeapon> weaponSelection = null;
	private static void init() {
		weaponSelection = new HashMap<>();
		weaponSelection.put(Type.NONE, new gWeaponsNone());
		weaponSelection.put(Type.PISTOL, new gWeaponsNone());
		weaponSelection.put(Type.SHOTGUN, new gWeaponsNone());
		weaponSelection.put(Type.AUTORIFLE, new gWeaponsNone());
		weaponSelection.put(Type.LAUNCHER, new gWeaponsNone());
		weaponSelection.put(Type.GLOVES, new gWeaponsNone());
	}
	public static HashMap<Type, gWeapon> weaponSelection() {
		if(weaponSelection == null)
			init();
		return weaponSelection;
	}



	static final int weapon_none = 0;
	static final int weapon_pistol = 1;
	static final int weapon_shotgun = 2;
	static final int weapon_autorifle = 3;
	static final int weapon_launcher = 4;
	static final int weapon_gloves = 5;

	static gWeapon[] weapons_selection = new gWeapon[]{
		new gWeaponsNone(),
		new gWeaponsPistol(),
		new gWeaponsShotgun(),
		new gWeaponsAutorifle(),
		new gWeaponsLauncher(),
		new gWeaponsGloves()
	};
}
