package ponggame.screen;

import ponggame.entity.Ball;
import ponggame.entity.Paddle;
import ponggame.input.PlayerInput;
import ponggame.ui.Score;
import ponggame.ui.Score.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PongGame implements Screen
{
	private SpriteBatch batch;
	private Paddle paddleLeft, paddleRight;
	private Ball ball;
	private Score score;
	private PlayerInput input1, input2;
	
	private final static int winningScore = 10;

	@Override
	public void show() 
	{
		batch = new SpriteBatch();
		
		score = new Score();
		
		initializePaddles();
		input1 = new PlayerInput(paddleLeft, PlayerInput.layoutInput.WASD);
		input2 = new PlayerInput(paddleRight, PlayerInput.layoutInput.ARROWS);
		initializeBall();
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		if (score.getScore(players.PLAYER1) == winningScore)
		{
			Gdx.app.log("player1", "wins");
		}
		else if (score.getScore(players.PLAYER2) == winningScore)
		{
			Gdx.app.log("player2", "wins");
		}
		else
		{
			updateGameEntities(delta);
		}
		
		drawGameEntities();
		score.render(batch);
						
		batch.end();
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

	private void updateGameEntities(float delta) 
	{	
		input1.handleInput(delta);
		input2.handleInput(delta);
		
		ball.update(delta, paddleLeft, paddleRight);
		
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
	public void dispose() {
	}
	
	private void initializeBall() 
	{
		float speed = 300;
		
		float paddleWidth = paddleLeft.getWidth();
		Vector2 position = new Vector2(paddleWidth + MathUtils.random(200), 
				MathUtils.random(50, Gdx.graphics.getHeight()-50));
		Vector2 velocity = new Vector2(speed, speed*MathUtils.randomSign());
		
		ball = new Ball(position, velocity);
	}
	
	private void initializePaddles()
	{
		// Left paddle
		paddleLeft = new Paddle(new Color(1,0,0,1), new Vector2(0,200));
		
		// Right paddle
		paddleRight = new Paddle(new Color(0,1,0,1), new Vector2(Gdx.app.getGraphics().getWidth()-20,200));
	}

	@Override
	public void hide() {		
	}
}
