// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.utils;

import org.slf4j.*;
import com.machinezoo.fingerprintio.*;

public class ValidateTemplate {
	private static Logger logger = LoggerFactory.getLogger(ValidateTemplate.class);
	public static void fail(boolean strict, String message) {
		if (strict)
			throw new TemplateFormatException(message);
		else
			logger.warn(message);
	}
	public static void condition(boolean condition, boolean strict, String message) {
		if (!condition)
			fail(strict, message);
	}
	public static void condition(boolean condition, String message) {
		condition(condition, true, message);
	}
	public static void range(int value, int min, int max, String message) {
		condition(value >= min && value <= max, message);
	}
	public static void int4(int value, String message) {
		range(value, 0, 0xf, message);
	}
	public static void int8(int value, String message) {
		range(value, 0, 0xff, message);
	}
	public static void int14(int value, String message) {
		range(value, 0, 0x3fff, message);
	}
	public static void int16(int value, String message) {
		range(value, 0, 0xffff, message);
	}
	public static void nonzero(int value, String message) {
		condition(value != 0, message);
	}
	public static void nonzero8(int value, String message) {
		int8(value, message);
		nonzero(value, message);
	}
	public static void nonzero14(int value, String message) {
		int14(value, message);
		nonzero(value, message);
	}
	public static void nonzero16(int value, String message) {
		int16(value, message);
		nonzero(value, message);
	}
	public static void position(int value, int size, String message) {
		int14(value, message);
		range(value, 0, size - 1, message);
	}
	public static void rules(Runnable validator, boolean strict, String message) {
		try {
			validator.run();
		} catch (Throwable ex) {
			if (strict)
				throw ex;
			logger.warn(message, ex);
		}
	}
	public static void structure(Runnable validator, boolean strict) {
		rules(validator, strict, "Template failed validation.");
	}
}
