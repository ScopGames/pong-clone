package pongserver.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkHelper 
{	
	public static enum Task {REGISTER_PLAYER, UPDATE_PADDLE, START_GAME}	
       
	public static DatagramPacket receive(DatagramSocket socket) throws IOException
    {
    	byte[] buffer = new byte[1024];
    	DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
    	
    	socket.receive(receivePacket); // blocking function
       	
    	return receivePacket;
    }
    
    public static void send(DatagramSocket socket, InetAddress address, int port, Task task) throws Exception 
    {		
		byte[] buffer = serialize(task); 
		
		if (buffer.length < 1024)
		{
			DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, address, port);
			socket.send(sendPacket);
			//socket.close();
			// maybe close the socket ? 
		}
		else
		{
			throw new Exception("Can't send data greater than 1024 bytes");
		}
    }
         
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
}
