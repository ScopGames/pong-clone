/**
 * MultiplayerPong.java 
 * 
 * this class is responsable of the game between two clients
 */
package ponggame.screen;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import ponggame.input.PlayerInput;
import ponggame.input.RemotePlayerInput;
import ponggame.renderentity.RenderableBall;
import ponggame.renderentity.RenderablePaddle;
import ponggame.renderentity.RenderableScore;
import ponggame.entity.Score.players;
import pongserver.utility.Data;
import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;
import pongserver.utility.NetworkNode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MultiplayerPong implements Screen {

	private DatagramSocket socket;
	private NetworkNode server;
	
	private DatagramPacket packet;
	private SpriteBatch batch;
	private RenderablePaddle paddleLeft, paddleRight;
	private RenderableBall ball;
	private RenderableScore score;
	private RemotePlayerInput input;
	private FPSLogger fpsLogger;
	private boolean isPaddleLeft;
	
	private OrthographicCamera camera;
	private Viewport viewport;
	
	boolean firstSync= true;
	
	/**
	 * Constructor
	 * 
	 * @param socket client's port
	 * @param server server's informations
	 * @param isPaddleLeft boolean with the side of client's paddle
	 * 
	 */
	public MultiplayerPong(DatagramSocket socket, NetworkNode server, 
			boolean isPaddleLeft) 
	{
		this.socket = socket;
		this.server = server;
		this.isPaddleLeft = isPaddleLeft;
	}
	
	
	/**
	 * Show()
	 * it handles graphics and create a new thread which receive packet from server 
	 * 
	 * @see #syncFromServer(Data)
	 * 
	 */
	@Override
	public void show() 
	{
		initializePaddles();
		initializeBall();
		
		
		if (isPaddleLeft)
		{
			input = new RemotePlayerInput(paddleLeft, 
					PlayerInput.layoutInput.WASD, 
					socket,
					server.ipaddress,
					isPaddleLeft);
		}
		else
		{
			input = new RemotePlayerInput(paddleRight, 
					PlayerInput.layoutInput.WASD, 
					socket,
					server.ipaddress,
					isPaddleLeft);
		}
		
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera(); // camera points at (0,0)
		camera.translate(PongGame.fieldWidht/2, PongGame.fieldHeight/2); // centers the camera
		viewport = new StretchViewport(PongGame.fieldWidht, PongGame.fieldHeight, camera);
		
		// still not used
		score = new RenderableScore();
		
		System.out.println("listening on " + socket.getLocalAddress() + " " + socket.getLocalPort());
				
		Thread t = new Thread(new Runnable() 
		{		
			@Override
			public void run() 
			{
				while(true)
				{
					packet = NetworkHelper.receive(socket);
					byte[] buffer = packet.getData();
					Json json = new Json();
					String data = new String(buffer);
					data = data.trim();
					Data gameData = json.fromJson(Data.class, data);
					
					if (gameData.getTask() == Task.UPDATE_GAME_ENTITIES)
						syncFromServer(gameData);
				}
			}
		});
		t.start();

		fpsLogger = new FPSLogger();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera = (OrthographicCamera) viewport.getCamera();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
			
		updateGameEntities(delta);
		
		drawGameEntities();
			
		//score.render(batch);
		//fpsLogger.log();
		
		batch.end();
	}	
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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
	
	/**
	 * initialize the graphics of paddles
	 */
	private void initializePaddles()
	{
		// Left paddle
		paddleLeft = new RenderablePaddle(new Vector2(), new Color(1,0,0,1));
		
		// Right paddle
		paddleRight = new RenderablePaddle(new Vector2(), new Color(0,1,0,1));
	}
	
	/**
	 * initialize the graphics of the ball with a  fake position
	 */
	private void initializeBall() 
	{		
		Vector2 dummy = new Vector2();
		
		ball = new RenderableBall(dummy, dummy);
	}
	
	
	/**
	 * handle client's input 
	 * @param delta render time 
	 */
	private void updateGameEntities(float delta) 
	{	
		input.handleInput(delta);
				
		/*if (ballLost())
		{
			updateUI();
			initializeBall();
		}*/
	}
	
	/**
	 * Draw graphics
	 */
	private void drawGameEntities() {
		paddleLeft.draw(batch);
		paddleRight.draw(batch);
		ball.draw(batch);		
	}
	
	/**
	 * update score graphics
	 */
	private void updateUI()
	{
		if (ball.getVelocity().x < 0f)
			score.addPoint(players.PLAYER2);
		else
			score.addPoint(players.PLAYER1);
	}

	/**
	 * detect if some player has scored
	 * 
	 * @return boolen
	 */
	private boolean ballLost() 
	{
		boolean lost = false;
		
		float x = ball.getPosition().x;
		
		if (x + ball.getWidth() < 0 || x > Gdx.graphics.getWidth()) 
			lost = true;
		
		return lost;
	}
	
	/**
	 * Sync the game's state with the server
	 * 
	 * @param gameData the data from server
	 * @see  pongserver.utility.Data
	 */
	private void syncFromServer(Data gameData)
	{		
		
		// if (gameData.containScore() == true)
		{
			// update score 
			// and send ack containing current score to server
		}
			
		Vector2 vBall = gameData.getGameEntity().getBall();
		ball.setPosition(vBall.x, vBall.y);
		Vector2 vPaddle1 = gameData.getGameEntity().getPaddle1();
		Vector2 vPaddle2 = gameData.getGameEntity().getPaddle2();
		
		boolean firstSync= true;
		if(firstSync == true)
		{
			paddleLeft.setPosition(vPaddle1.x, vPaddle1.y);
			paddleRight.setPosition(vPaddle2.x, vPaddle2.y);
			firstSync = false;
		}
		
		if(isPaddleLeft)
		{
			if((input.getLastTask() == Task.GOING_DOWN && vPaddle1.y < input.getTmpPosition().y)||
					(input.getLastTask() == Task.GOING_UP && vPaddle1.y > input.getTmpPosition().y))
			{
				// update the paddle with the position sent from the server
				paddleLeft.setPosition(vPaddle1.x, vPaddle1.y);
			}
			else if ((input.getLastTask() == Task.GOING_DOWN && vPaddle1.y > input.getTmpPosition().y)||
					(input.getLastTask() == Task.GOING_UP && vPaddle1.y < input.getTmpPosition().y))
			{
				// update the paddle with the local new Y. This will ignore the 
				// position sent from the server which is not correct due to 
				// UDP latency and not ordered packets.
				paddleLeft.setPosition(vPaddle1.x, input.getTmpPosition().y);
			}
			else
			{
				// no movement
			}
		}
		else
		{				
			if((input.getLastTask() == Task.GOING_DOWN && vPaddle2.y < input.getTmpPosition().y)||
					(input.getLastTask() == Task.GOING_UP && vPaddle2.y > input.getTmpPosition().y))
			{
				paddleRight.setPosition(vPaddle2.x, vPaddle2.y);
			}
			else if ((input.getLastTask() == Task.GOING_DOWN && vPaddle2.y > input.getTmpPosition().y)||
					(input.getLastTask() == Task.GOING_UP && vPaddle2.y < input.getTmpPosition().y))
			{
				paddleRight.setPosition(vPaddle2.x, input.getTmpPosition().y);
			}
			else
			{
				// no movement
			}
		}		
	}
}
