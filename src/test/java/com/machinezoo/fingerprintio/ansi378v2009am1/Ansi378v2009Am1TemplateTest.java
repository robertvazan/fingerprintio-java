// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2009am1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.machinezoo.fingerprintio.*;
import com.machinezoo.fingerprintio.ansi378v2009.*;

public class Ansi378v2009Am1TemplateTest {
	@Test
	public void roundtrip() {
		Ansi378v2009Am1Template t = new Ansi378v2009Am1Template();
		t.sensorCertified = true;
		t.sensorId = 0x6789;
		t.vendorId = 0x5678;
		t.subformat = 0x1234;
		Ansi378v2009Am1Fingerprint f = new Ansi378v2009Am1Fingerprint();
		f.position = Ansi378v2009Am1Position.LEFT_RING;
		f.scanType = Ansi378v2009Am1ScanType.LIVE_NONOPTICAL_CONTACTLESS_ROLLED;
		f.quality = 45;
		f.qualityVendorId = 0xf00f;
		f.qualityAlgorithmId = 0x2002;
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		Ansi378v2009Am1Minutia m = new Ansi378v2009Am1Minutia();
		m.type = Ansi378v2009Am1MinutiaType.ENDING;
		m.positionX = 500;
		m.positionY = 300;
		m.angle = 99;
		m.quality = 77;
		f.minutiae.add(m);
		m = new Ansi378v2009Am1Minutia();
		m.type = Ansi378v2009Am1MinutiaType.BIFURCATION;
		f.minutiae.add(m);
		Ansi378v2009Am1CountExtension cx = f.counts = new Ansi378v2009Am1CountExtension();
		cx.type = Ansi378v2009Am1CountType.OCTANTS;
		Ansi378v2009Am1CountEdge e = new Ansi378v2009Am1CountEdge();
		e.from = 0;
		e.to = 1;
		e.count = 15;
		cx.edges.add(e);
		e = new Ansi378v2009Am1CountEdge();
		e.from = 1;
		e.to = 0;
		e.count = 25;
		cx.edges.add(e);
		Ansi378v2009Am1CoreDeltaExtension cd = f.coredelta = new Ansi378v2009Am1CoreDeltaExtension();
		Ansi378v2009Am1Core c = new Ansi378v2009Am1Core();
		c.positionX = 123;
		c.positionY = 234;
		c.angle = 55;
		cd.cores.add(c);
		Ansi378v2009Am1Delta d = new Ansi378v2009Am1Delta();
		d.positionX = 111;
		d.positionY = 222;
		d.angles = new int[] { 10, 20, 30 };
		cd.deltas.add(d);
		Ansi378v2009Am1Extension x = new Ansi378v2009Am1Extension();
		x.type = 0x4455;
		x.data = new byte[] { 1, 2, 3 };
		f.extensions.add(x);
		t.fingerprints.add(f);
		f = new Ansi378v2009Am1Fingerprint();
		f.position = Ansi378v2009Am1Position.LEFT_RING;
		f.view = 1;
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		t.fingerprints.add(f);
		f = new Ansi378v2009Am1Fingerprint();
		f.position = Ansi378v2009Am1Position.BOTH_THUMBS;
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		t.fingerprints.add(f);
		t = new Ansi378v2009Am1Template(t.toByteArray());
		assertTrue(t.sensorCertified);
		assertEquals(0x6789, t.sensorId);
		assertEquals(0x5678, t.vendorId);
		assertEquals(0x1234, t.subformat);
		assertEquals(3, t.fingerprints.size());
		Ansi378v2009Am1Fingerprint fa = t.fingerprints.get(0);
		Ansi378v2009Am1Fingerprint fb = t.fingerprints.get(1);
		Ansi378v2009Am1Fingerprint fc = t.fingerprints.get(2);
		assertSame(Ansi378v2009Am1Position.LEFT_RING, fa.position);
		assertSame(Ansi378v2009Am1Position.LEFT_RING, fb.position);
		assertSame(Ansi378v2009Am1Position.BOTH_THUMBS, fc.position);
		assertEquals(0, fa.view);
		assertEquals(1, fb.view);
		assertEquals(0, fc.view);
		assertSame(Ansi378v2009Am1ScanType.LIVE_NONOPTICAL_CONTACTLESS_ROLLED, fa.scanType);
		assertEquals(45, fa.quality);
		assertEquals(0xf00f, fa.qualityVendorId);
		assertEquals(0x2002, fa.qualityAlgorithmId);
		assertEquals(600, fa.width);
		assertEquals(400, fa.height);
		assertEquals(200, fa.resolutionX);
		assertEquals(180, fa.resolutionY);
		assertEquals(2, fa.minutiae.size());
		Ansi378v2009Am1Minutia ma0 = fa.minutiae.get(0);
		assertSame(Ansi378v2009Am1MinutiaType.ENDING, ma0.type);
		assertEquals(500, ma0.positionX);
		assertEquals(300, ma0.positionY);
		assertEquals(99, ma0.angle);
		assertEquals(77, ma0.quality);
		Ansi378v2009Am1Minutia ma1 = fa.minutiae.get(1);
		assertSame(Ansi378v2009Am1MinutiaType.BIFURCATION, ma1.type);
		cx = fa.counts;
		assertSame(Ansi378v2009Am1CountType.OCTANTS, cx.type);
		assertEquals(2, cx.edges.size());
		Ansi378v2009Am1CountEdge ce0 = cx.edges.get(0);
		assertEquals(0, ce0.from);
		assertEquals(1, ce0.to);
		assertEquals(15, ce0.count);
		Ansi378v2009Am1CountEdge ce1 = cx.edges.get(1);
		assertEquals(1, ce1.from);
		assertEquals(0, ce1.to);
		assertEquals(25, ce1.count);
		cd = fa.coredelta;
		assertEquals(1, cd.cores.size());
		assertEquals(1, cd.deltas.size());
		Ansi378v2009Am1Core c0 = cd.cores.get(0);
		assertEquals(123, c0.positionX);
		assertEquals(234, c0.positionY);
		assertEquals(55, (int)c0.angle);
		Ansi378v2009Am1Delta d0 = cd.deltas.get(0);
		assertEquals(111, d0.positionX);
		assertEquals(222, d0.positionY);
		assertArrayEquals(new int[] { 10, 20, 30 }, d0.angles);
		assertEquals(1, fa.extensions.size());
		Ansi378v2009Am1Extension ex = fa.extensions.get(0);
		assertEquals(0x4455, ex.type);
		assertArrayEquals(new byte[] { 1, 2, 3 }, ex.data);
	}
	@Test
	public void compatibility() {
		byte[] v2009 = Ansi378v2009TemplateTest.sample();
		assertTrue(Ansi378v2009Template.accepts(v2009));
		assertFalse(Ansi378v2009Am1Template.accepts(v2009));
		new Ansi378v2009Am1Template(v2009, false);
	}
	@Test
	public void json() {
		TestUtils.compareJson(Ansi378v2009Am1TemplateTest.class, "ansi378v2009am1-sample.json", decode());
	}
	@Test
	public void spec() {
		Ansi378v2009Am1Template t = decode();
		assertEquals(0x0042, t.vendorId);
		assertEquals(0x0011, t.subformat);
		assertFalse(t.sensorCertified);
		assertEquals(0x0b5, t.sensorId);
		assertEquals(2, t.fingerprints.size());
		Ansi378v2009Am1Fingerprint fa = t.fingerprints.get(0);
		assertSame(Ansi378v2009Am1Position.LEFT_INDEX, fa.position);
		assertSame(Ansi378v2009Am1ScanType.LIVE_PLAIN, fa.scanType);
		assertEquals(90, fa.quality);
		assertEquals(0x0006, fa.qualityVendorId);
		assertEquals(0x0037, fa.qualityAlgorithmId);
		assertEquals(512, fa.width);
		assertEquals(512, fa.height);
		assertEquals(197, fa.resolutionX);
		assertEquals(197, fa.resolutionY);
		assertEquals(27, fa.minutiae.size());
		Ansi378v2009Am1Minutia ma0 = fa.minutiae.get(0);
		assertSame(Ansi378v2009Am1MinutiaType.ENDING, ma0.type);
		assertEquals(100, ma0.positionX);
		assertEquals(14, ma0.positionY);
		assertEquals(112.0, ma0.angle * 2 - 1, 1.0);
		assertEquals(90, ma0.quality);
		Ansi378v2009Am1Minutia ma26 = fa.minutiae.get(26);
		assertSame(Ansi378v2009Am1MinutiaType.BIFURCATION, ma26.type);
		assertEquals(126, ma26.positionX);
		assertEquals(115, ma26.positionY);
		assertEquals(172.0, ma26.angle * 2 - 1, 1.0);
		assertEquals(30, ma26.quality);
		Ansi378v2009Am1Fingerprint fb = t.fingerprints.get(1);
		assertSame(Ansi378v2009Am1Position.RIGHT_INDEX, fb.position);
		assertSame(Ansi378v2009Am1ScanType.LIVE_PLAIN, fb.scanType);
		assertEquals(0x0042, fb.qualityVendorId);
		assertEquals(0x000c, fb.qualityAlgorithmId);
		assertEquals(70, fb.quality);
		assertEquals(22, fb.minutiae.size());
		Ansi378v2009Am1Minutia mb0 = fb.minutiae.get(0);
		assertSame(Ansi378v2009Am1MinutiaType.ENDING, mb0.type);
		assertEquals(40, mb0.positionX);
		assertEquals(93, mb0.positionY);
		assertEquals(0.0, mb0.angle * 2 - 1, 1.0);
		assertEquals(90, mb0.quality);
		Ansi378v2009Am1Minutia mb21 = fb.minutiae.get(21);
		assertSame(Ansi378v2009Am1MinutiaType.BIFURCATION, mb21.type);
		assertEquals(125, mb21.positionX);
		assertEquals(73, mb21.positionY);
		assertEquals(350.0, mb21.angle * 2 - 1, 1.0);
		assertEquals(40, mb21.quality);
		Ansi378v2009Am1CountExtension cx = fb.counts;
		assertSame(Ansi378v2009Am1CountType.QUADRANTS, cx.type);
		assertEquals(4, cx.edges.size());
		Ansi378v2009Am1CountEdge ce0 = cx.edges.get(0);
		assertEquals(18, ce0.from);
		assertEquals(2, ce0.to);
		assertEquals(3, ce0.count);
	}
	@Test
	public void roundtripSpec() {
		byte[] original = sample();
		byte[] roundtripped = new Ansi378v2009Am1Template(original).toByteArray();
		assertArrayEquals(original, roundtripped);
	}
	@Test
	public void downgrade() {
		byte[] original = sample();
		byte[] downgraded = decode().downgrade().toByteArray();
		/*
		 * Version should be the only difference.
		 */
		downgraded[6] = '5';
		assertArrayEquals(original, downgraded);
	}
	private Ansi378v2009Am1Template decode() {
		/*
		 * Sample template from the spec.
		 */
		byte[] bytes = sample();
		assertTrue(Ansi378v2009Am1Template.accepts(bytes));
		return new Ansi378v2009Am1Template(bytes);
	}
	public static byte[] sample() {
		/*
		 * Sample template from the spec.
		 */
		return TestUtils.resource(Ansi378v2009Am1TemplateTest.class, "ansi378v2009am1-sample.dat");
	}
}
