package pongserver.utility;

import java.net.InetAddress;


public class EClient {
	
	private InetAddress ipaddress;
	private int port ;
	
	public EClient(InetAddress a, int p)
	{
		ipaddress = a;
		port = p;
		
	}
}
