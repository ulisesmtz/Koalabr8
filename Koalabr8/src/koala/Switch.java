package koala;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import wingman.game.MiscObject;
import wingman.game.GameObject;

public class Switch extends MiscObject{

	public Switch(int x, int y){
		super(new Point(x*40, y*40), new Point(0,0), KoalaWorld.sprites.get("switch_blue"));
	}
	
}


