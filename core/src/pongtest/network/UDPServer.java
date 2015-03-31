package pongtest.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

class UDPServer
{
    private DatagramSocket serverSocket;
    private byte[] buffer;
                    
    public UDPServer() throws SocketException
    {
    	serverSocket = new DatagramSocket(9876);
    	buffer = new byte[1024]; // default buffer size is 1024
    }
   
    /**
     * 
     * @param port
     * @param bufferSize in bytes
     * @throws SocketException
     */
    public UDPServer(int port, int bufferSize) throws SocketException
    {
    	serverSocket = new DatagramSocket(port);
    	buffer = new byte[bufferSize];
    }
    
    public DatagramPacket receive() throws IOException
    {
    	DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
    	
    	System.out.println("Listening...");
    	serverSocket.receive(receivePacket); // blocking function
    	System.out.println("Packet received: " + receivePacket.getLength());
    	
    	return receivePacket;
    }
    
	public static byte[] serialize(Object obj) throws IOException 
	{
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
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