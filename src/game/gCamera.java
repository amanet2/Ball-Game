package game;

public class gCamera {
	static int[] move = {0, 0, 0, 0};
	static int[] coords = {0, 0};
	private static final int velocity = 8;

	public static void updatePosition() {
		coords[0] += (velocity * move[3] - velocity * move[2]);
		coords[1] += (velocity * move[1] - velocity * move[0]);
	}
}
