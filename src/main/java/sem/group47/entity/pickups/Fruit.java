package sem.group47.entity.pickups;

import sem.group47.entity.Player;
import sem.group47.tilemap.TileMap;

/**
 * The class Fruit, gives extra points to the player when picked up.
 *
 * @author Bas
 *
 */
public class Fruit extends PickupObject {
	/**
	 * constructor.
	 *
	 * @param tm
	 *            TileMap
	 */
	public Fruit(final TileMap tm) {
		super(tm);
	}

	/**
	 * Score +25 on pickup.
	 *
	 * @param p
	 *            Player object
	 */
	@Override
	public final void onPickup(final Player p) {
		p.setScore(25);
	}

}
