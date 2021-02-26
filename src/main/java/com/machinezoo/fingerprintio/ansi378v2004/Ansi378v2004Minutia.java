// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Minutia (<a href="https://templates.machinezoo.com/ansi378-2004#minutia">MINUTIA</a>).
 */
public class Ansi378v2004Minutia {
	/**
	 * Minutia type (<a href="https://templates.machinezoo.com/ansi378-2004#mintype">MINTYPE</a>).
	 * Defaults to {@link Ansi378v2004MinutiaType#OTHER}.
	 */
	public Ansi378v2004MinutiaType type = Ansi378v2004MinutiaType.OTHER;
	/**
	 * Minutia X position (<a href="https://templates.machinezoo.com/ansi378-2004#minx">MINX</a>).
	 */
	public int positionX;
	/**
	 * Minutia Y position (<a href="https://templates.machinezoo.com/ansi378-2004#miny">MINY</a>).
	 */
	public int positionY;
	/**
	 * Minutia angle (<a href="https://templates.machinezoo.com/ansi378-2004#minangle">MINANGLE</a>).
	 */
	public int angle;
	/**
	 * Minutia quality (<a href="https://templates.machinezoo.com/ansi378-2004#minquality">MINQUALITY</a>).
	 * Defaults to zero.
	 */
	public int quality;
	/**
	 * Creates new minutia (<a href="https://templates.machinezoo.com/ansi378-2004#minutia">MINUTIA</a>).
	 */
	public Ansi378v2004Minutia() {
	}
	Ansi378v2004Minutia(TemplateReader in, boolean strict) {
		positionX = in.readUnsignedShort();
		type = TemplateUtils.decodeType(positionX >> 14, Ansi378v2004MinutiaType.class, strict, "Unrecognized minutia type code.");
		positionX &= 0x3fff;
		positionY = in.readUnsignedShort();
		angle = in.readUnsignedByte();
		quality = in.readUnsignedByte();
	}
	void write(TemplateWriter out) {
		out.writeShort((type.ordinal() << 14) | positionX);
		out.writeShort(positionY);
		out.writeByte(angle);
		out.writeByte(quality);
	}
	void validate(int width, int height) {
		Objects.requireNonNull(type, "Minutia type must be non-null.");
		ValidateTemplate.position(positionX, width, "Minutia X position must be an unsigned 14-bit number less than image width.");
		ValidateTemplate.position(positionY, height, "Minutia Y position must be an unsigned 14-bit number less than image height.");
		ValidateAnsi.angle(angle, "Minutia angle must be in range 0 through 179.");
		ValidateTemplate.range(quality, 0, 100, "Minutia quality must be in range 0 through 100.");
	}
}
