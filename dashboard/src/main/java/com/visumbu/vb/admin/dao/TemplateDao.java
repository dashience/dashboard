/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.DashboardTemplate;
import com.visumbu.vb.model.ProductAccountUserTemplate;
import com.visumbu.vb.model.VbUser;
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
    
    public ProductAccountUserTemplate findProductById(AgencyProduct productId) {
        Integer productIdInt = productId.getId();
        Query query = sessionFactory.getCurrentSession().createQuery("select t from ProductAccountUserTemplate t where t.productId.id = :id");
        query.setParameter("id", productIdInt);
        List<ProductAccountUserTemplate> productList = (List<ProductAccountUserTemplate>) query.list();
        if (productList != null && !productList.isEmpty()) {
            return productList.get(0);
        }
        return null;
    }
   

    public ProductAccountUserTemplate getProductAccountUserTemplateById(VbUser vbUser, AgencyProduct product, Account account) {
        Query query = sessionFactory.getCurrentSession().createQuery("select t from ProductAccountUserTemplate t where t.productId = :product and t.userId = :user and t.accountId = :account");
        query.setParameter("product", product);       
        query.setParameter("account", account);       
        query.setParameter("user", vbUser);   
        List<ProductAccountUserTemplate> list = query.list();
        if(list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
}