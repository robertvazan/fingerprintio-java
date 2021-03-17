// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import static java.util.stream.Collectors.*;
import java.util.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * ISO/IEC 19794-2:2005 template.
 * 
 * @see <a href="https://templates.machinezoo.com/iso-19794-2-2005">ISO/IEC 19794-2:2005 Summary</a>
 */
public class Iso19794p2v2005Template {
	private static final Logger logger = LoggerFactory.getLogger(Iso19794p2v2005Template.class);
	private static final byte[] magic = new byte[] { 'F', 'M', 'R', 0, ' ', '2', '0', 0 };
	/**
	 * Checks whether provided template is an ISO/IEC 19794-2:2005 template.
	 * This method does not do any template validation or conformance checking.
	 * It just differentiates ISO/IEC 19794-2:2005 from other template formats
	 * as quickly as possible, mostly by looking at template header.
	 * 
	 * @param template
	 *            serialized template that is to be evaluated
	 * @return {@code true} if {@code template} is an ISO/IEC 19794-2:2005 template, {@code false} otherwise
	 */
	public static boolean accepts(byte[] template) {
		if (template.length < magic.length + 4)
			return false;
		if (!Arrays.equals(magic, Arrays.copyOf(template, magic.length)))
			return false;
		TemplateReader in = new TemplateReader(template);
		in.skipBytes(magic.length);
		/*
		 * Differentiate from ANSI 378 by examining the length field.
		 */
		int bytes01 = in.readUnsignedShort();
		if (bytes01 >= 26) {
			/*
			 * Too big for ISO 19794-2. Must be ANSI 378-2004 with 2-byte length field.
			 */
			return false;
		} else if (bytes01 > 0) {
			/*
			 * Invalid length field for ANSI 378. Must be ISO 19794-2.
			 */
			return true;
		} else {
			int bytes23 = in.readUnsignedShort();
			if (bytes23 >= 24) {
				/*
				 * Too big for ANSI 378. Must be ISO 19794-2.
				 */
				return true;
			} else {
				/*
				 * It's ANSI 378-2004 with 6-byte length field.
				 */
				return false;
			}
		}
	}
	/**
	 * Indicates that the fingerprint reader has certificate of compliance with Annex B of ISO 19794-2 spec,
	 * which is a copy of Appendix F of CJIS-RS-0010 V7.
	 * This is the top bit of <a href="https://templates.machinezoo.com/iso-19794-2-2005#devstamp">DEVSTAMP</a> field.
	 */
	public boolean sensorCertified;
	/**
	 * Sensor ID (<a href="https://templates.machinezoo.com/iso-19794-2-2005#devid">DEVID</a>).
	 */
	public int sensorId;
	/**
	 * Image width (<a href="https://templates.machinezoo.com/iso-19794-2-2005#width">WIDTH</a>).
	 */
	public int width;
	/**
	 * Image height (<a href="https://templates.machinezoo.com/iso-19794-2-2005#height">HEIGHT</a>).
	 */
	public int height;
	/**
	 * Horizontal pixel density (<a href="https://templates.machinezoo.com/iso-19794-2-2005#resolutionx">RESOLUTIONX</a>).
	 * Defaults to 197 (500dpi).
	 */
	public int resolutionX = 197;
	/**
	 * Vertical pixel density (<a href="https://templates.machinezoo.com/iso-19794-2-2005#resolutiony">RESOLUTIONY</a>).
	 * Defaults to 197 (500dpi).
	 */
	public int resolutionY = 197;
	/**
	 * List of fingerprints (<a href="https://templates.machinezoo.com/iso-19794-2-2005#fingerprint">FINGERPRINT</a>).
	 */
	public List<Iso19794p2v2005Fingerprint> fingerprints = new ArrayList<>();
	/**
	 * Creates new ISO/IEC 19794-2:2005 template.
	 */
	public Iso19794p2v2005Template() {
	}
	/**
	 * Parses and validates ISO/IEC 19794-2:2005 template.
	 * 
	 * @param template
	 *            serialized template in ISO/IEC 19794-2:2005 format
	 * @throws TemplateFormatException
	 *             if the template cannot be parsed or it fails validation
	 */
	public Iso19794p2v2005Template(byte[] template) {
		this(template, true);
	}
	/**
	 * Parses and optionally validates ISO/IEC 19794-2:2005 template.
	 * 
	 * @param template
	 *            serialized template in ISO/IEC 19794-2:2005 format
	 * @param strict
	 *            {@code true} to validate the template, {@code false} to tolerate parsing errors as much as possible
	 * @throws TemplateFormatException
	 *             if the template cannot be parsed or if {@code strict} is {@code true} and the template fails validation
	 */
	public Iso19794p2v2005Template(byte[] template, boolean strict) {
		if (!accepts(template))
			throw new TemplateFormatException("This is not an ISO/IEC 19794-2:2005 template.");
		TemplateUtils.decodeTemplate(template, in -> {
			in.skipBytes(magic.length);
			long length = 0xffff_ffffL & in.readInt();
			ValidateTemplate.condition(length >= 24, "Total length must be at least 24 bytes.");
			ValidateTemplate.condition(length <= magic.length + 4 + in.available(), true, "Total length indicates trimmed template.");
			sensorId = in.readUnsignedShort();
			sensorCertified = (sensorId & 0x8000) != 0;
			if ((sensorId & 0x7000) != 0)
				logger.warn("Ignoring unrecognized sensor compliance bits.");
			sensorId &= 0xfff;
			width = in.readUnsignedShort();
			height = in.readUnsignedShort();
			resolutionX = in.readUnsignedShort();
			resolutionY = in.readUnsignedShort();
			int count = in.readUnsignedByte();
			in.skipBytes(1);
			for (int i = 0; i < count; ++i)
				fingerprints.add(new Iso19794p2v2005Fingerprint(in, width, height, strict));
			if (in.available() > 0)
				logger.debug("Ignored extra data at the end of the template.");
			ValidateTemplate.structure(this::validate, strict);
		});
	}
	/**
	 * Validates and serializes the template in ISO/IEC 19794-2:2005 format.
	 * 
	 * @return serialized template in ISO/IEC 19794-2:2005 format
	 * @throws TemplateFormatException
	 *             if the template fails validation
	 */
	public byte[] toByteArray() {
		validate();
		TemplateWriter out = new TemplateWriter();
		out.write(magic);
		out.writeInt(measure());
		out.writeShort((sensorCertified ? 0x8000 : 0) | sensorId);
		out.writeShort(width);
		out.writeShort(height);
		out.writeShort(resolutionX);
		out.writeShort(resolutionY);
		out.writeByte(fingerprints.size());
		out.writeByte(0);
		for (Iso19794p2v2005Fingerprint fp : fingerprints)
			fp.write(out);
		return out.toByteArray();
	}
	private int measure() {
		return 24 + fingerprints.stream().mapToInt(Iso19794p2v2005Fingerprint::measure).sum();
	}
	private void validate() {
		ValidateTemplate.range(sensorId, 0, 0xfff, "Sensor ID must be an unsigned 12-bit number.");
		ValidateTemplate.nonzero16(width, "Image width must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(height, "Image height must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(resolutionX, "Horizontal pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.condition(resolutionX >= 99, "Horizontal pixel density must be at least 99 (DPI 250+).");
		ValidateTemplate.nonzero16(resolutionY, "Vertical pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.condition(resolutionY >= 99, "Vertical pixel density must be at least 99 (DPI 250+).");
		ValidateTemplate.range(fingerprints.size(), 0, 176, "There cannot be more than 176 fingerprints.");
		for (Iso19794p2v2005Fingerprint fp : fingerprints)
			fp.validate(width, height);
		if (fingerprints.size() != fingerprints.stream().mapToInt(fp -> (fp.position.ordinal() << 16) + fp.view).distinct().count())
			throw new TemplateFormatException("Every fingerprint must have a unique combination of finger position and view offset.");
		fingerprints.stream()
			.collect(groupingBy(fp -> fp.position))
			.values().stream()
			.forEach(l -> {
				for (int i = 0; i < l.size(); ++i) {
					ValidateTemplate.range(l.get(i).view, 0, l.size() - 1, "Fingerprint view numbers must be assigned contiguously, starting from zero.");
					if (!l.equals(l.stream().sorted(Comparator.comparingInt(fp -> fp.view)).collect(toList())))
						throw new TemplateFormatException("Fingerprints with the same finger position must be sorted by view number.");
				}
			});
	}
}
