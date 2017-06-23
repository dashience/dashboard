/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;
import com.visumbu.api.bing.report.xml.bean.AccountDayOfWeekPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountDevicePerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountHourOfDayPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AccountPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AdGroupPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.AdPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.CampaignDevicePerformanceReport;
import com.visumbu.api.bing.report.xml.bean.CampaignPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.GeoCityLocationPerformanceReport;
import com.visumbu.api.bing.report.xml.bean.GeoZipLocationPerformanceReport;
import com.visumbu.api.admin.service.BingService;
import com.visumbu.api.utils.DateUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
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
@RequestMapping("bing")
public class BingController {

    @Autowired
    private BingService bingService;
//    public static final Long accountId = 2610614L;

//    @RequestMapping(value = "testBing", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    Object getAllDataSets(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {
//        try {
//            Date startDate = DateUtils.get30DaysBack();
//            Date endDate = new Date();
//            return bingService.getKeywordPerformanceReport(startDate, endDate, accountId);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    public String getFromMultiValueMap(MultiValueMap valueMap, String key) {
//        List<String> dataSourceTypeList = (List<String>) valueMap.get(key);
//        if (dataSourceTypeList != null && !dataSourceTypeList.isEmpty()) {
//            return dataSourceTypeList.get(0);
//        }
//        return null;
//    }

    @RequestMapping(value = "getData", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Map<String, Object>> getData(HttpServletRequest request, HttpServletResponse response) throws InterruptedException,
            ExecutionException, TimeoutException {
        Map returnMap = new HashMap<>();
        String dataSourceType = request.getParameter("dataSourceType");
        System.out.println("dataSourceType -000000000-> " + dataSourceType);
        String dataSetReportName = request.getParameter("dataSetReportName");
//        String startDate=request.getParameter("startDate");
//        String endDate=request.getParameter("endDate");

        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        System.out.println("startDate 1 ----> " + startDate);

        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String dataSetId = request.getParameter("dataSetId");
        String timeSegment = request.getParameter("timeSegment");
        String productSegment = request.getParameter("productSegment");
        if (timeSegment == null) {
            timeSegment = "daily";
        }

        
        String accountId = request.getParameter("bingAccountId");
        try {
            Long bingAccountId = Long.parseLong(accountId);

            List<Map<String, Object>> bingReport = bingService.get(dataSetReportName, bingAccountId, startDate, endDate,
                    timeSegment, productSegment);
//        String dataSourceType = getFromMultiValueMap(request, "dataSourceType");
//        System.out.println("dataSourceType --> " + dataSourceType);
//        String dataSetId = getFromMultiValueMap(request, "dataSetId");
//        System.out.println("My dataSourceType-->"+dataSourceType);
//        Integer dataSetIdInt = null;
//        if (dataSetId != null) {
//            try {
//                dataSetIdInt = Integer.parseInt(dataSetId);
//            } catch (Exception e) {
//
//            }
//            if (dataSetIdInt != null) {
//                DataSet dataSet = uiService.readDataSet(dataSetIdInt);
//                dataSourceType = dataSet.getDataSourceId().getDataSourceType();
//            }
//        }
            return bingReport;
        }catch(NumberFormatException ex){
            return null;
        }
    }
//
//    @RequestMapping(value = "accountPerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List<Map<String, Object>> getAccountPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            Date startDate = DateUtils.get30DaysBack();
//            Date endDate = new Date();
//            String dataSetReportName = "accountPerformance";
//            Long bingAccountIdLong = 2610614L;
//            String timeSegment = "none";
//            String productSegment = "none";
//
//            List<Map<String, Object>> data = bingService.get(dataSetReportName, bingAccountIdLong, startDate,
//                    endDate, timeSegment, productSegment);
//
//            return data;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

//    @RequestMapping(value = "accountHourOfDayPerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    AccountHourOfDayPerformanceReport getAccountHourOfDayPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            Date startDate = DateUtils.get30DaysBack();
//            Date endDate = new Date();
//            return bingService.getAccountHourOfDayPerformanceReport(startDate, endDate, accountId);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
////
//    @RequestMapping(value = "accountDayOfWeekPerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    AccountDayOfWeekPerformanceReport getAccountDayOfWeekPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            Date startDate = DateUtils.get30DaysBack();
//            Date endDate = new Date();
//            return bingService.getAccountDayOfWeekPerformanceReport(startDate, endDate, accountId);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    @RequestMapping(value = "accountDevicePerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    AccountDevicePerformanceReport getAccountDevicePerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        Date startDate = DateUtils.get30DaysBack();
//        Date endDate = new Date();
//        return bingService.getAccountDevicePerformanceReport(startDate, endDate, accountId, "");
//
//    }
//
//    @RequestMapping(value = "getCampaignPerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    CampaignPerformanceReport getCampaignPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        Date startDate = DateUtils.get30DaysBack();
//        Date endDate = new Date();
//        return bingService.getCampaignPerformanceReport(startDate, endDate, accountId, "");
//    }
////
//    @RequestMapping(value = "getCampaignDevicePerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    CampaignDevicePerformanceReport getCampaignDevicePerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        Date startDate = DateUtils.get30DaysBack();
//        Date endDate = new Date();
//        return bingService.getCampaignDevicePerformanceReport(startDate, endDate, accountId, "daily");
//    }
////
//    @RequestMapping(value = "getAdGroupPerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    AdGroupPerformanceReport getAdGroupPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        Date startDate = DateUtils.get30DaysBack();
//        Date endDate = new Date();
//        return bingService.getAdGroupPerformanceReport(startDate, endDate, accountId, "");
//    }
//
//    //Error
//    @RequestMapping(value = "getAdPerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    AdPerformanceReport getAdPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        Date startDate = DateUtils.get30DaysBack();
//        Date endDate = new Date();
//        return bingService.getAdPerformanceReport(startDate, endDate, accountId);
//    }
//
//    @RequestMapping(value = "getGeoCityLocationPerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    GeoCityLocationPerformanceReport getGeoCityLocationPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        Date startDate = DateUtils.get30DaysBack();
//        Date endDate = new Date();
//        return bingService.getGeoCityLocationPerformanceReport(startDate, endDate, accountId, "");
//    }
//
//    @RequestMapping(value = "getGeoZipLocationPerformanceReport", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    GeoZipLocationPerformanceReport getGeoZipLocationPerformanceReport(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            Date startDate = DateUtils.get30DaysBack();
//            Date endDate = new Date();
//            return bingService.getGeoZipLocationPerformanceReport(startDate, endDate, accountId);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TimeoutException ex) {
//            Logger.getLogger(BingController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public void handle(HttpMessageNotReadableException e) {
//        e.printStackTrace();
//    }
}