// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

/**
 * Minutia type (<a href="https://templates.machinezoo.com/iso-19794-2-2005#mintype">MINTYPE</a>).
 */
public enum Iso19794p2v2005MinutiaType {
	/**
	 * Other minutia type (code 00 binary).
	 */
	OTHER,
	/**
	 * Ridge ending (code 01 binary).
	 */
	ENDING,
	/**
	 * Ridge bifurcation (code 10 binary).
	 */
	BIFURCATION;
}
