package pongserver;

import java.net.SocketException;

import pongserver.network.PongServer;


public class PongServerLauncher 
{

	public static void main(String[] args) 
	{
		try 
		{
			PongServer server = new PongServer();	
						
			while(server.gameState == PongServer.GAME_STATE.PLAYERS_CONNECTING)
			{
				server.listen();
			}
			
			//Thread listeningT = new Thread(new ListenHandler());
			//listeningT.run();
			
			server.printClientsInfo();
			
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
	
	static class ListenHandler implements Runnable
	{
		@Override
		public void run() 
		{
			
		}	
	}
}
