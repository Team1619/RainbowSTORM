package org.team1619;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

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
					ServerSocket serverSocket = new ServerSocket(1619);
					while(true) {
						Socket socket = serverSocket.accept();
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String line;
						while((line = br.readLine()) != null) {
							System.out.println(line);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	
}
