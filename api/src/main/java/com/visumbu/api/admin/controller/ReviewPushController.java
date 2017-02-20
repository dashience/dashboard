/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.ReviewPushService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.bean.AccountDetails;
import com.visumbu.api.bean.ColumnDef;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.bean.ReportPage;
import com.visumbu.api.controller.BaseController;
import com.visumbu.api.utils.ApiUtils;
import com.visumbu.api.utils.DateUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
@RequestMapping("reviewPush")
public class ReviewPushController extends BaseController {

    @Autowired
    private ReviewPushService reviewPushService;

    @RequestMapping(value = "reviews", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getReviews(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("rating", "number", "Rating", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL2));
        columnDefs.add(new ColumnDef("review_url", "string", "Review URL"));
        columnDefs.add(new ColumnDef("reviewer", "string", "Reviewer"));
        columnDefs.add(new ColumnDef("type", "string", "Type"));
        columnDefs.add(new ColumnDef("respond_url", "string", "Respond Url"));
        columnDefs.add(new ColumnDef("review_date", "date", "Review Date"));
        columnDefs.add(new ColumnDef("review", "string", "Review"));
        columnDefs.add(new ColumnDef("review_time", "string", "Review Time"));
        columnDefs.add(new ColumnDef("created_time", "string", "Created Time"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "none");
        if (accountDetails.getReviewPushAccountId() != null) {
            returnMap.put("data", reviewPushService.getReviews(startDate, endDate, accountDetails.getReviewPushAccountId()));
        } else {
            returnMap.put("data", null);
        }
        return returnMap;
    }

    @RequestMapping(value = "ratingSummary", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getRatingSummaryByDealer(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("rating_weighted", "number", "Rating Weighted", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL2));
        columnDefs.add(new ColumnDef("rating", "number", "Rating", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL2));
        columnDefs.add(new ColumnDef("reviews", "number", "Reviews", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("zip", "string", "Zip"));
        columnDefs.add(new ColumnDef("city", "string", "City"));
        columnDefs.add(new ColumnDef("state", "string", "State"));
        columnDefs.add(new ColumnDef("display_name", "string", "Display Name"));
        columnDefs.add(new ColumnDef("address_1", "string", "Address1"));
        columnDefs.add(new ColumnDef("address_2", "string", "Address2"));
        columnDefs.add(new ColumnDef("created", "string", "Created Time"));
        columnDefs.add(new ColumnDef("type", "string", "Type"));
        columnDefs.add(new ColumnDef("name", "string", "Name"));
        columnDefs.add(new ColumnDef("email", "string", "Email"));
        columnDefs.add(new ColumnDef("phone", "string", "Phone"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "none");
        System.out.println("Review Push Account Id " + accountDetails.getReviewPushAccountId());
        if (accountDetails.getReviewPushAccountId() != null) {
            returnMap.put("data", reviewPushService.getRatingSummaryByDealer(startDate, endDate, accountDetails.getReviewPushAccountId()));
        } else {
            returnMap.put("data", null);
        }
        return returnMap;
    }

    @RequestMapping(value = "bySources", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getBySources(HttpServletRequest request, HttpServletResponse response) {
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        columnDefs.add(new ColumnDef("rating_weighted", "number", "Rating Weighted", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL2));
        columnDefs.add(new ColumnDef("rating", "number", "Rating", ColumnDef.Aggregation.AVG, ColumnDef.Format.DECIMAL2));
        columnDefs.add(new ColumnDef("reviews", "number", "Reviews", ColumnDef.Aggregation.SUM, ColumnDef.Format.INTEGER));
        columnDefs.add(new ColumnDef("url", "string", "Url"));
        columnDefs.add(new ColumnDef("created", "string", "Created Time"));
        columnDefs.add(new ColumnDef("type", "string", "Type"));
        columnDefs.add(new ColumnDef("name", "string", "Name"));
        columnDefs.add(new ColumnDef("site_name", "string", "Site Name"));

        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        AccountDetails accountDetails = ApiUtils.toAccountDetails(request, "none");
        if (accountDetails.getReviewPushAccountId() != null) {
            returnMap.put("data", reviewPushService.getBySources(startDate, endDate, accountDetails.getReviewPushAccountId()));
        } else {
            returnMap.put("data", null);
        }
        return returnMap;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
