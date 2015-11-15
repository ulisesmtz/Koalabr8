package wingman.game;

import java.awt.Image;
import java.awt.Point;

import wingman.GameWorld;

public class MiscObject extends GameObject{
	public MiscObject(Point location, Image img){
		super(location, GameWorld.getSpeed(), img);
	}
	
	public MiscObject(Point location, Point speed, Image img){
		super(location, speed, img);
	}

	
}
