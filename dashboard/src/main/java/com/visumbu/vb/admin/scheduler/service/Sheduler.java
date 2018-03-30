/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.scheduler.service;

import com.visumbu.vb.admin.dao.MongoSchedulerDao;
import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.MongoScheduler;
import com.visumbu.vb.utils.Rest;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author lino
 */
@EnableScheduling
@Service
public class Sheduler {

    @Autowired
    MongoSchedulerDao schedulerDao;

//    @Scheduled(cron = "0/20 * * ? * *")
//    public void schedule() {
//        List<MongoScheduler> schedulerList = schedulerDao.getCronSchedules("0/20 * * ? * *");
//        System.out.println("cron----->" + schedulerList);
//        if (schedulerList != null) {
//            schedule(schedulerList);
//        }
//    }

    public void insertNewScheduler(MongoScheduler schedulerObj) {
        schedulerDao.insert(schedulerObj);
    }

    private void schedule(List<MongoScheduler> schedulerList) {
        for (MongoScheduler schedule : schedulerList) {
            RunnableTask task = new RunnableTask();
            task.setAccountId(schedule.getAccountId().getId());
            task.setDataSet(schedule.getDataSetId());
            task.setScheduleId(schedule.getId());
            task.run();
        }
    }
//    @Autowired
//    ThreadPoolTaskScheduler newTaskScheduler;
//    ScheduledFuture scheduledFuture;
//
//    CronTrigger trigger;
//
//    public void setDelay(String cronExpression) {
//        System.out.println("the trigger = " + cronExpression);
//        trigger = new CronTrigger(cronExpression);
//    }
//
//    public void start(SchedulerTemplate template) {
//        RunnableTask task = new RunnableTask();
//        task.setAccountId(template.getAccountId()); 
//        task.setDataSet(template.getDataSet());
//
//        scheduledFuture = newTaskScheduler.schedule(task, trigger);
//    }
//
//    public void changeTrigger(String cronExpression) {
//        System.out.println("change trigger to: " + cronExpression);
//        scheduledFuture.cancel(false);
//        trigger = new CronTrigger(cronExpression);
////        start();
//    }
//
//    @Scheduled(cron = "0/20 * * ? * *")
//    public void executeOnce() {
//        System.out.println("execution start---------->");
//        String url = "http://localhost:8080/SpringSocial/admin/getData?dataSourceName=linkedIn&dataSetName=companyProfile";
//        String output = Rest.getData(url, null);
//        System.out.println("output---------->" + output);
//    }
}
