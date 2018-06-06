package de.dieser1memesprech.proxsync.database.dao;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.dieser1memesprech.proxsync.database.model.UserModel;

@Dependent
@TransactionManagement(value=TransactionManagementType.CONTAINER)
public class UserDao extends BaseDao<UserModel, Long> {


	public UserDao() {
		super();
	}
	
}

