package ponggame.entity;

public class Score {
	
	
	public enum players {PLAYER1, PLAYER2};
	protected int score1 = 0, score2 = 0;
	
	
	
	public void addPoint(players winner)
	{
		switch (winner) 
		{
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
	
	public int getScore(players player) 
	{
		int score = 0;
		
		switch (player) 
		{
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
