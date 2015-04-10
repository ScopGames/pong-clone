package pongserver.utility;

import java.net.InetAddress;

import com.badlogic.gdx.math.Vector2;


public class Player {
	
	public InetAddress ipaddress;
	public int port;
	public Vector2 position;
	
	public Player(InetAddress a, int p)
	{
		ipaddress = a;
		port = p;
	}
	
	public void setPosition(Vector2 position)
	{
		this.position = position;
	}
}
