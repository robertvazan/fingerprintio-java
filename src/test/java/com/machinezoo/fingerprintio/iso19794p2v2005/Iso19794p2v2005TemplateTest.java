// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2005;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.machinezoo.fingerprintio.*;
import com.machinezoo.noexception.*;

public class Iso19794p2v2005TemplateTest {
	@Test
	public void roundtrip() {
		Iso19794p2v2005Template t = new Iso19794p2v2005Template();
		t.width = 600;
		t.height = 400;
		t.resolutionX = 200;
		t.resolutionY = 180;
		t.sensorCertified = true;
		t.sensorId = 0x789;
		Iso19794p2v2005Fingerprint f = new Iso19794p2v2005Fingerprint();
		f.position = Iso19794p2v2005Position.LEFT_RING;
		f.scanType = Iso19794p2v2005ScanType.NONLIVE_ROLLED;
		f.quality = 45;
		Iso19794p2v2005Minutia m = new Iso19794p2v2005Minutia();
		m.type = Iso19794p2v2005MinutiaType.ENDING;
		m.positionX = 500;
		m.positionY = 300;
		m.angle = 99;
		m.quality = 77;
		f.minutiae.add(m);
		m = new Iso19794p2v2005Minutia();
		m.type = Iso19794p2v2005MinutiaType.BIFURCATION;
		f.minutiae.add(m);
		Iso19794p2v2005CountExtension cx = f.counts = new Iso19794p2v2005CountExtension();
		cx.type = Iso19794p2v2005CountType.OCTANTS;
		Iso19794p2v2005CountEdge e = new Iso19794p2v2005CountEdge();
		e.from = 0;
		e.to = 1;
		e.count = 15;
		cx.edges.add(e);
		e = new Iso19794p2v2005CountEdge();
		e.from = 1;
		e.to = 0;
		e.count = 25;
		cx.edges.add(e);
		Iso19794p2v2005CoreDeltaExtension cd = f.coredelta = new Iso19794p2v2005CoreDeltaExtension();
		Iso19794p2v2005Core c = new Iso19794p2v2005Core();
		c.positionX = 123;
		c.positionY = 234;
		c.angle = 55;
		cd.cores.add(c);
		c = new Iso19794p2v2005Core();
		c.positionX = 321;
		c.positionY = 312;
		cd.cores.add(c);
		Iso19794p2v2005Delta d = new Iso19794p2v2005Delta();
		d.positionX = 111;
		d.positionY = 222;
		d.angles = new int[] { 10, 20, 30 };
		cd.deltas.add(d);
		d = new Iso19794p2v2005Delta();
		d.positionX = 333;
		d.positionY = 303;
		cd.deltas.add(d);
		Iso19794p2v2005ZonalExtension zx = f.zones = new Iso19794p2v2005ZonalExtension();
		zx.zoneWidth = 200;
		zx.zoneHeight = 200;
		zx.bits = 3;
		zx.quality = new int[] { 1, 2, 3, 4, 5, 6 };
		Iso19794p2v2005Extension x = new Iso19794p2v2005Extension();
		x.type = 0x4455;
		x.data = new byte[] { 1, 2, 3 };
		f.extensions.add(x);
		t.fingerprints.add(f);
		f = new Iso19794p2v2005Fingerprint();
		f.position = Iso19794p2v2005Position.LEFT_LITTLE;
		t.fingerprints.add(f);
		f = new Iso19794p2v2005Fingerprint();
		f.position = Iso19794p2v2005Position.LEFT_RING;
		f.view = 1;
		t.fingerprints.add(f);
		t = new Iso19794p2v2005Template(t.toByteArray());
		assertEquals(600, t.width);
		assertEquals(400, t.height);
		assertEquals(200, t.resolutionX);
		assertEquals(180, t.resolutionY);
		assertTrue(t.sensorCertified);
		assertEquals(0x789, t.sensorId);
		assertEquals(3, t.fingerprints.size());
		Iso19794p2v2005Fingerprint fa = t.fingerprints.get(0);
		Iso19794p2v2005Fingerprint fb = t.fingerprints.get(1);
		Iso19794p2v2005Fingerprint fc = t.fingerprints.get(2);
		assertSame(Iso19794p2v2005Position.LEFT_RING, fa.position);
		assertSame(Iso19794p2v2005Position.LEFT_LITTLE, fb.position);
		assertSame(Iso19794p2v2005Position.LEFT_RING, fc.position);
		assertEquals(0, fa.view);
		assertEquals(0, fb.view);
		assertEquals(1, fc.view);
		assertSame(Iso19794p2v2005ScanType.NONLIVE_ROLLED, fa.scanType);
		assertEquals(45, fa.quality);
		assertEquals(2, fa.minutiae.size());
		Iso19794p2v2005Minutia ma0 = fa.minutiae.get(0);
		assertSame(Iso19794p2v2005MinutiaType.ENDING, ma0.type);
		assertEquals(500, ma0.positionX);
		assertEquals(300, ma0.positionY);
		assertEquals(99, ma0.angle);
		assertEquals(77, ma0.quality);
		Iso19794p2v2005Minutia ma1 = fa.minutiae.get(1);
		assertSame(Iso19794p2v2005MinutiaType.BIFURCATION, ma1.type);
		cx = fa.counts;
		assertSame(Iso19794p2v2005CountType.OCTANTS, cx.type);
		assertEquals(2, cx.edges.size());
		Iso19794p2v2005CountEdge ce0 = cx.edges.get(0);
		assertEquals(0, ce0.from);
		assertEquals(1, ce0.to);
		assertEquals(15, ce0.count);
		Iso19794p2v2005CountEdge ce1 = cx.edges.get(1);
		assertEquals(1, ce1.from);
		assertEquals(0, ce1.to);
		assertEquals(25, ce1.count);
		cd = fa.coredelta;
		assertEquals(2, cd.cores.size());
		assertEquals(2, cd.deltas.size());
		Iso19794p2v2005Core c0 = cd.cores.get(0);
		assertEquals(123, c0.positionX);
		assertEquals(234, c0.positionY);
		assertEquals(55, (int)c0.angle);
		Iso19794p2v2005Core c1 = cd.cores.get(1);
		assertNull(c1.angle);
		Iso19794p2v2005Delta d0 = cd.deltas.get(0);
		assertEquals(111, d0.positionX);
		assertEquals(222, d0.positionY);
		assertArrayEquals(new int[] { 10, 20, 30 }, d0.angles);
		Iso19794p2v2005Delta d1 = cd.deltas.get(1);
		assertNull(d1.angles);
		Iso19794p2v2005ZonalExtension zq = fa.zones;
		assertEquals(200, zq.zoneWidth);
		assertEquals(200, zq.zoneHeight);
		assertEquals(3, zq.bits);
		assertEquals(6, zq.quality.length);
		assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, zq.quality);
		assertEquals(1, fa.extensions.size());
		Iso19794p2v2005Extension ex = fa.extensions.get(0);
		assertEquals(0x4455, ex.type);
		assertArrayEquals(new byte[] { 1, 2, 3 }, ex.data);
	}
	@Test
	public void json() {
		TestUtils.compareJson(Iso19794p2v2005TemplateTest.class, "iso19794p2v2005-sample.json", decode());
	}
	@Test
	public void spec() {
		Iso19794p2v2005Template t = decode();
		assertEquals(512, t.width);
		assertEquals(512, t.height);
		assertEquals(197, t.resolutionX);
		assertEquals(197, t.resolutionY);
		assertEquals(2, t.fingerprints.size());
		Iso19794p2v2005Fingerprint fa = t.fingerprints.get(0);
		assertSame(Iso19794p2v2005Position.LEFT_INDEX, fa.position);
		assertEquals(90, fa.quality);
		assertSame(Iso19794p2v2005ScanType.LIVE_PLAIN, fa.scanType);
		assertEquals(27, fa.minutiae.size());
		Iso19794p2v2005Minutia ma0 = fa.minutiae.get(0);
		assertSame(Iso19794p2v2005MinutiaType.ENDING, ma0.type);
		assertEquals(100, ma0.positionX);
		assertEquals(14, ma0.positionY);
		assertEquals(112, ma0.angle / 256.0 * 360, 1.0);
		assertEquals(90, ma0.quality);
		Iso19794p2v2005Minutia ma26 = fa.minutiae.get(26);
		assertSame(Iso19794p2v2005MinutiaType.BIFURCATION, ma26.type);
		assertEquals(126, ma26.positionX);
		assertEquals(115, ma26.positionY);
		assertEquals(172, ma26.angle / 256.0 * 360, 1.0);
		assertEquals(30, ma26.quality);
		Iso19794p2v2005Fingerprint fb = t.fingerprints.get(1);
		assertSame(Iso19794p2v2005Position.RIGHT_INDEX, fb.position);
		assertEquals(70, fb.quality);
		assertSame(Iso19794p2v2005ScanType.LIVE_PLAIN, fb.scanType);
		assertEquals(22, fb.minutiae.size());
		Iso19794p2v2005Minutia mb0 = fb.minutiae.get(0);
		assertSame(Iso19794p2v2005MinutiaType.ENDING, mb0.type);
		assertEquals(40, mb0.positionX);
		assertEquals(93, mb0.positionY);
		assertEquals(0, mb0.angle / 256.0 * 360, 1.0);
		assertEquals(90, mb0.quality);
		Iso19794p2v2005Minutia mb21 = fb.minutiae.get(21);
		assertSame(Iso19794p2v2005MinutiaType.BIFURCATION, mb21.type);
		assertEquals(125, mb21.positionX);
		assertEquals(73, mb21.positionY);
		assertEquals(350, mb21.angle / 256.0 * 360, 1.0);
		assertEquals(40, mb21.quality);
		assertEquals(1, fb.extensions.size());
	}
	@Test
	public void roundtripSpec() {
		byte[] original = sample();
		byte[] roundtripped = new Iso19794p2v2005Template(original).toByteArray();
		assertArrayEquals(original, roundtripped);
	}
	@Test
	public void accepts() {
		for (TemplateFormat format : TemplateFormat.values()) {
			byte[] template = TestUtils.sample(format);
			assertEquals(format == TemplateFormat.ISO_19794_2_2005, Iso19794p2v2005Template.accepts(template));
			if (format != TemplateFormat.ISO_19794_2_2005)
				assertThrows(TemplateFormatException.class, () -> new Iso19794p2v2005Template(template, Exceptions.silence()));
		}
	}
	private Iso19794p2v2005Template decode() {
		byte[] bytes = sample();
		assertTrue(Iso19794p2v2005Template.accepts(bytes));
		return new Iso19794p2v2005Template(bytes);
	}
	public static byte[] sample() {
		/*
		 * Sample template from the spec.
		 */
		return TestUtils.resource(Iso19794p2v2005TemplateTest.class, "iso19794p2v2005-sample.dat");
	}
}
