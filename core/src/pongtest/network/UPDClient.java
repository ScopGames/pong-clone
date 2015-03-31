package pongtest.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UPDClient {
	public enum Task {REGISTER_PLAYER, UPDATE_PADDLE}
	
	public static void main(String args[]) throws Exception {
		DatagramSocket clientSocket;
		InetAddress IPAddress;	
		byte[] sendData = new byte[1024];
		DatagramPacket sendPacket;
		
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName("127.0.0.1");
		
		sendData = UDPServer.serialize(Task.REGISTER_PLAYER); 
		
		if (sendData.length <= 1024)
		{
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
			System.out.println("packet sent\nSize: " + sendPacket.getLength());
		}
					
	    clientSocket.close();
	}
}
