public class gCamera {
	static gArgSet argSet = null;
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
		for(int i = 0; i < 4; i++) {
			int t = i;
			argSet.putArg(new gArg("mov" + t, "0") {
				public void onChange() {
					move[t] = Integer.parseInt(value);
				}
			});
		}
	}

	public static void put(String k, String v) {
		argSet.put(k, v);
	}

	public static int getX() {
		return coords[0];
	}

	public static int getY() {
		return coords[1];
	}

	public static void updatePosition() {
		coords[0] += (velocity * move[3] - velocity * move[2]);
		coords[1] += (velocity * move[1] - velocity * move[0]);
	}
}
