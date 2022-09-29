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
			GameLogic.addOutputStream(new DataOutputStream(connSocket.getOutputStream()));
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

	public static void modtagKoordinater(int delta_x, int delta_y, String direction, BufferedReader inputReader) throws IOException {
		String koordinater = inputReader.readLine();
		String[] seperatkoordinater = koordinater.split(" ");
		GameLogic.updatePlayer(Integer.parseInt(seperatkoordinater[0]), Integer.parseInt(seperatkoordinater[1]), seperatkoordinater[3]);


	}



}
