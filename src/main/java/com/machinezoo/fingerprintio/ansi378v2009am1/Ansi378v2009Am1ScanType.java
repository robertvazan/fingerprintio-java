// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

/**
 * Impression type (<a href="https://templates.machinezoo.com/ansi378-2009am1#sampletype">SAMPLETYPE</a>).
 */
public enum Ansi378v2009Am1ScanType {
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
	 * Live vertical swipe (code 8).
	 */
	LIVE_SWIPE(8),
	/**
	 * Live palm (code 10).
	 */
	LIVE_PALM(10),
	/**
	 * Non-live palm (code 11).
	 */
	NONLIVE_PALM(11),
	/**
	 * Live optical contact plain (code 20).
	 */
	LIVE_OPTICAL_CONTACT_PLAIN(20),
	/**
	 * Live optical contact rolled (code 21).
	 */
	LIVE_OPTICAL_CONTACT_ROLLED(21),
	/**
	 * Live non-optical contact plain (code 22).
	 */
	LIVE_NONOPTICAL_CONTACT_PLAIN(22),
	/**
	 * Live non-optical contact rolled (code 23).
	 */
	LIVE_NONOPTICAL_CONTACT_ROLLED(23),
	/**
	 * Live optical contactless plain (code 24).
	 */
	LIVE_OPTICAL_CONTACTLESS_PLAIN(24),
	/**
	 * Live optical contactless rolled (code 25).
	 */
	LIVE_OPTICAL_CONTACTLESS_ROLLED(25),
	/**
	 * Live non-optical contactless plain (code 26).
	 */
	LIVE_NONOPTICAL_CONTACTLESS_PLAIN(26),
	/**
	 * Live non-optical contactless rolled (code 27).
	 */
	LIVE_NONOPTICAL_CONTACTLESS_ROLLED(27),
	/**
	 * Other (code 28).
	 */
	OTHER(28),
	/**
	 * Unknown (code 29).
	 */
	UNKNOWN(29);
	final int code;
	Ansi378v2009Am1ScanType(int code) {
		this.code = code;
	}
}
