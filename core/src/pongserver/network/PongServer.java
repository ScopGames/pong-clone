package pongserver.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import ponggame.entity.Ball;
import ponggame.entity.GameEntity;
import ponggame.entity.Paddle;
import pongserver.utility.Data;
import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;
import pongserver.utility.NetworkNode;

import com.badlogic.gdx.math.Vector2;


public class PongServer 
{
	/**
	 * clients.get(0) is the left Paddle
	 * clients.get(0) is the right Paddle
	 */
	private ArrayList<NetworkNode> clients;
	private DatagramSocket socket;
	private GameEntity gameEntity;
	private GameEntity updatedGame;
	private Ball ball;
	private Paddle paddleLeft, paddleRight;
	private DatagramPacket packet;
	
	public final static int DEFAULT_PORT = 9876;
	public enum GAME_STATE {PLAYERS_CONNECTING, STARTED};
	public GAME_STATE gameState;
	
	private enum PLAYER{LEFT, RIGHT, NOT_A_BANANA}
	
	public PongServer() throws SocketException
	{
		clients = new ArrayList<NetworkNode>();
		socket = NetworkHelper.getSocket(DEFAULT_PORT);
		gameState = GAME_STATE.PLAYERS_CONNECTING;
		
		// TODO use a random position ?
		// TODO get velocity from a configuration file look at #issue12
		ball = new Ball(new Vector2(300, 400), new Vector2(60, 25)); 
		paddleLeft = new Paddle(new Vector2(0, 250));
		paddleRight = new Paddle(new Vector2(640-20, 250)); // 640 is the screen width
		
		gameEntity = new GameEntity(ball.getPosition(), paddleLeft.getPosition(),
				paddleRight.getPosition()); 
		
		System.out.println("PongServer listening/sending on/from: " + socket.getLocalPort());
	}
		
	public void stop()
	{
		socket.close();
	}
	
	/**
	 * Warning: this is a blocking method.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void listen()
	{		
		packet = NetworkHelper.receive(socket); // blocking
				
		byte buffer[] = packet.getData();
		
		try 
		{
			PLAYER player;
			Data data;
			data = (Data) NetworkHelper.deserialize(buffer);			
			
			switch (data.getTask())
			{
				case REGISTER_PLAYER:
					register(packet.getAddress(), packet.getPort());
					break;
									
				case GOING_DOWN:
					player = getPlayerSide();
					
					updatedGame = data.getGameEntity();
					
					if (player != PLAYER.NOT_A_BANANA)
					{
						updatePaddlesDown(player, updatedGame);
					}
					else
						System.out.println("siamo delle banane");
					
					break;
				
				case GOING_UP:
					player = getPlayerSide();
					
					updatedGame = data.getGameEntity();
					
					if (player != PLAYER.NOT_A_BANANA)
					{
						updatePaddlesUp(player, updatedGame);
					}
									
					break;
				
				default:
					break; 
			}
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void sendDataToPlayers()
	{
		Data d = new Data(Task.UPDATE_GAME_ENTITIES, gameEntity);
		
		for (NetworkNode player : clients) 
		{
			NetworkHelper.send(socket, player, d);
		}	
	}
	
	public void updateBall(float delta)
	{	
		ball.update(delta, paddleLeft, paddleRight);
		
		gameEntity.setBall(ball.getPosition());
	}
	
	private void register(InetAddress address, int port)
	{
		if(clients.size() < 2)
		{
			clients.add(new NetworkNode(address, port));
			
			DatagramSocket socket = NetworkHelper.getSocket();
			Data data = new Data(Task.CONNECTED);
			
			if (clients.size() == 1)
			{
				NetworkHelper.send(socket, clients.get(0), data);
			}
			
			if (clients.size() == 2)
			{				
				data.setTask(Task.INIT_GAME_LEFT);
				NetworkHelper.send(socket, clients.get(0), data);
				
				data.setTask(Task.INIT_GAME_RIGHT);
				NetworkHelper.send(socket, clients.get(1), data);
				
				gameState = GAME_STATE.STARTED;
				
				//TODO close the socket ? 
			} 
		}
		else
			System.out.println("all players already connected");
	}
	
	public void printClientsInfo()
	{
		for (NetworkNode player : clients) 
		{
			System.out.println("Player address:" + player.ipaddress + " port:" + player.port);
		}
	}
	
	private void updatePaddlesDown(PLAYER player, GameEntity updatedGame)
	{
		float paddleY;
		Vector2 vec;
		
		if (player == PLAYER.LEFT)
		{
			vec = updatedGame.getPaddle1();
			paddleY = gameEntity.getPaddle1().y;
		}
		else  
		{
			vec = updatedGame.getPaddle2();
			paddleY = gameEntity.getPaddle2().y;
		}

		if (vec.y > paddleY)
		{
			System.out.println("[DOWN] -> y nuova : " + vec.y + " y vecchia : " + paddleY);
			//Debug
			//this.gameEntity.setPaddle2(vec);
		}
		else if (vec.y == paddleY)
		{
			System.out.println("UGUALE__");
		} 
		else
		{
			System.out.println("Sto andando giù correttamente");
			
			if (player == PLAYER.LEFT)
			{
				gameEntity.setPaddle1(vec);
				paddleLeft.setPosition(vec.x, vec.y);
			}
			else if (player == PLAYER.RIGHT)
			{
				gameEntity.setPaddle2(vec);
				paddleRight.setPosition(vec.x, vec.y);				
			}
		}
	}
	
	private void updatePaddlesUp(PLAYER player, GameEntity updatedGame)
	{
		float paddleY;
		Vector2 vec;
		
		if (player == PLAYER.LEFT)
		{
			vec = updatedGame.getPaddle1();
			paddleY = gameEntity.getPaddle1().y;
		}
		else  
		{
			vec = updatedGame.getPaddle2();
			paddleY = gameEntity.getPaddle2().y;
		}

		if (vec.y < paddleY)
		{
			System.out.println("[UP] -> y nuova : " + vec.y + " y vecchia : " + paddleY);
			//Debug
			//this.gameEntity.setPaddle2(vec);
		}
		else if (vec.y == paddleY)
		{
			System.out.println("UGUALE__");
		} 
		else
		{
			System.out.println("Sto andando sù correttamente");
			
			if (player == PLAYER.LEFT)
			{
				gameEntity.setPaddle1(vec);
				paddleLeft.setPosition(vec.x, vec.y);
								
			}
			else if (player == PLAYER.RIGHT)
			{
				gameEntity.setPaddle2(vec);
				paddleRight.setPosition(vec.x, vec.y);
			}
		}
	}
	
	private PLAYER getPlayerSide()
	{
		//boolean paddleLeft = false;
		PLAYER player;	
		
		if (clients.get(0).ipaddress.equals(packet.getAddress()) && clients.get(0).port == packet.getPort())
			player = PLAYER.LEFT;
		else if (clients.get(1).ipaddress.equals(packet.getAddress()) && clients.get(1).port == packet.getPort()) 
			player = PLAYER.RIGHT;
		else
			player = PLAYER.NOT_A_BANANA;
		
		return player;
	}
}
