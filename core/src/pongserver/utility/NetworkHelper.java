package pongserver.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class NetworkHelper 
{	
	/**
	 * Task is an enum representing the current state of the network game protocol.
	 *  
	 * Quick explanation of the values:
	 * - INIT_GAME_LEFT: game set up and you are the left paddle
	 * - INIT_GAME_RIGHT: game set up and you are the right paddle 
	 * 
	 */
	public static enum Task {REGISTER_PLAYER, 
		UPDATE_GAME_ENTITIES,
		GOING_UP,
		GOING_DOWN,
		INIT_GAME_LEFT,
		INIT_GAME_RIGHT,
		CONNECTED}	
       
	public static DatagramPacket receive(DatagramSocket socket)
    {
    	byte[] buffer = new byte[512];
    	DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
    	
    	try 
    	{
    		socket.receive(receivePacket); // blocking function
    	}
    	catch(IOException e)
    	{
    		// TODO
    	}
       	
    	return receivePacket;
    }
    
    /*public static void send(DatagramSocket socket, String address, int port, Task task)
    {	
    	InetAddress iAddress;
		try 
		{
			iAddress = InetAddress.getByName(address);
			send(socket, iAddress, port, task); 
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
    }*/
    
    /*public static void send(DatagramSocket socket, NetworkNode p, Data d) 
    {		
		byte[] buffer;
		
		try 
		{
			buffer = serialize(d);
			
			if (buffer.length < 1024)
			{
				DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, p.ipaddress, p.port);
				socket.send(sendPacket);
				//socket.close();
				// maybe close the socket ? 
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
    }
    */
         
	public static byte[] serialize(Object obj) throws IOException 
	{
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    out.flush();
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException 
	{
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	
	/**
	 * Warning, do not use this function unless you know
	 * what you are doing !
	 * @return 
	 */
	public static DatagramSocket getSocket(int port)
	{
		DatagramSocket sock = null;
		
		try 
		{
			sock = new DatagramSocket(port);	
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
		
		return sock;
	}
	
	/**
	 * Warning, do not use this function unless you know
	 * what you are doing !
	 * @return 
	 */
	public static DatagramSocket getSocket()
	{
		DatagramSocket sock = null;
		
		try 
		{
			sock = new DatagramSocket();	
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
		
		return sock;
	}
	
	public static void send(DatagramSocket socket, NetworkNode p, Data d)
	{
	byte[] buffer;
			
	try 
	{
		buffer = d.getStringData().getBytes();
		
		
		if (buffer.length < 1024)
		{
			DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, p.ipaddress, p.port);
			socket.send(sendPacket);
			//socket.close();
			// maybe close the socket ? 
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
}
