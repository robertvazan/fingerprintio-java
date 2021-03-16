// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

import java.nio.charset.*;

public enum Iso19794p1v2011Modality {
	ISO_19794_2("FMR"),
	ISO_19794_3("FSP"),
	ISO_19794_4("FIR"),
	ISO_19794_5("FAC"),
	ISO_19794_6("IIR"),
	ISO_19794_7("SDI"),
	ISO_19794_8("FSK"),
	ISO_19794_9("VIR"),
	ISO_19794_10("HND"),
	ISO_19794_11("SPD"),
	ISO_19794_13("VDI"),
	ISO_19794_14("DNA");
	final byte[] magic;
	Iso19794p1v2011Modality(String code) {
		magic = (code + "\0").getBytes(StandardCharsets.US_ASCII);
	}
}
