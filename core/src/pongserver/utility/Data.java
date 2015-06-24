/**
 * Data.java
 * 
 * Estabilish the format of the data incapsulated into the udp packets
 */
package pongserver.utility;

import ponggame.entity.GameEntity;
import pongserver.utility.NetworkHelper.Task;
import com.badlogic.gdx.utils.Json;


public class Data 
{
	/**
	 * two private attribute
	 * 
	 * mTask an enum to indicate task
	 * mGameEntity @see GameEntity
	 */
	private Task mTask;
	private GameEntity mGameEntity;
	
	/**
	 * empty constructor
	 */
	public Data(){}
	
	/**
	 * Constructor
	 * 
	 * it defines only the task
	 * @param task
	 */
	public Data(Task task)
	{	
		this.mTask = task;
	}
	
	/**
	 * Constructor 
	 * Create an object containing both the task and a GameEntity
	 * @param task the task to be done
	 * @param game information about game state
	 */
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
	
	/**
	 * Parse the informations in data as a string Json
	 * @return String a string formatted in Json
	 */
	public String getStringData()
	{
		String data = new String();
		Json json = new Json();
		data = json.toJson(this);
		return data;
	}
	
}