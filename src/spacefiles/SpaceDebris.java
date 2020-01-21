package spacefiles;

import java.awt.Point;
import java.util.Random;

public class SpaceDebris extends SpaceObject{
	int damage;
	public static int maxDamage = 1;
	public static int maxRadius = 15;	
	public static int minRadius = 5;
	public SpaceDebris(Point c) {
		this(c, Math.random() > 0.5 ? minRadius : maxRadius);
	}
	public SpaceDebris(Point c, int radius) {
		super(c);
		this.setRadius(radius);
		damage =0;
	}
}
