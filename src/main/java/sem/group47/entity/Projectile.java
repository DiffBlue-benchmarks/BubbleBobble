package sem.group47.entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import sem.group47.tilemap.TileMap;

/**
 * The Class Projectile.
 */
public class Projectile extends MapObject {

	/** The life time in ms */
	private int lifeTime;

	/** The float delay. */
	private int floatDelay;

	/** The last update time. */
	private long lastUpdateTime;

	/** The floating. */
	private boolean floating;

	/** The float speed. */
	private double floatSpeed;

	/**
	 * Instantiates a new projectile.
	 *
	 * @param tm
	 *            the tm
	 */
	public Projectile(TileMap tm) {
		super(tm);
		isAlive = true;
		width = 32;
		height = 32;
		cwidth = 20;
		cheight = 20;
		dx = 3;

		lifeTime = 7500;
		floatDelay = 1000;
		lastUpdateTime = System.currentTimeMillis();
		floating = false;
		floatSpeed = .01;

		try {
			BufferedImage spritesheet = ImageIO.read(getClass()
					.getResourceAsStream("/player/bubbles.png"));
			sprite = spritesheet.getSubimage(96, 0, 32, 32);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update.
	 */
	public void update() {
		long timePassed = System.currentTimeMillis() - lastUpdateTime;
		floatDelay -= timePassed;
		lifeTime -= timePassed;
		lastUpdateTime = System.currentTimeMillis();
		if (floatDelay <= 0) {
			dx = 0;
			dy -= floatSpeed;
		}
		if (lifeTime <= 0) {
			isAlive = false;
			return;
		}

		checkTileMapCollision();
		setPosition(xposNew, yposNew);
	}

	public int getFloatDelay() {
		return floatDelay;
	}

	public void setFloatDelay(int floatDelay) {
		this.floatDelay = floatDelay;
	}

	public boolean isFloating() {
		return floating;
	}

	public void setFloating(boolean floating) {
		this.floating = floating;
	}

	public double getFloatSpeed() {
		return floatSpeed;
	}

	public void setFloatSpeed(double floatSpeed) {
		this.floatSpeed = floatSpeed;
	}

}
