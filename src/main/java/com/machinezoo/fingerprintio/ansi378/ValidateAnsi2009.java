// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import com.machinezoo.fingerprintio.utils.*;

class ValidateAnsi2009 {
	static void quality(int value, String message) {
		Validate.condition(value >= 0 && value <= 100 || value == 254 || value == 255, message);
	}
}
