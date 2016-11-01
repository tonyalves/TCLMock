package main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.JsonObject;

public class Sender implements Runnable {
	private Socket client;
	private Scanner sc;
	public Sender(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		PrintStream stream = sendMessage();
		
		String channelIndex = "";
		while(!Thread.interrupted()){
			System.out.println("Send an event: ");
			sc = new Scanner(System.in);
			String option = sc.nextLine();
			
			JsonObject json = new JsonObject();
			switch (option) {
			case "0":
				json.addProperty("event", "incomingCall");
				int device2 = new Random().nextInt(3);
				int channel2 = new Random().nextInt(10);
				json.addProperty("channelId", device2 + "."+ channel2);
				json.addProperty("destinationAddress", 5555);
				json.addProperty("originationAddress", new Random().nextInt(999999999));
				json.addProperty("result", "success");
				try {
					System.out.println("Sending event: "+json.toString());
					stream.write((json.toString()+"\n").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				channelIndex = device2 + "."+ channel2;
				break;
			case "1":
				json.addProperty("event", "callConnect");
				int device = new Random().nextInt(3);
				int channel = new Random().nextInt(10);
				json.addProperty("channelId", channel);
				json.addProperty("destinationAddress", 7000);
				json.addProperty("originationAddress", 7000);
				json.addProperty("result", "success");
				try {
					System.out.println("Sending event: "+json.toString());
					stream.write((json.toString()+"\n").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				channelIndex = device + "."+ channel;
				break;
			
			case "11":
				json.addProperty("event", "callConnect");
				int device222 = new Random().nextInt(3);
				int channel222 = new Random().nextInt(10);
				json.addProperty("channelId", channel222);
				json.addProperty("destinationAddress", 8000);
				json.addProperty("originationAddress", 8000);
				json.addProperty("result", "success");
				try {
					System.out.println("Sending event: "+json.toString());
					stream.write((json.toString()+"\n").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				channelIndex = device222 + "."+ channel222;
				break;	
			case "111":
				json.addProperty("event", "callConnect");
				int device22 = new Random().nextInt(3);
				int channel22 = new Random().nextInt(10);
				json.addProperty("channelId", channel22);
				json.addProperty("destinationAddress", 9000);
				json.addProperty("originationAddress", 9000);
				json.addProperty("result", "success");
				try {
					System.out.println("Sending event: "+json.toString());
					stream.write((json.toString()+"\n").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				channelIndex = device22 + "."+ channel22;
				break;	
			case "2":
				json = new JsonObject();
				
				json.addProperty("Event", "dtmfReceived");
				json.addProperty("channelIndex", channelIndex);
				try {
					System.out.println("Sending event: "+json.toString());
					stream.write((json.toString()+"\n").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			case "3":
				json = new JsonObject();
				
				json.addProperty("Event", "callHangup");
				json.addProperty("channelIndex", channelIndex);
				
				try {
					System.out.println("Sending event: "+json.toString());
					stream.write((json.toString()+"\n").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "4":
				json = new JsonObject();
				
				json.addProperty("Event", "dtmfDetected");
				json.addProperty("channelIndex", channelIndex);
				System.out.println("Enter with a digit [0-9]: ");
				String digit = sc.nextLine();
				json.addProperty("digit", digit);
				try {
					System.out.println("Sending event: "+json.toString());
					stream.write((json.toString()+"\n").getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}	
			default:
				break;
			}
		}
	}

	public PrintStream sendMessage() {
		System.out.println("Starting sending events to the client");
		PrintStream stream = null;
		try {
			stream = new PrintStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
}
