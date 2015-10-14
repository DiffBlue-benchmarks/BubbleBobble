package sem.group47.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;

import sem.group47.entity.Animation;
import sem.group47.entity.MapObject;
import sem.group47.tilemap.TileMap;

public class Magiron extends Enemy {	
	private MapObject target;

	public Magiron(TileMap tm) {
		super(tm);

		setScorePoints(100);
		setWidth(90);
		setHeight(112);
		setCwidth(30);
		setCheight(30);
		setMovSpeed(1.3);

		setFacingRight(true);

		BufferedImage[] animationSprites = null;
		try {
			ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
			File input = new File("src/main/resources/enemies/magiaaron.gif");
			ImageInputStream stream = ImageIO.createImageInputStream(input);
			reader.setInput(stream);

			int count = reader.getNumImages(true);
			animationSprites = new BufferedImage[count];
			for (int index = 0; index < count; index++) {
				BufferedImage frame = reader.read(index);
				animationSprites[index] = frame;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(animationSprites);
		animation.setDelay(60);
	}

	@Override
	public final void draw(final Graphics2D g) {
		if (facingRight) {
			g.drawImage(animation.getImage(), (int) (getXpos() - getWidth() / (double) 2),
					(int) (getYpos() - getHeight() / (double) 2), getWidth(), getHeight(), null);
		} else {
			g.drawImage(animation.getImage(), (int) (getXpos() + getWidth() / (double) 2),
					(int) (getYpos() - getHeight() / (double) 2), -getWidth(), getHeight(), null);
		}
	}

	public void setTarget(MapObject t) {
		target = t;
	}

	@Override
	public void update() {
		animation.update();
		if(target != null) {
			moveTowards(target);
		}
	}

	public void moveTowards(MapObject mo) {
		moveTowards(mo.getx(), mo.gety());
	}

	public void moveTowards(double x, double y) {
		double newX = getx();
		double newY = gety();
		double speed = getMovSpeed();
		if (x - speed > getx() ) {
			newX += speed;
			facingRight = false;
		} else if (x + speed < getx()) {
			newX -= speed;
			facingRight = true;
		}
		if(y - speed > gety()) {
			newY += speed;
		} else if (y + speed < gety()) {
			newY -= speed;
		}
		setPosition(newX, newY);
	}

	@Override
	public void hit() {
		caught = false;
	}

	@Override
	public void setCaught() {
		caught = false;
	}

	@Override
	public void setCaught(boolean isCaught) {
		caught = false;
	}

}
