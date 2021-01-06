public class gWeapons {
	static final int weapon_none = 0;
	static final int weapon_pistol = 1;
	static final int weapon_shotgun = 2;
	static final int weapon_autorifle = 3;
	static final int weapon_launcher = 4;
	static final int weapon_boxingglove = 5;

	public static int getWeaponFromString(String s) {
		String[] weap_selection = new String[]{"pistol", "shotgun", "autorifle", "launcher", "boxingglove"};
		for(int j = 0; j < weap_selection.length; j++) {
			if(weap_selection[j].equalsIgnoreCase(s))
				return j+1;
		}
		return -1;
	}

	static gWeapon[] weapons_selection = new gWeapon[]{
		new gWeaponsNone(),
		new gWeaponsPistol(),
		new gWeaponsShotgun(),
		new gWeaponsAutorifle(),
		new gWeaponsLauncher(),
		new gWeaponsBoxingGlove()
	};
}
