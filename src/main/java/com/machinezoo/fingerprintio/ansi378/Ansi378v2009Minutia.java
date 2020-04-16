// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378v2009Minutia {
	public Ansi378v2009MinutiaType type = Ansi378v2009MinutiaType.EITHER;
	public int positionX;
	public int positionY;
	public int angle;
	public int quality = 254;
	public Ansi378v2009Minutia() {
	}
	Ansi378v2009Minutia(DataInputBuffer in, boolean lax) {
		positionX = in.readUnsignedShort();
		type = TemplateUtils.decodeType(positionX >> 14, Ansi378v2009MinutiaType.class, lax, "Unrecognized minutia type code.");
		positionX &= 0x3fff;
		positionY = in.readUnsignedShort();
		angle = in.readUnsignedByte();
		quality = in.readUnsignedByte();
	}
	void write(DataOutputBuffer out) {
		out.writeShort((type.ordinal() << 14) | positionX);
		out.writeShort(positionY);
		out.writeByte(angle);
		out.writeByte(quality);
	}
	void validate(int width, int height) {
		Objects.requireNonNull(type, "Minutia type must be non-null.");
		Validate.position(positionX, width, "Minutia X position must be an unsigned 14-bit number less than image width.");
		Validate.position(positionY, height, "Minutia Y position must be an unsigned 14-bit number less than image height.");
		ValidateAnsi.angle(angle, "Minutia angle must be in range 0 through 179.");
		ValidateAnsi2009.quality(quality, "Minutia quality must be in range 0 through 100 or one of the special values 254 and 255.");
	}
}
