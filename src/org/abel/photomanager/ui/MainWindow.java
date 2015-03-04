package org.abel.photomanager.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;

import org.abel.photomanager.core.*;

/**
 * Main window of the application
 * @author Tanguy Abel
 * @version 0.1
 */
public class MainWindow extends JFrame {

	private Project activeProject;
	
	/**
	 * Constructor
	 */
	public MainWindow() {
		initGUI();
		createWelcomePanel();
	}
	
	/**
	 * Initialize the interface and create the welcome panel
	 */
	public void initGUI() {
	    this.setTitle("PhotoManager v0.1");
	    this.setSize(500, 500);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Initialize the welcome panel, offering the user to start a new project or 
	 * open an existing one
	 */
	public void createWelcomePanel() {
		JPanel panel = new JPanel();
		GridLayout layout = new GridLayout(1, 2);
		layout.setHgap(5);
		layout.setVgap(10);
		panel.setLayout(layout);
	
		JButton btnNewProject = new JButton("New Project");
		JButton btnOpenProject = new JButton("Open Project");
		btnOpenProject.setEnabled(false);
		
		panel.add(btnNewProject);
		panel.add(btnOpenProject);
		panel.setBorder(BorderFactory.createEmptyBorder(100, 10, 100, 10));
		
		/*
		 * FIXME this interface is a bit poor; use a JFileChooser instead
		 */
		btnNewProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				String path = JOptionPane.showInputDialog(panel,
						"Select a path to pictures folder",
						"New project",
						JOptionPane.PLAIN_MESSAGE);
				if (new File(path).isDirectory() == true) {
					createProject(path);
				}
			}
		});
		
		this.setContentPane(panel);
	}
	
	/**
	 * Create a new project 
	 * @param path The path to the pictures folder for the project
	 */
	protected void createProject(String path) {
		WorkingPanel panel = new WorkingPanel();
		
		this.setContentPane(panel);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		activeProject = new Project(path, panel);
	}
	
	/**
	 * Main
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
        
            @Override
            public void run() {
            	MainWindow window = new MainWindow();
            	window.setVisible(true);
            }
        });
	}
}
