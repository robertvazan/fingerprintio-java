// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Minutia (<a href="https://templates.machinezoo.com/iso-19794-2-2011#minutia">MINUTIA</a>).
 */
public class Iso19794p2v2011Minutia {
	/**
	 * Minutia type (<a href="https://templates.machinezoo.com/iso-19794-2-2011#mintype">MINTYPE</a>).
	 * Defaults to {@link Iso19794p2v2011MinutiaType#OTHER}.
	 */
	public Iso19794p2v2011MinutiaType type = Iso19794p2v2011MinutiaType.OTHER;
	/**
	 * Minutia X position (<a href="https://templates.machinezoo.com/iso-19794-2-2011#minx">MINX</a>).
	 */
	public int positionX;
	/**
	 * Minutia Y position (<a href="https://templates.machinezoo.com/iso-19794-2-2011#miny">MINY</a>).
	 */
	public int positionY;
	/**
	 * Minutia angle (<a href="https://templates.machinezoo.com/iso-19794-2-2011#minangle">MINANGLE</a>).
	 */
	public int angle;
	/**
	 * Minutia quality (<a href="https://templates.machinezoo.com/iso-19794-2-2011#minquality">MINQUALITY</a>).
	 * Defaults to 254 (unreported quality).
	 */
	public int quality = 254;
	/**
	 * Creates new minutia (<a href="https://templates.machinezoo.com/iso-19794-2-2011#minutia">MINUTIA</a>).
	 */
	public Iso19794p2v2011Minutia() {
	}
	Iso19794p2v2011Minutia(TemplateReader in, boolean withQuality, boolean strict) {
		positionX = in.readUnsignedShort();
		type = TemplateUtils.decodeType(positionX >> 14, Iso19794p2v2011MinutiaType.class, strict, "Unrecognized minutia type code.");
		positionX &= 0x3fff;
		positionY = in.readUnsignedShort();
		angle = in.readUnsignedByte();
		if (withQuality)
			quality = in.readUnsignedByte();
	}
	void write(TemplateWriter out, boolean withQuality) {
		out.writeShort((type.ordinal() << 14) | positionX);
		out.writeShort(positionY);
		out.writeByte(angle);
		if (withQuality)
			out.writeByte(quality);
	}
	void validate(int width, int height) {
		Objects.requireNonNull(type, "Minutia type must be non-null.");
		ValidateTemplate.position(positionX, width, "Minutia X position must be an unsigned 14-bit number less than image width.");
		ValidateTemplate.position(positionY, height, "Minutia Y position must be an unsigned 14-bit number less than image height.");
		ValidateTemplate.int8(angle, "Minutia angle must be an unsigned 8-bit number.");
		ValidateTemplate.condition(quality >= 0 && quality <= 100 || quality == 254 || quality == 255,
			"Minutia quality must be in range 0 through 100 or a special value 254 or 255.");
	}
}
