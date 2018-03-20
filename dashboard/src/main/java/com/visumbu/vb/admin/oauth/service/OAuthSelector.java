/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.oauth.service;

import com.visumbu.vb.model.TokenDetails;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author dashience
 */
public interface OAuthSelector  {
    
   public MultiValueMap<String, Object> generateView(HttpServletRequest request) throws Exception;
   public TokenDetails getTokenDetails(MultiValueMap<String, Object> dataMap) throws Exception;
}
