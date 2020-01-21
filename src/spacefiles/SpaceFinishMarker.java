package spacefiles;

import java.awt.Point;

public class SpaceFinishMarker extends SpaceDebris {

	static int radius = 10;
	public SpaceFinishMarker(Point c) {
		super(c);
		this.setRadius(radius);
	}

}
