package spacefiles;
import java.awt.Point;
import java.util.Vector;

public class SpaceShuttle extends SpaceObject{
	int health;
	public static int shuttleradius =25;
	public SpaceShuttle(Point c) {
		super(shuttleradius, c);
		health = 10;
	}
	public void move(Point vmove) {
		this.center.setLocation(center.getX() + vmove.getX(), center.getY() + vmove.getY());
	}
	//public double distToDebris;
	//public double distToFinsh;
}
