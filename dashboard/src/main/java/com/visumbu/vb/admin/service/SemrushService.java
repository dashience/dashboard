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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author netphenix
 */
@Service("semrushService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SemrushService {

    public static String SEMRUSH_URL = "http://api.semrush.com/";
    public static String API_KEY;
    public static String PROJECT_ID;

    public Map getData(String dataSetReportName, String key, String level, String region, String domain, Date startDate, Date endDate, String apiKey, String projectId) throws MalformedURLException, IOException {
        API_KEY = apiKey;
        PROJECT_ID = projectId;
        if (dataSetReportName.equalsIgnoreCase("position")) {
            String data = getDataFromUrl(getUrl(dataSetReportName, key, level, region, domain, startDate, endDate));
            if (level.equalsIgnoreCase("tracking_position_organic") || level.equalsIgnoreCase("tracking_position_adwords")) {
                return getTrackingPosition(data);
            } else {
                return getCompetitorsDiscovery(data);
            }
        } else {
            return parseData(getUrl(dataSetReportName, key, level, region, domain, startDate, endDate));
        }
    }

    public String getUrl(String dataSetReportName, String key, String level, String region, String domain, Date startDate, Date endDate) {
        String startDateStr = DateUtils.dateToString(startDate, "yyyyMMDD");
        String endDateStr = DateUtils.dateToString(endDate, "yyyyMMDD");
        String url;
        if (dataSetReportName.equalsIgnoreCase("position")) {
            url = SEMRUSH_URL + "reports/v1/projects/" + PROJECT_ID + "/tracking?key=" + API_KEY + "&action=report&type=" + level + "&url=*.championwindow.com&url_type=rootdomain";
        } else {
            String dateStr = DateUtils.dateToString(endDate, "yyyyMM") + "15";
            url = SEMRUSH_URL + "?type=" + level + "&key=" + key + "&domain=" + domain + "&export_escape=1&database=" + region + "&display_date=" + dateStr;
        }
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
//        System.out.println(ss.getData(API_KEY, level, region, domain, startDate, endDate));
    }

    private Map getTrackingPosition(String responseData) {
        try {
            List<Map<String, Object>> data = new ArrayList();
            System.out.println("response  data ------->" + responseData);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(responseData);
            JSONObject array = (JSONObject) jsonObj;
            Map<String, Object> allData = (Map<String, Object>) array.get("data");
            for (Entry<String, Object> pair : allData.entrySet()) {
                int count = 0;
                Map<String, Object> requiredData = (Map<String, Object>) pair.getValue();
                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("keyword", requiredData.get("Ph"));
                reviewData.put("CPC", requiredData.get("Cp"));
                reviewData.put("volumne", requiredData.get("Nq"));
                Map<String, Object> positionByDate = (Map<String, Object>) requiredData.get("Dt");
                int size = positionByDate.size();
                for (Entry<String, Object> position : positionByDate.entrySet()) {
                    if (count == size) {
                        Map<String, Object> urlPosition = new HashMap<>();
                        urlPosition.put("position", position.getValue());
                        Map.Entry<String, Object> entry = urlPosition.entrySet().iterator().next();
                        reviewData.put("position", entry.getValue());
                    }
                }
                Map<String, Object> difference1 = (Map<String, Object>) requiredData.get("Diff1");
                Map.Entry<String, Object> entry = difference1.entrySet().iterator().next();
                reviewData.put("diff1", entry.getValue());
                Map<String, Object> difference7 = (Map<String, Object>) requiredData.get("Diff7");
                entry = difference7.entrySet().iterator().next();
                reviewData.put("diff7", entry.getValue());
                Map<String, Object> difference30 = (Map<String, Object>) requiredData.get("Diff30");
                entry = difference30.entrySet().iterator().next();
                reviewData.put("diff30", entry.getValue());
                data.add(reviewData);
            }
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("data", data);
            return returnMap;

        } catch (ParseException ex) {
            Logger.getLogger(SemrushService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Map getCompetitorsDiscovery(String responseData) {
        try {
            List<Map<String, Object>> data = new ArrayList();
            System.out.println("response  data ------->" + responseData);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(responseData);
            JSONObject array = (JSONObject) jsonObj;
            Map<String, Object> allData = (Map<String, Object>) array.get("data");
            for (Entry<String, Object> dataPair : allData.entrySet()) {
                int count = 0;
                Map<String, Object> requiredData = (Map<String, Object>) dataPair.getValue();
                Map<String, Object> reviewData = new HashMap<>();
                Map<String, Object> dataByDate = (Map<String, Object>) requiredData.get("Dt");
                int dataSize = dataByDate.size();
                for (Entry<String, Object> value : dataByDate.entrySet()) {
                    if (count == dataSize - 1) {
                        Map<String, Object> byDateMetrices = (Map<String, Object>) value.getValue();
                        reviewData.put("keywords", byDateMetrices.get("Mc"));
                        reviewData.put("avgPosition", byDateMetrices.get("Av"));
                        reviewData.put("visibility", byDateMetrices.get("Cl"));
                        reviewData.put("estimatedTraffic", byDateMetrices.get("Tr"));
                    }
                    count++;
                }
                reviewData.put("domain", requiredData.get("Ur"));
                data.add(reviewData);
            }
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("data", data);
            return returnMap;

        } catch (ParseException ex) {
            Logger.getLogger(SemrushService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
