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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author deldot
 */
@Entity
@Table(name = "widget_tag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WidgetTag.findAll", query = "SELECT w FROM WidgetTag w")
    , @NamedQuery(name = "WidgetTag.findById", query = "SELECT w FROM WidgetTag w WHERE w.id = :id")
    , @NamedQuery(name = "WidgetTag.findByWidgetId", query = "SELECT w FROM WidgetTag w WHERE w.widgetId = :widgetId")})
public class WidgetTag implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "widget_id")
    private int widgetId;
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tag tagId;

    public WidgetTag() {
    }

    public WidgetTag(Integer id) {
        this.id = id;
    }

    public WidgetTag(Integer id, int widgetId) {
        this.id = id;
        this.widgetId = widgetId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public Tag getTagId() {
        return tagId;
    }

    public void setTagId(Tag tagId) {
        this.tagId = tagId;
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
        if (!(object instanceof WidgetTag)) {
            return false;
        }
        WidgetTag other = (WidgetTag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.WidgetTag[ id=" + id + " ]";
    }
    
}