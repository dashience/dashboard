/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.google.common.base.CaseFormat;
import com.visumbu.vb.admin.dao.DealerDao;
import com.visumbu.vb.admin.dao.bean.DealerAccountBean;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.Rest;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("semrushService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SemrushService {

    public static String SEMRUSH_URL = "http://api.semrush.com/";
    public static String API_KEY = "2b88db8e48f0f8b1fdcf3007cfae388a";

    public Map getData(String key, String level, String region, String domain, Date startDate, Date endDate) throws MalformedURLException, IOException {
        return parseData(getUrl(key, level, region, domain, startDate, endDate));
    }

    public String getUrl(String key, String level, String region, String domain, Date startDate, Date endDate) {
        String dateStr = DateUtils.dateToString(endDate, "yyyyMM") + "15";
        String url = SEMRUSH_URL + "?type=" + level + "&key=" + key + "&domain=" + domain + "&export_escape=1&database=" + region + "&display_date=" + dateStr;
        return url;
    }

    public String getDataFromUrl(String url) {
        return Rest.getData(url);
    }

    public Map parseData(String urlString) throws MalformedURLException, IOException {
        System.out.println(urlString);
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(';').withQuote('"');
        URL url = new URL(urlString);
        final Reader reader = new InputStreamReader(new BOMInputStream(url.openStream()));

        //initialize the CSVParser object
        CSVParser parser = new CSVParser(reader, format);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Map.Entry<String, Integer> entrySet : headerMap.entrySet()) {
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            String fieldname = key.toLowerCase().replaceAll(" ", "_");
            ColumnDef columnDef = new ColumnDef(fieldname, "string", key);
            columnDefs.add(columnDef);

        }
        Map returnMap = new HashMap();
        System.out.println("Column Defs");
        System.out.println(columnDefs);
        returnMap.put("columnDefs", columnDefs);
        List<Map<String, Object>> data = new ArrayList<>();
        System.out.println(headerMap);
        for (CSVRecord record : parser) {
            Map<String, Object> dataMap = new HashMap<>();
            for (Map.Entry<String, Integer> entrySet : headerMap.entrySet()) {
                String key = entrySet.getKey();
                String fieldname = key.toLowerCase().replaceAll(" ", "_");
                dataMap.put(fieldname, record.get(key));
            }
            data.add(dataMap);
        }

        returnMap.put("data", data);
        //close the parser
        parser.close();
        return returnMap;
    }

    public static void main(String argv[]) throws IOException {
        SemrushService ss = new SemrushService();
        String level = "domain_ranks";
        String region = "us";
        Date startDate = DateUtils.get30DaysBack();
        Date endDate = DateUtils.get30DaysBack();
        String domain = "seobook.com";
        System.out.println(ss.getData(API_KEY, level, region, domain, startDate, endDate));
    }

}
