// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.utils;

import java.util.*;
import java.util.function.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.noexception.throwing.*;

public class TemplateUtils {
	public static void decodeTemplate(byte[] template, ThrowingConsumer<TemplateReader> parser) {
		decodeBytes(template, "Unexpected end of template data.", parser);
	}
	public static void decodeExtension(byte[] extension, ThrowingConsumer<TemplateReader> parser) {
		decodeBytes(extension, "Unexpected end of extension block.", parser);
	}
	public static void decodeBytes(byte[] data, String eofMessage, ThrowingConsumer<TemplateReader> parser) {
		try {
			TemplateReader in = new TemplateReader(data);
			parser.accept(in);
		} catch (Throwable ex) {
			throw convertException(ex, eofMessage);
		}
	}
	public static <T> T decodeBytesTo(byte[] data, String eofMessage, ThrowingFunction<TemplateReader, T> parser) {
		try {
			TemplateReader in = new TemplateReader(data);
			return parser.apply(in);
		} catch (Throwable ex) {
			throw convertException(ex, eofMessage);
		}
	}
	private static TemplateFormatException convertException(Throwable ex, String eofMessage) {
		if (ex instanceof TemplateEofException)
			return new TemplateFormatException(eofMessage);
		if (ex instanceof TemplateFormatException)
			return (TemplateFormatException)ex;
		return new TemplateFormatException(ex);
	}
	public static TemplateFormatException convertException(Throwable ex) {
		if (ex instanceof TemplateFormatException)
			return (TemplateFormatException)ex;
		return new TemplateFormatException(ex);
	}
	public static <T extends Enum<T>> T decodeType(int code, Class<T> clazz, boolean strict, String message) {
		return decodeType(code, clazz.getEnumConstants(), Enum::ordinal, strict, message);
	}
	public static <T> T decodeType(int code, T[] candidates, ToIntFunction<T> codes, boolean strict, String message) {
		T type = Arrays.stream(candidates).filter(t -> codes.applyAsInt(t) == code).findFirst().orElse(null);
		ValidateTemplate.condition(type != null, strict, message);
		return type;
	}
}
