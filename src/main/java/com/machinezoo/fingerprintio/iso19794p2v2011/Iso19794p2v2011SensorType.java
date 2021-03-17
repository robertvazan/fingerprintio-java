// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p2v2011;

/**
 * Sensor technology (<a href="https://templates.machinezoo.com/iso-19794-2-2011#devtech">DEVTECH</a>).
 */
public enum Iso19794p2v2011SensorType {
	/**
	 * Unknown (code 0).
	 */
	UNKNOWN,
	/**
	 * Optical / white light / total internal reflectance (code 1).
	 */
	OPTICAL_WHITE_TIR,
	/**
	 * Optical / white light / direct view on platen (code 2).
	 */
	OPTICAL_WHITE_DIRECT,
	/**
	 * Optical / white light / contactless (code 3).
	 */
	OPTICAL_WHITE_CONTACTLESS,
	/**
	 * Optical / monochromatic / visible / total internal reflectance (code 4).
	 */
	OPTICAL_MONOCHROMATIC_VISIBLE_TIR,
	/**
	 * Optical / monochromatic / visible / direct view on platen (code 5).
	 */
	OPTICAL_MONOCHROMATIC_VISIBLE_DIRECT,
	/**
	 * Optical / monochromatic / visible / contactless (code 6).
	 */
	OPTICAL_MONOCHROMATIC_VISIBLE_CONTACTLESS,
	/**
	 * Optical / monochromatic / infrared / total internal reflectance (code 7).
	 */
	OPTICAL_MONOCHROMATIC_INFRARED_TIR,
	/**
	 * Optical / monochromatic / infrared / direct view on platen (code 8).
	 */
	OPTICAL_MONOCHROMATIC_INFRARED_DIRECT,
	/**
	 * Optical / monochromatic / infrared / contactless (code 9).
	 */
	OPTICAL_MONOCHROMATIC_INFRARED_CONTACTLESS,
	/**
	 * Optical / multispectral / total internal reflectance (code 10).
	 */
	OPTICAL_MULTISPECTRAL_TIR,
	/**
	 * Optical / multispectral / direct view on platen (code 11).
	 */
	OPTICAL_MULTISPECTRAL_DIRECT,
	/**
	 * Optical / multispectral / contactless (code 12).
	 */
	OPTICAL_MULTISPECTRAL_CONTACTLESS,
	/**
	 * Electro luminescent (code 13).
	 */
	ELECTRO_LUMINESCENT,
	/**
	 * Semiconductor / capacitive (code 14).
	 */
	CAPACITIVE,
	/**
	 * Semiconductor / radio frequency (code 15).
	 */
	RADIO_FREQUENCY,
	/**
	 * Semiconductor / thermal (code 16).
	 */
	THERMAL,
	/**
	 * Pressure (code 17).
	 */
	PRESSURE,
	/**
	 * Ultrasound (code 18).
	 */
	ULTRASOUND,
	/**
	 * Mechanical (code 19).
	 */
	MECHANICAL,
	/**
	 * Glass fiber (code 20).
	 */
	GLASS_FIBER;
}
