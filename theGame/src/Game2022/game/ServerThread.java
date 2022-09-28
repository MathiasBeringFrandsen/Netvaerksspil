package Game2022.game;
import javafx.application.Application;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{
	Socket connSocket;
	common c;
	private DataOutputStream dataout;
	
	public ServerThread(Socket connSocket,common c) throws IOException {
		this.connSocket = connSocket;
		this.c=c; // Til Web-server opgaven skal denne ikke anvendes
		this.dataout = new DataOutputStream(connSocket.getOutputStream());

	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			String newPlayerName = inFromClient.readLine();
			GameLogic.makePlayers(newPlayerName);

			//return something;
			while (true){
				modtagKoordinater(inFromClient);
				sendPlayers(GameLogic.players, dataout);
			}
			
			// Do the work and the communication with the client here	
			// The following two lines are only an example
		
		} catch (IOException e) {
			e.printStackTrace();
		}		
		// do the work here
	}
	public static void modtagKoordinater(BufferedReader instream) throws IOException {
		String[] movements = instream.readLine().split(" ");
		GameLogic.updatePlayer(Integer.parseInt(movements[0]), Integer.parseInt(movements[1]), movements[2]);
	}
//
//	public void modtagPlayerMoved(int delta_x, int delta_y, String direction) {
//		GameLogic.updatePlayer(delta_x,delta_y,direction);
//		updateScoreTable();
//	}

	public static void sendPlayers(List<Player> players, DataOutputStream outstream) throws IOException {
		String playerString = "";
		for (int i = 0; i< players.size(); i++){
			playerString = playerString + players.get(i).getName()+ " " + players.get(i).getXpos() + " " + players.get(i).getYpos() + " " + players.get(i).getDirection() + "#";
		}
		outstream.writeBytes(playerString + "\n");
		System.out.println(playerString);
	}
}
