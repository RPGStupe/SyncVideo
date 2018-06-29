package org.dhbw.mosbach.ai.syncvideo.database.dao;

import org.dhbw.mosbach.ai.syncvideo.database.model.WatchlistModel;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;

/*
    Persistance-DAO for watchlist
 */
@Dependent
@TransactionManagement(value=TransactionManagementType.CONTAINER)
public class WatchlistDao extends BaseDao<WatchlistModel, Long>{
    public WatchlistDao() {
        super();
    }
}
