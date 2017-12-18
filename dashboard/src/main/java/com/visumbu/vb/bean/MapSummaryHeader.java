/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import com.visumbu.vb.utils.DateUtils;
import java.util.Date;

/**
 *
 * @author varghees
 */
public class MapSummaryHeader {
    private String startDate;
    private String endDate;

    public MapSummaryHeader(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public MapSummaryHeader(Date startDate, Date endDate) {
        this.startDate = DateUtils.dateToString(startDate, "MM-dd-yyyy");
        this.endDate = DateUtils.dateToString(endDate, "MM-dd-yyyy");
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
