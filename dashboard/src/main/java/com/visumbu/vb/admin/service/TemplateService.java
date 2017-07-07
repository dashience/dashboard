/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.TemplateDao;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.DashboardTemplate;
import com.visumbu.vb.model.ProductAccountUserTemplate;
import com.visumbu.vb.model.VbUser;
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
    @Autowired
    private UiDao uiDao;

    public ProductAccountUserTemplate create(ProductAccountUserTemplate productAccountUserTemplate) {
        ProductAccountUserTemplate productAccountUserTemplateId = templateDao.findAccountById(productAccountUserTemplate.getAccountId());
        if (productAccountUserTemplateId == null) {
            return (ProductAccountUserTemplate) templateDao.create(productAccountUserTemplate);
        }
        if (productAccountUserTemplateId.getAccountId().getId() == productAccountUserTemplate.getAccountId().getId()) {
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

    public DashboardTemplate getProductAccountUserTemplateById(VbUser vbUser, AgencyProduct product, Account account) {
        DashboardTemplate template = null;
        ProductAccountUserTemplate accountUserTemplate = templateDao.getProductAccountUserTemplateById(vbUser, product, account);
        if (accountUserTemplate != null && accountUserTemplate.getTemplateId() != null) {
            template = accountUserTemplate.getTemplateId();
            return template;
        }
        return product.getTemplateId();        
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
