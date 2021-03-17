// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Certification record (<a href="https://templates.machinezoo.com/iso-19794-1-2011#certificate">CERTIFICATE</a>).
 */
public class Iso19794p1v2011Certificate {
	/**
	 * Certification authority (<a href="https://templates.machinezoo.com/iso-19794-1-2011#certifiedby">CERTIFIEDBY</a>).
	 * Defaults to {@link IbiaOrganizations#UNKNOWN}.
	 */
	public int authority = IbiaOrganizations.UNKNOWN;
	/**
	 * Certification scheme (<a href="https://templates.machinezoo.com/iso-19794-1-2011#certtype">CERTTYPE</a>).
	 */
	public int scheme;
	/**
	 * Creates new certification record (<a href="https://templates.machinezoo.com/iso-19794-1-2011#certificate">CERTIFICATE</a>).
	 */
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
