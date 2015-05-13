package ponggame.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Paddle
{
	protected int width = 20;
	protected int height = 100;
	protected Vector2 position;
	
	private float movement = 250; // TODO refactor "movement" to "speed"
	
	/**
	 * 
	 */
	public Paddle(Vector2 position) 
	{	
		this.position = position;
	}
	
	public void moveUp(float delta) 
	{
		position.y += delta*movement;
	}

	public void moveDown(float delta) 
	{
		position.y -= delta*movement;
	}
	
	public float getMovement()
	{
		return movement;
	}
	
	public Rectangle getBoundingRec()
	{		
		return new Rectangle(position.x, position.y, width, height);
	}
	
	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
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
	
	/*public Vector2 getUpdatedPosition(float delta)
	{
		return new Vector2(getX(), getY() + delta*movement);
	}*/
}
