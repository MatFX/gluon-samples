package testserver;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer 
{

	public static void main(String[] args) {
		try 
		{
			
			ServerSocket serverSocket = new ServerSocket(4711);
			
			System.out.println("waiting socket");
			Socket socket = serverSocket.accept();
			
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println("This is a message sent to the server");
			int zaehler = 1;
			//bis Sankt-Nimmerleinstag
			while(true)
			{
				Thread.sleep(2000);
				System.out.println("current number " + zaehler);
				writer.println("Running number " + zaehler);
				writer.flush();
				zaehler++;
				
			}
		
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
