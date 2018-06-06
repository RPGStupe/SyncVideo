package de.dieser1memesprech.proxsync.rest;

import de.dieser1memesprech.proxsync.database.dao.UserDao;
import de.dieser1memesprech.proxsync.database.model.UserModel;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@ApplicationScoped
@Path("user")
public class UserRest {
    @Inject
    private UserDao userDao;



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

}
