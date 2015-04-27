package pongserver.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
	private DatagramSocket socketReceive;
	private Vector2 ballPosition;
	
	public GAME_STATE gameState;
	public final static int DEFAULT_PORT = 9876;
	public enum GAME_STATE {PLAYERS_CONNECTING, STARTED};
		
	public PongServer() throws SocketException
	{
		clients = new ArrayList<NetworkNode>();
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
				
		byte buffer[] = packet.getData();
				
		try 
		{
			Data data;
			data = (Data) NetworkHelper.deserialize(buffer);
			doTask(data.getTask(), packet);
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
		GameEntity gameentity = new GameEntity(ballPosition, new Vector2(0, 250), new Vector2(600, 250));
		Data d = new Data(Task.UPDATE_PADDLE,gameentity);
		
		for (NetworkNode player : clients) 
		{
			NetworkHelper.send(socketReceive, player,d);
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
		System.out.println("registrato");
		if(clients.size() < 2)
		{
			clients.add(new NetworkNode(packet.getAddress(),packet.getPort()));
			
			DatagramSocket socket = NetworkHelper.getSocket();
			Data data = new Data(Task.CONNECTED);
			
			if (clients.size() == 1)
			{
				NetworkHelper.send(socket, clients.get(0), data);
			}
			
			if (clients.size() == 2)
			{				
				data.setTask(Task.INIT_GAME);
				NetworkHelper.send(socket, clients.get(0), data);
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
