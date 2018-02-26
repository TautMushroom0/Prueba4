package com.osi.gaudi.security.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Base64;

import org.apache.axis.encoding.Base64;


public class EncryptingUtil {
//	private static final String SECRET_KEY = "TXz3JoI86CCn5WA/FOkTRQ==";

	public static String encrypt(String text, EEncryptingAlgorithm algorithm) {
		try {
			SecretKeySpec skeySpec = getSecretKeySpec(algorithm);

			Cipher cipher = Cipher.getInstance(algorithm.getName());
			cipher.init(1, skeySpec);

			byte[] encryptedBytes = cipher.doFinal(text.getBytes());
			
//			byte[] encodedBytes = Base64.encodeBase64(encryptedBytes);
//			return new String(encodedBytes);
			
			return Base64.encode(encryptedBytes);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String text, EEncryptingAlgorithm algorithm) {
		try {
			SecretKeySpec skeySpec = getSecretKeySpec(algorithm);

			Cipher cipher = Cipher.getInstance(algorithm.getName());
			cipher.init(2, skeySpec);

//			byte[] decodedBytes = Base64.decodeBase64(text.getBytes());
//			byte[] decryptedBytes = cipher.doFinal(decodedBytes);
//			return new String(decryptedBytes);
			
			byte[] decodedBytes = Base64.decode(text);
			byte[] decryptedBytes = cipher.doFinal(decodedBytes);
			return new String(decryptedBytes);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SecretKeySpec getSecretKeySpec(EEncryptingAlgorithm algorithm)
			throws NoSuchAlgorithmException {
		
//		byte[] secretKey = Base64.decode("TXz3JoI86CCn5WA/FOkTRQ==".getBytes());
		
		byte[] secretKey = Base64.decode("TXz3JoI86CCn5WA/FOkTRQ==");
		
		SecretKeySpec skeySpec = new SecretKeySpec(secretKey,
				algorithm.getName());
		return skeySpec;
	}

	public static String generateDigest(String text, EDigestAlgorithm algorithm) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm
					.getName());
			byte[] hashBytes = digest.digest(text.getBytes());
			
//			byte[] encodedBytes = Base64.encodeBase64(hashBytes);
//			return new String(encodedBytes);
			
			return Base64.encode(hashBytes);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean verifyDigest(String text, String digest,
			EDigestAlgorithm algorithm) {
		return generateDigest(text, algorithm).equals(digest);
	}

	public static void main(String[] args) {
		String text = "Cualquier texto.";
		String encryptedText = encrypt(text, EEncryptingAlgorithm.AES);
		System.out.println("Encrypted: " + encryptedText);

		String originalText = decrypt(encryptedText, EEncryptingAlgorithm.AES);
		System.out.println("Original: " + originalText);

		String digest = generateDigest(text, EDigestAlgorithm.MD5);
		System.out.println(digest);

		boolean valid = verifyDigest(text, digest, EDigestAlgorithm.MD5);
		System.out.println(valid);
	}
}