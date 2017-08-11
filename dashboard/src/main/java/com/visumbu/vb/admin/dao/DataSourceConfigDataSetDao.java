/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.bean.DataSourceConfigBean;
import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.DataSourceConfigDataset;
import com.visumbu.vb.model.DataSourceConfigDatasetFrequency;
import com.visumbu.vb.model.DataSourceConfigDatasetLevel;
import com.visumbu.vb.model.DataSourceConfigDatasetSegment;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author duc-dev-03
 */
@Transactional
@Repository("dataSourceConfigDataSetDao")
public class DataSourceConfigDataSetDao extends BaseDao {

    public List<DataSourceConfigDataset> getDataSourceConfigDataSet(Integer dataSourceId) {
        String queryStr = "SELECT d from DataSourceConfigDataset d where d.dataSourceId.id = :dataSourceId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSourceId", dataSourceId);
        return query.list();
    }

    public List<DataSourceConfigDatasetLevel> getDataSourceConfigLevel(Integer dataSetConfigId) {
        String queryStr = "SELECT d from DataSourceConfigDatasetLevel d where d.dataSetConfigId.id = :dataSetConfigId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSetConfigId", dataSetConfigId);
        return query.list();
    }

    public List<DataSourceConfigDatasetSegment> getDataSourceConfigSegment(Integer dataSetConfigId) {
        String queryStr = "SELECT d from DataSourceConfigDatasetSegment d where d.dataSetConfigId.id = :dataSetConfigId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSetConfigId", dataSetConfigId);
        return query.list();
    }

    public List<DataSourceConfigDatasetFrequency> getDataSourceConfigFrequency(Integer dataSetConfigId) {
        String queryStr = "SELECT d from DataSourceConfigDatasetFrequency d where d.dataSetConfigId.id = :dataSetConfigId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSetConfigId", dataSetConfigId);
        return query.list();
    }

    public void saveOrUpdate(Object object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }


    public List<DataSourceConfigDataset> getDataSetById(Integer dataSetConfigId) {
        String queryStr = "SELECT d from DataSourceConfigDataset d where d.id = :dataSetConfigId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);;
        query.setParameter("dataSetConfigId", dataSetConfigId);
        List<DataSourceConfigDataset> data = query.list();
        return data;
    }

}
