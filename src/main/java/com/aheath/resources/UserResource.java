package com.aheath.resources;

import com.aheath.api.User;
import com.aheath.db.UserDAO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jdbi.v3.core.Jdbi;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDAO;

    public UserResource(Jdbi jdbi) {
        this.userDAO = jdbi.onDemand(UserDAO.class);
    }


    @GET
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public User authenticateUser(User user) {
        // TODO: Implement authentication
        return user;
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public int createUser(User user) {
        // TODO: Re-write create user creation using Base64
        //https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
        //TODO: Review Generate Random Salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        //TODO: Convert salt to string
        Base64.Encoder encoder = Base64.getEncoder();
//        System.out.printf("salt: %s%n", encoder.encodeToString(salt));

        //TODO: set salt
        user.setSalt(encoder.encodeToString(salt));

        KeySpec spec = new PBEKeySpec(user.getPw_hash().toCharArray(),salt,65536,128);
        SecretKeyFactory factory = null;
        // This works, but it currently stores the HASH with the brackets and quotes. It's uggo. I'd rather concatenate them
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try {
            byte[] hash = factory.generateSecret(spec).getEncoded();
            //TODO: DELETE HASH RETURN
            user.setPw_hash(encoder.encodeToString(hash));
            return userDAO.createUser(
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getPw_hash(),
                    user.getPhone(),
                    user.getZip(),
                    user.getSalt()
            );
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
