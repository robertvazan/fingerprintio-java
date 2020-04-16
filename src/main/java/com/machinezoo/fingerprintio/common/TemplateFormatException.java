// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.common;

@SuppressWarnings("serial") public class TemplateFormatException extends RuntimeException {
	public TemplateFormatException(String message) {
		super(message);
	}
	public TemplateFormatException(Throwable cause) {
		super("Bad template.", cause);
	}
	public TemplateFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
