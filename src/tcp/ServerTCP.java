package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerTCP {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(2000);
		while (true) {
			Socket socket = serverSocket.accept();
			ServerThread st = new ServerThread(socket);
			st.start();
		}
	}
}
