// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import com.machinezoo.fingerprintio.utils.*;

public class Ansi378CountEdge {
	public int from;
	public int to;
	public int count;
	public Ansi378CountEdge() {
	}
	Ansi378CountEdge(DataInputBuffer in) {
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
