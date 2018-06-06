package de.dieser1memesprech.proxsync.rest;

import de.dieser1memesprech.proxsync.database.dao.UserDao;
import de.dieser1memesprech.proxsync.database.model.UserModel;
import de.dieser1memesprech.proxsync.util.RandomString;
import de.dieser1memesprech.proxsync.user.User;

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
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(UserModel user) {
    	userDao.persist(user);
		return Response.status(200).build();
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel update(UserModel user) {
        userDao.merge(user);
        return user;
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel get(@CookieParam("sessionId") String sessionId) {
        return userDao.findById(sessionToUid.get(sessionId));
    }

    @DELETE
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel remove(@PathParam("id") Long id) {
        UserModel user = userDao.findById(id);
        userDao.removeDetached(user);
        return user;
    }

    @POST
    @Path("/picture/avatar")
    public Response changeAvatar(@HeaderParam("url") String url, @CookieParam("sessionId") String sessionId) {
        UserModel user = userDao.findById(sessionToUid.get(sessionId));
        user.setAvatar(url);
        userDao.merge(user);
        return Response.status(200).build();
    }

    @POST
    @Path("/picture/banner")
    public Response changeBanner(@HeaderParam("url") String url, @CookieParam("sessionId") String sessionId) {
        UserModel user = userDao.findByUnique("id", sessionToUid.get(sessionId));
        user.setBanner(url);
        userDao.merge(user);
        return Response.status(200).build();
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
                return Response.status(200)
                        .cookie(new NewCookie("sessionId", sessionId,"/", "", "", 1000000, false))
                        .cookie(new NewCookie("loggedIn", "true","/", "", "", 1000000, false))
                        .build();
            }
        }
        return Response.status(501).build();
    }

    @POST
    @Path("/logout")
    public Response logout(@CookieParam("sessionId") String sessionId) {
        sessionToUid.remove(sessionId);
        return Response.status(200)
                .cookie(new NewCookie("loggedIn", "false","/", "", "", 1000000, false))
                .build();
    }
}
