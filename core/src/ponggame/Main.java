package ponggame;

import java.net.DatagramSocket;

import ponggame.screen.MainMenu;
import ponggame.screen.PongGame;
import ponggame.screen.MultiplayerPong;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game
{
	public enum Screens {PONGGAME, MAINSCREEN, MULTIPLAYER_PONG_GAME};
	
	@Override
	public void create() 
	{	
		changeScreen(Screens.MAINSCREEN);
	}
	
	public static void changeScreen(Screens newScreen)
	{
		switch(newScreen)
		{
			case MAINSCREEN:
				((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
				break;
			
			case PONGGAME:
				((Game)Gdx.app.getApplicationListener()).setScreen(new PongGame());
				break;
			
			//case MULTIPLAYER_PONG_GAME:
				//((Game)Gdx.app.getApplicationListener()).setScreen(new MultiplayerPong());
				//break;
				
			default:
				break;
		}		
	}
	
	public static void startMultiplayerPong(DatagramSocket socket)
	{
		((Game)Gdx.app.getApplicationListener()).setScreen(new MultiplayerPong(socket));
	}
}
