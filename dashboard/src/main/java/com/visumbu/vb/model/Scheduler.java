/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "scheduler")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scheduler.findAll", query = "SELECT s FROM Scheduler s")
    , @NamedQuery(name = "Scheduler.findById", query = "SELECT s FROM Scheduler s WHERE s.id = :id")
    , @NamedQuery(name = "Scheduler.findBySchedulerName", query = "SELECT s FROM Scheduler s WHERE s.schedulerName = :schedulerName")
    , @NamedQuery(name = "Scheduler.findByStartDate", query = "SELECT s FROM Scheduler s WHERE s.startDate = :startDate")
    , @NamedQuery(name = "Scheduler.findByEndDate", query = "SELECT s FROM Scheduler s WHERE s.endDate = :endDate")
    , @NamedQuery(name = "Scheduler.findBySchedulerWeekly", query = "SELECT s FROM Scheduler s WHERE s.schedulerWeekly = :schedulerWeekly")
    , @NamedQuery(name = "Scheduler.findBySchedulerTime", query = "SELECT s FROM Scheduler s WHERE s.schedulerTime = :schedulerTime")
    , @NamedQuery(name = "Scheduler.findBySchedulerMonthly", query = "SELECT s FROM Scheduler s WHERE s.schedulerMonthly = :schedulerMonthly")
    , @NamedQuery(name = "Scheduler.findBySchedulerYearOfWeek", query = "SELECT s FROM Scheduler s WHERE s.schedulerYearOfWeek = :schedulerYearOfWeek")
    , @NamedQuery(name = "Scheduler.findBySchedulerRepeatType", query = "SELECT s FROM Scheduler s WHERE s.schedulerRepeatType = :schedulerRepeatType")
    , @NamedQuery(name = "Scheduler.findBySchedulerNow", query = "SELECT s FROM Scheduler s WHERE s.schedulerNow = :schedulerNow")
    , @NamedQuery(name = "Scheduler.findBySchedulerYearly", query = "SELECT s FROM Scheduler s WHERE s.schedulerYearly = :schedulerYearly")
    , @NamedQuery(name = "Scheduler.findBySchedulerStatus", query = "SELECT s FROM Scheduler s WHERE s.schedulerStatus = :schedulerStatus")
    , @NamedQuery(name = "Scheduler.findBySchedulerType", query = "SELECT s FROM Scheduler s WHERE s.schedulerType = :schedulerType")
    , @NamedQuery(name = "Scheduler.findByCustomEndDate", query = "SELECT s FROM Scheduler s WHERE s.customEndDate = :customEndDate")
    , @NamedQuery(name = "Scheduler.findByCustomStartDate", query = "SELECT s FROM Scheduler s WHERE s.customStartDate = :customStartDate")
    , @NamedQuery(name = "Scheduler.findByDateRangeName", query = "SELECT s FROM Scheduler s WHERE s.dateRangeName = :dateRangeName")
    , @NamedQuery(name = "Scheduler.findByLastNdays", query = "SELECT s FROM Scheduler s WHERE s.lastNdays = :lastNdays")
    , @NamedQuery(name = "Scheduler.findByLastNmonths", query = "SELECT s FROM Scheduler s WHERE s.lastNmonths = :lastNmonths")
    , @NamedQuery(name = "Scheduler.findByLastNweeks", query = "SELECT s FROM Scheduler s WHERE s.lastNweeks = :lastNweeks")
    , @NamedQuery(name = "Scheduler.findByLastNyears", query = "SELECT s FROM Scheduler s WHERE s.lastNyears = :lastNyears")
    , @NamedQuery(name = "Scheduler.findByIsAccountEmail", query = "SELECT s FROM Scheduler s WHERE s.isAccountEmail = :isAccountEmail")})
public class Scheduler implements Serializable {

    @OneToMany(mappedBy = "schedulerId")
    private Collection<SchedulerHistory> schedulerHistoryCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "schedulerName")
    private String schedulerName;
    @Size(max = 255)
    @Column(name = "start_date")
    private String startDate;
    @Size(max = 255)
    @Column(name = "end_date")
    private String endDate;
    @Size(max = 255)
    @Column(name = "scheduler_weekly")
    private String schedulerWeekly;
    @Size(max = 255)
    @Column(name = "scheduler_time")
    private String schedulerTime;
    @Size(max = 255)
    @Column(name = "scheduler_monthly")
    private String schedulerMonthly;
    @Column(name = "scheduler_year_of_week")
    private Integer schedulerYearOfWeek;
    @Size(max = 255)
    @Column(name = "scheduler_repeat_type")
    private String schedulerRepeatType;
    @Size(max = 45)
    @Column(name = "scheduler_now")
    private String schedulerNow;
    @Size(max = 255)
    @Column(name = "scheduler_yearly")
    private String schedulerYearly;
    @Size(max = 45)
    @Column(name = "scheduler_status")
    private String schedulerStatus;
    @Size(max = 45)
    @Column(name = "scheduler_type")
    private String schedulerType;
    @Size(max = 45)
    @Column(name = "custom_end_date")
    private String customEndDate;
    @Size(max = 45)
    @Column(name = "custom_start_date")
    private String customStartDate;
    @Size(max = 255)
    @Column(name = "date_range_name")
    private String dateRangeName;
    @Column(name = "last_ndays")
    private Integer lastNdays;
    @Column(name = "last_nmonths")
    private Integer lastNmonths;
    @Column(name = "last_nweeks")
    private Integer lastNweeks;
    @Column(name = "last_nyears")
    private Integer lastNyears;
    @Lob
    @Size(max = 16777215)
    @Column(name = "scheduler_email")
    private String schedulerEmail;
    @Column(name = "is_account_email")
    private Boolean isAccountEmail;
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    @ManyToOne
    private Report reportId;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private Account accountId;

    public Scheduler() {
    }

    public Scheduler(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
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

    public String getSchedulerWeekly() {
        return schedulerWeekly;
    }

    public void setSchedulerWeekly(String schedulerWeekly) {
        this.schedulerWeekly = schedulerWeekly;
    }

    public String getSchedulerTime() {
        return schedulerTime;
    }

    public void setSchedulerTime(String schedulerTime) {
        this.schedulerTime = schedulerTime;
    }

    public String getSchedulerMonthly() {
        return schedulerMonthly;
    }

    public void setSchedulerMonthly(String schedulerMonthly) {
        this.schedulerMonthly = schedulerMonthly;
    }

    public Integer getSchedulerYearOfWeek() {
        return schedulerYearOfWeek;
    }

    public void setSchedulerYearOfWeek(Integer schedulerYearOfWeek) {
        this.schedulerYearOfWeek = schedulerYearOfWeek;
    }

    public String getSchedulerRepeatType() {
        return schedulerRepeatType;
    }

    public void setSchedulerRepeatType(String schedulerRepeatType) {
        this.schedulerRepeatType = schedulerRepeatType;
    }

    public String getSchedulerNow() {
        return schedulerNow;
    }

    public void setSchedulerNow(String schedulerNow) {
        this.schedulerNow = schedulerNow;
    }

    public String getSchedulerYearly() {
        return schedulerYearly;
    }

    public void setSchedulerYearly(String schedulerYearly) {
        this.schedulerYearly = schedulerYearly;
    }

    public String getSchedulerStatus() {
        return schedulerStatus;
    }

    public void setSchedulerStatus(String schedulerStatus) {
        this.schedulerStatus = schedulerStatus;
    }

    public String getSchedulerType() {
        return schedulerType;
    }

    public void setSchedulerType(String schedulerType) {
        this.schedulerType = schedulerType;
    }

    public String getCustomEndDate() {
        return customEndDate;
    }

    public void setCustomEndDate(String customEndDate) {
        this.customEndDate = customEndDate;
    }

    public String getCustomStartDate() {
        return customStartDate;
    }

    public void setCustomStartDate(String customStartDate) {
        this.customStartDate = customStartDate;
    }

    public String getDateRangeName() {
        return dateRangeName;
    }

    public void setDateRangeName(String dateRangeName) {
        this.dateRangeName = dateRangeName;
    }

    public Integer getLastNdays() {
        return lastNdays;
    }

    public void setLastNdays(Integer lastNdays) {
        this.lastNdays = lastNdays;
    }

    public Integer getLastNmonths() {
        return lastNmonths;
    }

    public void setLastNmonths(Integer lastNmonths) {
        this.lastNmonths = lastNmonths;
    }

    public Integer getLastNweeks() {
        return lastNweeks;
    }

    public void setLastNweeks(Integer lastNweeks) {
        this.lastNweeks = lastNweeks;
    }

    public Integer getLastNyears() {
        return lastNyears;
    }

    public void setLastNyears(Integer lastNyears) {
        this.lastNyears = lastNyears;
    }

    public String getSchedulerEmail() {
        return schedulerEmail;
    }

    public void setSchedulerEmail(String schedulerEmail) {
        this.schedulerEmail = schedulerEmail;
    }

    public Boolean getIsAccountEmail() {
        return isAccountEmail;
    }

    public void setIsAccountEmail(Boolean isAccountEmail) {
        this.isAccountEmail = isAccountEmail;
    }

    public Report getReportId() {
        return reportId;
    }

    public void setReportId(Report reportId) {
        this.reportId = reportId;
    }

    public Agency getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Agency agencyId) {
        this.agencyId = agencyId;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Scheduler)) {
            return false;
        }
        Scheduler other = (Scheduler) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.Scheduler[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<SchedulerHistory> getSchedulerHistoryCollection() {
        return schedulerHistoryCollection;
    }

    public void setSchedulerHistoryCollection(Collection<SchedulerHistory> schedulerHistoryCollection) {
        this.schedulerHistoryCollection = schedulerHistoryCollection;
    }
    
}
