package ponggame.renderentities;

import ponggame.entity.Score;
import ponggame.screen.PongGame;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderableScore extends Score
{
	private BitmapFont font;
	
	public RenderableScore()
	{
		font = new BitmapFont();
		//font.scale(1.0f);
	}
	
	
	public void render(SpriteBatch batch)
	{
		String scoreStr = score1 + " : " + score2;
		
		font.draw(batch, scoreStr, PongGame.fieldWidht/2 - 10, PongGame.fieldHeight - 10);
	}
}
