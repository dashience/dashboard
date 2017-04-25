/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.TagDao;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.model.Tag;
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

}
