package Game2022.game;
import java.io.*;
import java.net.*;

public class TCPClient {

	public static void main(String argv[]) throws Exception{
		String sentence;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket= new Socket("localhost",6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + '\n');

	}
}


