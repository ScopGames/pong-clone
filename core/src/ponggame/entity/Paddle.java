package ponggame.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Paddle
{
	protected int width = 20;
	protected int height = 100;
	protected Vector2 position;
	private Rectangle boundingRec;
	
	private float movement = 250; // TODO refactor "movement" to "speed"
	
	/**
	 * 
	 */
	public Paddle(Vector2 position) 
	{	
		this.position = position;
		boundingRec = new Rectangle(position.x, position.y, width, height);
	}
	
	public void moveUp(float delta) 
	{
		position.y += delta*movement;
		boundingRec = new Rectangle(position.x, position.y, width, height);
	}

	public void moveDown(float delta) 
	{
		position.y -= delta*movement;
		boundingRec = new Rectangle(position.x, position.y, width, height);
	}
	
	public float getMovement()
	{
		return movement;
	}
	
	public Rectangle getBoundingRec()
	{
		return boundingRec;
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
