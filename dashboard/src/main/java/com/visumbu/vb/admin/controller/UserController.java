/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.Permission;
import com.visumbu.vb.bean.UrlBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthRoleBean;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AccountUser;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.AgencyLicence;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.AgencySettings;
import com.visumbu.vb.model.Currency;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.VbUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.OrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author jp
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UiService uiService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    VbUser create(HttpServletRequest request, HttpServletResponse response, @RequestBody VbUser teUser) {
        return userService.create(teUser);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    VbUser update(HttpServletRequest request, HttpServletResponse response, @RequestBody VbUser teUser) {
        return userService.update(teUser);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List read(HttpServletRequest request, HttpServletResponse response) {
        return userService.read();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    VbUser read(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return userService.read(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    VbUser delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return userService.delete(id);
    }

//    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json")
//    public @ResponseBody
//    Map login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginUserBean loginUserBean) {
//        SecurityAuthBean authData = userService.getPermissions(loginUserBean);
//        //LoginUserBean userBean = userService.authenicate(loginUserBean);
//        HttpSession session = request.getSession();
//
//        session.setAttribute("isAuthenticated", authData != null);
//        if (authData == null) {
//            Map returnMap = new HashMap();
//            returnMap.put("authData", null);
//            returnMap.put("errorMessage", "Login Failed");
//            return returnMap;
//        }
//        VbUser user = userService.createNewUser(authData);
//        session.setAttribute("userId", user.getId());
//        session.setAttribute("username", authData.getUserName());
//        session.setAttribute("accessToken", authData.getAccessToken());
//        session.setAttribute("permission", authData.getPermission());
//        session.setAttribute("userGuid", authData.getUserGuid());
//        Map returnMap = new HashMap();
//        returnMap.put("authData", authData);
//        returnMap.put("dealers", new ArrayList<>());
//        return returnMap;
//    }
    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    LoginUserBean login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginUserBean loginUserBean) {
        LoginUserBean userBean = userService.authenicate(loginUserBean);
        System.out.println("----------------------------------------------->");
        System.out.println(userBean);
        HttpSession session = request.getSession();
        session.setAttribute("isAuthenticated", userBean.getAuthenticated());
        session.setAttribute("username", userBean.getUsername());
        session.setAttribute("agencyId", userBean.getAgencyId());
        if (userBean != null && userBean.getUsername() != null && userBean.getAuthenticated()) {
            VbUser user = userService.findByUsername(userBean.getUsername());
            userBean.setPermission(VbUtils.getPermissions(user, uiService.getUserPermissionById(user.getId())));
            session.setAttribute("permission", userBean.getPermission());
        }
        return userBean;
    }

    @RequestMapping(value = "getPermission", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    LoginUserBean getPermission(HttpServletRequest request, HttpServletResponse response) {
        String user = getUser(request);
        LoginUserBean loginUserBean = new LoginUserBean();
        loginUserBean.setUsername(user);
        loginUserBean.setPermission((Permission) request.getSession().getAttribute("permission"));
        return loginUserBean;
    }

    @RequestMapping(value = "sampleDealers", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Dealer> sampleDealers(HttpServletRequest request, HttpServletResponse response) {
        return userService.getSampleDealers();
    }

    private List<Dealer> getDealerBySecuityBean(SecurityAuthBean authData) {
        List<Dealer> returnList = new ArrayList<>();
        List<SecurityAuthRoleBean> roles = authData.getRoles();
        for (Iterator<SecurityAuthRoleBean> iterator = roles.iterator(); iterator.hasNext();) {
            SecurityAuthRoleBean role = iterator.next();
            System.out.println("DEALER ID " + role.getDealer().getId());
            if (role.getDealer() != null && !role.getDealer().getId().isEmpty() && !role.getDealer().getId().equalsIgnoreCase("0")) {
                System.out.println("DEALER ID " + role.getDealer().getId());
                returnList.addAll(userService.getAllowedDealerByMapId(role.getDealer().getId()));
            }
            if (role.getGroup() != null && !role.getGroup().getId().isEmpty() && !role.getGroup().getId().equalsIgnoreCase("0")) {
                System.out.println("GROUP ID " + role.getGroup().getId());
                List<Dealer> dealers = userService.getAllowedDealerByGroupId(role.getGroup().getId());
                if (dealers == null || dealers.isEmpty()) {
                    dealers = userService.getAllowedDealerByGroupName(role.getGroup().getName());
                }
                returnList.addAll(dealers);
            }
            if (role.getOem() != null && role.getOem().getRegion() != null
                    && role.getOem().getRegion().getId() != null && !role.getOem().getRegion().getId().isEmpty()
                    && !role.getOem().getRegion().getId().equalsIgnoreCase("0")) {
                System.out.println("OEM REGION ID " + role.getOem().getRegion().getId());
                returnList.addAll(userService.getAllowedDealerByOemRegionId(role.getOem().getRegion().getId()));
            }
        }
        //LoginUserBean userBean = userService.authenicate(loginUserBean);
        return returnList;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("../../login.html");
    }

    @RequestMapping(value = "datasets", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getDataSets(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map returnMap = new LinkedHashMap();
        returnMap.put("Overall", getOverallDataSets());
        returnMap.put("Paid Search", getPaidDataSets());
        returnMap.put("Display", getDisplayDataSets());
        returnMap.put("Dynamic Display", getDynamicDisplayDataSets());
        returnMap.put("Video", getVideoDataSets());
        returnMap.put("SEO", getSeoDataSets());
        returnMap.put("Paid Social", getPaidSocialDataSets());
        returnMap.put("Social Impact", getSocialImpactDataSets());
        returnMap.put("Reputation Management", getReviewPushDataSets());
        return returnMap;
    }

    private List<UrlBean> getOverallDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/overall/overallPerformance/summary/day/0;Summary",
            "../api/admin/overall/overallPerformance/detailed/week/12;12 Weeks Summary",
            "../api/admin/overall/overallPerformance/detailed/day/0;Summary By Source",
            "../api/admin/overall/inventory;Inventory",
            "../api/admin/overall/totalNoOfCalls;Total Calls",
            "../api/admin/overall/totalNoOfSales;Total Sales",
            "../api/admin/overall/totalBudget;Total Budget",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;
    }

    private List<UrlBean> getPaidDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/paid/accountPerformance;Account Performance",
            "../api/admin/paid/hourOfDayClickImpressions;Hour Of Day Click Impressions",
            "../api/admin/paid/dayOfWeekClickImpressions;Day Of Week Click Impressions",
            "../api/admin/paid/clicksImpressionsGraph;Clicks Impressions Graph",
            "../api/admin/paid/campaignPerformance;Campaign Performance",
            "../api/admin/paid/geoPerformance;Geo Performance",
            "../api/admin/paid/deviceConversion;Device Conversion",
            "../api/admin/paid/campaignDevice;Campaign Device",
            "../api/admin/paid/accountDevice;Account Device",
            "../api/admin/paid/adGroups;AdGroups",
            "../api/admin/paid/adPerformance;Ad Performance",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;
    }

    private List<UrlBean> getDynamicDisplayDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/dynamicDisplay/overallPerformance;Overall Performance",
            "../api/admin/dynamicDisplay/accountPerformance12Weeks;Account Performance 12 Weeks",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;
    }

    private List<UrlBean> getDisplayDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/display/geoPerformance;Geo Performance",
            "../api/admin/display/campaignDevice;Campaign Device",
            "../api/admin/display/accountDevice;Account Device",
            "../api/admin/display/ad;Ad",
            "../api/admin/display/campaignPerformance;Campaign Performance",
            "../api/admin/display/accountPerformance12Weeks;Account Performance 12 Weeks",
            "../api/admin/display/accountPerformance;Account Performance",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;

    }

    private List<UrlBean> getVideoDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/video/campaignDevicePerformance;Campaign Device Performance",
            "../api/admin/video/accountDevice;Account Device",
            "../api/admin/video/adPerformance;Ad Performance",
            "../api/admin/video/campaignPerformance;Campaign Performance",
            "../api/admin/video/accountPerformanceDayOfWeek;Account Performance Day Of Week",
            "../api/admin/video/accountPerformance12Weeks;Account Performance 12 Weeks",
            "../api/admin/video/accountPerformance;Account Performance",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;

    }

    private List<UrlBean> getSeoDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/seo/accountPerformance;Account Performance",
            "../api/admin/seo/accountPerformance12Weeks;Account Performance 12 Weeks",
            "../api/admin/seo/accountPerformanceDayOfWeek;Account Performance Day Of Week",
            "../api/admin/seo/accountPerformanceTop10Page;Account Performance Top 10 Page",
            "../api/admin/seo/accountDevicePerformance;Account Device Performance",
            "../api/admin/seo/accountGeoPerformance;Account Geo Performance",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;
    }

    private List<UrlBean> getPaidSocialDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/paidSocial/accountPerformance;Account Performance",
            "../api/admin/paidSocial/last12WeeksPerformance;Last 12 Weeks Performance",
            "../api/admin/paidSocial/genderPerformance;Gender Performance",
            "../api/admin/paidSocial/agePerformance;Age Performance",
            "../api/admin/paidSocial/devicePerformance;Device Performance",
            "../api/admin/paidSocial/adPerformance;Ad Performance",
            "../api/admin/paidSocial/campaignPerformance;Campaign Performance",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;

    }

    private List<UrlBean> getSocialImpactDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/socialImpact/postPerformance;Post Performance",
            "../api/admin/socialImpact/postPerformanceByType;Post Performance By Type",
            "../api/admin/socialImpact/postSummary;Post Performance Summary",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;

    }

    private List<UrlBean> getReviewPushDataSets() {
        List<UrlBean> returnList = new ArrayList<>();
        String[] urlList = {
            "../api/admin/reviewPush/reviews;Reviews",
            "../api/admin/reviewPush/ratingSummary;Summary",
            "../api/admin/reviewPush/bySources;By Source",};

        for (int i = 0; i < urlList.length; i++) {
            String urlStr = urlList[i];
            String[] url = urlStr.split(";");
            returnList.add(new UrlBean(url[0], url[1]));
        }
        return returnList;
    }

    @RequestMapping(value = "account", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Object createAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody Account account) {
        if (account.getAgencyId() == null) {
            VbUser loggedInUser = userService.findByUsername(getUser(request));
            account.setAgencyId(loggedInUser.getAgencyId());
        }
        return userService.createAccount(account);
    }

    @RequestMapping(value = "account", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Account updateAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody Account account) {
        return userService.updateAccount(account);
    }

    @RequestMapping(value = "account", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Account> getAccount(HttpServletRequest request, HttpServletResponse response) {
        VbUser loggedInUser = userService.findByUsername(getUser(request));
        if (loggedInUser.getAgencyId() == null) {
            return userService.getAccount();
        }
        return userService.getAccount(loggedInUser.getAgencyId());
    }

    @RequestMapping(value = "account/{accountId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Account deleteAccount(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer accountId) {
        return userService.deleteAccount(accountId);
    }

    @RequestMapping(value = "property", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Property createProperty(HttpServletRequest request, HttpServletResponse response, @RequestBody Property property) {
        return userService.createProperty(property);
    }

    @RequestMapping(value = "property", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Property updateProperty(HttpServletRequest request, HttpServletResponse response, @RequestBody Property property) {
        return userService.updateProperty(property);
    }

    @RequestMapping(value = "property", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getProperty(HttpServletRequest request, HttpServletResponse response) {
        return userService.getProperty();
    }

    @RequestMapping(value = "property/{accountId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getPropertyByAccountId(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer accountId) {
        return userService.getPropertyByAccountId(accountId);
    }

    @RequestMapping(value = "property/{propertyId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Property deleteProperty(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer propertyId) {
        return userService.deleteProperty(propertyId);
    }

    @RequestMapping(value = "userAccount", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    UserAccount createUserAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody UserAccount userAccount) {
        return userService.createUserAccount(userAccount);
    }

    @RequestMapping(value = "userAccount", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    UserAccount updateUserAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody UserAccount userAccount) {
        return userService.updateUserAccount(userAccount);
    }

    @RequestMapping(value = "userAccount", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getUserAccount(HttpServletRequest request, HttpServletResponse response) {
        return userService.getUserAccount();
    }

    @RequestMapping(value = "userAccount/{accountId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getUserAccountById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer accountId) {
        return userService.getUserAccountById(accountId);
    }

    @RequestMapping(value = "userAccount/{userAccountId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    UserAccount deleteUserAccount(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer userAccountId) {
        return userService.deleteUserAccount(userAccountId);
    }

    @RequestMapping(value = "agency", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Object createAgency(HttpServletRequest request, HttpServletResponse response, @RequestBody Agency agency) {
       
        return userService.createAgency(agency);
    }

    @RequestMapping(value = "agency", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Agency updateAgency(HttpServletRequest request, HttpServletResponse response, @RequestBody Agency agency) {
        return userService.updateAgency(agency);
    }

    @RequestMapping(value = "agency", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAgency(HttpServletRequest request, HttpServletResponse response) {
        return userService.getAgency();
    }

    @RequestMapping(value = "agency/{agencyId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Agency deleteAgency(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyId) {
        return userService.deleteAgency(agencyId);
    }

    @RequestMapping(value = "agencyLicence", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    AgencyLicence createAgencyLicence(HttpServletRequest request, HttpServletResponse response, @RequestBody AgencyLicence agencyLicence) {
        return userService.createAgencyLicence(agencyLicence);
    }

    @RequestMapping(value = "agencyLicence", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    AgencyLicence updateAgencyLicence(HttpServletRequest request, HttpServletResponse response, @RequestBody AgencyLicence agencyLicence) {
        return userService.updateAgencyLicence(agencyLicence);
    }

    @RequestMapping(value = "agencyLicence", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAgencyLicence(HttpServletRequest request, HttpServletResponse response) {
        return userService.getAgencyLicence();
    }

    @RequestMapping(value = "agencyLicence/{agencyId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAgencyLicenceById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyId) {
        return userService.getAgencyLicenceById(agencyId);
    }

    @RequestMapping(value = "agencyLicence/{agencyLicenceId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Agency deleteAgencyLicence(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyLicenceId) {
        return userService.deleteAgencyLicence(agencyLicenceId);
    }

    @RequestMapping(value = "agencyUser/{agencyUserId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAgencyUserById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyUserId) {
        return userService.getAgencyUserById(agencyUserId);
    }

    @RequestMapping(value = "agencyProduct", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    AgencyProduct createAgencyProduct(HttpServletRequest request, HttpServletResponse response, @RequestBody AgencyProduct agencyProduct) {
        return userService.createAgencyProduct(agencyProduct);
    }

    @RequestMapping(value = "agencyProduct", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    AgencyProduct updateAgencyProduct(HttpServletRequest request, HttpServletResponse response, @RequestBody AgencyProduct agencyProduct) {
        return userService.updateAgencyProduct(agencyProduct);
    }

    @RequestMapping(value = "agencyProduct/{agencyProductId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAgencyProductById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyProductId) {
        return userService.getAgencyProductById(agencyProductId);
    }

    @RequestMapping(value = "productUpdateOrder/{agencyProductId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateWidgetUpdateOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyProductId) {
        String productOrder = request.getParameter("productOrder");
        System.out.println(productOrder);
        userService.productUpdateOrder(agencyProductId, productOrder);
        return null;
    }

    @RequestMapping(value = "agencyProduct/{agencyProductId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    AgencyProduct deleteAgencyProductId(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyProductId) {
        return userService.deleteAgencyProductId(agencyProductId);
    }

    @RequestMapping(value = "agencySetting", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    AgencySettings createAgencySettings(HttpServletRequest request, HttpServletResponse response, @RequestBody AgencySettings agencySettings) {
     
        return userService.createAgencySettings(agencySettings);
    }

    @RequestMapping(value = "agencySetting", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    AgencySettings updateAgencySettings(HttpServletRequest request, HttpServletResponse response, @RequestBody AgencySettings agencySettings) {
        return userService.updateAgencySettings(agencySettings);
    }
    
    @RequestMapping(value = "agencySetting/{agencyId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    AgencySettings getAgencySettingsById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyId) {
        return userService.getAgencySettingsById(agencyId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
 