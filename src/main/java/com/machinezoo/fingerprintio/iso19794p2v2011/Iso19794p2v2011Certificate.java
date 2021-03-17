// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

import java.util.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.iso19794p1v2011.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Certification record (<a href="https://templates.machinezoo.com/iso-19794-2-2011#certificate">CERTIFICATE</a>).
 */
public class Iso19794p2v2011Certificate {
	/**
	 * Certification authority (<a href="https://templates.machinezoo.com/iso-19794-2-2011#certifiedby">CERTIFIEDBY</a>).
	 * Defaults to {@link IbiaOrganizations#UNKNOWN}.
	 */
	public int authority = IbiaOrganizations.UNKNOWN;
	/**
	 * Certification scheme (<a href="https://templates.machinezoo.com/iso-19794-2-2011#certtype">CERTTYPE</a>).
	 */
	public Iso19794p2v2011CertificationScheme scheme;
	Iso19794p2v2011Certificate(Iso19794p1v2011Certificate certificate, boolean strict) {
		authority = certificate.authority;
		scheme = TemplateUtils.decodeType(certificate.scheme, Iso19794p2v2011CertificationScheme.class, strict, "Unrecognized certification scheme.");
	}
	Iso19794p1v2011Certificate toCertificate() {
		Iso19794p1v2011Certificate certificate = new Iso19794p1v2011Certificate();
		certificate.authority = authority;
		certificate.scheme = scheme.code;
		return certificate;
	}
	void validate() {
		ValidateTemplate.nonzero16(authority, "Certification authority must be a non-zero 16-bit unsigned number.");
		Objects.requireNonNull(scheme, "Certification scheme must be non-null.");
	}
}
