package Game2022.game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;


public class GameLogic {

    public static List<Player> players = new ArrayList<>();
    public static Player player;
    public static DataOutputStream outputStream;
    public static ArrayList<Projectile> projectiles = new ArrayList<>();
    public static Chest chest;


    public static void setPlayerList(ArrayList<Player> newPlayerList) {
        for (Player player : players) {
            Gui.removePlayerOnScreen(player.getLocation());
        }
        if (!projectiles.isEmpty()) {
            for (Projectile p : projectiles) {
                Gui.removeProjectileOnScreen(p.location);
            }
            projectiles.clear();
        }

        players = newPlayerList;
        for (Player player : players) {
            Gui.placePlayerOnScreen(player.location, player.direction);
        }
    }

    public static void setProjectiles(ArrayList<Projectile> ps) {
        for (Projectile p : projectiles) {
            Gui.removeProjectileOnScreen(p.location);
        }
        projectiles = ps;
        for (int i = 0; i < projectiles.size(); i++) {
            if (i == 0) {
                Gui.placeProjectileOnScreen(projectiles.get(i), "start");
            } else if (i < projectiles.size() - 1) {
                Gui.placeProjectileOnScreen(projectiles.get(i), "middle");
            } else {
                Gui.placeProjectileOnScreen(projectiles.get(i), "end");
            }
        }
    }

    public static void setOutputStream(DataOutputStream out) {
        outputStream = out;
    }

    public static Player makePlayers(String name) {
        pair p = getRandomFreePosition();
        player = new Player(name, p, "up");
        players.add(player);
        return player;
    }

    public static Chest makeChest() {
        pair p = getRandomFreePosition();
        chest = new Chest(p);
        return chest;
    }

    public static void setChest(Chest c){
        if (!(chest == null)) {
            Gui.removeChestOnScreen(chest.getLocation());
        }
        chest = c;
        Gui.placeChestOnScreen(chest.getLocation());
    }

    public static pair getRandomFreePosition()
    // finds a random new position which is not wall
    // and not occupied by other players
    {
        int x = 1;
        int y = 1;
        boolean foundfreepos = false;
        while (!foundfreepos) {
            Random r = new Random();
            x = Math.abs(r.nextInt() % 18) + 1;
            y = Math.abs(r.nextInt() % 18) + 1;
            if (Generel.board[y].charAt(x) == ' ' && getProjectileAt(x, y) == null) // er det gulv ?
            {
                foundfreepos = true;
                for (Player p : players) {
                    if (p.getXpos() == x && p.getYpos() == y) //pladsen optaget af en anden
                        foundfreepos = false;
                }

            }
        }
        pair p = new pair(x, y);
        return p;
    }

    public static void sendProjectileToServer() {
        try {
            outputStream.writeBytes("projectile\n");
        } catch (IOException e) {
            System.out.println("IoException goddamnit");
        }

    }

    public static synchronized void sendProjectileToClient(Projectile projectile) throws IOException {
        String projectileString = "projectile#";
        projectiles.add(projectile);
        moveProjectilesForward(projectile);
//        System.out.println("Projectiles size: " + projectiles.size());
        for (Projectile projectile1 : projectiles) {
            Player playerKilled = getPlayerAt(projectile1.getLocation().getX(), projectile1.getLocation().getY());
            if (playerKilled != null) {
                playerHit(playerKilled);
            }
            projectileString = projectileString + projectile1.getLocation().getX() + " " + projectile1.getLocation().getY() + " " + projectile1.getDirection() + "#";
        }

        for (Player p : players) {
            p.getDataOut().writeBytes(projectileString + "\n");
        }

    }


    public static void sendKoordinater(int delta_x, int delta_y, String direction) {
        try {
            String koordinater;
            koordinater = delta_x + "#" + delta_y + "#" + direction;
            outputStream.writeBytes(koordinater + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void playerHit(Player playerKilled) {
        playerKilled.addPoints(-25);
        playerKilled.setLocation(getRandomFreePosition());
        try {
            sendPlayers();
        } catch (IOException e) {
            System.out.println("PlayerHit couldnt send players: IOexception");
        }
    }

    public static void sendPlayers() throws IOException {
        String playerString = "players#";
        for (int i = 0; i < players.size(); i++) {
            playerString = playerString + players.get(i).getName() + " " + players.get(i).getXpos() + " " + players.get(i).getYpos() + " " + players.get(i).getDirection() + " " + players.get(i).getPoint() + "#";
        }
        for (Player p : players) {
            p.getDataOut().writeBytes(playerString + "\n");
        }
    }

    public static void sendChest() throws IOException {
        String chestString = "" + chest.getLocation().getX() + "#" + chest.getLocation().getY();
        for (Player player : players){
            player.getDataOut().writeBytes(chestString + "\n");
        }
    }

    public static synchronized void updatePlayer(int delta_x, int delta_y, String direction, Player player) {
        player.direction = direction;
        int x = player.getXpos(), y = player.getYpos();

        if (Generel.board[y + delta_y].charAt(x + delta_x) == 'w') {
            player.addPoints(-1);
        } else {
            // collision detection
            Player p = getPlayerAt(x + delta_x, y + delta_y);
            Chest c = getChestAt(x + delta_x, y + delta_y);
            if (p != null) {
                player.addPoints(10);
                //update the other player
                p.addPoints(-10);
                pair pa = getRandomFreePosition();
                p.setLocation(pa);
            }
            if (c != null){
                player.addPoints(25);
                pair pa = getRandomFreePosition();
                c.setLocation(pa);
            } else
                player.addPoints(1);
            pair newpos = new pair(x + delta_x, y + delta_y);
            player.setLocation(newpos);
            try {
                sendChest();
                sendPlayers();
                projectiles.clear();
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
            if (p.getXpos() == x && p.getYpos() == y) {
                return p;
            }
        }
        return null;
    }

    public static Chest getChestAt(int x, int y){
        if (chest.getLocation().getX() == x && chest.getLocation().getY() == y){
            return chest;
        }
        return null;
    }

    public static Projectile getProjectileAt(int x, int y) {
        for (Projectile p : projectiles) {
            if (p.location.getX() == x && p.location.getY() == y) {
                return p;
            }
        }
        return null;
    }

    public static void moveProjectilesForward(Projectile p) {
        for (int i = 1; i <= 20; i++) {
            if (p.direction.equals("up")) {
                if (Generel.board[p.getLocation().getY() - i].charAt(p.getLocation().getX()) == ' ') {
                    Projectile projectile = new Projectile(new pair(p.location.getX(), p.location.getY() - i), p.direction);
                    projectiles.add(projectile);
                } else {
                    break;
                }
            }
            if (p.direction.equals("down")) {
                if (Generel.board[p.getLocation().getY() + i].charAt(p.getLocation().getX()) == ' ') {
                    Projectile projectile = new Projectile(new pair(p.location.getX(), p.location.getY() + i), p.direction);
                    projectiles.add(projectile);
                } else {
                    break;
                }
            }
            if (p.direction.equals("left")) {
                if (Generel.board[p.getLocation().getY()].charAt(p.getLocation().getX() - i) == ' ') {
                    Projectile projectile = new Projectile(new pair(p.location.getX() - i, p.location.getY()), p.direction);
                    projectiles.add(projectile);
                } else {
                    break;
                }

            }
            if (p.direction.equals("right")) {
                if (Generel.board[p.getLocation().getY()].charAt(p.getLocation().getX() + i) == ' ') {
                    Projectile projectile = new Projectile(new pair(p.location.getX() + i, p.location.getY()), p.direction);
                    projectiles.add(projectile);
                } else {
                    break;
                }
            }
        }
    }
}
