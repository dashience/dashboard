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
 * @author deeta
 */
@Entity
@Table(name = "template_tabs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TemplateTabs.findAll", query = "SELECT t FROM TemplateTabs t")
    , @NamedQuery(name = "TemplateTabs.findById", query = "SELECT t FROM TemplateTabs t WHERE t.id = :id")})
public class TemplateTabs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    @ManyToOne
    private DashboardTemplate templateId;
    @JoinColumn(name = "tab_id", referencedColumnName = "id")
    @ManyToOne
    private DashboardTabs tabId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;

    public TemplateTabs() {
    }

    public TemplateTabs(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DashboardTemplate getTemplateId() {
        return templateId;
    }

    public void setTemplateId(DashboardTemplate templateId) {
        this.templateId = templateId;
    }

    public DashboardTabs getTabId() {
        return tabId;
    }

    public void setTabId(DashboardTabs tabId) {
        this.tabId = tabId;
    }

    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
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
        if (!(object instanceof TemplateTabs)) {
            return false;
        }
        TemplateTabs other = (TemplateTabs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.TemplateTabs[ id=" + id + " ]";
    }
    
}
