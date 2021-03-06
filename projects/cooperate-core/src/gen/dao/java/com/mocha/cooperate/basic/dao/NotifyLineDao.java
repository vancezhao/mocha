package com.mocha.cooperate.basic.dao;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.coral.foundation.persistence.BaseDao;
import com.coral.foundation.security.model.BasicUser;
import com.mocha.cooperate.model.NotifyLine;

/**
  * NotifyLineDao is a auto Generated class. Please don't modify it.
  */
public class NotifyLineDao extends BaseDao<NotifyLine> {
	
	@Override
	public Class<NotifyLine> getEntityClass() {
		return NotifyLine.class;
	}
	

	public int loadNotifyNumber(BasicUser basicUser) {
		Query query = getEntityManager().createQuery("Select count(n) from NotifyLine as n where n.type='1' and n.notifiedUser = :notifiedUser");
		query.setParameter("notifiedUser", basicUser);
		Long count = (Long)query.getSingleResult();
		return count.intValue();
	}
	
	@Transactional
	public void readAllNotify(BasicUser basicUser) {
		try {
			Query query = getEntityManager().createQuery("UPDATE NotifyLine t SET t.type='0' where t.type='1' and t.notifiedUser = :notifiedUser");
			query.setParameter("notifiedUser", basicUser);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

