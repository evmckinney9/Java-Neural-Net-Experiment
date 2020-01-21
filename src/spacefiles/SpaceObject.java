package spacefiles;
import java.awt.Point;
import java.util.Random;

public abstract class SpaceObject {
	int radius;
	Point center;
	public SpaceObject(int r, Point c) {
		radius = r;
		center = c;
	}
	public SpaceObject(Point c) {
		center =c;
	}
	public Point getPoint() {
		return center;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int r) {
		radius = r;
	}
	
	public boolean collidedWith(SpaceObject obj2) {
		double distance = this.center.distance(obj2.center);
		if (distance < this.radius+obj2.radius) {
			//System.out.println("COLLISION!");
			return true;
		}
		return false;
	}
}
