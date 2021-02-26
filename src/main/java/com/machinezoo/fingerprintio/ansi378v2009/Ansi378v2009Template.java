// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009;

import static java.util.stream.Collectors.*;
import java.io.*;
import java.util.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.ansi378v2009am1.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

/*
 * Object model of ANSI INCITS 378-2009 template.
 */
public class Ansi378v2009Template {
	private static final Logger logger = LoggerFactory.getLogger(Ansi378v2009Template.class);
	private static final byte[] magic = new byte[] { 'F', 'M', 'R', 0, '0', '3', '0', 0 };
	public static boolean accepts(byte[] template) {
		if (!Arrays.equals(magic, Arrays.copyOf(template, magic.length)))
			return false;
		/*
		 * We differentiate the format from ISO 19794-2:2011 by failing to interpret the data as ISO 19794-2:2011,
		 * which is why the code below is copied from Iso19794p2v2001Template with return values inverted.
		 */
		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(template));
			in.skip(magic.length);
			long total = 0xffff_ffffL & in.readInt();
			int count = in.readUnsignedShort();
			in.skip(1);
			long sum = 0;
			for (int i = 0; i < count; ++i) {
				long length = 0xffff_ffffL & in.readInt();
				if (length < 34)
					return true;
				sum += length;
				in.skip(length - 4);
			}
			return total != 15 + sum;
		} catch (Throwable ex) {
			return true;
		}
	}
	public int vendorId = BiometricOrganizations.UNKNOWN;
	public int subformat;
	public boolean sensorCertified;
	public int sensorId;
	public List<Ansi378v2009Fingerprint> fingerprints = new ArrayList<>();
	public Ansi378v2009Template() {
	}
	public Ansi378v2009Template(byte[] template) {
		this(template, false);
	}
	public Ansi378v2009Template(byte[] template, boolean lax) {
		if (!accepts(template)) {
			if (lax && Ansi378v2009Am1Template.accepts(template)) {
				template = Arrays.copyOf(template, template.length);
				template[6] = '0';
			} else
				throw new TemplateFormatException("This is not an ANSI INCITS 378-2009 template.");
		}
		TemplateUtils.decodeTemplate(template, in -> {
			in.skipBytes(magic.length);
			long length = 0xffff_ffffL & in.readInt();
			Validate.condition(length >= 21, "Total length must be at least 21 bytes.");
			Validate.condition(length <= magic.length + 4 + in.available(), true, "Total length indicates trimmed template.");
			vendorId = in.readUnsignedShort();
			subformat = in.readUnsignedShort();
			int certification = in.readUnsignedByte();
			sensorCertified = (certification & 0x80) != 0;
			if ((certification & 0x7f) != 0)
				logger.warn("Ignoring unrecognized sensor compliance bits.");
			sensorId = in.readUnsignedShort();
			int count = in.readUnsignedByte();
			in.skipBytes(1);
			for (int i = 0; i < count; ++i)
				fingerprints.add(new Ansi378v2009Fingerprint(in, lax));
			if (in.available() > 0)
				logger.debug("Ignored extra data at the end of the template.");
			Validate.template(this::validate, lax);
		});
	}
	public byte[] toByteArray() {
		validate();
		DataOutputBuffer out = new DataOutputBuffer();
		out.write(magic);
		out.writeInt(measure());
		out.writeShort(vendorId);
		out.writeShort(subformat);
		out.writeByte(sensorCertified ? 0x80 : 0);
		out.writeShort(sensorId);
		out.writeByte(fingerprints.size());
		out.writeByte(0);
		for (Ansi378v2009Fingerprint fp : fingerprints)
			fp.write(out);
		return out.toByteArray();
	}
	public Ansi378v2009Am1Template upgrade() {
		byte[] serialized = toByteArray();
		serialized[6] = '5';
		return new Ansi378v2009Am1Template(serialized);
	}
	private int measure() {
		return 21 + fingerprints.stream().mapToInt(Ansi378v2009Fingerprint::measure).sum();
	}
	private void validate() {
		Validate.nonzero16(vendorId, "Vendor ID must be a non-zero unsigned 16-bit number.");
		Validate.int16(subformat, "Vendor subformat must be an unsigned 16-bit number.");
		Validate.int16(sensorId, "Sensor ID must be an unsigned 16-bit number.");
		Validate.int8(fingerprints.size(), "There cannot be more than 255 fingerprints.");
		for (Ansi378v2009Fingerprint fp : fingerprints)
			fp.validate();
		if (fingerprints.size() != fingerprints.stream().map(fp -> (fp.position.ordinal() << 16) + fp.view).distinct().count())
			throw new TemplateFormatException("Every fingerprint must have a unique combination of finger position and view offset.");
		fingerprints.stream()
			.collect(groupingBy(fp -> fp.position))
			.values().stream()
			.forEach(l -> {
				for (int i = 0; i < l.size(); ++i) {
					Validate.range(l.get(i).view, 0, l.size() - 1, "Fingerprint view numbers must be assigned contiguously, starting from zero.");
					if (!l.equals(l.stream().sorted(Comparator.comparingInt(fp -> fp.view)).collect(toList())))
						throw new TemplateFormatException("Fingerprints with the same finger position must be sorted by view number.");
					if (fingerprints.indexOf(l.get(0)) + l.size() - 1 != fingerprints.indexOf(l.get(l.size() - 1)))
						throw new TemplateFormatException("Fingerprints with the same finger position must be listed in the template together.");
				}
			});
	}
}
