// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

import com.machinezoo.fingerprintio.utils.*;

/**
 * Core point (<a href="https://templates.machinezoo.com/ansi378-2009am1#coredef">COREDEF</a>).
 */
public class Ansi378v2009Am1Core {
	/**
	 * Core X position (<a href="https://templates.machinezoo.com/ansi378-2009am1#corex">COREX</a>).
	 */
	public int positionX;
	/**
	 * Core Y position (<a href="https://templates.machinezoo.com/ansi378-2009am1#corey">COREY</a>).
	 */
	public int positionY;
	/**
	 * Core angle (<a href="https://templates.machinezoo.com/ansi378-2009am1#coreangle">COREANGLE</a>).
	 * This field is {@code null} if core angle is not present.
	 */
	public Integer angle;
	/**
	 * Creates new core point (<a href="https://templates.machinezoo.com/ansi378-2009am1#coredef">COREDEF</a>).
	 */
	public Ansi378v2009Am1Core() {
	}
	Ansi378v2009Am1Core(TemplateReader in, boolean hasAngle) {
		positionX = in.readUnsignedShort();
		positionY = in.readUnsignedShort();
		if (hasAngle)
			angle = in.readUnsignedByte();
	}
	void write(TemplateWriter out) {
		out.writeShort(positionX);
		out.writeShort(positionY);
		if (angle != null)
			out.writeByte(angle);
	}
	void validate(int width, int height) {
		ValidateTemplate.position(positionX, width, "Core X position must be an unsigned 14-bit number less than image width.");
		ValidateTemplate.position(positionY, height, "Core Y position must be an unsigned 14-bit number less than image height.");
		if (angle != null)
			ValidateAnsi.angle(angle, "Core angle must be in range 0 through 179.");
	}
}
