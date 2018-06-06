package de.dieser1memesprech.proxsync.database.dao;

import de.dieser1memesprech.proxsync.database.model.WatchlistModel;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;

@Dependent
@TransactionManagement(value=TransactionManagementType.CONTAINER)
public class WatchlistDao extends BaseDao<WatchlistModel, Long>{
    public WatchlistDao() {
        super();
    }
}
