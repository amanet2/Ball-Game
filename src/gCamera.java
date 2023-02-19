public class gCamera {
	private static gArgSet argSet = null;
	private static final int[] move = {0, 0, 0, 0};
	private static final int[] coords = {0, 0};
	private static final int velocity = 8;

	public static void init() {
		argSet = new gArgSet();
		argSet.putArg(new gArg("coords", "0:0") {
			public void onChange() {
				String[] vcoords = value.split(":");
				coords[0] = Integer.parseInt(vcoords[0]) - eUtils.unscaleInt(sSettings.width/2);
				coords[1] = Integer.parseInt(vcoords[1]) - eUtils.unscaleInt(sSettings.height/2);
			}
		});
		argSet.putArg(new gArg("mov0", "0") {
			public void onChange() {
				move[0] = Integer.parseInt(value);
			}
		});
		argSet.putArg(new gArg("mov1", "0") {
			public void onChange() {
				move[1] = Integer.parseInt(value);
			}
		});
		argSet.putArg(new gArg("mov2", "0") {
			public void onChange() {
				move[2] = Integer.parseInt(value);
			}
		});
		argSet.putArg(new gArg("mov3", "0") {
			public void onChange() {
				move[3] = Integer.parseInt(value);
			}
		});
	}

	public static void put(String k, String v) {
		argSet.put(k, v);
	}

	public static String get(String k) {
		return argSet.get(k);
	}

	public static boolean contains(String k) {
		return argSet.contains(k);
	}

	public static int getX() {
		return coords[0];
	}

	public static int getY() {
		return coords[1];
	}

	public static void setX(int x) {
		coords[0] = x;
	}

	public static void setY(int y) {
		coords[1] = y;
	}

	public static void updatePosition() {
		coords[0] += (velocity * move[3] - velocity * move[2]);
		coords[1] += (velocity * move[1] - velocity * move[0]);
	}
}
