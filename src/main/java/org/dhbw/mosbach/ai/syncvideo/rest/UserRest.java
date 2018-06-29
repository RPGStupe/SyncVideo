package org.dhbw.mosbach.ai.syncvideo.rest;

import org.dhbw.mosbach.ai.syncvideo.database.dao.UserDao;
import org.dhbw.mosbach.ai.syncvideo.database.model.UserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/*
    Rest-Interface for user-actions
 */
@ApplicationScoped
@Path("user")
public class UserRest {
    @Inject
    private UserDao userDao;

    static Map<String, Long> sessionToUid = new HashMap<>();

    /**
     * add a new user
     * @param user new user
     * @return statuscode 200 if user was added successfully
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@HeaderParam("user") String user, @HeaderParam("pw") String pw) {
        UserModel usermodel = new UserModel();
        usermodel.setUsername(user);
        usermodel.setPw(pw);
    	userDao.persist(usermodel);
		return Response.status(200).build();
    }

    /**
     * chance settings of user
     * @param user user to change
     * @return statuscode 200 if user was changed successfully
     */
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel update(UserModel user) {
        userDao.merge(user);
        return user;
    }

    /**
     * get a user by its sessionId
     * @param sessionId sessionId of user
     * @return user
     */
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel get(@CookieParam("sessionId") String sessionId) {
        return userDao.findById(sessionToUid.get(sessionId));
    }

    /**
     * remove a user
     * @param id user-id of user to remove
     * @return removed user
     */
    @DELETE
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel remove(@PathParam("id") Long id) {
        UserModel user = userDao.findById(id);
        userDao.removeDetached(user);
        return user;
    }

    /**
     * change Avatar-url
     * @param url avatar-url
     * @param sessionId session-id of user
     * @return statuscode 200 if avatar was changed successfully
     */
    @POST
    @Path("/picture/avatar")
    public Response changeAvatar(@HeaderParam("url") String url, @CookieParam("sessionId") String sessionId) {
        UserModel user = userDao.findById(sessionToUid.get(sessionId));
        user.setAvatar(url);
        userDao.merge(user);
        return Response.status(200).build();
    }

    /**
     * change banner-url
     * @param url url of banner
     * @param sessionId session-id of user
     * @return statuscode 200 if banner was changed successfully
     */
    @POST
    @Path("/picture/banner")
    public Response changeBanner(@HeaderParam("url") String url, @CookieParam("sessionId") String sessionId) {
        UserModel user = userDao.findByUnique("id", sessionToUid.get(sessionId));
        user.setBanner(url);
        userDao.merge(user);
        return Response.status(200).build();
    }

    /**
     * login-method
     * @param name name of user
     * @param password password of user
     * @return statuscode 200 if user was logged in successfully
     */
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

    /**
     * logout method
     * @param sessionId session-id of user
     * @return statuscode 200 if user was logged out successfully
     */
    @POST
    @Path("/logout")
    public Response logout(@CookieParam("sessionId") String sessionId) {
        sessionToUid.remove(sessionId);
        return Response.status(200)
                .cookie(new NewCookie("loggedIn", "false","/", "", "", 1000000, false))
                .build();
    }
}
