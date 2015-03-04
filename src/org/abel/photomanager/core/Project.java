package org.abel.photomanager.core;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import org.abel.photomanager.core.Photo;

/**
 * Project associated to a path to a folder on the hard drive, in charge to
 * handle all the pictures contained in this folder
 * @author Tanguy Abel
 * @version 0.1
 */
public class Project extends Observable implements Observer {

	String path;
	Observer gui;
	Vector<Photo> photos;
	
	/**
	 * Constructor
	 * @param path	The path to a folder containing the pictures to handle
	 * @param gui	The gui associated to this project, responsible for display
	 */
	public Project(String path, Observer gui) {
		this.path = path;
		this.gui = gui;
		

		SwingUtilities.invokeLater(new Runnable() {
	        
            @Override
            public void run() {
            	load();
            }
        });
	}
	
	private static final List<String> supportedFiles = Arrays.asList(
		"jpg",
		"jpeg"
	);
	
	/**
	 * Simple utility method to detect the extension from a filename
	 * @param fileName the name of the file
	 * @return the extension as a String, or an empty string if no extension found
	 */
    private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        else return "";
    }
	
    /**
     * Load a project from its path
     * @return true if loading succeed, false otherwise
     */
	private boolean load() {
		
		File root = new File(path);
		File[] files = root.listFiles();
		
		photos = new Vector<Photo>(files.length);
		
		for (File f : files) {
			if (f.isFile()) {
				String extension = getFileExtension(f.getName());
				if ( supportedFiles.contains(extension) ) {
					photos.addElement(new Photo(f.getAbsolutePath(), this));
//					if (photos.size() >= 30) {
//						break;
//					}
				}
			} else {
				// FIXME: explore subfolders
			}
		}
		
		return true;
	}
	
	/**
	 * Update the project when a photo is loaded
	 * @param photo The loaded photo
	 * @param arg1 Unused argument
	 */
	@Override
	public synchronized void update(Observable photo, Object arg1) {
		// TODO Auto-generated method stub
		gui.update(this, photo);
	}
	
	// using an executor to limit the number of running threads
	private ExecutorService executor = Executors.newFixedThreadPool(30);
	
	/**
	 * Getter for the instance of the executor
	 */
	public ExecutorService getExecutor() {
		return executor;
	}
	
}
