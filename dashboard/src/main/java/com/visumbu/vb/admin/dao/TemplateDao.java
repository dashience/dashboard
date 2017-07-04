/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.ProductAccountUserTemplate;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author arul
 */
@Transactional
@Repository("templateDao")
public class TemplateDao extends BaseDao{

    public ProductAccountUserTemplate findAccountById(Account accountId) {
        Integer accountIdInt = accountId.getId();
        Query query = sessionFactory.getCurrentSession().createQuery("select t from ProductAccountUserTemplate t where t.accountId.id = :id");
        query.setParameter("id", accountIdInt);
        List<ProductAccountUserTemplate> productList = (List<ProductAccountUserTemplate>) query.list();
        if (productList != null && !productList.isEmpty()) {
            return productList.get(0);
        }
        return null;
    }
    
    public List<ProductAccountUserTemplate> findProductById(Integer productId) {
        Query query = sessionFactory.getCurrentSession().createQuery("select t from ProductAccountUserTemplate t where t.productId.id = :id");
        query.setParameter("id", productId);       
        return query.list();
    }

    public List<ProductAccountUserTemplate> getProductAccountUserTemplateById(Integer productId) {
        Query query = sessionFactory.getCurrentSession().createQuery("select t from ProductAccountUserTemplate t where t.productId.id = :id");
        query.setParameter("id", productId);       
        return query.list();
    }
    
}