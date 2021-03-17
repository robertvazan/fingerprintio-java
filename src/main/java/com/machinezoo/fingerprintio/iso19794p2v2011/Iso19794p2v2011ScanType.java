// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

/**
 * Impression type (<a href="https://templates.machinezoo.com/iso-19794-2-2011#sampletype">SAMPLETYPE</a>).
 */
public enum Iso19794p2v2011ScanType {
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
	 * Latent impression (code 4).
	 */
	LATENT_IMPRESSION(4),
	/**
	 * Latent tracing (code 5).
	 */
	LATENT_TRACING(5),
	/**
	 * Latent photo (code 6).
	 */
	LATENT_PHOTO(6),
	/**
	 * Latent lift (code 7).
	 */
	LATENT_LIFT(7),
	/**
	 * Swipe (code 8).
	 */
	SWIPE(8),
	/**
	 * Vertical roll (code 9).
	 */
	VERTICAL_ROLL(9),
	/**
	 * Live contactless (code 24).
	 */
	LIVE_CONTACTLESS(24),
	/**
	 * Other (code 28).
	 */
	OTHER(28),
	/**
	 * Unknown (code 29).
	 */
	UNKNOWN(29);
	final int code;
	Iso19794p2v2011ScanType(int code) {
		this.code = code;
	}
}
