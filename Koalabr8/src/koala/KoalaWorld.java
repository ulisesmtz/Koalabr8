package koala;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.net.URL;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.*;

import wingman.game.*;
import wingman.*;
import wingman.ui.*;
import wingman.modifiers.*;
import wingman.modifiers.motions.*;
import wingman.modifiers.weapons.*;

public class KoalaWorld extends GameWorld {

    private Thread thread;
    
    // GameWorld is a singleton class!
    private static final KoalaWorld game = new KoalaWorld();
    public static final GameSounds sound = new GameSounds();
    public static final GameClock clock = new GameClock();
    GameMenu menu;
    public KoalaLevel level;
    
	public static HashMap<String,Image> sprites = GameWorld.sprites;
       
    private BufferedImage bimg;
    int score = 0, life = 4;
    Random generator = new Random();
    int sizeX, sizeY;
    Point mapSize;
    
    /*Some ArrayLists to keep track of game things*/
    private ArrayList<Bullet> bullets;
    private ArrayList<PlayerShip> players;
    private ArrayList<InterfaceObject> ui;
    private ArrayList<Ship> powerups;
    
    private ArrayList<MiscObject> miscObject;
    
    public static HashMap<String, MotionController> motions = new HashMap<String, MotionController>();

    // is player still playing, did they win, and should we exit
    boolean gameOver, gameWon, gameFinished;
    ImageObserver observer;
    public static int saved; // koalas saved
        
    // constructors makes sure the game is focusable, then
    // initializes a bunch of ArrayLists
    private KoalaWorld(){
        this.setFocusable(true);
        background = new ArrayList<BackgroundObject>();  
        players = new ArrayList<PlayerShip>();
        ui = new ArrayList<InterfaceObject>();
        powerups = new ArrayList<Ship>();
        bullets = new ArrayList<Bullet>();
        miscObject = new ArrayList<MiscObject>();

    }
    
    /* This returns a reference to the currently running game*/
    public static KoalaWorld getInstance(){
    	return game;
    }


    /*Game Initialization*/
    public void init() {
        setBackground(Color.white);
        loadSprites();
         
        gameOver = false;
        observer = this;
        saved = 0;
        
        level = new KoalaLevel("Resources/level.txt");
        level.addObserver(this);
        mapSize = new Point(level.w*40,level.h*40);
        GameWorld.setSpeed(new Point(0,0));

        addBackground(new Background(mapSize.x,mapSize.y,GameWorld.getSpeed(), sprites.get("background")));
    
    	level.load();
    }
    
    /*Functions for loading image resources*/
    protected void loadSprites(){
	    sprites.put("background", getSprite("Resources/Background.png"));
	    
	    sprites.put("wall", getSprite("Resources/wall2.png"));
	    sprites.put("rock", getSprite("Resources/rock.png"));
	    sprites.put("tnt", getSprite("Resources/TNT.png"));
	    sprites.put("exit_blue", getSprite("Resources/exit_blue.png"));
	    sprites.put("exit_red", getSprite("Resources/exit_red.png"));
	    sprites.put("switch_blue", getSprite("Resources/switch_blue.png"));
	    sprites.put("detonator", getSprite("Resources/detonator.png"));
	    sprites.put("lock_blue", getSprite("Resources/lock_blue.png"));
	    sprites.put("saw", getSprite("Resources/saw.png"));

	    sprites.put("explosion1_1", getSprite("Resources/explosion1_1.png"));
		sprites.put("explosion1_2", getSprite("Resources/explosion1_2.png"));
		sprites.put("explosion1_3", getSprite("Resources/explosion1_3.png"));
		sprites.put("explosion1_4", getSprite("Resources/explosion1_4.png"));
		sprites.put("explosion1_5", getSprite("Resources/explosion1_5.png"));
		sprites.put("explosion1_6", getSprite("Resources/explosion1_6.png"));
	    sprites.put("explosion2_1", getSprite("Resources/explosion2_1.png"));
		sprites.put("explosion2_2", getSprite("Resources/explosion2_2.png"));
		sprites.put("explosion2_3", getSprite("Resources/explosion2_3.png"));
		sprites.put("explosion2_4", getSprite("Resources/explosion2_4.png"));
		sprites.put("explosion2_5", getSprite("Resources/explosion2_5.png"));
		sprites.put("explosion2_6", getSprite("Resources/explosion2_6.png"));
		sprites.put("explosion2_7", getSprite("Resources/explosion2_7.png"));

		
		sprites.put("koala_left_1", getSprite("Resources/koala_left_1.png"));
		sprites.put("koala_left_2", getSprite("Resources/koala_left_2.png"));
		sprites.put("koala_left_3", getSprite("Resources/koala_left_3.png"));
		sprites.put("koala_right_1", getSprite("Resources/koala_right_1.png"));
		sprites.put("koala_right_2", getSprite("Resources/koala_right_2.png"));
		sprites.put("koala_right_3", getSprite("Resources/koala_right_3.png"));
		sprites.put("koala_up_1", getSprite("Resources/koala_up_1.png"));
		sprites.put("koala_up_2", getSprite("Resources/koala_up_2.png"));
		sprites.put("koala_up_3", getSprite("Resources/koala_up_3.png"));
		sprites.put("koala_down_1", getSprite("Resources/koala_down_1.png"));
		sprites.put("koala_down_2", getSprite("Resources/koala_down_2.png"));
		sprites.put("koala_down_3", getSprite("Resources/koala_down_3.png"));
		sprites.put("koala_dead", getSprite("Resources/koala_dead.png"));
		
		sprites.put("rescued", getSprite("Resources/rescued.png"));
		sprites.put("congrats", getSprite("Resources/congrats.png"));
		sprites.put("gameover", getSprite("Resources/gameover.png"));
    }
    
    public Image getSprite(String name) {
        URL url = KoalaWorld.class.getResource(name);
        Image img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }
    
    
    /********************************
     * 	These functions GET things	*
     * 		from the game world		*
     ********************************/
    
    public int getFrameNumber(){
    	return clock.getFrame();
    }
    
    public int getTime(){
    	return clock.getTime();
    }
    
    public void removeClockObserver(Observer theObject){
    	clock.deleteObserver(theObject);
    }
    
    
    
    public ListIterator<MiscObject> getMiscObjects(){
    	return miscObject.listIterator();
    }
    
    
    
    public ListIterator<BackgroundObject> getBackgroundObjects(){
    	return background.listIterator();
    }
    
    public ListIterator<PlayerShip> getPlayers(){
    	return players.listIterator();
    }
    
    public ListIterator<Bullet> getBullets(){
    	return bullets.listIterator();
    }
    
    public ListIterator<Ship> getPowerUps() {
    	return powerups.listIterator();
    }
    
    public int countPlayers(){
    	return players.size();
    }
    
    public void setDimensions(int w, int h){
    	this.sizeX = w;
    	this.sizeY = h;
    }
    
    /********************************
     * 	These functions ADD things	*
     * 		to the game world		*
     ********************************/
    
    public void addBullet(Bullet...newObjects){
    	for(Bullet bullet : newObjects){
    			bullets.add(bullet);
    	}
    }
    
    public void addPlayer(PlayerShip...newObjects){
    	for(PlayerShip player : newObjects){
    		players.add(player);
     		ui.add(new InfoBar(player,Integer.toString(players.size()))); 

    	}    	
    }
    
    // add background items (islands)
    public void addBackground(BackgroundObject...newObjects){
    	for(BackgroundObject object : newObjects){
    		background.add(object);
    	}
    }
    
    
    public void addMiscObject(MiscObject...newObjects){
    	for(MiscObject object : newObjects){
    		miscObject.add(object);
    	}
    }
    
    
    
    // add power ups to the game world
    public void addPowerUp(Ship powerup){
    	powerups.add(powerup);
    }
    
    public void addRandomPowerUp(){
    	// rapid fire weapon or pulse weapon
    	if(generator.nextInt(10)%2==0)
    		powerups.add(new PowerUp(generator.nextInt(sizeX), 1, new SimpleWeapon(5)));
    	else {
			powerups.add(new PowerUp(generator.nextInt(sizeX), 1, new PulseWeapon()));
		}
    }
    
    
    public void addClockObserver(Observer theObject){
    	clock.addObserver(theObject);
    }
    
    // this is the main function where game stuff happens!
    // each frame is also drawn here
    public void drawFrame(int w, int h, Graphics2D g2) {
        ListIterator<?> iterator = getBackgroundObjects();
        ListIterator<?> iteratorMisc = getMiscObjects();
        
        // iterate through all blocks
        while(iterator.hasNext()){
        	BackgroundObject obj = (BackgroundObject) iterator.next();
            obj.update(w, h);
            obj.draw(g2, this);
            
            if(obj instanceof BigExplosion || obj instanceof SmallExplosion){
            	if(!obj.show) iterator.remove();
            	continue;
            }
            
            // check player-block collisions
            ListIterator<PlayerShip> players = getPlayers();
            while(players.hasNext() && obj.show){
            	Koala koala = (Koala) players.next();
            	if (obj.collision(koala)) {
            		Rectangle playerRectangle = koala.getLocation();
            		Rectangle blockRectangle = obj.getLocation();            		
            		if( playerRectangle.x > blockRectangle.x && playerRectangle.y >= blockRectangle.y) koala.move(5,0); // works for down not up
            		else if( playerRectangle.x < blockRectangle.x) koala.move(-5,0);
               		else if (playerRectangle.y < blockRectangle.y) koala.move(0,-5); 
            		else if (playerRectangle.y > blockRectangle.y) koala.move(0,5);   		
            	}        
            }
            
            iteratorMisc = getMiscObjects();
            while (iteratorMisc.hasNext()) {
            	MiscObject obj2 = (MiscObject) iteratorMisc.next();
            	obj2.update(w, h);
            	obj2.draw(g2, this);
            	if (obj2 instanceof Rock && obj2.collision(obj) && obj instanceof Wall) {
            		obj2.move(-6, 0);
        			Rectangle wallLocation = obj.getLocation();
        			Rectangle rockLocation = obj2.getLocation();
        			if (rockLocation.x < wallLocation.x && rockLocation.y >= wallLocation.y) obj2.move(5,0);
        			else if (rockLocation.x > wallLocation.x) obj2.move(5,0);
        			else if (rockLocation.y > wallLocation.y) obj2.move(0,5);
        			else if (rockLocation.y < wallLocation.y) obj2.move(0,-5);
            	}
            }
        }
        
        iteratorMisc = getMiscObjects();

        while(iteratorMisc.hasNext()){
        	MiscObject obj = (MiscObject) iteratorMisc.next();

            // check player-block collisions
            ListIterator<PlayerShip> players = getPlayers();
            iterator = getBackgroundObjects();
            while(players.hasNext() && obj.show){
            	Koala koala = (Koala) players.next();
            	if (obj.collision(koala)) {
            		if (obj instanceof Tnt || obj instanceof Saw) {
            			koala.setImage(KoalaWorld.sprites.get("koala_dead"));
            			gameOver = true;
            			break;
            		}
            		if (obj instanceof Switch) {
            			iteratorMisc.remove();  // remove switch from iterator
            			iteratorMisc = getMiscObjects();
            			while (iteratorMisc.hasNext()) { 
            				MiscObject object = (MiscObject)iteratorMisc.next();
            				if (object instanceof Lock) {
            					iteratorMisc.remove(); // remove lock
            				}
            			}
            		}
            		
            		if (obj instanceof Rock) {
            			Rectangle koalaLocation = koala.getLocation();
            			Rectangle rockLocation = obj.getLocation();
            			if (koalaLocation.x < rockLocation.x && koalaLocation.y >= rockLocation.y) obj.move(5,0);
            			else if (koalaLocation.x > rockLocation.x) obj.move(-5,0);
            			else if (koalaLocation.y > rockLocation.y) obj.move(0,-5);
            			else if (koalaLocation.y < rockLocation.y) obj.move(0,5);
            		}
            		
            		if (obj instanceof BlueExit) {
            			iteratorMisc.remove();
            			players.remove();
            			saved++;            			
            		}
            		
            		if (obj instanceof RedExit) {
            			players.remove();
            			saved++;
            		}
            		
            		if (obj instanceof Detonator) {
            			iteratorMisc.remove();
            			iteratorMisc = getMiscObjects();
            			while (iteratorMisc.hasNext()) {
            				MiscObject object = (MiscObject)iteratorMisc.next();
            				if (object instanceof Tnt) { // blow up all tnts
            					addBackground(new BigExplosion(object.getLocationPoint()));
            					iteratorMisc.remove();
            				}
            			}
            		}
            		
            		if (obj instanceof Lock) {
            			Rectangle koalaLocation = koala.getLocation();
            			Rectangle lockLocation = obj.getLocation();
            			if (koalaLocation.x < lockLocation.x && koalaLocation.y >= lockLocation.y) koala.move(-5,0);
            			else if (koalaLocation.x > lockLocation.x) koala.move(5,0);
            			else if (koalaLocation.y > lockLocation.y) koala.move(0,5);
            			else if (koalaLocation.y < lockLocation.y) koala.move(0,-5);
            		}
            		
            	}        
            	
            }
            obj.update(w, h);
            obj.draw(g2, this);
        }
            	
    	// check if koala hits another koala
    	ListIterator<PlayerShip> players = getPlayers();
    	// make copy of koala in array
    	ArrayList<PlayerShip> playersCopy = new ArrayList<PlayerShip>();
    	while (players.hasNext()) playersCopy.add(players.next());
    	players = getPlayers();
    	while (players.hasNext()) {
    		Koala koala = (Koala) players.next();
    		int size = countPlayers();
    		for (int i = 0; i < size; i++) {
    			if (koala.collision(playersCopy.get(i)) && players.nextIndex()-1 != i) { // check its not same koala
    				Rectangle koala1Rectangle = koala.getLocation();
    				Rectangle koala2Rectangle = playersCopy.get(i).getLocation();
    				if (koala1Rectangle.x < koala2Rectangle.x) {
    					koala.move(-5,0); 
    				}
    				else if (koala1Rectangle.x > koala2Rectangle.x) {
    					koala.move(5,0);
    				}
    				else if (koala1Rectangle.y < koala2Rectangle.y) {
    					koala.move(0,-5);
    				}
    				else if (koala1Rectangle.y > koala2Rectangle.y) {
    					koala.move(0,5); 
    				}
    			
    			}
    		}
    		
			koala.draw(g2, this);

    		if (countPlayers()>0 && !gameOver) koala.update(w, h);
    		if (gameOver)g2.drawImage(KoalaWorld.sprites.get("gameover"),200,200, this);
    		
    	}
    	
    	if (countPlayers()==0) // user won
		g2.drawImage(KoalaWorld.sprites.get("congrats"),100,150,this);        
    }

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);        
        return g2;
    }

    /* paint each frame */
    public void paint(Graphics g) {
        if(players.size()!=0)
        	clock.tick();
    	Dimension windowSize = getSize();
        Graphics2D g2 = createGraphics2D(mapSize.x, mapSize.y);
        drawFrame(mapSize.x, mapSize.y, g2);
        g2.dispose();        
        g.drawImage(bimg, 0, 0, this);
        
        // interface stuff
        ListIterator<InterfaceObject> objects = ui.listIterator();
        int offset = 0;
        while(objects.hasNext()){
        	InterfaceObject object = objects.next();
        	object.draw(g, offset, windowSize.height);
        	offset += 500;
        }
    }

    /* start the game thread*/
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    /* run the game */
    public void run() {
    	
        Thread me = Thread.currentThread();
        while (thread == me) {
        	this.requestFocusInWindow();
            repaint();
          
          try {
                thread.sleep(23); // pause a little to slow things down
            } catch (InterruptedException e) {
                break;
            }
            
        }
    }
    
    /* End the game, and signal either a win or loss */
    public void endGame(boolean win){
    	this.gameOver = true;
    	this.gameWon = win;
    }
    
    public boolean isGameOver(){
    	return gameOver;
    }
    
    // signal that we can stop entering the game loop
    public void finishGame(){
    	gameFinished = true;
    }
    

    /*I use the 'read' function to have observables act on their observers.
     */
	@Override
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
	
	public static void main(String argv[]) {
	    final KoalaWorld game = KoalaWorld.getInstance();
	    JFrame f = new JFrame("Koalabr8");
	    f.addWindowListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        game.requestFocusInWindow();
		    }
	    });
	    f.getContentPane().add("Center", game);
	    f.pack();
	    f.setSize(new Dimension(650, 550));
	    game.setDimensions(650, 550);
	    game.init();
	    f.setVisible(true);
	    f.setResizable(false);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    game.start();
	}
}