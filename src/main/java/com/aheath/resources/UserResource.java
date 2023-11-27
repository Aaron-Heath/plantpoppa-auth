package com.aheath.resources;

import com.aheath.api.User;
import com.aheath.db.UserDAO;
import com.aheath.security.PasswordEncoder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jdbi.v3.core.Jdbi;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

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
        // USER SHOULD ONLY HAVE EMAIL and PW

        // get salt from user if exists.

        return user;
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public int createUser(User user) {
        //https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
//        SecureRandom random = new SecureRandom();
//        byte[] salt = new byte[16];
//        random.nextBytes(salt);
        Base64.Encoder encoder = Base64.getEncoder();
//        System.out.printf("salt: %s%n", encoder.encodeToString(salt));
        //   Instantiate password encoder.
        try {
            passwordEncoder = new PasswordEncoder();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
//        Store salt string in user
        user.setSalt(this.passwordEncoder.generateSalt());

//        Encrypt Password
        user.setPw_hash(this.passwordEncoder.encryptPassword(user.getPw_hash(), user.getSalt()));
//        KeySpec spec = new PBEKeySpec(user.getPw_hash().toCharArray(),user.getSalt(),65536,128);
//        SecretKeyFactory factory = null;
//        try {
//            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }

        return userDAO.createUser(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPw_hash(),
                user.getPhone(),
                user.getZip(),
                user.getSalt()
        );
    }

}
