// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio;

import com.machinezoo.fingerprintio.ansi378v2004.*;
import com.machinezoo.fingerprintio.ansi378v2009.*;
import com.machinezoo.fingerprintio.ansi378v2009am1.*;
import com.machinezoo.fingerprintio.iso19794p2v2005.*;
import com.machinezoo.fingerprintio.iso19794p2v2011.*;

/**
 * Fingerprint template format.
 * Only supported template formats are included in this {@code enum}.
 */
public enum TemplateFormat {
	/**
	 * ANSI 378-2004 template.
	 * The template can be parsed by {@link Ansi378v2004Template}.
	 * 
	 * @see <a href="https://templates.machinezoo.com/ansi378-2004">ANSI INCITS 378-2004 Summary</a>
	 */
	ANSI_378_2004,
	/**
	 * ANSI 378-2009 template.
	 * The template can be parsed by {@link Ansi378v2009Template}.
	 * 
	 * @see <a href="https://templates.machinezoo.com/ansi378-2009">ANSI INCITS 378-2009 Summary</a>
	 */
	ANSI_378_2009,
	/**
	 * ANSI 378-2009/AM1 template.
	 * The template can be parsed by {@link Ansi378v2009Am1Template}.
	 * 
	 * @see <a href="https://templates.machinezoo.com/ansi378-2009am1">ANSI INCITS 378-2009/AM 1 Summary</a>
	 */
	ANSI_378_2009_AM1,
	/**
	 * ISO 19794-2:2005 template.
	 * The template can be parsed by {@link Iso19794p2v2005Template}.
	 * 
	 * @see <a href="https://templates.machinezoo.com/iso-19794-2-2005">ISO/IEC 19794-2:2005 Summary</a>
	 */
	ISO_19794_2_2005,
	/**
	 * ISO 19794-2:2011 template.
	 * The template can be parsed by {@link Iso19794p2v2011Template}.
	 * 
	 * @see <a href="https://templates.machinezoo.com/iso-19794-2-2011">ISO/IEC 19794-2:2011 Summary</a>
	 */
	ISO_19794_2_2011;
	/**
	 * Detects template format used to encode provided template.
	 * 
	 * @param template
	 *            serialized template in unknown format
	 * @return format of the template or {@code null} if template's format is not supported
	 */
	public static TemplateFormat identify(byte[] template) {
		if (Ansi378v2004Template.accepts(template))
			return ANSI_378_2004;
		if (Ansi378v2009Template.accepts(template))
			return ANSI_378_2009;
		if (Ansi378v2009Am1Template.accepts(template))
			return ANSI_378_2009_AM1;
		if (Iso19794p2v2005Template.accepts(template))
			return ISO_19794_2_2005;
		if (Iso19794p2v2011Template.accepts(template))
			return ISO_19794_2_2011;
		return null;
	}
}
