package main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

import com.google.gson.JsonObject;

public class Receiver implements Runnable {
	private Socket client;

	public Receiver(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		String message = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (!Thread.interrupted()) {
				message = in.readLine();
				System.out.println(message);
				Map<String, String> m = parseMessage(message);
				System.out.println(m.keySet());
				String action = m.getOrDefault("Action", "Nothing");
				System.out.println(action);
				if ("PlaySound".equals(action)) {
					playSound(m);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> parseMessage(String rawMessage) {

		rawMessage = rawMessage.replace("}", "");
		rawMessage = rawMessage.replace("{", "");

		String[] fields = rawMessage.split(",");
		Map<String, String> message = new HashMap<>();

		for (String f : fields) {
			String[] keyValue = f.split(":");

			if (keyValue.length != 2)
				continue;

			message.put(keyValue[0].trim().replace("\"", ""), keyValue[1].trim().replace("\"", ""));
		}

		return message;

	}

	public void playSound(Map<String, String> m) {

		try {

			Clip clip = AudioSystem.getClip();

			File file = new File(m.get("path"));
			
			FileInputStream audio = new FileInputStream(file);
			InputStream stream = new BufferedInputStream(audio);
			
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(stream);
			
			clip.open(inputStream);
			clip.start();

			while(clip.getMicrosecondLength() != clip.getMicrosecondPosition())
			{
			}
			
			m.put("event", "playSoundEnded");

			sendMessage(convertToJson(m));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String convertToJson(Map<String, String> map) {
		int size = map.size();

		if (size < 1)
			return "{}" + "\n";

		JsonObject obj = new JsonObject();
		for (Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey() == null || entry.getValue() == null)
				continue;

			obj.addProperty(entry.getKey(), entry.getValue());
		}

		return obj.toString() + "\n";
	}

	private void sendMessage(String message) {
		Sender s = new Sender(client);
		PrintStream sendMessage = s.sendMessage();
		try {
			System.out.println("Sending message to the client");
			sendMessage.write(message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
