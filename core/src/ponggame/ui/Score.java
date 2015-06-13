package ponggame.ui;

import ponggame.screen.PongGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Score 
{
	public enum players {PLAYER1, PLAYER2};
	
	private int score1 = 0, score2 = 0;
	private BitmapFont font;
	
	public Score()
	{
		font = new BitmapFont();
		//font.scale(1.0f);
	}
	
	public void addPoint(players winner)
	{
		switch (winner) {
		case PLAYER1:
			score1 += 1;
			break;
		
		case PLAYER2:
			score2 += 1;
			break;
		
		default:
			break;
		}
	}
	
	public void render(SpriteBatch batch)
	{
		String scoreStr = score1 + " : " + score2;
		
		font.draw(batch, scoreStr, PongGame.fieldWidht/2 - 10, PongGame.fieldHeight - 10);
	}

	public int getScore(players player) {
		int score = 0;
		
		switch (player) {
		case PLAYER1:
			score = score1;
			break;
		
		case PLAYER2:
			score = score2;
			break;
		
		default:
			break;
		}
		
		return score;
	}
}
