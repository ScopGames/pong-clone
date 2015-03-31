package pongserver;

import java.io.IOException;
import java.net.SocketException;

import pongserver.network.PongServer;


public class PongServerLauncher {

	public static void main(String[] args) {
		try 
		{
			PongServer server = new PongServer();	
			
			server.start();
			
			while(true) // change this
			{
				server.listen();
			}
			
			//server.stop();
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
