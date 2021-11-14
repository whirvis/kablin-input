package org.ardenus.engine.input.device.adapter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When present, signals to a {@link DeviceAdapter} that a method should be
 * registered as a device feature adapter. Feature adapters are used to fetch
 * input device information as well as send output data.
 * <p>
 * Feature adapter methods require two parameters. The first parameter must be
 * the mapping for the feature to adapt, with the second parameter being the
 * state of that feature. Furthermore, it must be {@code public}, not
 * {@code static}, and return {@code void}. An example would be:
 * 
 * <pre>
 * 
 * &#64;FeatureAdapter
 * public void updateStick(GlfwStickMapping mapping, Vector2f stick) {
 * 	stick.x = axes.get(mapping.glfwAxisX);
 * 	stick.y = axes.get(mapping.glfwAxisY);
 * }
 * </pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureAdapter {
	/* I'm a little teapot! */
}
