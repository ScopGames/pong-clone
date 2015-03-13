package pongtest.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;

/** Interpolates values using differents easing equations.
 * 
 * Helper class for gdx.utils.Interpolation. */
public class Tween {
	float initialValue;
	float finalValue;
	float duration;
	float elapsed = 0;
	float value = 0;
	private boolean finished = false;
	Interpolation interpolation;

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
		elapsed += delta;
		float percentage = elapsed / duration;
		
		if (percentage > 1)
		{
			finished = true;
			percentage = 1;
		}
			
		
		value = interpolation.apply(initialValue , finalValue, percentage);
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
		finished = false;
		value = initialValue;
	}

	public boolean finished() {
		return finished;
	}
}