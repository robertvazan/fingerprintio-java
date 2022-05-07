// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.utils;

import com.machinezoo.fingerprintio.*;
import com.machinezoo.noexception.*;

public class ValidateTemplate {
	public static void fail(ExceptionHandler handler, String message, Throwable cause) {
		var exception = new TemplateFormatException(message, cause);
		if (!handler.handle(exception))
			throw exception;
	}
	public static void fail(ExceptionHandler handler, String message) {
		var exception = new TemplateFormatException(message);
		if (!handler.handle(exception))
			throw exception;
	}
	public static void condition(boolean condition, ExceptionHandler handler, String message) {
		if (!condition)
			fail(handler, message);
	}
	public static void condition(boolean condition, String message) {
		condition(condition, Exceptions.propagate(), message);
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
	public static void rules(Runnable validator, ExceptionHandler handler, String message) {
		try {
			validator.run();
		} catch (Throwable ex) {
			fail(handler, message, ex);
		}
	}
	public static void structure(Runnable validator, ExceptionHandler handler) {
		rules(validator, handler, "Template failed validation.");
	}
}
