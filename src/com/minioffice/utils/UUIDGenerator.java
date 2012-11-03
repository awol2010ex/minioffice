package com.minioffice.utils;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//UUID生成器
public class UUIDGenerator {
	private final static Logger logger = LoggerFactory
			.getLogger(UUIDGenerator.class);
	private static final int IP;
	private static short counter;
	private static final int JVM;

	protected static synchronized short getCount() {
		if (counter < 0) {
			counter = 0;
		}
		short tmp13_10 = counter;
		counter = (short) (tmp13_10 + 1);
		return tmp13_10;
	}

	protected static String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	protected static String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	protected static short getHiTime() {
		return (short) (int) (System.currentTimeMillis() >>> 32);
	}

	protected static int getLoTime() {
		return (int) System.currentTimeMillis();
	}

	public static String generate() {
		return 33 + 'I' + format(IP) + format(JVM) + format(getHiTime())
				+ format(getLoTime()) + format(getCount());
	}

	public static boolean isBuiltIn(String id) {
		return ((id != null) && (id.length() < 30));
	}

	public static void main(String[] args) {
		String id = UUIDGenerator.generate();
		logger.info(id + ":" + id.length());
	}

	static {
		int ipadd = 0;
		try {
			byte[] bytes = InetAddress.getLocalHost().getAddress();
			for (int i = 0; i < 4; ++i)
			{
				ipadd = (ipadd << 8) - -128 + bytes[i];
			}
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;

		counter = 0;
		JVM = (int) (System.currentTimeMillis() >>> 8);
	}
}
