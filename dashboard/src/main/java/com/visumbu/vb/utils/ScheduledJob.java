/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.visumbu.vb.admin.service.TimerService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


/**
 *
 * @author dashience
 */

@Component
public class ScheduledJob extends QuartzJobBean{

    public void setTimeService(TimerService timeService) {
        this.timeService = timeService;
    }
    @Autowired
    private TimerService timeService;
    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        if(context.getTrigger().getKey().toString().equals("DEFAULT.cronTrigger")) {
             System.out.println(context.getTrigger().getKey().toString()+"................crontrigger1");
            System.out.println(timeService.toString()+".........................timeservice object");  
            timeService.executeDailyTasks();
       }else if(context.getTrigger().getKey().toString().equals("DEFAULT.cronTrigger2")) {

               System.out.println(context.getTrigger().getKey().toString()+"................crontrigger2");
                           System.out.println(timeService.toString()+".........................timeservice object");  
               timeService.executeMonthlyTask();          
           } else if(context.getTrigger().getKey().toString().equals("DEFAULT.cronTrigger3")) {
               System.out.println(context.getTrigger().getKey().toString()+"............crontrigger3");
                 timeService.executeWeeklyTask();
           }else if(context.getTrigger().getKey().toString().equals("DEFAULT.cronTrigger4")) {
               System.out.println(context.getTrigger().getKey().toString()+".........crontrigger4");
                 timeService.executeYearlyTask();
           }
           else {
               
           }
    }
    
}
