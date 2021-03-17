// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

/**
 * Information about optional fields in ISO/IEC 19794-1:2011 template.
 * This information comes from individual ISO 19794-X specifications.
 * Every ISO 19794-X spec defines its own template format that specializes ISO 19794-1.
 */
public class Iso19794p1v2011Format {
	/**
	 * Length of format-specific template header fields.
	 */
	public int extraHeaderLength;
	/**
	 * Format includes sensor vendor ID (<a href="https://templates.machinezoo.com/iso-19794-1-2011#devvendor">DEVVENDOR</a>) field.
	 */
	public boolean hasSensorVendor;
	/**
	 * Format includes sensor ID (<a href="https://templates.machinezoo.com/iso-19794-1-2011#devid">DEVID</a>) field.
	 */
	public boolean hasSensorId;
	/**
	 * Format includes quality record (<a href="https://templates.machinezoo.com/iso-19794-1-2011#qrecord">QRECORD</a>) fields.
	 */
	public boolean hasQuality;
	/**
	 * Format includes certification record (<a href="https://templates.machinezoo.com/iso-19794-1-2011#certificate">CERTIFICATE</a>) fields.
	 */
	public boolean hasCertificates;
}
