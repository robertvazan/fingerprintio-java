// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.iso19794p1v2011.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Fingerprint (<a href="https://templates.machinezoo.com/iso-19794-2-2011#fingerprint">FINGERPRINT</a>).
 */
public class Iso19794p2v2011Fingerprint {
	private static final Logger logger = LoggerFactory.getLogger(Iso19794p2v2011Fingerprint.class);
	/**
	 * Capture date and time (<a href="https://templates.machinezoo.com/iso-19794-2-2011#datetime">DATETIME</a>).
	 * Defaults to no date/time (0xff filler).
	 */
	public Iso19794p1v2011DateTime datetime = new Iso19794p1v2011DateTime();
	/**
	 * Sensor technology (<a href="https://templates.machinezoo.com/iso-19794-2-2011#devtech">DEVTECH</a>).
	 * Defaults to {@link Iso19794p2v2011SensorType#UNKNOWN}.
	 */
	public Iso19794p2v2011SensorType sensorType = Iso19794p2v2011SensorType.UNKNOWN;
	/**
	 * Sensor vendor ID (<a href="https://templates.machinezoo.com/iso-19794-2-2011#devvendor">DEVVENDOR</a>).
	 * Defaults to zero (unknown vendor).
	 */
	public int sensorVendor;
	/**
	 * Sensor ID (<a href="https://templates.machinezoo.com/iso-19794-2-2011#devid">DEVID</a>).
	 */
	public int sensorId;
	/**
	 * List of quality records (<a href="https://templates.machinezoo.com/iso-19794-2-2011#qrecord">QRECORD</a>).
	 */
	public List<Iso19794p1v2011Quality> qrecords = new ArrayList<>();
	/**
	 * List of certification records (<a href="https://templates.machinezoo.com/iso-19794-2-2011#certificate">CERTIFICATE</a>).
	 */
	public List<Iso19794p2v2011Certificate> certificates = new ArrayList<>();
	/**
	 * Finger position on hands (<a href="https://templates.machinezoo.com/iso-19794-2-2011#position">POSITION</a>).
	 * Defaults to {@link Iso19794p2v2011Position#UNKNOWN}.
	 */
	public Iso19794p2v2011Position position = Iso19794p2v2011Position.UNKNOWN;
	/**
	 * Finger view number (<a href="https://templates.machinezoo.com/iso-19794-2-2011#viewoffset">VIEWOFFSET</a>).
	 */
	public int view;
	/**
	 * Horizontal pixel density (<a href="https://templates.machinezoo.com/iso-19794-2-2011#resolutionx">RESOLUTIONX</a>).
	 * Defaults to 197 (500dpi).
	 */
	public int resolutionX = 197;
	/**
	 * Vertical pixel density (<a href="https://templates.machinezoo.com/iso-19794-2-2011#resolutiony">RESOLUTIONY</a>).
	 * Defaults to 197 (500dpi).
	 */
	public int resolutionY = 197;
	/**
	 * Impression type (<a href="https://templates.machinezoo.com/iso-19794-2-2011#sampletype">SAMPLETYPE</a>).
	 * Defaults to {@link Iso19794p2v2011ScanType#LIVE_PLAIN}.
	 */
	public Iso19794p2v2011ScanType scanType = Iso19794p2v2011ScanType.LIVE_PLAIN;
	/**
	 * Image width (<a href="https://templates.machinezoo.com/iso-19794-2-2011#width">WIDTH</a>).
	 */
	public int width;
	/**
	 * Image height (<a href="https://templates.machinezoo.com/iso-19794-2-2011#height">HEIGHT</a>).
	 */
	public int height;
	/**
	 * Ridge ending type (<a href="https://templates.machinezoo.com/iso-19794-2-2011#endingtype">ENDINGTYPE</a>).
	 * Defaults to {@link Iso19794p2v2011EndingType#VALLEY_SKELETON_BIFURCATION}.
	 */
	public Iso19794p2v2011EndingType endingType = Iso19794p2v2011EndingType.VALLEY_SKELETON_BIFURCATION;
	/**
	 * List of minutiae (<a href="https://templates.machinezoo.com/iso-19794-2-2011#minutia">MINUTIA</a>).
	 */
	public List<Iso19794p2v2011Minutia> minutiae = new ArrayList<>();
	/**
	 * Ridge count extension (<a href="https://templates.machinezoo.com/iso-19794-2-2011#rcountext">RCOUNTEXT</a>).
	 * This field is {@code null} if ridge count extension is not present.
	 */
	public Iso19794p2v2011CountExtension counts;
	/**
	 * Core and delta extension (<a href="https://templates.machinezoo.com/iso-19794-2-2011#coredelta">COREDELTA</a>).
	 * This field is {@code null} if core and delta extension is not present.
	 */
	public Iso19794p2v2011CoreDeltaExtension coredelta;
	/**
	 * Zonal quality extension (<a href="https://templates.machinezoo.com/iso-19794-2-2011#zonalext">ZONALEXT</a>).
	 * This field is {@code null} if zonal quality extension is not present.
	 */
	public Iso19794p2v2011ZonalExtension zones;
	/**
	 * List of extension data blocks (<a href="https://templates.machinezoo.com/iso-19794-2-2011#extension">EXTENSION</a>).
	 */
	public List<Iso19794p2v2011Extension> extensions = new ArrayList<>();
	/**
	 * Creates new fingerprint (<a href="https://templates.machinezoo.com/iso-19794-2-2011#fingerprint">FINGERPRINT</a>).
	 */
	public Iso19794p2v2011Fingerprint() {
	}
	Iso19794p2v2011Fingerprint(Iso19794p1v2011Sample sample, boolean strict) {
		datetime = sample.datetime;
		sensorType = TemplateUtils.decodeType(sample.sensorType, Iso19794p2v2011SensorType.class, strict, "Unrecognized sensor type.");
		sensorVendor = sample.sensorVendor;
		sensorId = sample.sensorId;
		qrecords = sample.qrecords;
		certificates = sample.certificates.stream()
			.map(c -> new Iso19794p2v2011Certificate(c, strict))
			.collect(toList());
		TemplateUtils.decodeBytes(sample.data, "Unexpected end of fingerprint block.", in -> {
			position = TemplateUtils.decodeType(in.readUnsignedByte(), Iso19794p2v2011Position.class, strict, "Unrecognized finger position code.");
			view = in.readUnsignedByte();
			resolutionX = in.readUnsignedShort();
			resolutionY = in.readUnsignedShort();
			scanType = TemplateUtils.decodeType(in.readUnsignedByte(), Iso19794p2v2011ScanType.values(), t -> t.code, strict, "Unrecognized sensor type code.");
			width = in.readUnsignedShort();
			height = in.readUnsignedShort();
			int flags = in.readUnsignedByte();
			int minBytes = flags >> 4;
			ValidateTemplate.condition(minBytes == 5 || minBytes == 6, strict, "Minutia record size must be either 5 or 6 bytes.");
			endingType = TemplateUtils.decodeType(flags & 0xf, Iso19794p2v2011EndingType.class, strict, "Unrecognized ridge ending type code.");
			int count = in.readUnsignedByte();
			for (int i = 0; i < count; ++i)
				minutiae.add(new Iso19794p2v2011Minutia(in, minBytes == 6, strict));
			int totalBytes = in.readUnsignedShort();
			int readBytes = 0;
			while (readBytes < totalBytes) {
				Iso19794p2v2011Extension extension = new Iso19794p2v2011Extension(in);
				readBytes += extension.measure();
				if (extension.type == Iso19794p2v2011CountExtension.IDENTIFIER)
					decodeExtension(extension, data -> counts = new Iso19794p2v2011CountExtension(data, strict), strict, "Unable to decode ridge count extension.");
				else if (extension.type == Iso19794p2v2011CoreDeltaExtension.IDENTIFIER)
					decodeExtension(extension, data -> coredelta = new Iso19794p2v2011CoreDeltaExtension(data, strict), strict, "Unable to decode core/delta extension.");
				else if (extension.type == Iso19794p2v2011ZonalExtension.IDENTIFIER)
					decodeExtension(extension, data -> zones = new Iso19794p2v2011ZonalExtension(data, width, height, strict), strict, "Unable to decode zonal quality extension.");
				else
					extensions.add(extension);
			}
			ValidateTemplate.condition(readBytes == totalBytes, strict, "Total length of extension data does not match the sum of extension block lengths.");
			ValidateTemplate.condition(in.available() == 0, "Fingerprint length field value doesn't match natural end of the fingerprint.");
		});
	}
	private void decodeExtension(Iso19794p2v2011Extension extension, Consumer<byte[]> decoder, boolean strict, String message) {
		try {
			decoder.accept(extension.data);
		} catch (Throwable ex) {
			if (strict)
				throw ex;
			logger.warn(message, ex);
			extensions.add(extension);
		}
	}
	private boolean hasMinutiaQuality() {
		return minutiae.stream().anyMatch(m -> m.quality != 254);
	}
	Iso19794p1v2011Sample toSample() {
		Iso19794p1v2011Sample sample = new Iso19794p1v2011Sample();
		sample.datetime = datetime;
		sample.sensorType = sensorType.ordinal();
		sample.sensorVendor = sensorVendor;
		sample.sensorId = sensorId;
		sample.qrecords = qrecords;
		sample.certificates = certificates.stream()
			.map(c -> c.toCertificate())
			.collect(toList());
		TemplateWriter out = new TemplateWriter();
		out.writeByte(position.ordinal());
		out.writeByte(view);
		out.writeShort(resolutionX);
		out.writeShort(resolutionY);
		out.writeByte(scanType.code);
		out.writeShort(width);
		out.writeShort(height);
		boolean hasMinutiaQuality = hasMinutiaQuality();
		out.write(((hasMinutiaQuality ? 6 : 5) << 4) | endingType.ordinal());
		out.writeByte(minutiae.size());
		for (Iso19794p2v2011Minutia minutia : minutiae)
			minutia.write(out, hasMinutiaQuality);
		out.writeShort(extensionBytes());
		if (counts != null)
			counts.extension().write(out);
		if (coredelta != null)
			coredelta.extension().write(out);
		if (zones != null)
			zones.extension().write(out);
		for (Iso19794p2v2011Extension extension : extensions)
			extension.write(out);
		sample.data = out.toByteArray();
		return sample;
	}
	void validate() {
		Objects.requireNonNull(sensorType, "Sensor type must be non-null (even if unknown).");
		ValidateTemplate.int16(sensorVendor, "Sensor vendor ID must be an unsigned 16-bit number.");
		ValidateTemplate.int16(sensorId, "Sensor ID must be an unsigned 16-bit number.");
		Objects.requireNonNull(position, "Finger position must be non-null.");
		ValidateTemplate.int4(view, "View offset must be an unsigned 4-bit number.");
		ValidateTemplate.nonzero16(resolutionX, "Horizontal pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.condition(resolutionX >= 99, "Horizontal pixel density must be at least 99 (DPI 250+).");
		ValidateTemplate.nonzero16(resolutionY, "Vertical pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.condition(resolutionY >= 99, "Vertical pixel density must be at least 99 (DPI 250+).");
		Objects.requireNonNull(scanType, "Scan type must be non-null.");
		ValidateTemplate.nonzero14(width, "Image width must be a non-zero unsigned 14-bit number.");
		ValidateTemplate.nonzero14(height, "Image height must be a non-zero unsigned 14-bit number.");
		Objects.requireNonNull(endingType, "Ridge ending type must be non-null.");
		ValidateTemplate.nonzero8(minutiae.size(), "Minutia count must be in range 1 through 255.");
		for (Iso19794p2v2011Minutia minutia : minutiae)
			minutia.validate(width, height);
		if (counts != null)
			counts.validate(minutiae.size());
		if (coredelta != null)
			coredelta.validate(width, height);
		if (zones != null)
			zones.validate(width, height);
		for (Iso19794p2v2011Extension extension : extensions)
			extension.validate();
		ValidateTemplate.int16(extensionBytes(), "Total size of all extension blocks must a 16-bit number.");
	}
	private int extensionBytes() {
		int bytes = extensions.stream().mapToInt(Iso19794p2v2011Extension::measure).sum();
		if (counts != null)
			bytes += counts.measure();
		if (coredelta != null)
			bytes += coredelta.measure();
		if (zones != null)
			bytes += zones.measure();
		return bytes;
	}
}
