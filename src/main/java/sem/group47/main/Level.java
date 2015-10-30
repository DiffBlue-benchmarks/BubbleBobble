package sem.group47.main;

import java.awt.Graphics2D;
import java.util.ArrayList;

import sem.group47.audio.AudioPlayer;
import sem.group47.entity.Player;
import sem.group47.entity.PlayerSave;
import sem.group47.entity.WaterfallHolder;
import sem.group47.entity.enemies.Enemy;
import sem.group47.entity.enemies.Magiron;
import sem.group47.entity.pickups.Fruit;
import sem.group47.entity.pickups.PickupObject;
import sem.group47.tilemap.TileMap;

/**
 * The class Level, contains the logic of objects and their interactions. It
 * requires a LevelFactory to initialize it.
 *
 * @author Karin
 *
 */
public class Level extends DrawComposite {

	/** List of enemies in the level. */
	private ArrayList<Enemy> enemies;
	/** List of pickups in the level. */
	private ArrayList<PickupObject> pickups;
	/** The Magiron. */
	private Magiron magiron;
	/** The current tileMap. */
	private TileMap tileMap;
	/** The player 1. */
	private Player player1;
	/** Boolean to check whether there are 2 players. */
	private boolean multiplayer;
	/** Player 2 if there is one. */
	private Player player2;
	/** The current level count. */
	private int levelStepCount;
	/** Time in seconds before magiron appears. */
	private static int magiron_APPEAR_DELAY = 30;

	/** The Waterfall. */
	private WaterfallHolder waterfall;

	/**
	 * Constructor - initializes the lists and level count.
	 */
	public Level() {
		multiplayer = PlayerSave.getMultiplayerEnabled();
		clearComponents();
		enemies = new ArrayList<Enemy>();
		pickups = new ArrayList<PickupObject>();
		levelStepCount = 0;
	}

	/**
	 * Adds an enemy to the level.
	 *
	 * @param newEnemy
	 *            - a Magiron
	 */
	public final void addEnemy(final Enemy newEnemy) {
		enemies.add(newEnemy);
		addComponent(newEnemy);
	}

	/**
	 * Adds the Magiron to the level.
	 *
	 * @param newEnemy
	 *            - a Magiron
	 */
	public final void addMagiron(final Magiron newEnemy) {
		magiron = newEnemy;
		addComponent(magiron);
	}

	/**
	 * Adds the Waterfall to the level.
	 *
	 * @param pwaterfall
	 *            - a Waterfall
	 */
	public final void addWaterfall(final WaterfallHolder pwaterfall) {
		waterfall = pwaterfall;
		addComponent(waterfall);
	}

	/**
	 * Adds a pickup to the level.
	 *
	 * @param pu
	 *            - the PickupObject
	 */
	public final void addPickup(final PickupObject pu) {
		pickups.add(pu);
		addComponent(pu);
	}

	/**
	 * Updates the level and the things it contains.
	 */
	public final void update() {
		updatePlayers();
		updateMagiron();
		updateWaterfall();
		updateEnemies();
		updatePickups();
		levelStepCount++;
	}

	/**
	 * Updates the Players.
	 */
	public final void updatePlayers() {
		if (player1.getLives() > 0) {
			player1.update();
			directEnemyCollision(player1);
			player1.indirectEnemyCollision(enemies);
		} else {
			removeComponent(player1);
		}
		if (multiplayer) {
			if (player2.getLives() > 0) {
				player2.update();
				directEnemyCollision(player2);
				player2.indirectEnemyCollision(enemies);
			} else {
				removeComponent(player2);
			}
		}
	}

	/**
	 * Updates the Magiron.
	 */
	public final void updateMagiron() {
		if (levelStepCount == GamePanel.FPS * magiron_APPEAR_DELAY) {
			targetmagiron();
		}
		magiron.update();
	}

	/**
	 * Updates the waterfall.
	 */
	public final void updateWaterfall() {
		if (waterfall != null) {
			waterfall.update();
			waterfall.playerInteraction(player1);
			if (multiplayer) {
				waterfall.playerInteraction(player2);
			}
		}
	}

	/**
	 * Updates the enemies.
	 */
	public final void updateEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
			if (enemies.get(i).projectileCollision(player1)) {
				player1.kill();
			}
			if (multiplayer && enemies.get(i).projectileCollision(player2)) {
				player2.kill();
			}
		}
	}

	/**
	 * Updates the pickups.
	 */
	public final void updatePickups() {
		for (int i = 0; i < pickups.size(); i++) {
			if (pickups.get(i).checkCollision(player1)
					|| (multiplayer && pickups.get(i).checkCollision(player2))) {
				AudioPlayer.play("extraLife");
				removeComponent(pickups.get(i));
				pickups.remove(i);

				i--;
			} else {
				pickups.get(i).update();
			}
		}
	}

	/**
	 * checks what happens when the player directly collides with an enemy.
	 *
	 * @param player
	 *            the Player object to check collisions with
	 */
	public final void directEnemyCollision(final Player player) {
		if (player.intersects(magiron)) {
			player.kill();
			targetmagiron();
		}

		for (int i = 0; i < enemies.size(); i++) {

			if (player.intersects(enemies.get(i))) {
				if (enemies.get(i).isCaught()) {
					Fruit fr = new Fruit(tileMap);

					if (enemies.get(i).getXpos() > 400) {
						fr.setPosition(enemies.get(i).getXpos() - 72, enemies
								.get(i).getYpos());
						fr.setDx(-4);
					} else {
						fr.setPosition(enemies.get(i).getXpos() + 72, enemies
								.get(i).getYpos());
						fr.setDx(4);
					}
					pickups.add(fr);
					addComponent(fr);

					player.setScore(enemies.get(i).getProperties().getPoints());
					removeComponent(enemies.get(i));
					enemies.remove(i);

					Log.info("Player Action",
							"Player collision with Caught Enemy");

				} else if (player.getLives() > 1) {
					player.hit(1);
					Log.info("Player Action", "Player collision with Enemy");

				} else {
					AudioPlayer.play("crash");
					player.hit(1);
					Log.info("Player Action", "Player collision with Enemy");
				}
			}
		}

	}

	/**
	 * Target magiron.
	 */
	private void targetmagiron() {
		if (multiplayer) {
			if (player1.getLives() > 0) {
				if (Math.random() > .5d || player2.getLives() <= 0) {
					magiron.setTarget(player1);
				} else {
					magiron.setTarget(player2);
				}
			} else {
				magiron.setTarget(player2);
			}
		} else {
			magiron.setTarget(player1);
		}
	}

	/**
	 * checks if the player is dead.
	 *
	 * @return boolean true if the player has lost.
	 */
	public final boolean hasLost() {
		if (player1.getLives() <= 0) {
			removeComponent(player1);
			if (!multiplayer || player2.getLives() <= 0) {

				PlayerSave.setScoreP1(player1.getScore());

				if (multiplayer) {
					PlayerSave.setScoreP2(player2.getScore());
				}

				return true;
			}
		} else if (multiplayer && player2.getLives() <= 0) {
			removeComponent(player2);
		}
		return false;
	}

	/**
	 * Returns whether the level has been won.
	 *
	 * @return boolean true if the level has been won.
	 */
	public final boolean hasWon() {
		if (enemies.size() == 0) {
			removeComponent(player1);
			if (multiplayer) {
				removeComponent(player2);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public final void draw(final Graphics2D gr) {
		drawComponents(gr);
	}

	/**
	 * Returns the player 1 or null if there is none.
	 *
	 * @return Player object for player 1 or null.
	 */
	public final Player getPlayer1() {
		return player1;
	}

	/**
	 * Sets the player 1.
	 *
	 * @param player
	 *            - a player object.
	 */
	public final void setPlayer1(final Player player) {
		if (player1 != null) {
			removeComponent(player1);
		}
		player1 = player;
		addComponent(player);
	}

	/**
	 * Sets the player 2.
	 *
	 * @param player
	 *            - a player object.
	 */
	public final void setPlayer2(final Player player) {
		if (player2 != null) {
			removeComponent(player1);
		}
		player2 = player;
		addComponent(player);
	}

	/**
	 * Returns the player 2 or null if there is none.
	 *
	 * @return Player object for player 2 or null.
	 */
	public final Player getPlayer2() {
		return player2;
	}

	/**
	 * Get the current TileMap.
	 *
	 * @return A tileMap object.
	 */
	public final TileMap getTileMap() {
		return tileMap;
	}

	/**
	 * Set a tileMap.
	 *
	 * @param tilemap
	 *            the new tile map
	 */
	public final void setTileMap(final TileMap tilemap) {
		if (tileMap != null) {
			removeComponent(tileMap);
		}
		tileMap = tilemap;
		addComponent(tileMap);
	}

}
