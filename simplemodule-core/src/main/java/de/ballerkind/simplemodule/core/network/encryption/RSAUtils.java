package de.ballerkind.simplemodule.core.network.encryption;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {

	private static final int KEY_SIZE = 2048;

	public static KeyPair generate() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(KEY_SIZE);
			return keyGen.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static byte[] crypt(int mode, byte[] bytes, Key key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(mode, key);
			return cipher.doFinal(bytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] encrypt(byte[] bytes, PublicKey publicKey) {
		return crypt(Cipher.ENCRYPT_MODE, bytes, publicKey);
	}

	public static byte[] decrypt(byte[] bytes, PrivateKey privateKey) {
		return crypt(Cipher.DECRYPT_MODE, bytes, privateKey);
	}

	public static PublicKey getPublicKeyFromByteArray(byte[] bytes) {
		try {
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PrivateKey getPrivateKeyFromByteArray(byte[] bytes) {
		try {
			return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}