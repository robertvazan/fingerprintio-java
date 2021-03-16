// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2v2005CountExtension {
	static final int IDENTIFIER = 1;
	public Iso19794p2v2005CountType type = Iso19794p2v2005CountType.CUSTOM;
	public List<Iso19794p2v2005CountEdge> edges = new ArrayList<>();
	public Iso19794p2v2005CountExtension() {
	}
	Iso19794p2v2005CountExtension(byte[] extension, boolean strict) {
		TemplateUtils.decodeExtension(extension, in -> {
			type = TemplateUtils.decodeType(in.readUnsignedByte(), Iso19794p2v2005CountType.class, strict, "Unrecognized edge picking method.");
			int count = (extension.length - 1) / 3;
			ValidateTemplate.condition(3 * count == extension.length - 1, strict, "Extra misaligned data at the end of ridge count extension.");
			for (int i = 0; i < count; ++i)
				edges.add(new Iso19794p2v2005CountEdge(in));
		});
	}
	Iso19794p2v2005Extension extension() {
		Iso19794p2v2005Extension extension = new Iso19794p2v2005Extension();
		extension.type = IDENTIFIER;
		extension.data = toByteArray();
		return extension;
	}
	byte[] toByteArray() {
		TemplateWriter out = new TemplateWriter();
		out.writeByte(type.ordinal());
		for (Iso19794p2v2005CountEdge edge : edges)
			edge.write(out);
		return out.toByteArray();
	}
	int measure() {
		return 4 + 1 + 3 * edges.size();
	}
	void validate(int minutiaCount) {
		Objects.requireNonNull(type, "Edge picking method must be non-null.");
		for (Iso19794p2v2005CountEdge edge : edges)
			edge.validate(minutiaCount);
		ValidateTemplate.int16(measure(), "Ridge count extension size must be an unsigned 16-bit number.");
	}
}
