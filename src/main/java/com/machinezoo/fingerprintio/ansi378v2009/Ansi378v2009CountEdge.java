// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009;

import com.machinezoo.fingerprintio.utils.*;

public class Ansi378v2009CountEdge {
	public int from;
	public int to;
	public int count;
	public Ansi378v2009CountEdge() {
	}
	Ansi378v2009CountEdge(DataInputBuffer in) {
		from = in.readUnsignedByte();
		to = in.readUnsignedByte();
		count = in.readUnsignedByte();
	}
	void write(DataOutputBuffer out) {
		out.writeByte(from);
		out.writeByte(to);
		out.writeByte(count);
	}
	void validate(int minutiaCount) {
		Validate.range(from, 0, minutiaCount - 1, "Start of ridge count edge must be a valid minutia offset.");
		Validate.range(to, 0, minutiaCount - 1, "End of ridge count edge must be a valid minutia offset.");
		Validate.int8(count, "Ridge count must be an unsigned 8-bit number.");
	}
}
