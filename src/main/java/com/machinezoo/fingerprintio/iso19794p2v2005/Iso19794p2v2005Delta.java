// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2v2005Delta {
	public int positionX;
	public int positionY;
	public int[] angles;
	public Iso19794p2v2005Delta() {
	}
	Iso19794p2v2005Delta(TemplateReader in) {
		positionX = in.readUnsignedShort();
		boolean hasAngles = (positionX & 0x4000) != 0;
		positionX &= 0xffff - 0x4000;
		positionY = in.readUnsignedShort();
		if (hasAngles) {
			angles = new int[3];
			for (int i = 0; i < 3; ++i)
				angles[i] = in.readUnsignedByte();
		}
	}
	void write(TemplateWriter out) {
		out.writeShort((angles != null ? 0x4000 : 0) | positionX);
		out.writeShort(positionY);
		if (angles != null)
			for (int i = 0; i < 3; ++i)
				out.writeByte(angles[i]);
	}
	int measure() {
		return angles != null ? 7 : 4;
	}
	void validate(int width, int height) {
		ValidateTemplate.position(positionX, width, "Delta X position must be an unsigned 14-bit number less than image width.");
		ValidateTemplate.position(positionY, height, "Delta Y position must be an unsigned 14-bit number less than image height.");
		if (angles != null) {
			ValidateTemplate.condition(angles.length == 3, "Delta must have exactly 3 angles.");
			for (int i = 0; i < 3; ++i)
				ValidateTemplate.int8(angles[i], "Delta angle must be an unsigned 8-bit number.");
		}
	}
}
