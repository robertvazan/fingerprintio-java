// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import com.machinezoo.fingerprintio.utils.*;

class ValidateAnsi {
	static void angle(int value, String message) {
		Validate.range(value, 0, 179, message);
	}
}
