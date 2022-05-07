// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;
import com.machinezoo.noexception.*;

/**
 * Ridge count extension (<a href="https://templates.machinezoo.com/iso-19794-2-2005#rcountext">RCOUNTEXT</a>).
 */
public class Iso19794p2v2005CountExtension {
	static final int IDENTIFIER = 1;
	/**
	 * Edge picking method (<a href="https://templates.machinezoo.com/iso-19794-2-2005#startype">STARTYPE</a>).
	 * Defaults to {@link Iso19794p2v2005CountType#CUSTOM}.
	 */
	public Iso19794p2v2005CountType type = Iso19794p2v2005CountType.CUSTOM;
	/**
	 * List of edges (<a href="https://templates.machinezoo.com/iso-19794-2-2005#edgedef">EDGEDEF</a>).
	 */
	public List<Iso19794p2v2005CountEdge> edges = new ArrayList<>();
	/**
	 * Creates new ridge count extension (<a href="https://templates.machinezoo.com/iso-19794-2-2005#rcountext">RCOUNTEXT</a>).
	 */
	public Iso19794p2v2005CountExtension() {
	}
	Iso19794p2v2005CountExtension(byte[] extension, ExceptionHandler handler) {
		TemplateUtils.decodeExtension(extension, in -> {
			type = TemplateUtils.decodeType(in.readUnsignedByte(), Iso19794p2v2005CountType.class, handler, "Unrecognized edge picking method.");
			int count = (extension.length - 1) / 3;
			ValidateTemplate.condition(3 * count == extension.length - 1, handler, "Extra misaligned data at the end of ridge count extension.");
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
