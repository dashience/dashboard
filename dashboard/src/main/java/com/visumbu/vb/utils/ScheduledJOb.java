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

/**
 *
 * @author dashience
 */
public class ScheduledJOb extends QuartzJobBean{
    @Autowired
    private TimerService timeService;
    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        if(context.getTrigger().getKey().toString().equals("DEFAULT.cronTrigger")) {
               timeService.executeDailyTasks();
           }else if(context.getTrigger().getKey().toString().equals("DEFAULT.cronTrigger2")) {
                 timeService.executeMonthlyTask();          
           } else if(context.getTrigger().getKey().toString().equals("DEFAULT.cronTrigger3")) {
                 timeService.executeWeeklyTask();
           }else if(context.getTrigger().getKey().toString().equals("DEFAULT.cronTrigger4")) {
                 timeService.executeYearlyTask();
           }
           else {
               
           }
    }
    
}
