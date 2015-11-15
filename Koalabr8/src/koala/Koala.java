package koala;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Observable;

import wingman.GameWorld;
import wingman.game.BigExplosion;
import wingman.game.PlayerShip;
import wingman.game.SmallExplosion;
import wingman.modifiers.AbstractGameModifier;
import wingman.modifiers.motions.InputController;
import wingman.modifiers.weapons.SimpleWeapon;

public class Koala extends PlayerShip {
	int direction;

	public Koala(Point location, Image img, int[] controls, String name) {
		super(location, new Point(0, 0), img, controls, name);
		resetPoint = new Point(location);
		this.name = name;
		motion = new InputController(this, controls, KoalaWorld.getInstance());
		height = 37;
		width = 37;
		direction = 0;
		this.location = new Rectangle(location.x, location.y, width, height);
	}

	public void turn(int angle) {
		this.direction += angle;
		if (this.direction > 360) {
			this.direction = 0;
		} else if (this.direction <= 0) {
			this.direction = 359;
		}
	}

	
	public void update(int w, int h) {	
		// allowed either left/right or up/down movement, not diagonal
		
	  	if (right==1) {
	  		location.x += (right-left) * 3; 
	  		img = KoalaWorld.sprites.get("koala_right_1");
	  		
	  	} else 	if (left == 1) {
	  		location.x += (right-left) * 3; 
	  		img = KoalaWorld.sprites.get("koala_left_1");

	  	}
	  	else if (down == 1){
    		location.y += (down-up) * 3;
	  		img = KoalaWorld.sprites.get("koala_down_1");

    	}
	  	else if (up == 1) {
	  		location.y += (down-up) * 3;
	  		img = KoalaWorld.sprites.get("koala_up_1");

	  	}
	  	
	  	if(location.y<80) location.y=81; // top boundary of level
     	if(location.y>400) location.y=399; // bottom boundary 
     	if(location.x<40) location.x=41; // left boundary 
     	if(location.x>560) location.x=559; // right boundary

	}

	public void draw(Graphics g, ImageObserver obs) {
		if (respawnCounter <= 0)
			g.drawImage(
					img, // the image
					location.x,location.y, // destination top left
					location.x + this.getSizeX(),location.y + this.getSizeY(), // destination lower right
					(direction / 6) * this.getSizeX(),0, // source top left
					((direction / 6) * this.getSizeX()) + this.getSizeX(),this.getSizeY(), // source lower right
					obs);
		else if (respawnCounter == 80) {
			KoalaWorld.getInstance().addClockObserver(this.motion);
			respawnCounter -= 1;
			System.out.println(Integer.toString(respawnCounter));
		} else if (respawnCounter < 80) {
			if (respawnCounter % 2 == 0)
				g.drawImage(img, // the image
						location.x,location.y, // destination top left
						location.x + this.getSizeX(),location.y + this.getSizeY(), // destination lower right
						(direction / 6) * this.getSizeX(),0, // source top left
						((direction / 6) * this.getSizeX()) + this.getSizeX(),this.getSizeY(), // source lower right
						obs);
			respawnCounter -= 1;
		} else
			respawnCounter -= 1;
	}

	public void die() {
		this.show = false;
		GameWorld.setSpeed(new Point(0, 0));
		BigExplosion explosion = new BigExplosion(new Point(location.x,location.y));
		KoalaWorld.getInstance().addBackground(explosion);
		lives -= 1;
		if (lives >= 0) {
			KoalaWorld.getInstance().removeClockObserver(this.motion);
			reset();
		} else {
			this.motion.delete(this);
		}
	}

	public void reset() {
		this.setLocation(resetPoint);
		health = strength;
		respawnCounter = 160;
		this.weapon = new TankWeapon();
	}

}
