/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.SchedulerHistory;
import com.visumbu.vb.model.VbUser;
import java.util.Calendar;
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

    public List<Scheduler> getDailyTasks(Integer hour, Date today, Agency agency) {

        String scheduledHour = hour + ":00";
        if (hour < 10) {
            scheduledHour = "0" + hour + ":00";
        }
        String queryStr = "select d from Scheduler d where d.status = 'Active' and d.schedulerRepeatType = :schedulerRepeatType and d.schedulerTime = :hour and agencyId := agency";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Daily");
        query.setParameter("hour", scheduledHour);
        query.setParameter("agency", agency);
        return query.list();
    }

    public List<Scheduler> getWeeklyTasks(Integer hour, String weekDay, Date today) {

        String scheduledHour = hour + ":00";
        if (hour < 10) {
            scheduledHour = "0" + hour + ":00";
        }

        String queryStr = "select d from Scheduler d where d.status = 'Active' and d.schedulerRepeatType = :schedulerRepeatType and d.schedulerWeekly = :schedulerWeekly and d.schedulerTime = :hour";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerRepeatType", "Weekly");
        query.setParameter("schedulerWeekly", weekDay);
        query.setParameter("hour", scheduledHour);
        return query.list();
    }

    public List<Scheduler> getMonthlyTasks(String currentDateHour, Date today) {
        System.out.println("Query: ");
        Calendar now = Calendar.getInstance();
        System.out.println(currentDateHour);
        String queryStr = "select d from Scheduler d where d.status = 'Active' and d.schedulerRepeatType = 'Monthly' "
                + "and dayofmonth(str_to_date(d.schedulerMonthly, '%m/%d/%y %H')) =" + now.get(Calendar.DAY_OF_MONTH)
                + "and hour(str_to_date(d.schedulerMonthly, '%m/%d/%y %H')) = " + now.get(Calendar.HOUR_OF_DAY);
        System.out.println(queryStr);
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        System.out.println(query.list());
//        query.setParameter("hour", scheduledHour);
        return query.list();
    }

    public List<Scheduler> getYearlyTasks(String currentDateHour, Date today) {
        System.out.println(currentDateHour);
        try {
            Calendar now = Calendar.getInstance();
            String queryStr = "select d from Scheduler d where d.status = 'Active' and d.schedulerRepeatType = 'Yearly' "
                    + "and month(str_to_date(d.schedulerYearly, '%m/%d/%y %H')) = " + (now.get(Calendar.MONTH) + 1)
                    + "and dayofmonth(str_to_date(d.schedulerYearly, '%m/%d/%y %H')) =" + now.get(Calendar.DAY_OF_MONTH)
                    + "and hour(str_to_date(d.schedulerYearly, '%m/%d/%y %H')) = " + now.get(Calendar.HOUR_OF_DAY);
            Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
            System.out.println(query.list());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Scheduler> getYearOfWeekTasks(Integer hour, String weekDay, Integer currentYearOfWeekCount, Date today) {
        System.out.println(currentYearOfWeekCount);

        String scheduledHour = hour + ":00";
        if (hour < 10) {
            scheduledHour = "0" + hour + ":00";
        }
        System.out.println(scheduledHour);

        String queryStr = "select d from Scheduler d where d.status = 'Active' and d.schedulerRepeatType = :schedulerRepeatType "
                + "and d.schedulerWeekly = :weekly and d.schedulerYearOfWeek = :yearOfWeek and d.schedulerTime = :hour";
        System.out.println(queryStr);
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        System.out.println(queryStr);
        query.setParameter("schedulerRepeatType", "Year Of Week");
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
        String queryStr = "select d from Scheduler d where d.status = 'Active' and d.schedulerRepeatType = :schedulerRepeatType and d.startDate = :startDate and d.schedulerTime = :hour";
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

    public Scheduler updateSchedulerEnableDisable(Scheduler scheduler) {
        String queryStr;
        Integer schedulerId = scheduler.getId();
        Boolean activeStatus = Boolean.parseBoolean(scheduler.getStatus());
        if (activeStatus == true) {
            queryStr = "update Scheduler d set status = 'Active' where d.id = :schedulerId";
        } else {
            queryStr = "update Scheduler d set status = 'InActive' where d.id = :schedulerId";
        }
        System.out.println(queryStr);
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("schedulerId", schedulerId);
        query.executeUpdate();
        return null;
    }

    public List<Agency> getAllAgency() {
        return (List<Agency>) read(Agency.class);
    }
}
