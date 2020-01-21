package spacefiles;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Space {
	final static int numDebris = 15;
	final static int xrange =1000;
	final static int yrange =xrange;
	static final int spawndistfactor = 5;


	ArrayList<SpaceObject> 	objectArray;
	Point shuttlestart;
	public Space() {
		objectArray = new ArrayList<SpaceObject>();
		this.generateSpace(numDebris);
		shuttlestart = this.getShuttle().center.getLocation();
	}
	private void generateSpace(int numDebris) {
		Random r = new Random();
		Point c;

		c = new Point (r.nextInt(xrange-SpaceShuttle.shuttleradius), r.nextInt(yrange-SpaceShuttle.shuttleradius));
		SpaceShuttle shuttle = new SpaceShuttle(c);
		this.objectArray.add(shuttle);
		SpaceFinish finish = null;
		boolean finbool = false;
		while (!finbool) {
			c = new Point (r.nextInt(xrange-SpaceFinish.finishwidth), r.nextInt(yrange-SpaceFinish.finishwidth));
			finish = new SpaceFinish(c);
			//temp buffer object for finish square
			SpaceDebris tempbuffer = new SpaceDebris(c, (int) Math.ceil(1.25*(SpaceFinish.displaywidth*Math.sqrt(2.0))));
			finbool = tempbuffer.center.distance(shuttle.center) > spawndistfactor*shuttle.radius && !(tempbuffer.center.x+tempbuffer.radius > xrange || tempbuffer.center.x-tempbuffer.radius < 0 ||tempbuffer.center.y+tempbuffer.radius > yrange || tempbuffer.center.y-tempbuffer.radius < 0);
			
		} 
		objectArray.add(finish);
		SpaceFinishMarker fm;
		Point csave = (Point) c.clone();
		//bottom right corner
		c = new Point((int)(csave.x + SpaceFinish.displaywidth/2), (int)(csave.y + SpaceFinish.displaywidth/2));
		fm = new SpaceFinishMarker(c);
		objectArray.add(fm);
		//top right corner
		c = new Point((int)(csave.x + SpaceFinish.displaywidth/2), (int)(csave.y - SpaceFinish.displaywidth/2));
		fm = new SpaceFinishMarker(c);
		objectArray.add(fm);
		//bottom left corner
		c = new Point((int)(csave.x-SpaceFinish.displaywidth/2), (int)(csave.y + SpaceFinish.displaywidth/2));
		fm = new SpaceFinishMarker(c);
		objectArray.add(fm);
		//top left corner
		c = new Point((int)(csave.x-SpaceFinish.displaywidth/2), (int)(csave.y-SpaceFinish.displaywidth/2));
		fm = new SpaceFinishMarker(c);
		objectArray.add(fm);
		
		//temp buffer object for finish square
		SpaceDebris tempbuffer = new SpaceDebris(csave, (int) Math.ceil(1.25*(SpaceFinish.displaywidth*Math.sqrt(2.0))));
		objectArray.add(tempbuffer);
		
		boolean debbool;
		for (int i =0; i < numDebris; i++) {
			debbool = false;
			while (!debbool) {
				c = new Point (r.nextInt(xrange-SpaceDebris.maxRadius), r.nextInt(yrange-SpaceDebris.maxRadius));
				SpaceDebris d = new SpaceDebris(c);
				debbool = this.objectAdder(d);
			}
		}
		
		//remove temp buffer
		objectArray.remove(tempbuffer);

	}

	private boolean objectAdder(SpaceObject so) {
		for (SpaceObject in : objectArray) {
			if (so.collidedWith(in)) return false;
		}
		this.objectArray.add(so);
		return true;
	}
	public List<SpaceObject> getObjects(){
		return objectArray;
	}
	public SpaceShuttle getShuttle() {
		return (SpaceShuttle) objectArray.get(0);
	}
	public SpaceFinish getFinish() {
		return (SpaceFinish) objectArray.get(1);
	}

	public boolean isRunning() {
		return !(this.getFinish().reached || this.getFinish().destroyed);
	}
	public int[] getDimensions() {
		return new int[] {xrange,yrange};
	}

	public void handleCollisions() {
		boolean outofbounds = false;
		ArrayList<SpaceObject> collided = new ArrayList<SpaceObject>();
		SpaceObject so1 = this.getShuttle();
		if (so1.center.x+so1.radius > xrange || so1.center.x-so1.radius < 0 ||so1.center.y+so1.radius > yrange || so1.center.y-so1.radius < 0){
			outofbounds = true;
			collided.add(so1);
			//System.out.println("out of bounds!");
		}
		for (SpaceObject so2 : objectArray) {
			if (!so1.equals(so2) && so1.collidedWith(so2)) {
				collided.add(so1);
				collided.add(so2);
			}
		}

		for (SpaceObject c : collided) {
			if (c.getClass().equals(SpaceFinish.class)){
				((SpaceFinish)c).reached = true;
				//System.out.println("Reached END!");
			}
			if (c.getClass().equals(SpaceShuttle.class)){
				((SpaceShuttle)c).health -=10;
				//System.out.println("Shuttle damage!");
				if(((SpaceShuttle)c).health <= 0 || outofbounds) {
					((SpaceFinish)objectArray.get(1)).destroyed = true;
					//((SpaceFinish)objectArray.get(1)).reached = true;
				}
			}
			if (c.getClass().equals(SpaceDebris.class)){
				((SpaceDebris)c).damage += 1;
				//System.out.println("Debris damage!");
				if(((SpaceDebris)c).damage >= SpaceDebris.maxDamage) objectArray.remove(c);
			}
		}

	}
	public SpaceObject getNearestDebris() {
		double min = -1;
		double tempdist = 0;
		SpaceObject tempobj = null;
		for (SpaceObject so : objectArray) {
			if (so.getClass().equals(SpaceDebris.class)) {
				tempdist = this.getShuttle().center.distance(so.center);
				if (min == -1 || min >= tempdist) {
					min = tempdist;
					tempobj = so;
				}
			}
		}
		return tempobj;
		//if there are no debris there will be an error!
	}
	public double getStartDist() {
		return this.shuttlestart.distance(this.getFinish().center);
	}

	public double getEndDist() {
		if (!this.isRunning())return this.getFinish().reached ? 0.0 : this.getShuttle().center.distance(this.getFinish().center);
		return getStartDist();
	}
}
