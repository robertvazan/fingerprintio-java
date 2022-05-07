// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;
import com.machinezoo.noexception.*;

/**
 * Ridge count extension (<a href="https://templates.machinezoo.com/iso-19794-2-2011#rcountext">RCOUNTEXT</a>).
 */
public class Iso19794p2v2011CountExtension {
	static final int IDENTIFIER = 1;
	/**
	 * Edge picking method (<a href="https://templates.machinezoo.com/iso-19794-2-2011#startype">STARTYPE</a>).
	 * Defaults to {@link Iso19794p2v2011CountType#CUSTOM}.
	 */
	public Iso19794p2v2011CountType type = Iso19794p2v2011CountType.CUSTOM;
	/**
	 * List of edges (<a href="https://templates.machinezoo.com/iso-19794-2-2011#edgedef">EDGEDEF</a>).
	 */
	public List<Iso19794p2v2011CountEdge> edges = new ArrayList<>();
	/**
	 * Creates new ridge count extension (<a href="https://templates.machinezoo.com/iso-19794-2-2011#rcountext">RCOUNTEXT</a>).
	 */
	public Iso19794p2v2011CountExtension() {
	}
	Iso19794p2v2011CountExtension(byte[] extension, ExceptionHandler handler) {
		TemplateUtils.decodeExtension(extension, in -> {
			type = TemplateUtils.decodeType(in.readUnsignedByte(), Iso19794p2v2011CountType.class, handler, "Unrecognized edge picking method.");
			int count = (extension.length - 1) / 3;
			ValidateTemplate.condition(3 * count == extension.length - 1, handler, "Extra misaligned data at the end of ridge count extension.");
			for (int i = 0; i < count; ++i)
				edges.add(new Iso19794p2v2011CountEdge(in));
		});
	}
	Iso19794p2v2011Extension extension() {
		Iso19794p2v2011Extension extension = new Iso19794p2v2011Extension();
		extension.type = IDENTIFIER;
		extension.data = toByteArray();
		return extension;
	}
	byte[] toByteArray() {
		TemplateWriter out = new TemplateWriter();
		out.writeByte(type.ordinal());
		for (Iso19794p2v2011CountEdge edge : edges)
			edge.write(out);
		return out.toByteArray();
	}
	int measure() {
		return 4 + 1 + 3 * edges.size();
	}
	void validate(int minutiaCount) {
		Objects.requireNonNull(type, "Edge picking method must be non-null.");
		for (Iso19794p2v2011CountEdge edge : edges)
			edge.validate(minutiaCount);
		ValidateTemplate.int16(measure(), "Ridge count extension size must be an unsigned 16-bit number.");
	}
}
