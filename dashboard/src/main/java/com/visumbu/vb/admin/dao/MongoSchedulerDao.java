/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.MongoScheduler;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lino
 */
@Transactional
@Repository("MongoSchedulerDao")
public class MongoSchedulerDao extends BaseDao{

    public void insert(MongoScheduler mongoScheduler){
        create(mongoScheduler);
    }
    public List<MongoScheduler> getCronSchedules(String cron){
         Query query = sessionFactory.getCurrentSession().getNamedQuery("MongoScheduler.findByCron");
         query.setParameter("cron", cron);
         return query.list();
    }
}
