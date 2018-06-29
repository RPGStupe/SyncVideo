package org.dhbw.mosbach.ai.syncvideo;

import org.dhbw.mosbach.ai.syncvideo.database.dao.UserDao;
import org.dhbw.mosbach.ai.syncvideo.database.dao.WatchlistDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MyApplicationBinder extends AbstractBinder {
    /**
     * Method to bind all DAOs
     */
    @Override
    protected void configure() {
        bind(UserDao.class).to(UserDao.class);
        bind(WatchlistDao.class).to(WatchlistDao.class);
    }
}