// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.common;

/**
 * IBIA-assigned organization codes.
 * Only the most commonly used codes are listed here.
 * See <a href="https://www.ibia.org/cbeff/iso/biometric-organizations">IBIA organization directory</a> for full list.
 * 
 * @see <a href="https://www.ibia.org/cbeff/iso/biometric-organizations">IBIA-registered organizations</a>
 */
public interface IbiaOrganizations {
	/**
	 * Organization code of NIST (0x000f).
	 */
	public static final int NIST = 0x000f;
	/**
	 * Organization code of INCITS (0x001b).
	 */
	public static final int INCITS = 0x001b;
	/**
	 * Organization code of ISO (0x0101).
	 */
	public static final int ISO = 0x0101;
	/**
	 * Special placeholder organization code for unknown organization (0x0103).
	 */
	public static final int UNKNOWN = 0x0103;
	/**
	 * Special placeholder organization code for testing (0xfffe).
	 */
	public static final int TEST = 0xfffe;
}
