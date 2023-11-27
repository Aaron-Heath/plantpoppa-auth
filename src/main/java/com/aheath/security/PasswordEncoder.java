package com.aheath.security;

import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncoder {
    //    Encodes password + salt hash as a string
    private Base64.Encoder encoder = Base64.getEncoder();

    //    Creates Hash of password + salt
    private SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    //    This is for generating salts
    private SecureRandom random = new SecureRandom();

    private final int iterationCount = 65536;
    private final int keyLength = 128;

    public PasswordEncoder() throws NoSuchAlgorithmException {
    }

//    //    Security Methods
//    public boolean validate(String inputPassword,String validPassword, String salt) {
//        //  Hash Input Password using Salt
//        KeySpec spec = new PBEKeySpec(inputPassword.toCharArray(), salt, this.iterationCount, this.keyLength);
//
//        //
//    }

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
}
