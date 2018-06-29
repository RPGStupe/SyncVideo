package org.dhbw.mosbach.ai.syncvideo.rest;

import org.dhbw.mosbach.ai.syncvideo.database.dao.WatchlistDao;
import org.dhbw.mosbach.ai.syncvideo.database.model.WatchlistModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * REST-Interface for watchlist-actions
 */
@ApplicationScoped
@Path("watchlist")
public class WatchlistRest {
    @Inject
    private WatchlistDao watchlistDao;

    /**
     * add a new url to watchlist
     * @param sessionId session-id of user
     * @param url url of video
     * @return statuscode 200 if url was saved successfully
     */
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

    /**
     * get all urls of user
     * @param sessionId session-id of user
     * @return list of urls
     */
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
