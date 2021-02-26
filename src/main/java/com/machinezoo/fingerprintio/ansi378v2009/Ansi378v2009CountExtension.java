// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378v2009CountExtension {
	static final int IDENTIFIER = 1;
	public Ansi378v2009CountType type = Ansi378v2009CountType.CUSTOM;
	public List<Ansi378v2009CountEdge> edges = new ArrayList<>();
	public Ansi378v2009CountExtension() {
	}
	Ansi378v2009CountExtension(byte[] extension, boolean lax) {
		TemplateUtils.decodeExtension(extension, in -> {
			type = TemplateUtils.decodeType(in.readUnsignedByte(), Ansi378v2009CountType.class, lax, "Unrecognized edge picking method.");
			int count = (extension.length - 1) / 3;
			ValidateTemplate.condition(3 * count == extension.length - 1, lax, "Extra misaligned data at the end of ridge count extension.");
			for (int i = 0; i < count; ++i)
				edges.add(new Ansi378v2009CountEdge(in));
		});
	}
	Ansi378v2009Extension extension() {
		Ansi378v2009Extension extension = new Ansi378v2009Extension();
		extension.type = IDENTIFIER;
		extension.data = toByteArray();
		return extension;
	}
	byte[] toByteArray() {
		TemplateWriter out = new TemplateWriter();
		out.writeByte(type.ordinal());
		for (Ansi378v2009CountEdge edge : edges)
			edge.write(out);
		return out.toByteArray();
	}
	int measure() {
		return 4 + 1 + 3 * edges.size();
	}
	void validate(int minutiaCount) {
		Objects.requireNonNull(type, "Edge picking method must be non-null.");
		for (Ansi378v2009CountEdge edge : edges)
			edge.validate(minutiaCount);
		ValidateTemplate.int16(measure(), "Ridge count extension size must be an unsigned 16-bit number.");
	}
}
