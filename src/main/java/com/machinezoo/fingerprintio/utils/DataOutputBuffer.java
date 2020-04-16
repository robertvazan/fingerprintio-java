// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.utils;

import java.io.*;
import com.machinezoo.noexception.*;

public class DataOutputBuffer implements DataOutput {
	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	private final DataOutputStream stream = new DataOutputStream(buffer);
	@Override public void write(int byteValue) {
		Exceptions.wrap().run(() -> stream.write(byteValue));
	}
	@Override public void write(byte[] bytes) {
		Exceptions.wrap().run(() -> stream.write(bytes));
	}
	@Override public void write(byte[] bytes, int offset, int length) {
		Exceptions.wrap().run(() -> stream.write(bytes, offset, length));
	}
	@Override public void writeBoolean(boolean value) {
		Exceptions.wrap().run(() -> stream.writeBoolean(value));
	}
	@Override public void writeByte(int value) {
		Exceptions.wrap().run(() -> stream.writeByte(value));
	}
	@Override public void writeShort(int value) {
		Exceptions.wrap().run(() -> stream.writeShort(value));
	}
	@Override public void writeChar(int value) {
		Exceptions.wrap().run(() -> stream.writeChar(value));
	}
	@Override public void writeInt(int value) {
		Exceptions.wrap().run(() -> stream.writeInt(value));
	}
	@Override public void writeLong(long value) {
		Exceptions.wrap().run(() -> stream.writeLong(value));
	}
	@Override public void writeFloat(float value) {
		Exceptions.wrap().run(() -> stream.writeFloat(value));
	}
	@Override public void writeDouble(double value) {
		Exceptions.wrap().run(() -> stream.writeDouble(value));
	}
	@Override public void writeBytes(String text) {
		Exceptions.wrap().run(() -> stream.writeBytes(text));
	}
	@Override public void writeChars(String text) {
		Exceptions.wrap().run(() -> stream.writeChars(text));
	}
	@Override public void writeUTF(String text) {
		Exceptions.wrap().run(() -> stream.writeUTF(text));
	}
	public byte[] toByteArray() {
		return buffer.toByteArray();
	}
}
