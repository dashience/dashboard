/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.scheduler.service;

import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

/**
 *
 * @author lino
 */
//@EnableScheduling
@Service
public class Sheduler {

    @Autowired
    ThreadPoolTaskScheduler newTaskScheduler;
    ScheduledFuture scheduledFuture;

    CronTrigger trigger;

    public void setDelay(String cronExpression) {
        System.out.println("the trigger = " + cronExpression);
        trigger = new CronTrigger(cronExpression);
    }

    public void start(SchedulerTemplate template) {
        RunnableTask task = new RunnableTask();
        task.setAccountId(template.getAccountId()); 
        task.setDataSet(template.getDataSet());

        scheduledFuture = newTaskScheduler.schedule(task, trigger);
    }

    public void changeTrigger(String cronExpression) {
        System.out.println("change trigger to: " + cronExpression);
        scheduledFuture.cancel(false);
        trigger = new CronTrigger(cronExpression);
//        start();
    }
//
//    @Scheduled(cron = "0/20 * * ? * *")
//    public void executeOnce() {
//        System.out.println("execution start---------->");
//        String url = "http://localhost:8080/SpringSocial/admin/getData?dataSourceName=linkedIn&dataSetName=companyProfile";
//        String output = Rest.getData(url, null);
//        System.out.println("output---------->" + output);
//    }
}
