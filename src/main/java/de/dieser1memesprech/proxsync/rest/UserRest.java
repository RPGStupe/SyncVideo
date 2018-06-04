package de.dieser1memesprech.proxsync.rest;

import de.dieser1memesprech.proxsync.database.dao.UserDao;
import de.dieser1memesprech.proxsync.database.model.UserModel;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.glassfish.jersey.process.internal.RequestScoped;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@RequestScoped //this make the diference
@Path("user")
public class UserRest {
    @Inject
    private UserDao userDao;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserModel add(UserModel user) {
        userDao.persist(user);
        return user;
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserModel> all() {
        return userDao.getAll();
    }

    @GET
    @Path("/test")
    public void test() {
        System.out.println("TEST");
    }

}
