package com.aheath.resources;

import com.aheath.models.Session;
import com.aheath.models.User;
import com.aheath.dao.UserDAO;
import com.aheath.models.UserDto;
import com.aheath.security.AuthenticationService;
import com.aheath.security.PasswordEncoder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jdbi.v3.core.Jdbi;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticator;

    public UserResource(Jdbi jdbi) {
        this.userDAO = jdbi.onDemand(UserDAO.class);
        this.authenticator = new AuthenticationService(jdbi);
    }


    @GET
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Optional<Session> authenticateUser(UserDto userDto) {

        User queriedUser = this.userDAO.getUserByEmail(userDto.getEmail());
        boolean validated = authenticator.authenticateUser(userDto.getPassword(), queriedUser.getPw_hash(), queriedUser.getSalt());

        if(validated) {
            // Create session
            Session session = authenticator.createSession(queriedUser);
            return Optional.of(session);
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

        // TODO: Change this so it doesn't send back the hashed password
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
