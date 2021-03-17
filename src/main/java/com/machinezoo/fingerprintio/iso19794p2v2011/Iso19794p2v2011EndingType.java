// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

/**
 * Ridge ending type (<a href="https://templates.machinezoo.com/iso-19794-2-2011#endingtype">ENDINGTYPE</a>).
 */
public enum Iso19794p2v2011EndingType {
	/**
	 * Ending minutiae are located at valley skeleton bifurcations.
	 */
	VALLEY_SKELETON_BIFURCATION,
	/**
	 * Ending minutiae are located at ridge skeleton endpoints.
	 */
	RIDGE_SKELETON_ENDPOINT;
}
