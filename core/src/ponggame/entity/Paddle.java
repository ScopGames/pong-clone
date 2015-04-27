package ponggame.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Paddle extends Sprite
{
	private int width = 20;
	private int height = 100;
	private float movement = 250;
	
	/**
	 * 
	 */
	public Paddle(Color color, Vector2 position) 
	{	
		super(new Sprite());
		
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fillRectangle(0, 0, width, height);
		
		set(new Sprite(new Texture(pixmap)));
		setPosition(position.x, position.y);
	}
	
	public void moveUp(float delta) 
	{
		setPosition(getX(), getY() + delta*movement);		
	}

	public void moveDown(float delta) 
	{
		setPosition(getX(), getY() - delta*movement);
	}
	
	public Vector2 getUpdatedPosition(float delta)
	{
		return new Vector2(getX(), getY() + delta*movement);
	}
}
