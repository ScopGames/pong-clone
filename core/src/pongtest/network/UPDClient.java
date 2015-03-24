package pongtest.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UPDClient {
	
	public static void main(String args[]) throws Exception {
	
		BufferedReader inFromUser;
		DatagramSocket clientSocket;
		InetAddress IPAddress;
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		DatagramPacket sendPacket;
		DatagramPacket receivePacket;
		
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName("192.168.0.3");
		
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String sentence = inFromUser.readLine();
		
		sendData = sentence.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
	
		clientSocket.send(sendPacket);
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		
		String modifiedSentence = new String(receivePacket.getData());
	    System.out.println("FROM SERVER:" + modifiedSentence);
		
	    clientSocket.close();
	}

}
