// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio;

/**
 * Exception thrown when fingerprint template does not match format specification.
 */
public class TemplateFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * Creates new {@code TemplateFormatException} with the specified message.
	 * 
	 * @param message
	 *            informative message (possibly {@code null}) that can be later retrieved via {@link Throwable#getMessage()}
	 */
	public TemplateFormatException(String message) {
		super(message);
	}
	/**
	 * Creates new {@code TemplateFormatException} with the specified cause.
	 * 
	 * @param cause
	 *            cause of this exception (possibly {@code null}) that can be later retrieved via {@link Throwable#getCause()}
	 */
	public TemplateFormatException(Throwable cause) {
		super("Malformed template.", cause);
	}
	/**
	 * Creates new {@code TemplateFormatException} with the specified message and cause.
	 * 
	 * @param message
	 *            informative message (possibly {@code null}) that can be later retrieved via {@link Throwable#getMessage()}
	 * @param cause
	 *            cause of this exception (possibly {@code null}) that can be later retrieved via {@link Throwable#getCause()}
	 */
	public TemplateFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
