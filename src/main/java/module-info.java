// Part of FingerprintIO: https://fingerprintio.machinezoo.com
/**
 * FingerprintIO library implements a number of <a href="https://templates.machinezoo.com/">publicly documented</a>
 * fingerprint template formats defined by ISO and ANSI. Every version of every format has its own package.
 * 
 * @see <a href="https://fingerprintio.machinezoo.com/">FingerprintIO tutorial</a>
 */
module com.machinezoo.fingerprintio {
	exports com.machinezoo.fingerprintio;
	exports com.machinezoo.fingerprintio.common;
	exports com.machinezoo.fingerprintio.ansi378v2004;
	exports com.machinezoo.fingerprintio.ansi378v2009;
	exports com.machinezoo.fingerprintio.ansi378v2009am1;
	exports com.machinezoo.fingerprintio.iso19794p1v2011;
	exports com.machinezoo.fingerprintio.iso19794p2v2005;
	exports com.machinezoo.fingerprintio.iso19794p2v2011;
	requires com.machinezoo.noexception;
}
