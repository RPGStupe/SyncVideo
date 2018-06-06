package de.dieser1memesprech.proxsync;

import de.dieser1memesprech.proxsync.database.dao.UserDao;
import de.dieser1memesprech.proxsync.database.dao.WatchlistDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MyApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(UserDao.class).to(UserDao.class);
        bind(WatchlistDao.class).to(WatchlistDao.class);
    }
}