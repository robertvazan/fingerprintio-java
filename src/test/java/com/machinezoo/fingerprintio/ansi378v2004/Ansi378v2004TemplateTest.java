// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.ansi378v2004;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.machinezoo.fingerprintio.*;

public class Ansi378v2004TemplateTest {
	@Test
	public void roundtrip() {
		Ansi378v2004Template t = new Ansi378v2004Template();
		t.width = 600;
		t.height = 400;
		t.resolutionX = 200;
		t.resolutionY = 180;
		t.sensorCertified = true;
		t.sensorId = 0x789;
		t.vendorId = 0x5678;
		t.subformat = 0x1234;
		Ansi378v2004Fingerprint f = new Ansi378v2004Fingerprint();
		f.position = Ansi378v2004Position.LEFT_RING;
		f.scanType = Ansi378v2004ScanType.NONLIVE_ROLLED;
		f.quality = 45;
		Ansi378v2004Minutia m = new Ansi378v2004Minutia();
		m.type = Ansi378v2004MinutiaType.ENDING;
		m.positionX = 500;
		m.positionY = 300;
		m.angle = 99;
		m.quality = 77;
		f.minutiae.add(m);
		m = new Ansi378v2004Minutia();
		m.type = Ansi378v2004MinutiaType.BIFURCATION;
		f.minutiae.add(m);
		Ansi378v2004CountExtension cx = f.counts = new Ansi378v2004CountExtension();
		cx.type = Ansi378v2004CountType.OCTANTS;
		Ansi378v2004CountEdge e = new Ansi378v2004CountEdge();
		e.from = 0;
		e.to = 1;
		e.count = 15;
		cx.edges.add(e);
		e = new Ansi378v2004CountEdge();
		e.from = 1;
		e.to = 0;
		e.count = 25;
		cx.edges.add(e);
		Ansi378v2004CoreDeltaExtension cd = f.coredelta = new Ansi378v2004CoreDeltaExtension();
		Ansi378v2004Core c = new Ansi378v2004Core();
		c.positionX = 123;
		c.positionY = 234;
		c.angle = 55;
		cd.cores.add(c);
		Ansi378v2004Delta d = new Ansi378v2004Delta();
		d.positionX = 111;
		d.positionY = 222;
		d.angles = new int[] { 10, 20, 30 };
		cd.deltas.add(d);
		Ansi378v2004Extension x = new Ansi378v2004Extension();
		x.type = 0x4455;
		x.data = new byte[] { 1, 2, 3 };
		f.extensions.add(x);
		t.fingerprints.add(f);
		f = new Ansi378v2004Fingerprint();
		f.position = Ansi378v2004Position.LEFT_LITTLE;
		t.fingerprints.add(f);
		f = new Ansi378v2004Fingerprint();
		f.position = Ansi378v2004Position.LEFT_RING;
		f.view = 1;
		t.fingerprints.add(f);
		t = new Ansi378v2004Template(t.toByteArray());
		assertEquals(600, t.width);
		assertEquals(400, t.height);
		assertEquals(200, t.resolutionX);
		assertEquals(180, t.resolutionY);
		assertTrue(t.sensorCertified);
		assertEquals(0x789, t.sensorId);
		assertEquals(0x5678, t.vendorId);
		assertEquals(0x1234, t.subformat);
		assertEquals(3, t.fingerprints.size());
		Ansi378v2004Fingerprint fa = t.fingerprints.get(0);
		Ansi378v2004Fingerprint fb = t.fingerprints.get(1);
		Ansi378v2004Fingerprint fc = t.fingerprints.get(2);
		assertSame(Ansi378v2004Position.LEFT_RING, fa.position);
		assertSame(Ansi378v2004Position.LEFT_LITTLE, fb.position);
		assertSame(Ansi378v2004Position.LEFT_RING, fc.position);
		assertEquals(0, fa.view);
		assertEquals(0, fb.view);
		assertEquals(1, fc.view);
		assertSame(Ansi378v2004ScanType.NONLIVE_ROLLED, fa.scanType);
		assertEquals(45, fa.quality);
		assertEquals(2, fa.minutiae.size());
		Ansi378v2004Minutia ma0 = fa.minutiae.get(0);
		assertSame(Ansi378v2004MinutiaType.ENDING, ma0.type);
		assertEquals(500, ma0.positionX);
		assertEquals(300, ma0.positionY);
		assertEquals(99, ma0.angle);
		assertEquals(77, ma0.quality);
		Ansi378v2004Minutia ma1 = fa.minutiae.get(1);
		assertSame(Ansi378v2004MinutiaType.BIFURCATION, ma1.type);
		cx = fa.counts;
		assertSame(Ansi378v2004CountType.OCTANTS, cx.type);
		assertEquals(2, cx.edges.size());
		Ansi378v2004CountEdge ce0 = cx.edges.get(0);
		assertEquals(0, ce0.from);
		assertEquals(1, ce0.to);
		assertEquals(15, ce0.count);
		Ansi378v2004CountEdge ce1 = cx.edges.get(1);
		assertEquals(1, ce1.from);
		assertEquals(0, ce1.to);
		assertEquals(25, ce1.count);
		cd = fa.coredelta;
		assertEquals(1, cd.cores.size());
		assertEquals(1, cd.deltas.size());
		Ansi378v2004Core c0 = cd.cores.get(0);
		assertEquals(123, c0.positionX);
		assertEquals(234, c0.positionY);
		assertEquals(55, (int)c0.angle);
		Ansi378v2004Delta d0 = cd.deltas.get(0);
		assertEquals(111, d0.positionX);
		assertEquals(222, d0.positionY);
		assertArrayEquals(new int[] { 10, 20, 30 }, d0.angles);
		assertEquals(1, fa.extensions.size());
		x = fa.extensions.get(0);
		assertEquals(0x4455, x.type);
		assertArrayEquals(new byte[] { 1, 2, 3 }, x.data);
	}
	@Test
	public void json() {
		TestUtils.compareJson(Ansi378v2004TemplateTest.class, "ansi378-sample.json", decode());
	}
	@Test
	public void spec() {
		Ansi378v2004Template t = decode();
		assertEquals(512, t.width);
		assertEquals(512, t.height);
		assertEquals(197, t.resolutionX);
		assertEquals(197, t.resolutionY);
		assertEquals(0x0042, t.vendorId);
		assertEquals(0x0011, t.subformat);
		assertFalse(t.sensorCertified);
		assertEquals(0x0b5, t.sensorId);
		assertEquals(2, t.fingerprints.size());
		Ansi378v2004Fingerprint fa = t.fingerprints.get(0);
		assertSame(Ansi378v2004Position.LEFT_INDEX, fa.position);
		assertEquals(90, fa.quality);
		assertSame(Ansi378v2004ScanType.LIVE_PLAIN, fa.scanType);
		assertEquals(27, fa.minutiae.size());
		/*
		 * We aren't testing angles, because the example in the spec has angles inconsistent with the spec.
		 * Sample angles are in range 0.255 even though the spec is clear that angles should be in range 0..179.
		 * There is no corrigenda for the 2004 version of the spec, but the sample is fixed in 2009 version.
		 */
		Ansi378v2004Minutia ma0 = fa.minutiae.get(0);
		assertSame(Ansi378v2004MinutiaType.ENDING, ma0.type);
		assertEquals(100, ma0.positionX);
		assertEquals(14, ma0.positionY);
		assertEquals(90, ma0.quality);
		Ansi378v2004Minutia ma26 = fa.minutiae.get(26);
		assertSame(Ansi378v2004MinutiaType.BIFURCATION, ma26.type);
		assertEquals(126, ma26.positionX);
		assertEquals(115, ma26.positionY);
		assertEquals(30, ma26.quality);
		Ansi378v2004Fingerprint fb = t.fingerprints.get(1);
		assertSame(Ansi378v2004Position.RIGHT_INDEX, fb.position);
		assertEquals(70, fb.quality);
		assertSame(Ansi378v2004ScanType.LIVE_PLAIN, fb.scanType);
		assertEquals(22, fb.minutiae.size());
		Ansi378v2004Minutia mb0 = fb.minutiae.get(0);
		assertSame(Ansi378v2004MinutiaType.ENDING, mb0.type);
		assertEquals(40, mb0.positionX);
		assertEquals(93, mb0.positionY);
		assertEquals(90, mb0.quality);
		Ansi378v2004Minutia mb21 = fb.minutiae.get(21);
		assertSame(Ansi378v2004MinutiaType.BIFURCATION, mb21.type);
		assertEquals(125, mb21.positionX);
		assertEquals(73, mb21.positionY);
		assertEquals(40, mb21.quality);
		assertEquals(0, fb.extensions.size());
		assertNotNull(fb.counts);
	}
	private Ansi378v2004Template decode() {
		byte[] bytes = sampleWithFixedVersion();
		/*
		 * Sample template in the spec is bogus. Check for exception. Then enable permissive decoding.
		 */
		try {
			new Ansi378v2004Template(bytes);
			fail();
		} catch (Throwable ex) {
			assertTrue(ex instanceof TemplateFormatException);
		}
		return new Ansi378v2004Template(bytes, false);
	}
	public static byte[] sample() {
		/*
		 * Sample template from the spec.
		 */
		return TestUtils.resource(Ansi378v2004TemplateTest.class, "ansi378-sample.dat");
	}
	public static byte[] sampleWithFixedVersion() {
		byte[] bytes = sample();
		/*
		 * Sample template from the spec doesn't even get its version number right.
		 * Fix it here, so that we can load it.
		 */
		bytes[4] = ' ';
		assertTrue(Ansi378v2004Template.accepts(bytes));
		return bytes;
	}
}
