package ponggame.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ball
{
	protected int width = 20;
	protected int height = 20;
	protected Vector2 position;
	protected Vector2 velocity;
	
	private boolean overlapped = false;
	//private rotationSpeed = 0;
	
	public Ball()
	{			
		this.position = new Vector2(0, 0);	
		this.velocity = new Vector2(0, 0);
	}
	
	public Ball(Vector2 position, Vector2 velocity)
	{					
		// deep copies
		this.position = position;	
		this.velocity = velocity;
	}
	
	public void update(float delta, Paddle paddleL, Paddle paddleR)
	{	
		float margin = 10;
				
		if (!overlapped && (collisionPaddle(paddleL) || 
				collisionPaddle(paddleR)))
		{
			overlapped = true;
			
			// because it overlapped in the area between the two paddles
			velocity.x = -velocity.x;
			
			// if overlapped behind the paddle
			if (position.x + margin < paddleL.getPosition().x + paddleL.getWidth() ||
				position.x - margin + width > paddleR.getPosition().y)
			{
				velocity.y = -velocity.y;	
			}
		}
		else
		{
			if (!collisionPaddle(paddleL) && !collisionPaddle(paddleR))
			{
				overlapped = false;
			}
		}
		
		if (collisionTop() || collisionBottom())
		{
			// invert the direction on the y axis
			velocity.y = -velocity.y;
		}
		
		position.set(position.x + velocity.x*delta, position.y + velocity.y*delta);
	}
		
	public Vector2 getVelocity() 
	{
		return velocity;
	}
	
	public Vector2 getPosition() 
	{
		return position;
	}
	
	public float getWidth()
	{
		return width;
	}
	
	public float getHeight()
	{
		return height;
	}
	
	private boolean collisionPaddle(Paddle paddle) 
	{
		boolean collision = false;
		
		// TODO check if this cause memory problems/leaks
		Rectangle ballBoundingRec = new Rectangle(position.x, position.y, width, height);
		
		collision = paddle.getBoundingRec().overlaps(ballBoundingRec);
		
		return collision;
	}
	
	private boolean collisionBottom()
	{
		boolean collision = false;
	
		if (position.y <= 0)
			collision = true;
	
		return collision;
	}
	
	private boolean collisionTop()
	{
		boolean collision = false;
		float screenHeight = Gdx.graphics.getHeight();
		
		if (position.y + height >= screenHeight)
			collision = true;
		
		return collision;
	}
}
