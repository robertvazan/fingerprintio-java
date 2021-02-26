// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

public enum Ansi378v2009Am1Position {
	UNKNOWN(0),
	RIGHT_THUMB(1),
	RIGHT_INDEX(2),
	RIGHT_MIDDLE(3),
	RIGHT_RING(4),
	RIGHT_LITTLE(5),
	LEFT_THUMB(6),
	LEFT_INDEX(7),
	LEFT_MIDDLE(8),
	LEFT_RING(9),
	LEFT_LITTLE(10),
	RIGHT_INDEX_TO_LITTLE(13),
	LEFT_INDEX_TO_LITTLE(14),
	BOTH_THUMBS(15),
	RIGHT_INDEX_MIDDLE(40),
	RIGHT_MIDDLE_RING(41),
	RIGHT_RING_LITTLE(42),
	LEFT_INDEX_MIDDLE(43),
	LEFT_MIDDLE_RING(44),
	LEFT_RING_LITTLE(45),
	BOTH_INDEXES(46),
	RIGHT_INDEX_TO_RING(47),
	RIGHT_MIDDLE_TO_LITTLE(48),
	LEFT_INDEX_TO_RING(49),
	LEFT_MIDDLE_TO_LITTLE(50);
	final int code;
	Ansi378v2009Am1Position(int code) {
		this.code = code;
	}
}