// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

/**
 * Impression type (<a href="https://templates.machinezoo.com/iso-19794-2-2005#sampletype">SAMPLETYPE</a>).
 */
public enum Iso19794p2v2005ScanType {
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
	SWIPE(8);
	final int code;
	Iso19794p2v2005ScanType(int code) {
		this.code = code;
	}
}
