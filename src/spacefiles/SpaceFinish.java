package spacefiles;

import java.awt.Point;

public class SpaceFinish extends SpaceObject {
	boolean reached;
	boolean destroyed;
	public static int finishwidth = 15;
	public static int displaywidth = 100;
	public SpaceFinish(Point c) {
		super(finishwidth, c);
		reached = false;
		destroyed = false;
	}
}
