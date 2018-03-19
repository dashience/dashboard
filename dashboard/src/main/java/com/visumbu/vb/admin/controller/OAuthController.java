package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.oauth.service.Sheduler;
import com.visumbu.vb.admin.oauth.service.OAuthSelector;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping(value = "/social")
@Component
public class OAuthController {

    @Autowired
    OAuthSelector oAuthSelector;
    @Autowired
    Sheduler Sheduler;

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthController.class);
    String successUrl = "http://tellyourstory.lino.com:8080/dashboard/admin/social/success";
    private MultiValueMap<String, Object> oAuthData;

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView signin(HttpServletRequest request,
            HttpServletResponse response) {
        
        String authorizeUrl = "http://tellyourstory.lino.com:8080/dashboard/admin/social/error";
        try {
            oAuthData = oAuthSelector.generateView(request);
            authorizeUrl = (String) oAuthData.getFirst("authorizeUrl");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
                true);

        return new ModelAndView(redirectView);
    }

    @RequestMapping(value = "/callback", params = {"code"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView tokenGenetator(@RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            oAuthData.add("code", code);
            System.out.println("code----->" + code);
            oAuthSelector.getTokenDetails(oAuthData);
            System.out.println("code----->" + code);
            RedirectView redirectView = new RedirectView(successUrl, true, true,
                    true);

            return new ModelAndView(redirectView);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/callback", params = {"code", "state"}, method = RequestMethod.GET)
    public ModelAndView tokenGenetator(@RequestParam("code") String code, @RequestParam("state") String state,
            HttpServletRequest request,
            HttpServletResponse response) {
        System.out.println("code----->" + code);
        try {
            oAuthData.add("code", code);
            oAuthSelector.getTokenDetails(oAuthData);
            RedirectView redirectView = new RedirectView(successUrl, true, true,
                    true);

            return new ModelAndView(redirectView);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/callback", params = {"oauth_token", "oauth_verifier"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView oAuth1TokenGenerator(@RequestParam("oauth_token") String oauth_token, @RequestParam("oauth_verifier") String oauth_verifier,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            oAuthData.add("oauth_token", oauth_token);
            oAuthData.add("oauth_verifier", oauth_verifier);
            oAuthSelector.getTokenDetails(oAuthData);
            RedirectView redirectView = new RedirectView(successUrl, true, true,
                    true);

            return new ModelAndView(redirectView);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    @ResponseBody
    public String errorNotifier() {
        return "Error Occured Check if Entered details are correct";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    @ResponseBody
    public String successNotifier() {
        return "The tokens are generated successfully";
    }
}
