package ponggame.utility;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

/** Interpolates values using differents easing equations.
 * 
 * Helper class for gdx.utils.Interpolation. */
public class Tween {	
	float initialValue;
	float finalValue;
	float duration;
	float elapsed = 0;
	float value = 0;
	int repeatsCount = 0;
	int desiredRepetitions = 0;
	private boolean finished = false;
	Interpolation interpolation;
	RepeatMode repeatMode = RepeatMode.NORMAL;

	// TODO do we need all these repeat modes ? LOOP and REPEAT can be merged.
	// Remember to update the javadoc, if some of these is removed
	public enum RepeatMode {
		NORMAL, LOOP, LOOP_PINGPONG, REPEAT, REPEAT_PINGPONG
	};

	
	/** Creates a new tween.
	 * 
	 * @param interpolation Equation used for the interpolation.
	 * @param initialValue Initial value of the tween.
	 * @param finalValue Final value of the tween.
	 * @param duration Duration in seconds. */
	public Tween (Interpolation interpolation, float initialValue, float finalValue, float duration) {
		this.initialValue = initialValue;
		this.finalValue = finalValue;
		this.duration = duration;
		this.interpolation = interpolation;
	}

	/** Updates the tween.
	 * 
	 * @param delta Frame time. */
	public void update (float delta) {
		if (!finished)
		{
			elapsed += delta;
			float percentage = elapsed / duration;
			percentage = MathUtils.clamp(percentage, 0, 1);

			//
			finished = updateMode(percentage);
		}				
	}
	
	/**
	 * Updates the Tween according to the specified repeatMode.
	 * 
	 * @param percentage value between 0 and 1. 
	 * @return true if the tween is finished, false otherwise.
	 */
	private boolean updateMode(float percentage)
	{
		boolean modeFinished = false;
				
		switch (repeatMode) {
			case NORMAL:
				if (percentage  >= 1)
					modeFinished = true;
				break;
			
			case LOOP: // infinite loop
				if (percentage >= 1) {					
					elapsed = 0;
					value = initialValue;
				}
				break;
			
			case LOOP_PINGPONG: // infinite pingpong loop
				if (percentage >= 1) {								
					// swapping initial and final values
					float tmp = initialValue;
					initialValue = finalValue;
					finalValue = tmp;
					
					value = initialValue;
					elapsed = 0;
					percentage = 0;
				}				
				break;
			
			case REPEAT: // infinite loop
				if (percentage >= 1) {					
					elapsed = 0;
					value = initialValue;
					
					repeatsCount += 1;
					
					if (repeatsCount == desiredRepetitions)
						modeFinished = true;
				}
				break;
			
			case REPEAT_PINGPONG: // infinite pingpong loop
				if (percentage >= 1) {								
					// swapping initial and final values
					float tmp = initialValue;
					initialValue = finalValue;
					finalValue = tmp;
					
					value = initialValue;
					elapsed = 0;
					percentage = 0;
					
					repeatsCount += 1; 
					
					if (repeatsCount == desiredRepetitions)
						modeFinished = true;
				}				
				break;
			
			default:
				break;
		}
		
		value = interpolation.apply(initialValue , finalValue, percentage);
		
		return modeFinished;
	}
	
	/** Gets the current tween value.
	 * 
	 * @return Value of the tween. */
	public float getValue () {
		return value;
	}

	/** Resets the tween reverting the interpolation to the initial state. */
	public void reset() {		
		elapsed = 0;
		repeatsCount = 0;
		finished = false;
		value = initialValue;
	}

	public boolean finished() {
		return finished;
	}
	
	/**
	 * Gets the percentage of completion.
	 * 
	 * @return value between 0 and 1.
	 */
	public float getPercentage() {
		return elapsed/duration;
	}
	
	public void setRepeatMode(RepeatMode repeatMode) {
		this.repeatMode = repeatMode;
	}
	
	/**
	 * Sets the desired repetitions count for the tween.
	 * 
	 * Note that doesn't affect LOOP tweens.
	 * @param repetitions
	 */
	public void setRepetitions(int repetitions) {
		this.desiredRepetitions = repetitions;
	}
}