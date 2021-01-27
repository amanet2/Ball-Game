import java.util.HashMap;
import java.util.TreeMap;

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
		static final int none = 0;
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
	private static TreeMap<Integer, gWeapon> codes = null;
	private static void init() {
		types = new HashMap<>();
		types.put(type.NONE, new gWeaponsNone());
		types.put(type.PISTOL, new gWeaponsPistol());
		types.put(type.SHOTGUN, new gWeaponsShotgun());
		types.put(type.AUTORIFLE, new gWeaponsAutorifle());
		types.put(type.LAUNCHER, new gWeaponsLauncher());
		types.put(type.GLOVES, new gWeaponsGloves());
		codes = new TreeMap<>();
		codes.put(type.none, types.get(type.NONE));
		codes.put(type.pistol, types.get(type.PISTOL));
		codes.put(type.shotgun, types.get(type.SHOTGUN));
		codes.put(type.autorifle, types.get(type.AUTORIFLE));
		codes.put(type.launcher, types.get(type.LAUNCHER));
		codes.put(type.gloves, types.get(type.GLOVES));
	}
	public static HashMap<type, gWeapon> weaponSelection() {
		if(types == null)
			init();
		return types;
	}

	public static TreeMap<Integer, gWeapon> weaponCodes() {
		if(codes == null)
			init();
		return codes;
	}

	public static gWeapon fromCode(Integer code) {
		return weaponCodes().get(code);
	}
	public static gWeapon get(type type) {
		return weaponSelection().get(type);
	}
}
