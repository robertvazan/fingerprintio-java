// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

/**
 * Quality record (<a href="https://templates.machinezoo.com/iso-19794-1-2011#qrecord">QRECORD</a>).
 */
public class Iso19794p1v2011Quality {
	/**
	 * Sample quality (<a href="https://templates.machinezoo.com/iso-19794-1-2011#quality">QUALITY</a>).
	 * Defaults to 255 (failure to measure quality).
	 */
	public int quality = 255;
	/**
	 * Quality algorithm vendor (<a href="https://templates.machinezoo.com/iso-19794-1-2011#qvendor">QVENDOR</a>).
	 * Defaults to {@link IbiaOrganizations#UNKNOWN}.
	 */
	public int vendor = IbiaOrganizations.UNKNOWN;
	/**
	 * Quality algorithm (<a href="https://templates.machinezoo.com/iso-19794-1-2011#qalgo">QALGO</a>).
	 */
	public int algorithm;
	/**
	 * Create new quality record (<a href="https://templates.machinezoo.com/iso-19794-1-2011#qrecord">QRECORD</a>).
	 */
	public Iso19794p1v2011Quality() {
	}
	Iso19794p1v2011Quality(TemplateReader in) {
		quality = in.readUnsignedByte();
		vendor = in.readUnsignedShort();
		algorithm = in.readUnsignedShort();
	}
	void write(TemplateWriter out) {
		out.writeByte(quality);
		out.writeShort(vendor);
		out.writeShort(algorithm);
	}
	void validate() {
		ValidateTemplate.condition(quality >= 0 && quality <= 100 || quality == 255, "Sample quality must be in range 0 through 100 or special value 255.");
		ValidateTemplate.int16(vendor, "Quality algorithm vendor must be a 16-bit unsigned number.");
		ValidateTemplate.int16(algorithm, "Quality algorithm must be a 16-bit unsigned number.");
	}
}
