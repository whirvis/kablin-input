package io.ketill.controller;

import io.ketill.AutonomousField;
import io.ketill.AutonomousState;
import io.ketill.IoDeviceObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

/**
 * Contains the state of an {@link AnalogStick}.
 */
public final class StickPosZ implements AutonomousState {

    public final @NotNull Vector3f pos;

    @AutonomousField
    public @Nullable AnalogStickCalibration calibration;

    @AutonomousField
    public final @NotNull Vector3f calibratedPos;

    @AutonomousField
    public final @NotNull ButtonStateZ up, down, left, right;

    private final AnalogStickObserver upObserver;
    private final AnalogStickObserver downObserver;
    private final AnalogStickObserver leftObserver;
    private final AnalogStickObserver rightObserver;

    StickPosZ(@NotNull AnalogStick stick,
              @NotNull IoDeviceObserver observer,
              @Nullable AnalogStickCalibration calibration) {
        this.pos = new Vector3f();
        this.calibration = calibration;
        this.calibratedPos = new Vector3f();

        this.up = new ButtonStateZ();
        this.down = new ButtonStateZ();
        this.left = new ButtonStateZ();
        this.right = new ButtonStateZ();

        this.upObserver = new AnalogStickObserver(stick, Direction.UP,
                this, up, observer);
        this.downObserver = new AnalogStickObserver(stick, Direction.DOWN,
                this, down, observer);
        this.leftObserver = new AnalogStickObserver(stick, Direction.LEFT,
                this, left, observer);
        this.rightObserver = new AnalogStickObserver(stick, Direction.RIGHT,
                this, right, observer);
    }

    @Override
    public void update() {
        calibratedPos.set(pos);
        if (calibration != null) {
            calibration.applyTo(calibratedPos);
        }

        upObserver.poll();
        downObserver.poll();
        leftObserver.poll();
        rightObserver.poll();
    }

}
