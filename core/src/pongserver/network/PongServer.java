package pongserver.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import ponggame.entity.GameEntity;
import pongserver.utility.Data;
import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;
import pongserver.utility.NetworkNode;

import com.badlogic.gdx.math.Vector2;


public class PongServer 
{
	private ArrayList<NetworkNode> clients;
	private DatagramSocket socketReceive, socketSend;
	private GameEntity gameEntity;
	
	public final static int DEFAULT_PORT = 9876;
	public enum GAME_STATE {PLAYERS_CONNECTING, STARTED};
	public GAME_STATE gameState;
		
	public PongServer() throws SocketException
	{
		clients = new ArrayList<NetworkNode>();
		socketReceive = NetworkHelper.getSocket(DEFAULT_PORT);
		socketSend = NetworkHelper.getSocket();
		gameState = GAME_STATE.PLAYERS_CONNECTING;
		
		// TODO use a random position ?
		gameEntity = new GameEntity(new Vector2(100,100), new Vector2(0, 250),
				new Vector2(640-20, 250)); // 640 is the screen width
		
		System.out.println("PongServer listeningn on: " + socketReceive.getLocalPort());
		System.out.println("PongServer sending from: " + socketSend.getLocalPort());
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
				
		byte buffer[] = packet.getData();
				
		try 
		{
			Data data;
			data = (Data) NetworkHelper.deserialize(buffer);
			
			switch (data.getTask())
			{
				case REGISTER_PLAYER:
					register(packet.getAddress(), packet.getPort());
					break;
				
				case UPDATE_GAME_ENTITIES:
					//System.out.println("updating game entities");

					GameEntity updatedGame = data.getGameEntity();
					
					Vector2 vec = updatedGame.getPaddle1();
					if (vec != null)
					{
						System.out.println("updating paddle1" + "x: " + vec.x + " y: " + vec.y);
						this.gameEntity.setPaddle1(vec);						
					}
					
					vec = updatedGame.getPaddle2();
					if (vec != null)
					{
						System.out.println("updating paddle2" + "x: " + vec.x + " y: " + vec.y);
						this.gameEntity.setPaddle2(vec);						
					}
					
					break;
				
				default:
					break; 
			}
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
	
	public void sendDataToPlayers()
	{
		Data d = new Data(Task.UPDATE_GAME_ENTITIES, gameEntity);
		
		for (NetworkNode player : clients) 
		{
			NetworkHelper.send(socketSend, player, d);
		}	
	}
	
	private void register(InetAddress address, int port)
	{
		if(clients.size() < 2)
		{
			clients.add(new NetworkNode(address, port));
			
			DatagramSocket socket = NetworkHelper.getSocket();
			Data data = new Data(Task.CONNECTED);
			
			if (clients.size() == 1)
			{
				NetworkHelper.send(socket, clients.get(0), data);
			}
			
			if (clients.size() == 2)
			{				
				data.setTask(Task.INIT_GAME_LEFT);
				NetworkHelper.send(socket, clients.get(0), data);
				
				data.setTask(Task.INIT_GAME_RIGHT);
				NetworkHelper.send(socket, clients.get(1), data);
				
				gameState = GAME_STATE.STARTED;
				
				//TODO close the socket ? 
			} 
		}
		else
			System.out.println("all players already connected");
	}
	
	public void printClientsInfo()
	{
		for (NetworkNode player : clients) 
		{
			System.out.println("Player address:" + player.ipaddress + " port:" + player.port);
		}
	}
}
