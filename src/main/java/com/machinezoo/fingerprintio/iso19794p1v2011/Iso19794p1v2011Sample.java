// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

import java.util.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Biometric sample (<a href="https://templates.machinezoo.com/iso-19794-1-2011#sample">SAMPLE</a>).
 */
public class Iso19794p1v2011Sample {
	/**
	 * Capture date and time (<a href="https://templates.machinezoo.com/iso-19794-1-2011#datetime">DATETIME</a>).
	 */
	public Iso19794p1v2011DateTime datetime = new Iso19794p1v2011DateTime();
	/**
	 * Sensor technology (<a href="https://templates.machinezoo.com/iso-19794-1-2011#devtech">DEVTECH</a>).
	 */
	public int sensorType;
	/**
	 * Sensor vendor ID (<a href="https://templates.machinezoo.com/iso-19794-1-2011#devvendor">DEVVENDOR</a>).
	 * Defaults to {@link IbiaOrganizations#UNKNOWN}.
	 */
	public int sensorVendor = IbiaOrganizations.UNKNOWN;
	/**
	 * Sensor ID (<a href="https://templates.machinezoo.com/iso-19794-1-2011#devid">DEVID</a>).
	 */
	public int sensorId;
	/**
	 * List of quality records (<a href="https://templates.machinezoo.com/iso-19794-1-2011#qrecord">QRECORD</a>).
	 */
	public List<Iso19794p1v2011Quality> qrecords = new ArrayList<>();
	/**
	 * List of certification records (<a href="https://templates.machinezoo.com/iso-19794-1-2011#certificate">CERTIFICATE</a>).
	 */
	public List<Iso19794p1v2011Certificate> certificates = new ArrayList<>();
	/**
	 * Format-specific sample header fields and biometric data.
	 */
	public byte[] data;
	/**
	 * Creates new biometric sample (<a href="https://templates.machinezoo.com/iso-19794-1-2011#sample">SAMPLE</a>).
	 */
	public Iso19794p1v2011Sample() {
	}
	Iso19794p1v2011Sample(TemplateReader in, boolean strict, Iso19794p1v2011Format format, boolean hasCertificates) {
		long length = 0xffff_ffffL & in.readInt();
		ValidateTemplate.condition(length >= 14, strict, "Length of biometric sample must be at least 14 bytes.");
		long remaining = in.available() - (length - 4);
		datetime = new Iso19794p1v2011DateTime(in);
		sensorType = in.readUnsignedByte();
		if (format.hasSensorVendor)
			sensorVendor = in.readUnsignedShort();
		if (format.hasSensorId)
			sensorId = in.readUnsignedShort();
		if (format.hasQuality) {
			int qcount = in.readUnsignedByte();
			for (int i = 0; i < qcount; ++i)
				qrecords.add(new Iso19794p1v2011Quality(in));
		}
		if (hasCertificates) {
			int certCount = in.readUnsignedByte();
			for (int i = 0; i < certCount; ++i)
				certificates.add(new Iso19794p1v2011Certificate(in));
		}
		ValidateTemplate.condition(in.available() >= remaining, "Sample length in bytes is not large enough to accommodate sample header.");
		data = new byte[(int)(in.available() - remaining)];
		in.readFully(data);
	}
	void write(TemplateWriter out, Iso19794p1v2011Format format) {
		out.writeInt(measure(format));
		datetime.write(out);
		out.writeByte(sensorType);
		if (format.hasSensorVendor)
			out.writeShort(sensorVendor);
		if (format.hasSensorId)
			out.writeShort(sensorId);
		if (format.hasQuality) {
			out.writeByte(qrecords.size());
			for (Iso19794p1v2011Quality quality : qrecords)
				quality.write(out);
		}
		if (format.hasCertificates) {
			out.writeByte(certificates.size());
			for (Iso19794p1v2011Certificate certificate : certificates)
				certificate.write(out);
		}
		out.write(data);
	}
	int measure(Iso19794p1v2011Format format) {
		int length = 14;
		if (format.hasSensorVendor)
			length += 2;
		if (format.hasSensorId)
			length += 2;
		if (format.hasQuality)
			++length;
		length += 5 * qrecords.size();
		if (format.hasCertificates)
			++length;
		length += 3 * certificates.size();
		length += data.length;
		return length;
	}
	void validate(Iso19794p1v2011Format format) {
		Objects.requireNonNull(datetime, "Timestamp must be non-null (even if filled with defaults).");
		datetime.validate();
		ValidateTemplate.int16(sensorVendor, "Sensor vendor must be a 16-bit unsigned number.");
		if (!format.hasSensorVendor)
			ValidateTemplate.condition(sensorVendor == 0, "Modality-specific format does not have sensor vendor field.");
		ValidateTemplate.int16(sensorId, "Sensor ID must be a 16-bit unsigned number.");
		if (!format.hasSensorId)
			ValidateTemplate.condition(sensorId == 0, "Modality-specific format does not have sensor ID field.");
		Objects.requireNonNull(qrecords, "List of quality records must be non-null.");
		for (Iso19794p1v2011Quality quality : qrecords)
			quality.validate();
		if (!format.hasQuality)
			ValidateTemplate.condition(qrecords.isEmpty(), "Modality-specific format does not have quality record fields.");
		Objects.requireNonNull(certificates, "List of certificates must be non-null.");
		for (Iso19794p1v2011Certificate certificate : certificates)
			certificate.validate();
		if (!format.hasCertificates)
			ValidateTemplate.condition(certificates.isEmpty(), "Modality-specific format does not have certificate records.");
		Objects.requireNonNull("Biometric data of the sample must be non-null.");
	}
}
