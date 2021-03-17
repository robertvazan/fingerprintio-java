// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

/**
 * Certification scheme (<a href="https://templates.machinezoo.com/iso-19794-2-2011#certtype">CERTTYPE</a>).
 */
public enum Iso19794p2v2011CertificationScheme {
	/**
	 * Annex E.1 of ISO 19794-2:2011, image quality for AFIS (code 1).
	 */
	IMAGE_QUALITY_FOR_AFIS(1),
	/**
	 * Annex E.2 of ISO 19794-2:2011, image quality for personal verification (code 2).
	 */
	IMAGE_QUALITY_FOR_VERIFICATION(2),
	/**
	 * Annex E.3 of ISO 19794-2:2011, requirements for optical fingerprint readers (code 3).
	 */
	OPTICAL_SENSOR_REQUIREMENTS(3);
	final int code;
	Iso19794p2v2011CertificationScheme(int code) {
		this.code = code;
	}
}
