/**
 * GameEntity.java
 * 
 * Class to manage the entities of the game
 */
package ponggame.entity;


import ponggame.entity.Score.players;

import com.badlogic.gdx.math.Vector2;

public class GameEntity {
		
	/**
	 * Left paddle
	 */
	private Vector2 paddle1;
	/**
	 * Right paddle
	 */
	private Vector2 paddle2;
	/**
	 * Ball
	 */
	private Vector2 ball;
	/**
	 * Information about the score
	 */
	private Vector2 score;
	
	/**
	 * Empty constructor needed for json serialize & deserialize
	 */
	public GameEntity()
	{
		
	}
	
	/**
	 * Constructor
	 * 
	 * Create an object with information about game entities
	 * @param ball
	 * @param paddle1
	 * @param paddle2
	 */
	public GameEntity(Vector2 ball, Vector2 paddle1, Vector2 paddle2)
	{
		this.ball = ball;
		this.paddle1 = paddle1;
		this.paddle2 = paddle2;
	}

	
	public Vector2 getPaddle1() 
	{
		return paddle1;
	}

	public void setPaddle1(Vector2 paddle1) 
	{
		this.paddle1 = paddle1;
	}

	public Vector2 getPaddle2() 
	{
		return paddle2;
	}

	public void setPaddle2(Vector2 paddle2) 
	{
		this.paddle2 = paddle2;
	}

	public Vector2 getBall() 
	{
		return ball;
	}

	public void setBall(Vector2 ball) 
	{
		this.ball = ball;
	}

	public Vector2 getScore() 
	{
		return score;
	}

	public void setScore(Score score) 
	{
		this.score = new Vector2(score.getScore(players.PLAYER1), score.getScore(players.PLAYER2));
	}
	
	/**
	 * Method used to understand if the GameEntity contains a score
	 * 
	 * @return boolean
	 */
	public boolean containScore()
	{
		return (this.score != null);
	}
}