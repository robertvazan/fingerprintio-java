// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

import java.util.*;
import java.util.function.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378v2009Am1Fingerprint {
	private static final Logger logger = LoggerFactory.getLogger(Ansi378v2009Am1Fingerprint.class);
	public Ansi378v2009Am1Position position = Ansi378v2009Am1Position.UNKNOWN;
	public int view;
	public Ansi378v2009Am1ScanType scanType = Ansi378v2009Am1ScanType.LIVE_PLAIN;
	public int quality = 254;
	public int qualityVendorId = IbiaOrganizations.UNKNOWN;
	public int qualityAlgorithmId = IbiaQualityAlgorithms.UNKNOWN;
	public int width;
	public int height;
	public int resolutionX;
	public int resolutionY;
	public List<Ansi378v2009Am1Minutia> minutiae = new ArrayList<>();
	public Ansi378v2009Am1CountExtension counts;
	public Ansi378v2009Am1CoreDeltaExtension coredelta;
	public List<Ansi378v2009Am1Extension> extensions = new ArrayList<>();
	public Ansi378v2009Am1Fingerprint() {
	}
	Ansi378v2009Am1Fingerprint(TemplateReader in, boolean strict) {
		position = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378v2009Am1Position.class, strict, "Unrecognized finger position code.");
		view = in.readUnsignedByte();
		scanType = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378v2009Am1ScanType.values(), t -> t.code, strict, "Unrecognized sensor type code.");
		quality = in.readUnsignedByte();
		qualityVendorId = in.readUnsignedShort();
		qualityAlgorithmId = in.readUnsignedShort();
		width = in.readUnsignedShort();
		height = in.readUnsignedShort();
		resolutionX = in.readUnsignedShort();
		resolutionY = in.readUnsignedShort();
		int count = in.readUnsignedByte();
		for (int i = 0; i < count; ++i)
			minutiae.add(new Ansi378v2009Am1Minutia(in, strict));
		int totalBytes = in.readUnsignedShort();
		int readBytes = 0;
		while (readBytes < totalBytes) {
			Ansi378v2009Am1Extension extension = new Ansi378v2009Am1Extension(in);
			if (extension.type == Ansi378v2009Am1CountExtension.IDENTIFIER)
				decodeExtension(extension, data -> counts = new Ansi378v2009Am1CountExtension(data, strict), strict, "Unable to decode ridge count extension.");
			else if (extension.type == Ansi378v2009Am1CoreDeltaExtension.IDENTIFIER)
				decodeExtension(extension, data -> coredelta = new Ansi378v2009Am1CoreDeltaExtension(data, strict), strict, "Unable to decode core/delta extension.");
			else
				extensions.add(extension);
			readBytes += extension.measure();
		}
		ValidateTemplate.condition(readBytes == totalBytes, strict, "Total length of extension data doesn't match the sum of extension block lengths.");
	}
	private void decodeExtension(Ansi378v2009Am1Extension extension, Consumer<byte[]> decoder, boolean strict, String message) {
		try {
			decoder.accept(extension.data);
		} catch (Throwable ex) {
			if (strict)
				throw ex;
			logger.warn(message, ex);
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
		for (Ansi378v2009Am1Minutia minutia : minutiae)
			minutia.write(out);
		out.writeShort(extensionBytes());
		if (counts != null)
			counts.extension().write(out);
		if (coredelta != null)
			coredelta.extension().write(out);
		for (Ansi378v2009Am1Extension extension : extensions)
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
		ValidateTemplate.nonzero16(qualityVendorId, "Quality algorithm vendor ID must a nonzero unsigned 16-bit number.");
		ValidateTemplate.int16(qualityAlgorithmId, "Quality algorithm ID must an unsigned 16-bit number.");
		ValidateTemplate.nonzero16(width, "Image width must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(height, "Image height must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(resolutionX, "Horizontal pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(resolutionY, "Vertical pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.int8(minutiae.size(), "There cannot be more than 255 minutiae.");
		for (Ansi378v2009Am1Minutia minutia : minutiae)
			minutia.validate(width, height);
		if (counts != null)
			counts.validate(minutiae.size());
		if (coredelta != null)
			coredelta.validate(width, height);
		for (Ansi378v2009Am1Extension extension : extensions)
			extension.validate();
		ValidateTemplate.int16(extensionBytes(), "Total size of all extension blocks must a 16-bit number.");
	}
	private int extensionBytes() {
		int bytes = extensions.stream().mapToInt(Ansi378v2009Am1Extension::measure).sum();
		if (counts != null)
			bytes += counts.measure();
		if (coredelta != null)
			bytes += coredelta.measure();
		return bytes;
	}
}
