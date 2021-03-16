// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2v2005CountEdge {
	public int from;
	public int to;
	public int count;
	public Iso19794p2v2005CountEdge() {
	}
	Iso19794p2v2005CountEdge(TemplateReader in) {
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
		ValidateTemplate.range(from, 0, minutiaCount - 1, "Start of ridge count edge must be a valid minutia offset.");
		ValidateTemplate.range(to, 0, minutiaCount - 1, "End of ridge count edge must be a valid minutia offset.");
		ValidateTemplate.int8(count, "Ridge count must be an unsigned 8-bit number.");
	}
}
