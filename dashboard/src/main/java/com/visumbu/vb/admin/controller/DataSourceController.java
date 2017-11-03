/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.dao.bean.DataSourceBean;
import com.visumbu.vb.admin.service.DataSourceService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.VbUser;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * @author Varghees Samraj
 */
@Controller
@RequestMapping("datasources")
public class DataSourceController extends BaseController {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAllDataSources(HttpServletRequest request, HttpServletResponse response) {
        return dataSourceService.getAllDataSources();
    }

    @RequestMapping(value = "dataSet/{dataSourceName}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAllDataSets(HttpServletRequest request, HttpServletResponse response, @PathVariable String dataSourceName) throws IOException, GeneralSecurityException {
        return dataSourceService.getAllDataSets(dataSourceName);
    }

    @RequestMapping(value = "dataDimensions/{dataSourceName}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAllDataDimensions(HttpServletRequest request, HttpServletResponse response, @PathVariable String dataSourceName) throws IOException, GeneralSecurityException {
        String dataSet = request.getParameter("dataSet");
        return dataSourceService.getAllDimensions(dataSourceName, dataSet);
    }

    @RequestMapping(value = "data/{dataSourceName}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getData(HttpServletRequest request, HttpServletResponse response, @PathVariable String dataSourceName) throws IOException, GeneralSecurityException {
        String profileId = request.getParameter("profileId");
        String dimensions = request.getParameter("dimensions");
        String dataSet = request.getParameter("dataSet");
        String sort = request.getParameter("sort");
        String filter = request.getParameter("filter");
        Map options = new HashMap();
        options.put("profileId", profileId);
        options.put("dimensions", dimensions);
        options.put("sort", sort);
        options.put("filter", filter);
        ReportPage page = getPage(request);
        return dataSourceService.getData(dataSourceName, dataSet, options, page);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSource create(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSourceBean dataSource) {
        VbUser user = userService.findByUsername(getUser(request));
        dataSource.setUserId(user);
        dataSource.setAgencyId(user.getAgencyId());
        return dataSourceService.saveDataSource(dataSource);
    }

    @RequestMapping(value = "joinDataSource", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSource createDataSourceForJoinDataSet(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSourceBean dataSource) {
        VbUser user = userService.findByUsername(getUser(request));
        dataSource.setUserId(user);
        dataSource.setAgencyId(user.getAgencyId());
        return dataSourceService.createDataSourceForJoinDataSet(dataSource);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSource update(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSource dataSource) {
        return dataSourceService.update(dataSource);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDataSource(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return dataSourceService.getDataSourceByUser(user);
    }

//    @RequestMapping(value = "dataSource", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List getDataSource(HttpServletRequest request, HttpServletResponse response) {
//        return uiService.getDataSource();
//    }
    @RequestMapping(value = "dataSource/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSource delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return dataSourceService.deleteDataSource(id);
    }

    public ReportPage getPage(HttpServletRequest request) {
        ReportPage reportPage = new ReportPage();
        if (request.getParameter("page") == null && request.getParameter("count") == null) {
            return null;
        }
        Integer count = 50;
        if (request.getParameter("count") != null) {
            count = Integer.parseInt(request.getParameter("count"));
        }
        if (request.getParameter("page") != null) {
            Integer start = 0;
            Integer page = Integer.parseInt(request.getParameter("page"));
            start = count * (page - 1);
            reportPage.setStart(start);
            reportPage.setPageNo(page);
            reportPage.setCount(count);
        }
        return reportPage;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
