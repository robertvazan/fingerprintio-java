// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p1v2011Certificate {
	public int authority = IbiaOrganizations.UNKNOWN;
	public int scheme;
	public Iso19794p1v2011Certificate() {
	}
	Iso19794p1v2011Certificate(TemplateReader in) {
		authority = in.readUnsignedShort();
		scheme = in.readUnsignedByte();
	}
	void write(TemplateWriter out) {
		out.writeShort(authority);
		out.writeByte(scheme);
	}
	void validate() {
		ValidateTemplate.nonzero16(authority, "Certification authority must be a non-zero 16-bit unsigned number.");
		ValidateTemplate.int8(scheme, "Certification scheme must be an 8-bit unsigned number.");
	}
}
