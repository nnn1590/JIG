package jig;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class ResourceManager {
	private static final HashMap<String, Image> images = new HashMap<String, Image>();
	private static final HashMap<String, Sound> sounds = new HashMap<String, Sound>();

	private static URL findResource(final String rscName) {
		URL u = null;

		// Try first to get it from the actual path specified...
		u = ClassLoader.getSystemResource(rscName);

		if (u == null) {	
			System.err.println("Didn't find the resource on at the location " + rscName + "...preparing to hunt...");
			for (StackTraceElement ste : new Throwable().getStackTrace()) {
				if (!ste.getClassName().startsWith("jig") && !ste.getClassName().startsWith("org.newdawn.slick")) {
					try {
						Class<?> callingObjClass = Class.forName(ste.getClassName());
						u = callingObjClass.getResource(rscName);
						System.err.println("  - searching from location of class: "
								+ ste.getClassName());

					} catch (ClassNotFoundException cnfe) {
						continue;
					}

					if (u != null) {
						System.err.println(" - Found resource '" + rscName
								+ "' by inferring path. This is fragile;"
								+ " use a fully qualified path for your release.");
						return u;
					}

					// TODO: consider popping up the package stack...


				}
			}
		}
		System.err.println("  - Out of guesses.  Check your resource location and name. ("+rscName+")");
		return null;

	}

	/**
	 * Loads an image from the hard drive (via URL so that it will work from 
	 * within a JAR). If this is the first time this image has been loaded, it 
	 * will be cached for next time so that the same image data doesn't have to 
	 * be loaded every time the user calls for it.
	 * 
	 * @param path The file path to the resource
	 * @throws SlickException 
	 */
	public static void loadImage(final String rscName) {

		URL u = findResource(rscName);
		try {
			images.put(rscName, new Image(u.openStream(), rscName, false));
		} catch (Exception e) {

			System.err.println("Failed to load the resource found by the spec " + rscName);
			e.printStackTrace();
		}
	}

	/**
	 * Loads an image from the hard drive (via URL so that it will work from 
	 * within a JAR). If this is the first time this image has been loaded, it 
	 * will be cached for next time so that the same image data doesn't have to 
	 * be loaded every time the user calls for it.
	 * 
	 * @param path The file path to the resource (relative to the src folder)
	 * @return The image data requested from the file path provided.
	 * @throws SlickException 
	 */
	public static Image getImage(final String path) {
		if (images.get(path) == null) {
			System.out.println("Warning: Image '" + path + "' was requested that wasn't previously loaded. Use loadImage(path) before calling getImage(path) to avoid runtime lag.");
			loadImage(path);
		}
		return images.get(path);
	}

	/**
	 * Creats a SpriteSheet from the hard drive (via URL so that it will work from 
	 * within a JAR). If this is the first time the image for this sprite sheet has been loaded, it 
	 * will be cached for next time so that the same image data doesn't have to 
	 * be loaded every time the user calls for it.
	 * 
	 * @param rscName The file path to the resource (relative to the src folder)
	 * @param tx The x width of the sprites in the sprite sheet
	 * @param ty The y width of the sprites in the sprite sheet
	 * @return The sprite sheet data requested from the file path provided.
	 * @throws SlickException 
	 */
	public static SpriteSheet getSpriteSheet(final String rscName, final int tx, final int ty) {
		if (images.get(rscName) == null) {
			System.out.println("Warning: Image '" + rscName + "' was requested that wasn't previously loaded. Use loadImage(path) before calling getImage(path) to avoid runtime lag.");
			loadImage(rscName);
		}
		return new SpriteSheet(images.get(rscName), tx, ty);
	}

	/**
	 * Removes all cached images. Every time an image is loaded, it is saved here, and if many 
	 * images are loaded at one time they may start to take up quite a bit of space. Calling 
	 * this should help with that.
	 */
	public static void clearImageCache() {
		images.clear();
	}

	/**
	 * Loads an sound file from the hard drive (via URL so that it will work from 
	 * within a JAR). If this is the first time this image has been loaded, it 
	 * will be cached for next time so that the same image data doesn't have to 
	 * be loaded every time the user calls for it.
	 * <br>
	 * Note: Slick implements sound as two separate classes, Sound and Music. We should 
	 * find a happy medium between those two.
	 * 
	 * @param rscName The file path to the resource (relative to the src folder)
	 * @throws SlickException 
	 */
	public static void loadSound(final String rscName) {

		URL u = findResource(rscName);
		try {
			sounds.put(rscName, new Sound(u.openStream(), rscName));
		} catch (Exception e) {

			System.err.println("Failed to load the resource found by the spec " + rscName);
			e.printStackTrace();
		}
	}

	/**
	 * Loads an sound file from the hard drive (via URL so that it will work from 
	 * within a JAR). If this is the first time this image has been loaded, it 
	 * will be cached for next time so that the same image data doesn't have to 
	 * be loaded every time the user calls for it.
	 * <br>
	 * Note: Slick implements sound as two separate classes, Sound and Music. We should 
	 * find a happy medium between those two.
	 * 
	 * @param rscName The file path to the resource (relative to the src folder)
	 * @return The audio data requested from the file path provided.
	 * @throws SlickException 
	 */
	public static Sound getSound(final String rscName) {
		if(sounds.get(rscName) == null) {
			System.out.println("Warning: Sound '" + rscName + "' was requested that wasn't previously loaded. Use loadSound(path) before calling getSound(path) to avoid runtime lag.");
			loadSound(rscName);
		}

		return sounds.get(rscName);
	}

	/**
	 * Removes all cached sounds. Every time an sound is loaded, it is saved here, and if many 
	 * sounds are loaded at one time they may start to take up quite a bit of space. Calling 
	 * this should help with that.
	 */
	public static void clearSoundCache() {
		sounds.clear();
	}
}
