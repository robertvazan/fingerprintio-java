// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import com.machinezoo.fingerprintio.TemplateFormatException;
import com.machinezoo.fingerprintio.ansi378v2009am1.Ansi378v2009Am1Template;
import com.machinezoo.fingerprintio.common.IbiaOrganizations;
import com.machinezoo.fingerprintio.utils.TemplateUtils;
import com.machinezoo.fingerprintio.utils.TemplateWriter;
import com.machinezoo.fingerprintio.utils.ValidateTemplate;
import com.machinezoo.noexception.ExceptionHandler;
import com.machinezoo.noexception.Exceptions;

/**
 * ANSI INCITS 378-2009 template.
 *
 * @see <a href="https://templates.machinezoo.com/ansi378-2009">ANSI INCITS 378-2009 Summary</a>
 */
public class Ansi378v2009Template {
    private static final byte[] MAGIC = new byte[] { 'F', 'M', 'R', 0, '0', '3', '0', 0 };
    /**
     * Checks whether provided template is an ANSI INCITS 378-2009 template.
     * This method does not do any template validation or conformance checking.
     * It just differentiates ANSI INCITS 378-2009 from other template formats
     * as quickly as possible, mostly by looking at template header.
     * 
     * @param template
     *            serialized template that is to be evaluated
     * @return {@code true} if {@code template} is an ANSI INCITS 378-2009 template, {@code false} otherwise
     */
    public static boolean accepts(byte[] template) {
        if (!Arrays.equals(MAGIC, Arrays.copyOf(template, MAGIC.length)))
            return false;
        /*
         * We differentiate the format from ISO 19794-2:2011 by failing to interpret the data as ISO 19794-2:2011,
         * which is why the code below is copied from Iso19794p2v2001Template with return values inverted.
         */
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(template));
            in.skip(MAGIC.length);
            long total = 0xffff_ffffL & in.readInt();
            int count = in.readUnsignedShort();
            in.skip(1);
            long sum = 0;
            for (int i = 0; i < count; ++i) {
                long length = 0xffff_ffffL & in.readInt();
                if (length < 34)
                    return true;
                sum += length;
                in.skip(length - 4);
            }
            return total != 15 + sum;
        } catch (Throwable ex) {
            return true;
        }
    }
    /**
     * Vendor ID (<a href="https://templates.machinezoo.com/ansi378-2009#vendor">VENDOR</a>).
     * Defaults to {@link IbiaOrganizations#UNKNOWN}.
     */
    public int vendorId = IbiaOrganizations.UNKNOWN;
    /**
     * Vendor-specified subformat (<a href="https://templates.machinezoo.com/ansi378-2009#subformat">SUBFORMAT</a>).
     */
    public int subformat;
    /**
     * Indicates that the fingerprint reader has certificate of compliance with Appendix F of CJIS-RS-0010 V7.
     * This is the top bit of <a href="https://templates.machinezoo.com/ansi378-2009#devstamp">DEVSTAMP</a> field.
     */
    public boolean sensorCertified;
    /**
     * Sensor ID (<a href="https://templates.machinezoo.com/ansi378-2009#devid">DEVID</a>).
     */
    public int sensorId;
    /**
     * List of fingerprints (<a href="https://templates.machinezoo.com/ansi378-2009#fingerprint">FINGERPRINT</a>).
     */
    public List<Ansi378v2009Fingerprint> fingerprints = new ArrayList<>();
    /**
     * Creates new ANSI INCITS 378-2009 template.
     */
    public Ansi378v2009Template() {}
    /**
     * Parses and validates ANSI INCITS 378-2009 template.
     * 
     * @param template
     *            serialized template in ANSI INCITS 378-2009 format
     * @throws TemplateFormatException
     *             if the template cannot be parsed or it fails validation
     */
    public Ansi378v2009Template(byte[] template) { this(template, Exceptions.propagate()); }
    /**
     * Parses and optionally validates ANSI INCITS 378-2009 template.
     * 
     * @param template
     *            serialized template in ANSI INCITS 378-2009 format
     * @param strict
     *            {@code true} to validate the template, {@code false} to tolerate parsing errors as much as possible
     * @throws TemplateFormatException
     *             if the template cannot be parsed or if {@code strict} is {@code true} and the template fails validation
     * @deprecated Use {@link #Ansi378v2009Template(byte[], ExceptionHandler)} instead.
     */
    @Deprecated public Ansi378v2009Template(byte[] template, boolean strict) { this(template, strict ? Exceptions.propagate() : Exceptions.silence()); }
    /**
     * Parses and optionally validates ANSI INCITS 378-2009 template.
     * <p>
     * Recoverable validation exceptions encountered during parsing will be fed to the provided exception handler.
     * Pass in {@link Exceptions#silence()} to ignore all recoverable validation errors
     * or {@link Exceptions#propagate()} to throw exception even for recoverable errors.
     * 
     * @param template
     *            serialized template in ANSI INCITS 378-2009 format
     * @param handler
     *            handler for recoverable validation exceptions
     * @throws TemplateFormatException
     *             if unrecoverable validation error is encountered or the provided exception handler returns {@code false}
     */
    public Ansi378v2009Template(byte[] template, ExceptionHandler handler) {
        if (!accepts(template)) {
            if (Ansi378v2009Am1Template.accepts(template)) {
                ValidateTemplate.fail(handler, "This is ANSI INCITS 378-2009/AM1 template, not ANSI INCITS 378-2009 template.");
                template = Arrays.copyOf(template, template.length);
                template[6] = '0';
            } else
                throw new TemplateFormatException("This is not an ANSI INCITS 378-2009 template.");
        }
        TemplateUtils.decodeTemplate(template, in -> {
            in.skipBytes(MAGIC.length);
            long length = 0xffff_ffffL & in.readInt();
            ValidateTemplate.condition(length >= 21, handler, "Total length must be at least 21 bytes.");
            ValidateTemplate.condition(length <= MAGIC.length + 4 + in.available(), handler, "Total length indicates trimmed template.");
            vendorId = in.readUnsignedShort();
            subformat = in.readUnsignedShort();
            int certification = in.readUnsignedByte();
            sensorCertified = (certification & 0x80) != 0;
            ValidateTemplate.condition((certification & 0x7f) == 0, handler, "Unrecognized sensor compliance bits.");
            sensorId = in.readUnsignedShort();
            int count = in.readUnsignedByte();
            in.skipBytes(1);
            for (int i = 0; i < count; ++i)
                fingerprints.add(new Ansi378v2009Fingerprint(in, handler));
            ValidateTemplate.condition(in.available() == 0, handler, "Extra data at the end of the template.");
            ValidateTemplate.structure(this::validate, handler);
        });
    }
    /**
     * Validates and serializes the template in ANSI INCITS 378-2009 format.
     * 
     * @return serialized template in ANSI INCITS 378-2009 format
     * @throws TemplateFormatException
     *             if the template fails validation
     */
    public byte[] toByteArray() {
        validate();
        TemplateWriter out = new TemplateWriter();
        out.write(MAGIC);
        out.writeInt(measure());
        out.writeShort(vendorId);
        out.writeShort(subformat);
        out.writeByte(sensorCertified ? 0x80 : 0);
        out.writeShort(sensorId);
        out.writeByte(fingerprints.size());
        out.writeByte(0);
        for (Ansi378v2009Fingerprint fp : fingerprints)
            fp.write(out);
        return out.toByteArray();
    }
    private int measure() { return 21 + fingerprints.stream().mapToInt(Ansi378v2009Fingerprint::measure).sum(); }
    private void validate() {
        ValidateTemplate.nonzero16(vendorId, "Vendor ID must be a non-zero unsigned 16-bit number.");
        ValidateTemplate.int16(subformat, "Vendor subformat must be an unsigned 16-bit number.");
        ValidateTemplate.int16(sensorId, "Sensor ID must be an unsigned 16-bit number.");
        ValidateTemplate.int8(fingerprints.size(), "There cannot be more than 255 fingerprints.");
        for (Ansi378v2009Fingerprint fp : fingerprints)
            fp.validate();
        if (fingerprints.size() != fingerprints.stream().map(fp -> (fp.position.ordinal() << 16) + fp.view).distinct().count())
            throw new TemplateFormatException("Every fingerprint must have a unique combination of finger position and view offset.");
        fingerprints.stream()
            .collect(groupingBy(fp -> fp.position))
            .values().stream()
            .forEach(l -> {
                for (int i = 0; i < l.size(); ++i) {
                    ValidateTemplate.range(l.get(i).view, 0, l.size() - 1, "Fingerprint view numbers must be assigned contiguously, starting from zero.");
                    if (!l.equals(l.stream().sorted(Comparator.comparingInt(fp -> fp.view)).collect(toList())))
                        throw new TemplateFormatException("Fingerprints with the same finger position must be sorted by view number.");
                    if (fingerprints.indexOf(l.get(0)) + l.size() - 1 != fingerprints.indexOf(l.get(l.size() - 1)))
                        throw new TemplateFormatException("Fingerprints with the same finger position must be listed in the template together.");
                }
            });
    }
}
