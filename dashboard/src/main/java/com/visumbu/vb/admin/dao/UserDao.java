/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.Permission;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Netphenix
 */
@Transactional
@Repository("userDao")
public class UserDao extends BaseDao {

    public List<VbUser> read() {
        Query query = sessionFactory.getCurrentSession().createQuery("from VbUser where status is null or status != 'Deleted'");
        return query.list();
    }

    public List<VbUser> findByUserName(String username) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("VbUser.findByUserName");
        query.setParameter("userName", username);
        return query.list();
    }

    public VbUser createNewUser(String userId, String userName, String fullName) {
        VbUser user = new VbUser();
        user.setUserRefId(userId);
        user.setUserName(userName);
        create(user);
        System.out.println("Created User " + user);
        return user;
    }

    public void initUser(VbUser user) {
        List<Dashboard> dashboards = initDashboardItems(user);
    }

    private List<Dashboard> initDashboardItems(VbUser user) {
        List<Dashboard> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("Dashboard.findByUserId");
        query.setParameter("userId", 1);
        List<Dashboard> dashboards = query.list();
        for (Iterator<Dashboard> iterator = dashboards.iterator(); iterator.hasNext();) {
            Dashboard dashboard = iterator.next();
            Dashboard newObject = new Dashboard();
            try {
                BeanUtils.copyProperties(newObject, dashboard);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            newObject.setId(null);
            newObject.setUserId(user);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initDashboardTabs(user, dashboard, newObject);
            returnList.add(newObject);
        }
        return returnList;
    }

    private List<DashboardTabs> initDashboardTabs(VbUser user, Dashboard oldDashboard, Dashboard newDashboard) {
        List<DashboardTabs> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("DashboardTabs.findByDashboard");
        query.setParameter("dashboardId", oldDashboard);
        List<DashboardTabs> dashboardTabs = query.list();
        for (Iterator<DashboardTabs> iterator = dashboardTabs.iterator(); iterator.hasNext();) {
            DashboardTabs dashboardTab = iterator.next();
            DashboardTabs newObject = new DashboardTabs();
            try {
                BeanUtils.copyProperties(newObject, dashboardTab);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            newObject.setId(null);
            newObject.setDashboardId(newDashboard);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initDashboardTabWidget(user, dashboardTab, newObject);
            returnList.add(newObject);
        }
        return returnList;
    }

    private List<TabWidget> initDashboardTabWidget(VbUser user, DashboardTabs oldDashboardTab, DashboardTabs newDashboardTab) {
        List<TabWidget> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("TabWidget.findByTab");
        query.setParameter("tab", oldDashboardTab);
        List<TabWidget> widgets = query.list();
        for (Iterator<TabWidget> iterator = widgets.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next();
            TabWidget newObject = new TabWidget();
            try {
                BeanUtils.copyProperties(newObject, tabWidget);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            newObject.setId(null);
            newObject.setTabId(newDashboardTab);
            newObject.setCreatedTime(new Date());
            create(newObject);
            initWidgetColumns(user, tabWidget, newObject);
            returnList.add(newObject);
        }

        return returnList;
    }

    private List<WidgetColumn> initWidgetColumns(VbUser user, TabWidget oldWidget, TabWidget newWidget) {
        List<WidgetColumn> returnList = new ArrayList<>();
        Query query = sessionFactory.getCurrentSession().getNamedQuery("WidgetColumn.findByWidget");
        query.setParameter("widget", oldWidget);
        List<WidgetColumn> widgetColumns = query.list();
        for (Iterator<WidgetColumn> iterator = widgetColumns.iterator(); iterator.hasNext();) {
            WidgetColumn widgetColumn = iterator.next();
            WidgetColumn newObject = new WidgetColumn();
            try {
                BeanUtils.copyProperties(newObject, widgetColumn);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            newObject.setId(null);
            newObject.setWidgetId(newWidget);
            create(newObject);
            returnList.add(newObject);
        }

        return returnList;
    }

    public Account deleteAccount(Integer accountId) {
        String queryPermission = "delete Permission d where d.accountId.id = :accountId";
        Query querySession = sessionFactory.getCurrentSession().createQuery(queryPermission);
        querySession.setParameter("accountId", accountId);
        querySession.executeUpdate();

        String queryStr = "delete from Property d where d.accountId.id = :accountId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("accountId", accountId);
        query.executeUpdate();

        String queryString = "delete Account d where d.id = :accountId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("accountId", accountId);
        querySess.executeUpdate();
        return null;
    }

    public Property deleteProperty(Integer propertyId) {
        String queryStr = "delete from Permission d where d.propertyId.id = :propertyId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("propertyId", propertyId);
        query.executeUpdate();

        String queryString = "delete Property d where d.id = :propertyId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("propertyId", propertyId);
        querySess.executeUpdate();
        return null;
    }

    public Permission deletePermission(Integer permissionId) {
        String queryString = "delete Permission d where d.id = :permissionId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("permissionId", permissionId);
        querySess.executeUpdate();
        return null;
    }

}
