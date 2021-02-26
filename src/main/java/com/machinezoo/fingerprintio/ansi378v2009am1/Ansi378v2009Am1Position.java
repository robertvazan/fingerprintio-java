// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

/**
 * Finger position on hands (<a href="https://templates.machinezoo.com/ansi378-2009am1#position">POSITION</a>).
 */
public enum Ansi378v2009Am1Position {
	/**
	 * Unknown position (code 0).
	 */
	UNKNOWN(0),
	/**
	 * Thumb on right hand (code 1).
	 */
	RIGHT_THUMB(1),
	/**
	 * Index finger on right hand (code 2).
	 */
	RIGHT_INDEX(2),
	/**
	 * Middle finger on right hand (code 3).
	 */
	RIGHT_MIDDLE(3),
	/**
	 * Ring finger on right hand (code 4).
	 */
	RIGHT_RING(4),
	/**
	 * Little finger on right hand (code 5).
	 */
	RIGHT_LITTLE(5),
	/**
	 * Thumb on left hand (code 6).
	 */
	LEFT_THUMB(6),
	/**
	 * Index finger on left hand (code 7).
	 */
	LEFT_INDEX(7),
	/**
	 * Middle finger on left hand (code 8).
	 */
	LEFT_MIDDLE(8),
	/**
	 * Ring finger on left hand (code 9).
	 */
	LEFT_RING(9),
	/**
	 * Little finger on left hand (code 10).
	 */
	LEFT_LITTLE(10),
	/**
	 * Right index, middle, ring, and little (code 13).
	 */
	RIGHT_INDEX_TO_LITTLE(13),
	/**
	 * Left index, middle, ring, and little (code 14).
	 */
	LEFT_INDEX_TO_LITTLE(14),
	/**
	 * Left and right thumbs (code 15).
	 */
	BOTH_THUMBS(15),
	/**
	 * Right index and middle (code 40).
	 */
	RIGHT_INDEX_MIDDLE(40),
	/**
	 * Right middle and ring (code 41).
	 */
	RIGHT_MIDDLE_RING(41),
	/**
	 * Right ring and little (code 42).
	 */
	RIGHT_RING_LITTLE(42),
	/**
	 * Left index and middle (code 43).
	 */
	LEFT_INDEX_MIDDLE(43),
	/**
	 * Left middle and ring (code 44).
	 */
	LEFT_MIDDLE_RING(44),
	/**
	 * Left ring and little (code 45).
	 */
	LEFT_RING_LITTLE(45),
	/**
	 * Left and right index (code 46).
	 */
	BOTH_INDEXES(46),
	/**
	 * Right index, middle, and ring (code 47).
	 */
	RIGHT_INDEX_TO_RING(47),
	/**
	 * Right middle, ring, and little (code 48).
	 */
	RIGHT_MIDDLE_TO_LITTLE(48),
	/**
	 * Left index, middle, and ring (code 49).
	 */
	LEFT_INDEX_TO_RING(49),
	/**
	 * Left middle, ring, and little (code 50).
	 */
	LEFT_MIDDLE_TO_LITTLE(50);
	final int code;
	Ansi378v2009Am1Position(int code) {
		this.code = code;
	}
}
