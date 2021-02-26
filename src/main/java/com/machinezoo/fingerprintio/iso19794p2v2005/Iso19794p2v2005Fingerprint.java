// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2v2005Fingerprint {
	public Iso19794p2v2005Position position = Iso19794p2v2005Position.UNKNOWN;
	public int quality;
	public List<Iso19794p2v2005Minutia> minutiae = new ArrayList<>();
	public Iso19794p2v2005Fingerprint() {
	}
	Iso19794p2v2005Fingerprint(TemplateReader in, int width, int height, boolean strict) {
		position = TemplateUtils.decodeType(in.readUnsignedByte(), Iso19794p2v2005Position.class, strict, "Unrecognized finger position code.");
		in.skipBytes(1);
		quality = in.readUnsignedByte();
		int count = in.readUnsignedByte();
		for (int i = 0; i < count; ++i)
			minutiae.add(new Iso19794p2v2005Minutia(in, strict));
		int extensionBytes = in.readUnsignedShort();
		in.skipBytes(extensionBytes);
	}
	void validate(int width, int height) {
		Objects.requireNonNull(position, "Finger position must be non-null (even if unknown).");
		ValidateTemplate.int8(minutiae.size(), "There cannot be more than 255 minutiae.");
		for (Iso19794p2v2005Minutia minutia : minutiae)
			minutia.validate(width, height);
	}
}
