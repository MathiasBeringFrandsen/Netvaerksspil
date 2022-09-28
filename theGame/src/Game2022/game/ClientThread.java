package Game2022.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientThread extends Thread {
    private BufferedReader input;

    public ClientThread(BufferedReader input){
        this.input = input;
    }

    public void run(){
        while (true){
            try {

                GameLogic.setPlayerList(modtagArraylist(new ArrayList<>(), input));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void sendKoordinater(ArrayList<Person> personer, DataOutputStream outstream) throws IOException {
//        String persons = "";
//        for (int i = 0; i< personer.size(); i++){
//            persons = persons + personer.get(i).getId()+ " " + personer.get(i).getNavn() + " " + personer.get(i).getBy() + "#";
//        }
//        outstream.writeBytes(persons);
//    }

    public static ArrayList<Player> modtagArraylist(ArrayList<Player> players, BufferedReader instream) throws IOException {
        String[] playerArray = instream.readLine().split("#");
        System.out.println(Arrays.toString(playerArray));
        for (int i = 0; i < playerArray.length; i++) {
            String[] thisplayer = playerArray[i].split(" ");
            System.out.println(thisplayer[0]);
            players.add(new Player(thisplayer[0], new pair(Integer.parseInt(thisplayer[1]), Integer.parseInt(thisplayer[2])), thisplayer[3]));
        }
        return players;
    }

}
