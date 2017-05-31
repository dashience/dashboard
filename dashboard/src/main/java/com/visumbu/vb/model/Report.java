/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r")
    , @NamedQuery(name = "Report.findById", query = "SELECT r FROM Report r WHERE r.id = :id")
    , @NamedQuery(name = "Report.findByReportName", query = "SELECT r FROM Report r WHERE r.reportName = :reportName")
    , @NamedQuery(name = "Report.findByReportTitle", query = "SELECT r FROM Report r WHERE r.reportTitle = :reportTitle")
    , @NamedQuery(name = "Report.findByCreatedTime", query = "SELECT r FROM Report r WHERE r.createdTime = :createdTime")
    , @NamedQuery(name = "Report.findByStatus", query = "SELECT r FROM Report r WHERE r.status = :status")
    , @NamedQuery(name = "Report.findByDatasource", query = "SELECT r FROM Report r WHERE r.datasource = :datasource")
    , @NamedQuery(name = "Report.findByDataset", query = "SELECT r FROM Report r WHERE r.dataset = :dataset")
    , @NamedQuery(name = "Report.findByDimension", query = "SELECT r FROM Report r WHERE r.dimension = :dimension")
    , @NamedQuery(name = "Report.findByFilter", query = "SELECT r FROM Report r WHERE r.filter = :filter")
    , @NamedQuery(name = "Report.findBySort", query = "SELECT r FROM Report r WHERE r.sort = :sort")
    , @NamedQuery(name = "Report.findByDisplayOrder", query = "SELECT r FROM Report r WHERE r.displayOrder = :displayOrder")
    , @NamedQuery(name = "Report.findByRemarks", query = "SELECT r FROM Report r WHERE r.remarks = :remarks")
    , @NamedQuery(name = "Report.findByDefaultCount", query = "SELECT r FROM Report r WHERE r.defaultCount = :defaultCount")
    , @NamedQuery(name = "Report.findByIcon", query = "SELECT r FROM Report r WHERE r.icon = :icon")
    , @NamedQuery(name = "Report.findByDescription", query = "SELECT r FROM Report r WHERE r.description = :description")})
public class Report implements Serializable {

    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "logo")
    private String logo;
    @OneToMany(mappedBy = "reportId")
    private Collection<Scheduler> schedulerCollection;
    @OneToMany(mappedBy = "reportId")
    private Collection<ReportWidget> reportWidgetCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1024)
    @Column(name = "report_name")
    private String reportName;
    @Size(max = 1024)
    @Column(name = "report_title")
    private String reportTitle;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Size(max = 64)
    @Column(name = "status")
    private String status;
    @Size(max = 64)
    @Column(name = "datasource")
    private String datasource;
    @Size(max = 1024)
    @Column(name = "dataset")
    private String dataset;
    @Size(max = 1024)
    @Column(name = "dimension")
    private String dimension;
    @Size(max = 1024)
    @Column(name = "filter")
    private String filter;
    @Size(max = 1024)
    @Column(name = "sort")
    private String sort;
    @Lob
    @Size(max = 65535)
    @Column(name = "display_columns")
    private String displayColumns;
    @Column(name = "display_order")
    private Integer displayOrder;
    @Size(max = 1024)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "default_count")
    private Integer defaultCount;
    @Size(max = 256)
    @Column(name = "icon")
    private String icon;
    @Size(max = 4096)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;
    @JoinColumn(name = "report_type_id", referencedColumnName = "id")
    @ManyToOne
    private ReportType reportTypeId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private VbUser createdBy;

    public Report() {
    }

    public Report(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDisplayColumns() {
        return displayColumns;
    }

    public void setDisplayColumns(String displayColumns) {
        this.displayColumns = displayColumns;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDefaultCount() {
        return defaultCount;
    }

    public void setDefaultCount(Integer defaultCount) {
        this.defaultCount = defaultCount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ReportType getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(ReportType reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public VbUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(VbUser createdBy) {
        this.createdBy = createdBy;
    }

    public Agency getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Agency agencyId) {
        this.agencyId = agencyId;
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
        if (!(object instanceof Report)) {
            return false;
        }
        Report other = (Report) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Report{" + "id=" + id + ", reportName=" + reportName + ", reportTitle=" + reportTitle + ", createdTime=" + createdTime + ", status=" + status + ", datasource=" + datasource + ", dataset=" + dataset + ", dimension=" + dimension + ", filter=" + filter + ", sort=" + sort + ", displayColumns=" + displayColumns + ", displayOrder=" + displayOrder + ", remarks=" + remarks + ", defaultCount=" + defaultCount + ", icon=" + icon + ", description=" + description + ", logo=" + logo + ", reportTypeId=" + reportTypeId + ", createdBy=" + createdBy + '}';
    }

}
