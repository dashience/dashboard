/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
public class AuthFilter implements Filter {

    private List<String> urlList;

    FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig config) throws ServletException {
        String urls = config.getInitParameter("avoid-urls");
        StringTokenizer token = new StringTokenizer(urls, ",");

        urlList = new ArrayList<String>();
        this.filterConfig = config;
        while (token.hasMoreTokens()) {
            urlList.add(token.nextToken());

        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession session = httpRequest.getSession();
            String url = httpRequest.getServletPath();
            System.out.println("URL -> " + url);
            String contextPath = httpRequest.getContextPath();
            boolean allowRequest = false;
            String fullUrl = httpRequest.getRequestURI();
            System.out.println("Full URL -> " + fullUrl);
            if (session != null) {
                if (session.getAttribute("isAuthenticated") != null
                        && (boolean) session.getAttribute("isAuthenticated")
                        && session.getAttribute("username") != null) {
                    allowRequest = true;
                    chain.doFilter(request, response);
                    return;
                }
            }
            if (allowRequest == false) {
                if (fullUrl.endsWith("login") || fullUrl.endsWith("logout")) {
                    System.out.println("Login requests");
                    allowRequest = true;
                }
                if (fullUrl.endsWith(".js") || fullUrl.endsWith(".css") || fullUrl.endsWith("png") || fullUrl.endsWith("jpg")) {
                    System.out.println("static js/css/img files");
                    allowRequest = true;
                }
                if (url.endsWith("/index.html")) {
                    System.out.println("allow index file");
                    allowRequest = true;
                }
                if (url.endsWith("/static/index.html")) {
                    System.out.println("dont allow authenticated file");
                    allowRequest = false;
                }
            }
            if (allowRequest == false) {
                System.out.println("Allowd false");
                System.out.println("Context path  " + contextPath);
                if (url.contains("admin") || url.contains("static/index.html")) {
                    httpResponse.sendRedirect(contextPath + "/index.html");
                }
            } else {
                System.out.println("Allowd true");
                chain.doFilter(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", ex);
            request.getRequestDispatcher("/WEB-INF/views/jsp/error.jsp")
                    .forward(request, response);
        }

    }

    @Override
    public void destroy() {

    }

}
