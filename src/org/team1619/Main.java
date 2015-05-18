package org.team1619;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void handleKeysCommand(String data) {
		int[] keyCodes = parseKeys(data);
		for(int key : keyCodes) {
			System.out.println(key);
		}
	}
	
	static public int[] parseKeys(String data) {
		try {
			String[] keys = data.split(" ");
			int[] keyCodes = new int[keys.length];
			for(int i = 0; i < keys.length; i++) {
				keyCodes[i] = Integer.parseInt(keys[i]);
			}
			return keyCodes;
		}
		catch (Exception e) {
			return new int[0];
		}
	}

	public static void handleCommand(String buffer) {
		String[] split = buffer.split(" ", 2);
		if(split.length < 1)
			return;
		
		String command = split[0];
		String data = split.length == 1 ? "" : split[1];
		switch(command) {
		case "KEYS":
			System.out.println("Got KEYS command");
			handleKeysCommand(data);
			break;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		new Thread(new Runnable() {
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(RobotConnection.kRobotControlPort);
					while(true) {
						Socket socket = serverSocket.accept();
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String line;
						while((line = br.readLine()) != null) {
							System.out.println(line);
							handleCommand(line);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}