package com.whirvis.kibasan;

import java.lang.annotation.*;

/**
 * When present, gives a static ID to an {@link InputDevice} class. This allows
 * for the ID of an input device to be queried without an instance. This is
 * necessary for some features of the input system to function. While it is not
 * required that device classes use this annotation, it is recommended.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeviceId {

	public String value();

}
