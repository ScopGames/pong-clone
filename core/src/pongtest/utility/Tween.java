package pongtest.utility;

import com.badlogic.gdx.math.Interpolation;

/** Interpolates values using differents easing equations.
 * 
 * Helper class for gdx.utils.Interpolation. */
public class Tween {
	float initialValue;
	float finalValue;
	float duration;
	float percentage = 0;
	float elapsed = 0;
	float value = 0;
	private boolean isTweenActive = true;
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
		if (isTweenActive && percentage <= 1) {
			elapsed += delta;
			percentage = elapsed / duration;

			// don't let the percentage go over 1
			if (percentage > 1) percentage = 1;

			value = initialValue + (finalValue - initialValue) * interpolation.apply(percentage);
		}
	}

	/** Gets the current tween value.
	 * 
	 * @return Value of the tween. */
	public float getValue () {
		return value;
	}

	/** Pauses the tween.
	 * 
	 * After pausing it, it can be resumed with the resume() method. */
	public void pause () {
		isTweenActive = false;
	}

	/** Resumes a paused or stopped tween. */
	public void resume () {
		isTweenActive = true;
	}

	/** Stops the tween reverting the interpolation to the initial state. */
	public void stop () {
		isTweenActive = false;
		elapsed = 0;
		value = initialValue;
	}

	/** Restars the tween. */
	public void restart () {
		stop();
		resume();
	}

	/** Tells if the tween is active.
	 * 
	 * @return true if the tween is active, false otherwise. */
	public boolean isTweenActive () {
		return isTweenActive;
	}
}