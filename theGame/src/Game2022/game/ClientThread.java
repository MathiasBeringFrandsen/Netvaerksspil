package Game2022.game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ClientThread extends Thread {
    private BufferedReader input;

    public ClientThread(BufferedReader input){
        this.input = input;
    }

    public void run(){
        while (true){
            try {
                String arrayString;
                arrayString = input.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
