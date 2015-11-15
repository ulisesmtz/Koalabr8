package koala;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import wingman.game.PowerUp;
import wingman.modifiers.AbstractGameModifier;
import wingman.modifiers.weapons.AbstractWeapon;

/*This is where enemies are introduced into the game according to a timeline*/
public class KoalaLevel extends AbstractGameModifier implements Observer {
	int start;
	Integer position;
	String filename;
	BufferedReader level;
	int w, h;
	int endgameDelay = 100;	// don't immediately end on game end
	
	/*Constructor sets up arrays of enemies in a LinkedHashMap*/
	public KoalaLevel(String filename){
		super();
		this.filename = filename;
		String line;
		try {
			level = new BufferedReader(new InputStreamReader(KoalaWorld.class.getResource(filename).openStream()));
			line = level.readLine();
			w = line.length();
			h=0;
			while(line!=null){
				h++;
				line = level.readLine();
			}
			level.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void read(Object theObject){
	}
	
	public void load(){
		KoalaWorld world = KoalaWorld.getInstance();
		
		try {
			level = new BufferedReader(new InputStreamReader(KoalaWorld.class.getResource(filename).openStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		String line;
		try {
			line = level.readLine();
			w = line.length();
			h=0;
			while(line!=null){
				for(int i = 0, n = line.length() ; i < n ; i++) { 
				    char c = line.charAt(i); 
				    
				    if(c=='1'){
				    	Wall wall = new Wall(i,h);
				    	world.addBackground(wall);
				    }
				    
				    if(c=='2'){
				    	Rock rock= new Rock(i,h);
				    	world.addMiscObject(rock);
				    }
				    
				    if(c=='3'){
				    	int[] controls = new int[] {KeyEvent.VK_LEFT,KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER};
						world.addPlayer(new Koala(new Point(i*40, h*40),world.sprites.get("koala_right_3"), controls, "2"));
				    }
				    
				    if(c=='4'){
				    	Tnt tnt = new Tnt(i,h);
				    	world.addMiscObject(tnt);
				    }
				    
				    if (c=='5') {
				    	Switch aswitch = new Switch(i,h);
				    	world.addMiscObject(aswitch);
				    }
				    
				    if (c=='6') {
				    	BlueExit exit = new BlueExit(i,h);
				    	world.addMiscObject(exit);
				    }
				    
				    if (c=='7') {
				    	RedExit exit = new RedExit(i,h);
				    	world.addMiscObject(exit);
				    }
				    
				    if (c=='8') {
				    	Detonator detonator = new Detonator(i,h);
				    	world.addMiscObject(detonator);
				    }
				    
				    if (c=='9') {
				    	Lock lock = new Lock(i,h);
				    	world.addMiscObject(lock);
				    }
				    
				    if (c=='s') {
				    	Saw saw = new Saw(i,h);
				    	world.addMiscObject(saw);
				    }
				}
				
				
				h++;
				line = level.readLine();
			}
			level.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*Level observes GameClock and updates on every tick*/
	@Override
	public void update(Observable o, Object arg) {
		KoalaWorld world = KoalaWorld.getInstance();
		if(world.isGameOver()){
			if(endgameDelay<=0){
				world.removeClockObserver(this);
				world.finishGame();
			} else endgameDelay--;
		}
	}
}
