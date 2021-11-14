package org.ardenus.engine.input.device.feature;

public class Button1b implements Button1bc {

	public boolean pressed;

	/**
	 * @param force
	 *            the initial button state.
	 */
	public Button1b(boolean pressed) {
		this.pressed = pressed;
	}

	/**
	 * Constructs a new {@code Button1b} with {@code pressed} set to
	 * {@code false}.
	 */
	public Button1b() {
		this(false);
	}

	@Override
	public boolean pressed() {
		return this.pressed;
	}

}
