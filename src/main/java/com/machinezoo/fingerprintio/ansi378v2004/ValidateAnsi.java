// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

import com.machinezoo.fingerprintio.utils.*;

class ValidateAnsi {
	static void angle(int value, String message) {
		ValidateTemplate.range(value, 0, 179, message);
	}
}
