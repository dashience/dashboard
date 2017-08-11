/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.DataSourceConfigDataSetDao;
import com.visumbu.vb.bean.DataSourceConfigBean;
import com.visumbu.vb.model.DataSourceConfigDataset;
import com.visumbu.vb.model.DataSourceConfigDatasetFrequency;
import com.visumbu.vb.model.DataSourceConfigDatasetLevel;
import com.visumbu.vb.model.DataSourceConfigDatasetSegment;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author duc-dev-03
 */
@Service("dataSourceConfigDataSetService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DataSourceConfigDataSetService {

    @Autowired
    private DataSourceConfigDataSetDao dataSourceConfigurationDao;

    public List<DataSourceConfigDataset> getDataSourceConfigDataSet(Integer dataSourceId) {
        return dataSourceConfigurationDao.getDataSourceConfigDataSet(dataSourceId);
    }

    public List<DataSourceConfigDatasetLevel> getDataSourceConfigLevel(Integer dataSetConfigId) {
        return dataSourceConfigurationDao.getDataSourceConfigLevel(dataSetConfigId);
    }

    public List<DataSourceConfigDatasetSegment> getDataSourceConfigSegment(Integer dataSetConfigId) {
        return dataSourceConfigurationDao.getDataSourceConfigSegment(dataSetConfigId);
    }

    public List<DataSourceConfigDatasetFrequency> getDataSourceConfigFrequency(Integer dataSetConfigId) {
        return dataSourceConfigurationDao.getDataSourceConfigFrequency(dataSetConfigId);
    }

    //Levels
    public DataSourceConfigDatasetLevel saveDataSourceConfigLevel(DataSourceConfigBean dataSourceConfig) {
        List<DataSourceConfigDataset> configDataSet = dataSourceConfigurationDao.getDataSetById(dataSourceConfig.getDataSetConfigId());
        System.out.println("**********************************************");
        System.out.println(configDataSet);

        DataSourceConfigDatasetLevel dataSourceConfigDataSetLevel = new DataSourceConfigDatasetLevel();
        dataSourceConfigDataSetLevel.setAlias(dataSourceConfig.getAlias());
        dataSourceConfigDataSetLevel.setMetrics(dataSourceConfig.getMetrics());
        dataSourceConfigDataSetLevel.setOrderBy(dataSourceConfig.getOrderBy());
        dataSourceConfigDataSetLevel.setDataSetConfigId(configDataSet.get(0));
        dataSourceConfigDataSetLevel.setId(dataSourceConfig.getId());
        dataSourceConfigurationDao.saveOrUpdate(dataSourceConfigDataSetLevel);

        return dataSourceConfigDataSetLevel;
    }
    
    
    public DataSourceConfigDatasetLevel deleteDataSourceConfigDataSetLevel(Integer id) {
        DataSourceConfigDatasetLevel dataSourceConfigDatasetLevel = readLevel(id);
        return (DataSourceConfigDatasetLevel) dataSourceConfigurationDao.delete(dataSourceConfigDatasetLevel);
    }

    public DataSourceConfigDatasetLevel readLevel(Integer id) {
        return (DataSourceConfigDatasetLevel) dataSourceConfigurationDao.read(DataSourceConfigDatasetLevel.class, id);
    }
    
    

    //Segments
    public DataSourceConfigBean saveDataSourceConfigSegment(DataSourceConfigBean dataSourceConfig) {
        List<DataSourceConfigDataset> configDataSet = dataSourceConfigurationDao.getDataSetById(dataSourceConfig.getDataSetConfigId());
        System.out.println("**********************************************");
        System.out.println(configDataSet);

        DataSourceConfigDatasetSegment dataSourceConfigDataSetSegment = new DataSourceConfigDatasetSegment();
        dataSourceConfigDataSetSegment.setAlias(dataSourceConfig.getAlias());
        dataSourceConfigDataSetSegment.setMetrics(dataSourceConfig.getMetrics());
        dataSourceConfigDataSetSegment.setOrderBy(dataSourceConfig.getOrderBy());
        dataSourceConfigDataSetSegment.setDataSetConfigId(configDataSet.get(0));
        dataSourceConfigDataSetSegment.setId(dataSourceConfig.getId());
        dataSourceConfigurationDao.saveOrUpdate(dataSourceConfigDataSetSegment);

        return null;
    }
    
    
    public DataSourceConfigDatasetSegment deleteDataSourceConfigDataSetSegment(Integer id) {
        DataSourceConfigDatasetSegment dataSourceConfigDatasetSegment = readSegment(id);
        return (DataSourceConfigDatasetSegment) dataSourceConfigurationDao.delete(dataSourceConfigDatasetSegment);
    }

    public DataSourceConfigDatasetSegment readSegment(Integer id) {
        return (DataSourceConfigDatasetSegment) dataSourceConfigurationDao.read(DataSourceConfigDatasetSegment.class, id);
    }

    //Frequency
    public DataSourceConfigBean saveDataSourceConfigFrequency(DataSourceConfigBean dataSourceConfig) {
        List<DataSourceConfigDataset> configDataSet = dataSourceConfigurationDao.getDataSetById(dataSourceConfig.getDataSetConfigId());
        System.out.println("**********************************************");
        System.out.println(configDataSet);

        DataSourceConfigDatasetFrequency dataSourceConfigDataSetFrequency = new DataSourceConfigDatasetFrequency();
        dataSourceConfigDataSetFrequency.setAlias(dataSourceConfig.getAlias());
        dataSourceConfigDataSetFrequency.setMetrics(dataSourceConfig.getMetrics());
        dataSourceConfigDataSetFrequency.setOrderBy(dataSourceConfig.getOrderBy());
        dataSourceConfigDataSetFrequency.setDataSetConfigId(configDataSet.get(0));
        dataSourceConfigDataSetFrequency.setId(dataSourceConfig.getId());
        dataSourceConfigurationDao.saveOrUpdate(dataSourceConfigDataSetFrequency);

        return null;
    }
    
    
    public DataSourceConfigDatasetFrequency deleteDataSourceConfigDataSetFrequency(Integer id) {
        DataSourceConfigDatasetFrequency dataSourceConfigDatasetFrequency = readFrequency(id);
        return (DataSourceConfigDatasetFrequency) dataSourceConfigurationDao.delete(dataSourceConfigDatasetFrequency);
    }

    public DataSourceConfigDatasetFrequency readFrequency(Integer id) {
        return (DataSourceConfigDatasetFrequency) dataSourceConfigurationDao.read(DataSourceConfigDatasetFrequency.class, id);
    }
    

    //DataSet
    public DataSourceConfigDataset createDataSourceConfigDataSet(DataSourceConfigDataset dataSourceConfigDataset) {
        return (DataSourceConfigDataset) dataSourceConfigurationDao.create(dataSourceConfigDataset);
    }

    public DataSourceConfigDataset updateDataSourceConfigDataSet(DataSourceConfigDataset dataSourceConfigDataset) {
        return (DataSourceConfigDataset) dataSourceConfigurationDao.update(dataSourceConfigDataset);
    }

    public DataSourceConfigDataset deleteDataSourceConfigDataSet(Integer id) {
        DataSourceConfigDataset dataSourceConfigDataset = read(id);
        return (DataSourceConfigDataset) dataSourceConfigurationDao.delete(dataSourceConfigDataset);
    }

    public DataSourceConfigDataset read(Integer id) {
        return (DataSourceConfigDataset) dataSourceConfigurationDao.read(DataSourceConfigDataset.class, id);
    }
}
