// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

import com.machinezoo.fingerprintio.utils.*;

/**
 * Edge between minutiae (<a href="https://templates.machinezoo.com/iso-19794-2-2011#edgedef">EDGEDEF</a>).
 */
public class Iso19794p2v2011CountEdge {
	/**
	 * Starting minutia of the edge (<a href="https://templates.machinezoo.com/iso-19794-2-2011#edgefrom">EDGEFROM</a>).
	 */
	public int from;
	/**
	 * Ending minutia of the edge (<a href="https://templates.machinezoo.com/iso-19794-2-2011#edgeto">EDGETO</a>).
	 */
	public int to;
	/**
	 * Number of ridges the edge crosses (<a href="https://templates.machinezoo.com/iso-19794-2-2011#ridgecount">RIDGECOUNT</a>).
	 */
	public int count;
	/**
	 * Creates new edge between minutiae (<a href="https://templates.machinezoo.com/iso-19794-2-2011#edgedef">EDGEDEF</a>).
	 */
	public Iso19794p2v2011CountEdge() {
	}
	Iso19794p2v2011CountEdge(TemplateReader in) {
		from = in.readUnsignedByte();
		to = in.readUnsignedByte();
		count = in.readUnsignedByte();
	}
	void write(TemplateWriter out) {
		out.writeByte(from);
		out.writeByte(to);
		out.writeByte(count);
	}
	void validate(int minutiaCount) {
		ValidateTemplate.nonzero8(count, "Ridge count must be a non-zero unsigned 8-bit number.");
		ValidateTemplate.range(from, 0, minutiaCount - 1, "Start of ridge count edge must be a valid minutia offset.");
		if (count == 0xff)
			ValidateTemplate.condition(to == 0xff, "If ridge count is 0xff (placeholder edge), end minutia field must be also 0xff.");
		else {
			ValidateTemplate.condition(to != 0xff, "If ridge count is less than 0xff (not a placeholder edge), end minutia field must be also less than 0xff.");
			ValidateTemplate.range(to, 0, minutiaCount - 1, "End of ridge count edge must be a valid minutia offset.");
		}
	}
}
