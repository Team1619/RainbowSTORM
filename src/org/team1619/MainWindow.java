package org.team1619;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;

import javax.swing.JFrame;

public class MainWindow {

	private JFrame frame;
	private HashSet<Integer> keysPressed = new HashSet<Integer>();

	final private RobotConnection rc;

	public MainWindow() {
		frame = new JFrame("RainbowSTORM");
		frame.setBounds(100, 100, 450, 300);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if(keysPressed.contains(keyCode)) {
					keysPressed.remove(keyCode);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch(keyCode) {
				case KeyEvent.VK_UNDEFINED:
				case KeyEvent.VK_CAPS_LOCK:
					break;
				default:
					keysPressed.add(keyCode);
				}
			}
		});
		
		
		//rc = new RobotConnection("roboRIO-1619.local", RobotConnection.kRobotControlPort, this);
		rc = new RobotConnection("localhost", RobotConnection.kRobotControlPort, this);
		new Thread(rc).start();

		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e) {
				rc.stop();
			}

			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}

	public void show() {
		frame.setVisible(true);
	}

	public int[] getKeys() {
		int[] arr = new int[keysPressed.size()];

		int i = 0;
		for(Integer integer : keysPressed) {
			arr[i++] = integer;
		}

		return arr;
	}
}
