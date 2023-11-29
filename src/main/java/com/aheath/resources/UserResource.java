package com.aheath.resources;

import com.aheath.api.User;
import com.aheath.db.UserDAO;
import com.aheath.security.AuthenticationService;
import com.aheath.security.PasswordEncoder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.checkerframework.checker.units.qual.C;
import org.jdbi.v3.core.Jdbi;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.print.attribute.standard.Media;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticator = new AuthenticationService();

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
    public Optional<User> authenticateUser(User user) {

        User queriedUser = this.userDAO.getUserByEmail(user.getEmail());
        boolean validated = authenticator.authenticateUser(user.getPw_hash(), queriedUser.getPw_hash(), queriedUser.getSalt());

        if(validated) {
            return Optional.of(new User(
                    queriedUser.getUser_id(),
                    queriedUser.getFirstname(),
                    queriedUser.getLastname(),
                    queriedUser.getEmail(),
                    queriedUser.getPhone(),
                    queriedUser.getZip()
            ));
        }
        return Optional.empty();

    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public int createUser(User user) {
        //https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
        try {
            passwordEncoder = new PasswordEncoder();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // Store salt string in user
        user.setSalt(this.passwordEncoder.generateSalt());

        // Encrypt Password
        user.setPw_hash(this.passwordEncoder.encryptPassword(user.getPw_hash(), user.getSalt()));

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

    //TODO: This cannot be completed until a token is generated, stored, and passed.
//    @PUT
//    @Path("/chngpw")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public int changePassword(User user, String password2) {
//        // Validate correct previous password was sent
//        return 0;
//    }


}
