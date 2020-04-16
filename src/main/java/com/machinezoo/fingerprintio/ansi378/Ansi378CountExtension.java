// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378CountExtension {
	static final int IDENTIFIER = 1;
	public Ansi378CountType type = Ansi378CountType.CUSTOM;
	public List<Ansi378CountEdge> edges = new ArrayList<>();
	public Ansi378CountExtension() {
	}
	Ansi378CountExtension(byte[] extension, boolean lax) {
		TemplateUtils.decodeExtension(extension, in -> {
			type = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378CountType.class, lax, "Unrecognized edge picking method.");
			int count = (extension.length - 1) / 3;
			Validate.condition(3 * count == extension.length - 1, lax, "Extra misaligned data at the end of ridge count extension.");
			for (int i = 0; i < count; ++i)
				edges.add(new Ansi378CountEdge(in));
		});
	}
	Ansi378Extension extension() {
		Ansi378Extension extension = new Ansi378Extension();
		extension.type = IDENTIFIER;
		extension.data = toByteArray();
		return extension;
	}
	byte[] toByteArray() {
		DataOutputBuffer out = new DataOutputBuffer();
		out.writeByte(type.ordinal());
		for (Ansi378CountEdge edge : edges)
			edge.write(out);
		return out.toByteArray();
	}
	int measure() {
		return 4 + 1 + 3 * edges.size();
	}
	void validate(int minutiaCount) {
		Objects.requireNonNull(type, "Edge picking method must be non-null.");
		for (Ansi378CountEdge edge : edges)
			edge.validate(minutiaCount);
		Validate.int16(measure(), "Ridge count extension size must be an unsigned 16-bit number.");
	}
}
