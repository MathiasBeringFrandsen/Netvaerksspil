package Game2022.game;

import java.net.*;
import java.io.*;
import javafx.application.Application;

public class App {

	public static void main(String[] args) throws Exception{
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket= new Socket("localhost",6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		GameLogic.setOutputStream(outToServer);
		ClientThread in = new ClientThread(inFromServer);
		in.start();
		System.out.println("Indtast spillernavn");
		outToServer.writeBytes(inFromUser.readLine() + "\n");
		Application.launch(Gui.class);
	}
}