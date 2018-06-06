package de.dieser1memesprech.proxsync.database.dao;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.dieser1memesprech.proxsync.database.model.UserModel;

@Dependent
public class UserDao extends BaseDao<UserModel, Long> {


	public UserDao() {
		super();
	}
	
}

