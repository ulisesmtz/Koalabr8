package wingman.ui;

import java.awt.Graphics;

import koala.KoalaWorld;
import wingman.GameWorld;
import wingman.WingmanWorld;
import wingman.game.*;


public class InfoBar extends InterfaceObject {
	PlayerShip player;
	String name;
	
	
	public InfoBar(PlayerShip player, String name){
		this.player = player;
		this.name = name;
		
	}
	
	public void draw(Graphics g2, int x, int y){
		g2.drawImage(KoalaWorld.sprites.get("rescued"), 0, 0, observer);
		
		for (int i = 0; i < KoalaWorld.saved; i++) {
			g2.drawImage(KoalaWorld.sprites.get("koala_down_3"), x+140 + i*50, 0, observer);
		}
		
        
	}

}
