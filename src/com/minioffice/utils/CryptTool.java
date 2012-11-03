package com.minioffice.utils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.codec.binary.Base64;

public class CryptTool {
	private final static Logger logger = LoggerFactory.getLogger(CryptTool.class);
	public static Random random = new Random();

	public static String getRandomString(int length) {
		char[] ach = new char[length];
		for (int i = 0; i < ach.length; ++i) {
			ach[i] = getRandomChar();
		}

		return new String(ach);
	}

	public static char getRandomChar() {
		int iRandomInt = random.nextInt(52);
		char ch;
		if (iRandomInt < 10) {
			ch = (char) (iRandomInt + 48);
		} else if (iRandomInt < 36) {
			ch = (char) (iRandomInt + 56);
		} else {
			ch = (char) (iRandomInt + 61);
		}

		return ch;
	}

	public static String encrypt(String password) throws Exception {
		String sSafeCode = getRandomString(16);
		return sSafeCode + encryptWithSafeCode(password, sSafeCode);
	}

	public static void main(String[] args) throws Exception {
		logger.info(encrypt("manager"));
		try {
			if (authenticate("manager", "C5Z9mPi9J3DTaNBR/UeLwjQxK0kGA6ASwyOrtw==")) {
				logger.info("正确!");
			} else
			{
				logger.info("不正确!");
			}
		} catch (Exception e) {
			logger.info("不正确!",e);
		}
	}

	private static String encryptWithSafeCode(String password, String safeCode)
			throws Exception {
	
		byte[] abyteSafeCode = Base64.decodeBase64(safeCode);
		MessageDigest messagedigest = MessageDigest.getInstance("MD5");
		messagedigest.update(abyteSafeCode);
		messagedigest.update(password.getBytes("UTF8"));
		return Base64.encodeBase64String(messagedigest.digest());
	}

	public static boolean authenticate(String password, String cryptograph)
			throws Exception {
		if (cryptograph.length() <= 16) {
			throw new Exception("安全域存放的口令不正确！");
		}

		String sSafeCode = cryptograph.substring(0, 16);
		cryptograph = cryptograph.substring(16, cryptograph.length());
		String encryptedPassword = encryptWithSafeCode(password, sSafeCode);

		return Arrays.equals(encryptedPassword.getBytes(), cryptograph
				.getBytes());
	}

}
