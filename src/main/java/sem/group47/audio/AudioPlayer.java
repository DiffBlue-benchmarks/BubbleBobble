package sem.group47.audio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * AudioPlayer.
 * 
 * @author Bas
 *
 */
public class AudioPlayer {

	/** The clips. */
	private static HashMap<String, Clip> clips;

	/** The gap. */
	private static int gap;

	/** The mute. */
	private static boolean mute = false;

	/**
	 * Inits the.
	 */
	public static void init() {
		clips = new HashMap<String, Clip>();
		gap = 0;
	}

	/**
	 * Load.
	 *
	 * @param s
	 *            the s
	 * @param n
	 *            the n
	 */
	public static void load(final String s, final String n) {
		if (clips.get(n) != null) {
			return;
		}
		Clip clip;
		try {
			AudioInputStream ais = AudioSystem
					.getAudioInputStream(AudioPlayer.class
							.getResourceAsStream(s));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			AudioInputStream dais = AudioSystem.getAudioInputStream(
					decodeFormat, ais);
			DataLine.Info info = new DataLine.Info(Clip.class, decodeFormat);
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(dais);
			clips.put(n, clip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play.
	 *
	 * @param s
	 *            the s
	 */
	public static void play(final String s) {
		play(s, gap);
	}

	/**
	 * Play.
	 *
	 * @param s
	 *            the s
	 * @param i
	 *            the i
	 */
	public static void play(final String s, final int i) {
		if (mute) {
			return;
		}
		Clip c = clips.get(s);
		if (c == null) {
			return;
		}
		if (c.isRunning()) {
			c.stop();
		}
		c.setFramePosition(i);
		while (!c.isRunning()) {
			c.start();
		}
	}

	/**
	 * Stop.
	 *
	 * @param s
	 *            the s
	 */
	public static void stop(final String s) {
		if (clips.get(s) == null) {
			return;
		}
		if (clips.get(s).isRunning()) {
			clips.get(s).stop();
		}
	}

	/**
	 * Resume.
	 *
	 * @param s
	 *            the s
	 */
	public static void resume(final String s) {
		if (mute) {
			return;
		}
		if (clips.get(s).isRunning()) {
			return;
		}
		clips.get(s).start();

	}

	/**
	 * Resume.
	 *
	 * @param s
	 *            the s
	 */
	public static void resumeLoop(final String s) {
		if (mute) {
			return;
		}
		if (clips.get(s).isRunning()) {
			return;
		}
		loop(s);

	}

	/**
	 * Loop.
	 *
	 * @param s
	 *            the s
	 */
	public static void loop(final String s) {
		loop(s, gap, gap, clips.get(s).getFrameLength() - 1);
	}

	/**
	 * Loop.
	 *
	 * @param s
	 *            the s
	 * @param frame
	 *            the frame
	 */
	public static void loop(final String s, final int frame) {
		loop(s, frame, gap, clips.get(s).getFrameLength() - 1);
	}

	/**
	 * Loop.
	 *
	 * @param s
	 *            the s
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	public static void loop(final String s, final int start, final int end) {
		loop(s, gap, start, end);
	}

	/**
	 * Loop.
	 *
	 * @param s
	 *            the s
	 * @param frame
	 *            the frame
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	public static void loop(final String s, final int frame, final int start,
			final int end) {
	 try {
 		stop(s);
 		if (mute) {
 			return;
 		}
 		clips.get(s).setLoopPoints(start, end);
 		clips.get(s).setFramePosition(frame);
 		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
	 }
	 catch (Exception e) {
	  e.printStackTrace();
	 }
	}

	/**
	 * Sets the position.
	 *
	 * @param s
	 *            the s
	 * @param frame
	 *            the frame
	 */
	public static void setPosition(final String s, final int frame) {
	 try {
	  clips.get(s).setFramePosition(frame);
	 }
	 catch (Exception e) {
	  
	 }
	}

	/**
	 * Gets the frames.
	 *
	 * @param s
	 *            the s
	 * @return the frames
	 */
	public static int getFrames(final String s) {
	 try {
	  return clips.get(s).getFrameLength();
	 }
	 catch(Exception e) {
	  return 0;
	 }
	}

	/**
	 * Gets the position.
	 *
	 * @param s
	 *            the s
	 * @return the position
	 */
	public static int getPosition(final String s) {
	 try {
	  return clips.get(s).getFramePosition();
	 }
	 catch (Exception e) {
	  return 0;
	 }
	}

	/**
	 * Close.
	 *
	 * @param s
	 *            the s
	 */
	public static void close(final String s) {
	 try {
	  stop(s);
		 clips.get(s).close();
	 }
	 catch (Exception e) {
	  
	 }
	}

	/**
	 * Stops all music clips.
	 */
	public static void stopAll() {
	 try {
 	 Iterator<Entry<String, Clip>> it = clips.entrySet().iterator();
 	 while (it.hasNext()) {
 	  Clip clip = it.next().getValue();
 	  if (clip != null && clip.isRunning()) {
 	   clip.stop();
 	  }
 	 }
	 }
	 catch (Exception e) {
	  
	 }
	}
}
