package ponggame.renderentities;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ponggame.entity.Ball;

public class RenderableBall extends Ball
{
	// __Inherited member attributes__
	// protected int width = 20;
	// protected int height = 20;
	// protected Vector2 position;
	// protected Vector2 velocity;
	
	private Texture texture;
	
	public RenderableBall(Vector2 position, Vector2 velocity) 
	{
		super(position, velocity);
		
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(1,0,0,1);
		pixmap.fillCircle(width/2, height/2, width/2 - 1);
		
		texture = new Texture(pixmap);
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.draw(texture, position.x, position.y, width, height);
	}
}
