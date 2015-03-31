package pongserver.utility;

import java.net.InetAddress;


public class EClient {
	
	public InetAddress ipaddress;
	public int port ;
	
	public EClient(InetAddress a, int p)
	{
		ipaddress = a;
		port = p;
		
	}
}
