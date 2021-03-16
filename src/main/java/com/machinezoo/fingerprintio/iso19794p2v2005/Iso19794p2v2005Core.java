// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2v2005Core {
	public int positionX;
	public int positionY;
	public Integer angle;
	public Iso19794p2v2005Core() {
	}
	Iso19794p2v2005Core(TemplateReader in) {
		positionX = in.readUnsignedShort();
		boolean hasAngle = (positionX & 0x4000) != 0;
		positionX &= 0xffff - 0x4000;
		positionY = in.readUnsignedShort();
		if (hasAngle)
			angle = in.readUnsignedByte();
	}
	void write(TemplateWriter out) {
		out.writeShort((angle != null ? 0x4000 : 0) | positionX);
		out.writeShort(positionY);
		if (angle != null)
			out.writeByte(angle);
	}
	int measure() {
		return angle != null ? 5 : 4;
	}
	void validate(int width, int height) {
		ValidateTemplate.position(positionX, width, "Core X position must be an unsigned 14-bit number less than image width.");
		ValidateTemplate.position(positionY, height, "Core Y position must be an unsigned 14-bit number less than image height.");
		if (angle != null)
			ValidateTemplate.int8(angle, "Core angle must be an unsigned 8-bit number.");
	}
}
