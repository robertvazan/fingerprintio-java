// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

/**
 * Impression type (<a href="https://templates.machinezoo.com/ansi378-2004#sampletype">SAMPLETYPE</a>).
 */
public enum Ansi378v2004ScanType {
	/**
	 * Live plain (code 0).
	 */
	LIVE_PLAIN(0),
	/**
	 * Live rolled (code 1).
	 */
	LIVE_ROLLED(1),
	/**
	 * Non-live plain (code 2).
	 */
	NONLIVE_PLAIN(2),
	/**
	 * Non-live rolled (code 3).
	 */
	NONLIVE_ROLLED(3),
	/**
	 * Swipe (code 8).
	 */
	SWIPE(8),
	/**
	 * Live contactless (code 9).
	 */
	LIVE_CONTACTLESS(9);
	final int code;
	Ansi378v2004ScanType(int code) {
		this.code = code;
	}
}
