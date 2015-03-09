package org.abel.photomanager.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.abel.photomanager.core.Photo;

/**
 * UI Class responsible for picture display
 * @author Tanguy Abel
 *
 */
public class WorkingPhoto {
	
	private int x;
	private int y;
	private int w;
	private int h;
	private double rotation;
	BufferedImage image;
	
	private Photo photo;
	
	/**
	 * Constructor
	 * @param photo Photo to link with
	 * @param x		Initial x position on the panel
	 * @param y		Initial y position on the panel
	 * @param r		Initial rotation on the panel
	 */
	public WorkingPhoto(Photo photo, int x, int y, int r) {
		this.photo = photo;
		image = photo.getImage();
		this.x  = x;
		this.y = y;
		this.w = photo.getThumbnailWidth();
		this.h = photo.getThumbnailHeight();
		this.rotation = r;
	}

	/**
	 * Draw the image on the panel
	 * @param g2d The Graphics2D from the panel
	 */
	public void drawOn(Graphics2D g2d) {
		
		Graphics2D g = (Graphics2D) g2d.create();
		
		g.translate(x, y);
        g.rotate(Math.toRadians(rotation), image.getWidth() / 2, image.getHeight() / 2);
        g.drawImage(image, 0, 0, w, h, null);
	}

}
