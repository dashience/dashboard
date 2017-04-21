/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.UserAccount;
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
//        Query query = sessionFactory.getCurrentSession().createQuery("from VbUser where (agencyId.status is null or agencyId.status != 'Deleted') and (status is null or status != 'Deleted')");
        Query query = sessionFactory.getCurrentSession().createQuery("from VbUser where status is null or status != 'Deleted'");
        return query.list();
    }

    public List<VbUser> findByUserName(String username) {
        Query query = sessionFactory.getCurrentSession().createQuery("from VbUser where agencyId is null and userName = :userName");//.getNamedQuery("VbUser.findByUserName");
        query.setParameter("userName", username);
        List<VbUser> users = query.list();
        if (!users.isEmpty()) {
            return users;
        }

        query = sessionFactory.getCurrentSession().createQuery("from VbUser where (agencyId is null or agencyId.status is null or agencyId.status != 'Deleted') and (status is null or status != 'Deleted') and userName = :userName");//.getNamedQuery("VbUser.findByUserName");
        query.setParameter("userName", username);
        return query.list();
    }

    public List<Agency> findUserNameByUser(String userName) {
        Query query = sessionFactory.getCurrentSession().createQuery("from VbUser where userName = :userName");//.getNamedQuery("VbUser.findByUserName");
        query.setParameter("userName", userName);
        return query.list();
    }

    public List<Account> findByAccountName(String accountName) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Account where accountName = :accountName");//.getNamedQuery("VbUser.findByUserName");
        query.setParameter("accountName", accountName);
        return query.list();
    }

    public List<Agency> findByAgencyName(String agencyName) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Agency where agencyName = :agencyName");//.getNamedQuery("VbUser.findByUserName");
        query.setParameter("agencyName", agencyName);
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
        String queryUserAccount = "delete UserAccount d where d.accountId.id = :accountId";
        Query querySession = sessionFactory.getCurrentSession().createQuery(queryUserAccount);
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
        String queryString = "delete Property d where d.id = :propertyId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("propertyId", propertyId);
        querySess.executeUpdate();
        return null;
    }

    public UserAccount deleteUserAccount(Integer userAccountId) {
        String queryString = "delete UserAccount d where d.id = :userAccountId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("userAccountId", userAccountId);
        querySess.executeUpdate();
        return null;
    }

    public List<Property> getPropertyByAccountId(Integer accountId) {
        String queryStr = "select d from Property d where d.accountId.id = :accountId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("accountId", accountId);
        return query.list();
    }

    public List getUserAccountById(Integer accountId) {
        String queryStr = "select d from UserAccount d where (d.userId.status is null or d.userId.status != 'Deleted') and d.accountId.id = :accountId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("accountId", accountId);
        return query.list();
    }

    public List getActiveAgency() {
        String queryStr = "select d from Agency d where d.status is null or d.status != 'Deleted'";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("accountId", accountId);
        return query.list();
    }

    public Agency deleteAgency(Integer agencyId) {
        String queryStr = "update Agency d set status = 'Deleted' where d.id = :agencyId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyId", agencyId);
        query.executeUpdate();
//        String queryStr = "delete from AgencyLicence d where d.agencyId.id = :agencyId";
//        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("agencyId", agencyId);
//        query.executeUpdate();
//
//        String queryString = "delete Agency d where d.id = :agencyId";
//        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
//        querySess.setParameter("agencyId", agencyId);
//        querySess.executeUpdate();
        return null;
    }

    public List getAgencyLicenceById(Integer agencyId) {
        String queryStr = "select d from AgencyLicence d where (d.agencyId.status is null or d.agencyId.status != 'Deleted') and d.agencyId.id = :agencyId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyId", agencyId);
        return query.list();
    }

    public Agency deleteAgencyLicence(Integer agencyLicenceId) {
        String queryString = "delete AgencyLicence d where d.id = :agencyLicenceId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("agencyLicenceId", agencyLicenceId);
        querySess.executeUpdate();
        return null;
    }

//    public List getAgencyUserById(Integer agencyUserId) {
//        String queryStr = "select d from VbUser d where d.agencyId.id = :agencyUserId";
//        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("agencyUserId", agencyUserId);
//        return query.list();
//    }
    public List getAgencyUserById(Integer agencyUserId) {
        String queryStr = "select d from VbUser d where (d.status is null or d.status != 'Deleted') and (d.agencyId.status is null or d.agencyId.status != 'Deleted') and d.agencyId.id = :agencyUserId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyUserId", agencyUserId);
        return query.list();
    }

    public List<Account> getAccount() {
        String queryStr = "select a from Account a where (a.agencyId.status is null or a.agencyId.status != 'Deleted')";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("agencyId", agency.getId());
        return query.list();
    }

    public String getAccountName(Integer id) {
        String queryStr = "select a.accountName FROM Account a Where a.id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("id", id);
        String accountName = (String) query.list().get(0);
        return accountName;
    }

//    public String getProductName(Integer id) {
//        String queryStr = "select a.productName FROM Product a Where a.id = :id";
//        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("id", id);
//        String productName = (String) query.list().get(0);
//        return productName;
//    }

    public List<Account> getAccountByAgency(Agency agency) {
        String queryStr = "select a from Account a where (a.agencyId.status is null or a.agencyId.status != 'Deleted') and a.agencyId.id = :agencyId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyId", agency.getId());
        return query.list();
    }

    public List<AgencyProduct> getAgencyProductById(Integer agencyProductId) {
        String queryStr = "select a from AgencyProduct a where (a.status is null or a.status != 'Deleted') and a.agencyId.id = :agencyId order by productOrder";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyId", agencyProductId);
        return query.list();
    }

    public AgencyProduct getAgencyProductOrderId(Integer agencyProductId) {
        AgencyProduct agencyProduct = (AgencyProduct) sessionFactory.getCurrentSession().get(AgencyProduct.class, agencyProductId);
        return agencyProduct;
    }

    public String productUpdateOrder(Integer agencyProductId, String productOrder) {
        System.out.println(productOrder);
        String[] productOrderArray = productOrder.split(",");
        for (int i = 0; i < productOrderArray.length; i++) {
            Integer productId = Integer.parseInt(productOrderArray[i]);
            AgencyProduct agencyProduct = getAgencyProductOrderId(productId);
            agencyProduct.setProductOrder(i);
            update(agencyProduct);
        }
        return null;
    }

    public AgencyProduct deleteAgencyProduct(Integer agencyProductId) {
        String queryStr = "update AgencyProduct d set status = 'Deleted' where d.id = :agencyProductId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyProductId", agencyProductId);
        query.executeUpdate();
        return null;
    }

}
