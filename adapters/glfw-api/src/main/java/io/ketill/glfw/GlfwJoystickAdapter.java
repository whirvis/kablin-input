package io.ketill.glfw;

import io.ketill.AdapterSupplier;
import io.ketill.FeatureAdapter;
import io.ketill.IoDevice;
import io.ketill.MappedFeatureRegistry;
import io.ketill.MappingMethod;
import io.ketill.controller.AnalogStick;
import io.ketill.controller.AnalogTrigger;
import io.ketill.controller.ButtonStateZ;
import io.ketill.controller.DeviceButton;
import io.ketill.controller.StickPosZ;
import io.ketill.controller.TriggerStateZ;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;

/**
 * GLFW joystick adapters are an extension of {@link GlfwDeviceAdapter}
 * which specialize in mapping data from GLFW joysticks. An assortment
 * of utility methods are provided for mapping features.
 * <p>
 * Feature adapters like {@link #updateStick(StickPosZ, GlfwStickMapping)}
 * can also be overridden to modify data returned from GLFW. An example of
 * this would be switching the polarity of an axis. This is done in the
 * {@code glfw.psx} module, and can be seen in {@code GlfwPs4Adapter}.
 * <pre>
 *     &#64;Override
 *     protected void updateTrigger(@NotNull Trigger1f trigger,
 *                                  int glfwAxis) {
 *         super.updateTrigger(trigger, glfwAxis);
 *         if (glfwAxis == AXIS_LT || glfwAxis == AXIS_RT) {
 *             trigger.force += 1.0F;
 *             trigger.force /= 2.0F;
 *         }
 *     }
 * </pre>
 *
 * @param <I> the I/O device type.
 * @see #mapButton(DeviceButton, int)
 * @see #mapStick(AnalogStick, GlfwStickMapping)
 * @see #mapTrigger(AnalogTrigger, int)
 * @see AdapterSupplier
 * @see GlfwJoystickSeeker
 */
public abstract class GlfwJoystickAdapter<I extends IoDevice>
        extends GlfwDeviceAdapter<I> {

    protected final int glfwJoystick;

    /*
     * These fields were originally protected, meaning extending
     * classes could access them directly. They were privatized in
     * favor of using isPressed() and getAxis(), which are safer.
     */
    private ByteBuffer buttons;
    private FloatBuffer axes;

    /**
     * @param device         the device which owns this adapter.
     * @param registry       the device's mapped feature registry.
     * @param ptr_glfwWindow the GLFW window pointer.
     * @param glfwJoystick   the GLFW joystick.
     * @throws NullPointerException     if {@code device} or {@code registry}
     *                                  are {@code null};
     *                                  if {@code ptr_glfwWindow} is a null
     *                                  pointer (has a value of zero.)
     * @throws IllegalArgumentException if {@code glfwJoystick} is not a
     *                                  valid GLFW joystick.
     */
    public GlfwJoystickAdapter(@NotNull I device,
                               @NotNull MappedFeatureRegistry registry,
                               long ptr_glfwWindow, int glfwJoystick) {
        super(device, registry, ptr_glfwWindow);
        this.glfwJoystick = GlfwUtils.requireJoystick(glfwJoystick);
    }

    /**
     * @param button     the button to map.
     * @param glfwButton the GLFW button to map {@code button} to.
     * @throws NullPointerException     if {@code button} is {@code null}.
     * @throws IllegalArgumentException if {@code glfwButton} is negative.
     * @see #updateButton(ButtonStateZ, int)
     */
    @MappingMethod
    protected void mapButton(@NotNull DeviceButton button, int glfwButton) {
        Objects.requireNonNull(button, "button");
        if (glfwButton < 0) {
            throw new IllegalArgumentException("glfwButton < 0");
        }
        registry.mapFeature(button, glfwButton, this::updateButton);
    }

    /**
     * @param stick   the stick to map.
     * @param mapping the GLFW stick mapping for {@code stick}.
     * @throws NullPointerException if {@code stick} or {@code mapping}
     *                              are {@code null}.
     * @see #updateStick(StickPosZ, GlfwStickMapping)
     */
    @MappingMethod
    protected void mapStick(@NotNull AnalogStick stick,
                            @NotNull GlfwStickMapping mapping) {
        Objects.requireNonNull(stick, "stick");
        Objects.requireNonNull(mapping, "mapping");
        registry.mapFeature(stick, mapping, this::updateStick);
    }

    /**
     * @param trigger  the trigger to map.
     * @param glfwAxis the GLFW axis to map {@code trigger} to.
     * @throws NullPointerException     if {@code trigger} is {@code null}.
     * @throws IllegalArgumentException if {@code glfwAxis} is negative.
     * @see #updateTrigger(TriggerStateZ, int)
     */
    @MappingMethod
    protected void mapTrigger(@NotNull AnalogTrigger trigger, int glfwAxis) {
        Objects.requireNonNull(trigger, "trigger");
        if (glfwAxis < 0) {
            throw new IllegalArgumentException("glfwAxis < 0");
        }
        registry.mapFeature(trigger, glfwAxis, this::updateTrigger);
    }

    /**
     * @return the amount of GLFW buttons present on this joystick,
     * {@code -1} if the adapter has not yet been polled.
     */
    protected final int getButtonCount() {
        return buttons != null ? buttons.limit() : -1;
    }

    /**
     * @param glfwButton the ID of the GLFW button to check.
     * @return {@code true} if {@code glfwButton} is currently pressed,
     * {@code false} otherwise (or if it does not exist.)
     * @throws IndexOutOfBoundsException if {@code glfwButton} is negative or
     *                                   not smaller than the button count.
     */
    protected final boolean isPressed(int glfwButton) {
        if (glfwButton < 0) {
            throw new IndexOutOfBoundsException("glfwButton < 0");
        } else if (buttons == null) {
            return false; /* buttons have yet to set, ignore */
        } else if (glfwButton >= buttons.limit()) {
            throw new IndexOutOfBoundsException(
                    "glfwButton >= buttons.limit()");
        }
        return buttons.get(glfwButton) != 0;
    }

    /**
     * @return the amount of GLFW axes present on this joystick,
     * {@code -1} if the adapter has not yet been polled.
     */
    protected final int getAxisCount() {
        return axes != null ? axes.limit() : -1;
    }

    /**
     * @param glfwAxis the ID of the GLFW axis to fetch.
     * @return the current axis value, {@code 0.0F} it does not exist.
     * @throws IndexOutOfBoundsException if {@code glfwAxis} is negative or
     *                                   not smaller than the axis count.
     */
    protected final float getAxis(int glfwAxis) {
        if (glfwAxis < 0) {
            throw new IndexOutOfBoundsException("glfwAxis < 0");
        } else if (axes == null) {
            return 0.0F; /* axes have yet to be set, ignore */
        } else if (glfwAxis >= axes.limit()) {
            throw new IndexOutOfBoundsException("glfwAxis >= axes.limit()");
        }
        return axes.get(glfwAxis);
    }

    /**
     * Updater for buttons mapped via
     * {@link #mapButton(DeviceButton, int)}.
     *
     * @param state      the button state.
     * @param glfwButton the GLFW button.
     */
    @FeatureAdapter
    protected void updateButton(@NotNull ButtonStateZ state, int glfwButton) {
        state.pressed = this.isPressed(glfwButton);
    }

    /**
     * Updater for analog sticks mapped via
     * {@link #mapStick(AnalogStick, GlfwStickMapping)}.
     *
     * @param state   the analog stick position.
     * @param mapping the GLFW stick mapping.
     */
    @FeatureAdapter
    protected void updateStick(@NotNull StickPosZ state,
                               @NotNull GlfwStickMapping mapping) {
        state.pos.x = this.getAxis(mapping.glfwXAxis);
        state.pos.y = this.getAxis(mapping.glfwYAxis);
        if (mapping.hasZButton) {
            boolean pressed = this.isPressed(mapping.glfwZButton);
            state.pos.z = pressed ? -1.0F : 0.0F;
        } else {
            state.pos.z = 0.0F;
        }
    }

    /**
     * Updater for analog triggers mapped via
     * {@link #mapTrigger(AnalogTrigger, int)}.
     *
     * @param state    the analog trigger state.
     * @param glfwAxis the GLFW axis.
     */
    @FeatureAdapter
    protected void updateTrigger(@NotNull TriggerStateZ state, int glfwAxis) {
        state.force = this.getAxis(glfwAxis);
    }

    @Override
    @MustBeInvokedByOverriders
    protected void pollDevice() {
        this.buttons = glfwGetJoystickButtons(glfwJoystick);
        this.axes = glfwGetJoystickAxes(glfwJoystick);
    }

    @Override
    protected final boolean isDeviceConnected() {
        return glfwJoystickPresent(glfwJoystick);
    }

}
