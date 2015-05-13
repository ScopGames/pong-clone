package pongserver.utility;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import ponggame.entity.GameEntity;
import pongserver.utility.NetworkHelper.Task;

public class Data implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Task mTask;
	private GameEntity mGameEntity;
	private Date time;
	
	
	public Data(Task task)
	{	
		Calendar cal = Calendar.getInstance();
		this.mTask = task;
		this.setTime(cal.getTime());
	}
	
	public Data(Task task, GameEntity game)
	{
		this(task);
		this.mGameEntity = game;
	}
	
	public void setTask(Task t){
		
		this.mTask = t;
	}	
	
	public Task getTask(){
		return mTask;
	}

	public GameEntity getGameEntity(){
		return mGameEntity;
	}

	public void setGameEntity(GameEntity mGameEntity) {
		this.mGameEntity = mGameEntity;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	


}

