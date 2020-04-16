// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import java.util.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378Extension {
	public int type;
	public byte[] data;
	public Ansi378Extension() {
	}
	Ansi378Extension(DataInputBuffer in) {
		type = in.readUnsignedShort();
		int length = in.readUnsignedShort();
		if (length < 4)
			throw new TemplateFormatException("Extension block must be at least 4 bytes long.");
		data = new byte[length - 4];
		in.readFully(data);
	}
	void write(DataOutputBuffer out) {
		out.writeShort(type);
		out.writeShort(measure());
		out.write(data);
	}
	int measure() {
		return data.length + 4;
	}
	void validate() {
		Validate.nonzero16(type, "Extension type must be a non-zero unsigned 16-bit number.");
		Validate.condition((type & 0xff) != 0, "Extension type must not be in reserved range.");
		if ((type >> 8) == 0)
			Validate.condition((type & 0xff) <= 2, "Extension type must not be in reserved range.");
		Objects.requireNonNull(data, "Extension data must be non-null.");
		Validate.int16(measure(), "Extension length must be an unsigned 16-bit number.");
	}
}
