// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.machinezoo.fingerprintio.*;
import com.machinezoo.fingerprintio.ansi378v2009am1.*;

public class Ansi378v2009TemplateTest {
	@Test
	public void roundtrip() {
		Ansi378v2009Template t = new Ansi378v2009Template();
		t.sensorCertified = true;
		t.sensorId = 0x6789;
		t.vendorId = 0x5678;
		t.subformat = 0x1234;
		Ansi378v2009Fingerprint f = new Ansi378v2009Fingerprint();
		f.position = Ansi378v2009Position.LEFT_RING;
		f.scanType = Ansi378v2009ScanType.LIVE_NONOPTICAL_CONTACTLESS_ROLLED;
		f.quality = 45;
		f.qualityVendorId = 0xf00f;
		f.qualityAlgorithmId = 0x2002;
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		Ansi378v2009Minutia m = new Ansi378v2009Minutia();
		m.type = Ansi378v2009MinutiaType.ENDING;
		m.positionX = 500;
		m.positionY = 300;
		m.angle = 99;
		m.quality = 77;
		f.minutiae.add(m);
		m = new Ansi378v2009Minutia();
		m.type = Ansi378v2009MinutiaType.BIFURCATION;
		f.minutiae.add(m);
		Ansi378v2009CountExtension cx = f.counts = new Ansi378v2009CountExtension();
		cx.type = Ansi378v2009CountType.OCTANTS;
		Ansi378v2009CountEdge ce = new Ansi378v2009CountEdge();
		ce.from = 0;
		ce.to = 1;
		ce.count = 15;
		cx.edges.add(ce);
		ce = new Ansi378v2009CountEdge();
		ce.from = 1;
		ce.to = 0;
		ce.count = 25;
		cx.edges.add(ce);
		Ansi378v2009CoreDeltaExtension cd = f.coredelta = new Ansi378v2009CoreDeltaExtension();
		Ansi378v2009Core c = new Ansi378v2009Core();
		c.positionX = 123;
		c.positionY = 234;
		c.angle = 55;
		cd.cores.add(c);
		Ansi378v2009Delta d = new Ansi378v2009Delta();
		d.positionX = 111;
		d.positionY = 222;
		d.angles = new int[] { 10, 20, 30 };
		cd.deltas.add(d);
		Ansi378v2009Extension x = new Ansi378v2009Extension();
		x.type = 0x4455;
		x.data = new byte[] { 1, 2, 3 };
		f.extensions.add(x);
		t.fingerprints.add(f);
		f = new Ansi378v2009Fingerprint();
		f.position = Ansi378v2009Position.LEFT_RING;
		f.view = 1;
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		t.fingerprints.add(f);
		f = new Ansi378v2009Fingerprint();
		f.position = Ansi378v2009Position.LEFT_LITTLE;
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		t.fingerprints.add(f);
		t = new Ansi378v2009Template(t.toByteArray());
		assertTrue(t.sensorCertified);
		assertEquals(0x6789, t.sensorId);
		assertEquals(0x5678, t.vendorId);
		assertEquals(0x1234, t.subformat);
		assertEquals(3, t.fingerprints.size());
		Ansi378v2009Fingerprint fa = t.fingerprints.get(0);
		Ansi378v2009Fingerprint fb = t.fingerprints.get(1);
		Ansi378v2009Fingerprint fc = t.fingerprints.get(2);
		assertSame(Ansi378v2009Position.LEFT_RING, fa.position);
		assertSame(Ansi378v2009Position.LEFT_RING, fb.position);
		assertSame(Ansi378v2009Position.LEFT_LITTLE, fc.position);
		assertEquals(0, fa.view);
		assertEquals(1, fb.view);
		assertEquals(0, fc.view);
		assertSame(Ansi378v2009ScanType.LIVE_NONOPTICAL_CONTACTLESS_ROLLED, fa.scanType);
		assertEquals(45, fa.quality);
		assertEquals(0xf00f, fa.qualityVendorId);
		assertEquals(0x2002, fa.qualityAlgorithmId);
		assertEquals(600, fa.width);
		assertEquals(400, fa.height);
		assertEquals(200, fa.resolutionX);
		assertEquals(180, fa.resolutionY);
		assertEquals(2, fa.minutiae.size());
		Ansi378v2009Minutia ma0 = fa.minutiae.get(0);
		assertSame(Ansi378v2009MinutiaType.ENDING, ma0.type);
		assertEquals(500, ma0.positionX);
		assertEquals(300, ma0.positionY);
		assertEquals(99, ma0.angle);
		assertEquals(77, ma0.quality);
		Ansi378v2009Minutia ma1 = fa.minutiae.get(1);
		assertSame(Ansi378v2009MinutiaType.BIFURCATION, ma1.type);
		cx = fa.counts;
		assertSame(Ansi378v2009CountType.OCTANTS, cx.type);
		assertEquals(2, cx.edges.size());
		Ansi378v2009CountEdge ce0 = cx.edges.get(0);
		assertEquals(0, ce0.from);
		assertEquals(1, ce0.to);
		assertEquals(15, ce0.count);
		Ansi378v2009CountEdge ce1 = cx.edges.get(1);
		assertEquals(1, ce1.from);
		assertEquals(0, ce1.to);
		assertEquals(25, ce1.count);
		cd = fa.coredelta;
		assertEquals(1, cd.cores.size());
		assertEquals(1, cd.deltas.size());
		Ansi378v2009Core c0 = cd.cores.get(0);
		assertEquals(123, c0.positionX);
		assertEquals(234, c0.positionY);
		assertEquals(55, (int)c0.angle);
		Ansi378v2009Delta d0 = cd.deltas.get(0);
		assertEquals(111, d0.positionX);
		assertEquals(222, d0.positionY);
		assertArrayEquals(new int[] { 10, 20, 30 }, d0.angles);
		assertEquals(1, fa.extensions.size());
		x = fa.extensions.get(0);
		assertEquals(0x4455, x.type);
		assertArrayEquals(new byte[] { 1, 2, 3 }, x.data);
	}
	@Test
	public void compatibility() {
		byte[] am1 = Ansi378v2009Am1TemplateTest.sample();
		assertTrue(Ansi378v2009Am1Template.accepts(am1));
		assertFalse(Ansi378v2009Template.accepts(am1));
		new Ansi378v2009Template(am1, false);
	}
	@Test
	public void json() {
		TestUtils.compareJson(Ansi378v2009TemplateTest.class, "ansi378v2009-sample.json", decode());
	}
	@Test
	public void spec() {
		Ansi378v2009Template t = decode();
		assertEquals(0x0042, t.vendorId);
		assertEquals(0x0011, t.subformat);
		assertFalse(t.sensorCertified);
		assertEquals(0x0b5, t.sensorId);
		assertEquals(2, t.fingerprints.size());
		Ansi378v2009Fingerprint fa = t.fingerprints.get(0);
		assertSame(Ansi378v2009Position.LEFT_INDEX, fa.position);
		assertSame(Ansi378v2009ScanType.LIVE_PLAIN, fa.scanType);
		assertEquals(90, fa.quality);
		assertEquals(0x0006, fa.qualityVendorId);
		assertEquals(0x0037, fa.qualityAlgorithmId);
		assertEquals(512, fa.width);
		assertEquals(512, fa.height);
		assertEquals(197, fa.resolutionX);
		assertEquals(197, fa.resolutionY);
		assertEquals(27, fa.minutiae.size());
		Ansi378v2009Minutia ma0 = fa.minutiae.get(0);
		assertSame(Ansi378v2009MinutiaType.ENDING, ma0.type);
		assertEquals(100, ma0.positionX);
		assertEquals(14, ma0.positionY);
		assertEquals(112.0, ma0.angle * 2 - 1, 1.0);
		assertEquals(90, ma0.quality);
		Ansi378v2009Minutia ma26 = fa.minutiae.get(26);
		assertSame(Ansi378v2009MinutiaType.BIFURCATION, ma26.type);
		assertEquals(126, ma26.positionX);
		assertEquals(115, ma26.positionY);
		assertEquals(172.0, ma26.angle * 2 - 1, 1.0);
		assertEquals(30, ma26.quality);
		Ansi378v2009Fingerprint fb = t.fingerprints.get(1);
		assertSame(Ansi378v2009Position.RIGHT_INDEX, fb.position);
		assertSame(Ansi378v2009ScanType.LIVE_PLAIN, fb.scanType);
		assertEquals(0x0042, fb.qualityVendorId);
		assertEquals(0x000c, fb.qualityAlgorithmId);
		assertEquals(70, fb.quality);
		assertEquals(22, fb.minutiae.size());
		Ansi378v2009Minutia mb0 = fb.minutiae.get(0);
		assertSame(Ansi378v2009MinutiaType.ENDING, mb0.type);
		assertEquals(40, mb0.positionX);
		assertEquals(93, mb0.positionY);
		assertEquals(0.0, mb0.angle * 2 - 1, 1.0);
		assertEquals(90, mb0.quality);
		Ansi378v2009Minutia mb21 = fb.minutiae.get(21);
		assertSame(Ansi378v2009MinutiaType.BIFURCATION, mb21.type);
		assertEquals(125, mb21.positionX);
		assertEquals(73, mb21.positionY);
		assertEquals(350.0, mb21.angle * 2 - 1, 1.0);
		assertEquals(40, mb21.quality);
		Ansi378v2009CountExtension cx = fb.counts;
		assertSame(Ansi378v2009CountType.QUADRANTS, cx.type);
		assertEquals(2, cx.edges.size());
		Ansi378v2009CountEdge ce0 = cx.edges.get(0);
		assertEquals(18, ce0.from);
		assertEquals(20, ce0.to);
		assertEquals(3, ce0.count);
	}
	@Test
	public void roundtripSpec() {
		byte[] original = sample();
		/*
		 * ANSI 378-2009 spec doesn't get its own sample right.
		 * Length field is off by one, so let's correct it here.
		 */
		original[11] = 108;
		byte[] roundtripped = new Ansi378v2009Template(original).toByteArray();
		assertArrayEquals(original, roundtripped);
	}
	private Ansi378v2009Template decode() {
		byte[] bytes = sample();
		assertTrue(Ansi378v2009Template.accepts(bytes));
		return new Ansi378v2009Template(bytes);
	}
	public static byte[] sample() {
		/*
		 * Sample template from the spec.
		 */
		return TestUtils.resource(Ansi378v2009TemplateTest.class, "ansi378v2009-sample.dat");
	}
}
