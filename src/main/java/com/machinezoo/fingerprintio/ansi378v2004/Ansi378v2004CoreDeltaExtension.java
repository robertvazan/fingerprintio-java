// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

import java.util.*;
import com.machinezoo.fingerprintio.common.*;
import com.machinezoo.fingerprintio.utils.*;

public class Ansi378v2004CoreDeltaExtension {
	static final int IDENTIFIER = 2;
	public List<Ansi378v2004Core> cores = new ArrayList<>();
	public List<Ansi378v2004Delta> deltas = new ArrayList<>();
	public Ansi378v2004CoreDeltaExtension() {
	}
	Ansi378v2004CoreDeltaExtension(byte[] extension, boolean lax) {
		TemplateUtils.decodeExtension(extension, in -> {
			int coreInfo = in.readUnsignedByte();
			for (int i = 0; i < (coreInfo & 0xf); ++i)
				cores.add(new Ansi378v2004Core(in, (coreInfo & 0x40) != 0));
			int deltaInfo = in.readUnsignedByte();
			for (int i = 0; i < (deltaInfo & 0xf); ++i)
				deltas.add(new Ansi378v2004Delta(in, (deltaInfo & 0x40) != 0));
			Validate.condition(in.available() == 0, lax, "Extra data at the end of core/delta extension.");
		});
	}
	Ansi378v2004Extension extension() {
		Ansi378v2004Extension extension = new Ansi378v2004Extension();
		extension.type = IDENTIFIER;
		extension.data = toByteArray();
		return extension;
	}
	byte[] toByteArray() {
		DataOutputBuffer out = new DataOutputBuffer();
		out.writeByte((hasCoreAngles() ? 0x40 : 0) | cores.size());
		for (Ansi378v2004Core core : cores)
			core.write(out);
		out.writeByte((hasDeltaAngles() ? 0x40 : 0) | deltas.size());
		for (Ansi378v2004Delta delta : deltas)
			delta.write(out);
		return out.toByteArray();
	}
	int measure() {
		int length = 4 + 2;
		length += 4 * cores.size();
		if (hasCoreAngles())
			length += cores.size();
		length += 4 * deltas.size();
		if (hasDeltaAngles())
			length += 3 * deltas.size();
		return length;
	}
	void validate(int width, int height) {
		for (Ansi378v2004Core core : cores)
			core.validate(width, height);
		if (hasCoreAngles() && cores.stream().anyMatch(c -> c.angle == null))
			throw new TemplateFormatException("Inconsistent core angle information. Either all cores should have angle or none should.");
		Validate.int4(cores.size(), "There can be no more than 15 cores.");
		for (Ansi378v2004Delta delta : deltas)
			delta.validate(width, height);
		if (hasDeltaAngles() && deltas.stream().anyMatch(d -> d.angles == null))
			throw new TemplateFormatException("Inconsistent delta angle information. Either all deltas should have angles or none should.");
		Validate.int4(deltas.size(), "There can be no more than 15 deltas.");
	}
	private boolean hasCoreAngles() {
		for (Ansi378v2004Core core : cores)
			if (core.angle == null)
				return false;
		return true;
	}
	private boolean hasDeltaAngles() {
		for (Ansi378v2004Delta delta : deltas)
			if (delta.angles == null)
				return false;
		return true;
	}
}
