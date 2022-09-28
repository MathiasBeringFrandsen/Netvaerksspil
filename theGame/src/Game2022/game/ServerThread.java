package Game2022.game;
import javafx.application.Application;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ServerThread extends Thread{
	Socket connSocket;
	common c;
	
	public ServerThread(Socket connSocket,common c) {
		this.connSocket = connSocket;
		this.c=c; // Til Web-server opgaven skal denne ikke anvendes
	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			String newPlayerName = inFromClient.readLine();
			GameLogic.makePlayers(newPlayerName);
			//return something;
			while (true){

			}
			
			// Do the work and the communication with the client here	
			// The following two lines are only an example
		
		} catch (IOException e) {
			e.printStackTrace();
		}		
		// do the work here
	}
	public static ArrayList<Person> modtagPersoner(ArrayList<Person> personer, BufferedReader instream) throws IOException {
		String[] personArray = instream.readLine().split("#");
		for (int i = 0; i < personArray.length; i++) {
			String[] thisPerson = personArray[i].split(" ");
			personer.add(new Person(Integer.parseInt(thisPerson[0]), thisPerson[1], thisPerson[2]));
		}
		return personer;
	}
}
