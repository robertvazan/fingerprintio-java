// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2;

import java.util.*;
import org.slf4j.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

/*
 * Object model of ISO/IEC 19794-2:2005 template.
 */
public class Iso19794p2Template {
	private static final Logger logger = LoggerFactory.getLogger(Iso19794p2Template.class);
	private static final byte[] magic = new byte[] { 'F', 'M', 'R', 0, ' ', '2', '0', 0 };
	public static boolean accepts(byte[] template) {
		if (template.length < magic.length + 4)
			return false;
		if (!Arrays.equals(magic, Arrays.copyOf(template, magic.length)))
			return false;
		DataInputBuffer in = new DataInputBuffer(template);
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
	public int width;
	public int height;
	public int resolutionX;
	public int resolutionY;
	public List<Iso19794p2Fingerprint> fingerprints = new ArrayList<>();
	public Iso19794p2Template() {
	}
	public Iso19794p2Template(byte[] template) {
		this(template, false);
	}
	public Iso19794p2Template(byte[] template, boolean lax) {
		if (!accepts(template))
			throw new TemplateFormatException("This is not an ISO/IEC 19794-2:2005 template.");
		TemplateUtils.decodeTemplate(template, in -> {
			in.skipBytes(magic.length);
			long length = 0xffff_ffffL & in.readInt();
			Validate.condition(length >= 24, "Total length must be at least 24 bytes.");
			Validate.condition(length <= magic.length + 4 + in.available(), true, "Total length indicates trimmed template.");
			in.skipBytes(2);
			width = in.readUnsignedShort();
			height = in.readUnsignedShort();
			resolutionX = in.readUnsignedShort();
			resolutionY = in.readUnsignedShort();
			int count = in.readUnsignedByte();
			in.skipBytes(1);
			for (int i = 0; i < count; ++i)
				fingerprints.add(new Iso19794p2Fingerprint(in, width, height, lax));
			if (in.available() > 0)
				logger.debug("Ignored extra data at the end of the template.");
			Validate.template(this::validate, lax);
		});
	}
	private void validate() {
		Validate.nonzero16(width, "Image width must be a non-zero unsigned 16-bit number.");
		Validate.nonzero16(height, "Image height must be a non-zero unsigned 16-bit number.");
		Validate.nonzero16(resolutionX, "Horizontal pixel density must be a non-zero unsigned 16-bit number.");
		Validate.condition(resolutionX >= 99, "Horizontal pixel density must be at least 99 (DPI 250+).");
		Validate.nonzero16(resolutionY, "Vertical pixel density must be a non-zero unsigned 16-bit number.");
		Validate.condition(resolutionY >= 99, "Vertical pixel density must be at least 99 (DPI 250+).");
		Validate.int8(fingerprints.size(), "There cannot be more than 255 fingerprints.");
		for (Iso19794p2Fingerprint fp : fingerprints)
			fp.validate(width, height);
	}
}
