// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.common;

/**
 * IBIA-assigned quality algorithm codes.
 * Only the most commonly used codes are listed here.
 * See <a href="https://www.ibia.org/cbeff/iso/biometric-quality-algorithm-identifiers">IBIA quality algorithm directory</a> for full list.
 * 
 * @see <a href="https://www.ibia.org/cbeff/iso/biometric-quality-algorithm-identifiers">IBIA-registered quality algorithms</a>
 */
public interface IbiaQualityAlgorithms {
	/**
	 * Code for NIST NFIQ algorithm (0x377d).
	 * It should be used in conjunction with {@link IbiaOrganizations#NIST}.
	 */
	public static final int NIST_NFIQ = 0x377d;
	/**
	 * Special placeholder code for unknown algorithm (0x0001).
	 * It should be used in conjunction with {@link IbiaOrganizations#UNKNOWN}.
	 */
	public static final int UNKNOWN = 0x0001;
}
