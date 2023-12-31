package com.aheath.resources;

import com.aheath.annotations.AuthenticationRequired;
import com.aheath.models.Session;
import com.aheath.models.User;
import com.aheath.models.UserDto;
import com.aheath.services.AuthenticationService;
import com.aheath.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private AuthenticationService authenticator;
    private UserService userService;

    @Inject
    public UserResource(AuthenticationService authenticationService, UserService userService) {
        this.authenticator = authenticationService;
        this.userService = userService;
    }

    @AuthenticationRequired
    @GET
    public List<User> getAllUsers() {
//        return userDAO.getAllUsers();
        return userService.getAllUsers();
    }

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Optional<Session> authenticateUser(UserDto userDto) {

//        User queriedUser = this.userDAO.getUserByEmail(userDto.getEmail());
//        boolean validated = authenticator.authenticateUser(userDto.getPassword(), queriedUser.getPw_hash(), queriedUser.getSalt());
        return authenticator.authenticateUser(userDto);
//        if(validated) {
//            // Create session
//            Session session = authenticator.createSession(userDto.toUser());
//            return Optional.of(session);
//        }
//        return Optional.empty();

    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public int createUser(UserDto userDto) {

        UserDto userDto1 = userService.createUser(userDto);

        return 0;

        //https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
//        try {
//            passwordEncoder = new PasswordEncoder();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        // Store salt string in user
//        user.setSalt(this.passwordEncoder.generateSalt());
//
//        // Encrypt Password
//        user.setPw_hash(this.passwordEncoder.encryptPassword(user.getPw_hash(), user.getSalt()));
//
//        // TODO: Change this so it doesn't send back the hashed password
//        return userDAO.createUser(
//                user.getUuid(),
//                user.getFirstname(),
//                user.getLastname(),
//                user.getEmail(),
//                user.getPw_hash(),
//                user.getPhone(),
//                user.getZip(),
//                user.getSalt()
//        );
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
