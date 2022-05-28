package io.ketill.glfw.nx;

import io.ketill.MappingType;
import io.ketill.controller.AnalogStick;
import io.ketill.glfw.GlfwUtils;

/**
 * A mapping for an {@link AnalogStick} used by {@link GlfwNxJoyConAdapter}.
 *
 * @see GlfwNxJoyConAdapter#mapJoyConStick(AnalogStick, GlfwNxJoyConStickMapping)
 */
@MappingType
public final class GlfwNxJoyConStickMapping {

    /**
     * The GLFW button for up.
     */
    public final int glfwUp;

    /**
     * The GLFW button for down.
     */
    public final int glfwDown;

    /**
     * The GLFW button for left.
     */
    public final int glfwLeft;

    /**
     * The GLFW button for right.
     */
    public final int glfwRight;

    /**
     * The GLFW button for the thumb button.
     */
    public final int glfwThumb;

    /**
     * Constructs a new {@code JoyConStickMapping}.
     *
     * @param glfwUp    the GLFW button for up.
     * @param glfwDown  the GLFW button for down.
     * @param glfwLeft  the GLFW button for left.
     * @param glfwRight the GLFW button for right.
     * @param glfwThumb the GLFW button for the thumb button.
     * @throws IllegalArgumentException if {@code glfwUp}, {@code glfwDown},
     *                                  {@code glfwLeft}, {@code glfwRight},
     *                                  or {@code glfwThumb} are negative.
     */
    public GlfwNxJoyConStickMapping(int glfwUp, int glfwDown, int glfwLeft,
                                    int glfwRight, int glfwThumb) {
        this.glfwUp = GlfwUtils.requireButton(glfwUp, "glfwUp");
        this.glfwDown = GlfwUtils.requireButton(glfwDown, "glfwDown");
        this.glfwLeft = GlfwUtils.requireButton(glfwLeft, "glfwLeft");
        this.glfwRight = GlfwUtils.requireButton(glfwRight, "glfwRight");
        this.glfwThumb = GlfwUtils.requireButton(glfwThumb, "glfwThumb");
    }

}
