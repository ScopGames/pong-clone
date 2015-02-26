package pongtest.entity;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Ball extends Sprite
{
	private int width = 50;
	private int height = 50;
	
	public Ball()
	{
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		
		pixmap.setColor(1,0,0,1);
		pixmap.fillCircle(25, 25, 24);
		
		this.set(new Sprite(new Texture(pixmap)));
		setPosition(250, 250);
	}
	
	public void update(float delta)
	{
		
	}
}
