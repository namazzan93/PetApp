

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class JavaSocketServer {
	
	public static void main(String[] args) {
		int portNumber = 5000;
		ArrayList<String> params;
		DatabaseConnector dbCon;
		String str;
		
		try {
			System.out.println("Starting Java Socket Server ...");
			ServerSocket aServerSocket = new ServerSocket(portNumber);
			System.out.println("Listening at port " + portNumber + " ...");
			
			while(true) {
				Socket sock = aServerSocket.accept();
				InetAddress clientHost = sock.getLocalAddress();
				int clientPort = sock.getPort();
				System.out.println("A client connected. host : " + clientHost + ", port : "  + clientPort);
				
				ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
				params = (ArrayList<String>)instream.readObject();
				
				dbCon = new DatabaseConnector(params);
				boolean ret = dbCon.run();
				
				params.clear();
				params.add(Boolean.toString(ret));
				params.add(dbCon.getUserInfoStr());
				
				ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
				outstream.writeObject(params);
				outstream.flush();
				sock.close();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
