/*
 * @author Abdul-Azeez Rabiu
 * 
 * "DODGE THE ASTEROIDS"
 * Guide your rocket ship through space in order to dodge the incoming asteroids and survive as long as you can.
 * The longer you last, the higher your score is. However, the longer you last, the harder the game also gets.
 * Use the '[' key to turn left, the ']' key to turn right, and the spacebar to accelerate
 * Press 'A' to add fixed bounding boxes to the ship and the asteroids
 * Press 'R' to add relative bounding boxes to the ship and the asteroids
 * Press 'N' to remove any bounding boxes
 */

import processing.core.*;

public class Prog_3 extends PApplet {
	// Our spaceship
	private Spaceship ship;
	// To store the ship's initial health
	private float initHealth;
	
	// The asteroids
	private Asteroid[] astField;
	
	// A timer mechanism for controlling the frequency of asteroids
	private long timer;
	private float wait = 3000; // This variable is in milliseconds
	
	// Variable to count how many asteroids have been created
	private int astCounter = 0;
	
	// Bounding box mode: No box, Relative, or Absolute
	private Mode mode;
	
	
	public void settings(){
		size(1200,800);
	}
	
	
	public void setup() {
		frameRate(500);
		// millis() returns the number of milliseconds since starting the program 
		timer = millis();
		
		// Initially, there shouldn't be any bounding boxes
		mode = mode.NO_BOX;
		
		// Spaceship constructor:
		// Spaceship(PApplet pen, float theShipX, float theShipY, float theScale, int theHealth, 
		// int theShipR, int theShipG, int theShipB,
		// int theExhaustR, int theExhaustG, int theExhaustB)
		ship = new Spaceship(this, width/2, height/2, 1.5f, 300, 255, 0, 50, 255, 175, 0);
		initHealth = ship.getHealth();
		
		astField = new Asteroid[50];
		for (int i = 0; i < astField.length; i++) {
			astField[i] = new Asteroid(this);
		}
		
	}
	
	
	public void draw() {
		background(0,11,84);
		
		// This block of if statements is responsible for what type of bounding boxes to draw
		if (keyPressed && key == 'a') {
			// If the A key is pressed, then we have absolute boxes
			mode = mode.ABSOLUTE_BOX;
		} else if (keyPressed && key == 'r') {
			// If the R key is pressed, then we have relative boxes
			mode = mode.RELATIVE_BOX;
		} else if (keyPressed && key == 'n') {
			// If the N key is pressed, then there are no boxes
			mode = mode.NO_BOX;
		}
		
		// Drawing and animating the ship
		ship.draw(mode);
		ship.animate();
		
		// Using the timer to control the entry of each asteroid
		if (millis() - timer >= wait) {
			astCounter++;
			// Updating the timer
			timer = millis();
		}
		
		// Reset the asteroid counter if it exceeds the capacity of the asteroid array, then create new asteroid objects 
		if (astCounter >= astField.length) {
			for (int i = 0; i < astField.length; i++) {
				astField[i] = new Asteroid(this);
			}
			astCounter = 0;
		}
		
		// Drawing and animating the asteroids
		for (int i = 0; i < astCounter; i++) {
			astField[i].draw(mode);
			astField[i].animate();
			// Also checking for a collision with the spaceship
			if (ship.collision(astField[i])) {
				ship.crash();
			}
		}
		
		// The HUD (Head Up Display)
		// Shows the player's score and health
		// Score increases with time, meaning the longer you last, the higher your score
		textSize(16);
		fill(255);
		text("Score: " + millis() / 1000, 30, 30);
		
		// Increases the difficulty as the score increases by reducing the wait time
		if (millis() >= 19990 && millis() <= 20000) {
			wait = wait * 0.8f;
		} else if (millis() >= 39990 && millis() <= 40000) {
			wait = wait * 0.7f;
		} else if (millis() >= 54990 && millis() == 55000) {
			wait = wait * 0.6f;
		} else if (millis() >= 64990 && millis() == 65000) {
			wait = wait * 0.5f;
		}
		
		// Health bar
		noStroke();
		fill(0,200,75);
		float healthBar = ship.getHealth() / initHealth;
		rect(30, 60, 150 * healthBar, 10);
		stroke(255);
		strokeWeight(1);
		noFill();
		rect(30, 60, 150, 10);
		fill(255);
		text("Health: " + (int)(healthBar * 100), 30, 48);
		
		// If the ship's health hits zero, then game over! 
		// noLoop() causes the draw function to stop executing, effectively halting the program
		if (ship.getHealth() <= 0) {
			noLoop();
		}
		
	}
	
	/*
	 * The driver
	 */
	public static void main(String[] args){
		PApplet.main("Prog_3");
	}
}