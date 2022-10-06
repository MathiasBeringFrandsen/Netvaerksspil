package Game2022.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientThread extends Thread {
    private BufferedReader input;

    public ClientThread(BufferedReader input) {
        this.input = input;
    }

    public void run() {
        try {
            Thread.sleep(4000);
            while (true) {
                String line = input.readLine();
                String[] checkLine = line.split("#");
                String[] checkLine2 = checkLine[0].split(" ");
                System.out.println(line.split(" ").length);
                if (checkLine2.length != 3){
                    GameLogic.setPlayerList(modtagArraylist(new ArrayList<>(), line));
                }
                else{
                    GameLogic.setProjectiles(recieveProjectiles(line));
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Player> modtagArraylist(ArrayList<Player> players, String line) throws IOException {
        String[] playerArray = line.split("#");
        System.out.println(Arrays.toString(playerArray));
        for (int i = 0; i < playerArray.length; i++) {
            String[] thisplayer = playerArray[i].split(" ");
            System.out.println(thisplayer[0]);
            Player player = new Player(thisplayer[0], new pair(Integer.parseInt(thisplayer[1]), Integer.parseInt(thisplayer[2])), thisplayer[3]);
            player.setPoint(Integer.parseInt(thisplayer[4]));
            players.add(player);
        }
        System.out.println(players.toString());
        return players;
    }


    public static HashMap<Projectile, ArrayList<Projectile>> recieveProjectiles(String line){
        HashMap<Projectile, ArrayList<Projectile>> projectiles = new HashMap<>();
        String[] projectileArray = line.split("#");
        for (int i = 0; i < projectileArray.length; i++){
            String[] thisProjectile = projectileArray[i].split(" ");
            System.out.println(Arrays.toString(thisProjectile));
            Projectile projectile = new Projectile(new pair(Integer.parseInt(thisProjectile[0]), Integer.parseInt(thisProjectile[1])), thisProjectile[2]);
            projectiles.put(projectile, new ArrayList<>());
        }
        return projectiles;
    }

}
