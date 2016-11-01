package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.calldesk.common.concurrent.PooledExecutor;
import com.calldesk.common.concurrent.ScheduledExecutor;

public class ServerSocketMain {
	public static void main(String[] args) {
		System.out.println("Starting ServerSocket");
		
		ServerSocket server = null;
		try {
			server = new ServerSocket(9875);
		} catch (IOException e) {
			e.printStackTrace();
		}

		PooledExecutor.setup(100, 10);

		ScheduledExecutor.setup(50);

		while (!Thread.interrupted()) {
			try {
				Socket client = server.accept();
				System.out.println("Client was accepted at " + client.getInetAddress());
				
				Thread thread1 = new Thread(new Sender(client));
				Thread thread2 = new Thread(new Receiver(client));
				
				thread1.start();
				thread2.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
