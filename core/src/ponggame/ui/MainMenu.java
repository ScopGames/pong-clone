package ponggame.ui;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ponggame.Main;
import ponggame.Main.Screens;
import ponggame.utility.Tween;
import pongserver.network.PongServer;
import pongserver.utility.NetworkHelper;
import pongserver.utility.NetworkHelper.Task;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

public class MainMenu implements Screen, InputProcessor {
	private Stage stage = new Stage();
	private TextureAtlas atlas;
	private Table mainTable, multiplayerTable;
	private TextButton singlePlayerButton, multiplayerButton, submitIpButton;
	private TextField ipInput;
	private Label ipLabel, connectionStatusLabel;
	private Skin skin;
	private BitmapFont mainFont;
	private Action showMultiplayerMenu, hideMultiplayerMenu;
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		// initialize table field
		initializeActions();
		createUI();
		
		stage.addActor(mainTable);
		stage.addActor(multiplayerTable);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}
	
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
		
		TextFieldStyle style = new TextFieldStyle();
		style.font = mainFont;
		style.fontColor = new Color(1, 1, 0, 1);
		style.background = skin.getDrawable("button");
		//style.cursor.setMinWidth(2f);
		
		LabelStyle labelStyle = new LabelStyle(mainFont, Color.WHITE);
		ipLabel = new Label("IP del server:", labelStyle);

		ipInput = new TextField("",style);
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
				
				connectToServer(ipAddress);
			}
		});
		
		connectionStatusLabel = new Label("placeholder...", labelStyle);
		
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
	}
	
	private void initializeActions() {
		final Tween menuTweenShow = new Tween(Interpolation.exp5Out, 0, -150, 1.0f);
		//menuTweenShow.setRepeatMode(RepeatMode.REPEAT_PINGPONG);
		final Tween menuTweenHide = new Tween(Interpolation.exp5Out, -150, 0, 1.0f);
		
		showMultiplayerMenu = new Action() {
			@Override
			public boolean act(float delta) {
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
		
		hideMultiplayerMenu = new Action() {
			@Override
			public boolean act(float delta) {
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
	
	private void connectToServer(String address_str)
	{		
		try 
		{
			// sending
			InetAddress address = InetAddress.getByName(address_str);
			DatagramSocket socket = new DatagramSocket();
			NetworkHelper.send(socket, address, PongServer.SERVER_PORT, Task.REGISTER_PLAYER);
			System.out.println("MainMenu : Packet sent");
			connectionStatusLabel.setText("Packet sent");
			
			//TODO maybe do this in another thread ? 
			// receiving
			System.out.println("MainMenu: Listening...");
			connectionStatusLabel.setText("Waiting for other players to connect");
			DatagramPacket packet = NetworkHelper.receive(socket);
			Task task = (Task)NetworkHelper.deserialize(packet.getData());
			
			System.out.println("Receveid packet. Task = " + task);
			connectionStatusLabel.setText("Task = " + task);
			
			//Main.changeScreen(Screens.PONGGAME_MULTIPLAYER);
		} 
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void resize(int width, int height) {		
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		mainFont.dispose();
		atlas.dispose();
		skin.dispose();
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
}
