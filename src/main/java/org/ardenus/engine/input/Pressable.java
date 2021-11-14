package org.ardenus.engine.input;

/**
 * Represents an object which can be pressed and released.
 * <p>
 * Objects which can be pressed and released come in multiple forms. Examples
 * include, but are not limited to: a keyboard key, a controller button, or a
 * GUI element. The internal state of these "pressable" objects is usually
 * stored via an instance of {@link PressableState}. However, this is not a
 * requirement by any means.
 * <p>
 * While this is also not a requirement, instances of {@code Pressable} are
 * usually only the representation. An example of this would be:
 * 
 * <pre>
 * public class GameController {
 *
 *	public static final Pressable BUTTON_A = new ControllerButton("A");
 *
 *	private final Map&lt;Pressable, PressableState&gt; buttons;
 *
 *	public GameController() {
 *		this.buttons = new HashMap&lt;&gt;();
 *		buttons.put(BUTTON_A, new PressableState());
 *	}
 *
 *	public boolean isPressed(Pressable button) {
 *		&sol;*
 *		 * As can be seen in this example, BUTTON_A will be reused
 *		 * for different instances of GameController. Each controller
 *		 * contains a map of each button, with the stored value being
 *		 * the state of each button via the PressableState class. This
 *		 * works out well for groups of pressables known in advance.
 *		 *&sol;
 *		PressableState state = buttons.get(button);
 *		if (state != null) {
 *			return state.pressed;
 *		}
 *		return false;
 *	}
 * 
 * }
 * </pre>
 */
public interface Pressable {

	/**
	 * Returns if this object can currently be pressed.
	 * <p>
	 * Unless overridden, this method will always return {@code true} by
	 * default.<br>
	 * It is up to the implementation of this class to determine whether or not
	 * an object is currently pressable.
	 * <p>
	 * If an object is not currently pressable, it does not mean, <i>and should
	 * <b>never</b> mean</i>, that it is currently pressed (i.e., it cannot be
	 * pressed further). The same goes for whether or not it is released. An
	 * object can become unpressable while in any state. However, this should
	 * usually only happen when the object is released. Implementations should
	 * also automatically release pressable objects if they become unpressable
	 * while being pressed down. However, this is not a requirement.
	 * 
	 * @return {@code true} if this object can currently be pressed,
	 *         {@code false} otherwise.
	 */
	public default boolean isPressable() {
		return true;
	}

}
