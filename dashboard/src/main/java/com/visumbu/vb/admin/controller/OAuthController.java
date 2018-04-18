package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.scheduler.service.Sheduler;
import com.visumbu.vb.admin.oauth.service.OAuthSelectorImpl;
import com.visumbu.vb.admin.oauth.service.TokenTemplate;
import com.visumbu.vb.admin.service.TokenService;
import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.TokenDetails;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Lino
 */
@RequestMapping(value = "/social")
@Component
@Scope("session")
public class OAuthController {

    @Autowired
    OAuthSelectorImpl oAuthSelector;
    @Autowired
    Sheduler Sheduler;
    @Autowired
    TokenService tokenService;
    @Autowired
    UiService uiService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthController.class);
    String successUrl;
    public static MultiValueMap<String, Object> oAuthData;

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView signin(HttpServletRequest request,
            HttpServletResponse response) {
        String domainName = request.getParameter("domainName");
        successUrl = "http://" + domainName + "/dashboard/admin/social/success";
        String authorizeUrl = "http://" + domainName + "/dashboard/admin/social/error";
        try {
            HttpSession session = request.getSession();
            System.out.println("session id-------->" + session.getId());
            oAuthData = oAuthSelector.generateOAuthUrl(request);
            authorizeUrl = (String) oAuthData.getFirst("authorizeUrl");
            System.out.println("oauthData------>" + oAuthData);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
                true);

        return new ModelAndView(redirectView);
    }

    @RequestMapping(value = "/callback", params = {"code"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView tokenGenerator(@RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        System.out.println("session id-------->" + session.getId());
        try {
            System.out.println("code------------->" + code);
            System.out.println("oAuthData------------->" + oAuthData);
            oAuthData.add("code", code);
            TokenTemplate tokenTemplate = oAuthSelector.getTokenDetails(oAuthData);
            tokenService.insertIntoDb(oAuthData, tokenTemplate, request);
            RedirectView redirectView = new RedirectView(successUrl, true, true,
                    true);

            return new ModelAndView(redirectView);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/callback", params = {"code", "state"}, method = RequestMethod.GET)
    public ModelAndView tokenGenerator(@RequestParam("code") String code, @RequestParam("state") String state,
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        System.out.println("session id-------->" + session.getId());
        try {
            oAuthData.add("code", code);
            TokenTemplate tokenTemplate = oAuthSelector.getTokenDetails(oAuthData);
            tokenService.insertIntoDb(oAuthData, tokenTemplate, request);
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
        HttpSession session = request.getSession();
        System.out.println("session id-------->" + session.getId());
        try {
            oAuthData.add("oauth_token", oauth_token);
            oAuthData.add("oauth_verifier", oauth_verifier);
            TokenTemplate tokenTemplate = oAuthSelector.getTokenDetails(oAuthData);
            tokenService.insertIntoDb(oAuthData, tokenTemplate, request);
            RedirectView redirectView = new RedirectView(successUrl, true, true,
                    true);

            return new ModelAndView(redirectView);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/saveToken", method = RequestMethod.POST)
    @ResponseBody
    public TokenDetails saveToken(@RequestBody TokenDetails tokenDetails) {
        tokenService.insertTokenDetails(tokenDetails);
        return tokenDetails;
    }

    @RequestMapping(value = "/oauthStatus", method = RequestMethod.PUT)
    @ResponseBody
    public DataSource updateOauthStatus(@RequestBody DataSource dataSource) {
        return uiService.update(dataSource);
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
