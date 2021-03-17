// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

import java.io.*;
import java.util.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * ISO/IEC 19794-1:2011 base template.
 * 
 * @see <a href="https://templates.machinezoo.com/iso-19794-1-2011">ISO/IEC 19794-1:2011 Summary</a>
 */
public class Iso19794p1v2011Template {
	private static final Logger logger = LoggerFactory.getLogger(Iso19794p1v2011Template.class);
	/**
	 * Checks whether provided template is an instance of ISO/IEC 19794-1:2011 base template.
	 * This method does not do any template validation or conformance checking.
	 * It just differentiates ISO/IEC 19794-1:2011 from other template formats
	 * as quickly as possible, mostly by looking at template header.
	 * 
	 * @param template
	 *            serialized template that is to be evaluated
	 * @return {@code true} if {@code template} is an instance of ISO/IEC 19794-1:2011 base template, {@code false} otherwise
	 */
	public static boolean accepts(byte[] template) {
		if (template.length < 15)
			return false;
		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(template));
			for (int i = 0; i < 3; ++i) {
				int magicByte = in.readUnsignedByte();
				if (magicByte < 'A' || magicByte > 'Z')
					return false;
			}
			if (in.readUnsignedByte() != 0)
				return false;
			for (int i = 0; i < 3; ++i) {
				int versionByte = in.readUnsignedByte();
				if (versionByte < '0' || versionByte > '9')
					return false;
			}
			if (in.readUnsignedByte() != 0)
				return false;
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}
	/**
	 * File signature / magic number (<a href="https://templates.machinezoo.com/iso-19794-1-2011#magic">MAGIC</a>).
	 */
	public Iso19794p1v2011Modality modality;
	/**
	 * Format version / major (<a href="https://templates.machinezoo.com/iso-19794-1-2011#version">VERSION</a>).
	 * Major version number is stored in the first two bytes of the VERSION field.
	 * Defaults to 3.
	 */
	public int versionMajor = 3;
	/**
	 * Format version / minor (<a href="https://templates.machinezoo.com/iso-19794-1-2011#version">VERSION</a>).
	 * Minor version number is stored in the third byte of the VERSION field.
	 * Defaults to 0.
	 */
	public int versionMinor;
	/**
	 * Format-specific template header fields.
	 */
	public byte[] extra;
	/**
	 * List of biometric samples (<a href="https://templates.machinezoo.com/iso-19794-1-2011#sample">SAMPLE</a>).
	 */
	public List<Iso19794p1v2011Sample> samples = new ArrayList<>();
	/**
	 * Creates new ISO/IEC 19794-1:2011 template.
	 */
	public Iso19794p1v2011Template() {
	}
	/**
	 * Parses and validates ISO/IEC 19794-1:2011 template.
	 * 
	 * @param template
	 *            serialized template in ISO/IEC 19794-1:2011 format
	 * @param format
	 *            information about optional fields
	 * @throws TemplateFormatException
	 *             if the template cannot be parsed or it fails validation
	 */
	public Iso19794p1v2011Template(byte[] template, Iso19794p1v2011Format format) {
		this(template, true, format);
	}
	/**
	 * Parses and optionally validates ISO/IEC 19794-1:2011 template.
	 * 
	 * @param template
	 *            serialized template in ISO/IEC 19794-1:2011 format
	 * @param strict
	 *            {@code true} to validate the template, {@code false} to tolerate parsing errors as much as possible
	 * @param format
	 *            information about optional fields
	 * @throws TemplateFormatException
	 *             if the template cannot be parsed or if {@code strict} is {@code true} and the template fails validation
	 */
	public Iso19794p1v2011Template(byte[] template, boolean strict, Iso19794p1v2011Format format) {
		if (!accepts(template))
			throw new TemplateFormatException("This is not an ISO/IEC 19794-1:2011 biometric record.");
		TemplateUtils.decodeTemplate(template, in -> {
			byte[] magic = new byte[4];
			in.readFully(magic);
			modality = Arrays.stream(Iso19794p1v2011Modality.values()).filter(m -> Arrays.equals(m.magic, magic)).findFirst().orElse(null);
			ValidateTemplate.condition(modality != null, strict, "Unrecognized modality-specific file signature.");
			byte[] version = new byte[4];
			in.readFully(version);
			versionMajor = (version[0] - '0') * 10 + (version[1] = '0');
			versionMinor = version[2] - '0';
			long length = 0xffff_ffffL & in.readInt();
			ValidateTemplate.condition(length >= 29, strict, "Total length must be at least 29 bytes.");
			ValidateTemplate.condition(length <= 12 + in.available(), true, "Total length indicates trimmed template.");
			int count = in.readUnsignedShort();
			int certFlag = in.readUnsignedByte();
			ValidateTemplate.condition(certFlag < 2, strict, "Certification flag must be either 0 or 1.");
			if (!format.hasCertificates)
				ValidateTemplate.condition(certFlag == 0, strict, "Certificates cannot be present in this modality-specific format.");
			byte[] data = new byte[format.extraHeaderLength];
			in.readFully(data);
			for (int i = 0; i < count; ++i)
				samples.add(new Iso19794p1v2011Sample(in, strict, format, certFlag != 0));
			if (in.available() > 0)
				logger.debug("Ignored extra data at the end of the template.");
			ValidateTemplate.structure(() -> validate(format), strict);
		});
	}
	/**
	 * Validates and serializes the template in ISO/IEC 19794-1:2011 format.
	 * 
	 * @param format
	 *            information about optional fields
	 * @return serialized template in ISO/IEC 19794-1:2011 format
	 * @throws TemplateFormatException
	 *             if the template fails validation
	 */
	public byte[] toByteArray(Iso19794p1v2011Format format) {
		validate(format);
		TemplateWriter out = new TemplateWriter();
		out.write(modality.magic);
		out.writeByte(versionMajor / 10 + '0');
		out.writeByte(versionMajor % 10 + '0');
		out.writeByte(versionMinor + '0');
		out.writeByte(0);
		out.writeInt(measure(format));
		out.writeShort(samples.size());
		out.writeByte(format.hasCertificates ? 1 : 0);
		if (format.extraHeaderLength > 0)
			out.write(extra);
		for (Iso19794p1v2011Sample sample : samples)
			sample.write(out, format);
		return out.toByteArray();
	}
	private int measure(Iso19794p1v2011Format format) {
		return 15 + format.extraHeaderLength + samples.stream().mapToInt(s -> s.measure(format)).sum();
	}
	private void validate(Iso19794p1v2011Format format) {
		Objects.requireNonNull(modality, "Modality type must be non-null.");
		ValidateTemplate.range(versionMajor, 0, 99, "Major version must be in range 0 through 99.");
		ValidateTemplate.range(versionMinor, 0, 9, "Minor version must be in range 0 through 9.");
		if (format.extraHeaderLength > 0) {
			Objects.requireNonNull(extra, "Extra header data must be non-null.");
			ValidateTemplate.condition(extra.length == format.extraHeaderLength, "Extra header data must have correct length.");
		} else
			ValidateTemplate.condition(extra == null, "No extra header data is allowed.");
		ValidateTemplate.int16(samples.size(), "Biometric sample count must be a 16-bit unsigned number.");
		for (Iso19794p1v2011Sample sample : samples)
			sample.validate(format);
	}
}
