package org.team1619;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RobotConnection implements Runnable {

	private boolean keepRunning;
	private final String robotHostName;
	private final int robotControlPort;
	private final MainWindow manWindow;
	
	public RobotConnection(String robotHostName, int robotControlPort, MainWindow manWindow) {
		this.robotHostName = new String(robotHostName);
		this.robotControlPort = robotControlPort;
		this.manWindow = manWindow;
	}
	
	private void sendKeys(OutputStream os) throws IOException {
		os.write(("KEYS " + new String(manWindow.getKeys()) + "\n").getBytes());
		os.flush();
	}
	
	public void run() {
		keepRunning = true;

		while(keepRunning) {
			Socket socket = new Socket();
			try {
				socket.connect(new InetSocketAddress(robotHostName, robotControlPort), 1000);
				OutputStream os = socket.getOutputStream();
				while(true) {
					synchronized(this) {
						this.wait(100);
					}
					sendKeys(os);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					// If socket is still valid, make sure it's closed
					socket.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void stop() {
		keepRunning = false;
		this.notify();
	}
}
