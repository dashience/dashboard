/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.dao.BaseDao;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dashience
 */
@Transactional
@Repository("TokenDao")
public class TokenDao extends BaseDao {
    
}