// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import java.util.*;
import com.machinezoo.fingerprintio.utils.*;
import com.machinezoo.noexception.*;

/**
 * Zonal quality extension (<a href="https://templates.machinezoo.com/iso-19794-2-2005#zonalext">ZONALEXT</a>).
 */
public class Iso19794p2v2005ZonalExtension {
	static final int IDENTIFIER = 3;
	/**
	 * Zone width (<a href="https://templates.machinezoo.com/iso-19794-2-2005#zonewidth">ZONEWIDTH</a>).
	 */
	public int zoneWidth;
	/**
	 * Zone height (<a href="https://templates.machinezoo.com/iso-19794-2-2005#zoneheight">ZONEHEIGHT</a>).
	 */
	public int zoneHeight;
	/**
	 * Bits per zone (<a href="https://templates.machinezoo.com/iso-19794-2-2005#zonebits">ZONEBITS</a>).
	 * Defaults to 8.
	 */
	public int bits = 8;
	/**
	 * Zonal quality data (<a href="https://templates.machinezoo.com/iso-19794-2-2005#zonalquality">ZONALQUALITY</a>).
	 * Every array item represents quality of one zone.
	 */
	public int[] quality;
	/**
	 * Creates new zonal quality extension (<a href="https://templates.machinezoo.com/iso-19794-2-2005#zonalext">ZONALEXT</a>).
	 */
	public Iso19794p2v2005ZonalExtension() {
	}
	Iso19794p2v2005ZonalExtension(byte[] extension, int imageWidth, int imageHeight, ExceptionHandler handler) {
		TemplateUtils.decodeExtension(extension, in -> {
			zoneWidth = in.readUnsignedByte();
			zoneHeight = in.readUnsignedByte();
			bits = in.readUnsignedByte();
			int width = (imageWidth + zoneWidth - 1) / zoneWidth;
			int height = (imageHeight + zoneHeight - 1) / zoneHeight;
			int size = width * height;
			quality = new int[size];
			int buffered = 0;
			int buffer = 0;
			for (int i = 0; i < size; ++i) {
				while (buffered < bits) {
					buffer = (buffer << 8) | in.readUnsignedByte();
					buffered += 8;
				}
				quality[i] = buffer >> (buffered - bits);
				buffered -= bits;
				buffer &= (1 << buffered) - 1;
			}
			ValidateTemplate.condition(in.available() == 0, handler, "Extra data at the end of zonal quality extension.");
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
		out.writeByte(zoneWidth);
		out.writeByte(zoneHeight);
		out.writeByte(bits);
		int length = 0;
		int packed = 0;
		for (int i = 0; i < quality.length; ++i) {
			packed = (packed << bits) | quality[i];
			length += bits;
			if (length >= 8) {
				out.writeByte(packed >> (length - 8));
				length -= 8;
				packed &= (1 << length) - 1;
			}
		}
		if (length > 0)
			out.writeByte(packed << (8 - length));
		return out.toByteArray();
	}
	int measure() {
		return 4 + 3 + (quality.length * bits + 7) / 8;
	}
	void validate(int imageWidth, int imageHeight) {
		ValidateTemplate.nonzero8(zoneWidth, "Zonal quality cell width must be a non-zero unsigned 8-bit number.");
		ValidateTemplate.nonzero8(zoneHeight, "Zonal quality cell height must be a non-zero unsigned 8-bit number.");
		ValidateTemplate.range(bits, 1, 8, "Number of bits per zone in zonal quality extension must be in range 1 through 8.");
		int width = (imageWidth + zoneWidth - 1) / zoneWidth;
		int height = (imageHeight + zoneHeight - 1) / zoneHeight;
		Objects.requireNonNull(quality, "Zonal quality array must be non-null.");
		ValidateTemplate.condition(quality.length == width * height, "Zonal quality array size must be derived from image and zone size.");
		for (int i = 0; i < quality.length; ++i)
			ValidateTemplate.range(quality[i], 0, (1 << bits) - 1, "Zonal quality values must be unsigned numbers that fit within specified bit depth.");
		ValidateTemplate.int16(measure(), "Zonal quality extension size must be an unsigned 16-bit number.");
	}
}
