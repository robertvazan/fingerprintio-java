// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2Minutia {
	public Iso19794p2MinutiaType type = Iso19794p2MinutiaType.OTHER;
	public int positionX;
	public int positionY;
	public int angle;
	public int quality;
	public Iso19794p2Minutia() {
	}
	Iso19794p2Minutia(DataInputBuffer in, boolean lax) {
		positionX = in.readUnsignedShort();
		type = TemplateUtils.decodeType(positionX >> 14, Iso19794p2MinutiaType.class, lax, "Unrecognized minutia type code.");
		positionX &= 0x3fff;
		positionY = in.readUnsignedShort();
		angle = in.readUnsignedByte();
		quality = in.readUnsignedByte();
	}
	void validate(int width, int height) {
		Objects.requireNonNull(type, "Minutia type must be non-null.");
		Validate.position(positionX, width, "Minutia X position must be an unsigned 14-bit number less than image width.");
		Validate.position(positionY, height, "Minutia Y position must be an unsigned 14-bit number less than image height.");
		Validate.int8(angle, "Minutia angle must be an unsigned 8-bit number.");
	}
}
