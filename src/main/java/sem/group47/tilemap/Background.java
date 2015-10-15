package sem.group47.tilemap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import sem.group47.entity.Animation;
import sem.group47.main.GamePanel;

public class Background {

	/** The background image. */
	private BufferedImage image;

	/** The animation. */
	private Animation animation;
	
	/**
	 * Instantiates a new background.
	 */
	public Background() {
		BufferedImage[] animationSprites = null;
		try {
			ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
			File input = new File("src/main/resources/backgrounds/rainbow.gif");
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

	/** Draw the background.
	 * 
	 * @param g
	 * 			the graphics to draw;
	 *  */
	public void draw(Graphics2D g) {
		animation.update();
		g.drawImage(animation.getImage(), 0, 60, GamePanel.WIDTH, GamePanel.HEIGHT-60, null);
	}


}
