package pongserver.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;
import pongserver.utility.Player;

import com.badlogic.gdx.math.Vector2;


public class PongServer 
{
	private ArrayList<Player> clients;
	private DatagramSocket socketReceive;
	private Vector2 ballPosition;
	
	public GAME_STATE gameState;
	public final static int DEFAULT_PORT = 9876;
	public enum GAME_STATE {PLAYERS_CONNECTING, STARTED};
		
	public PongServer() throws SocketException
	{
		clients = new ArrayList<Player>();
		socketReceive = NetworkHelper.getSocket(DEFAULT_PORT);
		gameState = GAME_STATE.PLAYERS_CONNECTING;
		
		// TODO use a random position
		ballPosition = new Vector2(100,100);
		
		System.out.println("Listening...");
		System.out.println("PongServer Local Port: " + socketReceive.getLocalPort());
	}
		
	public void stop()
	{
		socketReceive.close();
	}
	
	/**
	 * Warning: this is a blocking method.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void listen()
	{
		DatagramPacket packet = NetworkHelper.receive(socketReceive); // blocking
				
		byte data[] = packet.getData();
				
		try 
		{
			Task task;
			task = (Task) NetworkHelper.deserialize(data);
			doTask(task, packet);
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * TODO Complete this method ! Now it uses fixed values, so it's 
	 * completely useless ! 
	 */	
	public void sendDataToPlayers()
	{
		ArrayList<Vector2> data = new ArrayList<Vector2>();
		
		data.add(ballPosition);
		
		clients.get(0).setPosition(new Vector2(0, 250));
		data.add(clients.get(0).position);
		
		clients.get(1).setPosition(new Vector2(600, 250));
		data.add(clients.get(1).position);
		
		for (Player player : clients) 
		{
			NetworkHelper.send(socketReceive, player.ipaddress,
					player.port, Task.UPDATE_BALL, data);
		}
	}
	
	private void doTask(Task task, DatagramPacket packet)
	{
		switch (task)
		{
			case REGISTER_PLAYER:
				register(packet);
				break;
			
			default:
				break; 
		}
	}
	
	private void register(DatagramPacket packet)
	{
		if(clients.size() < 2)
		{
			clients.add(new Player(packet.getAddress(),packet.getPort()));
			
			DatagramSocket socket = NetworkHelper.getSocket();
			
			if (clients.size() == 1)
			{
				Player player0 = clients.get(0);
				NetworkHelper.send(socket, 
						player0.ipaddress, 
						player0.port, 
						Task.CONNECTED);
			}
			
			if (clients.size() == 2)
			{				
				NetworkHelper.send(socket, 
						clients.get(0).ipaddress,
						clients.get(0).port,
						Task.INIT_GAME);
				
				NetworkHelper.send(socket, 
						clients.get(1).ipaddress,
						clients.get(1).port,
						Task.INIT_GAME);		
				
				gameState = GAME_STATE.STARTED;
				
				//TODO close the socket ? 
			} 
		}
		else
			System.out.println("all players already connected");
	}
	
	public void printClientsInfo()
	{
		for (Player player : clients) 
		{
			System.out.println("Player address:" + player.ipaddress + " port:" + player.port);
		}
	}
}
