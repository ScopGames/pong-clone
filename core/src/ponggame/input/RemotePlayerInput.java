package ponggame.input;

import java.net.DatagramSocket;

import ponggame.entity.Paddle;
import pongserver.utility.NetworkHelper;

public class RemotePlayerInput extends PlayerInput {
	private DatagramSocket socket;
	
	public RemotePlayerInput(Paddle paddle, layoutInput layout, DatagramSocket msocket) 
	{
		super(paddle, layout);
		socket = msocket;
	}

	@Override
	void onDownKeyPressed(float delta) 
	{
		// TODO 
	}

	@Override
	void onUpKeyPressed(float delta) 
	{
		// TODO
	}
}
