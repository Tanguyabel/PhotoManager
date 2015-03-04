package org.abel.photomanager.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

/**
 * Photo associated to a file on the hard drive, encapsulating all data needed for management
 * @author Tanguy Abel
 *
 */
public class Photo extends Observable {

	/**
	 * Utility method to generate a thumbnail by scaling down the original image 
	 * @param img	The original loaded image
	 * @param size	The size to scale down to
	 * @return		The thumbnail image
	 */
	static private BufferedImage generateThumbnail(BufferedImage img, int size) {
		int width = size;
		int height = size;
		
		if (img.getWidth() >= img.getHeight()) {
			height = (int)((double)img.getHeight() / (double)img.getWidth() * size);
		} else {
			width = (int)((double)img.getWidth() / (double)img.getHeight() * size);
		}
		 
		Image scaledImage = img.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
		BufferedImage thumbnail = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_RGB);
		Graphics g = thumbnail.createGraphics();
		g.drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
		g.dispose();
		
		return thumbnail;
	}
	
	String path;
	//BufferedImage image;
	BufferedImage thumbnail;
	boolean isLoaded = false;
	
	private Observer project;
	
	/**
	 * Constructor
	 * @param path		The path to the picture file on the hard drive
	 * @param project	The project responsible for the management of this picture
	 */
	public Photo(String path, Observer project) {
		this.path = path;
		this.project = project;

		// FIXME: make a call to the executor of the project is poor design
		((Project)project).getExecutor().execute(new Runnable() {
	        
            @Override
            public void run() {
            	load();
            }
            
        });
	}
	
	/**
	 * Load a photo from its associated file
	 */
	public void load() {
		// FIXME: handle complete image
		// FIMXE: handle metadata
		
		try {
			BufferedImage image = ImageIO.read(new File(path));
			thumbnail = generateThumbnail(image, 110);
			
		} catch (IOException e) {
			// handle IO error here
		}
		isLoaded = true;
		project.update(this, null);
	}

	/**
	 * Getter for the thumbnail
	 * @return the thumbnail
	 */
	public BufferedImage getThumbnail() {
		// TODO Auto-generated method stub
		return thumbnail;
	}
}
