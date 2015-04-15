package ponggame.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ball extends Sprite
{
	private int width = 20;
	private int height = 20;
	private Vector2 velocity;
	
	private boolean overlapped = false;
	//private rotationSpeed = 0;
	
	public Ball()
	{			
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(1,0,0,1);
		pixmap.fillCircle(width/2, height/2, width/2 - 1);
		this.set(new Sprite(new Texture(pixmap)));
	}
	
	public Ball(Vector2 position, Vector2 velocity)
	{			
		this();
		
		setPosition(position.x, position.y);
		this.velocity = velocity;
	}
	
	public void update(float delta, Paddle paddleL, Paddle paddleR)
	{	
		float margin = 10;
		
		if (!overlapped && (collisionPaddle(paddleL) || collisionPaddle(paddleR)))
		{
			overlapped = true;
			
			// because it overlapped in the area between the two paddles
			velocity.x = -velocity.x;
			
			// if overlapped behind the paddle
			if (getX() + margin < paddleL.getX() + paddleL.getWidth() ||
				getX() - margin + width > paddleR.getX())
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
				
		setPosition(getX() + velocity.x*delta, getY() + velocity.y*delta);
	}
		
	public Vector2 getVelocity() 
	{
		return velocity;
	}

	private boolean collisionPaddle(Paddle paddle) 
	{
		boolean collision = false;
		
		Rectangle paddleCollision = paddle.getBoundingRectangle();
		
		collision = paddleCollision.overlaps(this.getBoundingRectangle());
		
		return collision;
	}
	
	private boolean collisionBottom()
	{
		boolean collision = false;
	
		if (getY()<= 0)
			collision = true;
	
		return collision;
	}
	
	private boolean collisionTop()
	{
		boolean collision = false;
		float screenHeight = Gdx.graphics.getHeight();
		
		if (getY() + height >= screenHeight)
			collision = true;
		
		return collision;
	}
}
