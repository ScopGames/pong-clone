package pongserver.utility;

import java.net.InetAddress;


public class Player {
	
	public InetAddress ipaddress;
	public int port;
	
	public Player(InetAddress a, int p)
	{
		ipaddress = a;
		port = p;
	}
}
