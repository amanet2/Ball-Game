public class gWeapons {
	static final int weapon_none = 0;
	static final int weapon_pistol = 1;
	static final int weapon_shotgun = 2;
	static final int weapon_autorifle = 3;
	static final int weapon_launcher = 4;

	static gWeapon[] weapons_selection = new gWeapon[]{
		new gWeaponsNone(),
		new gWeaponsPistol(),
		new gWeaponsShotgun(),
		new gWeaponsAutorifle(),
		new gWeaponsLauncher()
	};
}
