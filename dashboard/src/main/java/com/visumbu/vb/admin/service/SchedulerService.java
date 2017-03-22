/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.SchedulerDao;
import com.visumbu.vb.model.Scheduler;
import com.visumbu.vb.model.SchedulerHistory;
import com.visumbu.vb.model.VbUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author duc-dev-04
 */
@Service("schedulerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SchedulerService {
    
    @Autowired
    private SchedulerDao schedulerDao;

    public Scheduler createScheduler(Scheduler scheduler) {
        return (Scheduler) schedulerDao.create(scheduler);
    }

    public Scheduler updateScheduler(Scheduler scheduler) {
        return (Scheduler) schedulerDao.update(scheduler);
    }

    public List getScheduler(VbUser user) {
        if (user.getAgencyId() == null) {
            List<Scheduler> scheduler = schedulerDao.read(Scheduler.class);
            return scheduler;
        }
        return schedulerDao.getAgencyScheduler(user);
    }

    public Scheduler getSchedulerById(Integer schedulerId) {
        return schedulerDao.getSchedulerById(schedulerId);
    }

    public Scheduler deleteScheduler(Integer schedulerId) {
       return schedulerDao.deleteScheduler(schedulerId);
    }
    
    public SchedulerHistory createSchedulerHistory(SchedulerHistory schedulerHistory) {
        return (SchedulerHistory) schedulerDao.create(schedulerHistory);
    }
    
}
