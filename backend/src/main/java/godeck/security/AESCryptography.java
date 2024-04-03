package godeck.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.stereotype.Component;

import godeck.utils.ErrorHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class that manages the AES cryptography. It is responsible for generating
 * keys and IVs, encrypting and decrypting messages.
 * 
 * Is a component. Can be injected anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@Getter
public class AESCryptography {
    // Properties

    private SecretKey key;
    private IvParameterSpec iv;
    private static final String KEY_ALGORITHM = "AES";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    // Constructors

    /**
     * Main constructor. Generates a new key and IV.
     * 
     * @param keySizeBytes The size of the key in bytes.
     */
    public AESCryptography(int keySizeBytes) {
        try {
            this.key = generateKey(keySizeBytes);
            this.iv = generateIV();
        } catch (NoSuchAlgorithmException e) {
            ErrorHandler.message(e);
        }
    }

    // Private Methods

    /**
     * Generates a new key with the given size.
     * 
     * @param keySizeBytes The size of the key in bytes.
     * @return The generated key.
     * @throws NoSuchAlgorithmException If the algorithm is not found. Should never
     *                                  happen.
     */
    private SecretKey generateKey(int keySizeBytes) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(keySizeBytes * 8);
        this.key = keyGenerator.generateKey();
        return this.key;
    }

    /**
     * Generates a new Initialization Vector (IV).
     * 
     * @return The generated IV.
     */
    private IvParameterSpec generateIV() {
        byte[] ivByte = new byte[16];
        new SecureRandom().nextBytes(ivByte);
        this.iv = new IvParameterSpec(ivByte);
        return this.iv;
    }

    /**
     * Adds the needed bytes to the message to make it a multiple of 16 bytes so it
     * can be encrypted. The added bytes are underscores at the beginning of the
     * message.
     * 
     * @param msg The message to be added bytes.
     * @return The message with the added bytes.
     */
    private String addNeededBytes(String msg) {
        int neededBytes = 16 - (msg.getBytes().length % 16);
        String addition = "";
        for (int i = 0; i < neededBytes; i++) {
            addition += "_";
        }
        return addition + msg;
    }

    // Public Methods

    /**
     * Encrypts the given input using the key and IV.
     * 
     * @param input The input to be encrypted.
     * @return The encrypted input.
     * @throws NoSuchPaddingException             If the padding is not found.
     *                                            Should never happen.
     * @throws NoSuchAlgorithmException           If the algorithm is not found.
     *                                            Should never happen.
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are
     *                                            invalid. Should never happen.
     * @throws InvalidKeyException                If the key is invalid. Should
     *                                            never happen if the key was
     *                                            generated by this class.
     * @throws BadPaddingException                If the padding is bad. Should
     *                                            never happen.
     * @throws IllegalBlockSizeException          If the block size is illegal.
     *                                            Should never happen.
     */
    public String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
        String msg = addNeededBytes(input);
        byte[] cipherText = cipher.doFinal(msg.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Decrypts the given cipher text using the key and IV.
     * 
     * @param cipherText The cipher text to be decrypted.
     * @return The decrypted cipher text.
     * @throws NoSuchPaddingException             If the padding is not found.
     *                                            Should never happen.
     * @throws NoSuchAlgorithmException           If the algorithm is not found.
     *                                            Should never happen.
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are
     *                                            invalid. Should never happen.
     * @throws InvalidKeyException                If the key is invalid. Should
     *                                            never happen if the key was
     *                                            generated by this class.
     * @throws BadPaddingException                If the cipherText padding is bad.
     * @throws IllegalBlockSizeException          If the cipherText block size is
     *                                            illegal.
     */
    public String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    /**
     * Gets the key as a string. It's bytes are encoded in Base64.
     * 
     * @return The key as a string.
     */
    public String getKeyString() {
        return Base64.getEncoder().encodeToString(this.key.getEncoded());
    }

    /**
     * Gets the IV as a string. It's bytes are encoded in Base64.
     * 
     * @return The IV as a string.
     */
    public String getIVString() {
        return Base64.getEncoder().encodeToString(this.iv.getIV());
    }
}
