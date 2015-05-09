package ponggame.renderentities;

import ponggame.entity.Paddle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class RenderablePaddle extends Paddle
{
	// __Inherited attributes__
	// protected int width = 20;
	// protected int height = 100;
	// protected Vector2 position;
	
	Texture texture;
	
	public RenderablePaddle(Vector2 position, Color color) 
	{
		super(position);
		
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fillRectangle(0, 0, width, height);
		
		texture = new Texture(pixmap);
		this.position.set(position.x, position.y);
	}	
	
	public void draw(SpriteBatch batch)
	{
		batch.draw(texture, position.x, position.y, width, height);
	}
}
