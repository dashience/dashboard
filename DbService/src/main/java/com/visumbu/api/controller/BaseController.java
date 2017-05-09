/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.controller;

import com.visumbu.api.bean.ReportPage;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
public class BaseController {
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
}
