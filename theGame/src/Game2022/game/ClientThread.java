package Game2022.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientThread extends Thread {
    private BufferedReader input;

    public ClientThread(BufferedReader input) {
        this.input = input;
    }

    public void run() {
        try {
            Thread.sleep(8000);
            while (true) {
                modtag(input);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void modtag(BufferedReader input) {
        try {
            String[] inArray = input.readLine().split("#");
            if (inArray[0].equals("players")){
                GameLogic.setPlayerList(modtagArraylist(new ArrayList<>(), inArray));
            } else if (inArray[0].equals("projectile")){
                GameLogic.setProjectiles(recieveProjectiles(inArray));
            } else {
                GameLogic.setChest(recieveChest(inArray));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Player> modtagArraylist(ArrayList<Player> players, String[] array) throws IOException {
        String[] playerArray = array;
//        System.out.println(Arrays.toString(playerArray));
        for (int i = 1; i < playerArray.length; i++) {
            String[] thisplayer = playerArray[i].split(" ");
//            System.out.println(thisplayer[0]);
            Player player = new Player(thisplayer[0], new pair(Integer.parseInt(thisplayer[1]), Integer.parseInt(thisplayer[2])), thisplayer[3]);
            player.setPoint(Integer.parseInt(thisplayer[4]));
            players.add(player);
        }
        System.out.println(players.toString());
        return players;
    }


    public static ArrayList<Projectile> recieveProjectiles(String[] array){
        ArrayList<Projectile> projectiles = new ArrayList<>();
        String[] projectileArray = array;
        for (int i = 1; i < projectileArray.length; i++){
            String[] thisProjectile = projectileArray[i].split(" ");
            Projectile projectile = new Projectile(new pair(Integer.parseInt(thisProjectile[0]), Integer.parseInt(thisProjectile[1])), thisProjectile[2]);
            projectiles.add(projectile);
        }
        return projectiles;
    }

    public static Chest recieveChest(String[] array){
        String[] chestLoc = array;
        Chest chest = new Chest(new pair(Integer.parseInt(chestLoc[0]), Integer.parseInt(chestLoc[1])));
        return chest;
    }

}
