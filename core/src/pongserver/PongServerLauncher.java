package pongserver;

import java.net.SocketException;

import pongserver.network.PongServer;


public class PongServerLauncher 
{
	public static void main(String[] args) 
	{
		try 
		{
			final PongServer server = new PongServer();	
			
			while(server.gameState == PongServer.GAME_STATE.PLAYERS_CONNECTING)
			{
				server.listen();
			}
			
			server.printClientsInfo();
			
			Thread listeningT = new Thread(new Runnable() 
			{
				@Override
				public void run() 
				{
					while(server.gameState == PongServer.GAME_STATE.STARTED)
						server.listen();				
				}
			});
			listeningT.start();
						
			while(server.gameState == PongServer.GAME_STATE.STARTED)
			{
				server.sendDataToPlayers();
			}
			
			//listeningT.close();
			//server.stop();
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
	}
}
