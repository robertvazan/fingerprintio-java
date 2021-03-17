// Part of FingerprintIO: https://fingerprintio.machinezoo.com
package com.machinezoo.fingerprintio.iso19794p1v2011;

import com.machinezoo.fingerprintio.utils.*;

/**
 * Capture date and time (<a href="https://templates.machinezoo.com/iso-19794-1-2011#datetime">DATETIME</a>).
 */
public class Iso19794p1v2011DateTime {
	/**
	 * Year (<a href="https://templates.machinezoo.com/iso-19794-1-2011#year">YEAR</a>).
	 * Defaults ot 0xffff (not provided).
	 */
	public int year = 0xffff;
	/**
	 * Month (<a href="https://templates.machinezoo.com/iso-19794-1-2011#month">MONTH</a>).
	 * Defaults ot 0xff (not provided).
	 */
	public int month = 0xff;
	/**
	 * Day of month (<a href="https://templates.machinezoo.com/iso-19794-1-2011#day">DAY</a>).
	 * Defaults ot 0xff (not provided).
	 */
	public int day = 0xff;
	/**
	 * Hour (<a href="https://templates.machinezoo.com/iso-19794-1-2011#hour">HOUR</a>).
	 * Defaults ot 0xff (not provided).
	 */
	public int hour = 0xff;
	/**
	 * Minute (<a href="https://templates.machinezoo.com/iso-19794-1-2011#minute">MINUTE</a>).
	 * Defaults ot 0xff (not provided).
	 */
	public int minute = 0xff;
	/**
	 * Second (<a href="https://templates.machinezoo.com/iso-19794-1-2011#second">SECOND</a>).
	 * Defaults ot 0xff (not provided).
	 */
	public int second = 0xff;
	/**
	 * Millisecond (<a href="https://templates.machinezoo.com/iso-19794-1-2011#millisecond">MILLISECOND</a>).
	 * Defaults ot 0xffff (not provided).
	 */
	public int millisecond = 0xffff;
	/**
	 * Creates new capture date and time (<a href="https://templates.machinezoo.com/iso-19794-1-2011#datetime">DATETIME</a>).
	 */
	public Iso19794p1v2011DateTime() {
	}
	Iso19794p1v2011DateTime(TemplateReader in) {
		year = in.readUnsignedShort();
		month = in.readUnsignedByte();
		day = in.readUnsignedByte();
		hour = in.readUnsignedByte();
		minute = in.readUnsignedByte();
		second = in.readUnsignedByte();
		millisecond = in.readUnsignedShort();
	}
	void write(TemplateWriter out) {
		out.writeShort(year);
		out.writeByte(month);
		out.writeByte(day);
		out.writeByte(hour);
		out.writeByte(minute);
		out.writeByte(second);
		out.writeShort(millisecond);
	}
	void validate() {
		validate16(year, 1, 0xfffe, "Year must be in range 1 through 0xfffe if present or special value 0xffff if omitted.");
		validate8(month, 1, 12, "Month must be in range 1 through 12 if present or special value 0xff if omitted.");
		validate8(day, 1, 31, "Day of month must be in range 1 through 31 if present or special value 0xff if omitted.");
		validate8(hour, 0, 23, "Hour must be in range 0 through 23 if present or special value 0xff if omitted.");
		validate8(minute, 0, 59, "Minute must be in range 0 through 59 if present or special value 0xff if omitted.");
		validate8(second, 0, 59, "Second must be in range 0 through 59 if present or special value 0xff if omitted.");
		validate16(millisecond, 0, 999, "Millisecond must be in range 0 through 999 if present or special value 0xffff if omitted.");
		boolean defaulted = year == 0xffff;
		ValidateTemplate.condition(!defaulted || month == 0xff, "If year is not specified, month must not be specified either.");
		defaulted |= month == 0xff;
		ValidateTemplate.condition(!defaulted || day == 0xff, "If month is not specified, day of month must not be specified either.");
		defaulted |= day == 0xff;
		ValidateTemplate.condition(!defaulted || hour == 0xff, "If date is not specified, hour must not be specified either.");
		defaulted |= hour == 0xff;
		ValidateTemplate.condition(!defaulted || minute == 0xff, "If hour is not specified, minute must not be specified either.");
		defaulted |= minute == 0xff;
		ValidateTemplate.condition(!defaulted || second == 0xff, "If minute is not specified, second must not be specified either.");
		defaulted |= second == 0xff;
		ValidateTemplate.condition(!defaulted || millisecond == 0xffff, "If second is not specified, millisecond must not be specified either.");
	}
	private static void validate8(int component, int min, int max, String message) {
		ValidateTemplate.condition(component >= min && component <= max || component == 0xff, message);
	}
	private static void validate16(int component, int min, int max, String message) {
		ValidateTemplate.condition(component >= min && component <= max || component == 0xffff, message);
	}
}
