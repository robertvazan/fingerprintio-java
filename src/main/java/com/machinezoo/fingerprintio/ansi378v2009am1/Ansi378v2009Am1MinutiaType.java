// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

/**
 * Minutia type (<a href="https://templates.machinezoo.com/ansi378-2009am1#mintype">MINTYPE</a>).
 */
public enum Ansi378v2009Am1MinutiaType {
	/**
	 * Either ending or bifurcation (code 00 binary).
	 */
	EITHER,
	/**
	 * Ridge ending (code 01 binary).
	 */
	ENDING,
	/**
	 * Ridge bifurcation (code 10 binary).
	 */
	BIFURCATION;
}
