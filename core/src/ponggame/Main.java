package ponggame;

import ponggame.screen.PongGame;
import ponggame.screen.MultiplayerPong;
import ponggame.ui.MainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game
{
	public enum Screens {PONGGAME, MAINSCREEN, MULTIPLAYER_PONG_GAME};
	
	@Override
	public void create() 
	{	
		//mainMenu = new MainMenu();
		changeScreen(Screens.MAINSCREEN);
	}
	
	public static void changeScreen(Screens newScreen)
	{
		switch(newScreen){
		case MAINSCREEN:
			((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
			break;
		
		case PONGGAME:
			((Game)Gdx.app.getApplicationListener()).setScreen(new PongGame());
			break;
		
		case MULTIPLAYER_PONG_GAME:
			((Game)Gdx.app.getApplicationListener()).setScreen(new MultiplayerPong());
			break;
			
		default:
			break;
		}		
	}
}
