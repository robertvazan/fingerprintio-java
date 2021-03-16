// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

public enum Iso19794p2v2005ScanType {
	LIVE_PLAIN(0),
	LIVE_ROLLED(1),
	NONLIVE_PLAIN(2),
	NONLIVE_ROLLED(3),
	SWIPE(8);
	final int code;
	Iso19794p2v2005ScanType(int code) {
		this.code = code;
	}
}
