// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio;

import com.machinezoo.fingerprintio.ansi378v2004.*;
import com.machinezoo.fingerprintio.ansi378v2009.*;
import com.machinezoo.fingerprintio.ansi378v2009am1.*;
import com.machinezoo.fingerprintio.iso19794p2v2005.*;

public enum TemplateFormat {
	ANSI_378_2004,
	ANSI_378_2009,
	ANSI_378_2009_AM1,
	ISO_19794_2_2005;
	public static TemplateFormat detect(byte[] template) {
		if (Ansi378v2004Template.accepts(template))
			return ANSI_378_2004;
		if (Ansi378v2009Template.accepts(template))
			return ANSI_378_2009;
		if (Ansi378v2009Am1Template.accepts(template))
			return ANSI_378_2009_AM1;
		if (Iso19794p2v2005Template.accepts(template))
			return ISO_19794_2_2005;
		return null;
	}
}
