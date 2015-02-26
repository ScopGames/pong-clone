package pongtest;

import pongtest.entity.Ball;
import pongtest.entity.Paddle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PongGame implements ApplicationListener
{
	SpriteBatch batch;
	Paddle paddleLeft, paddleRight;
	Ball ball;

	@Override
	public void create() {
		batch = new SpriteBatch();
		
		ball = new Ball();
		
		paddleLeft = new Paddle(new Color(1,0,0,1), new Vector2(0,200));
		paddleLeft.enableInput();
		
		paddleRight = new Paddle(new Color(0,1,0,1), new Vector2(Gdx.app.getGraphics().getWidth()-20,200));
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		paddleLeft.update(Gdx.graphics.getDeltaTime());
		paddleLeft.draw(batch);
		paddleRight.update(Gdx.graphics.getDeltaTime());
		paddleRight.draw(batch);
		
		ball.draw(batch);
		
		batch.end();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
