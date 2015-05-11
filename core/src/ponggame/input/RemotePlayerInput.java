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
	private Task lastTask;
	private Vector2 tmpPosition;
	

	public RemotePlayerInput(Paddle paddle, layoutInput layout, 
			DatagramSocket msocket, InetAddress address, boolean isPaddleLeft) 
	{
		super(paddle, layout);
		this.tmpPosition = paddle.getPosition();
		this.socket = msocket;
		this.server = new NetworkNode(address, PongServer.DEFAULT_PORT);
		this.isPaddleLeft = isPaddleLeft;
	}

	@Override
	void onDownKeyPressed(float delta) 
	{
		float newY = tmpPosition.y - delta*paddle.getMovement();
		
		GameEntity gameEntity = new GameEntity();
		
		if (isPaddleLeft)
		{
			gameEntity.setPaddle1(new Vector2(paddle.getPosition().x, newY));			
		}
		else
		{
			gameEntity.setPaddle2(new Vector2(paddle.getPosition().x, newY));
		}
		
		Data data = new Data(Task.GOING_DOWN, gameEntity);
		setLastTask(Task.GOING_DOWN);
		NetworkHelper.send(socket, server, data);
		
		tmpPosition = new Vector2(paddle.getPosition().x, newY);
		//paddle.setPosition(paddle.getPosition().x, newY);;
	}

	@Override
	void onUpKeyPressed(float delta) 
	{		
		float newY = tmpPosition.y + delta*paddle.getMovement();
		
		GameEntity gameEntity = new GameEntity();
		if (isPaddleLeft)
		{
			gameEntity.setPaddle1(new Vector2(paddle.getPosition().x, newY));
		}
		else
		{
			gameEntity.setPaddle2(new Vector2(paddle.getPosition().x, newY));
		}
		
		Data data = new Data(Task.GOING_UP, gameEntity);
		setLastTask(Task.GOING_UP);
		NetworkHelper.send(socket, server, data);
		
		tmpPosition = new Vector2(paddle.getPosition().x, newY);		
		//paddle.setPosition(paddle.getPosition().x, newY);
	}

	public Task getLastTask() {
		return lastTask;
	}

	public void setLastTask(Task lastTask) {
		this.lastTask = lastTask;
	}
	
	public Vector2 getTmpPosition() {
		return tmpPosition;
	}
	
	public void setTmpPosition(Vector2 tmpPosition) {
		this.tmpPosition = tmpPosition;
	}
}
