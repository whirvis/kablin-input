package org.ardenus.engine.input.device.adapter.gamecube;

import org.ardenus.engine.input.device.adapter.AnalogMapping;
import org.ardenus.engine.input.device.feature.AnalogStick;

public class GcStickMapping extends AnalogMapping<AnalogStick> {

	public final int gcAxisX;
	public final int gcAxisY;
	public final int xMin, xMax;
	public final int yMin, yMax;

	public GcStickMapping(AnalogStick analog, int gcAxisX, int gcAxisY,
			int xMin, int xMax, int yMin, int yMax) {
		super(analog);
		this.gcAxisX = gcAxisX;
		this.gcAxisY = gcAxisY;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

}
