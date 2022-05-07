// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;
import com.machinezoo.noexception.*;

/**
 * Core and delta extension (<a href="https://templates.machinezoo.com/iso-19794-2-2011#coredelta">COREDELTA</a>).
 */
public class Iso19794p2v2011CoreDeltaExtension {
	static final int IDENTIFIER = 2;
	/**
	 * List of core points (<a href="https://templates.machinezoo.com/iso-19794-2-2011#coredata">COREDATA</a>).
	 */
	public List<Iso19794p2v2011Core> cores = new ArrayList<>();
	/**
	 * List of delta points (<a href="https://templates.machinezoo.com/iso-19794-2-2011#deltadata">DELTADATA</a>).
	 */
	public List<Iso19794p2v2011Delta> deltas = new ArrayList<>();
	/**
	 * Creates new core and delta extension (<a href="https://templates.machinezoo.com/iso-19794-2-2011#coredelta">COREDELTA</a>).
	 */
	public Iso19794p2v2011CoreDeltaExtension() {
	}
	Iso19794p2v2011CoreDeltaExtension(byte[] extension, ExceptionHandler handler) {
		TemplateUtils.decodeExtension(extension, in -> {
			int coreCount = in.readUnsignedByte();
			for (int i = 0; i < coreCount; ++i)
				cores.add(new Iso19794p2v2011Core(in));
			int deltaCount = in.readUnsignedByte();
			for (int i = 0; i < deltaCount; ++i)
				deltas.add(new Iso19794p2v2011Delta(in));
			ValidateTemplate.condition(in.available() == 0, handler, "Extra data at the end of core/delta extension.");
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
		out.writeByte(cores.size());
		for (Iso19794p2v2011Core core : cores)
			core.write(out);
		out.writeByte(deltas.size());
		for (Iso19794p2v2011Delta delta : deltas)
			delta.write(out);
		return out.toByteArray();
	}
	int measure() {
		int length = 4 + 2;
		length += cores.stream().mapToInt(Iso19794p2v2011Core::measure).sum();
		length += deltas.stream().mapToInt(Iso19794p2v2011Delta::measure).sum();
		return length;
	}
	void validate(int width, int height) {
		for (Iso19794p2v2011Core core : cores)
			core.validate(width, height);
		ValidateTemplate.int4(cores.size(), "There can be no more than 15 cores.");
		for (Iso19794p2v2011Delta delta : deltas)
			delta.validate(width, height);
		ValidateTemplate.int4(deltas.size(), "There can be no more than 15 deltas.");
	}
}
