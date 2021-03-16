// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import java.util.*;
import java.util.function.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Fingerprint (<a href="https://templates.machinezoo.com/iso-19794-2-2005#fingerprint">FINGERPRINT</a>).
 */
public class Iso19794p2v2005Fingerprint {
	private static final Logger logger = LoggerFactory.getLogger(Iso19794p2v2005Fingerprint.class);
	/**
	 * Finger position on hands (<a href="https://templates.machinezoo.com/iso-19794-2-2005#position">POSITION</a>).
	 * Defaults to {@link Iso19794p2v2005Position#UNKNOWN}.
	 */
	public Iso19794p2v2005Position position = Iso19794p2v2005Position.UNKNOWN;
	/**
	 * Finger view number (<a href="https://templates.machinezoo.com/iso-19794-2-2005#viewoffset">VIEWOFFSET</a>).
	 */
	public int view;
	/**
	 * Impression type (<a href="https://templates.machinezoo.com/iso-19794-2-2005#sampletype">SAMPLETYPE</a>).
	 * Defaults to {@link Iso19794p2v2005ScanType#LIVE_PLAIN}.
	 */
	public Iso19794p2v2005ScanType scanType = Iso19794p2v2005ScanType.LIVE_PLAIN;
	/**
	 * Fingerprint quality (<a href="https://templates.machinezoo.com/iso-19794-2-2005#fpquality">FPQUALITY</a>).
	 * Defaults to 100.
	 */
	public int quality = 100;
	/**
	 * List of minutiae (<a href="https://templates.machinezoo.com/iso-19794-2-2005#minutia">MINUTIA</a>).
	 */
	public List<Iso19794p2v2005Minutia> minutiae = new ArrayList<>();
	/**
	 * Ridge count extension (<a href="https://templates.machinezoo.com/iso-19794-2-2005#rcountext">RCOUNTEXT</a>).
	 * This field is {@code null} if ridge count extension is not present.
	 */
	public Iso19794p2v2005CountExtension counts;
	/**
	 * Core and delta extension (<a href="https://templates.machinezoo.com/iso-19794-2-2005#coredelta">COREDELTA</a>).
	 * This field is {@code null} if core and delta extension is not present.
	 */
	public Iso19794p2v2005CoreDeltaExtension coredelta;
	/**
	 * Zonal quality extension (<a href="https://templates.machinezoo.com/iso-19794-2-2005#zonalext">ZONALEXT</a>).
	 * This field is {@code null} if zonal quality extension is not present.
	 */
	public Iso19794p2v2005ZonalExtension zones;
	/**
	 * List of extension data blocks (<a href="https://templates.machinezoo.com/iso-19794-2-2005#extension">EXTENSION</a>).
	 */
	public List<Iso19794p2v2005Extension> extensions = new ArrayList<>();
	/**
	 * Creates new fingerprint (<a href="https://templates.machinezoo.com/iso-19794-2-2005#fingerprint">FINGERPRINT</a>).
	 */
	public Iso19794p2v2005Fingerprint() {
	}
	Iso19794p2v2005Fingerprint(TemplateReader in, int width, int height, boolean strict) {
		position = TemplateUtils.decodeType(in.readUnsignedByte(), Iso19794p2v2005Position.class, strict, "Unrecognized finger position code.");
		int offsetAndType = in.readUnsignedByte();
		view = offsetAndType >> 4;
		scanType = TemplateUtils.decodeType(offsetAndType & 0xf, Iso19794p2v2005ScanType.values(), t -> t.code, strict, "Unrecognized sensor type code.");
		quality = in.readUnsignedByte();
		int count = in.readUnsignedByte();
		for (int i = 0; i < count; ++i)
			minutiae.add(new Iso19794p2v2005Minutia(in, strict));
		int totalBytes = in.readUnsignedShort();
		byte[] extensionBlock = new byte[totalBytes];
		in.readFully(extensionBlock);
		try {
			TemplateUtils.decodeBytes(extensionBlock, "Unexpected end of extension data. Likely misinterpretation of extension lengths.", inx -> {
				while (inx.available() > 0) {
					Iso19794p2v2005Extension extension = new Iso19794p2v2005Extension(inx);
					if (extension.type == Iso19794p2v2005CountExtension.IDENTIFIER)
						decodeExtension(extension, data -> counts = new Iso19794p2v2005CountExtension(data, strict), strict, "Unable to decode ridge count extension.");
					else if (extension.type == Iso19794p2v2005CoreDeltaExtension.IDENTIFIER)
						decodeExtension(extension, data -> coredelta = new Iso19794p2v2005CoreDeltaExtension(data, strict), strict, "Unable to decode core/delta extension.");
					else if (extension.type == Iso19794p2v2005ZonalExtension.IDENTIFIER)
						decodeExtension(extension, data -> zones = new Iso19794p2v2005ZonalExtension(data, width, height, strict), strict, "Unable to decode zonal quality extension.");
					else
						extensions.add(extension);
				}
			});
		} catch (Throwable ex) {
			if (!strict)
				throw ex;
			logger.warn("Failed to parse extension data.", ex);
		}
	}
	private void decodeExtension(Iso19794p2v2005Extension extension, Consumer<byte[]> decoder, boolean strict, String message) {
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
		out.writeByte((view << 4) | scanType.code);
		out.writeByte(quality);
		out.writeByte(minutiae.size());
		for (Iso19794p2v2005Minutia minutia : minutiae)
			minutia.write(out);
		out.writeShort(extensionBytes());
		if (counts != null)
			counts.extension().write(out);
		if (coredelta != null)
			coredelta.extension().write(out);
		if (zones != null)
			zones.extension().write(out);
		for (Iso19794p2v2005Extension extension : extensions)
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
		for (Iso19794p2v2005Minutia minutia : minutiae)
			minutia.validate(width, height);
		if (counts != null)
			counts.validate(minutiae.size());
		if (coredelta != null)
			coredelta.validate(width, height);
		if (zones != null)
			zones.validate(width, height);
		for (Iso19794p2v2005Extension extension : extensions)
			extension.validate();
		ValidateTemplate.int16(extensionBytes(), "Total size of all extension blocks must a 16-bit number.");
	}
	private int extensionBytes() {
		int bytes = extensions.stream().mapToInt(Iso19794p2v2005Extension::measure).sum();
		if (counts != null)
			bytes += counts.measure();
		if (coredelta != null)
			bytes += coredelta.measure();
		if (zones != null)
			bytes += zones.measure();
		return bytes;
	}
}
