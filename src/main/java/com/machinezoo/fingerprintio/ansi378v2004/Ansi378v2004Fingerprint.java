// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

import java.util.*;
import java.util.function.*;
import com.machinezoo.fingerprintio.utils.*;
import com.machinezoo.noexception.*;

/**
 * Fingerprint (<a href="https://templates.machinezoo.com/ansi378-2004#fingerprint">FINGERPRINT</a>).
 */
public class Ansi378v2004Fingerprint {
	/**
	 * Finger position on hands (<a href="https://templates.machinezoo.com/ansi378-2004#position">POSITION</a>).
	 * Defaults to {@link Ansi378v2004Position#UNKNOWN}.
	 */
	public Ansi378v2004Position position = Ansi378v2004Position.UNKNOWN;
	/**
	 * Finger view number (<a href="https://templates.machinezoo.com/ansi378-2004#viewoffset">VIEWOFFSET</a>).
	 */
	public int view;
	/**
	 * Impression type (<a href="https://templates.machinezoo.com/ansi378-2004#sampletype">SAMPLETYPE</a>).
	 * Defaults to {@link Ansi378v2004ScanType#LIVE_PLAIN}.
	 */
	public Ansi378v2004ScanType scanType = Ansi378v2004ScanType.LIVE_PLAIN;
	/**
	 * Fingerprint quality (<a href="https://templates.machinezoo.com/ansi378-2004#fpquality">FPQUALITY</a>).
	 * Defaults to 100.
	 */
	public int quality = 100;
	/**
	 * List of minutiae (<a href="https://templates.machinezoo.com/ansi378-2004#minutia">MINUTIA</a>).
	 */
	public List<Ansi378v2004Minutia> minutiae = new ArrayList<>();
	/**
	 * Ridge count extension (<a href="https://templates.machinezoo.com/ansi378-2004#rcountext">RCOUNTEXT</a>).
	 * This field is {@code null} if ridge count extension is not present.
	 */
	public Ansi378v2004CountExtension counts;
	/**
	 * Core and delta extension (<a href="https://templates.machinezoo.com/ansi378-2004#coredelta">COREDELTA</a>).
	 * This field is {@code null} if core and delta extension is not present.
	 */
	public Ansi378v2004CoreDeltaExtension coredelta;
	/**
	 * List of extension data blocks (<a href="https://templates.machinezoo.com/ansi378-2004#extension">EXTENSION</a>).
	 */
	public List<Ansi378v2004Extension> extensions = new ArrayList<>();
	/**
	 * Creates new fingerprint (<a href="https://templates.machinezoo.com/ansi378-2004#fingerprint">FINGERPRINT</a>).
	 */
	public Ansi378v2004Fingerprint() {
	}
	Ansi378v2004Fingerprint(TemplateReader in, ExceptionHandler handler) {
		position = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378v2004Position.class, handler, "Unrecognized finger position code.");
		int offsetAndType = in.readUnsignedByte();
		view = offsetAndType >> 4;
		scanType = TemplateUtils.decodeType(offsetAndType & 0xf, Ansi378v2004ScanType.values(), t -> t.code, handler, "Unrecognized sensor type code.");
		quality = in.readUnsignedByte();
		int count = in.readUnsignedByte();
		for (int i = 0; i < count; ++i)
			minutiae.add(new Ansi378v2004Minutia(in, handler));
		int totalBytes = in.readUnsignedShort();
		int readBytes = 0;
		while (readBytes < totalBytes) {
			Ansi378v2004Extension extension = new Ansi378v2004Extension(in);
			if (extension.type == Ansi378v2004CountExtension.IDENTIFIER)
				decodeExtension(extension, data -> counts = new Ansi378v2004CountExtension(data, handler), handler, "Unable to decode ridge count extension.");
			else if (extension.type == Ansi378v2004CoreDeltaExtension.IDENTIFIER)
				decodeExtension(extension, data -> coredelta = new Ansi378v2004CoreDeltaExtension(data, handler), handler, "Unable to decode core/delta extension.");
			else
				extensions.add(extension);
			readBytes += extension.measure();
		}
		ValidateTemplate.condition(readBytes == totalBytes, handler, "Total length of extension data doesn't match the sum of extension block lengths.");
	}
	private void decodeExtension(Ansi378v2004Extension extension, Consumer<byte[]> decoder, ExceptionHandler handler, String message) {
		try {
			decoder.accept(extension.data);
		} catch (Throwable ex) {
			ValidateTemplate.fail(handler, message, ex);
			extensions.add(extension);
		}
	}
	void write(TemplateWriter out) {
		out.writeByte(position.ordinal());
		out.writeByte((view << 4) | scanType.code);
		out.writeByte(quality);
		out.writeByte(minutiae.size());
		for (Ansi378v2004Minutia minutia : minutiae)
			minutia.write(out);
		out.writeShort(extensionBytes());
		if (counts != null)
			counts.extension().write(out);
		if (coredelta != null)
			coredelta.extension().write(out);
		for (Ansi378v2004Extension extension : extensions)
			extension.write(out);
	}
	int measure() {
		int length = 6;
		length += 6 * minutiae.size();
		length += extensionBytes();
		return length;
	}
	void validate(int width, int height) {
		Objects.requireNonNull(position, "Finger position must be non-null (even if unknown).");
		ValidateTemplate.int4(view, "View offset must be an unsigned 4-bit number.");
		Objects.requireNonNull(scanType, "Scan type must be non-null.");
		ValidateTemplate.range(quality, 0, 100, "Fingerprint quality must be in range 0 through 100.");
		ValidateTemplate.int8(minutiae.size(), "There cannot be more than 255 minutiae.");
		for (Ansi378v2004Minutia minutia : minutiae)
			minutia.validate(width, height);
		if (counts != null)
			counts.validate(minutiae.size());
		if (coredelta != null)
			coredelta.validate(width, height);
		for (Ansi378v2004Extension extension : extensions)
			extension.validate();
		ValidateTemplate.int16(extensionBytes(), "Total size of all extension blocks must a 16-bit number.");
		ValidateTemplate.condition(coredelta == null || !coredelta.cores.isEmpty(), "Not strictly compliant template. Core count is zero.");
	}
	private int extensionBytes() {
		int bytes = extensions.stream().mapToInt(Ansi378v2004Extension::measure).sum();
		if (counts != null)
			bytes += counts.measure();
		if (coredelta != null)
			bytes += coredelta.measure();
		return bytes;
	}
}
