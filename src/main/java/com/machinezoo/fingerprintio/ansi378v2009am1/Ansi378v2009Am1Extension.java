// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

import java.util.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Extension data block (<a href="https://templates.machinezoo.com/ansi378-2009am1#extension">EXTENSION</a>).
 * Predefined extension blocks have specialized representation.
 * See {@link Ansi378v2009CountExtension} and {@link Ansi378v2009CoreDeltaExtension}.
 */
public class Ansi378v2009Am1Extension {
	/**
	 * Extension type (<a href="https://templates.machinezoo.com/ansi378-2009am1#exttype">EXTTYPE</a>).
	 */
	public int type;
	/**
	 * Extension data (<a href="https://templates.machinezoo.com/ansi378-2009am1#extdata">EXTDATA</a>).
	 */
	public byte[] data;
	/**
	 * Creates new extension data block (<a href="https://templates.machinezoo.com/ansi378-2009am1#extension">EXTENSION</a>).
	 */
	public Ansi378v2009Am1Extension() {
	}
	Ansi378v2009Am1Extension(TemplateReader in) {
		type = in.readUnsignedShort();
		int length = in.readUnsignedShort();
		if (length < 4)
			throw new TemplateFormatException("Extension block must be at least 4 bytes long.");
		data = new byte[length - 4];
		in.readFully(data);
	}
	void write(TemplateWriter out) {
		out.writeShort(type);
		out.writeShort(measure());
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
		ValidateTemplate.int16(measure(), "Extension length must be an unsigned 16-bit number.");
	}
}
