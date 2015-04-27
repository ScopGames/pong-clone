package ponggame.entity;

import com.badlogic.gdx.math.Vector2;

public class GameEntity {
	
	private Vector2 mpaddle1;
	private Vector2 mpaddle2;
	private Vector2 mball;


	public GameEntity(Vector2 ball, Vector2 paddle1,Vector2 paddle2){
		
		this.mball = ball;
		this.mpaddle1 = paddle1;
		this.mpaddle2 = paddle2;
	}
}