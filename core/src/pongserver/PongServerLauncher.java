package pongserver;

import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import pongserver.network.PongServer;


public class PongServerLauncher 
{
	/**
	 * Delay in milliseconds between each call to sendDataToPlayers().
	 * 
	 * For a 60 updates per second, this value has to be at least 17:
	 *     
	 *     1/60 = 16.6 ~= 17
	 *     
	 */
	static long responseDelay = 15; // millis
	
	public static void main(String[] args) 
	{
		try 
		{
			final PongServer server = new PongServer();	
			
			// while there aren't 2 players connected, listen for requests
			while(server.gameState == PongServer.GAME_STATE.PLAYERS_CONNECTING)
			{
				server.listen();
			}
			// now 2 players are connected
			
			// prints the clients' ip and port.
			server.printClientsInfo();
			
			// listen on a new thread for players' actions.
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
			
			// Updates the clients with the game state periodically with 
			// period = responseDelay
					
			Timer timer = new Timer();
			timer.schedule(new TimerTask() 
			{
				@Override
				public void run() 
				{
					if(server.gameState == PongServer.GAME_STATE.STARTED)
					{
						server.sendDataToPlayers();
					}
					else
						this.cancel(); // close timer
				}
			}, 0, responseDelay);
			
			//listeningT.close();
			//server.stop();
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
	}
}
