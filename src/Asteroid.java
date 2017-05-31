/*
 *  This is the class for creating asteroids
 */

import processing.core.PApplet;

public class Asteroid {
	
	// Width (and height) of the asteroid
	private static int WIDTH = 50;
	
	// X and Y coordinates from which the asteroid is drawn
	private float astX, astY;
	
	// Scaling factor to change the size of the asteroid
	private float scale;

	// RGB values of the asteroid
	private int astR, astG, astB;
	
	// The vector of the asteroid's movement; vX for horizontal, vY for vertical
	// These components will be randomly generated so that the asteroid appears to float freely through space
	private float vX, vY;
	
	// The angle (in radians) at which the asteroid spins
	private float theta;
	
	// The angular velocity of the asteroid's spin
	private float omega;
	
	// The parent PApplet
	private PApplet app;
	
	
	// Constructor
	public Asteroid(PApplet pen) {
		app = pen;
		
		scale = app.random(0.5f, 2);
		astY = app.random(0 - (WIDTH * scale), app.height + (WIDTH * scale));
		
		// Randomly generated number to decide which side of the screen the asteroid will appear from and what it's velocity will be
		int leftOrRight = (int)app.random(0, 2);
		
		if (leftOrRight == 0 && astY < (app.height/2)) {
			// If leftOrRight equals 0 and astY is less than half the height of the window,
			// Then the asteroid will appear somewhere from the left in a downward direction
			astX = 0 - (WIDTH * scale);
			vX = app.random(0.25f, 2);
			vY = app.random(0.25f, 1);
		} else if (leftOrRight == 0 && astY >= (app.height/2)) {
			// If leftOrRight equals 0 and astY is greater than half the height of the window,
			// Then the asteroid will appear somewhere from the left in an upward direction
			astX = 0 - (WIDTH * scale);
			vX = app.random(0.25f, 2);
			vY = app.random(-1, -0.25f);
		} else if (leftOrRight == 1 && astY < (app.height/2)) {
			// If leftOrRight equals 1 and astY is less than half the height of the window,
			// Then the asteroid will appear somewhere from the right in a downward direction
			astX = app.width + (WIDTH * scale);
			vX = app.random(-2, -0.25f);
			vY = app.random(0.25f, 1);
		} else if (leftOrRight == 1 && astY >= (app.height/2)) {
			// If leftOrRight equals 1 and astY is greater than half the height of the window,
			// Then the asteroid will appear somewhere from the right in an upward direction
			astX = app.width + (WIDTH * scale);
			vX = app.random(-2, -0.25f);
			vY = app.random(-1, -0.25f);
		}
		
		astR = 100;
		astG = 50;
		astB = 0;
		
		// The initial angle of the asteroid's rotation
		theta = 0;
		
		// The asteroid's angular velocity will be random
		omega = app.random(-0.03f, 0.03f);
		
	}
	
	
	// Accessor for X coordinate of asteroid
	public float getX(){
		return astX;
	}
	
	// Accessor for Y coordinate of asteroid
	public float getY(){
		return astY;
	}
	
	// Accessor for width of asteroid
	public float getWidth(){
		return WIDTH * scale;
	}
	
	/*
	 * Drawing the asteroid, which is a simple circle with rectangular pock marks
	 */
	public void draw(Mode mode) {
		
		// Applying rotation to the asteroid by translating the window's origin to the asteroid's coordinates, that way the asteroid will rotate
		// about itself rather than rotate about the top left corner of the window
		app.pushMatrix();
		// One translation to allow the asteroid to rotate around its coordinates....
		app.translate(astX, astY); 
		app.rotate(theta);
		// ....and another translation to KEEP the asteroid at its coordinates, otherwise it will orbit in a circle
		app.translate(-astX, -astY); 
		
		app.stroke(0);
		app.strokeWeight(1);
		
		// Asteroid
		app.fill(astR,astG,astB);
		app.ellipse(astX, astY, WIDTH * scale, WIDTH * scale);
		app.fill(0);
		app.rectMode(app.CENTER);
		app.rect(astX + (WIDTH * scale)/5, astY, 9 * scale, 3 * scale);
		app.rect(astX, astY - (WIDTH * scale)/5, 9 * scale, 3 * scale);
		app.rect(astX, astY + (WIDTH * scale)/5, 9 * scale, 3 * scale);
		app.rect(astX - (WIDTH * scale)/5, astY, 9 * scale, 3 * scale);
		
		// Draws relative bounding boxes
		if (mode == mode.RELATIVE_BOX) {
			app.stroke(0);
			app.strokeWeight(1);
			app.noFill();
			app.rect(astX, astY, WIDTH * scale, WIDTH * scale);
		}
		
		// Operation that creates rotation of the asteroid
		theta = theta + omega;
		app.popMatrix();
		
		// Draws absolute bounding boxes
		if (mode == mode.ABSOLUTE_BOX) {
			app.stroke(0);
			app.strokeWeight(1);
			app.noFill();
			app.rect(astX, astY, WIDTH * scale, WIDTH * scale);
		}
		
		app.rectMode(app.CORNER);
		
	}
	
	/* 
	 * The function that allows the asteroid to move
	 */
	public void animate() {
		// Actual operations that cause movement of the asteroid
		astX = astX + vX;
		astY = astY + vY;
		
	}
}