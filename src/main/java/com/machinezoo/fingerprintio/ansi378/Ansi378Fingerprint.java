// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import java.util.*;
import java.util.function.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378Fingerprint {
	private static final Logger logger = LoggerFactory.getLogger(Ansi378Fingerprint.class);
	public Ansi378Position position = Ansi378Position.UNKNOWN;
	public int view;
	public Ansi378ScanType scanType = Ansi378ScanType.LIVE_PLAIN;
	public int quality = 100;
	public List<Ansi378Minutia> minutiae = new ArrayList<>();
	public Ansi378CountExtension counts;
	public Ansi378CoreDeltaExtension coredelta;
	public List<Ansi378Extension> extensions = new ArrayList<>();
	public Ansi378Fingerprint() {
	}
	Ansi378Fingerprint(DataInputBuffer in, boolean lax) {
		position = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378Position.class, lax, "Unrecognized finger position code.");
		int offsetAndType = in.readUnsignedByte();
		view = offsetAndType >> 4;
		scanType = TemplateUtils.decodeType(offsetAndType & 0xf, Ansi378ScanType.values(), t -> t.code, lax, "Unrecognized sensor type code.");
		quality = in.readUnsignedByte();
		int count = in.readUnsignedByte();
		for (int i = 0; i < count; ++i)
			minutiae.add(new Ansi378Minutia(in, lax));
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
		out.writeByte((view << 4) | scanType.code);
		out.writeByte(quality);
		out.writeByte(minutiae.size());
		for (Ansi378Minutia minutia : minutiae)
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
		int length = 6;
		length += 6 * minutiae.size();
		length += extensionBytes();
		return length;
	}
	void validate(int width, int height) {
		Objects.requireNonNull(position, "Finger position must be non-null (even if unknown).");
		Validate.int4(view, "View offset must be an unsigned 4-bit number.");
		Objects.requireNonNull(scanType, "Scan type must be non-null.");
		Validate.range(quality, 0, 100, "Fingerprint quality must be in range 0 through 100.");
		Validate.int8(minutiae.size(), "There cannot be more than 255 minutiae.");
		for (Ansi378Minutia minutia : minutiae)
			minutia.validate(width, height);
		if (counts != null)
			counts.validate(minutiae.size());
		if (coredelta != null)
			coredelta.validate(width, height);
		for (Ansi378Extension extension : extensions)
			extension.validate();
		Validate.int16(extensionBytes(), "Total size of all extension blocks must a 16-bit number.");
		if (coredelta != null && coredelta.cores.isEmpty())
			logger.debug("Not strictly compliant template. Core count is zero.");
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
