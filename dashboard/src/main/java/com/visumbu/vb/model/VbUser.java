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

/**
 *
 * @author user
 */
@Entity
@Table(name = "vb_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VbUser.findAll", query = "SELECT v FROM VbUser v")
    ,
    @NamedQuery(name = "VbUser.findById", query = "SELECT v FROM VbUser v WHERE v.id = :id")
    ,
    @NamedQuery(name = "VbUser.findAgencyByUser", query = "SELECT v.agencyId.id FROM VbUser v WHERE v.userName = :userName")
        ,
    @NamedQuery(name = "VbUser.findByCreatedTime", query = "SELECT v FROM VbUser v WHERE v.createdTime = :createdTime")
    ,
    @NamedQuery(name = "VbUser.findByDisableNotification", query = "SELECT v FROM VbUser v WHERE v.disableNotification = :disableNotification")
    ,
    @NamedQuery(name = "VbUser.findByEmail", query = "SELECT v FROM VbUser v WHERE v.email = :email")
    ,
    @NamedQuery(name = "VbUser.findByEmailSignature", query = "SELECT v FROM VbUser v WHERE v.emailSignature = :emailSignature")
    ,
    @NamedQuery(name = "VbUser.findByFailedLoginCount", query = "SELECT v FROM VbUser v WHERE v.failedLoginCount = :failedLoginCount")
    ,
    @NamedQuery(name = "VbUser.findByFirstName", query = "SELECT v FROM VbUser v WHERE v.firstName = :firstName")
    ,
    @NamedQuery(name = "VbUser.findByGender", query = "SELECT v FROM VbUser v WHERE v.gender = :gender")
    ,
    @NamedQuery(name = "VbUser.findByIsAdmin", query = "SELECT v FROM VbUser v WHERE v.isAdmin = :isAdmin")
    ,
    @NamedQuery(name = "VbUser.findByLastLoginTime", query = "SELECT v FROM VbUser v WHERE v.lastLoginTime = :lastLoginTime")
    ,
    @NamedQuery(name = "VbUser.findByLastName", query = "SELECT v FROM VbUser v WHERE v.lastName = :lastName")
    ,
    @NamedQuery(name = "VbUser.findByPassword", query = "SELECT v FROM VbUser v WHERE v.password = :password")
    ,
    @NamedQuery(name = "VbUser.findByPrimaryPhone", query = "SELECT v FROM VbUser v WHERE v.primaryPhone = :primaryPhone")
    ,
    @NamedQuery(name = "VbUser.findBySecondaryPhone", query = "SELECT v FROM VbUser v WHERE v.secondaryPhone = :secondaryPhone")
    ,
    @NamedQuery(name = "VbUser.findByStatus", query = "SELECT v FROM VbUser v WHERE v.status = :status")
    ,
    @NamedQuery(name = "VbUser.findByTheme", query = "SELECT v FROM VbUser v WHERE v.theme = :theme")
    ,
    @NamedQuery(name = "VbUser.findByUserName", query = "SELECT v FROM VbUser v WHERE v.userName = :userName")
,@NamedQuery(name = "VbUser.findUserCountById", query = "SELECT count(v) FROM VbUser v WHERE v.agencyId.id = :agencyId")})
public class VbUser implements Serializable {

    @OneToMany(mappedBy = "userId")
    private Collection<AgencyUser> agencyUserCollection;

    @OneToMany(mappedBy = "userId")
    private Collection<UserPermission> userPermissionCollection;

    @OneToMany(mappedBy = "userId")
    private Collection<UserAccount> userAccountCollection;

    @OneToMany(mappedBy = "userId")
    private Collection<AccountUser> accountUserCollection;

    @OneToMany(mappedBy = "userId")
    private Collection<DataSet> dataSetCollection;
    @OneToMany(mappedBy = "userId")
    private Collection<DataSource> dataSourceCollection;

    @OneToMany(mappedBy = "createdBy")
    private Collection<TabWidget> tabWidgetCollection;
    @OneToMany(mappedBy = "createdBy")
    private Collection<Report> reportCollection;
    @OneToMany(mappedBy = "userId")
    private Collection<ReportType> reportTypesCollection;
    @OneToMany(mappedBy = "createdBy")
    private Collection<ReportType> reportTypesCollection1;
    @OneToMany(mappedBy = "userId")
    private Collection<Dashboard> dashboardCollection;
    @OneToMany(mappedBy = "userId")
    private Collection<WidgetTag> widgetTagCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Size(max = 255)
    @Column(name = "disable_notification")
    private String disableNotification;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "email_signature")
    private String emailSignature;
    @Column(name = "failed_login_count")
    private Integer failedLoginCount;
    @Size(max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 255)
    @Column(name = "gender")
    private String gender;
    @Column(name = "is_admin")
    private Boolean isAdmin;
    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    @Size(max = 255)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    @Size(max = 255)
    @Column(name = "primary_phone")
    private String primaryPhone;
    @Size(max = 255)
    @Column(name = "secondary_phone")
    private String secondaryPhone;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "theme")
    private String theme;
    @Size(max = 255)
    @Column(name = "user_name")
    private String userName;
    @Size(max = 255)
    @Column(name = "user_ref_id")
    private String userRefId;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;

    public VbUser() {
    }

    public VbUser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getDisableNotification() {
        return disableNotification;
    }

    public void setDisableNotification(String disableNotification) {
        this.disableNotification = disableNotification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailSignature() {
        return emailSignature;
    }

    public void setEmailSignature(String emailSignature) {
        this.emailSignature = emailSignature;
    }

    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRefId() {
        return userRefId;
    }

    public void setUserRefId(String userRefId) {
        this.userRefId = userRefId;
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
        if (!(object instanceof VbUser)) {
            return false;
        }
        VbUser other = (VbUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.VbUser[ id=" + id + " ]";
    }

}
