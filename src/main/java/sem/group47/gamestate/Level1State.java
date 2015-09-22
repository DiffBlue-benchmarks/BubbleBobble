package sem.group47.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import sem.group47.entity.Enemy;
import sem.group47.entity.HUD;
import sem.group47.entity.Player;
import sem.group47.entity.PlayerSave;
import sem.group47.entity.enemies.Level1Enemy;
import sem.group47.main.GamePanel;
import sem.group47.main.Log;
import sem.group47.tilemap.TileMap;

/**
 * The Class Level1State.
 */
public class Level1State extends GameState {

 /** Whether multiplayer is on **/
 private boolean multiplayer = true;
 
	/** The player. */
	private Player player1;
	private Player player2;

	/** The enemies. */
	private ArrayList<Enemy> enemies;

	/** The hud. */
	private HUD hud;

	/** The tile map. */
	private TileMap tileMap;

	/**
	 * Instantiates a new level1 state.
	 *
	 * @param gsm
	 *            the gsm
	 */
	public Level1State(final GameStateManager gsm) {
		setGsm(gsm);
		init();
	}

	/**
	 * Init.
	 */
	@Override
	public final void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/tiles/Bubble_Tile.gif");
		tileMap.loadMap("/maps/level1.map");
		
		player1 = new Player(tileMap);
		player1.setPosition(30d*(2d+.5d), 30d*(18d+.5d)-1);
		player1.setLives(PlayerSave.getLives());
		player1.setScore(PlayerSave.getScore());
		player1.setExtraLive(PlayerSave.getExtraLive());

		if(multiplayer) {
		 player2 = new Player(tileMap);
		 player2.setPosition(30d*(24d+.5d), 30d*(18d+.5d)-1);
		 player2.setLives(PlayerSave.getLives());
		 player2.setScore(PlayerSave.getScore());
		 player2.setExtraLive(PlayerSave.getExtraLive());
		 player2.setFacingRight(false);
		 //TODO
		 
		}
		
		populateEnemies();
		hud = new HUD(player1);
	}

	/**
	 * populate the game with enemies.
	 */
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		Point[] points = new Point[] { new Point(300, 100),
		  new Point(500, 100), new Point(300, 250), new Point(500, 400),
		  new Point(200, 550) };
		Level1Enemy e;
		for (int i = 0; i < points.length; i++) {
			e = new Level1Enemy(tileMap);
			e.setPosition(points[i].x, points[i].y);
			enemies.add(e);
		}
	}

	/**
	 * Update the player and enemies.
	 */
	@Override
	public final void update() {
		player1.update();
		player1.directEnemyCollision(enemies, getGsm());
		player1.indirectEnemyCollision(enemies);
		
		if(multiplayer) {
		 player2.update();
		 player2.directEnemyCollision(enemies, getGsm());
		 player2.indirectEnemyCollision(enemies);
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
		}

		nextLevel();
	}

	/**
	 * Next level.
	 */
	public final void nextLevel() {
		if (enemies.size() == 0) {
			PlayerSave.setLives(player1.getLives());
			PlayerSave.setScore(player1.getScore());
			PlayerSave.setExtraLive(player1.getExtraLive());
			System.out.println(PlayerSave.getExtraLive());
			getGsm().setState(GameStateManager.LEVEL2STATE);
			Log.info("Player Action", "Player reached next level");
		}
	}

	/**
	 * Draw everything of level 1.
	 */
	@Override
	public final void draw(final Graphics2D g) {

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		tileMap.draw(g);
		player1.draw(g);
		if(multiplayer)
		 player2.draw(g);

		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		hud.draw(g);
	}

	/**
	 * keyPressed.
	 */
	@Override
	public final void keyPressed(final int k) {
	 switch(k) {
	 case KeyEvent.VK_LEFT:
	  player1.setLeft(true);
	  return;
	 case KeyEvent.VK_RIGHT:
	  player1.setRight(true);
	  return;
	 case KeyEvent.VK_UP:
	  player1.setUp(true);
	  return;
	 case KeyEvent.VK_DOWN:
	  player1.setDown(true);
	  return;
	 case KeyEvent.VK_A:
	  if(multiplayer)
	   player2.setLeft(true);
	  return;
	 case KeyEvent.VK_D:
	  if(multiplayer)
	   player2.setRight(true);
	  return;
	 case KeyEvent.VK_W:
	  if(multiplayer)
	   player2.setUp(true);
	  return;
	 case KeyEvent.VK_S:
	  if(multiplayer)
	   player2.setDown(true);
	  return;
	 //case KeyEvent.VK_ESCAPE:
	  //getGsm().setPaused(true);
	 }
	}

	/**
	 * keyReleased.
	 */
	@Override
	public final void keyReleased(final int k) {
	 switch(k) {
  case KeyEvent.VK_LEFT:
   player1.setLeft(false);
   return;
  case KeyEvent.VK_RIGHT:
   player1.setRight(false);
   return;
  case KeyEvent.VK_UP:
   player1.setUp(false);
   return;
  case KeyEvent.VK_DOWN:
   player1.setDown(false);
   return;
  case KeyEvent.VK_A:
   if(multiplayer)
    player2.setLeft(false);
   return;
  case KeyEvent.VK_D:
   if(multiplayer)
    player2.setRight(false);
   return;
  case KeyEvent.VK_W:
   if(multiplayer)
    player2.setUp(false);
   return;
  case KeyEvent.VK_S:
   if(multiplayer)
    player2.setDown(false);
   return;
  case KeyEvent.VK_ESCAPE:
   getGsm().setPaused(true);
   return;
  }
	}

}
