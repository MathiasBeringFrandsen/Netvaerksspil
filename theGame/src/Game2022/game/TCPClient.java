package Game2022.game;
import javafx.application.Application;

import java.io.*;
import java.net.*;
// Denne er kun medtaget til Test-formål, skal IKKE anvendes.
public class TCPClient {

	public static void main(String argv[]) throws Exception {

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", 6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(inFromUser.readLine() + "\n");

	}
}


