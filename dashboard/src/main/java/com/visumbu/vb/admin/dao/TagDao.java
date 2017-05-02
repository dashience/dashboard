/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Tag;
import com.visumbu.vb.model.WidgetTag;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author deldot
 */
@Transactional
@Repository("tagDao")
public class TagDao extends BaseDao {

    public Tag findTagName(String tagName) {
        Query query = sessionFactory.getCurrentSession().createQuery("select t from Tag t where t.tagName = :tagName");
        query.setParameter("tagName", tagName);
        List<Tag> tagList = (List<Tag>) query.list();
        if (tagList != null && !tagList.isEmpty()) {
            return tagList.get(0);
        }
        return null;
    }

    public List<WidgetTag> getWidgetTagById(Integer tagWidgetId) {
        String queryStr = "select t from WidgetTag t where t.widgetId.id = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", tagWidgetId);
        return query.list();
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
    
     public WidgetTag deleteWidgetTag(Integer widgetTagId) {
        String queryStr = "delete WidgetTag t where t.id = :widgetTagId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetTagId", widgetTagId);
        query.executeUpdate();
        return null;
    }

}
