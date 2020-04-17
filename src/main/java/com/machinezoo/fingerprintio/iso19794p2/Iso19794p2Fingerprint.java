// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2Fingerprint {
	public Iso19794p2Position position = Iso19794p2Position.UNKNOWN;
	public int quality;
	public List<Iso19794p2Minutia> minutiae = new ArrayList<>();
	public Iso19794p2Fingerprint() {
	}
	Iso19794p2Fingerprint(DataInputBuffer in, int width, int height, boolean lax) {
		position = TemplateUtils.decodeType(in.readUnsignedByte(), Iso19794p2Position.class, lax, "Unrecognized finger position code.");
		in.skipBytes(1);
		quality = in.readUnsignedByte();
		int count = in.readUnsignedByte();
		for (int i = 0; i < count; ++i)
			minutiae.add(new Iso19794p2Minutia(in, lax));
		int extensionBytes = in.readUnsignedShort();
		in.skipBytes(extensionBytes);
	}
	void validate(int width, int height) {
		Objects.requireNonNull(position, "Finger position must be non-null (even if unknown).");
		Validate.int8(minutiae.size(), "There cannot be more than 255 minutiae.");
		for (Iso19794p2Minutia minutia : minutiae)
			minutia.validate(width, height);
	}
}
