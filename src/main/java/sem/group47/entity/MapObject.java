package sem.group47.entity;

import sem.group47.tilemap.Tile;
import sem.group47.tilemap.TileMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


/**
 * MapObject, SuperClass for all movable and interactable objects inside the
 * gameworld.
 */

public abstract class MapObject {

	// TILEMAP

	/** The tile map. */
	protected TileMap tileMap;

	/** The tile size. */
	protected int tileSize;

	// POSITION AND VECTOR

	/** The actual x position. */
	protected double xpos;

	/** The actual y position. */
	protected double ypos;

	/** Vector in x direction. */
	protected double dx;

	/** Vector in y direction. */
	protected double dy;

	// SPRITE DIMENSION

	/** The width. */
	protected int width;

	/** The height. */
	protected int height;

	// COLLISION

	/** The cwidth. */
	protected int cwidth;

	/** The cheight. */
	protected int cheight;

	/** The curr row. */
	protected int currRow;

	/** The curr col. */
	protected int currCol;

	/** The xdest: x position + x vector(x+dx). */
	protected double xdest;

	/** The ydest: y position + y vector(y+dy). */
	protected double ydest;

	/** The xtemp: Helper variable used in physics calculation. */
	protected double xtemp;

	/** The ytemp. Helper variable used in physics calculation. */
	protected double ytemp;

	/** The top left. */
	protected boolean topLeftBlocked;

	/** The top right. */
	protected boolean topRightBlocked;

	/** The bottom left. */
	protected boolean bottomLeftBlocked;

	/** The bottom right. */
	protected boolean bottomRightBlocked;

	/** The top left. */
	protected boolean topLeftSemiBlocked;

	/** The top right. */
	protected boolean topRightSemiBlocked;

	/** The bottom left. */
	protected boolean bottomLeftSemiBlocked;

	/** The bottom right. */
	protected boolean bottomRightSemiBlocked;

	// ANIMATION

	/** The current action. */
	protected int currentAction;

	/** The previous action. */
	protected int previousAction;

	/** The facing right. */
	protected boolean facingRight;

	// MOVEMENT

	/** The left. */
	protected boolean left;

	/** The right. */
	protected boolean right;

	/** The up. */
	protected boolean up;

	/** The down. */
	protected boolean down;

	/** The jumping. */
	protected boolean jumping;

	/** The falling. */
	protected boolean falling;

	/** The mov speed. */
	protected double movSpeed;

	/** The max speed. */
	protected double maxSpeed;

	/** The stop speed. */
	protected double stopSpeed;

	/** The fall speed. */
	protected double fallSpeed;

	/** The max fall speed. */
	protected double maxFallSpeed;

	/** The jump start. */
	protected double jumpStart;

	/** The stop jump speed. */
	protected double stopJumpSpeed;

	/** The is alive. */
	protected boolean isAlive;

	// GRAPHICS
	/** The sprite. */
	protected BufferedImage sprite;

	/**
	 * Constructor.
	 *
	 * @param tm
	 *            TileMap in which this object lives
	 */
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}

	/**
	 * Determines if this MapObject intersects with another object.
	 *
	 * @param o
	 *            the other MapObject
	 * @return true if they intersect
	 */
	public boolean intersects(MapObject obj) {
		return this.getRectangle().intersects(obj.getRectangle());
	}

	/**
	 * returns a Rectangle object describing the collisionbox of the MapObject.
	 *
	 * @return Rectangle
	 */
	public Rectangle getRectangle() {
		return new Rectangle((int) xpos - cwidth / 2, (int) ypos - cheight / 2,
				cwidth, cheight);
	}

	/**
	 * modifies xtemp and ytemp so that they don't intersect with the tileMap.
	 */
	public void checkTileMapCollision() {
		// Determine current column and row
		currCol = (int) xpos / tileSize;
		currRow = (int) ypos / tileSize;

		// Destination coordinates
		xdest = xpos + dx;
		ydest = ypos + dy;

		// Temporary coordinates
		xtemp = xpos;
		ytemp = ypos;

		calculateCorners(xpos, ydest);

		// If we are moving upwards
		if (dy < 0) {
			// And our topLeft or topRight corner is
			// colliding with a 'BLOCKED' tile
			if (topLeftBlocked || topRightBlocked) {
				// Our vertical movement vector becomes 0
				dy = 0;

				// Our new position becomes just below the tile above we are
				// colliding with
				ytemp = currRow * tileSize + cheight / 2;
			} else { // If we are not colliding
				// Our y vector is added to our temporary y position
				ytemp += dy;
			}
		} else if (dy > 0) { // If we are moving downwards
			// And our bottomLeft or bottomRight corner is colliding with a
			// 'BLOCKED' or 'SEMIBLOCKED' tile
			if (bottomLeftBlocked || bottomRightBlocked
					|| bottomLeftSemiBlocked || bottomRightSemiBlocked) {

				// Our horizontal movement vector becomes 0
				dy = 0;
				// We are standing on a solid tile, so we are not falling
				falling = false;

				// Our new position becomes just on top of the tile we are
				// colliding with
				//
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			} else { // If we are not colliding
				// Our y vector is added to our temporary y position
				ytemp += dy;
				if (ytemp >= tileMap.getHeight() - 15) {
					ytemp = 3 * tileMap.getTileSize();
				}
			}
		}

		// Check for collisions in the x direction
		calculateCorners(xdest, ypos);

		// If we are moving to the left
		if (dx < 0) {
			// And we are colliding on the left

			if (topLeftBlocked) {
				// Our horizontal movement vector becomes 0
				dx = 0;

				// Our new position is set to just to the right of the blocking
				// tile
				xtemp = currCol * tileSize + cwidth / 2;

			} else { // If we are not colliding
				// Apply the horizontal movement vector
				xtemp += dx;
			}
		} else if (dx > 0) { // If we are moving to the right
			// And we are colliding on the right
			if (topRightBlocked) {
				// Our horizontal movement vector becomes 0
				dx = 0;

				// Our new position is set just to the left of the blocking tile
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			} else { // If we are not colliding

				// Apply the horizontal movement vector;
				xtemp += dx;
			}
		}

		// Check if we are falling
		if (!falling) {
			// See if we move 1 pixel downwards if we are colliding
			calculateCorners(xpos, ydest + 1);

			// If we do not collide we are falling
			if (!bottomLeftBlocked && !bottomLeftSemiBlocked
					&& !bottomRightSemiBlocked && !bottomRightBlocked) {
				falling = true;
			}
		}
	}

	/**
	 * calculates if any of the corners of the object intersect with tiles and
	 * sets the topLeft, topRight, bottomLeft and bottomRight boolean
	 * accordingly.
	 *
	 * @param xin
	 *            xposition to check
	 * @param yin
	 *            yposition to check
	 */
	@SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
	public void calculateCorners(double xin, double yin) {
		int leftTile = (int) ((xin - cwidth / 2) / (double) tileSize);
		int rightTile = (int) ((xin - cwidth / 2 + cwidth - 1) / (double) tileSize);
		int topTile = (int) ((yin - cheight / 2) / (double) tileSize);
		int bottomTile = (int) ((yin - cheight / 2 + cheight) / (double) tileSize);
		
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		// Flags for whether we are colliding
		topLeftBlocked = tl == Tile.BLOCKED;
		topRightBlocked = tr == Tile.BLOCKED;
		bottomLeftBlocked = bl == Tile.BLOCKED;
		bottomRightBlocked = br == Tile.BLOCKED;
		topLeftSemiBlocked = tl == Tile.SEMIBLOCKED;
		topRightSemiBlocked = tr == Tile.SEMIBLOCKED;
		bottomLeftSemiBlocked = bl == Tile.SEMIBLOCKED;
		bottomRightSemiBlocked = br == Tile.SEMIBLOCKED;
	}

	/**
	 * Draw.
	 *
	 * @param gr
	 *            the graphics
	 */
	public void draw(Graphics2D gr) {
		if (facingRight) {
			gr.drawImage(sprite, (int) (xpos - width / (double) 2),
					(int) (ypos - height / (double) 2),	null);
		} else {
			gr.drawImage(sprite, (int) (xpos + width / (double) 2), 
					(int) (ypos - height / (double) 2),	
					-width, height, null);
		}
	}

	/**
	 * Gets the checks if is alive.
	 *
	 * @return the checks if is alive
	 */
	public boolean getIsAlive() {
		return isAlive;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getx() {
		return xpos;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public double gety() {
		return ypos;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the c width.
	 *
	 * @return the c width
	 */
	public int getCWidth() {
		return cwidth;
	}

	/**
	 * Gets the c height.
	 *
	 * @return the c height
	 */
	public int getCHeight() {
		return cheight;
	}

	/**
	 * Sets the position.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setPosition(double xnew, double ynew) {
		this.xpos = xnew;
		this.ypos = ynew;
	}

	/**
	 * Sets the vector.
	 *
	 * @param dx
	 *            the dx
	 * @param dy
	 *            the dy
	 */
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * Sets the left.
	 *
	 * @param b
	 *            the new left
	 */
	public void setLeft(boolean bleft) {
		left = bleft;
	}

	/**
	 * Sets the right.
	 *
	 * @param b
	 *            the new right
	 */
	public void setRight(boolean bright) {
		right = bright;
	}

	/**
	 * Sets the up.
	 *
	 * @param b
	 *            the new up
	 */
	public void setUp(boolean bup) {
		up = bup;
	}

	/**
	 * Sets the down.
	 *
	 * @param b
	 *            the new down
	 */
	public void setDown(boolean bdown) {
		down = bdown;
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

}
