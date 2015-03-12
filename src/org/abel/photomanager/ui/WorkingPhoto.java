package org.abel.photomanager.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
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
	private double ratio = .4;
	private boolean focus = false;
	
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
		this.x = x;
		this.y = y;
		this.w = (int)(photo.getDim().getWidth() * ratio);
		this.h = (int)(photo.getDim().getHeight() * ratio);
		this.rotation = r;
	}

	/**
	 * Draw the image on the panel
	 * @param g2d The Graphics2D from the panel
	 */
	public void drawOn(Graphics2D g2d) {
		
		Graphics2D g = (Graphics2D) g2d.create();
		
		w = (int)(photo.getDim().getWidth() * ratio);
		h = (int)(photo.getDim().getHeight() * ratio);
		
		g.translate(x, y);
		g.rotate(Math.toRadians(rotation), 0, 0);
        g.drawImage(image, -w/2, -h/2, w, h, null);

        // debug center
//        g.setColor(Color.GREEN);
//        g.fillRect(-5, -5, 10, 10);
        
        if (focus) {
        	g.setStroke(new BasicStroke(3));
        	g.setColor(Color.CYAN);
        	g.drawRect(-w/2, -h/2, w, h);
        }
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
//		if (focus) {
//			ratio = .6;
//		} else {
//			ratio = .4;
//		}
	}
	
	/**
	 * check if the position (x, y) is include in the boundaries of the pictures
	 * @param x x position to check
	 * @param y y position to check
	 * @return True if (x, y) is included, false otherwise
	 */
	public boolean checkBoundaries(int posX, int posY) {

		double sr = Math.sin(Math.toRadians(rotation));
		double cr = Math.cos(Math.toRadians(rotation));
		
		Point A = new Point((int)(-w/2 * cr + h/2 * sr + x),
				(int)(-w/2 * sr - h/2 * cr + y));
		Point B = new Point((int)(w/2 * cr + h/2 * sr + x),
				(int)(w/2 * sr - h/2 * cr + y));
		Point C = new Point((int)(w/2 * cr - h/2 * sr + x),
				(int)(w/2 * sr + h/2 * cr + y));
		Point D = new Point((int)(-w/2 * cr - h/2 * sr + x),
				(int)(-w/2 * sr + h/2 * cr + y));
		
		double abp = (B.x - A.x) * (posY - A.y) - (B.y - A.y) * (posX - A.x);
		double bcp = (C.x - B.x) * (posY - B.y) - (C.y - B.y) * (posX - B.x);
		double cdp = (D.x - C.x) * (posY - C.y) - (D.y - C.y) * (posX - C.x);
		double dap = (A.x - D.x) * (posY - D.y) - (A.y - D.y) * (posX - D.x);

		if (abp > 0 && bcp > 0 && cdp > 0 && dap > 0) {
			return true;
		}

		return false;
	}
}
