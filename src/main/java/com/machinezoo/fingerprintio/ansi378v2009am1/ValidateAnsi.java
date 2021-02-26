// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

import com.machinezoo.fingerprintio.utils.*;

class ValidateAnsi {
	static void angle(int value, String message) {
		ValidateTemplate.range(value, 0, 179, message);
	}
	static void quality(int value, String message) {
		ValidateTemplate.condition(value >= 0 && value <= 100 || value == 254 || value == 255, message);
	}
}
