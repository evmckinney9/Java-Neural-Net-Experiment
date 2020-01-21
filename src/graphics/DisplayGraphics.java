package graphics;

import java.awt.*;  
import javax.swing.JFrame;

import spacefiles.Space;
import spacefiles.SpaceDebris;
import spacefiles.SpaceFinish;
import spacefiles.SpaceObject;
import spacefiles.SpaceShuttle;  

public class DisplayGraphics extends Canvas{  

	/**
	 * 
	 */
	Space sp;
	static final boolean debugfinish = false;

	public DisplayGraphics(Space sp1) {
		sp = sp1;
		JFrame f=new JFrame();  
		f.add(this);  
		f.setSize(sp1.getDimensions()[0],sp1.getDimensions()[1]);  
		//f.setLayout(null);  
		f.setVisible(true); 
	}
	private static final long serialVersionUID = 1L;
	public void paint(Graphics g) {  
		setBackground(Color.BLACK);
		boolean fin = false;
		for (SpaceObject so : sp.getObjects()) {
			int x = (int)so.getPoint().getX();
			int y = (int)so.getPoint().getY();
			fin = false;
			if (so.getClass().equals(SpaceFinish.class)) {
				g.setColor(Color.GREEN);
				if (debugfinish) {
					g.fillRect(x-SpaceFinish.displaywidth/2, y-SpaceFinish.displaywidth/2, SpaceFinish.displaywidth, SpaceFinish.displaywidth);
					g.setColor(Color.ORANGE);
				}
				else{
					fin = true;
				}
			}
			else if (so.getClass().equals(SpaceDebris.class)) {
				g.setColor(Color.WHITE);  

			}
			else if (so.getClass().equals(SpaceShuttle.class)){
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLUE);
			}
			if (!fin)g.fillOval(x-so.getRadius()/2, y-so.getRadius()/2, so.getRadius(), so.getRadius());
		}
	}  
	public void updateSpace(Space sp1) {
		sp = sp1;
	}

}  