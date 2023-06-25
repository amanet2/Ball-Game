import java.util.TreeMap;

public class gWeapons {
	static final int none = 0;
	static final int pistol = 1;
	static final int shotgun = 2;
	static final int autorifle = 3;
	static final int launcher = 4;
	static final int gloves = 5;

	private static TreeMap<Integer, gWeapon> selection;

	public static void init() {
		selection = new TreeMap<>();
		selection.put(none,
				new gWeapon(
						"NONE",
						new int[] {225, 150},
						"misc/rock.png",
						"sounds/splash.wav",
						"",
						new int[] {225, 225}
				) {
					public void fireWeapon(gPlayer p, gScene scene) {
						super.fireWeapon(p, scene);
						if (p == null)
							return;
						gThing bullet = new gThing();
						bullet.dims = new int[]{bulletDims[0], bulletDims[1]};
						bullet.coords = new int[]{p.coords[0]+p.dims[0]/2-bullet.dims[0]/2, p.coords[1]+p.dims[1]/2-bullet.dims[1]/2};
						bullet.sprite = gTextures.getGScaledImage(bulletSpritePath, bullet.dims[0], bullet.dims[1]);
						bullet.dmg = damage;
						bullet.src = gWeapons.none;
						bullet.anim = -1;
						bullet.fv = p.fv + (Math.random() * ((Math.PI/10))) - Math.PI/20;
						bullet.id = eUtils.createId();
						bullet.srcId = p.id;
						scene.getThingMap("THING_BULLET").put(bullet.id, bullet);
						xMain.shellLogic.scheduledEvents.put(Long.toString(sSettings.gameTime + bulletTtl),
								new gDoable() {
									public void doCommand() {
										scene.getThingMap("THING_BULLET").remove(bullet.id);
									}
								}
						);
					}
				}
		);
		selection.put(pistol,
				new gWeapon(
						"PISTOL",
						new int[] {200, 100},
						"objects/misc/firegreen.png",
						"sounds/laser.wav",
						"misc/bfg.png",
						new int[] {100, 100}
				) {
					public void fireWeapon(gPlayer p, gScene scene) {
						super.fireWeapon(p, scene);
						if (p == null)
							return;
						gThing bullet = new gThing();
						bullet.dims = new int[]{bulletDims[0], bulletDims[1]};
						bullet.coords = new int[]{p.coords[0]+p.dims[0]/2-bullet.dims[0]/2, p.coords[1]+p.dims[1]/2-bullet.dims[1]/2};
						bullet.sprite = gTextures.getGScaledImage(eManager.getPath(String.format("objects/misc/fire%s.png", p.color)), bullet.dims[0], bullet.dims[1]);
						bullet.dmg = damage;
						bullet.src = gWeapons.pistol;
						bullet.anim = -1;
						bullet.fv = p.fv + (Math.random() * ((Math.PI/10))) - Math.PI/20;
						bullet.id = eUtils.createId();
						bullet.srcId = p.id;
						scene.getThingMap("THING_BULLET").put(bullet.id, bullet);
						xMain.shellLogic.scheduledEvents.put(Long.toString(sSettings.gameTime + bulletTtl),
								new gDoable() {
									public void doCommand() {
										scene.getThingMap("THING_BULLET").remove(bullet.id);
									}
								}
						);
					}
				}
		);
		selection.put(shotgun,
				new gWeapon(
						"SHOTGUN",
						new int[] {200, 100},
						"objects/misc/fireblue.png",
						"sounds/shotgun.wav",
						"misc/shotgun.png",
						new int[] {100, 100}
				) {
					public void fireWeapon(gPlayer p, gScene scene) {
						super.fireWeapon(p, scene);
						if(p == null)
							return;
						int numpellets = 7;
						for(int i = 0; i < numpellets; i++) {
							gThing bullet = new gThing();
							bullet.dims = new int[]{bulletDims[0], bulletDims[1]};
							bullet.coords = new int[]{p.coords[0]+p.dims[0]/2-bullet.dims[0]/2, p.coords[1]+p.dims[1]/2-bullet.dims[1]/2};
							bullet.sprite = gTextures.getGScaledImage(eManager.getPath(String.format("objects/misc/fire%s.png", p.color)), bullet.dims[0], bullet.dims[1]);
							bullet.dmg = damage/numpellets;
							bullet.src = gWeapons.shotgun;
							bullet.anim = -1;
							double randomOffset = (Math.random() * ((Math.PI/16))) - Math.PI/32;
							bullet.fv = p.fv + (i*Math.PI/32-(numpellets/2)*Math.PI/32+randomOffset);
							bullet.id = eUtils.createId();
							bullet.srcId = p.id;
							scene.getThingMap("THING_BULLET").put(bullet.id, bullet);
							xMain.shellLogic.scheduledEvents.put(Long.toString(sSettings.gameTime + bulletTtl),
									new gDoable() {
										public void doCommand() {
											scene.getThingMap("THING_BULLET").remove(bullet.id);
										}
									}
							);
						}
					}
				}
		);
		selection.put(autorifle,
				new gWeapon(
					"AUTORIFLE",
					new int[] {200, 100},
					"objects/misc/fireorange.png",
					"sounds/30cal.wav",
						"misc/autorifle.png",
						new int[] {100, 100}
				) {
					public void fireWeapon(gPlayer p, gScene scene) {
						super.fireWeapon(p, scene);
						if (p == null)
							return;
						gThing bullet = new gThing();
						bullet.dims = new int[]{bulletDims[0], bulletDims[1]};
						bullet.coords = new int[]{p.coords[0]+p.dims[0]/2-bullet.dims[0]/2, p.coords[1]+p.dims[1]/2-bullet.dims[1]/2};
						bullet.sprite = gTextures.getGScaledImage(eManager.getPath(String.format("objects/misc/fire%s.png", p.color)), bullet.dims[0], bullet.dims[1]);
						bullet.dmg = damage;
						bullet.src = gWeapons.autorifle;
						bullet.anim = -1;
						bullet.fv = p.fv + (Math.random() * ((Math.PI/8))) - Math.PI/16;
						bullet.id = eUtils.createId();
						bullet.srcId = p.id;
						scene.getThingMap("THING_BULLET").put(bullet.id, bullet);
						xMain.shellLogic.scheduledEvents.put(Long.toString(sSettings.gameTime + bulletTtl),
								new gDoable() {
									public void doCommand() {
										scene.getThingMap("THING_BULLET").remove(bullet.id);
									}
								}
						);
					}
				}
		);
		selection.put(launcher,
				new gWeapon(
						"LAUNCHER",
						new int[] {200, 100},
						"objects/misc/firegreen.png",
						"sounds/bfg.wav",
						"misc/launcher.png",
						new int[] {100, 100}
				) {
					public void fireWeapon(gPlayer p, gScene scene) {
						super.fireWeapon(p, scene);
						if (p == null)
							return;
						gThing bullet = new gThing();
						bullet.dims = new int[]{bulletDims[0], bulletDims[1]};
						bullet.coords = new int[]{p.coords[0]+p.dims[0]/2-bullet.dims[0]/2, p.coords[1]+p.dims[1]/2-bullet.dims[1]/2};
						bullet.sprite = gTextures.getGScaledImage(eManager.getPath(String.format("objects/misc/fire%s.png", p.color)), bullet.dims[0], bullet.dims[1]);
						bullet.dmg = damage;
						bullet.src = gWeapons.launcher;
						bullet.anim = -1;
						bullet.fv = p.fv + (Math.random() * ((Math.PI/8))) - Math.PI/16;
						bullet.id = eUtils.createId();
						bullet.srcId = p.id;
						bullet.spritePath = eManager.getPath(String.format("objects/misc/fire%s.png", p.color));
						scene.getThingMap("THING_BULLET").put(bullet.id, bullet);
						xMain.shellLogic.scheduledEvents.put(Long.toString(sSettings.gameTime + bulletTtl),
								new gDoable() {
									public void doCommand() {
										createGrenadeExplosion(bullet, scene);
										scene.getThingMap("THING_BULLET").remove(bullet.id);
									}
								}
						);
					}
				}
		);
		selection.put(gloves,
				new gWeapon(
						"GLOVES",
						new int[] {225, 150},
						"misc/glove.png",
						"sounds/splash.wav",
						"misc/glove.png",
						new int[] {225, 225}
				) {
					public void fireWeapon(gPlayer p, gScene scene) {
						super.fireWeapon(p, scene);
						if (p == null)
							return;
						gThing bullet = new gThing();
						bullet.dims = new int[]{bulletDims[0], bulletDims[1]};
						bullet.coords = new int[]{p.coords[0]+p.dims[0]/2-bullet.dims[0]/2, p.coords[1]+p.dims[1]/2-bullet.dims[1]/2};
						bullet.sprite = gTextures.getGScaledImage(bulletSpritePath, bullet.dims[0], bullet.dims[1]);
						bullet.dmg = damage;
						bullet.src = gWeapons.gloves;
						bullet.anim = -1;
						bullet.fv = p.fv + (Math.random() * ((Math.PI/10))) - Math.PI/20;
						bullet.id = eUtils.createId();
						bullet.srcId = p.id;
						scene.getThingMap("THING_BULLET").put(bullet.id, bullet);
						xMain.shellLogic.scheduledEvents.put(Long.toString(sSettings.gameTime + bulletTtl),
								new gDoable() {
									public void doCommand() {
										scene.getThingMap("THING_BULLET").remove(bullet.id);
									}
								}
						);
					}
				}
		);
	}

	public static gWeapon fromCode(int code) {
		return selection.get(code);
	}

	public static void createGrenadeExplosion(gThing seed, gScene scene) {
//		//launcher explosion
		for (int i = 0; i < 8; i++) {
			gThing bullet = new gThing();
			bullet.dims = new int[]{300, 300};
			bullet.coords = new int[]{seed.coords[0], seed.coords[1]};
			bullet.sprite = gTextures.getGScaledImage(seed.spritePath, bullet.dims[0], bullet.dims[1]);
			bullet.dmg = seed.dmg;
			bullet.anim = -1;
			bullet.fv = ((i * (2.0*Math.PI/8.0) - Math.PI / 16 + ((Math.random() * ((Math.PI/8))) - Math.PI/16)));
			bullet.id = eUtils.createId();
			bullet.srcId = seed.srcId;
			scene.getThingMap("THING_BULLET").put(bullet.id, bullet);
			xMain.shellLogic.scheduledEvents.put(Long.toString(sSettings.gameTime + 75),
					new gDoable() {
						public void doCommand() {
							scene.getThingMap("THING_BULLET").remove(bullet.id);
						}
					}
			);
		}
	}
}
