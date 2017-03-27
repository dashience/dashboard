/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.SchedulerHistory;
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

        String scheduledHour = hour + ":00";
        if (hour < 10) {
            scheduledHour = "0" + hour + ":00";
        }
        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType and d.startDate = :startDate and d.schedulerTime = :hour";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Daily");
        query.setParameter("startDate", today);
        query.setParameter("hour", scheduledHour);
        return query.list();
    }

    public List<Scheduler> getWeeklyTasks(Integer hour, String weekDay, Date today) {

        String scheduledHour = hour + ":00";
        if (hour < 10) {
            scheduledHour = "0" + hour + ":00";
        }

        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType and d.startDate = :startDate and d.schedulerWeekly = :schedulerWeekly and d.schedulerTime = :hour";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Weekly");
        query.setParameter("startDate", today);
        query.setParameter("schedulerWeekly", weekDay);
        query.setParameter("hour", scheduledHour);
        return query.list();
    }

    public List<Scheduler> getMonthlyTasks(String currentDateHour, Date today) {
        System.out.println("Query: ");
        System.out.println(currentDateHour);
        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType and d.startDate = :startDate and d.schedulerMonthly = :schedulerMonthly";
        System.out.println(queryStr);
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Monthly");
        query.setParameter("startDate", today);
        query.setParameter("schedulerMonthly", currentDateHour);
        System.out.println(query.list());
//        query.setParameter("hour", scheduledHour);
        return query.list();
    }

    public List<Scheduler> getYearlyTasks(String currentDateHour, Date today) {
        System.out.println(currentDateHour);
        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType and d.schedulerYearly = :schedulerYearly";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Yearly");
        // query.setParameter("startDate", today);
        query.setParameter("schedulerYearly", currentDateHour);
//        query.setParameter("hour", scheduledHour);
        System.out.println(query.list());
        return query.list();
    }

    public List<Scheduler> getYearOfWeekTasks(Integer hour, String weekDay, Integer currentYearOfWeekCount, Date today) {
        System.out.println(currentYearOfWeekCount);

        String scheduledHour = hour + ":00";
        if (hour < 10) {
            scheduledHour = "0" + hour + ":00";
        }
        System.out.println(scheduledHour);

        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType "
                + "and d.startDate = :startDate and d.schedulerWeekly = :weekly and d.schedulerYearOfWeek = :yearOfWeek and d.schedulerTime = :hour";
        System.out.println(queryStr);
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        System.out.println(queryStr);
        query.setParameter("schedulerRepeatType", "Year Of Week");
        query.setParameter("startDate", today);
        query.setParameter("weekly", weekDay);
        query.setParameter("yearOfWeek", currentYearOfWeekCount);
        query.setParameter("hour", scheduledHour);
        System.out.println(query.list());
        return query.list();
    }

    public List<Scheduler> getOnce(Integer hour, Date today) {

        System.out.println(hour);
        System.out.println(today);
        String scheduledHour = hour + ":00";
        if (hour < 10) {
            scheduledHour = "0" + hour + ":00";
        }
        String queryStr = "select d from Scheduler d where d.schedulerRepeatType = :schedulerRepeatType and d.startDate = :startDate and d.schedulerTime = :hour";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Once");
        query.setParameter("startDate", today);
        query.setParameter("hour", scheduledHour);
        System.out.println(query.list());
        return query.list();
    }

    public List<SchedulerHistory> getSchedulerHistoryById(Integer schedulerId) {
        String queryStr = "select d from SchedulerHistory d where d.schedulerId.id = :schedulerId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerId", schedulerId);
        //query.executeUpdate();
        return query.list();
    }

}
