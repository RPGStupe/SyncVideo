package org.dhbw.mosbach.ai.syncvideo.database.dao;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;

import org.dhbw.mosbach.ai.syncvideo.database.model.UserModel;

/**
 *Persistance-DAO for users
 */
@Dependent
@TransactionManagement(value=TransactionManagementType.CONTAINER)
public class UserDao extends BaseDao<UserModel, Long> {
	public UserDao() {
		super();
	}
}

