package sem.group47.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import sem.group47.audio.AudioPlayer;
import sem.group47.main.GamePanel;

/**
 * The Class GameOverState, which extends the super class GameState.
 */

@SuppressWarnings("PMD")

public class GameOverState extends GameState {

	/** The current choice. */
	private int currentChoice = 0;

	/** The options. */
	private String[] options = {"Restart", "Main Menu" };

	/** The Background. */
	private String bg = "/backgrounds/BubbleBobble_Logo.gif";

	/** The image. */
	private BufferedImage image;

	/**
	 * Instantiates a new menu state.
	 *
	 * @param gsm
	 *            the gamestatemanager
	 */
	public GameOverState(final GameStateManager gsm) {
		try {
			AudioPlayer.stopAll();
			AudioPlayer.loop("gameover");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setGsm(gsm);

		try {
			image = ImageIO.read(getClass().getResourceAsStream(bg));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Draws everything of the menu screen.
	 */
	@Override
	public final void draw(final Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		int x = (GamePanel.WIDTH - image.getWidth(null)) / 2;
		int y = (GamePanel.HEIGHT - image.getHeight(null)) / 5;
		g.drawImage(image, x, y, null);

		g.setFont(font);
		g.setColor(Color.RED);
		g.drawString("GAME OVER", 320, 420);

		g.setFont(font);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.WHITE);
			}

			g.drawString(options[i], 320, 500 + i * 50);
		}

	}

	/**
	 * Select between different menu options.
	 */
	private void select() {
		if (currentChoice == 0) {
			getGsm().setState(GameStateManager.LEVELSTATE);
			AudioPlayer.stop("gameover");
		}
		if (currentChoice == 1) {
			getGsm().setState(GameStateManager.MENUSTATE);
			AudioPlayer.stop("gameover");
		}
	}

	/**
	 * Lets you scroll through menu options with up and down keys.
	 */
	@Override
	public final void keyPressed(final int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
}
