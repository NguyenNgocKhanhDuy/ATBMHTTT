package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTCP {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 2000);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
		String greeting = br.readLine();
		System.out.println(greeting);
		while (true) {
			String line = userIn.readLine();
			pw.println(line);
			if (line.equals("QUIT")) {
				break;
			}
			System.out.println(br.readLine());
		}
		socket.close();
	}
}
