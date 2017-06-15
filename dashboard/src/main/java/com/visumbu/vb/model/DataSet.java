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
@Table(name = "data_set")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataSet.findAll", query = "SELECT d FROM DataSet d")
    , @NamedQuery(name = "DataSet.findById", query = "SELECT d FROM DataSet d WHERE d.id = :id")
    , @NamedQuery(name = "DataSet.findByName", query = "SELECT d FROM DataSet d WHERE d.name = :name")
    , @NamedQuery(name = "DataSet.findByProductSegment", query = "SELECT d FROM DataSet d WHERE d.productSegment = :productSegment")
    , @NamedQuery(name = "DataSet.findByReportName", query = "SELECT d FROM DataSet d WHERE d.reportName = :reportName")
    , @NamedQuery(name = "DataSet.findByTimeSegment", query = "SELECT d FROM DataSet d WHERE d.timeSegment = :timeSegment")
    , @NamedQuery(name = "DataSet.findByUrl", query = "SELECT d FROM DataSet d WHERE d.url = :url")})
public class DataSet implements Serializable {

    @OneToMany(mappedBy = "datasetId")
    private Collection<DatasetColumns> datasetColumnsCollection;

    @Size(max = 255)
    @Column(name = "sheet_name")
    private String sheetName;
    @OneToMany(mappedBy = "dataSetId")
    private Collection<TabWidget> tabWidgetCollection;

    @Size(max = 500)
    @Column(name = "report_performance")
    private String reportPerformance;
    @Size(max = 255)
    @Column(name = "network_type")
    private String networkType;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Lob
    @Size(max = 65535)
    @Column(name = "query")
    private String query;
    @Size(max = 255)
    @Column(name = "product_segment")
    private String productSegment;
    @Size(max = 255)
    @Column(name = "report_name")
    private String reportName;
    @Size(max = 255)
    @Column(name = "time_segment")
    private String timeSegment;
    @Size(max = 4095)
    @Column(name = "url")
    private String url;
    @JoinColumn(name = "data_source_id", referencedColumnName = "id")
    @ManyToOne
    private DataSource dataSourceId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;

    public DataSet() {
    }

    public DataSet(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getProductSegment() {
        return productSegment;
    }

    public void setProductSegment(String productSegment) {
        this.productSegment = productSegment;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getTimeSegment() {
        return timeSegment;
    }

    public void setTimeSegment(String timeSegment) {
        this.timeSegment = timeSegment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DataSource getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(DataSource dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
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
        if (!(object instanceof DataSet)) {
            return false;
        }
        DataSet other = (DataSet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DataSet[ id=" + id + " ]";
    }

    public String getReportPerformance() {
        return reportPerformance;
    }

    public void setReportPerformance(String reportPerformance) {
        this.reportPerformance = reportPerformance;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

}
