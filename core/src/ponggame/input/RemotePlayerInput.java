package ponggame.input;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.sun.corba.se.spi.activation.Server;

import ponggame.entity.Paddle;
import pongserver.network.PongServer;
import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;

public class RemotePlayerInput extends PlayerInput {
	private DatagramSocket socket;
	private InetAddress address;
	
	public RemotePlayerInput(Paddle paddle, layoutInput layout, DatagramSocket msocket, InetAddress address) 
	{
		super(paddle, layout);
		
		this.socket = msocket;
		this.address = address;
	}

	@Override
	void onDownKeyPressed(float delta) 
	{
		ArrayList<Vector2> data = new ArrayList<Vector2>();
		data.add(new Vector2(paddle.getX(), paddle.getY() + ));
		
		NetworkHelper.send(socket, address, PongServer.DEFAULT_PORT, Task.UPDATE_PADDLE, data);
	}

	@Override
	void onUpKeyPressed(float delta) 
	{
		// TODO
	}
}
