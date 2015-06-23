package ponggame;

import java.net.DatagramSocket;

import ponggame.screen.MainMenu;
import ponggame.screen.PongGame;
import ponggame.screen.MultiplayerPong;
import pongserver.utility.NetworkNode;

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
			default:
				break;
		}		
	}
	
	public static void startMultiplayerPong(DatagramSocket socket, 
			NetworkNode server, 
			boolean isPaddleLeft)
	{
		((Game)Gdx.app.getApplicationListener()).setScreen(
				new MultiplayerPong(socket, server, isPaddleLeft));
	}
}
