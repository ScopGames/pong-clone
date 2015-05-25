package pongserver.utility;

import ponggame.entity.GameEntity;
import pongserver.utility.NetworkHelper.Task;
import com.badlogic.gdx.utils.Json;


public class Data 
{
	private Task mTask;
	private GameEntity mGameEntity;
	
	
	public Data(Task task)
	{	
		this.mTask = task;
	}
	
	public Data(Task task, GameEntity game)
	{
		this(task);
		this.mGameEntity = game;
	}
	
	public void setTask(Task t)
	{
		this.mTask = t;
	}	
	
	public Task getTask()
	{
		return mTask;
	}

	public GameEntity getGameEntity()
	{
		return mGameEntity;
	}

	public void setGameEntity(GameEntity mGameEntity) 
	{
		this.mGameEntity = mGameEntity;
	}
	
	public String getStringData()
	{
		String data = new String();
		Json json = new Json();
		data = json.toJson(this);
		return data;
	}
	
}