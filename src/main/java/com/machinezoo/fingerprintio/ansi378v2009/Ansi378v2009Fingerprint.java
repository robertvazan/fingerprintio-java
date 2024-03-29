// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009;

import java.util.*;
import java.util.function.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;
import com.machinezoo.noexception.*;

/**
 * Fingerprint (<a href="https://templates.machinezoo.com/ansi378-2009#fingerprint">FINGERPRINT</a>).
 */
public class Ansi378v2009Fingerprint {
	/**
	 * Finger position on hands (<a href="https://templates.machinezoo.com/ansi378-2009#position">POSITION</a>).
	 * Defaults to {@link Ansi378v2009Position#UNKNOWN}.
	 */
	public Ansi378v2009Position position = Ansi378v2009Position.UNKNOWN;
	/**
	 * Finger view number (<a href="https://templates.machinezoo.com/ansi378-2009#viewoffset">VIEWOFFSET</a>).
	 */
	public int view;
	/**
	 * Impression type (<a href="https://templates.machinezoo.com/ansi378-2009#sampletype">SAMPLETYPE</a>).
	 * Defaults to {@link Ansi378v2009ScanType#LIVE_PLAIN}.
	 */
	public Ansi378v2009ScanType scanType = Ansi378v2009ScanType.LIVE_PLAIN;
	/**
	 * Fingerprint quality (<a href="https://templates.machinezoo.com/ansi378-2009#fpquality">FPQUALITY</a>).
	 * Defaults to 254.
	 */
	public int quality = 254;
	/**
	 * Quality algorithm vendor (<a href="https://templates.machinezoo.com/ansi378-2009#qvendor">QVENDOR</a>).
	 * Defaults to {@link IbiaOrganizations#UNKNOWN}.
	 */
	public int qualityVendorId = IbiaOrganizations.UNKNOWN;
	/**
	 * Quality algorithm (<a href="https://templates.machinezoo.com/ansi378-2009#qalgo">QALGO</a>).
	 * Defaults to {@link IbiaQualityAlgorithms#UNKNOWN}.
	 */
	public int qualityAlgorithmId = IbiaQualityAlgorithms.UNKNOWN;
	/**
	 * Image width (<a href="https://templates.machinezoo.com/ansi378-2009#width">WIDTH</a>).
	 */
	public int width;
	/**
	 * Image height (<a href="https://templates.machinezoo.com/ansi378-2009#height">HEIGHT</a>).
	 */
	public int height;
	/**
	 * Horizontal pixel density (<a href="https://templates.machinezoo.com/ansi378-2009#resolutionx">RESOLUTIONX</a>).
	 * Defaults to 197 (500dpi).
	 */
	public int resolutionX = 197;
	/**
	 * Vertical pixel density (<a href="https://templates.machinezoo.com/ansi378-2009#resolutiony">RESOLUTIONY</a>).
	 * Defaults to 197 (500dpi).
	 */
	public int resolutionY = 197;
	/**
	 * List of minutiae (<a href="https://templates.machinezoo.com/ansi378-2009#minutia">MINUTIA</a>).
	 */
	public List<Ansi378v2009Minutia> minutiae = new ArrayList<>();
	/**
	 * Ridge count extension (<a href="https://templates.machinezoo.com/ansi378-2009#rcountext">RCOUNTEXT</a>).
	 * This field is {@code null} if ridge count extension is not present.
	 */
	public Ansi378v2009CountExtension counts;
	/**
	 * Core and delta extension (<a href="https://templates.machinezoo.com/ansi378-2009#coredelta">COREDELTA</a>).
	 * This field is {@code null} if core and delta extension is not present.
	 */
	public Ansi378v2009CoreDeltaExtension coredelta;
	/**
	 * List of extension data blocks (<a href="https://templates.machinezoo.com/ansi378-2009#extension">EXTENSION</a>).
	 */
	public List<Ansi378v2009Extension> extensions = new ArrayList<>();
	/**
	 * Creates new fingerprint (<a href="https://templates.machinezoo.com/ansi378-2009#fingerprint">FINGERPRINT</a>).
	 */
	public Ansi378v2009Fingerprint() {
	}
	Ansi378v2009Fingerprint(TemplateReader in, ExceptionHandler handler) {
		position = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378v2009Position.class, handler, "Unrecognized finger position code.");
		view = in.readUnsignedByte();
		scanType = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378v2009ScanType.values(), t -> t.code, handler, "Unrecognized sensor type code.");
		quality = in.readUnsignedByte();
		qualityVendorId = in.readUnsignedShort();
		qualityAlgorithmId = in.readUnsignedShort();
		width = in.readUnsignedShort();
		height = in.readUnsignedShort();
		resolutionX = in.readUnsignedShort();
		resolutionY = in.readUnsignedShort();
		int count = in.readUnsignedByte();
		for (int i = 0; i < count; ++i)
			minutiae.add(new Ansi378v2009Minutia(in, handler));
		int totalBytes = in.readUnsignedShort();
		int readBytes = 0;
		while (readBytes < totalBytes) {
			Ansi378v2009Extension extension = new Ansi378v2009Extension(in);
			if (extension.type == Ansi378v2009CountExtension.IDENTIFIER)
				decodeExtension(extension, data -> counts = new Ansi378v2009CountExtension(data, handler), handler, "Unable to decode ridge count extension.");
			else if (extension.type == Ansi378v2009CoreDeltaExtension.IDENTIFIER)
				decodeExtension(extension, data -> coredelta = new Ansi378v2009CoreDeltaExtension(data, handler), handler, "Unable to decode core/delta extension.");
			else
				extensions.add(extension);
			readBytes += extension.measure();
		}
		ValidateTemplate.condition(readBytes == totalBytes, handler, "Total length of extension data doesn't match the sum of extension block lengths.");
	}
	private void decodeExtension(Ansi378v2009Extension extension, Consumer<byte[]> decoder, ExceptionHandler handler, String message) {
		try {
			decoder.accept(extension.data);
		} catch (Throwable ex) {
			ValidateTemplate.fail(handler, message, ex);
			extensions.add(extension);
		}
	}
	void write(TemplateWriter out) {
		out.writeByte(position.ordinal());
		out.writeByte(view);
		out.writeByte(scanType.code);
		out.writeByte(quality);
		out.writeShort(qualityVendorId);
		out.writeShort(qualityAlgorithmId);
		out.writeShort(width);
		out.writeShort(height);
		out.writeShort(resolutionX);
		out.writeShort(resolutionY);
		out.writeByte(minutiae.size());
		for (Ansi378v2009Minutia minutia : minutiae)
			minutia.write(out);
		out.writeShort(extensionBytes());
		if (counts != null)
			counts.extension().write(out);
		if (coredelta != null)
			coredelta.extension().write(out);
		for (Ansi378v2009Extension extension : extensions)
			extension.write(out);
	}
	int measure() {
		int length = 19;
		length += 6 * minutiae.size();
		length += extensionBytes();
		return length;
	}
	void validate() {
		Objects.requireNonNull(position, "Finger position must be non-null (even if unknown).");
		ValidateTemplate.int4(view, "View offset must be an unsigned 4-bit number.");
		Objects.requireNonNull(scanType, "Scan type must be non-null.");
		ValidateAnsi.quality(quality, "Fingerprint quality must be in range 0 through 100 or one of the special values 254 and 255.");
		ValidateTemplate.nonzero16(qualityVendorId, "Quality algorithm vendor ID must a non-zero unsigned 16-bit number.");
		ValidateTemplate.int16(qualityAlgorithmId, "Quality algorithm ID must an unsigned 16-bit number.");
		ValidateTemplate.nonzero16(width, "Image width must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(height, "Image height must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(resolutionX, "Horizontal pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(resolutionY, "Vertical pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.int8(minutiae.size(), "There cannot be more than 255 minutiae.");
		for (Ansi378v2009Minutia minutia : minutiae)
			minutia.validate(width, height);
		if (counts != null)
			counts.validate(minutiae.size());
		if (coredelta != null)
			coredelta.validate(width, height);
		for (Ansi378v2009Extension extension : extensions)
			extension.validate();
		ValidateTemplate.int16(extensionBytes(), "Total size of all extension blocks must a 16-bit number.");
	}
	private int extensionBytes() {
		int bytes = extensions.stream().mapToInt(Ansi378v2009Extension::measure).sum();
		if (counts != null)
			bytes += counts.measure();
		if (coredelta != null)
			bytes += coredelta.measure();
		return bytes;
	}
}
