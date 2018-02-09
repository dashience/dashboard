/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.Rest;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author lino
 */
@Service("googleMyBusinessService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GoogleMyBusinessService {

    private static HashMap<String, String> locationListArray;

    private static String gmbAccessToken;

    public List<Map<String, Object>> get(String dataSetReportName, String gmbRefreshToken, String gmbAccountId, String clientId, String clientSecret,
            Date startDate, Date endDate, String timeSegment, String productSegment) {
        gmbAccessToken = getAccessToken(gmbAccountId, gmbRefreshToken, clientId, clientSecret);
        boolean locationData = true;
        while (locationData) {
            if (!locationListArray.isEmpty()) {
                locationData = false;
            }
        }
        if ("reportInsights".equalsIgnoreCase(dataSetReportName)) {
            return getInsightsReport(gmbRefreshToken, gmbAccountId, clientId, clientSecret, startDate, endDate);
        } else if ("overallReportInsights".equalsIgnoreCase(dataSetReportName)) {
            return customerSearch(gmbRefreshToken, gmbAccountId, clientId, clientSecret, startDate, endDate);
        } else if ("dailyReportInsights".equalsIgnoreCase(dataSetReportName)) {
            return dailyLocationData(gmbRefreshToken, gmbAccountId, clientId, clientSecret, startDate, endDate);
        } else {
            return getPhoneCalls(gmbRefreshToken, gmbAccountId, clientId, clientSecret, startDate, endDate, timeSegment);
        }
    }

    private String getAccessToken(String gmbAccountId, String gmbRefreshToken, String clientId, String clientSecret) {
        try {
            String url = "https://www.googleapis.com/oauth2/v4/token?refresh_token=" + gmbRefreshToken + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=refresh_token";
            String tokenData = Rest.postRawForm(url, "{}");
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(tokenData);
            JSONObject array = (JSONObject) jsonObj;
            String accessToken = (String) array.get("access_token");
            return accessToken;
        } catch (ParseException | IOException ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void getLocations(String gmbRefreshToken, String gmbAccountId, String gmbClientId, String gmbClientSecret) {
        JSONObject array = null;
        try {
            ArrayList<String> locationList = new ArrayList();
            gmbAccessToken = getAccessToken(gmbAccountId, gmbRefreshToken, gmbClientId, gmbClientSecret);
            String url = "https://mybusiness.googleapis.com/v4/accounts/" + gmbAccountId + "/locations?access_token=" + gmbAccessToken;
            String locationData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(locationData);
            array = (JSONObject) jsonObj;
            List<Map<String, Object>> LocationsArray = (List<Map<String, Object>>) array.get("locations");
            HashMap<String, String> locationMap = new HashMap<>();
            for (Object singleLocation : LocationsArray) {
                Map<String, Object> locationObject = (Map<String, Object>) singleLocation;
                Map<String, String> locationAddress = (Map<String, String>) locationObject.get("address");
                String locationName = "\"" + (String) locationObject.get("name") + "\"";
                locationMap.put(locationName, locationAddress.get("postalCode"));
            }
            locationListArray = locationMap;

        } catch (Exception ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Map<String, Object>> getPhoneCalls(String gmbRefreshToken, String gmbAccountId, String gmbClientId, String gmbClientSecret, Date startDate, Date endDate, String timeSegment) {
        try {
            String url = "https://mybusiness.googleapis.com/v4/accounts/" + gmbAccountId + "/locations:reportInsights?access_token=" + gmbAccessToken;
            List<Map<String, Object>> returnMap = new ArrayList<>();
            String startDateStr = DateUtils.dateToString(startDate, "yyyy-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "yyyy-MM-dd");
            String requestBody = ",\"basicRequest\":{\"metricRequests\":{\"metric\":\"ACTIONS_PHONE\",\"options\":[\"" + timeSegment + "\"]},\"timeRange\":{\"startTime\":\"" + startDateStr + "T00:00:00.001Z\",\"endTime\":\"" + endDateStr + "T23:59:00.000Z\"}}}";
            String responseString = getAppendedResponse(requestBody, url);
            if (responseString == null) {
                return null;
            }
            JSONParser parser = new JSONParser();
            Object jsonObject = parser.parse(responseString);
            JSONObject array = (JSONObject) jsonObject;
            Map<String, Object> arrangeData = new LinkedHashMap<>();
            List<Map<String, Object>> locationMetrics = (List<Map<String, Object>>) array.get("locationMetrics");
            if ("BREAKDOWN_DAY_OF_WEEK".equals(timeSegment)) {
                for (Map<String, Object> location : locationMetrics) {
                    List<Map<String, Object>> MatricValues = (List<Map<String, Object>>) location.get("metricValues");
                    Map<String, Object> metricValue = MatricValues.get(0);
                    if (metricValue.size() != 1) {
                        List<Map<String, Object>> dimensionalValues = (List<Map<String, Object>>) metricValue.get("dimensionalValues");
                        for (Map<String, Object> dimensionalValue : dimensionalValues) {
                            Map<String, String> metricName = (Map<String, String>) dimensionalValue.get("timeDimension");
                            for (int i = 1; i < 8; i++) {
                                if (DayOfWeek.of(i).toString().equalsIgnoreCase(metricName.get("dayOfWeek"))) {
                                    String dayOfWeek = DayOfWeek.of(i).toString();
                                    if (!arrangeData.containsKey(dayOfWeek)) {
                                        arrangeData.put(dayOfWeek, dimensionalValue.get("value"));
                                    } else {
                                        arrangeData.put(dayOfWeek, Integer.parseInt((String) arrangeData.get(dayOfWeek)) + Integer.parseInt((String) dimensionalValue.get("value")));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                for (int i = 1; i < 8; i++) {
                    String dayOfWeek = DayOfWeek.of(i).toString();
                    String titleCase = dayOfWeek.charAt(0)+dayOfWeek.substring(1, dayOfWeek.length()).toLowerCase();
                    if (!arrangeData.containsKey(dayOfWeek)) {
                        arrangeData.put(titleCase, 0);
                    }else{
                        arrangeData.put(titleCase, arrangeData.get(dayOfWeek));
                        arrangeData.remove(dayOfWeek);
                    }
                }
                for (Map.Entry<String, Object> entry : arrangeData.entrySet()) {
                    Map<String, Object> callData = new HashMap<>();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    callData.put("Day", key);
                    callData.put("No of Calls", value);
                    returnMap.add(callData);
                }
            } else {
                for (Map<String, Object> location : locationMetrics) {
                    List<Map<String, Object>> MatricValues = (List<Map<String, Object>>) location.get("metricValues");
                    Map<String, Object> metricValue = MatricValues.get(0);
                    if (metricValue.size() != 1) {
                        List<Map<String, Object>> dimensionalValues = (List<Map<String, Object>>) metricValue.get("dimensionalValues");
                        for (Map<String, Object> dimensionalValue : dimensionalValues) {
                            Map<String, Object> metricName = (Map<String, Object>) dimensionalValue.get("timeDimension");
                            Map<String, Object> hourValue = (Map<String, Object>) metricName.get("timeOfDay");
                            Long hour = (Long) hourValue.get("hours");
                            int intHour = hour.intValue();
                            String stringValue = (String) dimensionalValue.get("value");
                            int intValue = (int) Integer.parseInt(stringValue);
                            for (int i = 1; i < 25; i++) {
                                if (intHour == i) {
                                    String key = Integer.toString(i);
                                    if (arrangeData.containsKey(key)) {
                                        arrangeData.replace(key, (int) arrangeData.get(key) + intValue);
                                    } else {
                                        arrangeData.put(key, intValue);
                                    }
                                }
                            }
                        }
                    }
                }
                for (Map.Entry<String, Object> entry : arrangeData.entrySet()) {
                    Map<String, Object> callData = new HashMap<>();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    callData.put("Hour", key);
                    callData.put("noOfCalls", value);
                    returnMap.add(callData);
                }
            }
            return returnMap;
        } catch (ParseException ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Map<String, Object>> getInsightsReport(String gmbRefreshToken, String gmbAccountId, String gmbClientId, String gmbClientSecret, Date startDate, Date endDate) {
        try {
            String url = "https://mybusiness.googleapis.com/v4/accounts/" + gmbAccountId + "/locations:reportInsights?access_token=" + gmbAccessToken;
            List<Map<String, Object>> returnMap = new ArrayList<>();
            String startDateStr = DateUtils.dateToString(startDate, "yyyy-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "yyyy-MM-dd");
            String requestBody = ",\"basicRequest\":{\"metricRequests\":{\"metric\":\"ALL\",\"options\":[\"AGGREGATED_TOTAL\"]},\"timeRange\":{\"startTime\":\"" + startDateStr + "T00:00:00.001Z\",\"endTime\":\"" + endDateStr + "T23:59:00.000Z\"}}}";
            String responseString = getAppendedResponse(requestBody, url);
            System.out.println("Insights Response -->" + responseString);
            if (responseString == null) {
                return null;
            }
            JSONParser parser = new JSONParser();
            Object jsonObject = parser.parse(responseString);
            JSONObject array = (JSONObject) jsonObject;
            List<Map<String, Object>> locationMetrics = (List<Map<String, Object>>) array.get("locationMetrics");
            for (Map<String, Object> location : locationMetrics) {
                Map<String, Object> data = new HashMap<>();
                List<Map<String, Object>> MatricValues = (List<Map<String, Object>>) location.get("metricValues");
                for (Map<String, Object> metrics : MatricValues) {
                    String metricName = (String) metrics.get("metric");
                    if (metricName != null) {
                        Map<String, Object> value = (Map<String, Object>) metrics.get("totalValue");
                        String metricValue = (String) value.get("value");
                        data.put(metricName, metricValue);
                    }
                }
                data.put("location", locationListArray.get("\"" + location.get("locationName") + "\""));
                returnMap.add(data);
            }
            return returnMap;
        } catch (Exception ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Map<String, Object>> dailyLocationData(String gmbRefreshToken, String gmbAccountId, String gmbClientId, String gmbClientSecret, Date startDate, Date endDate) {
        System.out.println("daily Location called");
        try {
            String url = "https://mybusiness.googleapis.com/v4/accounts/" + gmbAccountId + "/locations:reportInsights?access_token=" + gmbAccessToken;

            String startDateStr = DateUtils.dateToString(startDate, "yyyy-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "yyyy-MM-dd");
            String requestBody = ",\"basicRequest\":{\"metricRequests\":{\"metric\":\"ALL\",\"options\":[\"AGGREGATED_DAILY\"]},\"timeRange\":{\"startTime\":\"" + startDateStr + "T00:00:00.001Z\",\"endTime\":\"" + endDateStr + "T23:59:00.000Z\"}}}";
            String responseString = getAppendedResponse(requestBody, url);
            if (responseString == null) {
                return null;
            }
            JSONParser parser = new JSONParser();
            Object jsonObject = parser.parse(responseString);
            JSONObject array = (JSONObject) jsonObject;

            List<Map<String, Object>> dataMap = new ArrayList<>();
            int i = 0;
            List<Map<String, Object>> locationMetrics = (List<Map<String, Object>>) array.get("locationMetrics");
            LinkedList<String> dateTimeValue = new LinkedList();
            LinkedList<String> metrices = new LinkedList();
            Map<String, Object> dateValue = new HashMap<>();
            int loop2 = 0;
            int loop1 = 0;
            for (Map<String, Object> location : locationMetrics) {
                loop1++;
                List<Map<String, Object>> MatricValues = (List<Map<String, Object>>) location.get("metricValues");
                i = 0;
                for (Map<String, Object> metrics : MatricValues) {
                    if (metrics.containsKey("metric")) {
                        Map<String, Object> aggregatedDate = new HashMap<>();
                        if (loop1 > 1) {
                            try {
                                aggregatedDate = (Map<String, Object>) dateValue.get(metrices.get(i));
                                i++;
                            } catch (Exception e) {
                                System.out.println("aggregated exception--------->" + e);
                            }
                        }
                        String metricName = (String) metrics.get("metric");
                        if (loop1 == 1) {
                            metrices.add(metricName);
                        }
                        List<Map<String, Object>> value = (List<Map<String, Object>>) metrics.get("dimensionalValues");
                        for (Map<String, Object> perDayValues : value) {
                            String metricValue = null;
                            String startTime = null;
                            Map<String, Object> timeDimension = (Map<String, Object>) perDayValues.get("timeDimension");
                            Map<String, Object> timeRange = (Map<String, Object>) timeDimension.get("timeRange");
                            startTime = (String) timeRange.get("startTime");
                            startTime = startTime.substring(0, startTime.indexOf("T"));
                            metricValue = null;
                            if (perDayValues.containsKey("value")) {
                                metricValue = (String) perDayValues.get("value");
                            } else {
                                metricValue = Integer.toString(0);
                            }
                            if (loop1 == 1) {
                                aggregatedDate.put(startTime, metricValue);

                            }
                            if (loop2 == 0) {
                                dateTimeValue.add(startTime);

                            }
                            if (loop1 > 1) {
                                try {
                                    aggregatedDate.put(startTime, Integer.toString(Integer.parseInt((String) aggregatedDate.get(startTime)) + Integer.parseInt(metricValue)));
                                } catch (NumberFormatException numberFormatException) {
                                    System.out.println("exception---->" + numberFormatException);
                                }
                            }
                        }
                        loop2++;
                        dateValue.put(metricName, aggregatedDate);
                    }
                }
            }
            List<Map<String, Object>> returnDataMap = null;
            returnDataMap = new ArrayList<>();
            for (String date : dateTimeValue) {
                Map<String, Object> extractedData = new HashMap<>();
                extractedData.put("dateValue", date);
                for (Map.Entry<String, Object> entry : dateValue.entrySet()) {
                    String key = entry.getKey();
                    Map<String, Object> value = (Map<String, Object>) entry.getValue();
                    extractedData.put(key, value.get(date));
                }
                returnDataMap.add(extractedData);
            }
            return returnDataMap;
        } catch (Exception ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> sumOfLocationsData(String gmbRefreshToken, String gmbAccountId, String gmbClientId, String gmbClientSecret, Date startDate, Date endDate) {
        try {
            String url = "https://mybusiness.googleapis.com/v4/accounts/" + gmbAccountId + "/locations:reportInsights?access_token=" + gmbAccessToken;
            List<Map<String, Object>> returnMap = new ArrayList<>();
            String startDateStr = DateUtils.dateToString(startDate, "yyyy-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "yyyy-MM-dd");
            String requestBody = ",\"basicRequest\":{\"metricRequests\":{\"metric\":\"ALL\",\"options\":[\"AGGREGATED_TOTAL\"]},\"timeRange\":{\"startTime\":\"" + startDateStr + "T00:00:00.001Z\",\"endTime\":\"" + endDateStr + "T23:59:00.000Z\"}}}";
            String responseString = getAppendedResponse(requestBody, url);
            if (responseString == null) {
                return null;
            }
            JSONParser parser = new JSONParser();
            Object jsonObject = parser.parse(responseString);
            JSONObject array = (JSONObject) jsonObject;
            List<Map<String, Object>> locationMetrics = (List<Map<String, Object>>) array.get("locationMetrics");
            Map<String, Object> data = new HashMap<>();
            for (Map<String, Object> location : locationMetrics) {
                List<Map<String, Object>> MatricValues = (List<Map<String, Object>>) location.get("metricValues");
                if (data.isEmpty()) {
                    for (Map<String, Object> metrics : MatricValues) {
                        String metricName = (String) metrics.get("metric");
                        if (metricName != null) {
                            Map<String, Object> value = (Map<String, Object>) metrics.get("totalValue");
                            String metricValue = (String) value.get("value");
                            data.put(metricName, metricValue);
                        }
                    }
                } else {
                    for (Map<String, Object> metrics : MatricValues) {
                        String metricName = (String) metrics.get("metric");
                        if (metricName != null) {
                            Map<String, Object> value = (Map<String, Object>) metrics.get("totalValue");
                            String metricValue = (String) value.get("value");
                            data.put(metricName, Integer.parseInt((String) data.get(metricName)) + Integer.parseInt(metricValue));
                        }
                    }
                }
            }
            returnMap.add(data);
            return returnMap;
        } catch (Exception ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Map<String, Object>> customerSearch(String gmbRefreshToken, String gmbAccountId, String gmbClientId, String gmbClientSecret, Date startDate, Date endDate) {
        try {
            String url = "https://mybusiness.googleapis.com/v4/accounts/" + gmbAccountId + "/locations:reportInsights?access_token=" + gmbAccessToken;
            String startDateStr = DateUtils.dateToString(startDate, "yyyy-MM-dd");
            String endDateStr = DateUtils.dateToString(endDate, "yyyy-MM-dd");
            String requestBody = ",\"basicRequest\":{\"metricRequests\":[{\"metric\":\"QUERIES_DIRECT\",\"options\":[\"AGGREGATED_TOTAL\"]},{\"metric\":\"QUERIES_INDIRECT\",\"options\":[\"AGGREGATED_TOTAL\"]}],\"timeRange\":{\"startTime\":\"" + startDateStr + "T00:00:00.001Z\",\"endTime\":\"" + endDateStr + "T23:59:00.000Z\"}}}";
            String responseString = getAppendedResponse(requestBody, url);
            if (responseString == null) {
                return null;
            }
            JSONParser parser = new JSONParser();
            Object jsonObject = parser.parse(responseString);
            JSONObject array = (JSONObject) jsonObject;
            List<Map<String, Object>> locationMetrics = (List<Map<String, Object>>) array.get("locationMetrics");
            Map<String, Object> data = new HashMap<>();
            for (Map<String, Object> location : locationMetrics) {
                List<Map<String, Object>> MatricValues = (List<Map<String, Object>>) location.get("metricValues");
                if (data.isEmpty()) {
                    for (Map<String, Object> metrics : MatricValues) {
                        String metricName = (String) metrics.get("metric");
                        if (metricName != null) {
                            Map<String, Object> value = (Map<String, Object>) metrics.get("totalValue");
                            String metricValue = (String) value.get("value");
                            data.put(metricName, metricValue);
                        }
                    }
                } else {
                    for (Map<String, Object> metrics : MatricValues) {
                        String metricName = (String) metrics.get("metric");
                        if (metricName != null) {
                            Map<String, Object> value = (Map<String, Object>) metrics.get("totalValue");
                            String metricValue = (String) value.get("value");
                            data.put(metricName, Integer.parseInt((String) data.get(metricName)) + Integer.parseInt(metricValue));
                        }
                    }
                }
            }
            List<Map<String, Object>> returnDataMap = new ArrayList<>();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Map<String, Object> dataMap = new HashMap<>();
                String key = entry.getKey();
                Object value = entry.getValue();
                if ("QUERIES_DIRECT".equals(key)) {
                    key = "Direct";
                } else if ("QUERIES_INDIRECT".equals(key)) {
                    key = "Discovery";
                }
                dataMap.put("searchType", key);
                dataMap.put("searchValue", value);
                returnDataMap.add(dataMap);
            }
            return returnDataMap;
        } catch (Exception ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public String getAppendedResponse(String requestBody, String url) {
        List<String> locations = new ArrayList<>();
        StringBuilder responseString = new StringBuilder();
        if (locationListArray.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, String> entry : locationListArray.entrySet()) {
            String key = entry.getKey();
            locations.add(key);
        }
        int listSize = locations.size();
        for (int i = 0; i < listSize;) {
            StringBuilder locationString = new StringBuilder();
            if (listSize < 10) {
                locationString.append(locations.toString());
            } else {
                List<String> limitedLocations = new ArrayList<>();
                limitedLocations = locations.subList(i, i + 9);
                locationString.append(limitedLocations.toString());
            }
            String payload = "{\"locationNames\":" + locationString.toString() + requestBody;
            String result = getResponse(url, null, payload);
            if (result == null) {
                return null;
            }
            if (!responseString.toString().equals("")) {
                result = result.substring(result.indexOf("[") + 1, result.lastIndexOf("]") - 1);
                responseString.insert(responseString.indexOf("[") + 1, result);
            } else {
                responseString.append(result);
            }
            i = i + 9;
        }
        return responseString.toString();
    }

    public String getResponse(String url, String authorizationHeaders, String params) {
        StringEntity postEntity = new StringEntity(params,
                ContentType.APPLICATION_JSON);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            post.setEntity(postEntity);
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            String responseJSON = EntityUtils.toString(response.getEntity());
            EntityUtils.consume(response.getEntity());
            return responseJSON;
        } catch (IOException | org.apache.http.ParseException ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
