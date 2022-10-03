package Game2022.game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GameLogic {
public static List<Player> players = new ArrayList<>();
	public static Player me;
	public static DataOutputStream outputStream;



	public static void setPlayerList(ArrayList<Player> newPlayerList){
		for (Player player : players){
			Gui.removePlayerOnScreen(player.getLocation());
		}
		players = newPlayerList;
		System.out.println(players.size());
		for (Player player : players) {
			Gui.placePlayerOnScreen(player.location, player.direction);
		}
	}

	public static void setOutputStream(DataOutputStream out){
		outputStream = out;
	}
	
	public static Player makePlayers(String name) {
		pair p=getRandomFreePosition();
		me = new Player(name,p,"up");
		players.add(me);
		return me;
	}
	
	public static pair getRandomFreePosition()
	// finds a random new position which is not wall 
	// and not occupied by other players 
	{
		int x = 1;
		int y = 1;
		boolean foundfreepos = false;
		while  (!foundfreepos) {
			Random r = new Random();
			x = Math.abs(r.nextInt()%18) +1;
			y = Math.abs(r.nextInt()%18) +1;
			if (Generel.board[y].charAt(x)==' ') // er det gulv ?
			{
				foundfreepos = true;
				for (Player p: players) {
					if (p.getXpos()==x && p.getYpos()==y) //pladsen optaget af en anden 
						foundfreepos = false;
				}
				
			}
		}
		pair p = new pair(x,y);
		return p;
	}



	public static void sendKoordinater(int delta_x, int delta_y, String direction){
		try {
			String koordinater;
			koordinater = delta_x + " " + delta_y + " " + direction;
			outputStream.writeBytes(koordinater);
		} catch (IOException e){
			System.out.println(e.getMessage());
		}

	}

	public static void sendPlayers() throws IOException {
		String playerString = "";
		for (int i = 0; i< players.size(); i++){
			playerString = playerString + players.get(i).getName()+ " " + players.get(i).getXpos() + " " + players.get(i).getYpos() + " " + players.get(i).getDirection() + "#";
		}
		for (Player p : players){
			p.getDataOut().writeBytes(playerString + "\n");
		}
	}
	
	public static void updatePlayer(int delta_x, int delta_y, String direction)
	{
		me.direction = direction;
		int x = me.getXpos(),y = me.getYpos();

		if (Generel.board[y+delta_y].charAt(x+delta_x)=='w') {
			me.addPoints(-1);
		} 
		else {
			// collision detection
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
              me.addPoints(10);
              //update the other player
              p.addPoints(-10);
              pair pa = getRandomFreePosition();
              p.setLocation(pa);
              pair oldpos = new pair(x+delta_x,y+delta_y);
              Gui.movePlayerOnScreen(oldpos,pa,p.direction);
			} else 
				me.addPoints(1);
			pair oldpos = me.getLocation();
			pair newpos = new pair(x+delta_x,y+delta_y);
			sendKoordinater(delta_x,delta_y,direction);
			Gui.movePlayerOnScreen(oldpos,newpos,direction);
			me.setLocation(newpos);
		}
		
		
	}
	
	public static Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}
	
	
	

}
