/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.VbUser;
import java.util.Date;
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

    public List<Scheduler> getScheduledTasks(String intervalName) {
        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", intervalName);
        return query.list();
    }

    public List<Scheduler> getDailyTasks(Integer hour, Date today) {
        
        String scheduledHour = hour +":00";
        if(hour < 10) {
            scheduledHour = "0"+hour + ":00";
        }
        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType and d.startDate = :startDate and d.schedulerTime = :hour";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Daily");
        query.setParameter("startDate", today);
        query.setParameter("hour", scheduledHour);
        return query.list();
    }
    
    public List<Scheduler> getWeeklyTasks(Integer hour, String weekDay, Date today) {
        
        String scheduledHour = hour +":00";
        if(hour < 10) {
            scheduledHour = "0"+hour + ":00";
        }       
        
        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType and d.startDate = :startDate and d.schedulerWeekly = :schedulerWeekly and d.schedulerTime = :hour";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Weekly");
        query.setParameter("startDate", today);
        query.setParameter("schedulerWeekly", weekDay);
        query.setParameter("hour", scheduledHour);
        return query.list();
    }

}
