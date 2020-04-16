// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import java.util.*;
import java.util.function.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378v2009Fingerprint {
	private static final Logger logger = LoggerFactory.getLogger(Ansi378v2009Fingerprint.class);
	public Ansi378Position position = Ansi378Position.UNKNOWN;
	public int view;
	public Ansi378v2009ScanType scanType = Ansi378v2009ScanType.LIVE_PLAIN;
	public int quality = 254;
	public int qualityVendorId = BiometricOrganizations.UNKNOWN;
	public int qualityAlgorithmId = BiometricQualityAlgorithms.UNKNOWN;
	public int width;
	public int height;
	public int resolutionX;
	public int resolutionY;
	public List<Ansi378v2009Minutia> minutiae = new ArrayList<>();
	public Ansi378CountExtension counts;
	public Ansi378CoreDeltaExtension coredelta;
	public List<Ansi378Extension> extensions = new ArrayList<>();
	public Ansi378v2009Fingerprint() {
	}
	Ansi378v2009Fingerprint(DataInputBuffer in, boolean lax) {
		position = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378Position.class, lax, "Unrecognized finger position code.");
		view = in.readUnsignedByte();
		scanType = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378v2009ScanType.values(), t -> t.code, lax, "Unrecognized sensor type code.");
		quality = in.readUnsignedByte();
		qualityVendorId = in.readUnsignedShort();
		qualityAlgorithmId = in.readUnsignedShort();
		width = in.readUnsignedShort();
		height = in.readUnsignedShort();
		resolutionX = in.readUnsignedShort();
		resolutionY = in.readUnsignedShort();
		int count = in.readUnsignedByte();
		for (int i = 0; i < count; ++i)
			minutiae.add(new Ansi378v2009Minutia(in, lax));
		int totalBytes = in.readUnsignedShort();
		int readBytes = 0;
		while (readBytes < totalBytes) {
			Ansi378Extension extension = new Ansi378Extension(in);
			if (extension.type == Ansi378CountExtension.IDENTIFIER)
				decodeExtension(extension, data -> counts = new Ansi378CountExtension(data, lax), lax, "Unable to decode ridge count extension.");
			else if (extension.type == Ansi378CoreDeltaExtension.IDENTIFIER)
				decodeExtension(extension, data -> coredelta = new Ansi378CoreDeltaExtension(data, lax), lax, "Unable to decode core/delta extension.");
			else
				extensions.add(extension);
			readBytes += extension.measure();
		}
		Validate.condition(readBytes == totalBytes, lax, "Total length of extension data doesn't match the sum of extension block lengths.");
	}
	private void decodeExtension(Ansi378Extension extension, Consumer<byte[]> decoder, boolean lax, String message) {
		try {
			decoder.accept(extension.data);
		} catch (Throwable ex) {
			if (!lax)
				throw ex;
			logger.warn(message, ex);
			extensions.add(extension);
		}
	}
	void write(DataOutputBuffer out) {
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
		for (Ansi378Extension extension : extensions)
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
		Validate.int4(view, "View offset must be an unsigned 4-bit number.");
		Objects.requireNonNull(scanType, "Scan type must be non-null.");
		ValidateAnsi2009.quality(quality, "Fingerprint quality must be in range 0 through 100 or one of the special values 254 and 255.");
		Validate.nonzero16(qualityVendorId, "Quality algorithm vendor ID must a non-zero unsigned 16-bit number.");
		Validate.int16(qualityAlgorithmId, "Quality algorithm ID must an unsigned 16-bit number.");
		Validate.nonzero16(width, "Image width must be a non-zero unsigned 16-bit number.");
		Validate.nonzero16(height, "Image height must be a non-zero unsigned 16-bit number.");
		Validate.nonzero16(resolutionX, "Horizontal pixel density must be a non-zero unsigned 16-bit number.");
		Validate.nonzero16(resolutionY, "Vertical pixel density must be a non-zero unsigned 16-bit number.");
		Validate.int8(minutiae.size(), "There cannot be more than 255 minutiae.");
		for (Ansi378v2009Minutia minutia : minutiae)
			minutia.validate(width, height);
		if (counts != null)
			counts.validate(minutiae.size());
		if (coredelta != null)
			coredelta.validate(width, height);
		for (Ansi378Extension extension : extensions)
			extension.validate();
		Validate.int16(extensionBytes(), "Total size of all extension blocks must a 16-bit number.");
	}
	private int extensionBytes() {
		int bytes = extensions.stream().mapToInt(Ansi378Extension::measure).sum();
		if (counts != null)
			bytes += counts.measure();
		if (coredelta != null)
			bytes += coredelta.measure();
		return bytes;
	}
}
