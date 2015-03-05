package pongtest.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	boolean inputEnabled = false;
	
	
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
	
	public void enableInput() 
	{
		this.inputEnabled = true;
	}
	
	public void update(float delta)
	{
		if (inputEnabled)
			handleInput(delta);
	}
	
	/**
	 * 
	 * @param delta
	 */
	private void handleInput(float delta) 
	{				
		if (Gdx.input.isKeyPressed(Keys.W) && getY()+height < Gdx.graphics.getHeight()) 
		{
			setPosition(getX(), getY()+delta*movement);
		}
		else if (Gdx.input.isKeyPressed(Keys.S) && getY() >= 0)
		{
			setPosition(getX(), getY()-delta*movement);
		}
	}
}
