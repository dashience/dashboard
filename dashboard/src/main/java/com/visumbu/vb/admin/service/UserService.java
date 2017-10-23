/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.DealerDao;
import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.bean.AgencyBean;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.AgencyLicence;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.AgencyProperty;
import com.visumbu.vb.model.AgencySettings;
import com.visumbu.vb.model.Currency;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.VbUser;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DealerDao dealerDao;

    public VbUser create(VbUser teUser) {
        List<VbUser> users = userDao.findByUserName(teUser.getUserName());
        if (users.isEmpty()) {
            teUser.setStatus("Active");
            teUser.setCreatedTime(new Date());
            teUser.setTheme("default");
            return (VbUser) userDao.create(teUser);
        }
        return null;
    }

    public VbUser read(Integer id) {
        return (VbUser) userDao.read(VbUser.class, id);
    }

    public List<VbUser> read() {
        List<VbUser> users = userDao.read();
        return users;
    }

    public VbUser update(VbUser teUser) {
        return (VbUser) userDao.update(teUser);
    }

    public VbUser delete(Integer id) {
        VbUser teUser = read(id);
        teUser.setStatus("Deleted");
        return update(teUser);
    }

    public VbUser delete(VbUser teUser) {
        return (VbUser) userDao.delete(teUser);
    }

    public VbUser findByUsername(String username) {
        List<VbUser> vbUsers = userDao.findByUserName(username);
        if (!vbUsers.isEmpty()) {
            return vbUsers.get(0);
        }
        return null;
    }

    public Agency getAgencyByDomain(String dashiencePath) {
        return userDao.findAgencyByDashiencePath(dashiencePath);
    }

    public LoginUserBean authenicate(LoginUserBean userBean) {
        String dashiencePath = userBean.getDashiencePath();
        VbUser user = null;
        if (dashiencePath == null || dashiencePath.equalsIgnoreCase("")) {
            user = userDao.findAdminUserByName(userBean.getUsername());
            userBean.setIsAdmin("true");
        } else {
            String userName = userBean.getUsername();
            Agency agency = userDao.findAgencyByDashiencePath(dashiencePath);
            user = userDao.findUser(dashiencePath, userName,  agency);
        }
       
        LoginUserBean loginUserBean = null;
        if (user != null) {
            if (user.getPassword().equals(userBean.getPassword())
                    && user.getUserName().equals(userBean.getUsername())) {
                user.setFailedLoginCount(0);
                user.setLastLoginTime(new Date());
                loginUserBean = toLoginUserBean(user);
                System.out.println(loginUserBean);
                loginUserBean.setId(user.getId());
                loginUserBean.setAuthenticated(Boolean.TRUE);
            } else {
                if (user != null) {
                    user.setFailedLoginCount(user.getFailedLoginCount() + 1);
                    loginUserBean = toLoginUserBean(user);
                }
                loginUserBean.setAuthenticated(Boolean.FALSE);
                loginUserBean.setErrorMessage("Invalid Username or Password");
            }
        }
        if (loginUserBean == null) {
            loginUserBean = new LoginUserBean();
            loginUserBean.setErrorMessage("Invalid Login");
        }
        return loginUserBean;
    }

    private AgencyBean toAgencyBean(Agency agency) {
        if (agency == null) {
            return null;
        }
        AgencyBean agencyBean = new AgencyBean();
        agencyBean.setAgencyName(agency.getAgencyName());
        agencyBean.setDescription(agency.getDescription());
        agencyBean.setEmail(agency.getEmail());
        agencyBean.setStatus(agency.getStatus());
        agencyBean.setId(agency.getId());
        return agencyBean;
    }

    private LoginUserBean toLoginUserBean(VbUser teUser) {
        LoginUserBean userBean = new LoginUserBean();
        userBean.setUsername(teUser.getUserName());
        System.out.println("Agency Id " + teUser.getAgencyId());
        userBean.setAgencyId(toAgencyBean(teUser.getAgencyId()));
        System.out.println("AGENCY BEAN " + userBean.getAgencyId());
//        userBean.setPassword(teUser.getPassword());
        userBean.setFailLoginCount(teUser.getFailedLoginCount());
        userBean.setIsAdmin(teUser.getIsAdmin() != null && teUser.getIsAdmin() == true ? "admin" : "");
        return userBean;
    }

    public List<Dealer> getAllowedDealerByMapId(String delaerRefId) {
        return dealerDao.getAllowedDealerByMapId(delaerRefId);
    }

    public List<Dealer> getAllowedDealerByGroupId(String groupId) {
        return dealerDao.getAllowedDealerByGroupId(groupId);
    }

    public List<Dealer> getAllowedDealerByGroupName(String groupName) {
        return dealerDao.getAllowedDealerByGroupName(groupName);
    }

    public List<Dealer> getAllowedDealerByOemRegionId(String oemRegionId) {
        return dealerDao.getAllowedDealerByOemRegionId(oemRegionId);
    }

    public List<Dealer> getSampleDealers() {
        return dealerDao.getSampleDealers();
    }

    public VbUser createNewUser(SecurityAuthBean authData) {
        String userId = authData.getUserId();
        String userName = authData.getUserName();
        List<VbUser> users = userDao.findByUserName(userName);
        VbUser user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        if (user == null) {
            System.out.println("Creating user " + userName);
            user = userDao.createNewUser(userId, userName, authData.getFullName());
            userDao.initUser(user);
        }
        return user;
    }

    public HashMap createAccount(Account account) {
        String returnMsg = null;
        Boolean isSuccess = null;
        HashMap returnMap = new HashMap();
        List findUserAccount = userDao.findByAccountName(account.getAccountName());
        if (findUserAccount.isEmpty()) {
            userDao.create(account);
            returnMsg = "Success";
            isSuccess = true;

        } else {
            returnMsg = "User Already Exist";
            isSuccess = false;
        }
        returnMap.put("message", returnMsg);
        returnMap.put("status", isSuccess);
        return returnMap;
//        return (Account) userDao.create(account);
    }

    public Account updateAccount(Account account) {
        return (Account) userDao.update(account);
    }

    public List<Account> getAccount() {
//        List<Account> account = userDao.read(Account.class);
        return userDao.getAccount();
    }

    public Account getAccountId(Integer id) {
        return (Account) userDao.read(Account.class, id);
    }

    public Account deleteAccount(Integer accountId) {
        ///Account account = getAccountId(accountId);
        return userDao.deleteAccount(accountId);
    }

    public Property createProperty(Property property) {
        return (Property) userDao.create(property);
    }

    public Property updateProperty(Property property) {
        return (Property) userDao.update(property);
    }

    public List<Property> getProperty() {
        List<Property> property = userDao.read(Property.class);
        return property;
    }

    public List<Property> getPropertyByAccountId(Integer accountId) {
        return userDao.getPropertyByAccountId(accountId);
    }

    public Property getPropertyId(Integer id) {
        return (Property) userDao.read(Property.class, id);
    }

    public Property deleteProperty(Integer propertyId) {
        return userDao.deleteProperty(propertyId);
    }

    public UserAccount createUserAccount(UserAccount userAccount) {
        return (UserAccount) userDao.create(userAccount);
    }

    public UserAccount updateUserAccount(UserAccount userAccount) {
        return (UserAccount) userDao.update(userAccount);
    }

    public List<UserAccount> getUserAccount() {
        List<UserAccount> userAccount = userDao.read(UserAccount.class);
        return userAccount;
    }

    public UserAccount getUserAccountId(Integer id) {
        return (UserAccount) userDao.read(UserAccount.class, id);
    }

    public UserAccount deleteUserAccount(Integer userAccountId) {
        return userDao.deleteUserAccount(userAccountId);
    }

    public List getUserAccountById(Integer accountId) {
        return userDao.getUserAccountById(accountId);
    }

    public HashMap createAgency(Agency agency) {
        String returnMsg = null;
        Boolean isSuccess = null;
        HashMap returnMap = new HashMap();
        List findAgencyName = userDao.findByAgencyName(agency.getAgencyName());
        if (findAgencyName.isEmpty()) {
            userDao.create(agency);
            returnMsg = "Success";
            isSuccess = true;

        } else {
            returnMsg = "User Already Exist";
            isSuccess = false;
        }
        returnMap.put("message", returnMsg);
        returnMap.put("status", isSuccess);
        return returnMap;
        //return (Agency) userDao.create(agency);
    }

    public Agency updateAgency(Agency agency) {
        return (Agency) userDao.update(agency);
    }

    public List<Agency> getAgency() {
//        List<Agency> agency = userDao.read(Agency.class);
        return userDao.getActiveAgency();
    }

    public Agency deleteAgency(Integer agencyId) {
        return userDao.deleteAgency(agencyId);
    }

    public AgencyLicence createAgencyLicence(AgencyLicence agencyLicence) {
        return (AgencyLicence) userDao.create(agencyLicence);
    }

    public AgencyLicence updateAgencyLicence(AgencyLicence agencyLicence) {
        return (AgencyLicence) userDao.update(agencyLicence);
    }

    public List<AgencyLicence> getAgencyLicence() {
        List<AgencyLicence> agencyLicence = userDao.read(AgencyLicence.class);
        return agencyLicence;
    }

    public List getAgencyLicenceById(Integer agencyId) {
        return userDao.getAgencyLicenceById(agencyId);
    }

    public Agency deleteAgencyLicence(Integer agencyLicenceId) {
        return userDao.deleteAgencyLicence(agencyLicenceId);
    }

    public List getAgencyUserById(Integer agencyUserId) {
        return userDao.getAgencyUserById(agencyUserId);
    }

    public List<Account> getAccount(Agency agency) {
        return userDao.getAccountByAgency(agency);
    }

    public String getAccountName(Integer id) {
        return userDao.getAccountName(id);
    }

//    public String getProductName(Integer id) {
//        return userDao.getProductName(id);
//    }
    public AgencyProduct createAgencyProduct(AgencyProduct agencyProduct) {
        System.out.println("==================================>");
        System.out.println(agencyProduct);
        return (AgencyProduct) userDao.create(agencyProduct);
    }

    public AgencyProduct updateAgencyProduct(AgencyProduct agencyProduct) {
        return (AgencyProduct) userDao.update(agencyProduct);
    }

    public List<AgencyProduct> getAgencyProductById(Integer agencyProductId) {
        return userDao.getAgencyProductById(agencyProductId);
    }

    public String productUpdateOrder(Integer agencyProductId, String productOrder) {
        return userDao.productUpdateOrder(agencyProductId, productOrder);
    }

    public AgencyProduct deleteAgencyProductId(Integer agencyProductId) {
        return userDao.deleteAgencyProduct(agencyProductId);
    }

    public AgencySettings createAgencySettings(AgencySettings agencySettings) {
        System.out.println(agencySettings.getAgencyId() + "....." + agencySettings.getCurrencyId() + "....." + agencySettings.getTimeZoneId());
        return (AgencySettings) userDao.create(agencySettings);
    }

    public AgencySettings updateAgencySettings(AgencySettings agencysettings) {
        return (AgencySettings) userDao.update(agencysettings);
    }

    public AgencySettings getAgencySettingsById(Integer agencyId) {
        return userDao.getAgencySettingsById(agencyId);
    }

    public Currency getCurrencyById(Integer id) {
        return (Currency) userDao.read(Property.class, id);
    }

    public TimeZone getTimezoneById(Integer id) {
        return (TimeZone) userDao.read(Property.class, id);
    }

    public List<AgencyProperty> getAgencyPropertyById(Integer agencyId) {
        return userDao.getAgencyPropertyById(agencyId);
    }

    public AgencyProperty createAgencyProperty(AgencyProperty agencyProperty) {
        return (AgencyProperty) userDao.create(agencyProperty);
    }

    public AgencyProperty updateAgencyProperty(AgencyProperty agencyProperty) {
        return (AgencyProperty) userDao.update(agencyProperty);
    }

    public AgencyProperty deleteAgencyPropertyId(Integer agencyPropertyId) {
        return userDao.deleteAgencyPropertyId(agencyPropertyId);
    }
}
