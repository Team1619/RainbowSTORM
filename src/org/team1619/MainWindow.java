package org.team1619;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;

import javax.swing.JFrame;

public class MainWindow {

	private JFrame frame;
	private HashSet<Character> keysPressed = new HashSet<Character>();

	final private RobotConnection rc;

	public MainWindow() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				String s = KeyEvent.getKeyText(e.getKeyCode());
				char c = 0;
				if(s.length() == 1) {
					c = s.charAt(0);
					if(c < 0 || c > 127)
						c = 0;
					if('a' <= c && c <= 'z')
						c = (char)(c & ~32);
				}
				
				switch(e.getID()) {
				case KeyEvent.KEY_PRESSED:
					switch(c) {
					case '\r':
					case '\n':
					case '\t':
					case '\b':
					case ' ':
					case 0:
						break;
					default:
						keysPressed.add(c);
						break;
					}
					break;
				case KeyEvent.KEY_RELEASED:
					if(keysPressed.contains(c)) {
						keysPressed.remove(c);
					}
					break;
				}
				return false;
			}
		});

		//rc = new RobotConnection("roboRIO-1619.local", 1619, this);
		rc = new RobotConnection("localhost", 1619, this);
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

	public char[] getKeys() {
		char[] arr = new char[keysPressed.size()];

		int i = 0;
		for(Character c : keysPressed) {
			arr[i++] = c;
		}

		return arr;
	}
}
