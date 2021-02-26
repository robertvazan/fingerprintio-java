// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009;

import com.machinezoo.fingerprintio.utils.*;

public class Ansi378v2009Core {
	public int positionX;
	public int positionY;
	public Integer angle;
	public Ansi378v2009Core() {
	}
	Ansi378v2009Core(DataInputBuffer in, boolean hasAngle) {
		positionX = in.readUnsignedShort();
		positionY = in.readUnsignedShort();
		if (hasAngle)
			angle = in.readUnsignedByte();
	}
	void write(DataOutputBuffer out) {
		out.writeShort(positionX);
		out.writeShort(positionY);
		if (angle != null)
			out.writeByte(angle);
	}
	void validate(int width, int height) {
		Validate.position(positionX, width, "Core X position must be an unsigned 14-bit number less than image width.");
		Validate.position(positionY, height, "Core Y position must be an unsigned 14-bit number less than image height.");
		if (angle != null)
			ValidateAnsi.angle(angle, "Core angle must be in range 0 through 179.");
	}
}
