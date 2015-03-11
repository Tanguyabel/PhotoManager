package org.abel.photomanager.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Photo associated to a file on the hard drive, encapsulating all data needed for management
 * @author Tanguy Abel
 *
 */
public class Photo extends Observable {
	
	static int commonSize = 400;	// fixed size for images
	//static int thumbnailRatio = 10; // in percentage of the image
	
	String path;
	//BufferedImage image;
	BufferedImage image;
	boolean isLoaded = false;
	Dimension dim;
	
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
	 * Down scale the dimensions of the image and conserve the original ratio 
	 * @param imgW	The original width
	 * @param imgH	The original height
	 * @param size	The size to scale down to
	 */
	private void computeDimensions(int imgW, int imgH, int size) {
		int w = size;
		int h = size;
		
		if (imgW >= imgH) {
			h = (int)((double)imgH / (double)imgW * size);
		} else {
			w = (int)((double)imgW / (double)imgH * size);
		}

		dim = new Dimension(w, h);
	}
	
	/**
	 * Load a photo from its associated file
	 * @return True if succeed, false otherwise
	 */
	public boolean load() {
		// FIXME: handle complete image
		// FIMXE: handle metadata
		
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(new FileInputStream(path));
			
			Iterator<ImageReader> it = ImageIO.getImageReaders(iis);
			if (!it.hasNext()) {
				System.out.println("No reader found");
				return false;
			}
			
			ImageReader reader = (ImageReader)it.next();
			//System.out.println("using reader " + reader.getFormatName());
			reader.setInput(iis, true, false);
			ImageReadParam param = reader.getDefaultReadParam();
			
			computeDimensions(reader.getWidth(0), reader.getHeight(0), commonSize);
			
			int samplingX = (int) (reader.getWidth(0) / dim.getWidth());
			int samplingY = (int) (reader.getHeight(0) / dim.getHeight());
			param.setSourceSubsampling(samplingX, samplingY, 0, 0);
			image = reader.read(0, param);
			
		} catch (IOException e) {
			// handle IO error here
		}
		isLoaded = true;
		project.update(this, null);
		return true;
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
	 * Getter for the dimension
	 * @return the dimension
	 */
	public Dimension getDim() {
		return dim;
	}
}
