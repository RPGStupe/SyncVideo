package de.dieser1memesprech.proxsync.rest;

import de.dieser1memesprech.proxsync.database.dao.UserDao;
import de.dieser1memesprech.proxsync.database.model.UserModel;
import de.dieser1memesprech.proxsync.util.RandomString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;


@ApplicationScoped
@Path("user")
public class UserRest {
    @Inject
    private UserDao userDao;

    public static Map<String, Long> sessionToUid = new HashMap<>();


    @GET
    @Path("/add/{username}/{pw}")
    public Response add(@PathParam("username") String username, @PathParam("pw") String pw) {
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPw(pw);
        userDao.persist(user);
        return Response.status(200).build();
    }

    @GET
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel update(UserModel user) {
        userDao.merge(user);
        return user;
    }

    @GET
    @Path("/getbyid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel getById(@PathParam("id") Long id) {
        UserModel user = userDao.findById(id);
        return user;
    }

    @DELETE
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel remove(@PathParam("id") Long id) {
        UserModel user = userDao.findById(id);
        userDao.removeDetached(user);
        return user;
    }

    @GET
    @Path("/all")
    public String all() {
        List<UserModel> users = userDao.getAll();
        String s = "";
        for (UserModel user : users) {
            s += user.getUsername() + " " + user.getPw() + "\n";
        }
        return s;
    }

    @GET
    @Path("/test")
    public String test() {
        return "TEST";
    }

    @GET
    @Path("/login/{username}/{pw}")
    public Response login(@PathParam("username") String name, @PathParam("pw") String password)
    {
        UserModel user = userDao.findByUnique("username", name);
        if (user != null) {
            if (password.equals(user.getPw())) {
                String sessionId = UUID.randomUUID().toString();
                sessionToUid.put(sessionId, user.getId());
                return Response.status(200).cookie(new NewCookie("sessionId", sessionId)).build();
            }
        }
        return Response.status(501).build();
    }

}
