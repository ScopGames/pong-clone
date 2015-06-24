/**
 * NetworkNode.java
 * 
 * This Class represent a generic Node in the Network
 */
package pongserver.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.math.Vector2;


public class NetworkNode {
	
	public InetAddress ipaddress;
	public int port;
	
	/**
	 * Create an object containing the InetAddress a and the port p
	 * 
	 * @param a
	 * @param p
	 */
	public NetworkNode(InetAddress a, int p)
	{
		ipaddress = a;
		port = p;
	}
	
	/**
	 * Constructor overloading
	 * 
	 * Ip address is given as a String and converted into an InetAddress
	 * 
	 * @param a
	 * @param p
	 */
	public NetworkNode (String a, int p)
	{
		try {
			ipaddress = InetAddress.getByName(a);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		port = p;
	}

}
