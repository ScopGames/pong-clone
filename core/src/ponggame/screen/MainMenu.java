/**
 * MainMenu.java  
 * 
 * show the graphics of main menu and listen for player's input
 */
package ponggame.screen;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import ponggame.Main;
import ponggame.Main.Screens;
import ponggame.entity.GameEntity;
import ponggame.utility.Tween;
import pongserver.PongServer;
import pongserver.utility.Data;
import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;
import pongserver.utility.NetworkNode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;

public class MainMenu implements Screen, InputProcessor {
	private Stage stage = new Stage();
	private TextureAtlas atlas;
	private Table mainTable, multiplayerTable;
	private TextButton singlePlayerButton, multiplayerButton, submitIpButton;
	private TextField ipInput;
	private Label ipLabel;
	private static Label connectionStatusLabel;
	private Skin skin;
	private BitmapFont mainFont;
	private Action showMultiplayerMenu, hideMultiplayerMenu;
	private static Task threadTask;
	private static NetworkNode server;
	DatagramSocket socket = NetworkHelper.getSocket();
	
	/**
	 *  
	 */
	@Override
	public void show() 
	{
		Gdx.input.setInputProcessor(stage);
		
		// initialize table field
		initializeActions();
		createUI();
		
		stage.addActor(mainTable);
		stage.addActor(multiplayerTable);
	}

	/**
	 * 
	 */
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}
	
	/**
	 *  Creates the user interface with buttons listeners and a label and change the screen to multiplayerPong
	 *  
	 */
	private void createUI()
	{
		atlas = new TextureAtlas(Gdx.files.internal("ui/img/buttons.pack"));
		skin = new Skin(atlas);
		
		mainFont = new BitmapFont(Gdx.files.internal("ui/font/kongfont.fnt"));
		mainFont.setScale(0.38f); // maybe we should reduce 
								  // the export font size ?
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button");
		textButtonStyle.down = skin.getDrawable("button_afa");
		textButtonStyle.font = mainFont;
		
		singlePlayerButton = new TextButton("Single Player", textButtonStyle);
		singlePlayerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Main.changeScreen(Screens.PONGGAME);
			}
		});
		
		multiplayerButton = new TextButton("Multiplayer", textButtonStyle);
		
		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.font = mainFont;
		textFieldStyle.fontColor = new Color(1, 1, 0, 1);
		textFieldStyle.background = skin.getDrawable("button");
		textFieldStyle.cursor = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/cursor.png"))));
		
		LabelStyle labelStyle = new LabelStyle(mainFont, Color.WHITE);
		ipLabel = new Label("IP del server:", labelStyle);

		ipInput = new TextField("127.0.0.1", textFieldStyle);
		ipInput.setTextFieldFilter(new TextFieldFilter() {
			@Override
			public boolean acceptChar(TextField textField, char c) {
				boolean accepted = false;
				
				if (! Character.isAlphabetic(c)) {
					accepted = true;
				}
				
				// ip address are 12 digits + 3 points
				if (ipInput.getText().length() >= 15)
					accepted = false;
							
				return accepted;
			}
		});
		
		submitIpButton = new TextButton("Submit", textButtonStyle);		
		submitIpButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				String ipAddress = ipInput.getText();
				server = new NetworkNode(ipAddress, PongServer.DEFAULT_PORT);
				connectToServer(server);
			}
		});
		
		connectionStatusLabel = new Label("", labelStyle);
		
		multiplayerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				
				if(!multiplayerTable.isVisible()) {
					stage.addAction(showMultiplayerMenu);
				}
				else {
					stage.addAction(hideMultiplayerMenu);
				}
			}
		});
				
		mainTable = new Table();
		multiplayerTable = new Table();
		
		mainTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		multiplayerTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		mainTable.add(singlePlayerButton);
		mainTable.row();
		mainTable.add(multiplayerButton);
		
		// this is needed to set the size of buttons that are inside a table
		for (Cell cell : mainTable.getCells())
		{
			cell.minWidth(180);
		}
	
		multiplayerTable.align(Align.left);
		multiplayerTable.add(ipLabel);
		multiplayerTable.row();
		multiplayerTable.add(ipInput);
		multiplayerTable.row();
		multiplayerTable.add(submitIpButton);
		multiplayerTable.row();
		multiplayerTable.add(connectionStatusLabel);
		multiplayerTable.moveBy(mainTable.getWidth()/2, 0);
		//multiplayerTable.debug();
		multiplayerTable.setVisible(false);
		
		stage.addAction(new Action() 
		{
			@Override
			public boolean act(float delta) 
			{
				boolean done = false;
				
				if (threadTask == Task.INIT_GAME_LEFT)
				{
					done = true;
					Main.startMultiplayerPong(socket, server, true);
				}
				else if(threadTask == Task.INIT_GAME_RIGHT)
				{
					done = true;
					Main.startMultiplayerPong(socket, server, false);
				}
				
				return done;
			}
		});
	}
	
	/**
	 * Manage the transitions and animation in main menu
	 * 	 
	*/
	private void initializeActions() 
	{
		final Tween menuTweenShow = new Tween(Interpolation.exp5Out, 0, -150, 1.0f);
		//menuTweenShow.setRepeatMode(RepeatMode.REPEAT_PINGPONG);
		final Tween menuTweenHide = new Tween(Interpolation.exp5Out, -150, 0, 1.0f);
		
		showMultiplayerMenu = new Action() 
		{
			@Override
			public boolean act(float delta) 
			{
				boolean done = false;
				menuTweenShow.update(delta);
				
				multiplayerTable.setVisible(true);
				
				if (!menuTweenShow.finished())
				{
					mainTable.setX(menuTweenShow.getValue());
					multiplayerTable.setColor(1, 1, 1, menuTweenShow.getPercentage());
				}
				else
				{					
					menuTweenShow.reset();
					done = true;
				}
												
				return done;
			}
		};
		
		hideMultiplayerMenu = new Action() 
		{
			@Override
			public boolean act(float delta) 
			{
				boolean done = false;
					
				menuTweenHide.update(delta);
				
				if (!menuTweenHide.finished())
				{
					mainTable.setX(menuTweenHide.getValue());
					multiplayerTable.setColor(0, 0, 0, 1 - menuTweenHide.getPercentage());
				}
				else
				{
					multiplayerTable.setVisible(false);
					menuTweenHide.reset();
					done = true;	
				}
												
				return done;
			}
		};
	}
	
	
	/**
	 * Manages the connection to server 
	 * @param NetworkNode contains information about the server 
	 */
	private void connectToServer(NetworkNode p)
	{
		Data data = new Data(Task.REGISTER_PLAYER, new GameEntity());
		NetworkHelper.send(socket, p, data);
		System.out.println("MainMenu : Packet sent");
		connectionStatusLabel.setText("Packet sent");
		
		submitIpButton.setTouchable(Touchable.disabled);
		
		final Thread t = new Thread(new ReceivingHandler(socket, connectionStatusLabel));
		t.start();

		Timer timer = new Timer();			
		float timeout = 5; // seconds
			
		// TODO check if this works
		timer.scheduleTask(new Timer.Task() 
		{
			@Override
			public void run() 
			{				
				// If the server doesn't respond in 5 seconds, and the thread is
				//alive, then interrupt the thread.
				if(threadTask != Task.CONNECTED && t.isAlive())
				{ 
					t.interrupt();
					connectionStatusLabel.setText("No repsonse...");
					System.out.println("Thread stopped");
					submitIpButton.setTouchable(Touchable.enabled);
				}				
				//else means that: 
				// - one player is connected and he is waiting for the other one
				// to connect, doesn't interrupt the thread. 
				// or
				// -  both players are connected, the thread is finished.
			}
		}, timeout);
	}
	
	@Override
	public void resize(int width, int height) 
	{	
		this.stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		mainFont.dispose();
		atlas.dispose();
		skin.dispose();
		System.out.println("MainMenu disposed.");
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}	
	
	/**
	 * 
	 * @author pasquale
	 *
	 * A static class with a new thread that listen for packet client's side
	 */
	public static class ReceivingHandler implements Runnable {
		
		public DatagramSocket tsocket;
		public Label tconnection;
		
		public ReceivingHandler(DatagramSocket socket, Label connection)
		{
			tsocket = socket;
			tconnection = connection;
		}
		
		public void run()
		{
			System.out.println("Thread: Waiting for server response");
			tconnection.setText("Connecting...");
			
			DatagramPacket packet;
						
			do
			{
				packet = NetworkHelper.receive(this.tsocket);
				byte[] buffer = packet.getData();
				Json json = new Json();
				String d = new String(buffer);
				d = d.trim();
				Data data = json.fromJson(Data.class, d);
				System.out.println("Receveid packet. Task = " + data.getTask());
				tconnection.setText("Task = " + data.getTask());
				threadTask = data.getTask();
			}
			while (threadTask != Task.INIT_GAME_LEFT && threadTask != Task.INIT_GAME_RIGHT);
		}
	}
}
