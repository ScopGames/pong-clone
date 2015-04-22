package ponggame.screen;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import ponggame.entity.Ball;
import ponggame.entity.Paddle;
import ponggame.input.PlayerInput;
import ponggame.input.RemotePlayerInput;
import ponggame.ui.Score;
import ponggame.ui.Score.players;
import pongserver.utility.NetworkHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MultiplayerPong implements Screen {

	private DatagramSocket msocket;
	private InetAddress serverAddress;
	
	private SpriteBatch batch;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;
	private Score score;
	private PlayerInput input1;
	private FPSLogger fpsLogger;
	
	public MultiplayerPong(DatagramSocket socket) 
	{
		this.msocket = socket;
	}
	
	@Override
	public void show() 
	{
		batch = new SpriteBatch();
		score = new Score();
		
		initializePaddles();
		initializeBall();
		

		//TODO check why this line crashes...
		//DatagramSocket socket = NetworkHelper.getSocket(port);
		
		System.out.println("listening on " + msocket.getLocalAddress() + " " + msocket.getLocalPort());
		
		syncFromServer();
		
		Thread t = new Thread(new Runnable() 
		{		
			@Override
			public void run() 
			{
				while(true)
					syncFromServer();
			}
		});
		t.start();
		
		// serverAddress is setted by the syncFromServer().
		input1 = new RemotePlayerInput(paddleLeft, 
				PlayerInput.layoutInput.WASD, 
				msocket,
				serverAddress);

		fpsLogger = new FPSLogger();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
				
		updateGameEntities(delta);
		
		drawGameEntities();
			
		//score.render(batch);
		fpsLogger.log();
		
		batch.end();
	}	
	
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
	
	private void initializePaddles()
	{
		// Left paddle
		paddleLeft = new Paddle(new Color(1,0,0,1), new Vector2());
		
		// Right paddle
		paddleRight = new Paddle(new Color(0,1,0,1), new Vector2());
	}
	
	private void initializeBall() 
	{		
		Vector2 dummy = new Vector2();
		
		ball = new Ball(dummy, dummy);
	}
	
	private void updateGameEntities(float delta) 
	{	
		input1.handleInput(delta);
				
		if (ballLost())
		{
			updateUI();
			initializeBall();
		}
	}
	
	private void drawGameEntities() {
		paddleLeft.draw(batch);
		paddleRight.draw(batch);
		ball.draw(batch);		
	}
	
	private void updateUI()
	{
		if (ball.getVelocity().x < 0f)
			score.addPoint(players.PLAYER2);
		else
			score.addPoint(players.PLAYER1);
	}

	private boolean ballLost() 
	{
		boolean lost = false;
		
		float x = ball.getX();
		
		if (x + ball.getWidth() < 0 || x > Gdx.graphics.getWidth()) 
			lost = true;
		
		return lost;
	}
	
	/**
	 * Sync the game's state with the server
	 */
	private void syncFromServer()
	{
		DatagramPacket packet = NetworkHelper.receive(msocket);
		
		if (serverAddress == null) // if serverAddress is not set
			serverAddress = packet.getAddress();
		
		try
		{
			ArrayList<Vector2> gameData = (ArrayList<Vector2>) NetworkHelper.deserialize(packet.getData());
			
			ball.setPosition(gameData.get(0).x, gameData.get(0).y);
			paddleLeft.setPosition(gameData.get(1).x, gameData.get(1).y);
			paddleRight.setPosition(gameData.get(2).x, gameData.get(2).y);
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
}
