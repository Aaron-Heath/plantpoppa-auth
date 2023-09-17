package com.aheath.resources;


import com.aheath.api.User;
import com.aheath.db.UserDAO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jdbi.v3.core.Jdbi;

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

}
