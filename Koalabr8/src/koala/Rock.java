package koala;

import java.awt.Point;
import java.awt.image.ImageObserver;

import wingman.game.MiscObject;

public class Rock extends MiscObject {
	
	public Rock(int x, int y){
		super(new Point(x*40, y*40), new Point(0,0), KoalaWorld.sprites.get("rock"));
	}
 
}
