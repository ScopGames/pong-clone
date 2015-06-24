/**
 * Main.java our main class 
 * 
 * It manages the Screens in our application
 * 
 */
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
	/**
	 * public enum Screens contains the several screen we can have in the application
	 */
	public enum Screens {PONGGAME, MAINSCREEN, MULTIPLAYER_PONG_GAME};
	
	/**
	 * @see changeScreen
	 */
	@Override
	public void create() 
	{	
		changeScreen(Screens.MAINSCREEN);
	}
	
	/**
	 * Set the screen which is requested 
	 * 
	 * @param newScreen
	 */
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
	
	/**
	 * Starts the game in multiplayer mode 
	 * 
	 * @param socket server's socket
	 * @param server server's ip
	 * @param isPaddleLeft a boolean indicating the side of player's paddle
	 */
	public static void startMultiplayerPong(DatagramSocket socket, 
			NetworkNode server, 
			boolean isPaddleLeft)
	{
		((Game)Gdx.app.getApplicationListener()).setScreen(
				new MultiplayerPong(socket, server, isPaddleLeft));
	}
}
