package pongtest;

import com.badlogic.gdx.ApplicationAdapter;

public class Main extends ApplicationAdapter 
{
	PongGame mainGame;
	
	@Override
	public void create () 
	{
		mainGame = new PongGame();
		mainGame.create();
	}

	@Override
	public void render () 
	{
		mainGame.render();
	}
}
