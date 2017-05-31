/*
 *  This is the class for creating the spaceship
 */

import processing.core.PApplet;

public class Spaceship {
  
	// Width and height of the spaceship
	private static int WIDTH = 40;
	private static int HEIGHT = 80;
	  
	// Width and height of the exhaust
	private static int EX_WIDTH = 5;
	private static int EX_HEIGHT = 20;
	  
	// X and Y coordinates from which the spaceship is drawn
	private float shipX, shipY;
	
	// Scaling factor to change the size of the spaceship
	private float scale;

	// RGB values of the spaceship and exhaust
	private int shipR, shipG, shipB;
	private int exhaustR, exhaustG, exhaustB;
	
	// The vector of the spaceship's movement; vX for horizontal, vY for vertical
	private float vX, vY;
	
	// The angle (in radians) at which the ship rotates
	private float theta;
	
	// The spaceship's acceleration; aX for horizontal, aY for vertical
	// Acceleration is the rate of change of velocity, therefore these variables will be what the user directly controls
	// while the velocity is indirectly controlled
	private float aX, aY;
	
	// The angular velocity of the ship's rotation, which will remain at a constant magnitude
	private float omega;
	
	// How much damage the ship can take before it is destroyed
	private float health;
	
	// The parent PApplet
	private PApplet app;
	
	
	// Constructor
	public Spaceship(PApplet pen, float theShipX, float theShipY, float theScale, int theHealth, 
	int theShipR, int theShipG, int theShipB,
	int theExhaustR, int theExhaustG, int theExhaustB) {
		app = pen;
		health = theHealth;
		
		scale = theScale;
		
		shipX = theShipX;
		shipY = theShipY;
		
		shipR = theShipR;
		shipG = theShipG;
		shipB = theShipB;
		exhaustR = theExhaustR;
		exhaustG = theExhaustG;
		exhaustB = theExhaustB;
		
		// Initially the ship is not moving
		vX = 0;
		vY = 0;
		
		// The initial angle of the ship's rotation should be zero
		theta = 0;
		
	}
	
	
	// Accessor for X coordinate of spaceship
	public float getX(){
		return shipX;
	}
	
	// Accessor for Y coordinate of spaceship
	public float getY(){
		return shipY;
	}
	
	// Accessor for the health of spaceship
	public float getHealth(){
		return health;
	}

	/*
	 *  Checks for a collision between the ship and an asteroid
	 */
	public boolean collision(Asteroid asteroid){
		// Is there a collision or no?
		// Initially should be false
		boolean collide = false;
		
		// If the distance between a particular point inside of the ship (near the base) and the edge of an asteroid is less than 
		// the half the width of the ship, then we have a collision
		if (app.dist(shipX, shipY + ((HEIGHT * scale) / 12), 
			asteroid.getX(), asteroid.getY()) 
			<= ((WIDTH * scale) / 2) + (asteroid.getWidth() / 2)) {
			collide = true;
			
		}
		// If the distance between another particular point inside of the ship (this time right in the middle) and the edge of an asteroid 
		// is less than the half the width of the ship, then we have a collision
		if (app.dist(shipX, shipY - ((HEIGHT * scale) / 4), 
			asteroid.getX(), asteroid.getY()) 
			<= ((WIDTH * scale) / 3.5f) + (asteroid.getWidth() / 2)) {
			collide = true;
			
		}
		
		return collide;
		
	}
	
	
	/*
	 * Collision!
	 * Makes the ship turn random colors to signify that damage was taken, brings it to a stop by setting the velocity to zero,
	 * and reduces the health
	 */
	public void crash(){
		shipR = (int)app.random(255);
		shipG = (int)app.random(255);
		shipB = (int)app.random(255);
		exhaustR = (int)app.random(255);
		exhaustG = (int)app.random(255);
		exhaustB = (int)app.random(255);
		vX = 0;
		vY = 0;
		health--;
	}
	
	/*
	 * The drawing of the spaceship, which is a chevron (a quadrilateral) on top of two rectangles
	 */
	public void draw(Mode mode) {
		
		// Applying rotation to the ship by translating the window's origin to the ship's coordinates, that way the ship will rotate
		// about itself rather than rotate about the top left corner of the window
		app.pushMatrix();
		// One translation to allow the spaceship to rotate around its coordinates....
		app.translate(shipX, shipY); 
		app.rotate(theta);
		// ....and another translation to KEEP the spaceship at its coordinates, otherwise it will orbit in a circle
		app.translate(-(shipX), -(shipY)); 
		
		app.noStroke();
		
		// Exhaust
		app.fill(exhaustR,exhaustG,exhaustB);
		app.rect(shipX - ((EX_WIDTH * 2) * scale), shipY, EX_WIDTH * scale, EX_HEIGHT * scale);
		app.pushMatrix();
		app.translate((EX_WIDTH * 3) * scale, 0);
		app.rect(shipX - ((EX_WIDTH * 2) * scale), shipY, EX_WIDTH * scale, EX_HEIGHT * scale);
		app.popMatrix();
		
		app.stroke(0);
		app.strokeWeight(1);
		
		// Body
		app.fill(shipR,shipG,shipB);
		app.quad(shipX, shipY, 
		shipX - ((WIDTH/2) * scale), shipY + ((HEIGHT/4) * scale), 
		shipX, shipY - ((3 * HEIGHT/4) * scale), 
		shipX + ((WIDTH/2) * scale), shipY + ((HEIGHT/4) * scale));
		
		app.rectMode(app.CENTER);
		// Draws relative bounding boxes
		if (mode == mode.RELATIVE_BOX) {
			app.stroke(0);
			app.strokeWeight(1);
			app.noFill();
			app.rect(shipX, shipY - ((HEIGHT * scale)/4), WIDTH * scale, HEIGHT * scale);
		}
		
		
		// Operation that creates rotation of the ship
		theta = theta + omega;
		app.popMatrix();
		
		// Draws absolute bounding boxes
		if (mode == mode.ABSOLUTE_BOX) {
			app.stroke(0);
			app.strokeWeight(1);
			app.noFill();
			// This part completely done by trial and error, as the math involved is rather insane
			app.rect(shipX + ((WIDTH/2 * scale) * app.sin(theta)), shipY - (((HEIGHT * scale)/4) * app.cos(theta)), 
					(WIDTH * scale) + app.abs((WIDTH * scale * app.sin(theta))), HEIGHT * scale);
		}
		app.rectMode(app.CORNER);
		
	}
	
	/* 
	 * The animation function that allows the spaceship to move
	 */
	public void animate() {
		// When the open bracket key '[' is pressed, set this boolean for rotating counter-clockwise to true
		boolean counterCWise = (app.keyPressed && app.key == '[');
		// When the close bracket key ']' is pressed, set this boolean for rotating clockwise to true
		boolean cWise = (app.keyPressed && app.key == ']');
		// When the spacebar is pressed, set this boolean for accelerating to true
		boolean accel = (app.keyPressed && app.key == ' ');
		
		// Constant acceleration that will be applied to the velocity vector
		aX = 0.025f;
		aY = 0.015f;
		
		// Actual operations that cause movement of the ship
		shipX = shipX + vX;
		shipY = shipY + vY;
		
		// This if block causes the rocket to warp from one end of the screen to the other
		if (shipX - HEIGHT >= app.width) {
			shipX = 0 - HEIGHT;
		} else if (shipX + HEIGHT <= 0) {
			shipX = app.width + HEIGHT;
		} else if (shipY - HEIGHT >= app.height) {
			shipY = 0 - HEIGHT;
		} else if (shipY + HEIGHT <= 0) {
			shipY = app.height + HEIGHT;
		}
		
		// This block of if statements is responsible for the rotation of the rocket
		if (counterCWise) {
			// If the open bracket key is pressed, rotate counter-clockwise
			omega = -0.025f;
		} else if (cWise) {
			// If the close bracket key is pressed, rotate clockwise
			omega = 0.025f;
		} else if (accel) {
			// If the spacebar is pressed, accelerate!
			// The cos() and sin() functions are used here to get the proper X and Y coordinates of a velocity vector that points in 
			// the same direction as the nose of the ship.
			vX = vX + (aX * app.cos(theta - app.PI/2));
			vY = vY + (aY * app.sin(theta - app.PI/2));
		} else {
			omega = 0;
		}
		
		
		
	}
}