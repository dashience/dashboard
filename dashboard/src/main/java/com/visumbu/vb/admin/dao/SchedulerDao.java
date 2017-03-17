/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.VbUser;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author duc-dev-04
 */
@Transactional
@Repository("schedulerDao")
public class SchedulerDao extends BaseDao {

    public List getAgencyScheduler(VbUser user) {
        String queryStr = "select d from Scheduler d where (d.schedulerStatus is null or  d.schedulerStatus != 'Deleted') and d.agencyId.id = :agencyId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyId", user.getAgencyId().getId());
        return query.list();
    }

    public Scheduler getSchedulerById(Integer schedulerId) {
        Scheduler scheduler = (Scheduler) sessionFactory.getCurrentSession().get(Scheduler.class, schedulerId);
        return scheduler;
    }

    public Scheduler deleteScheduler(Integer schedulerId) {
        String queryStr = "update Scheduler d set schedulerStatus = 'Deleted' where d.id = :schedulerId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerId", schedulerId);
        query.executeUpdate();
        return null;
    }

}
