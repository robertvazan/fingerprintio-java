// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2v2005Minutia {
	public Iso19794p2v2005MinutiaType type = Iso19794p2v2005MinutiaType.OTHER;
	public int positionX;
	public int positionY;
	public int angle;
	public int quality;
	public Iso19794p2v2005Minutia() {
	}
	Iso19794p2v2005Minutia(TemplateReader in, boolean strict) {
		positionX = in.readUnsignedShort();
		type = TemplateUtils.decodeType(positionX >> 14, Iso19794p2v2005MinutiaType.class, strict, "Unrecognized minutia type code.");
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
		ValidateTemplate.int8(angle, "Minutia angle must be an unsigned 8-bit number.");
		ValidateTemplate.range(quality, 0, 100, "Minutia quality must be in range 0 through 100.");
	}
}
