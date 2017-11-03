/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.dao.bean.DataSourceBean;
import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.datasource.BaseDataSource;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.VbUser;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("dataSourceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DataSourceService {

    @Autowired
    private UiDao uiDao;

    public List getAllDataSources() {
        return BaseDataSource.getAllDataSources();
    }

    public List getAllDataSets(String dataSourceName) throws IOException, GeneralSecurityException {
        BaseDataSource dataSource = BaseDataSource.getInstance(dataSourceName);
        return dataSource.getDataSets();
    }

    public List getAllDimensions(String dataSourceName, String dataSet) throws IOException, GeneralSecurityException {
        BaseDataSource dataSource = BaseDataSource.getInstance(dataSourceName);
        return dataSource.getDataDimensions();
    }

    public Object getData(String dataSourceName, String dataSet, Map options, ReportPage page) throws IOException, GeneralSecurityException {
        BaseDataSource dataSource = BaseDataSource.getInstance(dataSourceName);
        return dataSource.getData(dataSet, options, page);
    }

    public DataSource saveDataSource(DataSourceBean dataSource) {
        try {
            DataSource dbDataSource = new DataSource();
            BeanUtils.copyProperties(dbDataSource, dataSource);
            uiDao.create(dbDataSource);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UiService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(UiService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public DataSource createDataSourceForJoinDataSet(DataSourceBean dataSource) {
        List<DataSource> joinDataSourceList = uiDao.getJoinDataSource(dataSource.getUserId());
        System.out.println("dataSource" + dataSource.getName());
        DataSource newDataSource = new DataSource();
        if (joinDataSourceList.size() > 0) {
            System.out.println("if update --->");
            DataSource joinDataSource = joinDataSourceList.get(0);
            newDataSource.setId(joinDataSource.getId());
            newDataSource.setName(joinDataSource.getName());
            newDataSource.setDataSourceType(joinDataSource.getDataSourceType());
            newDataSource.setUserId(joinDataSource.getUserId());
            newDataSource.setAgencyId(joinDataSource.getAgencyId());
            uiDao.saveOrUpdate(newDataSource);
        } else {
            System.out.println("else create ---> ");
            newDataSource.setName(dataSource.getName());
            newDataSource.setDataSourceType("join");
            newDataSource.setUserId(dataSource.getUserId());
            newDataSource.setAgencyId(dataSource.getAgencyId());
            uiDao.saveOrUpdate(newDataSource);
        }
        return newDataSource;
    }

    public List<DataSource> getDataSourceByUser(VbUser user) {
        return uiDao.getDataSourceByUser(user);
    }

    public DataSource update(DataSource dataSource) {
        return (DataSource) uiDao.update(dataSource);
    }

    public DataSource deleteDataSource(Integer id) {
        return (DataSource) uiDao.deleteDataSource(id);
    }

}
