package de.dieser1memesprech.proxsync.rest;

import de.dieser1memesprech.proxsync.database.dao.UserDao;
import de.dieser1memesprech.proxsync.database.dao.WatchlistDao;
import de.dieser1memesprech.proxsync.database.model.UserModel;
import de.dieser1memesprech.proxsync.database.model.WatchlistModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("watchlist")
public class WatchlistRest {
    @Inject
    private WatchlistDao watchlistDao;

    @POST
    @Path("/add/{userid}")
    public Response add(@PathParam("userid") Long userId, @HeaderParam("url") String url) {
        WatchlistModel watchlistModel = new WatchlistModel();
        watchlistModel.setUserId(userId);
        watchlistModel.setUrl(url);
        watchlistModel.setTimestamp(System.currentTimeMillis());
        watchlistDao.persist(watchlistModel);
        return Response.status(200).build();
    }

    @GET
    @Path("/get/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public /*List<WatchlistModel>*/String getAll(@PathParam("userid") Long userId) {
        List<WatchlistModel> watchlist = new ArrayList<>();
        List<WatchlistModel> listOfAll = watchlistDao.getAll();
        for(WatchlistModel model : listOfAll) {
            if(model.getUserId().equals(userId))
                watchlist.add(model);
        }
        //return watchlist;
        String retString = "";
        for(WatchlistModel model : watchlist) {
            retString += model.getUrl() + "\n";
        }
        return retString;
    }
}
