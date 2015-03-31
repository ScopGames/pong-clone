package ponggame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import ponggame.entity.Paddle;

public class PlayerInput 
{
	public enum layoutInput {WASD, ARROWS};
	
	private Paddle paddle;
	private layoutInput layout;
	
	public PlayerInput(Paddle paddle, layoutInput layout)
	{
		this.paddle = paddle;
		this.layout = layout;
	}
	
	public void handleInput(float delta)
	{
		float height = paddle.getHeight();
		float paddleY = paddle.getY();
		
		if (layout == layoutInput.WASD)
		{
			if (Gdx.input.isKeyPressed(Keys.W) && paddleY + height < Gdx.graphics.getHeight()) 
			{
				paddle.moveUp(delta);
			}
			else if (Gdx.input.isKeyPressed(Keys.S) && paddleY >= 0)
			{
				paddle.moveDown(delta);
			}
		}
		else if (layout == layoutInput.ARROWS)
		{
			if (Gdx.input.isKeyPressed(Keys.UP) && paddleY + height < Gdx.graphics.getHeight()) 
			{
				paddle.moveUp(delta);
			}
			else if (Gdx.input.isKeyPressed(Keys.DOWN) && paddleY >= 0)
			{
				paddle.moveDown(delta);
			}
		}
	}
}
