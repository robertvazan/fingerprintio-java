// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

import com.machinezoo.fingerprintio.utils.*;

/**
 * Delta point (<a href="https://templates.machinezoo.com/ansi378-2004#deltadef">DELTADEF</a>).
 */
public class Ansi378v2004Delta {
	/**
	 * Delta X position (<a href="https://templates.machinezoo.com/ansi378-2004#deltax">DELTAX</a>).
	 */
	public int positionX;
	/**
	 * Delta Y position (<a href="https://templates.machinezoo.com/ansi378-2004#deltay">DELTAY</a>).
	 */
	public int positionY;
	/**
	 * List of delta angles (<a href="https://templates.machinezoo.com/ansi378-2004#deltaangle">DELTAANGLE</a>).
	 * This field is either {@code null} or it is an array of exactly three angles.
	 */
	public int[] angles;
	/**
	 * Creates new delta point (<a href="https://templates.machinezoo.com/ansi378-2004#deltadef">DELTADEF</a>).
	 */
	public Ansi378v2004Delta() {
	}
	Ansi378v2004Delta(TemplateReader in, boolean hasAngles) {
		positionX = in.readUnsignedShort();
		positionY = in.readUnsignedShort();
		if (hasAngles) {
			angles = new int[3];
			for (int i = 0; i < 3; ++i)
				angles[i] = in.readUnsignedByte();
		}
	}
	void write(TemplateWriter out) {
		out.writeShort(positionX);
		out.writeShort(positionY);
		if (angles != null)
			for (int i = 0; i < 3; ++i)
				out.writeByte(angles[i]);
	}
	void validate(int width, int height) {
		ValidateTemplate.position(positionX, width, "Delta X position must be an unsigned 14-bit number less than image width.");
		ValidateTemplate.position(positionY, height, "Delta Y position must be an unsigned 14-bit number less than image height.");
		if (angles != null) {
			ValidateTemplate.condition(angles.length == 3, "Delta must have exactly 3 angles.");
			for (int i = 0; i < 3; ++i)
				ValidateAnsi.angle(angles[i], "Delta angle must be in range 0 through 179.");
		}
	}
}
