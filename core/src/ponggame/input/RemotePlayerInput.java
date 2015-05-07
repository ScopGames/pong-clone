package ponggame.input;

import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.math.Vector2;

import ponggame.entity.GameEntity;
import ponggame.entity.Paddle;
import pongserver.network.PongServer;
import pongserver.utility.Data;
import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;
import pongserver.utility.NetworkNode;

public class RemotePlayerInput extends PlayerInput {
	private DatagramSocket socket;
	private NetworkNode server;
	private boolean isPaddleLeft;	
	
	public RemotePlayerInput(Paddle paddle, layoutInput layout, 
			DatagramSocket msocket, InetAddress address, boolean isPaddleLeft) 
	{
		super(paddle, layout);
		
		this.socket = msocket;
		this.server = new NetworkNode(address, PongServer.DEFAULT_PORT);
		this.isPaddleLeft = isPaddleLeft;
	}

	@Override
	void onDownKeyPressed(float delta) 
	{
		GameEntity gameEntity = new GameEntity();
		if (isPaddleLeft)
		{
			gameEntity.setPaddle1(new Vector2(paddle.getX(), 
					paddle.getY() - delta*paddle.getMovement()));			
		}
		else
		{
			gameEntity.setPaddle2(new Vector2(paddle.getX(), 
					paddle.getY() - delta*paddle.getMovement()));
		}
		
		Data data = new Data(Task.GOING_DOWN, gameEntity);
				
		NetworkHelper.send(socket, server, data);
	}

	@Override
	void onUpKeyPressed(float delta) 
	{		
		GameEntity gameEntity = new GameEntity();
		if (isPaddleLeft)
		{
			gameEntity.setPaddle1(new Vector2(paddle.getX(), 
					paddle.getY() + delta*paddle.getMovement()));
		}
		else
		{
			gameEntity.setPaddle2(new Vector2(paddle.getX(), 
					paddle.getY() + delta*paddle.getMovement()));
		}
		
		Data data = new Data(Task.GOING_UP, gameEntity);
				
		NetworkHelper.send(socket, server, data);
	}
}
