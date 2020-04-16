// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.utils;

import java.util.*;
import java.util.function.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.noexception.throwing.*;

public class TemplateUtils {
	public static void decodeTemplate(byte[] template, ThrowingConsumer<DataInputBuffer> parser) {
		decodeBytes(template, "Unexpected end of template data.", parser);
	}
	public static void decodeExtension(byte[] extension, ThrowingConsumer<DataInputBuffer> parser) {
		decodeBytes(extension, "Unexpected end of extension block.", parser);
	}
	public static void decodeBytes(byte[] data, String eofMessage, ThrowingConsumer<DataInputBuffer> parser) {
		try {
			DataInputBuffer in = new DataInputBuffer(data);
			parser.accept(in);
		} catch (Throwable ex) {
			throw convertException(ex, eofMessage);
		}
	}
	public static <T> T decodeBytesTo(byte[] data, String eofMessage, ThrowingFunction<DataInputBuffer, T> parser) {
		try {
			DataInputBuffer in = new DataInputBuffer(data);
			return parser.apply(in);
		} catch (Throwable ex) {
			throw convertException(ex, eofMessage);
		}
	}
	private static TemplateFormatException convertException(Throwable ex, String eofMessage) {
		if (ex instanceof BufferEofException)
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
	public static <T extends Enum<T>> T decodeType(int code, Class<T> clazz, boolean lax, String message) {
		return decodeType(code, clazz.getEnumConstants(), Enum::ordinal, lax, message);
	}
	public static <T> T decodeType(int code, T[] candidates, ToIntFunction<T> codes, boolean lax, String message) {
		T type = Arrays.stream(candidates).filter(t -> codes.applyAsInt(t) == code).findFirst().orElse(null);
		Validate.condition(type != null, lax, message);
		return type;
	}
}
