package pongserver.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.math.Vector2;


public class NetworkNode {
	
	public InetAddress ipaddress;
	public int port;
	
	public NetworkNode(InetAddress a, int p)
	{
		ipaddress = a;
		port = p;
	}
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
