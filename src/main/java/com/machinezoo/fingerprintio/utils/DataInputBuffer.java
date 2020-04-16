// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.utils;

import java.io.*;
import com.machinezoo.noexception.*;

public class DataInputBuffer implements DataInput {
	private final DataInputStream stream;
	public DataInputBuffer(byte[] buffer) {
		stream = new DataInputStream(new ByteArrayInputStream(buffer));
	}
	private static final CheckedExceptionHandler handler = new CheckedExceptionHandler() {
		@Override public RuntimeException handle(Exception exception) {
			if (exception instanceof EOFException)
				return new BufferEofException();
			/*
			 * We should never get here, because byte array reads cannot fail.
			 */
			return new WrappedException(exception);
		}
	};
	public int available() {
		return handler.getAsInt(() -> stream.available());
	}
	@Override public void readFully(byte[] bytes) {
		handler.run(() -> stream.readFully(bytes));
	}
	@Override public void readFully(byte[] bytes, int offset, int length) {
		handler.run(() -> stream.readFully(bytes, offset, length));
	}
	@Override public int skipBytes(int number) {
		return handler.getAsInt(() -> stream.skipBytes(number));
	}
	@Override public boolean readBoolean() {
		return handler.getAsBoolean(() -> stream.readBoolean());
	}
	@Override public byte readByte() {
		return (byte)handler.getAsInt(() -> stream.readByte());
	}
	@Override public int readUnsignedByte() {
		return handler.getAsInt(() -> stream.readUnsignedByte());
	}
	@Override public short readShort() {
		return (short)handler.getAsInt(() -> stream.readShort());
	}
	@Override public int readUnsignedShort() {
		return handler.getAsInt(() -> stream.readUnsignedShort());
	}
	@Override public char readChar() {
		return (char)handler.getAsInt(() -> stream.readChar());
	}
	@Override public int readInt() {
		return handler.getAsInt(() -> stream.readInt());
	}
	@Override public long readLong() {
		return handler.getAsLong(() -> stream.readLong());
	}
	@Override public float readFloat() {
		/*
		 * Avoid conversion float->double->float. We are not sure it won't change the float.
		 */
		return handler.get(() -> stream.readFloat());
	}
	@Override public double readDouble() {
		return handler.getAsDouble(() -> stream.readDouble());
	}
	@SuppressWarnings("deprecation") @Override public String readLine() {
		return handler.get(() -> stream.readLine());
	}
	@Override public String readUTF() {
		return handler.get(() -> stream.readUTF());
	}
}
