package sem.group47.gamestate;

import java.util.ArrayList;

/**
 * The Class GameStateManager.
 */
public class GameStateManager {

	/** The list of game states. */
	private ArrayList<GameState> gameStates;

	/** The current state. */
	private int currentState;

	/** The Constant MENUSTATE. */
	public static final int MENUSTATE = 0;

	/** The Constant LEVEL1STATE. */
	public static final int LEVEL1STATE = 1;

	/** The Constant LEVEL2STATE. */
	public static final int LEVEL2STATE = 2;

	/** The Constant GAMEOVER. */
	public static final int GAMEOVER = 3;

	/**
	 * Instantiates a new game state manager.
	 */
	public GameStateManager() {

		gameStates = new ArrayList<GameState>();
		currentState = MENUSTATE;

		gameStates.add(new MenuState(this));
		gameStates.add(new Level1State(this));
		gameStates.add(new Level2State(this));
		gameStates.add(new GameOverState(this));

	}

	/**
	 * Sets the current state.
	 *
	 * @param state
	 *            the new state
	 */
	public void setState(int state) {
		currentState = state;
		gameStates.get(currentState).init();
	}

	/**
	 * Updates the current state
	 */
	public void update() {
		gameStates.get(currentState).update();
	}

	/**
	 * Draws the current state
	 *
	 * @param g
	 *            the g
	 */
	public void draw(java.awt.Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}

	/**
	 * Key pressed.
	 *
	 * @param k
	 *            the k
	 */
	public void keyPressed(int k) {
		gameStates.get(currentState).keyPressed(k);
	}

	/**
	 * Key released.
	 *
	 * @param k
	 *            the k
	 */
	public void keyReleased(int k) {
		gameStates.get(currentState).keyReleased(k);
	}

	/*
	 * Returns current gameState
	 * 
	 * @return current gameState
	 */
	public int getCurrentState() {
		return currentState;
	}
}
