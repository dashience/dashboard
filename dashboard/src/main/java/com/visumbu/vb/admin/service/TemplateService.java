/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.TemplateDao;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.ProductAccountUserTemplate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author arul
 */
@Service("templateService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TemplateService {

    @Autowired
    private TemplateDao templateDao;

    public ProductAccountUserTemplate create(ProductAccountUserTemplate productAccountUserTemplate) {
        ProductAccountUserTemplate productAccountUserTemplateId = templateDao.findAccountById(productAccountUserTemplate.getAccountId());
        if(productAccountUserTemplateId == null){
            return (ProductAccountUserTemplate) templateDao.create(productAccountUserTemplate);
        }
        if (productAccountUserTemplateId.getAccountId().getId()==productAccountUserTemplate.getAccountId().getId()) {
            productAccountUserTemplate.setId(productAccountUserTemplateId.getId());
            return (ProductAccountUserTemplate) templateDao.update(productAccountUserTemplate);
        } else {
            return (ProductAccountUserTemplate) templateDao.create(productAccountUserTemplate);
        }
    }

    public ProductAccountUserTemplate update(ProductAccountUserTemplate productAccountUserTemplate) {
        return (ProductAccountUserTemplate) templateDao.update(productAccountUserTemplate);
    }

    public List read() {
        List<ProductAccountUserTemplate> productAccountUserTemplate = templateDao.read(ProductAccountUserTemplate.class);
        return productAccountUserTemplate;
    }

    public ProductAccountUserTemplate delete(Integer id) {
        return (ProductAccountUserTemplate) templateDao.delete(id);
    }

    public List<ProductAccountUserTemplate> getProductAccountUserTemplateById(Integer productId) {
       return templateDao.getProductAccountUserTemplateById(productId);
    }

}
