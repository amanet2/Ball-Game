public class gWeapons {
	static final int weapon_none = 0;
	static final int weapon_pistol = 1;
	static final int weapon_shotgun = 2;
	static final int weapon_autorifle = 3;
	static final int weapon_launcher = 4;
	static final int weapon_boxingglove = 5;

	public static int getWeaponFromString(String s) {
		for(int j = 0; j < weapons_selection.length; j++) {
			if(weapons_selection[j].name.equalsIgnoreCase(s))
				return j;
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
