/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "scheduler_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SchedulerHistory.findAll", query = "SELECT s FROM SchedulerHistory s")
    , @NamedQuery(name = "SchedulerHistory.findById", query = "SELECT s FROM SchedulerHistory s WHERE s.id = :id")
    , @NamedQuery(name = "SchedulerHistory.findByExecutionStartTime", query = "SELECT s FROM SchedulerHistory s WHERE s.executionStartTime = :executionStartTime")
    , @NamedQuery(name = "SchedulerHistory.findByStatus", query = "SELECT s FROM SchedulerHistory s WHERE s.status = :status")
    , @NamedQuery(name = "SchedulerHistory.findByStartTime", query = "SELECT s FROM SchedulerHistory s WHERE s.startTime = :startTime")
    , @NamedQuery(name = "SchedulerHistory.findByEndTime", query = "SELECT s FROM SchedulerHistory s WHERE s.endTime = :endTime")
    , @NamedQuery(name = "SchedulerHistory.findBySchedulerName", query = "SELECT s FROM SchedulerHistory s WHERE s.schedulerName = :schedulerName")
    , @NamedQuery(name = "SchedulerHistory.findByExecutionEndTime", query = "SELECT s FROM SchedulerHistory s WHERE s.executionEndTime = :executionEndTime")
    , @NamedQuery(name = "SchedulerHistory.findByFileName", query = "SELECT s FROM SchedulerHistory s WHERE s.fileName = :fileName")
    , @NamedQuery(name = "SchedulerHistory.findByEmailSubject", query = "SELECT s FROM SchedulerHistory s WHERE s.emailSubject = :emailSubject")
    , @NamedQuery(name = "SchedulerHistory.findByEmailMessage", query = "SELECT s FROM SchedulerHistory s WHERE s.emailMessage = :emailMessage")})
public class SchedulerHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "execution_start_time")
    @Temporal(TemporalType.TIME)
    private Date executionStartTime;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Column(name = "start_time")
    @Temporal(TemporalType.DATE)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.DATE)
    private Date endTime;
    @Size(max = 255)
    @Column(name = "scheduler_name")
    private String schedulerName;
    @Column(name = "execution_end_time")
    @Temporal(TemporalType.TIME)
    private Date executionEndTime;
    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;
    @Lob
    @Size(max = 65535)
    @Column(name = "email_id")
    private String emailId;
    @Size(max = 255)
    @Column(name = "email_subject")
    private String emailSubject;
    @Size(max = 255)
    @Column(name = "email_message")
    private String emailMessage;
    @JoinColumn(name = "scheduler_id", referencedColumnName = "id")
    @ManyToOne
    private Scheduler schedulerId;

    public SchedulerHistory() {
    }

    public SchedulerHistory(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getExecutionStartTime() {
        return executionStartTime;
    }

    public void setExecutionStartTime(Date executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public Date getExecutionEndTime() {
        return executionEndTime;
    }

    public void setExecutionEndTime(Date executionEndTime) {
        this.executionEndTime = executionEndTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public Scheduler getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(Scheduler schedulerId) {
        this.schedulerId = schedulerId;
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
        if (!(object instanceof SchedulerHistory)) {
            return false;
        }
        SchedulerHistory other = (SchedulerHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.SchedulerHistory[ id=" + id + " ]";
    }
    
}
