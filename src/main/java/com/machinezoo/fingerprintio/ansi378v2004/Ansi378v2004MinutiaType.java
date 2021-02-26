// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

/**
 * Minutia type (<a href="https://templates.machinezoo.com/ansi378-2004#mintype">MINTYPE</a>).
 */
public enum Ansi378v2004MinutiaType {
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
