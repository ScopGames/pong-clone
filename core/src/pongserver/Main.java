package pongserver;

import java.io.IOException;
import java.net.SocketException;

import pongserver.network.PongServer;


public class Main {

	public static void main(String[] args) {
		try 
		{
			PongServer server = new PongServer();	
			
			while(true)
			{
				server.listen();
			}
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
