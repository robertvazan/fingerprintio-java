// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import com.machinezoo.fingerprintio.utils.*;

public class Ansi378Delta {
	public int positionX;
	public int positionY;
	public int[] angles;
	public Ansi378Delta() {
	}
	Ansi378Delta(DataInputBuffer in, boolean hasAngles) {
		positionX = in.readUnsignedShort();
		positionY = in.readUnsignedShort();
		if (hasAngles) {
			angles = new int[3];
			for (int i = 0; i < 3; ++i)
				angles[i] = in.readUnsignedByte();
		}
	}
	void write(DataOutputBuffer out) {
		out.writeShort(positionX);
		out.writeShort(positionY);
		if (angles != null)
			for (int i = 0; i < 3; ++i)
				out.writeByte(angles[i]);
	}
	void validate(int width, int height) {
		Validate.position(positionX, width, "Delta X position must be an unsigned 14-bit number less than image width.");
		Validate.position(positionY, height, "Delta Y position must be an unsigned 14-bit number less than image height.");
		if (angles != null) {
			Validate.condition(angles.length == 3, "Delta must have exactly 3 angles.");
			for (int i = 0; i < 3; ++i)
				ValidateAnsi.angle(angles[i], "Delta angle must be in range 0 through 179.");
		}
	}
}
