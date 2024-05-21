package com.plantpoppa.auth.services;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class CredentialSecurityService {
    //    Encodes password + salt hash as a string
    private Base64.Encoder encoder = Base64.getEncoder();

    //    Creates Hash of password + salt
    private SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    //    This is for generating salts
    private SecureRandom random = new SecureRandom();

    private final int iterationCount = 65536;
    private final int keyLength = 128;
    private static final int secretLength = 32;

    public CredentialSecurityService() throws NoSuchAlgorithmException {
    }
    /**
     * This method takes no parameters. It generates a string to be used as a salt for password encryption and storage.
     *
     * @return a String salt to be used when encrypting passwords for storage.
     */
    public byte[] generateSalt() {

        byte[] salt = new byte[16];
        this.random.nextBytes(salt);
        return salt;
    }

    /**
     *
     * @param password string input by user either creating their account or password, or authenticating.
     * @param salt byte array either pulled from db when authenticating or randomly generated when creating or updateing password.
     * @return encoded string of password for storage or comparison.
     */
    public String encryptPassword(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, this.iterationCount, this.keyLength);
        try {
            byte[] hash = this.factory.generateSecret(spec).getEncoded();
            return this.encoder.encodeToString(hash);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateSecret() {
        byte[] bytes = new byte[secretLength];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }
}
