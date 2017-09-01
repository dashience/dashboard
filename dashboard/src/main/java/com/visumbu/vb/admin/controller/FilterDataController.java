/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import static com.visumbu.vb.admin.controller.ProxyController.log;
import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author jp
 */
@Controller
@RequestMapping("filterData")
public class FilterDataController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UiService uiService;

    @RequestMapping(value = "getFilter/{dataType}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map create(HttpServletRequest request, HttpServletResponse response, @PathVariable String dataType) {
        String query = "";

        if (dataType.equalsIgnoreCase("salesType")) {
            query = "select distinct sales_type as fieldName,  sales_type as displayName from skyzone_wtd ";
        } else if (dataType.equalsIgnoreCase("country")) {
            query = "select distinct country as fieldName,  country as displayName from skyzone_wtd ";
        } else if (dataType.equalsIgnoreCase("state")) {
            query = "select distinct state as fieldName,  state as displayName from skyzone_wtd ";
        } else if (dataType.equalsIgnoreCase("city")) {
            query = "select distinct city as fieldName,  city as displayName from skyzone_wtd ";
        } else if (dataType.equalsIgnoreCase("store")) {
            query = "select distinct location as fieldName,  location as displayName from skyzone_wtd ";
        } else if (dataType.equalsIgnoreCase("category")) {
            query = "select distinct category as fieldName,  category as displayName from skyzone_center_edge ";
        } else if (dataType.equalsIgnoreCase("subCategory")) {
            query = "select distinct sub_category as fieldName,  sub_category as displayName from skyzone_center_edge ";
        } else if (dataType.equalsIgnoreCase("brand")) {
            query = "select distinct brand as fieldName,  brand as displayName from auto ";
        } else if (dataType.equalsIgnoreCase("model")) {
            query = "select distinct model as fieldName,  model as displayName from auto ";
        } else if (dataType.equalsIgnoreCase("yearOfRegistration")) {
            query = "select distinct yearOfRegistration as fieldName,  yearOfRegistration as displayName from auto order by 1";
        } else if (dataType.equalsIgnoreCase("kilometer")) {
            query = "select distinct kilometer as fieldName,  kilometer as displayName from auto order by 1";
        } else if (dataType.equalsIgnoreCase("price")) {
            query = "select distinct price as fieldName,  price as displayName from auto order by 1";
        } else if (dataType.equalsIgnoreCase("seller")) {
            query = "select distinct seller as fieldName,  seller as displayName from auto ";
        } else if (dataType.equalsIgnoreCase("vehicleType")) {
            query = "select distinct vehicleType as fieldName,  vehicleType as displayName from auto ";
        } else if (dataType.equalsIgnoreCase("offerType")) {
            query = "select distinct offerType as fieldName,  offerType as displayName from auto ";
        } else if (dataType.equalsIgnoreCase("fuelType")) {
            query = "select distinct fuelType as fieldName,  fuelType as displayName from auto ";
        } else if (dataType.equalsIgnoreCase("gearBox")) {
            query = "select distinct gearBox as fieldName,  gearBox as displayName from auto ";
        }

        return getSqlData(request, response, query);
    }

    private Map getSqlData(HttpServletRequest request, HttpServletResponse response, String query) {
        try {

            String url = "../dbApi/admin/dataSet/getData";
            Integer port = 80;
            if (request != null) {
                port = request.getServerPort();
            }

            String localUrl = "http://localhost/";
            if (request != null) {
                localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";
            }
            if (url.startsWith("../")) {
                url = url.replaceAll("\\.\\./", localUrl);
            }

            String dashboardFilter = request.getParameter("dashboardFilter");

            if (isNullOrEmpty(dashboardFilter)) {
                dashboardFilter = "";
            }
            dashboardFilter = getQueryFilter(dashboardFilter);

            if (dashboardFilter != null && !isNullOrEmpty(dashboardFilter.trim())) {
                if (query != null) {
                    if (query.indexOf("where") > 0) {
                        query += " " + dashboardFilter;
                    } else {
                        query += " where " + dashboardFilter;
                    }
                }
            }
            try {
                query = URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
            }
            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            valueMap.put("query", Arrays.asList(query));
            valueMap.put("driver", Arrays.asList("com.mysql.jdbc.Driver"));
            valueMap.put("username", Arrays.asList("root"));
            valueMap.put("password", Arrays.asList(""));
            valueMap.put("connectionUrl", Arrays.asList("jdbc:mysql://localhost/retail_prod"));

            String data = Rest.getData(url, valueMap);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            Map returnData = JsonSimpleUtils.toMap((JSONObject) jsonObj);
            return returnData;
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty() || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("undefined");
    }

    private static String getQueryFilter(String jsonDynamicFilter) {
        try {
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(jsonDynamicFilter);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = (Map<String, Object>) JsonSimpleUtils.jsonToMap(json);
            List<String> queryString = new ArrayList<>();
            for (Map.Entry<String, Object> entry : jsonToMap.entrySet()) {
                String key = entry.getKey();
                List<String> value = (List<String>) entry.getValue();
                List<String> innerQuery = new ArrayList<>();
                for (Iterator<String> iterator = value.iterator(); iterator.hasNext();) {
                    String valueString = iterator.next();
                    innerQuery.add(key + " = " + "'" + valueString + "'");
                }
                String join = String.join(" OR ", innerQuery);
                // String output = key + " in " + join;
                queryString.add(" ( " + join + " ) ");
            }
            return String.join(" AND ", queryString);

        } catch (ParseException ex) {
            // java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
