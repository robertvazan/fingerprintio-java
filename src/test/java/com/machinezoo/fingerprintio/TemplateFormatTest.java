package com.machinezoo.fingerprintio;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.machinezoo.fingerprintio.ansi378v2004.*;
import com.machinezoo.fingerprintio.ansi378v2009.*;
import com.machinezoo.fingerprintio.ansi378v2009am1.*;

public class TemplateFormatTest {
	@Test
	public void identify() {
		assertEquals(TemplateFormat.ANSI_378_2004, TemplateFormat.identify(Ansi378v2004TemplateTest.sampleWithFixedVersion()));
		assertEquals(TemplateFormat.ANSI_378_2009, TemplateFormat.identify(Ansi378v2009TemplateTest.sample()));
		assertEquals(TemplateFormat.ANSI_378_2009_AM1, TemplateFormat.identify(Ansi378v2009Am1TemplateTest.sample()));
	}
}