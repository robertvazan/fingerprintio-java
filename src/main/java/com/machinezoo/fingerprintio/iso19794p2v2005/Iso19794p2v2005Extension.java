// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2v2005Extension {
	public int type;
	public byte[] data;
	public Iso19794p2v2005Extension() {
	}
	Iso19794p2v2005Extension(TemplateReader in) {
		type = in.readUnsignedShort();
		int length = in.readUnsignedShort();
		data = new byte[length];
		in.readFully(data);
	}
	void write(TemplateWriter out) {
		out.writeShort(type);
		out.writeShort(data.length);
		out.write(data);
	}
	int measure() {
		return data.length + 4;
	}
	void validate() {
		ValidateTemplate.nonzero16(type, "Extension type must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.condition((type & 0xff) != 0, "Extension type must not be in reserved range.");
		if ((type >> 8) == 0)
			ValidateTemplate.condition((type & 0xff) <= 2, "Extension type must not be in reserved range.");
		Objects.requireNonNull(data, "Extension data must be non-null.");
		ValidateTemplate.int16(data.length, "Extension length must be an unsigned 16-bit number.");
	}
}
