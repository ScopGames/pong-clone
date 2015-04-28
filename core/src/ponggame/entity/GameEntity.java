package ponggame.entity;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class GameEntity implements Serializable {
	
	// TODO check this
	private static final long serialVersionUID = 1L;
	
	/**
	 * Left paddle
	 */
	private Vector2 paddle1;
	/**
	 * Right paddle
	 */
	private Vector2 paddle2;
	private Vector2 ball;
	
	boolean p1, p2;

	public GameEntity() {
		// empty constructor
	}
	
	public GameEntity(Vector2 ball, Vector2 paddle1,Vector2 paddle2){
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
}