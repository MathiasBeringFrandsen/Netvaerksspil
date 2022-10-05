package Game2022.game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;


public class GameLogic {

	public static List<Player> players = new ArrayList<>();
	public static Player player;
	public static DataOutputStream outputStream;
	public static List<Projectile> projectiles = new ArrayList<>();


	public static void setPlayerList(ArrayList<Player> newPlayerList){
		for (Player player : players){
			Gui.removePlayerOnScreen(player.getLocation());
		}
		players = newPlayerList;
		System.out.println(newPlayerList.toString());
		System.out.println(players.size());
		for (Player player : players) {
			Gui.placePlayerOnScreen(player.location, player.direction);
		}
		moveProjectilesForward();
	}

	public static void setProjectiles(ArrayList<Projectile> ps){
		projectiles = ps;
		ArrayList<Projectile> deleteProjectiles = new ArrayList<>();
		for (Projectile p : projectiles){
			if (Generel.board[p.getLocation().getY()].charAt(p.getLocation().getX())==' ') {
				Gui.placeProjectileOnScreen(p, "start");
			}
			else{
				deleteProjectiles.add(p);
			}
		}
		for (Projectile p : deleteProjectiles){
			projectiles.remove(p);
		}
	}

	public static void setOutputStream(DataOutputStream out){
		outputStream = out;
	}
	
	public static Player makePlayers(String name) {
		pair p=getRandomFreePosition();
		player = new Player(name,p,"up");
		players.add(player);
		return player;
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

	public static void sendProjectileToServer() {
		try {
			System.out.println("Send!");
			outputStream.writeBytes("projectile\n");
		}
		catch (IOException e){
			System.out.println("IoException goddamnit");
		}

	}

	public static void sendProjectileToClient(Projectile projectile) throws IOException {
		String projectileString = "";
		projectiles.add(projectile);
		for (int i = 0; i < projectiles.size(); i++){
			projectileString = projectileString + projectiles.get(i).getLocation().getX() + " " + projectiles.get(i).getLocation().getY() + " " + projectiles.get(i).getDirection() + "#";
		}
		for (Player p : players){
			p.getDataOut().writeBytes(projectileString + "\n");
		}

	}



	public static void sendKoordinater(int delta_x, int delta_y, String direction){
		try {
			String koordinater;
			koordinater = delta_x + "#" + delta_y + "#" + direction;
			outputStream.writeBytes(koordinater + "\n");
		} catch (IOException e){
			System.out.println(e.getMessage());
		}

	}

	public static void sendPlayers() throws IOException {
		String playerString = "";
		for (int i = 0; i< players.size(); i++){
			playerString = playerString + players.get(i).getName()+ " " + players.get(i).getXpos() + " " + players.get(i).getYpos() + " " + players.get(i).getDirection() + " " + players.get(i).getPoint() + "#";
		}
		System.out.println(playerString);
		for (Player p : players){

			p.getDataOut().writeBytes(playerString + "\n");

		}
	}
	
	public static synchronized void updatePlayer(int delta_x, int delta_y, String direction, Player player)
	{
		player.direction = direction;
		int x = player.getXpos(),y = player.getYpos();

		if (Generel.board[y+delta_y].charAt(x+delta_x)=='w') {
			player.addPoints(-1);
		} 
		else {
			// collision detection
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			Projectile projectile = getProjectileAt(x+delta_x, y+delta_y);
			if (p!=null) {
              player.addPoints(10);
              //update the other player
              p.addPoints(-10);
              pair pa = getRandomFreePosition();
              p.setLocation(pa);
			}
			else if(projectile != null){
				projectiles.remove(projectile);
			}
			else
				player.addPoints(1);
			pair newpos = new pair(x+delta_x,y+delta_y);
			player.setLocation(newpos);
			try {
				sendPlayers();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	/*
	public static void updateProjectile(Projectile projectile){
		if (!projectiles.contains(projectile)){
			projectiles.add(projectile);
			sendProjectileToServer();
		}
	}
	*/
	
	public static Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}

	public static Projectile getProjectileAt(int x, int y) {
		for (Projectile p : projectiles) {
			if (p.location.getX()==x && p.location.getY()==y) {
				return p;
			}
		}
		return null;
	}

	public static void moveProjectilesForward(){
		for (Projectile p : projectiles){
			boolean end = false;
			int xPos = p.getLocation().getX();
			int yPos = p.getLocation().getY();
				switch (p.getDirection()) {
					case "up":
						if (Generel.board[p.getLocation().getY()-1].charAt(p.getLocation().getX())==' '){
							p.setLocation(new pair(xPos, yPos - 1));
						}
						else{
							end = true;
						}
						break;
					case "down":
						if (Generel.board[p.getLocation().getY()+1].charAt(p.getLocation().getX())==' '){
							p.setLocation(new pair(xPos, yPos + 1));
						}
						else{
							end = true;
						}
						break;
					case "left":
						if (Generel.board[p.getLocation().getY()].charAt(p.getLocation().getX()-1)==' '){
							p.setLocation(new pair(xPos - 1, yPos));
						}
						else{
							end = true;
						}
						break;
					case "right":
						if (Generel.board[p.getLocation().getY()].charAt(p.getLocation().getX()+1)==' '){
							p.setLocation(new pair(xPos + 1, yPos));
						}
						else{
							end = true;
						}
						break;
					default:
						break;
				}
				if (!end){
					Gui.placeProjectileOnScreen(p, "middle");
				}
				else{
					Gui.placeProjectileOnScreen(p, "end");
				}
		}
	}
	

}
