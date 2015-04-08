package pongserver.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;
import pongserver.utility.Player;


public class PongServer 
{
	private ArrayList<Player> clients;
	public final static int DEFAULT_PORT = 9876;
	
	DatagramSocket socketReceive;
	
	public PongServer() throws SocketException
	{
		clients = new ArrayList<Player>();
	}
	
	public void start()
	{
		try 
		{
			socketReceive = new DatagramSocket(DEFAULT_PORT);
		} 
		catch (SocketException e) 
		{	
			e.printStackTrace();
		}
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
	public void listen() throws IOException
	{
		System.out.println("Listening...");
		System.out.println("PongServer Local Port: " + socketReceive.getLocalPort());
		System.out.println("PongServer Port: " + socketReceive.getPort());
		
		DatagramPacket packet = NetworkHelper.receive(socketReceive); // blocking
		
		System.out.println("PongServer: received");
		
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
	}
	
	public void doTask(Task task, DatagramPacket packet)
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
			
			if (clients.size() == 2)
			{
				try 
				{
					DatagramSocket socket = new DatagramSocket();
					System.out.println("PongServer - Starting game - Sending two packets");
					
					NetworkHelper.send(socket, 
							clients.get(0).ipaddress,
							clients.get(0).port,
							Task.START_GAME);
					
					NetworkHelper.send(socket, 
							clients.get(1).ipaddress,
							clients.get(1).port,
							Task.START_GAME);		
					
					//TODO close the socket ? 
				} 
				catch (SocketException e1) 
				{
					//TODO
					e1.printStackTrace();
				}				
				catch (Exception e) 
				{
					//TODO
					e.printStackTrace();
				} 
			}
		}
		else
			System.out.println("all players already connected");
	}
}
