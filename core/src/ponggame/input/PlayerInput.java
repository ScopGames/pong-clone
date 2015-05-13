package ponggame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import ponggame.entity.Paddle;

public abstract class PlayerInput 
{
	public enum layoutInput {WASD, ARROWS};
	
	protected Paddle paddle;
	protected layoutInput layout;
	
	public PlayerInput(Paddle paddle, layoutInput layout)
	{
		this.paddle = paddle;
		this.layout = layout;
	}
	
	public void handleInput(float delta)
	{
		float height = paddle.getHeight();
		float paddleY = paddle.getPosition().y;
		
		if (layout == layoutInput.WASD)
		{
			if (Gdx.input.isKeyPressed(Keys.W) && paddleY + height < Gdx.graphics.getHeight()) 
			{
				onUpKeyPressed(delta);
			}
			else if (Gdx.input.isKeyPressed(Keys.S) && paddleY >= 0)
			{
				onDownKeyPressed(delta);
			}
		}
		else if (layout == layoutInput.ARROWS)
		{
			if (Gdx.input.isKeyPressed(Keys.UP) && paddleY + height < Gdx.graphics.getHeight()) 
			{
				onUpKeyPressed(delta);
			}
			else if (Gdx.input.isKeyPressed(Keys.DOWN) && paddleY >= 0)
			{
				onDownKeyPressed(delta);				
			}
		}
	}
	
	abstract void onDownKeyPressed(float delta);
	abstract void onUpKeyPressed(float delta);
}
