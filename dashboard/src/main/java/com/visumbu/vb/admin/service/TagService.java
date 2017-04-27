/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.TagDao;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.bean.TagWidgetBean;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.Tag;
import com.visumbu.vb.model.WidgetTag;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author deldot
 */
@Service("tagService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TagService {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private UiDao uiDao;

    public Tag create(Tag tag) {
        return (Tag) tagDao.create(tag);
    }

    public Tag read(Integer id) {
        return (Tag) tagDao.read(Tag.class, id);
    }

    public List<Tag> read() {
        List<Tag> tag = tagDao.read(Tag.class);
        return tag;
    }

    public Tag update(Tag tag) {
        return (Tag) tagDao.update(tag);
    }

    public Tag delete(Integer id) {
        Tag tag = read(id);
        return (Tag) tagDao.delete(tag);
        //return dealer;
    }

    public Tag delete(Tag tag) {
        return (Tag) tagDao.delete(tag);
    }

    public WidgetTag createWidgetTag(TagWidgetBean tagWidgetBean) {
        String[] tagArray = tagWidgetBean.getTagName().split(",");
        for (int i = 0; i < tagArray.length; i++) {
            WidgetTag widgetTag = new WidgetTag();
            String widgetTagName = tagArray[i];
            Tag tag = tagDao.findTagName(widgetTagName);
            if (tag == null) {
                tag = new Tag();
                tag.setTagName(widgetTagName);
                tag = (Tag) tagDao.create(tag);
            }
            widgetTag.setTagId(tag);
            Integer widgetId = tagWidgetBean.getWidgetId();
            TabWidget tabWidget = uiDao.getTabWidgetById(widgetId);
            widgetTag.setWidgetId(tabWidget);

            WidgetTag tagWidget = tagDao.findWidgetTag(tag.getId());
            if (tagWidget == null) {
                tagDao.create(widgetTag);
            } else {
                widgetTag.setId(tagWidget.getId());
                tagDao.update(widgetTag);
            }
        }
        return null;
    }

    public List getWidgetTagById(Integer tagWidgetId) {
        return tagDao.getWidgetTagById(tagWidgetId);
    }

    public WidgetTag deleteWidgetTag(Integer widgetTagId) {
        return tagDao.deleteWidgetTag(widgetTagId);
    }
    
//     public WidgetTag readWidgetTag(Integer id) {
//        return (WidgetTag) tagDao.read(Tag.class, id);
//    }
//    
//    public WidgetTag deleteWidgetTag(Integer id) {
//        WidgetTag widgetTag = readWidgetTag(id);
//        return (WidgetTag) tagDao.delete(id);
//        //return dealer;
//    }

}
