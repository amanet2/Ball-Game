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
						bullet.coords = new int[]{p.coords[0], p.coords[1]};
						bullet.dims = new int[]{bulletDims[0], bulletDims[1]};
						bullet.sprite = gTextures.getGScaledImage(bulletSpritePath, bullet.dims[0], bullet.dims[1]);
						bullet.dmg = damage;
						bullet.src = gWeapons.none;
						bullet.anim = -1;
//						bullet.ttl = bulletTtl;
//						bullet.timestamp = sSettings.gameTime;
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
//						if(p == null)
//							return;
//						gBullet b = new gBullet(p.coords[0]+p.dims[0]/2-bulletDims[0]/2,
//								p.coords[1]+p.dims[1]/2-bulletDims[1]/2, bulletDims[0],
//								bulletDims[1],
//								eManager.getPath(String.format("objects/misc/fire%s.png", p.get("color"))),
//								p.getDouble("fv"), damage);
//						b.put("srcid", p.get("id"));
//						b.putInt("ttl",bulletTtl);
//						b.putInt("src", pistol);
//						double randomOffset = (Math.random() * ((Math.PI/10))) - Math.PI/20;
//						b.putDouble("fv", b.getDouble("fv") + randomOffset);
//						b.putInt("anim", gAnimations.ANIM_SPLASH_GREEN);
//						scene.getThingMap("THING_BULLET").put(b.get("id"), b);
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
//						if(p == null)
//							return;
//						int numpellets = 7;
//						for (int i = 0; i < numpellets; i++) {
//							gBullet b = new gBullet(
//									p.coords[0] + p.dims[0] / 2 - bulletDims[0] / 2,
//									p.coords[1] + p.dims[1] / 2 - bulletDims[1] / 2,
//									bulletDims[0], bulletDims[1],
//									eManager.getPath(String.format("objects/misc/fire%s.png", p.get("color"))),
//									p.getDouble("fv"), damage/numpellets);
//							b.putInt("ttl",bulletTtl);
//							b.put("srcid", p.get("id"));
//							b.putInt("src", shotgun);
//							double randomOffset = (Math.random() * ((Math.PI / 16)))-Math.PI/32;
//							b.putDouble("fv", b.getDouble("fv") + (i*Math.PI/32-(numpellets/2)*Math.PI/32+randomOffset));
//							b.putInt("anim", gAnimations.ANIM_SPLASH_BLUE);
//							scene.getThingMap("THING_BULLET").put(b.get("id"), b);
//						}
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
//						if(p == null)
//							return;
//						gBullet b = new gBullet(p.coords[0]+p.dims[0]/2-bulletDims[0]/2,
//								p.coords[1]+p.dims[1]/2-bulletDims[1]/2, bulletDims[0],
//								bulletDims[1],
//								eManager.getPath(String.format("objects/misc/fire%s.png", p.get("color"))),
//								p.getDouble("fv"), damage);
//						b.putInt("ttl",bulletTtl);
//						b.putInt("src", autorifle);
//						b.put("srcid", p.get("id"));
//						double randomOffset = (Math.random() * Math.PI/8) - Math.PI/16;
//						b.putDouble("fv", b.getDouble("fv") + randomOffset);
//						b.putInt("anim", gAnimations.ANIM_SPLASH_ORANGE);
//						scene.getThingMap("THING_BULLET").put(b.get("id"), b);
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
//						if(p == null)
//							return;
//						gBullet b = new gBullet(p.coords[0]+p.dims[0]/2-bulletDims[0]/2,
//								p.coords[1]+p.dims[1]/2-bulletDims[1]/2, bulletDims[0], bulletDims[1],
//								eManager.getPath(String.format("objects/misc/fire%s.png", p.get("color"))), p.getDouble("fv"), damage);
//						b.put("srcid", p.get("id"));
//						b.putInt("ttl",bulletTtl);
//						b.putInt("src", launcher);
//						b.putInt("anim", gAnimations.ANIM_SPLASH_GREEN);
//						scene.getThingMap("THING_BULLET").put(b.get("id"), b);
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
//						if(p == null)
//							return;
//						gThing b = new gThing();
//						b.coords = new int[]{
//								p.coords[0]+p.dims[0]/2-bulletDims[0]/2,
//								p.coords[1]+p.dims[1]/2-bulletDims[1]/2
//						};
//						b.dims = new int[]{bulletDims[0], bulletDims[1]};
//						b.spritePath = bulletSpritePath;
//						,
//								, , p.getDouble("fv"), damage
//						b.put("srcid", p.get("id"));
//						b.putInt("ttl",bulletTtl);
//						b.putInt("src", gWeapons.gloves);
//						scene.getThingMap("THING_BULLET").put(b.get("id"), b);
					}
				}
		);
	}

	public static gWeapon fromCode(int code) {
		return selection.get(code);
	}

	public static void createGrenadeExplosion(gThing seed) {
//		//launcher explosion
//		for (int i = 0; i < 8; i++) {
//			gBullet g = new gBullet(seed.coords[0],seed.coords[1], 300, 300,
//					seed.get("sprite"), 0,
//					fromCode(launcher).damage);
//			double randomOffset = (Math.random() * ((Math.PI / 8))) - Math.PI / 16;
//			g.putDouble("fv", g.getDouble("fv")+(i * (2.0*Math.PI/8.0) - Math.PI / 16 + randomOffset));
//			g.putInt("ttl",75);
//			g.put("srcid", seed.get("srcid"));
//			g.putInt("anim", gAnimations.ANIM_SPLASH_ORANGE);
//			if(sSettings.IS_SERVER && sSettings.IS_CLIENT) {
//				xMain.shellLogic.serverScene.getThingMap("THING_BULLET").put(g.get("id"), g);
//				xMain.shellLogic.clientScene.getThingMap("THING_BULLET").put(g.get("id"), g);
//			}
//			else if(sSettings.IS_SERVER)
//				xMain.shellLogic.serverScene.getThingMap("THING_BULLET").put(g.get("id"), g);
//			else if(sSettings.IS_CLIENT)
//				xMain.shellLogic.clientScene.getThingMap("THING_BULLET").put(g.get("id"), g);
//		}
	}
}
