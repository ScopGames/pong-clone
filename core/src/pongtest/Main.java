package pongtest;

import pongtest.ui.MainMenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game
{
	public enum Screens {PONGGAME, MAINSCREEN};
	
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
		default:
			break;
		}		
	}
}
