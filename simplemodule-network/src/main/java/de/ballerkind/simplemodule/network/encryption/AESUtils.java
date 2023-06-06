package de.ballerkind.simplemodule.network.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtils {

	private static final String CIPHER_IDENTIFIER = "AES/CBC/PKCS5PADDING";
	private static final int IV_LENGTH = 16;
	private static final int KEY_SIZE = 128;

	private static IvParameterSpec ok;

	public static SecretKey generate() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(KEY_SIZE);
			return keyGen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] encrypt(byte[] bytes, SecretKey secretKey) {
		try {
			byte[] initVector = generateInitVector();
			IvParameterSpec iv = ok = new IvParameterSpec(initVector);

			Cipher cipher = Cipher.getInstance(CIPHER_IDENTIFIER);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

			byte[] encrypted = cipher.doFinal(bytes);

			byte[] fnl = new byte[IV_LENGTH + encrypted.length];
			System.arraycopy(initVector, 0, fnl, 0, IV_LENGTH);
			System.arraycopy(encrypted, 0, fnl, IV_LENGTH, encrypted.length);

			return fnl;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(byte[] bytes, SecretKey secretKey) {
		try {
			byte[] initVector = new byte[IV_LENGTH];
			byte[] encrypted = new byte[bytes.length - IV_LENGTH];
			System.arraycopy(bytes, 0, initVector, 0, IV_LENGTH);
			System.arraycopy(bytes, IV_LENGTH, encrypted, 0, encrypted.length);

			IvParameterSpec iv = new IvParameterSpec(initVector);

			Cipher cipher = Cipher.getInstance(CIPHER_IDENTIFIER);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

			return cipher.doFinal(encrypted);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SecretKey getKeyFromByteArray(byte[] bytes) {
		return new SecretKeySpec(bytes, "AES");
	}

	private static byte[] generateInitVector() {
		byte[] iv = new byte[IV_LENGTH];
		new SecureRandom().nextBytes(iv);
		return iv;
	}

}