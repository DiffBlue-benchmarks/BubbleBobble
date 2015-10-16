package sem.group47.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import sem.group47.audio.AudioPlayer;
import sem.group47.entity.PlayerSave;
import sem.group47.main.GamePanel;

/**
 * The Class MenuState, which extends the super class GameState.
 */
public class MenuState extends GameState {

	/** The current choice. */
	private int currentChoice = 0;

	/** The options. */
	private String[] options = {"Start", "2 Player Mode", "Help",
			"Options", "Quit" };

	/** The font. */
	private Font font;

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
	public MenuState(final GameStateManager gsm) {

		setGsm(gsm);

		try {
			image = ImageIO.read(getClass().getResourceAsStream(bg));
			font = new Font("Arial", Font.PLAIN, 30);
			AudioPlayer.resumeLoop("menu");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Init.
	 */
	@Override
	public void init() {
	}

	/**
	 * Update.
	 */
	@Override
	public void update() {
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
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.white);
			}

			g.drawString(options[i], 370, 380 + i * 50);
		}

	}

	/**
	 * Select between different menu options.
	 */
	private void select() {
		if (currentChoice == 0) {
			PlayerSave.setMultiplayerEnabled(false);
			getGsm().setState(GameStateManager.LEVELSTATE);
			AudioPlayer.stop("menu");
		}
		if (currentChoice == 1) {
			PlayerSave.setMultiplayerEnabled(true);
			getGsm().setState(GameStateManager.LEVELSTATE);
			AudioPlayer.stop("menu");
		}
		if (currentChoice == 2) {
			getGsm().setState(GameStateManager.HELPSTATE);
		}

		if (currentChoice == 3) {
			getGsm().setState(GameStateManager.OPTIONSSTATE);
		}
		if (currentChoice == 4) {
			System.exit(0);
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
		if (k == KeyEvent.VK_M) {
			if (AudioPlayer.isMute() == false) {
				AudioPlayer.setMute(true);
				System.out.println("hiero");
			} else {
				AudioPlayer.setMute(false);
				System.out.println("daaro");
			}
		}

	}

	/**
	 * keyReleased.
	 */
	@Override
	public final void keyReleased(final int k) {
	}

}
