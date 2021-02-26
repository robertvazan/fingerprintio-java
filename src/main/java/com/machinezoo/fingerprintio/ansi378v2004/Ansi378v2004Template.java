// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

import static java.util.stream.Collectors.*;
import java.util.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

/*
 * Object model of ANSI INCITS 378-2004 template.
 */
public class Ansi378v2004Template {
	private static final Logger logger = LoggerFactory.getLogger(Ansi378v2004Template.class);
	private static final byte[] magic = new byte[] { 'F', 'M', 'R', 0, ' ', '2', '0', 0 };
	public static boolean accepts(byte[] template) {
		if (template.length < magic.length + 4)
			return false;
		if (!Arrays.equals(magic, Arrays.copyOf(template, magic.length)))
			return false;
		TemplateReader in = new TemplateReader(template);
		in.skipBytes(magic.length);
		/*
		 * Differentiate from ISO 19794-2 by examining the length field.
		 */
		int bytes01 = in.readUnsignedShort();
		if (bytes01 >= 26) {
			/*
			 * Too big for ISO 19794-2. It's indeed ANSI 378-2004 with 2-byte length field.
			 */
			return true;
		} else if (bytes01 > 0) {
			/*
			 * Invalid length field for ANSI 378. Must be ISO 19794-2.
			 */
			return false;
		} else {
			int bytes23 = in.readUnsignedShort();
			if (bytes23 >= 24) {
				/*
				 * Too big for ANSI 378. Must be ISO 19794-2.
				 */
				return false;
			} else {
				/*
				 * It's ANSI 378-2004 with 6-byte length field.
				 */
				return true;
			}
		}
	}
	public int vendorId = IbiaOrganizations.UNKNOWN;
	public int subformat;
	public boolean sensorCertified;
	public int sensorId;
	public int width;
	public int height;
	public int resolutionX;
	public int resolutionY;
	public List<Ansi378v2004Fingerprint> fingerprints = new ArrayList<>();
	public Ansi378v2004Template() {
	}
	public Ansi378v2004Template(byte[] template) {
		this(template, false);
	}
	public Ansi378v2004Template(byte[] template, boolean lax) {
		if (!accepts(template))
			throw new TemplateFormatException("This is not an ANSI INCITS 378-2004 template.");
		TemplateUtils.decodeTemplate(template, in -> {
			in.skipBytes(magic.length);
			skipLength(in);
			vendorId = in.readUnsignedShort();
			subformat = in.readUnsignedShort();
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
				fingerprints.add(new Ansi378v2004Fingerprint(in, lax));
			if (in.available() > 0)
				logger.debug("Ignored extra data at the end of the template.");
			ValidateTemplate.structure(this::validate, lax);
		});
	}
	private void skipLength(TemplateReader in) {
		int available = in.available();
		int length = in.readUnsignedShort();
		if (length == 0) {
			/*
			 * Zero 2-byte length means this template has a 6-byte length field.
			 */
			length = (in.readUnsignedShort() << 16) | in.readUnsignedShort();
			if (length < 0x10000)
				logger.debug("Not strictly compliant template: 6-byte length field should have value of at least 0x10000.");
		}
		ValidateTemplate.condition(length >= 26, "Total length must be at least 26 bytes.");
		ValidateTemplate.condition(length <= magic.length + available, true, "Total length indicates trimmed template.");
	}
	public byte[] toByteArray() {
		validate();
		TemplateWriter out = new TemplateWriter();
		out.write(magic);
		int length = measure();
		if (length < 0x10000)
			out.writeShort(length);
		else {
			out.writeShort(0);
			out.writeInt(length);
		}
		out.writeShort(vendorId);
		out.writeShort(subformat);
		out.writeShort((sensorCertified ? 0x8000 : 0) | sensorId);
		out.writeShort(width);
		out.writeShort(height);
		out.writeShort(resolutionX);
		out.writeShort(resolutionY);
		out.writeByte(fingerprints.size());
		out.writeByte(0);
		for (Ansi378v2004Fingerprint fp : fingerprints)
			fp.write(out);
		return out.toByteArray();
	}
	private int measure() {
		int length = 26;
		length += fingerprints.stream().mapToInt(Ansi378v2004Fingerprint::measure).sum();
		return length < 0x10000 ? length : length + 4;
	}
	private void validate() {
		ValidateTemplate.nonzero16(vendorId, "Vendor ID must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.int16(subformat, "Vendor subformat must be an unsigned 16-bit number.");
		ValidateTemplate.range(sensorId, 0, 0xfff, "Sensor ID must be an unsigned 12-bit number.");
		ValidateTemplate.nonzero16(width, "Image width must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(height, "Image height must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(resolutionX, "Horizontal pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.nonzero16(resolutionY, "Vertical pixel density must be a non-zero unsigned 16-bit number.");
		ValidateTemplate.int8(fingerprints.size(), "There cannot be more than 255 fingerprints.");
		for (Ansi378v2004Fingerprint fp : fingerprints)
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
