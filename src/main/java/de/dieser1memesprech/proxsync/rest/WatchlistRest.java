package de.dieser1memesprech.proxsync.rest;

import de.dieser1memesprech.proxsync.database.dao.WatchlistDao;
import de.dieser1memesprech.proxsync.database.model.WatchlistModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    @Path("/add")
    public Response add(@CookieParam("sessionId") String sessionId, @HeaderParam("url") String url) {
        if(UserRest.sessionToUid.get(sessionId) != null) {
            WatchlistModel watchlistModel = new WatchlistModel();
            watchlistModel.setUserId(UserRest.sessionToUid.get(sessionId));
            watchlistModel.setUrl(url);
            watchlistModel.setTimestamp(System.currentTimeMillis());
            watchlistDao.persist(watchlistModel);
        }
        return Response.status(200).build();
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WatchlistModel> getAll(@CookieParam("sessionId") String sessionId) {
        List<WatchlistModel> watchlist = new ArrayList<>();
        List<WatchlistModel> listOfAll = watchlistDao.getAll();
        for(WatchlistModel model : listOfAll) {
            if(model.getUserId().equals(UserRest.sessionToUid.get(sessionId)))
                watchlist.add(model);
        }
        return watchlist;
    }
}
