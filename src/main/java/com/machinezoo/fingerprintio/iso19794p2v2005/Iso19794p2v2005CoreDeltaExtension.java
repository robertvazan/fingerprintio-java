// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;

public class Iso19794p2v2005CoreDeltaExtension {
	static final int IDENTIFIER = 2;
	public List<Iso19794p2v2005Core> cores = new ArrayList<>();
	public List<Iso19794p2v2005Delta> deltas = new ArrayList<>();
	public Iso19794p2v2005CoreDeltaExtension() {
	}
	Iso19794p2v2005CoreDeltaExtension(byte[] extension, boolean strict) {
		TemplateUtils.decodeExtension(extension, in -> {
			int coreCount = in.readUnsignedByte();
			for (int i = 0; i < coreCount; ++i)
				cores.add(new Iso19794p2v2005Core(in));
			int deltaCount = in.readUnsignedByte();
			for (int i = 0; i < deltaCount; ++i)
				deltas.add(new Iso19794p2v2005Delta(in));
			ValidateTemplate.condition(in.available() == 0, strict, "Extra data at the end of core/delta extension.");
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
		out.writeByte(cores.size());
		for (Iso19794p2v2005Core core : cores)
			core.write(out);
		out.writeByte(deltas.size());
		for (Iso19794p2v2005Delta delta : deltas)
			delta.write(out);
		return out.toByteArray();
	}
	int measure() {
		int length = 4 + 2;
		length += cores.stream().mapToInt(Iso19794p2v2005Core::measure).sum();
		length += deltas.stream().mapToInt(Iso19794p2v2005Delta::measure).sum();
		return length;
	}
	void validate(int width, int height) {
		for (Iso19794p2v2005Core core : cores)
			core.validate(width, height);
		ValidateTemplate.int4(cores.size(), "There can be no more than 15 cores.");
		for (Iso19794p2v2005Delta delta : deltas)
			delta.validate(width, height);
		ValidateTemplate.int4(deltas.size(), "There can be no more than 15 deltas.");
	}
}
