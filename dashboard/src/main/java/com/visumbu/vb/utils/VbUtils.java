/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.google.common.base.CaseFormat;
import com.visumbu.vb.bean.Permission;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthPermission;
import com.visumbu.vb.bean.map.auth.SecurityAuthRoleBean;
import com.visumbu.vb.bean.map.auth.SecurityTokenBean;
import com.visumbu.vb.model.UserPermission;
import com.visumbu.vb.model.VbUser;
import static com.visumbu.vb.utils.Rest.getData;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author user
 */
public class VbUtils {

    public static String getPageName(String url) {

        String baseName = FilenameUtils.getBaseName(url);
        String extension = FilenameUtils.getExtension(url);

        System.out.println("Basename : " + baseName);
        System.out.println("extension : " + extension);
        if (extension != null && !extension.isEmpty()) {
            return baseName + "." + extension;
        }
        return baseName;
    }

    public static Long toLong(String longVal) {
        if (longVal == null) {
            return 0L;
        }
        Long returnValue = 0L;
        try {
            returnValue = Long.parseLong(longVal);
        } catch (Exception e) {
            returnValue = 0L;
        }
        return returnValue;
    }

    public static Integer toInteger(String integer) {
        if (integer == null) {
            return 0;
        }
        Integer returnValue = 0;
        try {
            returnValue = Integer.parseInt(integer);
        } catch (Exception e) {
            returnValue = 0;
        }
        return returnValue;
    }

    public static String getDomainName(String url) {
        // Alternative Solution
        // http://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException ex) {
            Logger.getLogger(VbUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Permission getPermissions(SecurityAuthBean authData) {
        List<SecurityAuthRoleBean> roles = authData.getRoles();
        Permission permission = new Permission();
        for (Iterator<SecurityAuthRoleBean> iterator = roles.iterator(); iterator.hasNext();) {
            SecurityAuthRoleBean role = iterator.next();
            List<SecurityAuthPermission> permissions = role.getPermissions();
            for (Iterator<SecurityAuthPermission> iterator1 = permissions.iterator(); iterator1.hasNext();) {
                SecurityAuthPermission authPermission = iterator1.next();
                permission.setPermission(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, authPermission.getName()), Boolean.TRUE);
            }
        }
        return permission;
    }

    public static Permission getPermissions(VbUser user, List<UserPermission> userPermissions) {

        Permission permission = new Permission();
        for (Iterator<UserPermission> iterator = userPermissions.iterator(); iterator.hasNext();) {
            UserPermission userPermission = iterator.next();
                permission.setPermission(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, userPermission.getPermissionId().getPermissionName().toLowerCase().replaceAll(" ", "_")), userPermission.getStatus());
//            permission.setPermission(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, userPermission.getPermissionId().getPermissionName().toLowerCase().replaceAll(" ", "_")), Boolean.TRUE);
        }
//        if (user.getIsAdmin() != null && user.getIsAdmin()) {
//            permission.setViewAgency(Boolean.TRUE);
//        }
//        if (user.getAgencyId() == null || (user.getIsAdmin() != null && user.getIsAdmin())) {
//            permission.setViewAgency(Boolean.TRUE);
//        }
        return permission;
    }
}
