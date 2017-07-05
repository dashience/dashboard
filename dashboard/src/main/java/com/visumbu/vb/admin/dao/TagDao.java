/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.Tag;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetTag;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author deldot
 */
@Transactional
@Repository("tagDao")
public class TagDao extends BaseDao {
    
    @Autowired
    private ReportDao reportDao;

    public Tag findTagName(String tagName) {
        Query query = sessionFactory.getCurrentSession().createQuery("select t from Tag t where t.tagName = :tagName");
        query.setParameter("tagName", tagName);
        List<Tag> tagList = (List<Tag>) query.list();
        if (tagList != null && !tagList.isEmpty()) {
            return tagList.get(0);
        }
        return null;
    }

    public List<WidgetTag> getWidgetTagById(Integer tagId) {
        String queryStr = "select t from WidgetTag t where t.tagId.id = :tagId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tagId", tagId);
        
        List<WidgetTag> widgetTag = query.list();
        for (Iterator<WidgetTag> iterator = widgetTag.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next().getWidgetId();
            tabWidget.setColumns(reportDao.getColumns(tabWidget));
        }
        return widgetTag;
    }
    
    public List getWidgetTagByName(String tagName, VbUser vbUser) {
        String queryStr = "select t from WidgetTag t where t.tagId.tagName = :tagName and t.userId.id = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tagName", tagName);
        query.setParameter("userId", vbUser.getId());
        
        List<WidgetTag> widgetTag = query.list();
        for (Iterator<WidgetTag> iterator = widgetTag.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next().getWidgetId();
            tabWidget.setColumns(reportDao.getColumns(tabWidget));
        }
        return widgetTag;        
    }

    public WidgetTag findWidgetTag(Integer tagId) {
        String queryStr = "select t from WidgetTag t where t.tagId.id = :tagId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tagId", tagId);
        List<WidgetTag> widgetTagList = (List<WidgetTag>) query.list();
        if (widgetTagList != null && !widgetTagList.isEmpty()) {
            return widgetTagList.get(0);
        }
        return null;
    }
    
    public WidgetTag findWidgetTagByWidget(Integer tagId, Integer widgetId) {
        String queryStr = "select t from WidgetTag t where t.tagId.id = :tagId and t.widgetId.id = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tagId", tagId);
        query.setParameter("widgetId", widgetId);
        List<WidgetTag> widgetTagList = (List<WidgetTag>) query.list();
        if (widgetTagList != null && !widgetTagList.isEmpty()) {
            return widgetTagList.get(0);
        }
        return null;
    }
    public WidgetTag findWidgetTagByUserNTag(Integer tagId, Integer widgetId, VbUser user) {
        String queryStr = "select t from WidgetTag t where t.tagId.id = :tagId and t.widgetId.id = :widgetId and t.userId = :user";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tagId", tagId);
        query.setParameter("widgetId", widgetId);
        query.setParameter("user", user);
        List<WidgetTag> widgetTagList = (List<WidgetTag>) query.list();
        if (widgetTagList != null && !widgetTagList.isEmpty()) {
            return widgetTagList.get(0);
        }
        return null;
    }
    
    public WidgetTag findWidgetTagByWidgetId(Integer widgetId) {
        String queryStr = "select t from WidgetTag t where t.widgetId.id = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", widgetId);
        List<WidgetTag> widgetTagList = (List<WidgetTag>) query.list();
        if (widgetTagList != null && !widgetTagList.isEmpty()) {
            return widgetTagList.get(0);
        }
        return null;
    }
    
     public WidgetTag deleteWidgetTag(Integer widgetTagId) {
        String queryStr = "delete WidgetTag t where t.id = :widgetTagId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetTagId", widgetTagId);
        query.executeUpdate();
        return null;
    }

    public List<TabWidget> findAllWidgetsByTag(VbUser user, Tag tag) {
        String queryStr = "select w.widgetId from WidgetTag w where w.userId.id = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("tag", tag);
        query.setParameter("userId", user.getId());
        return query.list();
    }   

}
