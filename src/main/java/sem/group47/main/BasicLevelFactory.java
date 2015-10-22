package sem.group47.main;

import java.util.ArrayList;

import sem.group47.entity.Player;
import sem.group47.entity.PlayerSave;
import sem.group47.entity.enemies.Enemy;
import sem.group47.entity.enemies.Level1Enemy;
import sem.group47.entity.enemies.Magiron;
import sem.group47.entity.enemies.ProjectileEnemy;
import sem.group47.entity.pickups.BubbleSizePowerup;
import sem.group47.entity.pickups.BubbleSpeedPowerup;
import sem.group47.entity.pickups.MovementSpeedPowerup;
import sem.group47.entity.pickups.PickupObject;
import sem.group47.tilemap.TileMap;

public class BasicLevelFactory implements LevelFactory {

	private TileMap tileMap;
	public Level makeLevel(String filename, boolean multiplayer) {
		Level newlevel = new Level();
		loadTileMap(filename, newlevel);
		loadPlayers(newlevel, multiplayer);
		populateEnemies(newlevel);
		populatePowerups(newlevel);
		return newlevel;
	}
	
	private void loadTileMap(final String levelFileName, Level level) {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/tiles/Bubble_Tile.gif");
		tileMap.loadMap("/maps/" + levelFileName);
		level.setTileMap(tileMap);
	};
	
	private void populateEnemies(Level level) {
		ArrayList<int[]> points = tileMap.getEnemyStartLocations();
		Enemy enemy;
		int j = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			switch (points.get(i)[2]) {
			case Enemy.LEVEL1_ENEMY:
				enemy = new Level1Enemy(tileMap);
				break;
			case Enemy.PROJECTILE_ENEMEY:
				enemy = new ProjectileEnemy(tileMap);
				break;
			default:
				enemy = new Level1Enemy(tileMap);
			}
			enemy.setPosition((points.get(i)[0] + .5d) * 30,
					(points.get(i)[1] + 1) * 30
							- .5d * enemy.getCHeight());
			level.addEnemy(enemy);
			j = i;
		}

		Magiron aaron = new Magiron(tileMap);
		aaron.setPosition(GamePanel.WIDTH / 2, -150);
		level.addAaron(aaron);
	};
	
	public void loadPlayers(Level level, boolean multiplayer) {
		Player player1 = new Player(tileMap);
		player1.setPosition(tileMap.getTileSize() * (2d + .5d) + 5,
				tileMap.getTileSize() * (tileMap.getNumRows() - 2 + .5d));
		player1.setLives(PlayerSave.getLivesP1());

		player1.setExtraLive(PlayerSave.getExtraLiveP1());
		player1.setScore(PlayerSave.getScoreP1());
		level.setPlayer1(player1);

		if (multiplayer) {
			Player player2 = new Player(tileMap);
			player2.setPosition(
					tileMap.getTileSize() * (tileMap.getNumCols() - 3 + .5d) - 5,
					tileMap.getTileSize() * (tileMap.getNumRows() - 2 + .5d));
			player2.setLives(PlayerSave.getLivesP2());
			player2.setExtraLive(PlayerSave.getExtraLiveP2());
			player2.setScore(PlayerSave.getScoreP2());
			player2.setFacingRight(false);
			level.setPlayer2(player2);
		}
	};
	
	/**
	 * loads the powerups.
	 */
	private void populatePowerups(Level level) {
		PickupObject po = new MovementSpeedPowerup(tileMap);
		po.setPosition(100, 100);
		level.addPickup(po);
		po = new BubbleSizePowerup(tileMap);
		po.setPosition(tileMap.getWidth() - 100, 100);
		level.addPickup(po);
		po = new BubbleSpeedPowerup(tileMap);
		po.setPosition(tileMap.getWidth() / 2, 100);
		level.addPickup(po);
	}

}
