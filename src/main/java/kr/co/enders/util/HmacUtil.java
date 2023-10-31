/**
 * 작성자 : 이혜민
 * 작성일시 : 2022.10.21
 * 설명 : GRS API HASH KEY
 */
package kr.co.enders.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64.Decoder;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.StringFixedSaltGenerator;

public class HmacUtil {
	private static Logger logger = Logger.getLogger(HmacUtil.class);
	
	// GRS
	public static String generateHMAC(String msg, String keyString) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		String digest = null;
		String algo = "HmacSHA256";
		SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
		Mac mac = Mac.getInstance(algo);
		mac.init(key);
		final byte[] bytes = mac.doFinal(msg.getBytes("UTF-8"));
		StringBuffer hash = new StringBuffer();
		for (int i = 0; i < bytes.length; i ++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				hash.append('0');
			}
			hash.append(hex);
		}
		digest = hash.toString();
		digest = digest.toUpperCase();
		return digest;
	}
}