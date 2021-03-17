// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.machinezoo.fingerprintio.*;
import com.machinezoo.fingerprintio.iso19794p1v2011.*;

public class Iso19794p2v2011TemplateTest {
	@Test
	public void roundtrip() {
		Iso19794p2v2011Template t = new Iso19794p2v2011Template();
		Iso19794p2v2011Fingerprint f = new Iso19794p2v2011Fingerprint();
		f.datetime.year = 2011;
		f.datetime.month = 3;
		f.datetime.day = 17;
		f.datetime.hour = 7;
		f.datetime.minute = 51;
		f.datetime.second = 33;
		f.datetime.millisecond = 789;
		f.sensorType = Iso19794p2v2011SensorType.OPTICAL_MONOCHROMATIC_INFRARED_TIR;
		f.sensorVendor = 0x1234;
		f.sensorId = 0x5678;
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		f.position = Iso19794p2v2011Position.LEFT_RING;
		f.scanType = Iso19794p2v2011ScanType.NONLIVE_ROLLED;
		f.endingType = Iso19794p2v2011EndingType.RIDGE_SKELETON_ENDPOINT;
		Iso19794p2v2011Certificate ct = new Iso19794p2v2011Certificate();
		ct.authority = 0x1234;
		ct.scheme = Iso19794p2v2011CertificationScheme.IMAGE_QUALITY_FOR_VERIFICATION;
		f.certificates.add(ct);
		Iso19794p1v2011Quality qr = new Iso19794p1v2011Quality();
		qr.vendor = 0x5432;
		qr.algorithm = 0x9876;
		qr.quality = 55;
		f.qrecords.add(qr);
		Iso19794p2v2011Minutia m = new Iso19794p2v2011Minutia();
		m.type = Iso19794p2v2011MinutiaType.ENDING;
		m.positionX = 500;
		m.positionY = 300;
		m.angle = 99;
		m.quality = 77;
		f.minutiae.add(m);
		m = new Iso19794p2v2011Minutia();
		m.type = Iso19794p2v2011MinutiaType.BIFURCATION;
		f.minutiae.add(m);
		Iso19794p2v2011CountExtension cx = f.counts = new Iso19794p2v2011CountExtension();
		cx.type = Iso19794p2v2011CountType.OCTANTS;
		Iso19794p2v2011CountEdge e = new Iso19794p2v2011CountEdge();
		e.from = 0;
		e.to = 1;
		e.count = 15;
		cx.edges.add(e);
		e = new Iso19794p2v2011CountEdge();
		e.from = 1;
		e.to = 0;
		e.count = 25;
		cx.edges.add(e);
		Iso19794p2v2011CoreDeltaExtension cd = f.coredelta = new Iso19794p2v2011CoreDeltaExtension();
		Iso19794p2v2011Core c = new Iso19794p2v2011Core();
		c.positionX = 123;
		c.positionY = 234;
		c.angle = 55;
		cd.cores.add(c);
		c = new Iso19794p2v2011Core();
		c.positionX = 321;
		c.positionY = 312;
		cd.cores.add(c);
		Iso19794p2v2011Delta d = new Iso19794p2v2011Delta();
		d.positionX = 111;
		d.positionY = 222;
		d.angles = new int[] { 10, 20, 30 };
		cd.deltas.add(d);
		d = new Iso19794p2v2011Delta();
		d.positionX = 333;
		d.positionY = 303;
		cd.deltas.add(d);
		Iso19794p2v2011ZonalExtension zx = f.zones = new Iso19794p2v2011ZonalExtension();
		zx.vendor = 0x2345;
		zx.algorithm = 0x6789;
		zx.zoneWidth = 200;
		zx.zoneHeight = 200;
		zx.bits = 3;
		zx.quality = new int[] { 1, 2, 3, 4, 5, 6 };
		Iso19794p2v2011Extension x = new Iso19794p2v2011Extension();
		x.type = 0x4455;
		x.data = new byte[] { 1, 2, 3 };
		f.extensions.add(x);
		t.fingerprints.add(f);
		f = new Iso19794p2v2011Fingerprint();
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		f.position = Iso19794p2v2011Position.RIGHT_INDEX_TO_RING;
		f.minutiae.add(m);
		t.fingerprints.add(f);
		f = new Iso19794p2v2011Fingerprint();
		f.width = 600;
		f.height = 400;
		f.resolutionX = 200;
		f.resolutionY = 180;
		f.position = Iso19794p2v2011Position.LEFT_RING;
		f.view = 1;
		f.minutiae.add(m);
		t.fingerprints.add(f);
		t = new Iso19794p2v2011Template(t.toByteArray());
		assertEquals(3, t.fingerprints.size());
		f = t.fingerprints.get(0);
		assertSame(Iso19794p2v2011Position.LEFT_RING, f.position);
		assertEquals(0, f.view);
		assertEquals(2011, f.datetime.year);
		assertEquals(3, f.datetime.month);
		assertEquals(17, f.datetime.day);
		assertEquals(7, f.datetime.hour);
		assertEquals(51, f.datetime.minute);
		assertEquals(33, f.datetime.second);
		assertEquals(789, f.datetime.millisecond);
		assertSame(Iso19794p2v2011SensorType.OPTICAL_MONOCHROMATIC_INFRARED_TIR, f.sensorType);
		assertEquals(0x1234, f.sensorVendor);
		assertEquals(0x5678, f.sensorId);
		assertEquals(600, f.width);
		assertEquals(400, f.height);
		assertEquals(200, f.resolutionX);
		assertEquals(180, f.resolutionY);
		assertSame(Iso19794p2v2011ScanType.NONLIVE_ROLLED, f.scanType);
		assertSame(Iso19794p2v2011EndingType.RIDGE_SKELETON_ENDPOINT, f.endingType);
		assertEquals(1, f.certificates.size());
		ct = f.certificates.get(0);
		assertEquals(0x1234, ct.authority);
		assertEquals(Iso19794p2v2011CertificationScheme.IMAGE_QUALITY_FOR_VERIFICATION, ct.scheme);
		assertEquals(1, f.qrecords.size());
		qr = f.qrecords.get(0);
		assertEquals(0x5432, qr.vendor);
		assertEquals(0x9876, qr.algorithm);
		assertEquals(55, qr.quality);
		assertEquals(2, f.minutiae.size());
		m = f.minutiae.get(0);
		assertSame(Iso19794p2v2011MinutiaType.ENDING, m.type);
		assertEquals(500, m.positionX);
		assertEquals(300, m.positionY);
		assertEquals(99, m.angle);
		assertEquals(77, m.quality);
		m = f.minutiae.get(1);
		assertSame(Iso19794p2v2011MinutiaType.BIFURCATION, m.type);
		cx = f.counts;
		assertSame(Iso19794p2v2011CountType.OCTANTS, cx.type);
		assertEquals(2, cx.edges.size());
		e = cx.edges.get(0);
		assertEquals(0, e.from);
		assertEquals(1, e.to);
		assertEquals(15, e.count);
		e = cx.edges.get(1);
		assertEquals(1, e.from);
		assertEquals(0, e.to);
		assertEquals(25, e.count);
		cd = f.coredelta;
		assertEquals(2, cd.cores.size());
		assertEquals(2, cd.deltas.size());
		c = cd.cores.get(0);
		assertEquals(123, c.positionX);
		assertEquals(234, c.positionY);
		assertEquals(55, (int)c.angle);
		c = cd.cores.get(1);
		assertNull(c.angle);
		d = cd.deltas.get(0);
		assertEquals(111, d.positionX);
		assertEquals(222, d.positionY);
		assertArrayEquals(new int[] { 10, 20, 30 }, d.angles);
		d = cd.deltas.get(1);
		assertNull(d.angles);
		zx = f.zones;
		assertEquals(0x2345, zx.vendor);
		assertEquals(0x6789, zx.algorithm);
		assertEquals(200, zx.zoneWidth);
		assertEquals(200, zx.zoneHeight);
		assertEquals(3, zx.bits);
		assertEquals(6, zx.quality.length);
		assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6 }, zx.quality);
		assertEquals(1, f.extensions.size());
		Iso19794p2v2011Extension ex = f.extensions.get(0);
		assertEquals(0x4455, ex.type);
		assertArrayEquals(new byte[] { 1, 2, 3 }, ex.data);
		f = t.fingerprints.get(1);
		assertSame(Iso19794p2v2011Position.RIGHT_INDEX_TO_RING, f.position);
		assertEquals(0, f.view);
		f = t.fingerprints.get(2);
		assertSame(Iso19794p2v2011Position.LEFT_RING, f.position);
		assertEquals(1, f.view);
	}
	@Test
	public void json() {
		TestUtils.compareJson(Iso19794p2v2011TemplateTest.class, "iso19794p2v2011-sample.json", decode());
	}
	@Test
	public void spec() {
		Iso19794p2v2011Template t = decode();
		assertEquals(2, t.fingerprints.size());
		Iso19794p2v2011Fingerprint f = t.fingerprints.get(0);
		assertEquals(512, f.width);
		assertEquals(512, f.height);
		assertEquals(197, f.resolutionX);
		assertEquals(197, f.resolutionY);
		assertEquals(0xb5, f.sensorId);
		assertSame(Iso19794p2v2011Position.LEFT_INDEX, f.position);
		assertSame(Iso19794p2v2011ScanType.LIVE_PLAIN, f.scanType);
		assertEquals(1, f.qrecords.size());
		Iso19794p1v2011Quality qr = f.qrecords.get(0);
		assertEquals(90, qr.quality);
		assertEquals(27, f.minutiae.size());
		Iso19794p2v2011Minutia m = f.minutiae.get(0);
		assertSame(Iso19794p2v2011MinutiaType.ENDING, m.type);
		assertEquals(100, m.positionX);
		assertEquals(14, m.positionY);
		assertEquals(112, m.angle / 256.0 * 360, 1.0);
		assertEquals(90, m.quality);
		m = f.minutiae.get(26);
		assertSame(Iso19794p2v2011MinutiaType.BIFURCATION, m.type);
		assertEquals(126, m.positionX);
		assertEquals(115, m.positionY);
		assertEquals(172, m.angle / 256.0 * 360, 1.0);
		assertEquals(30, m.quality);
		f = t.fingerprints.get(1);
		assertSame(Iso19794p2v2011Position.RIGHT_INDEX, f.position);
		assertSame(Iso19794p2v2011ScanType.LIVE_PLAIN, f.scanType);
		assertEquals(1, f.qrecords.size());
		qr = f.qrecords.get(0);
		assertEquals(70, qr.quality);
		assertEquals(22, f.minutiae.size());
		m = f.minutiae.get(0);
		assertSame(Iso19794p2v2011MinutiaType.ENDING, m.type);
		assertEquals(40, m.positionX);
		assertEquals(93, m.positionY);
		assertEquals(0, m.angle / 256.0 * 360, 1.0);
		assertEquals(90, m.quality);
		m = f.minutiae.get(21);
		assertSame(Iso19794p2v2011MinutiaType.BIFURCATION, m.type);
		assertEquals(125, m.positionX);
		assertEquals(73, m.positionY);
		assertEquals(350, m.angle / 256.0 * 360, 1.0);
		assertEquals(40, m.quality);
		assertEquals(1, f.extensions.size());
		assertArrayEquals(new byte[] { 0x01, 0x44, (byte)0xBC, 0x36, 0x21, 0x43 }, f.extensions.get(0).data);
	}
	@Test
	public void accepts() {
		for (TemplateFormat format : TemplateFormat.values()) {
			byte[] template = TestUtils.sample(format);
			assertEquals(format == TemplateFormat.ISO_19794_2_2011, Iso19794p2v2011Template.accepts(template));
			if (format != TemplateFormat.ISO_19794_2_2011)
				assertThrows(TemplateFormatException.class, () -> new Iso19794p2v2011Template(template, false));
		}
	}
	private Iso19794p2v2011Template decode() {
		byte[] bytes = sample();
		assertTrue(Iso19794p2v2011Template.accepts(bytes));
		/*
		 * Sample template in the spec is bogus. Check for exception. Then enable permissive decoding.
		 */
		assertThrows(TemplateFormatException.class, () -> new Iso19794p2v2011Template(bytes));
		return new Iso19794p2v2011Template(bytes, false);
	}
	public static byte[] sample() {
		/*
		 * Sample template from the spec.
		 */
		return TestUtils.resource(Iso19794p2v2011TemplateTest.class, "iso19794p2v2011-sample.dat");
	}
}
