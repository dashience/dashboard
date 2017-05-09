/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.adwords.report.xml.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@XmlRootElement(name = "report")
public class AccountDayOfWeekReport {
    private List<AccountDayOfWeekReportRow> accountDayOfWeekReportRow;

    public List<AccountDayOfWeekReportRow> getAccountDayOfWeekReportRow() {
        return accountDayOfWeekReportRow;
    }

    @XmlElementWrapper(name = "table")
    @XmlElement(name = "row")
    public void setAccountDayOfWeekReportRow(List<AccountDayOfWeekReportRow> accountDayOfWeekReportRow) {
        this.accountDayOfWeekReportRow = accountDayOfWeekReportRow;
    }

    @Override
    public String toString() {
        return "AccountDayOfWeekReport{" + "accountDayOfWeekReportRow=" + accountDayOfWeekReportRow + '}';
    }
    
}
