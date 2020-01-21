package spacefiles;
import spacefiles.*;
import java.awt.*;
import java.util.Random;

import javax.swing.JFrame;

import graphics.DisplayGraphics;


public class SpaceRunner {
	Space sp1;
	boolean running;
	DisplayGraphics dp;
	boolean graphics;
	double numsteps;
	static int fps = 60;
	public SpaceRunner() {
		this(false);
	}
	public SpaceRunner(boolean graphics) {
		running = false;
		sp1 = new Space();
		this.graphics = graphics;
		numsteps=0;
		
	}
	
	public DisplayGraphics run(){
		running = true;
		if (graphics) {
			dp = new DisplayGraphics(sp1);
			return dp;
		}
		return null;
	}
	
	public void run(DisplayGraphics dp){
		running = true;
		if (graphics) {
			this.dp = dp;
			dp.updateSpace(sp1);
			dp.repaint();
		}
		try {
			Thread.sleep(10000/fps);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void step(float[] gameInputs) {
		if (running) {
			numsteps++;
			double movex = (gameInputs[0]*5 - gameInputs[1]*5);
			double movey = (gameInputs[2]*5 - gameInputs[3]*5);
			sp1.getShuttle().move(new Point((int)movex, (int)movey));
			//System.out.println("move " + movex + ", " + movey);
			//System.out.println(sp1.getShuttle().center);
			sp1.handleCollisions();
			if (graphics) {
				dp.updateSpace(sp1);
				dp.repaint();
				try {
					Thread.sleep(1000/fps);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!sp1.isRunning()) running = false;
		}
	}
	public float getScore() {
		double startdist = sp1.getStartDist();
		double enddist = sp1.getEndDist();
		double temp1 = 1- enddist/startdist;
		if (temp1 < 0)temp1 = 0;
		if (temp1 == 1) temp1 *= startdist/numsteps;
		return (float)temp1;
	}

	public boolean isRunning() {
		return this.running;
	}

	public float[] getInputsfromGame() {
		//{shuttle x, shuttle y, shuttle radius, finish x, finish y, finish radius, 
		//nearest debris x, nearest debris y, nearest debris radius}
		return new float[] {sp1.getShuttle().center.x, sp1.getShuttle().center.y,
				sp1.getFinish().center.x, sp1.getFinish().center.y,
				sp1.getNearestDebris().center.x, sp1.getNearestDebris().center.y, sp1.getNearestDebris().radius};
	}

	public void handleOutputfromNet(float[] gameInputs) {
		this.step(gameInputs);
	}

} 