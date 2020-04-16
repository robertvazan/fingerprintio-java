// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

public enum Ansi378ScanType {
	LIVE_PLAIN(0),
	LIVE_ROLLED(1),
	NONLIVE_PLAIN(2),
	NONLIVE_ROLLED(3),
	SWIPE(8),
	LIVE_CONTACTLESS(9);
	final int code;
	Ansi378ScanType(int code) {
		this.code = code;
	}
}
