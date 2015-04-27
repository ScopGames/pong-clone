package ponggame.input;

import ponggame.entity.Paddle;

public class LocalPlayerInput extends PlayerInput {

	public LocalPlayerInput(Paddle paddle, layoutInput layout) 
	{
		super(paddle, layout);
	}

	@Override
	void onDownKeyPressed(float delta) 
	{
		paddle.moveDown(delta);
	}

	@Override
	void onUpKeyPressed(float delta) 
	{
		paddle.moveUp(delta);
	}
}
