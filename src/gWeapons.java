import java.util.HashMap;

public class gWeapons {
	public enum type {
		NONE (0),
		PISTOL (1),
		SHOTGUN (2),
		AUTORIFLE (3),
		LAUNCHER (4),
		GLOVES (5)
		;
		private final int code;
		static final int pistol = 1;
		static final int shotgun = 2;
		static final int autorifle = 3;
		static final int launcher = 4;
		static final int gloves = 5;

		type(int code) {
			this.code = code;
		}
		public int code() {
			return code;
		}
	}
	private static HashMap<type, gWeapon> types = null;
	private static HashMap<Integer, gWeapon> codes = null;
	private static void init() {
		types = new HashMap<>();
		types.put(type.NONE, new gWeaponsNone());
		types.put(type.PISTOL, new gWeaponsNone());
		types.put(type.SHOTGUN, new gWeaponsNone());
		types.put(type.AUTORIFLE, new gWeaponsNone());
		types.put(type.LAUNCHER, new gWeaponsNone());
		types.put(type.GLOVES, new gWeaponsNone());
		codes = new HashMap<>();
		codes.put(0, types.get(type.NONE));
		codes.put(1, types.get(type.PISTOL));
		codes.put(2, types.get(type.SHOTGUN));
		codes.put(3, types.get(type.AUTORIFLE));
		codes.put(4, types.get(type.LAUNCHER));
		codes.put(5, types.get(type.GLOVES));
	}
	public static HashMap<type, gWeapon> weaponSelection() {
		if(types == null)
			init();
		return types;
	}

	public static gWeapon fromCode(int code) {
		return codes.get(code);
	}

	static gWeapon[] weapons_selection = new gWeapon[]{
		new gWeaponsNone(),
		new gWeaponsPistol(),
		new gWeaponsShotgun(),
		new gWeaponsAutorifle(),
		new gWeaponsLauncher(),
		new gWeaponsGloves()
	};
}
