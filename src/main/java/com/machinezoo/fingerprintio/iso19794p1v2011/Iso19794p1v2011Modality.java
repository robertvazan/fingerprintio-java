// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

import java.nio.charset.*;

/**
 * File signature / magic number (<a href="https://templates.machinezoo.com/iso-19794-1-2011#magic">MAGIC</a>).
 */
public enum Iso19794p1v2011Modality {
	/**
	 * ISO 19794-2: Fingerprint minutiae (FMR).
	 */
	ISO_19794_2("FMR"),
	/**
	 * ISO 19794-3: Fingerprint spectral data (FSP).
	 */
	ISO_19794_3("FSP"),
	/**
	 * ISO 19794-4: Fingerprint images (FIR).
	 */
	ISO_19794_4("FIR"),
	/**
	 * ISO 19794-5: Face images (FAC).
	 */
	ISO_19794_5("FAC"),
	/**
	 * ISO 19794-6: Iris images (IIR).
	 */
	ISO_19794_6("IIR"),
	/**
	 * ISO 19794-7: Raw signature (SDI).
	 */
	ISO_19794_7("SDI"),
	/**
	 * ISO 19794-8: Fingerprint skeletons (FSK).
	 */
	ISO_19794_8("FSK"),
	/**
	 * ISO 19794-9: Vascular images (VIR).
	 */
	ISO_19794_9("VIR"),
	/**
	 * ISO 19794-10: Hand geometry (HND).
	 */
	ISO_19794_10("HND"),
	/**
	 * ISO 19794-11: Processed signature (SPD).
	 */
	ISO_19794_11("SPD"),
	/**
	 * ISO 19794-13: Voice (VDI).
	 */
	ISO_19794_13("VDI"),
	/**
	 * ISO 19794-14: DNA material (DNA).
	 */
	ISO_19794_14("DNA");
	final byte[] magic;
	Iso19794p1v2011Modality(String code) {
		magic = (code + "\0").getBytes(StandardCharsets.US_ASCII);
	}
}
