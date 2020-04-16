// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378Minutia {
	public Ansi378MinutiaType type = Ansi378MinutiaType.OTHER;
	public int positionX;
	public int positionY;
	public int angle;
	public int quality;
	public Ansi378Minutia() {
	}
	Ansi378Minutia(DataInputBuffer in, boolean lax) {
		positionX = in.readUnsignedShort();
		type = TemplateUtils.decodeType(positionX >> 14, Ansi378MinutiaType.class, lax, "Unrecognized minutia type code.");
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
		Validate.range(quality, 0, 100, "Minutia quality must be in range 0 through 100.");
	}
}
