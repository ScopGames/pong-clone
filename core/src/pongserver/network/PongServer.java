package pongserver.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;

import pongserver.utility.EClient;
import pongserver.utility.UDPServer;
import pongserver.utility.UPDClient.Task;


public class PongServer 
{
	private UDPServer server;
	private ArrayList<EClient> clients;
	public enum Gamestate {STARTED, FINISHED}
	
	public PongServer() throws SocketException
	{
		server = new UDPServer();
		clients = new ArrayList<EClient>();
	}
	
	/**
	 * Warning: this is a blocking method.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void listen() throws IOException
	{
		DatagramPacket packet = server.receive(); // blocking
		byte data[] = packet.getData();
				
		try 
		{
			Task task;
			task = (Task)UDPServer.deserialize(data);
			doTask(task, packet);
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			
		}	
	}
	
	public void doTask(Task task, DatagramPacket packet) throws ClassNotFoundException, IOException 
	{
		switch (task)
		{
			case REGISTER_PLAYER:
				if(task == Task.REGISTER_PLAYER && clients.size() < 2)
				{
					clients.add(new EClient(packet.getAddress(),packet.getPort()));
				}
				else
					System.out.println("all players already connected");
				break;
		
			default:
				break; 
		}
	}
}
