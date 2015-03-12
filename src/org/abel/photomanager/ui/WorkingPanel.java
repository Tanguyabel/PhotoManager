package org.abel.photomanager.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.abel.photomanager.core.Photo;

/**
 * Panel aimed to display pictures on and interact with them
 * @author Tanguy Abel
 *
 */
public class WorkingPanel extends JPanel implements Observer, MouseMotionListener {
	
	private int translateX = 0;
	private int translateY = 0;
	
	private List<WorkingPhoto> photos = new ArrayList<WorkingPhoto>();
	
	private Random rand = new Random();
	
	/**
	 * Generate a random integer
	 * @param min	Lower boundary for the range
	 * @param max	Upper boundary for the range
	 * @return		A random integer in the range defined by min an max
	 */
	private int randInt(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}
	
	
	/**
	 * Constructor
	 */
	public WorkingPanel() {
		addMouseMotionListener(this);
	}

	/**
	 * Update the panel when a new picture is loaded from a project
	 * @param project	The project invoking the update
	 * @param photo		The photo to add for display
	 */
	@Override
	public synchronized void update(Observable project, Object photo) {
		
		photos.add(new WorkingPhoto((Photo)photo,
				randInt(100, getWidth() - 100),
				randInt(100, getHeight() - 100),
				randInt(-37, 37)
		));
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				repaint();
			}
		});
	}
	
	/**
	 * Repaint the panel, displaying all pictures available
	 * @param g The Graphics for painting, used as a Graphics2D
	 */
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.clearRect(0, 0, getWidth(), getHeight());
       
        for (WorkingPhoto p : photos) {
        		p.drawOn(g2d);
        }
        
        g2d.dispose();
    } 
    
    @Override
    public void mouseMoved(MouseEvent e) {
    	
    	WorkingPhoto focusedPhoto = null;
    	
        for (WorkingPhoto p : photos) {
        	p.setFocus(false);
    		if (p.checkBoundaries(e.getX(), e.getY())) {
    			focusedPhoto = p;
    		}
        }
        
        if (focusedPhoto != null) {
        	focusedPhoto.setFocus(true);        	
        }

        repaint();
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
