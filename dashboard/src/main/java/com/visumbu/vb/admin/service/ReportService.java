/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.ReportDao;
import com.visumbu.vb.bean.ReportPage;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("reportService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReportService {

    @Autowired
    private ReportDao reportDao;
    
    @Autowired
    private UiService uiService;

//    public List getVisitDetailedList(Date startDate, Date endDate, ReportPage page) {
//        return reportDao.getVisitDetailedList(startDate, endDate, page);
//    }

    public List getAgencyReport(VbUser user) {
        if (user.getAgencyId() == null) {
            List<Report> report = reportDao.read(Report.class);
            return report;
        }
        return reportDao.getAgencyReport(user);
    }

    public Report getReportById(Integer reportId) {
        return reportDao.getReportById(reportId);
    }

    public Report addReport(Report report) {
        return (Report) reportDao.create(report);
    }

    public Report updateReport(Report report) {
        return (Report) reportDao.update(report);
    }

    public String updateReportOrder(Integer reportId, String widgetOrder) {
        return reportDao.updateReportOrder(reportId, widgetOrder);
    }   

    public Report deleteReport(Integer reportId) {
        return reportDao.deleteReport(reportId);
    }

    public ReportWidget createReportWidget(ReportWidget reportWidget) {
        return (ReportWidget) reportDao.create(reportWidget);
    }

    public ReportWidget updateReportWidget(ReportWidget reportWidget) {
        return (ReportWidget) reportDao.update(reportWidget);
    }

    public List<ReportWidget> getReportWidget() {
        List<ReportWidget> reportWidget = reportDao.read(ReportWidget.class);
        return reportWidget;
    }

    public List<ReportWidget> getReportWidget(Integer reportId) {
        return reportDao.getReportWidget(reportId);
    }

    public ReportWidget deleteReportWidget(Integer reportId) {
        return reportDao.deleteReportWidget(reportId);
    }

    public TabWidget saveTabWidget(Integer tabId, TabWidgetBean tabWidget) {
        return uiService.saveTabWidget(tabId, tabWidget);
    }
    

}
