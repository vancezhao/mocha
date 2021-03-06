/**
 * 
 */
package com.coral.foundation.security.service;

import java.util.List;

import com.coral.foundation.security.basic.dao.BasicUserDao;
import com.coral.foundation.security.model.BasicUser;
import com.coral.foundation.spring.bean.SpringContextUtils;

/**
 * @author Coral.Ma
 *
 */
public class BasicUserService {

	BasicUserDao basicUserDao = SpringContextUtils.getBean(BasicUserDao.class);
	
	public BasicUser loadUserById(Long id) {
		return basicUserDao.load(id);
	}
	
	public List<BasicUser> findAll() {
		return basicUserDao.loadAll();
	}
	
	public BasicUser findById(Long id) {
		return basicUserDao.load(id);
	}
	
	public void merge(BasicUser entity) {
		basicUserDao.persist(entity);
	}
}
