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
	private void generateThumbnail(int size) {
		thumbnailW = size;
		thumbnailH = size;
		
		if (image.getWidth() >= image.getHeight()) {
			thumbnailH = (int)((double)image.getHeight() / (double)image.getWidth() * size);
		} else {
			thumbnailW = (int)((double)image.getWidth() / (double)image.getHeight() * size);
		}

		/*
		Image scaledImage = img.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
		BufferedImage thumbnail = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_RGB);
		Graphics g = thumbnail.createGraphics();
		g.drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
		g.dispose();
		
		*/
	}
	
	String path;
	//BufferedImage image;
	BufferedImage image;
	boolean isLoaded = false;
	int thumbnailW, thumbnailH;
	
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
			image = ImageIO.read(new File(path));
			generateThumbnail(110);
			
		} catch (IOException e) {
			// handle IO error here
		}
		isLoaded = true;
		project.update(this, null);
	}

	/**
	 * Getter for the image
	 * @return the image
	 */
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return image;
	}
	
	/**
	 * Getter for the thumbnail's dimension
	 * @return the image
	 */
	public int getThumbnailWidth() {
		return thumbnailW;
	}
	
	/**
	 * Getter for the thumbnail's dimension
	 * @return the image
	 */
	public int getThumbnailHeight() {
		return thumbnailH;
	}
}
