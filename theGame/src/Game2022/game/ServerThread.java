package Game2022.game;
import javafx.application.Application;
import javafx.util.Pair;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{
	Socket connSocket;
	common c;
	Player player;
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
			player = GameLogic.makePlayers(newPlayerName);
			player.setDataOut(new DataOutputStream(connSocket.getOutputStream()));
			GameLogic.sendPlayers();
			GameLogic.sendChest();
			//return something;
			while (true){
				String line = inFromClient.readLine();

				if (!line.equals("projectile")){
					modtagKoordinater(line);
				}
				else{
					recieveProjectiles();
				}
			}
			
			// Do the work and the communication with the client here	
			// The following two lines are only an example
		
		} catch (IOException e) {
			e.printStackTrace();
		}		
		// do the work here
	}

	public void modtagKoordinater(String line) throws IOException {
		String koordinater = line;
		String[] seperatkoordinater = koordinater.split("#");
		System.out.println(koordinater);
		GameLogic.updatePlayer(Integer.parseInt(seperatkoordinater[0]), Integer.parseInt(seperatkoordinater[1]), seperatkoordinater[2], player);
	}

	public void recieveProjectiles() throws IOException {
		System.out.println("Recieved");
		int xPos = 0;
		int yPos = 0;
		switch (player.getDirection()) {
			case "up":  xPos = player.getXpos(); yPos = player.getYpos()-1; break;
			case "down":  xPos = player.getXpos(); yPos = player.getYpos()+1; break;
			case "left":  xPos = player.getXpos()-1; yPos = player.getYpos(); break;
			case "right":  xPos = player.getXpos()+1; yPos = player.getYpos(); break;
			default: break;
		}
		if (Generel.board[yPos].charAt(xPos) == ' '){
			Projectile projectile = new Projectile(new pair(xPos, yPos), player.getDirection());
			GameLogic.sendProjectileToClient(projectile);
		}
	}



}
