package Game2022.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientThread extends Thread {
    private BufferedReader input;

    public ClientThread(BufferedReader input){
        this.input = input;
    }

    public void run(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                GameLogic.setPlayerList(modtagArraylist(new ArrayList<>(), input));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static ArrayList<Player> modtagArraylist(ArrayList<Player> players, BufferedReader instream) throws IOException {
        String[] playerArray = instream.readLine().split("#");
        System.out.println(Arrays.toString(playerArray));
        for (int i = 0; i < playerArray.length; i++) {
            String[] thisplayer = playerArray[i].split(" ");
            System.out.println(thisplayer[0]);
            Player player = new Player(thisplayer[0], new pair(Integer.parseInt(thisplayer[1]), Integer.parseInt(thisplayer[2])), thisplayer[3]);
            players.add(player);
        }
        System.out.println(players.toString());
        return players;
    }

}
