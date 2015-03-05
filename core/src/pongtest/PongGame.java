package pongtest;

import pongtest.entity.Ball;
import pongtest.entity.Paddle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PongGame implements ApplicationListener
{
	SpriteBatch batch;
	Paddle paddleLeft, paddleRight;
	Ball ball,b,b2;

	@Override
	public void create() 
	{
		batch = new SpriteBatch();
		
		initializeBall();
		initializePaddles();		
	}

	@Override
	public void render() 
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		updateGameEntities();
		drawGameEntities();
		
		if (ballLost())
			initializeBall();
				
		batch.end();
	}
	
	private boolean ballLost() {
		boolean lost = false;
		
		float x = ball.getX();
		
		if (x + ball.getWidth() < 0 || x > Gdx.graphics.getWidth()) 
			lost = true;
		
		return lost;
	}

	private void updateGameEntities() {
		float delta = Gdx.graphics.getDeltaTime();
		
		paddleLeft.update(delta);
		paddleRight.update(delta);
		
		ball.update(delta, paddleLeft, paddleRight);
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
		float speed = 250;
		
		Vector2 position = new Vector2(MathUtils.random(200), MathUtils.random(250));
		Vector2 velocity = new Vector2(speed, speed*MathUtils.randomSign());
		
		ball = new Ball(position, velocity);
	}
	
	private void initializePaddles()
	{
		// Left paddle
		paddleLeft = new Paddle(new Color(1,0,0,1), new Vector2(0,200));
		paddleLeft.enableInput();
		
		// Right paddle
		paddleRight = new Paddle(new Color(0,1,0,1), new Vector2(Gdx.app.getGraphics().getWidth()-20,200));
		paddleRight.enableInput();
	}
}
