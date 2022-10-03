package Game2022.game;
import javafx.application.Application;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{
	Socket connSocket;
	common c;
	private BufferedReader dataInputStream;
	
	public ServerThread(Socket connSocket,common c) throws IOException {
		this.connSocket = connSocket;
		this.c=c; // Til Web-server opgaven skal denne ikke anvendes
		dataInputStream = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			String newPlayerName = inFromClient.readLine();
			Player player = GameLogic.makePlayers(newPlayerName);
			player.setDataOut(new DataOutputStream(connSocket.getOutputStream()));
			GameLogic.sendPlayers();
			//return something;
			while (true){
			modtagKoordinater(inFromClient);
			}
			
			// Do the work and the communication with the client here	
			// The following two lines are only an example
		
		} catch (IOException e) {
			e.printStackTrace();
		}		
		// do the work here
	}

	public static void modtagKoordinater(BufferedReader inputReader) throws IOException {
		String koordinater = inputReader.readLine();
		String[] seperatkoordinater = koordinater.split("#");
		System.out.println(koordinater);
		GameLogic.updatePlayer(Integer.parseInt(seperatkoordinater[0]), Integer.parseInt(seperatkoordinater[1]), seperatkoordinater[2]);


	}



}
