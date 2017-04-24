/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "report_widget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportWidget.findAll", query = "SELECT r FROM ReportWidget r")
    , @NamedQuery(name = "ReportWidget.findById", query = "SELECT r FROM ReportWidget r WHERE r.id = :id")
    , @NamedQuery(name = "ReportWidget.findByWidgetOrder", query = "SELECT r FROM ReportWidget r WHERE r.widgetOrder = :widgetOrder")})
public class ReportWidget implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "widget_order")
    private Integer widgetOrder;
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    @ManyToOne
    private Report reportId;
    @JoinColumn(name = "widget_id", referencedColumnName = "id")
    @ManyToOne
    private TabWidget widgetId;

    public ReportWidget() {
    }

    public ReportWidget(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWidgetOrder() {
        return widgetOrder;
    }

    public void setWidgetOrder(Integer widgetOrder) {
        this.widgetOrder = widgetOrder;
    }

    public Report getReportId() {
        return reportId;
    }

    public void setReportId(Report reportId) {
        this.reportId = reportId;
    }

    public TabWidget getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(TabWidget widgetId) {
        this.widgetId = widgetId;
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
        if (!(object instanceof ReportWidget)) {
            return false;
        }
        ReportWidget other = (ReportWidget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.ReportWidget[ id=" + id + " ]";
    }
    
}
