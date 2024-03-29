// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

import static java.util.stream.Collectors.*;
import java.io.*;
import java.util.*;
import com.machinezoo.fingerprintio.*;
import com.machinezoo.fingerprintio.iso19794p1v2011.*;
import com.machinezoo.fingerprintio.utils.*;
import com.machinezoo.noexception.*;

/**
 * ISO/IEC 19794-2:2011 off-card template.
 * 
 * @see <a href="https://templates.machinezoo.com/iso-19794-2-2011">ISO/IEC 19794-2:2011 Summary</a>
 */
public class Iso19794p2v2011Template {
	private static final Iso19794p1v2011Format FORMAT = new Iso19794p1v2011Format();
	static {
		FORMAT.hasSensorVendor = true;
		FORMAT.hasSensorId = true;
		FORMAT.hasQuality = true;
		FORMAT.hasCertificates = true;
	}
	private static final byte[] MAGIC = new byte[] { 'F', 'M', 'R', 0, '0', '3', '0', 0 };
	/**
	 * Checks whether provided template is an ISO/IEC 19794-2:2011 off-card template.
	 * This method does not do any template validation or conformance checking.
	 * It just differentiates off-card ISO/IEC 19794-2:2011 from other template formats
	 * as quickly as possible, mostly by looking at template header.
	 * 
	 * @param template
	 *            serialized template that is to be evaluated
	 * @return {@code true} if {@code template} is an ISO/IEC 19794-2:2011 off-card template, {@code false} otherwise
	 */
	public static boolean accepts(byte[] template) {
		if (!Arrays.equals(MAGIC, Arrays.copyOf(template, MAGIC.length)))
			return false;
		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(template));
			in.skip(MAGIC.length);
			long total = 0xffff_ffffL & in.readInt();
			int count = in.readUnsignedShort();
			in.skip(1);
			long sum = 0;
			for (int i = 0; i < count; ++i) {
				long length = 0xffff_ffffL & in.readInt();
				/*
				 * Allow zero minutia count when just checking template format.
				 * Otherwise the minimum length would be 39.
				 */
				if (length < 34)
					return false;
				sum += length;
				in.skip(length - 4);
			}
			return total == 15 + sum;
		} catch (Throwable ex) {
			return false;
		}
	}
	/**
	 * List of fingerprints (<a href="https://templates.machinezoo.com/iso-19794-2-2011#fingerprint">FINGERPRINT</a>).
	 */
	public List<Iso19794p2v2011Fingerprint> fingerprints = new ArrayList<>();
	/**
	 * Creates new ISO/IEC 19794-2:2011 off-card template.
	 */
	public Iso19794p2v2011Template() {
	}
	/**
	 * Parses and validates ISO/IEC 19794-2:2011 off-card template.
	 * 
	 * @param template
	 *            serialized template in ISO/IEC 19794-2:2011 off-card format
	 * @throws TemplateFormatException
	 *             if the template cannot be parsed or it fails validation
	 */
	public Iso19794p2v2011Template(byte[] template) {
		this(template, Exceptions.propagate());
	}
	/**
	 * Parses and optionally validates ISO/IEC 19794-2:2011 off-card template.
	 * 
	 * @param template
	 *            serialized template in ISO/IEC 19794-2:2011 off-card format
	 * @param strict
	 *            {@code true} to validate the template, {@code false} to tolerate parsing errors as much as possible
	 * @throws TemplateFormatException
	 *             if the template cannot be parsed or if {@code strict} is {@code true} and the template fails validation
	 * @deprecated Use {@link #Iso19794p2v2011Template(byte[], ExceptionHandler)} instead.
	 */
	@Deprecated
	public Iso19794p2v2011Template(byte[] template, boolean strict) {
		this(template, strict ? Exceptions.propagate() : Exceptions.silence());
	}
	/**
	 * Parses and optionally validates ISO/IEC 19794-2:2011 off-card template.
	 * <p>
	 * Recoverable validation exceptions encountered during parsing will be fed to the provided exception handler.
	 * Pass in {@link Exceptions#silence()} to ignore all recoverable validation errors
	 * or {@link Exceptions#propagate()} to throw exception even for recoverable errors.
	 * 
	 * @param template
	 *            serialized template in ISO/IEC 19794-2:2011 off-card format
	 * @param handler
	 *            handler for recoverable validation exceptions
	 * @throws TemplateFormatException
	 *             if unrecoverable validation error is encountered or the provided exception handler returns {@code false}
	 */
	public Iso19794p2v2011Template(byte[] template, ExceptionHandler handler) {
		if (!accepts(template))
			throw new TemplateFormatException("This is not an ISO/IEC 19794-2:2011 off-card template.");
		try {
			Iso19794p1v2011Template decoded = new Iso19794p1v2011Template(template, handler, FORMAT);
			fingerprints = decoded.samples.stream()
				.map(s -> new Iso19794p2v2011Fingerprint(s, handler))
				.collect(toList());
			ValidateTemplate.structure(this::validate, handler);
		} catch (Throwable ex) {
			throw TemplateUtils.convertException(ex);
		}
	}
	/**
	 * Validates and serializes the template in ISO/IEC 19794-2:2011 off-card format.
	 * 
	 * @return serialized template in ISO/IEC 19794-2:2011 off-card format
	 * @throws TemplateFormatException
	 *             if the template fails validation
	 */
	public byte[] toByteArray() {
		validate();
		Iso19794p1v2011Template container = new Iso19794p1v2011Template();
		container.modality = Iso19794p1v2011Modality.ISO_19794_2;
		container.versionMajor = 3;
		container.versionMinor = 0;
		container.samples = fingerprints.stream()
			.map(fp -> fp.toSample())
			.collect(toList());
		return container.toByteArray(FORMAT);
	}
	private void validate() {
		ValidateTemplate.range(fingerprints.size(), 1, 176, "Fingerprint count must be in range 1 through 176.");
		for (Iso19794p2v2011Fingerprint fp : fingerprints)
			fp.validate();
		if (fingerprints.size() != fingerprints.stream().mapToInt(fp -> (fp.position.ordinal() << 16) + fp.view).distinct().count())
			throw new TemplateFormatException("Every fingerprint must have a unique combination of finger position and view offset.");
		fingerprints.stream()
			.collect(groupingBy(fp -> fp.position))
			.values().stream()
			.forEach(l -> {
				for (int i = 0; i < l.size(); ++i) {
					ValidateTemplate.range(l.get(i).view, 0, l.size() - 1, "Fingerprint view numbers must be assigned contiguously, starting from zero.");
					if (!l.equals(l.stream().sorted(Comparator.comparingInt(fp -> fp.view)).collect(toList())))
						throw new TemplateFormatException("Fingerprints with the same finger position must be sorted by view number.");
				}
			});
	}
}
